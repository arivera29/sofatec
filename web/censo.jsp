<%@page import="java.util.ArrayList"%>
<%@page import="com.are.manejadores.GeneradorPlantilla"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="validausuario.jsp"%>
<%@ page import="com.are.sofatec.*" %>
<%@ page import="java.sql.*" %>
<%    db conexion = new db();
    ResultSet rs = null;
    ResultSet rsOrden = null;
    boolean rsIsEmpty = true;
    boolean rsOrdenIsEmpty = true;

    String mensaje = "";

    if (request.getParameter("cmd_guardar") != null) {  // Guardar
        String[] equipos = request.getParameterValues("equipo");
        String[] cantidad = request.getParameterValues("cantidad");

        String acta = request.getParameter("acta");
        String fecha = request.getParameter("fecha");
        String tecnico = request.getParameter("tecnico");
        String ct = request.getParameter("ct");
        String mt = request.getParameter("mt");
        String tarifa = request.getParameter("tarifa");
        String ciuu = request.getParameter("ciuu");

        int cont = 0;
        int cntx = 0;

        String SQL = "UPDATE QO_ORDENES SET NUM_ACTA=?, NOMBRE_OPERARIO_HDA=?, FECHA_CENSO=?, NUM_CT=?, NUM_MT=?, TARIFA=?, CIUU=? WHERE NUM_OS=?";
        java.sql.PreparedStatement pst3 = conexion.getConnection().prepareStatement(SQL);
        pst3.setString(1, acta);
        pst3.setString(2, tecnico);
        pst3.setString(3, fecha);
        pst3.setString(4, ct);
        pst3.setString(5, mt);
        pst3.setString(6, tarifa);
        pst3.setString(7, ciuu);
        pst3.setString(8, (String) request.getParameter("orden"));

        conexion.Update(pst3);  // Actualizando Orden de Servicio

        conexion.Update("DELETE FROM QO_CENSO WHERE NUM_OS='" + (String) request.getParameter("orden") + "'");
        for (int x = 0; x < equipos.length; x++) {
            if (Integer.parseInt(cantidad[x]) > 0) {
                cont++;
                String sql = "INSERT INTO QO_CENSO (NUM_OS,ID_EQUIPO,CARGA,CANTIDAD) VALUES (?,?,?,?)";
                java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
                pst.setString(1, (String) request.getParameter("orden"));
                pst.setString(2, equipos[x]);
                pst.setString(3, "0");
                pst.setString(4, cantidad[x]);

                if (conexion.Update(pst) > 0) {
                    cntx++;
                }
            }

        }

        if (cont == cntx) {
            conexion.Commit();
            mensaje = "Censo guardado correctamente";

            GeneradorPlantilla controlador = new GeneradorPlantilla(conexion);
            controlador.GenerarPlantilla((String) request.getParameter("orden"), (String) request.getParameter("tipo"));
//            String obs = controlador.obtenerObservacion(request.getParameter("orden"));
//            ArrayList<String> lista = controlador.dividirObservacion(obs, 200);
//           
//            mensaje += obs;
//            mensaje += "<br/>";
//             mensaje += "Size Lista: " + lista.size() + "<br/>";
//            for (int x=0; x < lista.size(); x++) {
//                mensaje += "Lista " + x + "=" + lista.get(x);
//                mensaje += "<br/>";
//            }

        } else {
            conexion.Rollback();
            mensaje = "<strong>Error </strong>al guardar el censo, verifique por favor";
        }

    }

    if (request.getParameter("orden") != null) {
        String orden = (String) request.getParameter("orden");

        String sql = "SELECT NUM_ACTA, NOMBRE_OPERARIO_HDA, FECHA_CENSO, NUM_CT, NUM_MT,CIUU,TARIFA FROM QO_ORDENES WHERE NUM_OS=?";
        java.sql.PreparedStatement pst4 = conexion.getConnection().prepareStatement(sql);
        pst4.setString(1, orden);
        rsOrden = conexion.Query(pst4);
        rsOrdenIsEmpty = !rsOrden.next();
    }

    java.sql.ResultSet rsTarifas = conexion.Query("SELECT COD_TAR, DESC_TAR FROM QO_TARIFAS ORDER BY DESC_TAR");
    java.sql.ResultSet rsCIUU = conexion.Query("SELECT COD_CIUU, DESC_CIUU FROM QO_CIUU ORDER BY DESC_CIUU");
    java.sql.ResultSet rsCategorias = conexion.Query("SELECT * FROM QO_CATEGORIAS WHERE CATEG_ID != 1 ORDER BY CATEG_DESC");


%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>Censo de Carga</title>
        <link rel="stylesheet" href="themes/redmond/jquery.ui.all.css">
        <LINK REL="stylesheet" TYPE="text/css" HREF="main.css">
        <script src="js/jquery.js" language="JavaScript"></script>
        <script src="ui/jquery.ui.core.js"></script>
        <script src="ui/jquery.ui.widget.js"></script>
        <script src="ui/jquery.ui.button.js"></script>
        <script src="ui/jquery.effects.core.js"></script>
        <script src="ui/jquery.effects.slide.js"></script>
        <script src="ui/jquery.ui.datepicker.js"></script>
        <script src="ui/jquery.effects.explode.js"></script>
        <script src="ui/jquery.effects.fold.js"></script>
        <script src="ui/jquery.effects.slide.js"></script>
        <script src="ui/jquery.ui.tabs.js"></script>
        <script type="text/javascript">
            $(function () {
                $(".boton").button();
                $(".fecha").datepicker({
                    showOn: "button",
                    buttonImage: "images/calendar.gif",
                    buttonImageOnly: true,
                    dateFormat: "yy-mm-dd"
                });
                $(".fecha").datepicker("option", "showAnim", "slide");
                $("#tabs").tabs();
            });

        </script>
        <script type="text/javascript">
            function seleccionar(cedula, nombre) {
                window.opener.document.form1.cedula.value = cedula;
                window.opener.document.form1.nombre.value = nombre;
                window.close();
            }

            function calcularTotal() {
                var total = 0;
                $('input[name = "cantidad" ]').each(function () {

                    total = total + (parseFloat($(this).val())) * (parseFloat($(this).attr("carga")));

                });
                $("#total").html(total);
            }

            function validar() {
                var tarifa = $("#tarifa").val();
                if (tarifa == "") {
                    alert("Debe seleccionar la tarifa");
                    return false;
                }

                var ciuu = $("#ciuu").val();
                if (ciuu == "") {
                    alert("Debe seleccionar el uso general");
                    return false;
                }


                return true;
            }

        </script>
    </head>
    <body>
        <h3>Censo de Carga Orden No. <%= (String) request.getParameter("orden")%></h3>
        <%  if (!mensaje.equals("")) {%>
        <p>
            <%= mensaje%>
            <script>
                window.opener.document.location.reload();
            </script>
        </p>

        <% }  %>
        <% if (!rsOrdenIsEmpty) {%>
        <form name="" id="" action="" method="get" onsubmit="javascript: return validar();">
            <input type="hidden" name="orden" id="orden" value="<%= (String) request.getParameter("orden")%>">
            <input type="hidden" name="tipo" id="tipo" value="<%= (String) request.getParameter("tipo")%>">

            <table>
                <tr>
                    <th colspan="2">Datos</th>
                </tr>
                <tr>
                    <td><strong>No. de Acta</strong></td>
                    <td><input type="text" name="acta" id="acta" value="<%= (rsOrdenIsEmpty) ? "" : rsOrden.getString("NUM_ACTA")%>" required></td>
                </tr>
                <tr>
                    <td><strong>Fecha Acta</strong></td>
                    <td><input type="text" class="fecha" name="fecha" id="fecha" value="<%= (rsOrdenIsEmpty) ? "" : rsOrden.getString("FECHA_CENSO")%>" required readonly ></td>
                </tr>
                <tr>
                    <td><strong>Tarifa</strong></td>
                    <td>
                        <select name="tarifa" id="tarifa" >
                            <option value="">Seleccionar</option>
                            <% while (rsTarifas.next()) {%>
                            <option value="<%= rsTarifas.getString("COD_TAR")%>" <%= rsTarifas.getString("COD_TAR").equals(rsOrden.getString("TARIFA"))?"selected":""  %>><%= rsTarifas.getString("DESC_TAR")%></option>

                            <% }  %>

                        </select>
                    </td>
                </tr>
                <tr>
                    <td><strong>Uso General</strong></td>
                    <td>
                        <select name="ciuu" id="ciuu" >
                            <option value="">Seleccionar</option>
                            <% while (rsCIUU.next()) {%>
                            <option value="<%= rsCIUU.getString("COD_CIUU")%>" <%= rsCIUU.getString("COD_CIUU").equals(rsOrden.getString("CIUU"))?"selected":""  %> ><%= rsCIUU.getString("DESC_CIUU")%></option>

                            <% }%>

                        </select>
                    </td>
                </tr>
                <tr>
                    <td><strong>Tecnico</strong></td>
                    <td><input type="text" name="tecnico" id="tecnico" value="<%= (rsOrdenIsEmpty) ? "" : rsOrden.getString("NOMBRE_OPERARIO_HDA")%>" required></td>
                </tr>
                <tr>
                    <td><strong>CT</strong></td>
                    <td><input type="text" name="ct" id="ct" value="<%= (rsOrdenIsEmpty) ? "" : rsOrden.getString("NUM_CT")%>" required></td>
                </tr>
                <tr>
                    <td><strong>MT</strong></td>
                    <td><input type="text" name="mt" id="mt" value="<%= (rsOrdenIsEmpty) ? "" : rsOrden.getString("NUM_MT")%>" required></td>
                </tr>
            </table>

            <div id="tabs">
                <ul>
                    <% while (rsCategorias.next()) {%>
                    <li><a href="#tabs-<%= rsCategorias.getInt("CATEG_ID")%>"><%= rsCategorias.getString("CATEG_DESC")%></a></li>
                        <% }  %>

                    <%
                        rsCategorias.beforeFirst();
                        while (rsCategorias.next()) {

                    %>
                    <div id="tabs-<%= rsCategorias.getInt("CATEG_ID")%>">
                        <%
                            String orden = (String) request.getParameter("orden");
                            String sql = "SELECT qo_equipos.ID,"
                                    + " DESC_EQ, qo_equipos.CARGA C1, "
                                    + " DESC_AV, CANTIDAD, qo_censo.CARGA C2 "
                                    + " FROM sofatec.qo_equipos "
                                    + " LEFT JOIN qo_censo ON qo_censo.ID_EQUIPO = qo_equipos.ID and qo_censo.NUM_OS = ? "
                                    + " WHERE qo_equipos.CATEG = ?";
                            java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
                            pst.setString(1, orden);
                            pst.setInt(2, rsCategorias.getInt("CATEG_ID"));
                            rs = conexion.Query(pst);
                            rsIsEmpty = !rs.next();

                        %>
                        <br>
                        <table>
                            <!--
                            <tr>
                                <th>Id</th>
                                <th>Equipo</th>
                                <th>Carga</th>
                                <th>Cantidad</th>
                            </tr>
                            -->
                            <% do {%>
                            <tr>
<!--                                <td><%=rs.getString("ID")%></td>-->
                                <td><%=rs.getString("DESC_EQ")%></td>
                                <td><%=rs.getString("C1")%></td>
                                <td>
                                    <input type="hidden" name="equipo" value="<%=rs.getString("ID")%>">
                                    <input type="number" carga="<%=rs.getDouble("C1")%>" name="cantidad" id="" value="<%= rs.getInt("cantidad")%>" onblur="javascript:calcularTotal()">
                                </td>
                            </tr>

                            <% } while (rs.next()); %>
                        </table>

                    </div>

                    <%                        }
                    %>

                </ul>
            </div>
            <p>
                <label>Total Censo</label>
                <span style="font-size: 20px; font-weight: bold" id="total">0</span>
            </p>


            <input type="submit" class="boton" name="cmd_guardar" id="cmd_guardar" value="Guardar Censo">
            <input type="button" class="boton" name="cmd_total" id="cmd_total" onclick="javascript:calcularTotal()" value="Total Censo">
        </form>
        <% } %>

        <%@ include file="foot.jsp" %>
    </body>
    <script>
        calcularTotal();
    </script>
</html>

<%
    if (rs != null) {
        rs.close();
    }
    if (conexion != null) {
        conexion.Close();
    }
%>