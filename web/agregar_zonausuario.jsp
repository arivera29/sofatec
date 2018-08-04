<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<%@ include file="validausuario.jsp"%>
<%@ page import="com.are.sofatec.*" %>
<%@page import="com.are.entidades.Zona"%>
<%@page import="com.are.manejadores.ManejadorZonas"%>
<%@ page import="java.sql.*" %>

<%
        db conexion = new db();
        String usuario = (String)request.getParameter("usuario");
        ManejadorZonas manejador = new ManejadorZonas(conexion);
        ArrayList<Zona> lista = manejador.List();
        ArrayList<Zona> ZonasAsignadas = manejador.List(usuario);
	
%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>GRUPOS PERSONAL</title>
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
            function agregar(usuario) {
                var zona = $("#zona").val();
                if (zona == "") {
                    $("#info").html("<p><img src='images/alerta.gif'>Debe seleccionar una zona</p>");
                    return;
                }

                $.get("SrvZonas", {
                    operacion: "add_user",
                    usuario: usuario,
                    id: zona
                }, function (data) {
                    if (data.trim() == "OK") {
                        $("#info").html("<p><img src='images/alerta.gif'>Zona agregada correctamente</p>");
                        listar_zonas(usuario);
                    } else {
                        $("#info").html("<p><img src='images/alerta.gif'>Error al procesar la solicitud. " + data + "</p>");
                    }
                });

            }

            function eliminar(zona, usuario) {
                if (confirm("Esta seguro de eliminar el registro?")) {
                    $.get("SrvZonas", {
                        operacion: "remove_user",
                        id: zona,
                        usuario: usuario
                    }, function (data) {
                        if (data.trim() == "OK") {
                            $("#info").html("<p><img src='images/alerta.gif'>Registro eliminado correctamente</p>");
                            listar_zonas(usuario);
                        } else {
                            $("#info").html("<p><img src='images/alerta.gif'>Error al procesar la solicitud. " + data + "</p>");
                        }
                    });
                }
            }

            function listar_zonas(usuario) {
                var url = "SrvZonas";
                $("#lista").load(url, {
                    operacion: "list_user",
                    usuario: usuario
                }, function (data) {
                    $("input:submit, a, button", ".demo").button();
                });
            }

            function cancelar() {
                document.location.href = "adduser.jsp";
            }


        </script>
    </head>
    <body>
        <%@ include file="header.jsp" %>
        <div class="contencenter demo">
            <h2>ZONAS POR USUARIO (<%= request.getParameter("usuario") %>)</h2>
            <div id="info"></div>
            <form action="" name="form1" onsubmit="javascript: return validar();" > 
                <label  for="zona">Zona</label>
                <select id="zona" name="zona">
                    <% for (Zona zona : lista ) { %>
                    <option value="<%= zona.getId()   %>"><%= zona.getNombre() %></option>
                    <% } %>
                </select>	
                <button type="button" name="cmd_agregar" id="cmd_agregar" onclick="javascript:agregar('<%= request.getParameter("usuario") %>');">Agregar</button>
                <button type="button" name="cmd_cancelar" id="cmd_cancelar" onclick="javascript:cancelar();">Cancelar</button>
            </form>

            <h2>Zonas Asignadas</h2>
            <div id="lista">
                <% if (ZonasAsignadas.size() > 0) { %>
                <table>
                    <tr>
                        <th>ID</th>
                        <th>NOMBRE</th>
                        <th>ACCION</th>
                    </tr>
                    <% for (Zona zona : ZonasAsignadas ) { %>
                    <tr>
                        <td><%= zona.getId() %></td>
                        <td><%= zona.getNombre() %></td>
                        <td><a href="javascript:eliminar('<%= zona.getId()  %>','<%= request.getParameter("usuario") %>')">Eliminar</a></td>
                    </tr>

                    <% } %>
                </table>
                <% } else { %>
                No hay registros.
                <% } %>
            </div>
        </div>
        <%@ include file="foot.jsp" %>
    </body>
</html>