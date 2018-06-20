<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<%@ include file="validausuario.jsp"%>
<%@ page import="com.are.sofatec.*" %>
<%@ page import="com.are.manejadores.*" %>
<%@ page import="com.are.entidades.*" %>
<%@ page import="java.sql.*" %>

<%
        db conexion = new db();
        String usuario = (String)request.getParameter("usuario");
        ManejadorContratista manejador = new ManejadorContratista(conexion);
        ArrayList<Contratista> lista = manejador.List();
        ArrayList<Contratista> lista1 = manejador.ListByUsuario(usuario);
	
%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>CONTRATISTAS USUARIOS</title>
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
                var contratista = $("#contratista").val();
                if (contratista == "") {
                    $("#info").html("<p><img src='images/alerta.gif'>Debe seleccionar el contratista</p>");
                    return;
                }

                $.get("SrvContratistas", {
                    operacion: "add_user",
                    usuario: usuario,
                    key: contratista
                }, function (data) {
                    if (data.trim() == "OK") {
                        $("#info").html("<p><img src='images/alerta.gif'>Contratista agregado correctamente</p>");
                        listar_contratistas(usuario);
                    } else {
                        $("#info").html("<p><img src='images/alerta.gif'>Error al procesar la solicitud. " + data + "</p>");
                    }
                });

            }

            function eliminar(contratista, usuario) {
                if (confirm("Esta seguro de eliminar el registro?")) {
                    $.get("SrvContratistas", {
                        operacion: "remove_user",
                        key: contratista,
                        usuario: usuario
                    }, function (data) {
                        if (data.trim() == "OK") {
                            $("#info").html("<p><img src='images/alerta.gif'>Registro eliminado correctamente</p>");
                            listar_contratistas(usuario);
                        } else {
                            $("#info").html("<p><img src='images/alerta.gif'>Error al procesar la solicitud. " + data + "</p>");
                        }
                    });
                }
            }

            function listar_contratistas(usuario) {
                var url = "SrvContratistas";
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
            <h2>CONTRATISTAS POR USUARIO (<%= request.getParameter("usuario") %>)</h2>
            <div id="info"></div>
            <form action="" name="form1" onsubmit="javascript: return validar();" > 
                <label  for="zona">Zona</label>
                <select id="contratista" name="contratista">
                    <% for (Contratista contratista : lista ) { %>
                    <option value="<%= contratista.getCodigo()   %>"><%= contratista.getNombre() %></option>
                    <% } %>
                </select>	
                <button type="button" name="cmd_agregar" id="cmd_agregar" onclick="javascript:agregar('<%= request.getParameter("usuario") %>');">Agregar</button>
                <button type="button" name="cmd_cancelar" id="cmd_cancelar" onclick="javascript:cancelar();">Cancelar</button>
            </form>

            <h2>Contratistas asignados</h2>
            <div id="lista">
                <% if (lista1.size() > 0) { %>
                <table>
                    <tr>
                        <th>CODIGO</th>
                        <th>NOMBRE</th>
                        <th>ACCION</th>
                    </tr>
                    <% for (Contratista contratista : lista1 ) { %>
                    <tr>
                        <td><%= contratista.getCodigo() %></td>
                        <td><%= contratista.getNombre() %></td>
                        <td><a href="javascript:eliminar('<%= contratista.getCodigo()  %>','<%= request.getParameter("usuario") %>')">Eliminar</a></td>
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