<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<%@ include file="validausuario.jsp"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>CONTRATISTAS</title>
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
            function agregar() {
                var nombre = $("#nombre").val();
                var estado = $("#estado").val();
                var correo = $("#correo").val();

                if (nombre == "") {
                    alert("Falta ingresar informacion");
                    return;
                }

                var cmd = document.getElementById("cmd_agregar");
                cmd.disabled = true;
                $("#info").html("<img src=\"images/loading.gif\" >Procesando solicitud");
                $.post(
                        "SrvContratistas",
                        {
                            operacion: "add",
                            nombre: nombre,
                            correo: correo,
                            estado: estado
                        },
                        procesar

                        );

            }

            function procesar(resultado) {
                var cmd = document.getElementById("cmd_agregar");
                cmd.disabled = false;
                if (resultado != 'OK') {
                    $("#info").html("<img src=\"images/alerta.gif\">" + resultado);
                } else {
                    $("#info").html("<img src=\"images/edit.png\">Contratita agregado correctamente");
                    $("#nombre").val("");
                    $("#correo").val("");
                    document.getElementById("nombre").focus();
                    list();
                }

            }

            function list() {
                $("#list").load("SrvContratistas?operacion=list", function () {
                    $("input:submit, a, button", ".demo").button();
                });
            }


        </script>
    </head>
    <body onload="list()">
        <%@ include file="header.jsp" %>
        <div class="contencenter demo">
            <h2>CONTRATISTAS</h2>
            <div id="info"></div>
            <form action="" name="form1">
                <table id="lista" border=0>
                    <tr>
                        <th colspan="2">NUEVO CONTRATISTA</th>
                    </tr>
                    <tr>
                        <td>Nombre</td>
                        <td><input type="text" name="nombre" id="nombre" size=40></td>
                    </tr>
                    <tr>
                        <td>Correo electrónico</td>
                        <td><textarea name="correo" id="correo" rows="10" cols="40"></textarea></td>
                    </tr>
                    <tr>
                        <td>Activo</td>
                        <td>
                            <select name="estado" id="estado">
                                <option value="1">Si</option>
                                <option value="0">No</option>
                            </select>
                        </td>
                    </tr>	
                </table>
            </form>
            <input type="button" name="cmd_agregar" id="cmd_agregar" value="Agregar" onclick="javascript:agregar()" >
            <div id="list"></div>
        </div>
        <%@ include file="foot.jsp" %>
    </body>
</html>