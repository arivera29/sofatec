<%@page import="com.are.entidades.Camp"%>
<%@page import="com.are.manejadores.ManejadorCamp"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<%@ include file="validausuario.jsp"%>
<%    if (request.getParameter("codigo") == null) {
        response.sendRedirect("camp.jsp");
        return;
    }

    String codigo = (String) request.getParameter("codigo");
    db conexion = new db();
    ManejadorCamp manejador = new ManejadorCamp(conexion);
    Camp camp = null;
    if (manejador.Find(codigo)) {
        camp = manejador.getCampania();
    } else {
        conexion.Close();
        response.sendRedirect("Marcas.jsp");
        return;
    }

%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>EDITAR CAMPAÑA</title>
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
                var codigo = $("#codigo").val();
                var descripcion = $("#descripcion").val();
                var plan = $("#plan").val();
                var estado = $("#estado").val();

                if (codigo == "" || descripcion == "" || plan == "") {
                    alert("falta ingresar informacion");
                    return;
                }
                var cmd = document.getElementById("cmd_modificar");
                cmd.disabled = true;
                $("#info").html("<img src=\"images/loading.gif\" >Procesando solicitud");
                $.post(
                        "SrvCamp",
                        {
                            operacion: "update",
                            codigo: codigo,
                            descripcion: descripcion,
                            plan : plan,
                            estado: estado,
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
                    $("#info").html("<img src=\"images/edit.png\">Campaña modificada correctamente");
                }
            }

            function eliminar(key) {
                if (confirm("Desea eliminar la Campaña ID " + key)) {
                    var cmd = document.getElementById("cmd_eliminar");
                    cmd.disabled = true;
                    $("#info").html("<img src=\"images/loading.gif\" >Procesando solicitud");
                    $.post(
                            "SrvCamp",
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
                    alert("Campaña eliminada correctamente");
                    window.location.href = "camp.jsp";
                }

            }

            function cancelar() {
                window.location.href = "camp.jsp";
            }

        </script>
    </head>
    <body>
        <%@ include file="header.jsp" %>
        <div class="contencenter demo">
            <h2>Modificar Campaña</h2>
            <div id="info"></div>
            <form action="" name="form1">
                <table>
                    <tr>
                        <th colspan="2">Información Campaña</th>
                    </tr>
                    <tr>
                        <td>Id</td>
                        <td><input type="text" name="codigo" id="codigo" value="<%= camp.getCodigo()%>"></td>
                    </tr>
                    <tr>
                        <td>Descripcion</td>
                        <td><input type="text" name="descripcion" id="descripcion" size=40 value ="<%= camp.getDescripcion()%>"></td>
                    </tr>
                    <tr>
                        <td>Plan</td>
                        <td><input type="text" name="plan" id="plan" value="<%= camp.getPlan() %>" size=40></td>
                    </tr>
                    <tr>
                        <td>Activo</td>
                        <td>
                            <select name="estado" id="estado">
                                <option value="1" <%= (camp.getEstado() == 1) ? "selected" : ""%>>Si</option>
                                <option value="0" <%= (camp.getEstado() == 0) ? "selected" : ""%>>No</option>
                            </select>
                        </td>
                    </tr>
                </table>
                <input type="button" onclick="javascript:modificar('<%= (String) request.getParameter("codigo")%>');" value="Guardar" id="cmd_modificar" name="cmd_modificar" > <input type="button" onclick="javascript:eliminar('<%= (String) request.getParameter("codigo")%>');" value="Eliminar" id="cmd_eliminar" name="cmd_eliminar" >  <input type="button" name="cmd_cancelar" id="cmd_cancelar" value ="Cancelar" onclick="javascript:cancelar()" >
            </form>
        </div>
        <%@ include file="foot.jsp" %>
    </body>
</html>

<%
    conexion.Close();
%>