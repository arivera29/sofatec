<%@page import="com.are.entidades.Contratista"%>
<%@page import="com.are.manejadores.ManejadorContratista"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"  pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<%@ include file="validausuario.jsp"%>
<%@ page import="com.are.sofatec.*" %>
<%    if (request.getParameter("id") == null) {
        response.sendRedirect("contratistas.jsp");
        return;
    }

    String codigo = (String) request.getParameter("id");
    db conexion = new db();
    ManejadorContratista manejador = new ManejadorContratista(conexion);
    Contratista contratista = null;
    if (manejador.Find(Integer.parseInt(codigo))) {
        contratista = manejador.getContratista();
    } else {
        conexion.Close();
        response.sendRedirect("contratistas.jsp");
        return;
    }


%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>EDITAR CONTRATISTA</title>
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
            function modificar(key) {
                var nombre = $("#nombre").val();
                var estado = $("#estado").val();
                var correo = $("#correo").val();

                if (nombre == "") {
                    alert("falta ingresar informacion");
                    return;
                }
                var cmd = document.getElementById("cmd_modificar");
                cmd.disabled = true;
                $("#info").html("<img src=\"images/loading.gif\" >Procesando solicitud");
                $.post(
                        "SrvContratistas",
                        {
                            operacion: "update",
                            nombre: nombre,
                            estado: estado,
                            correo: correo,
                            key: key
                        },
                        procesar

                        );

            }
            function procesar(resultado) {
                var cmd = document.getElementById("cmd_modificar");
                cmd.disabled = false;
                if (resultado != 'OK') {
                    $("#info").html("<img src=\"warning.jpg\">" + resultado);
                } else {
                    $("#info").html("<img src=\"images/edit.png\">Contratista modificado correctamente");
                }
            }

            function eliminar(key) {
                if (confirm("Desea eliminar el contratista ID " + key)) {
                    var cmd = document.getElementById("cmd_eliminar");
                    cmd.disabled = true;
                    $("#info").html("<img src=\"images/loading.gif\" >Procesando solicitud");
                    $.post(
                            "SrvContratistas",
                            {
                                operacion: "remove",
                                key: key
                            },
                            procesarEliminar

                            );
                }

            }

            function procesarEliminar(resultado) {
                var cmd = document.getElementById("cmd_eliminar");
                cmd.disabled = false;
                if (resultado != 'OK') {
                    $("#info").html("<img src=\"warning.jpg\">" + resultado);
                } else {
                    alert("Contratista eliminado");
                    window.location.href = "contratistas.jsp";
                }

            }

            function cancelar() {
                window.location.href = "contratistas.jsp";
            }

        </script>
    </head>
    <body>
        <%@ include file="header.jsp" %>
        <div class="contencenter demo">
            <h2>EDITAR CONTRATISTA</h2>
            <div id="info"></div>
            <form action="" name="form1">
                <table>
                    <tr>
                        <th colspan="2">INFORMACION CONTRATISTA</th>
                    </tr>
                    <tr>
                        <td>ID</td>
                        <td><%= contratista.getCodigo()%></td>
                    </tr>
                    <tr>
                        <td>Nombre</td>
                        <td><input type="text" name="nombre" id="nombre" size=40 value="<%= contratista.getNombre()%>"></td>
                    </tr>
                    <tr>
                        <td>Correo electrónico</td>
                        <td><textarea name="correo" id="correo" rows="10" cols="40"><%= contratista.getCorreo() %></textarea></td>
                    </tr>
                    <tr>
                        <td>Activo</td>
                        <td>
                            <select name="estado" id="estado">
                                <option value="1" <%= (contratista.getEstado() == 1) ? "selected" : ""%>>Si</option>
                                <option value="0" <%= (contratista.getEstado() == 0) ? "selected" : ""%>>No</option>
                            </select>
                        </td>
                    </tr>	
                </table>
                <input type="button" onclick="javascript:modificar('<%= (String) request.getParameter("id")%>');" value="Modificar" id="cmd_modificar" name="cmd_modificar" > <input type="button" onclick="javascript:eliminar('<%= (String) request.getParameter("id")%>');" value="Eliminar" id="cmd_eliminar" name="cmd_eliminar" >  <input type="button" name="cmd_cancelar" id="cmd_cancelar" value ="Cancelar" onclick="javascript:cancelar()" >
            </form>
        </div>
        <%@ include file="foot.jsp" %>
    </body>
</html>

<%
    conexion.Close();
%>