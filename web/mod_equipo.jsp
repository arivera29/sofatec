<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="validausuario.jsp"%>
<%@ page import="com.are.sofatec.*" %>
<%    if (request.getParameter("codigo") == null) {
        response.sendRedirect("Equipos.jsp");
        return;
    }

    String codigo = (String) request.getParameter("codigo");
    db conexion = new db();
    Marcas marca = new Marcas(conexion);
    Equipos equipo = new Equipos(conexion);
    departamento dpto = new departamento(conexion);
    equipo.Find(codigo);

%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>MODIFICAR SMARTPHONE</title>
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
                var marca = $("#marca").val();
                var imei = $("#imei").val();
                var estado = $("#estado").val();
                var tipo = $("#tipo").val();
                var departamento = $("#departamento").val();
                var foto = $("#foto").val();

                if (departamento == "" || marca == "" || imei == "" || foto == "") {
                    alert("falta ingresar informacion");
                    return;
                }
                var cmd = document.getElementById("cmd_modificar");
                cmd.disabled = true;
                $("#info").html("<img src=\"images/loading.gif\" >Procesando solicitud");
                $.post(
                        "SrvEquipos",
                        {
                            operacion: "modify",
                            imei: imei,
                            marca: marca,
                            estado: estado,
                            tipo: tipo,
                            departamento: departamento,
                            foto: foto,
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
                    $("#info").html("<img src=\"images/edit.png\">Equipo modificado correctamente");
                }
            }

            function eliminar(key) {
                if (confirm("Desea eliminar el Equipo " + key)) {
                    var cmd = document.getElementById("cmd_eliminar");
                    cmd.disabled = true;
                    $("#info").html("<img src=\"images/loading.gif\" >Procesando solicitud");
                    $.post(
                            "SrvEquipos",
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
                    alert("Marca eliminada");
                    window.location.href = "Equipos.jsp";
                }

            }

            function cancelar() {
                window.location.href = "Equipos.jsp";
            }

        </script>
    </head>
    <body>
        <%@ include file="header.jsp" %>
        <div class="contencenter demo">
            <h2>MODIFICAR SMARTPHONE</h2>
            <div id="info"></div>
            <form action="" name="form1">
                <table>
                    <tr>
                        <th colspan="2">INFORMACION SMARTPHONE</th>
                    </tr>
                    <tr>
                        <td>Departamento</td>
                        <td><%= dpto.CreateSelectHTML("departamento", equipo.getDepartamento())%></td>
                    </tr>
                    <tr>
                        <td>Marca</td>
                        <td><%= marca.CreateSelectHTML("marca", equipo.getMarca())%></td>
                    </tr>
                    <tr>
                        <td>IMEI</td>
                        <td><input type="text" name="imei" id="imei" size=40 value="<%= equipo.getImei()%>"></td>
                    </tr>
                    <tr>
                        <td>Visualizacion Ordenes</td>
                        <td>
                            <select name="tipo" id="tipo">
                                <option value="0" <%= (equipo.getTipo() == 0) ? "selected" : ""%>>Todas las ordenes</option>
                                <option value="1" <%= (equipo.getTipo() == 1) ? "selected" : ""%>>Por orden de visualizacion</option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td>Restringir Envio Foto?</td>
                        <td>
                            <select name="foto" id="foto">
                                <option value="1" <%= (equipo.getFoto() == 1) ? "selected" : ""%>>Si</option>
                                <option value="0" <%= (equipo.getFoto() == 0) ? "selected" : ""%>>No</option>
                            </select>
                        </td>
                    </tr>	
                    <tr>
                        <td>Estado</td>
                        <td>
                            <select name="estado" id="estado">
                                <option value="1" <%= (equipo.getActivo() == 1) ? "selected" : ""%>>Activo</option>
                                <option value="0" <%= (equipo.getActivo() == 0) ? "selected" : ""%>>Inactivo</option>
                            </select>
                        </td>
                    </tr>	
                </table>
                <input type="button" onclick="javascript:modificar('<%= (String) request.getParameter("codigo")%>');" value="Modificar" id="cmd_modificar" name="cmd_modificar" > <input type="button" onclick="javascript:eliminar('<%= (String) request.getParameter("codigo")%>');" value="Eliminar" id="cmd_eliminar" name="cmd_eliminar" >  <input type="button" name="cmd_cancelar" id="cmd_cancelar" value ="Cancelar" onclick="javascript:cancelar()" >
            </form>
        </div>
        <%@ include file="foot.jsp" %>
    </body>
</html>

<%
    conexion.Close();
%>