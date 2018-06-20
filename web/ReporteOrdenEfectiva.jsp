<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<%@ include file="validausuario.jsp"%>
<%@ page import="com.are.sofatec.*"%>
<%@ page import="java.sql.*"%>
<%    db conexion = new db();

    ManejadorUsuarios mu = new ManejadorUsuarios(conexion);
    mu.find((String) session.getAttribute("usuario"));
    Usuarios usuario = mu.getUsuario();
    /*
        if (usuario.getResolver() != 1) {
                response.sendRedirect("EstadoLotes.jsp");
                conexion.Close();
                return;
        }
     */
    String orden = (String) request.getParameter("orden");

    boolean rsIsEmpty = true;
    String sql = "SELECT QO_ORDENES.*, QO_DATOSUM.*, QO_APARATOS.NUM_APA,ESTADOS.ESTADESC,QO_ANOM.DESC_COD,RECURSO.RECUNOMB "
            + " FROM QO_ORDENES, QO_DATOSUM, QO_APARATOS,ESTADOS,QO_ANOM,RECURSO "
            + " WHERE QO_ORDENES.NIC = QO_DATOSUM.NIC"
            + " AND QO_ORDENES.NIS_RAD = QO_DATOSUM.NIS_RAD"
            + " AND QO_ORDENES.ESTADO_OPER = ESTADOS.ESTACODI"
            + " AND QO_ORDENES.ANOMALIA = QO_ANOM.COD"
            + " AND QO_ORDENES.TECNICO = RECURSO.RECUCODI"
            + " AND QO_ORDENES.NUM_OS = ?";

    PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
    pst.setString(1, orden);
    ResultSet rs = conexion.Query(pst);
    rsIsEmpty = !rs.next();
    ResultSet rsAparatos = null;
    boolean rsAparatosIsEmpty = true;
    String tip_os = "-1";
    if (!rsIsEmpty) {
        tip_os = rs.getString("TIP_OS");
        sql = "SELECT QO_APARATOS.*, QO_CODIGOS.DESC_COD FROM QO_APARATOS,QO_CODIGOS WHERE QO_APARATOS.CO_MARCA = QO_CODIGOS.COD AND  QO_APARATOS.NUM_OS=? ";
        pst = conexion.getConnection().prepareStatement(sql);
        pst.setString(1, orden);
        rsAparatos = conexion.Query(pst);
        rsAparatosIsEmpty = !rsAparatos.next();
    }
    int fila = 0;
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>DETALLE ORDEN DE SERVICIO</title>
        <link rel="stylesheet" href="themes/redmond/jquery.ui.all.css">
        <LINK REL="stylesheet" TYPE="text/css" HREF="main.css">
        <script src="js/function_reporte_orden_efectiva.js" language="JavaScript"></script>
        <script src="js/jquery.js" language="JavaScript"></script>
        <script src="ui/jquery.ui.core.js" language="JavaScript"></script>
        <script src="ui/jquery.ui.widget.js" language="JavaScript"></script>
        <script src="ui/jquery.ui.button.js"></script>
        <script src="ui/jquery.ui.tabs.js"></script>
        <script src="ui/jquery.ui.dialog.js"></script>
        <script src="ui/jquery.ui.position.js"></script>
        <script src="ui/jquery.effects.core.js"></script>
        <script src="ui/jquery.effects.slide.js"></script>
        <script src="ui/jquery.ui.datepicker.js"></script>
        <script src="ui/jquery.effects.explode.js"></script>
        <script src="ui/jquery.effects.fold.js"></script>
        <script src="ui/jquery.effects.slide.js"></script>
        <script type="text/javascript">
            $(function () {
                $(".boton").button();
            });

            function Accion(orden, paso, tipo, parent) {
                url = "SeleccionarAccion.jsp?orden=" + orden + "&paso=" + paso + "&tipo=" + tipo + "&parent=" + parent;
                window.open(url, "SeleccionarAccion", "width=600,height=500,scrollbars=YES,menubar=No,toolbar=NO,status=YES");
            }

            function AccionNuevoPasos(orden, paso, tipo, flujo) {
                url = "SeleccionarAccionNuevoPaso.jsp?orden=" + orden + "&paso=" + paso + "&tipo=" + tipo + "&flujo=" + flujo;
                window.open(url, "SeleccionarAccionNuevoPaso", "width=600,height=500,scrollbars=YES,menubar=No,toolbar=NO,status=YES");
            }

            function Materiales(orden, accion, tipo) {
                url = "SeleccionarMaterial.jsp?orden=" + orden + "&accion=" + accion + "&tipo=" + tipo;
                window.open(url, "SeleccionarMaterial", "width=600,height=200,scrollbars=YES,menubar=No,toolbar=NO,status=YES");
            }
            function AgregarMedidor(orden) {
                url = "AgregarMedidor.jsp?orden=" + orden;
                window.open(url, "AgregarMedidor", "width=600,height=500,scrollbars=YES,menubar=No,toolbar=NO,status=YES");
            }
            function ModificarObservacion(id, tipo, torden) {
                url = "ModificarObservacion.jsp?id=" + id + "&tipo=" + tipo + "&torden=" + torden;
                window.open(url, "ModificarObservacion", "width=600,height=300,scrollbars=YES,menubar=No,toolbar=NO,status=YES");
            }

            function ventanaCenso(orden, tipo) {
                url = "censo.jsp?orden=" + orden + "&tipo=" + tipo;
                window.open(url, "Censo", "width=800,height=600,scrollbars=YES,menubar=No,toolbar=NO,status=YES");
            }

            function Efectiva(orden, lote) {
                if (confirm("Está seguro de reportar efectiva la orden de servicio " + orden + "?")) {
                    var url = "SrvReporteEfectiva";
                    var fecha = $("#fecha_cierre").val();
                    $.post(url, {
                        orden: orden,
                        fecha: fecha
                    }, function (data) {

                        if (data.trim() == "OK") {
                            alert("Orden reportada Efectiva");
                            document.location.href = "asignadas.jsp?num_lote=" + lote;
                        } else {
                            alert("Error." + data);
                        }

                    });


                }


            }
            function plantilla(orden, tipo) {
                if (confirm("Esta seguro de cargar la plantilla?")) {
                    var url = "SrvPlantillaResolver";
                    $.post(url, {
                        orden: orden,
                        tipo: tipo
                    }, function (data) {
                        if (data.trim() == "OK") {
                            $("#info").html("<img src='images/alerta.gif'> Plantilla cargada correctamente.");
                            ListaLectura(orden);
                            ListaPasos(orden, tipo);
                            ListaNuevosPasos(orden, tipo);

                        } else {
                            $("#info").html("<img src='images/alerta.gif'>" + data);
                        }
                    });

                }



            }

        </script>
        <script type="text/javascript">

        </script>
    </head>
    <body onload="javascript:ListaSellos('<%=(String) request.getParameter("orden")%>');
            ListaPasos('<%=(String) request.getParameter("orden")%>', '<%= tip_os%>');
            ListaNuevosPasos('<%=(String) request.getParameter("orden")%>', '<%= tip_os%>');
            ListaMaterialInstalado('<%=(String) request.getParameter("orden")%>');ListaMaterialRetirado('<%=(String) request.getParameter("orden")%>');ListaMedidorInstalado('<%=(String) request.getParameter("orden")%>');ListaLectura('<%=(String) request.getParameter("orden")%>');"">
        <%@ include file="header.jsp"%>
        <div class="contencenter demo">
            <div class="orden">ORDEN DE SERVICIO <%=(String) request.getParameter("orden")%></div>
            <% if (rs.getInt("ESTADO_OPER") != 3 && usuario.getResolver() == 1) {%>
            <button class="boton" name="cmd_plantilla" id="cmd_plantilla" type="button" onclick="javascript:plantilla('<%= rs.getString("NUM_OS")%>', '<%= rs.getString("TIP_OS")%>')">Cargar Plantilla</button>
            <button class="boton" name="cmd_plantilla" id="cmd_plantilla" type="button" onclick="javascript:ventanaCenso('<%= rs.getString("NUM_OS")%>', '<%= rs.getString("TIP_OS")%>')">Censo</button>
            <% }  %>
            <div id="info"></div>
            <% if (!rsIsEmpty) {%>
            <table>
                <tr>
                    <th colspan="4">INFORMACION ORDEN DE SERVICIO</th>
                </tr>
                <tr>
                    <td>NUMERO ORDEN</td>
                    <td><%=(String) rs.getString("NUM_OS")%></td>
                    <td>LOTE</td>
                    <td><%=(String) rs.getString("NUM_LOTE")%></td>
                </tr>
                <tr>
                    <td>NIC</td>
                    <td><%=(String) rs.getString("NIC")%></td>
                    <td>NIS RAD</td>
                    <td><%=(String) rs.getString("NIS_RAD")%></td>
                </tr>
                <tr>
                    <td>TIPO ORDEN</td>
                    <td><%=(String) rs.getString("TIP_OS")%> <%=(String) rs.getString("DESC_TIPO_ORDEN")%></td>
                    <td>PRIORIDAD</td>
                    <td><%=(String) rs.getString("DESC_COD_PRIORIDAD")%></td>
                </tr>
                <tr>
                    <td>COMENTARIO OS</td>
                    <td><%=(String) rs.getString("COMENT_OS")%></td>
                    <td>COMENTATIO OS 2</td>
                    <td><%=(String) rs.getString("COMENT_OS2")%></td>
                </tr>
                <tr>
                    <td>DEPARTAMENTO</td>
                    <td><%=(String) rs.getString("DEPARTAMENTO")%></td>
                    <td>MUNICIPIO</td>
                    <td><%=(String) rs.getString("MUNICIPIO")%></td>
                </tr>
                <tr>
                    <td>LOCALIDAD</td>
                    <td><%=(String) rs.getString("LOCALIDAD")%></td>
                    <td>FINCA</td>
                    <td><%=(String) rs.getString("ACC_FINCA")%></td>
                </tr>
                <tr>
                    <td>DIRECCION</td>
                    <td><%=(String) rs.getString("DIRECCION")%></td>
                    <td>DIRECCION REF</td>
                    <td><%=(String) rs.getString("REF_DIR")%></td>
                </tr>
                <tr>
                    <td>ESTADO DE LA ORDEN</td>
                    <td style="font-size: 14px"><strong><%=(String) rs.getString("ESTADESC")%></strong></td>
                    <td></td>
                    <td></td>
                </tr>


            </table>
            <h2>Informacion Medidor</h2>
            <%
                if (!rsAparatosIsEmpty) {
            %>
            <table>
                <tr>
                    <th colspan="4">Medidor</th>
                </tr>
                <tr>
                    <td>MEDIDOR</td>
                    <td><%=(String) rsAparatos.getString("NUM_APA")%></td>
                    <td>MARCA</td>
                    <td><%=(String) rsAparatos.getString("DESC_COD")%></td>
                </tr>
                <tr>
                    <td>FECHA INSTALACION</td>
                    <td><%=(String) rsAparatos.getString("F_INST")%></td>
                    <td>FECHA FABRICACION</td>
                    <td><%=(String) rsAparatos.getString("F_FABRIC")%></td>
                </tr>
            </table>


            <%
            } else {
            %>
            No Hay informacion de medidor.
            <%
                }
            %>


            <%
                if (rs.getInt("ESTADO_OPER") != 3) {
            %>
            <H2>Informacion de Cierre</H2>


            <%
                if (!rsAparatosIsEmpty) {
            %>
            <% if (usuario.getResolver() == 1) {%>
            <h2>Lectura</h2>

            <div id="info_lectura"></div>
            <form action="" name="form3">
                Medidor: <strong><%=(String) rsAparatos.getString("NUM_APA")%></strong>
                <br /> Lectura: <input type="text" name="lectura" id="lectura" value="0">
                <input class="boton" type="button" onclick="javascript:AgregarLectura('<%=(String) request.getParameter("orden")%>', '<%=(String) rsAparatos.getString("NUM_APA")%>')" name="cmd_lectura" id="cmd_lectura" value="Agregar" class="boton">
            </form>
            <% } %>
            <% } %>
            <div id="lista_lectura"></div>

            <div id="info_pasos"></div>
            <div id="lista_pasos">

            </div>

            <div id="info_nuevos_pasos"></div>
            <div id="lista_nuevos_pasos">

            </div>
            <h2>Precintos Reportados</h2>
            <% if (usuario.getResolver() == 1) { %>
            <% if (!rsAparatosIsEmpty) {%>
            <form name="form2" action="">
                Medidor: <strong><%=(String) rsAparatos.getString("NUM_APA")%></strong>
                <br />
                Precinto: <input type="text" name="precinto" id="precinto" value="">
                <input type="button"
                       onclick="javascript:AgregarSello('<%=(String) request.getParameter("orden")%>', '<%=(String) rsAparatos.getString("NUM_APA")%>')"
                       name="cmd_precinto" id="cmd_precinto" value="Agregar" class="boton">
            </form>
            <% } %>
            <% } %>

            <div id="info_sellos"></div>
            <div id="lista_sellos">


            </div>

            <div id="info_material_instalado"></div>
            <div id="lista_material_instalado">

            </div>

            <div id="info_material_retirado"></div>
            <div id="lista_material_retirado">

            </div>

            <div id="info_medidor_instalado"></div>
            <div id="lista_medidor_instalado">

            </div>

            <%
                }
            %>
            <% if (usuario.getResolver() == 1) { %>
            <% if (rs.getInt("ESTADO_OPER") == 1) {%>
            <h2>Informacion de Resolucion</h2>
            <div id="info_orden"></div>
            <label>Fecha Cierre (AAAA-MM-DD HH:MM:SS)</label>
            <input type="text" class="fecha" name="fecha_cierre" id="fecha_cierre" value="<%= Utilidades.getTimeServer()%>"> 
            <p>
                <button type="button" name="cmd_efectiva" id="cmd_efectiva" onclick="javascript:Efectiva('<%= (String) request.getParameter("orden")%>', '<%=(String) rs.getString("NUM_LOTE")%>')" class="boton">Reportar Efectiva</button>
            </p>
            <% } %>
            <% } %>
        </div>

        <% } else {%> 
        <label>Orden de servicio <%= (String) request.getParameter("orden")%> no encontrada.</label>
        <% } %>
    </body>
</html>

<%
    conexion.Close();
%>