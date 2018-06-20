<%@page import="com.are.entidades.Camp"%>
<%@page import="com.are.manejadores.ManejadorCamp"%>
<%@page import="com.are.sofatec.db"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<%@ include file="validausuario.jsp"%>
<%@ page import="java.sql.*"%>
<%    db conexion = new db();
    String id = (String) request.getParameter("id");

    String sql = "SELECT ID,NIC, NUM_OS, COMENTARIO, CONTRATISTA, CONTNOMB, "
            + " BRIGADA,R1.RECUNOMB AS NOMB_BRIGADA, ID_CAMP,CAMPDESC, ZONA,"
            + " ZONANOMB, R2.RECUNOMB AS NOMB_INSPECTOR, R3.RECUNOMB AS NOMB_INGENIERO, "
            + " FECHA_CARGA, USUARIO_CARGA, CORRIENTE1,VOLTAJE1, CORRIENTE2, VOLTAJE2, "
            + " IRREG, LATITUD_APP, LONGITUD_APP, IMEI_APP"
            + " FROM camp_orden "
            + " INNER JOIN contratistas ON CONTRATISTA = CONTCODI "
            + " INNER JOIN recurso R1 ON BRIGADA = R1.RECUCODI "
            + " INNER JOIN recurso R2 ON INSPECTOR = R2.RECUCODI "
            + " INNER JOIN recurso R3 ON INGENIERO = R3.RECUCODI "
            + " INNER JOIN camp ON ID_CAMP = campcodi "
            + " INNER JOIN zonas ON ZONA = zonacodi "
            + " WHERE ID = ?";

    java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
    pst.setString(1, id);
    java.sql.ResultSet rs = conexion.Query(pst);
    if (!rs.next()) {
        conexion.Close();
        response.sendRedirect("pendiente_generar_orden.jsp");
        return;
    }

    int count = 0;

    java.sql.ResultSet rsSuministro = null;

    sql = "SELECT NIS_RAD, COD_TAR,MUNICIPIO,LOCALIDAD,"
            + "DEPARTAMENTO,DIRECCION,APE1_CLI,APE2_CLI,NOM_CLI,REF_DIR,ACC_FINCA "
            + " FROM qo_datosum "
            + " WHERE NIC=? LIMIT 1 ";

    java.sql.PreparedStatement pst1 = conexion.getConnection().prepareStatement(sql);
    pst1.setString(1, rs.getString("NIC"));
    rsSuministro = conexion.Query(pst1);

    ManejadorCamp m1 = new ManejadorCamp(conexion);
    ArrayList<Camp> lista = m1.ListByEstado(1);


%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>Actualizar Orden de servicio</title>
        <link rel="stylesheet" href="themes/redmond/jquery.ui.all.css">
        <link rel="stylesheet" TYPE="text/css" HREF="main.css">
        <script src="js/jquery.js" type="text/javascript"></script>
        <script src="ui/jquery.ui.core.js" language="JavaScript"></script>
        <script src="ui/jquery.ui.widget.js" language="JavaScript"></script>
        <script src="ui/jquery.ui.button.js"></script>
        <script>
            $(function () {
                $("input:submit, a, button", ".demo").button();
                $("input:button, a, button", ".demo").button();
            });

            function cancelar() {
                window.location.href = "pendiente_generar_orden.jsp";
            }

            function actualizar(id) {
                var orden = $("#orden").val();
                var camp = $("#camp").val();
                if (orden == "" || camp == "") {
                    alert("Faltan datos para generar la orden de servicio");
                    return;
                }

                if (orden != "") {
                    var patron = /^\d*$/;
                    var OK = orden.search(patron);
                    if (OK) {
                        alert("La orden debe contener solo numeros, favor verificar");
                        return;
                    } else {
                        if (orden.length != 8) {
                            alert("La orden debe contener solo 8 digitos, favor verificar");
                            return;
                        }
                    }
                }

                var url = "SrvActualizarOrden";
                $.get(url, {
                    id: id,
                    orden: orden,
                    camp : camp
                }, function (data) {
                    if (data.trim() == "OK") {
                        alert("Registro actualizado correctamente");
                        cancelar();
                    } else {
                        $("#info").html("<img src=\"warning.jpg\">" + data);
                    }
                });

            }

            function eliminar(id) {
                if (confirm("Esta seguro de eliminar el registro")) {
                    var url = "SrvEliminarOrden";
                    $.get(url, {
                        id: id
                    }, function (data) {
                        if (data.trim() == "OK") {
                            alert("Registro eliminado correctamente");
                            cancelar();
                        } else {
                            $("#info").html("<img src=\"warning.jpg\">" + data);
                        }
                    });


                }

            }

        </script>

    </head>
    <body>

        <%@ include file="header.jsp"%>
        <div class="contencenter demo">
            <h2>Actualizar numero de orden de servicio</h2>

            <table>
                <tr>
                    <th colspan="2">Información Registro</th>
                </tr>
                <tr>
                    <td>NIC</td>
                    <td><%= rs.getString("NIC")%></td>
                </tr>
                <tr>
                    <td>Comentario</td>
                    <td><%= rs.getString("COMENTARIO")%></td>
                </tr>
                <tr>
                    <td>Contratista</td>
                    <td><%= rs.getString("CONTNOMB")%></td>
                </tr>
                <tr>
                    <td>Inspector</td>
                    <td><img src='images/recurso.png'><%= rs.getString("NOMB_INSPECTOR")%></td>
                </tr>
                <tr>
                    <td>Ingeniero</td>
                    <td><img src='images/recurso.png'><%= rs.getString("NOMB_INGENIERO")%></td>
                </tr>
                <tr>
                    <td>Brigada</td>
                    <td><img src='images/recurso.png'><%= rs.getString("NOMB_BRIGADA")%></td>
                </tr>
                <tr>
                    <td>Campaña</td>
                    <td>
                        <select name="camp" id="camp">
                            <option value="">Seleccionar</option>
                            <% for (Camp camp : lista) {%>
                            <option value ="<%= camp.getCodigo()%>" <%= camp.getCodigo().equals(rs.getString("ID_CAMP"))?"selected":""  %>  ><%= camp.getDescripcion()%> (<%= camp.getCodigo()%>)</option>
                            <% }%>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td>Zona</td>
                    <td><%= rs.getString("ZONANOMB")%></td>
                </tr>
                 <tr>
                    <td>Corriente1/Voltaje1</td>
                    <td><%= rs.getString("CORRIENTE1")%> / <%= rs.getString("VOLTAJE1")%></td>
                </tr>
                <tr>
                    <td>Corriente2/Voltaje2</td>
                    <td><%= rs.getString("CORRIENTE2")%> / <%= rs.getString("VOLTAJE2")%></td>
                </tr>
                <tr>
                    <td>Latitud/Longitud</td>
                    <td><%= rs.getString("LATITUD_APP")%> / <%= rs.getString("LONGITUD_APP")%> <a href="mapa.jsp?lat=<%= rs.getString("LATITUD_APP")%>&lon=<%= rs.getString("LONGITUD_APP")%>" target="_blank" >Ver mapa</a></td>
                </tr>
                <tr>
                    <td>IMEI</td>
                    <td><%= rs.getString("IMEI_APP")%></td>
                </tr>
                <tr>
                    <td>Fecha Creación</td>
                    <td><img src='images/calendario.png'><%= rs.getString("FECHA_CARGA")%></td>
                </tr>
                <tr>
                    <td>Usuario creador</td>
                    <td><img src='images/recurso.png'><%= rs.getString("USUARIO_CARGA")%></td>
                </tr>
                <tr>
                    <td>Orden de Servicio</td>
                    <td><input type="text" name="orden" id="orden"></td>
                </tr>
            </table>
            <a href='javascript:actualizar(<%= rs.getInt("ID")%>)'>Actualizar</a>
            <a href='javascript:eliminar(<%= rs.getInt("ID")%>)'>Eliminar</a>
            <a href='javascript:cancelar()'>Cancelar</a>
            <div id="info"></div>
            <%  if (rsSuministro.next()) {%>
            <h2>Informacion Suministro</h2>
            <table>
                <tr>
                    <th colspan="4">SUMINISTRO</th>
                </tr>
                <tr>
                    <td>Nombre Cliente</td>
                    <td><%= rsSuministro.getString("NOM_CLI") + " " + rsSuministro.getString("APE1_CLI") + " " + rsSuministro.getString("APE2_CLI")%></td>
                    <td>Direccion</td>
                    <td><%= rsSuministro.getString("DIRECCION")%></td>
                </tr>
                <tr>
                    <td>Municipio</td>
                    <td><%= rsSuministro.getString("MUNICIPIO")%></td>
                    <td>Localidad</td>
                    <td><%= rsSuministro.getString("LOCALIDAD")%></td>
                </tr>
                <tr>
                    <td>Departamento</td>
                    <td><%= rsSuministro.getString("DEPARTAMENTO")%></td>
                    <td>NIS RAD</td>
                    <td><%= rsSuministro.getString("NIS_RAD")%></td>
                </tr>

            </table>


            <% }  %>
        </div>

    </body>
</html>
<%
    conexion.Close();
%>
