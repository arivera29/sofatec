<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="validausuario.jsp"%>
<%@ page import="com.are.sofatec.*" %>
<%@ page import="java.sql.*" %>
<%
        db conexion = new db();
	
        String codigo = (String)request.getParameter("codigo");
        TipoOrden tipo = new TipoOrden(conexion);
        if (!tipo.Find(codigo)) {
                response.sendRedirect("TipoOrden.jsp");
                return;
        }
	
        Actividades actividades = new Actividades(conexion);
        ResultSet rs = actividades.List();
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>Actividades por Tipo de Orden</title>
        <link rel="stylesheet" href="themes/redmond/jquery.ui.all.css">
        <LINK REL="stylesheet" TYPE="text/css" HREF="main.css">
        <script src="js/jquery.js" language="JavaScript"></script>
        <script src="ui/jquery.ui.core.js" language="JavaScript"></script>
        <script src="ui/jquery.ui.widget.js" language="JavaScript"></script>
        <script src="ui/jquery.ui.button.js"></script>

        <script type="text/javascript">
            $(function () {
                $("input:submit, a, button", ".demo").button();
                $("input:button, a, button", ".demo").button();
            });
        </script>
        <script type="text/javascript" language="javascript">
            function agregar(tipo) {
                var actividad = $("#actividad").val();
                var accion = $("#accion").val();
                if (actividad == "" || accion == "") {
                    alert("Debe seleccionar la actividad");
                    return;
                }

                var cmd = document.getElementById("cmd_agregar");
                cmd.disabled = true;
                $("#info").html("<img src=\"images/loading.gif\" >Procesando solicitud");
                $.post(
                        "SrvTipoOrden",
                        {
                            operacion: "add_actividad",
                            tipo: tipo,
                            actividad: actividad,
                            accion: accion
                        },
                        procesar

                        );
            }
            function procesar(resultado) {
                var cmd = document.getElementById("cmd_agregar");
                cmd.disabled = false;
                if (resultado != 'OK') {
                    $("#info").html("<img src=\"warning.jpg\">" + resultado);
                } else {
                    $("#info").html("<img src=\"images/ok.png\">Actividad Agregada correctamente" + resultado);
                    lista('<%= (String)request.getParameter("codigo") %>');
                }
            }

            function Eliminar(actividad, tipo) {

                if (confirm("Esta seguro de eliminar la actividad " + actividad)) {
                    var cmd = document.getElementById("cmd_agregar");
                    cmd.disabled = true;
                    $("#info").html("<img src=\"images/loading.gif\" >Procesando solicitud");
                    $.post(
                            "SrvTipoOrden",
                            {
                                operacion: "remove_actividad",
                                tipo: tipo,
                                actividad: actividad
                            },
                            procesarEliminar

                            );
                }
            }
            function procesarEliminar(resultado) {
                var cmd = document.getElementById("cmd_agregar");
                cmd.disabled = false;
                if (resultado != 'OK') {
                    $("#info").html("<img src=\"warning.jpg\">" + resultado);
                } else {
                    $("#info").html("<img src=\"images/ok.png\">Actividad eliminada correctamente" + resultado);
                    lista('<%= (String)request.getParameter("codigo") %>');
                }
            }

            function lista(tipo) {
                $("#list").load("SrvTipoOrden?operacion=lista_actividades&tipo=" + tipo, function () {
                    $("input:button, a, button", ".demo").button();
                });
            }

        </script>
    </head>
    <body onload="javascript:lista('<%= (String)request.getParameter("codigo")  %>')">
        <%@ include file="header.jsp" %>
        <div class="contencenter demo">
            <h2>ACTIVIDADES POR TIPO DE ORDEN</h2>
            <div id="info"></div>
            <form action="" name="form1">
                <table id="lista" border=0>
                    <tr>
                        <th colspan="2">AGREGAR ACTIVIDAD</th>
                    </tr>
                    <tr>
                        <td>Tipo de Orden</td>
                        <td><%= tipo.getCodigo() %> <%= tipo.getDescripcion() %></td>
                    </tr>
                    <tr>
                        <td>Actividad</td>
                        <td>
                            <select name="actividad" id="actividad">
                                <% while (rs.next()) { %>
                                <option value ="<%= rs.getString("acticodi") %>"><%= rs.getString("acticodi") %> - <%=rs.getString("actidesc") %></option>
                                <% } %>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td>Accion</td>
                        <td>
                            <select name="accion" id="accion">
                                <option value="R">Resolver</option>
                                <option value="A">Anular</option>
                            </select>
                        </td>
                    </tr>	
                </table>
                <input type="button" name="cmd_agregar" id="cmd_agregar" value="Agregar" onclick="javascript:agregar('<%= (String)request.getParameter("codigo") %>')" >
            </form>
            <div id="list"></div>
        </div>
        <%@ include file="foot.jsp" %>
    </body>
</html>
<%
        if (conexion != null) {
                conexion.Close();
        }
%>