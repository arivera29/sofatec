<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="validausuario.jsp"%>
<%@ page import="com.are.sofatec.*" %>
<%
        db conexion = new db();
        Marcas marca = new Marcas(conexion);
        departamento dpto = new departamento(conexion);
	
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>SMARTPHONES</title>
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

                var cmd = document.getElementById("cmd_agregar");
                cmd.disabled = true;
                $("#info").html("<img src=\"images/loading.gif\" >Procesando solicitud");
                $.post(
                        "SrvEquipos",
                        {
                            operacion: "add",
                            marca: marca,
                            imei: imei,
                            estado: estado,
                            tipo: tipo,
                            departamento: departamento,
                            foto: foto
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
                    $("#info").html("<img src=\"images/ok.png\">Equipo agregado correctamente");
                    document.getElementById("imei").value = "";
                    document.getElementById("imei").focus();
                }
            }

            function Buscar() {
                var criterio = $("#criterio").val();
                if (criterio == "") {
                    alert("Debe ingresar el criterio de busqueda");
                    return;
                }
                $("#list").load("SrvEquipos?operacion=list&criterio=" + criterio, function () {
                    $("input:submit, a, button", ".demo").button();
                });
            }


        </script>
    </head>
    <body>
        <%@ include file="header.jsp" %>
        <div class="contencenter demo">
            <h2>SMARTPHONES</h2>
            <div id="info"></div>
            <form action="" name="form1">
                <table id="lista" border=0>
                    <tr>
                        <th colspan="2">NUEVO SMARTPHONE</th>
                    </tr>
                    <tr>
                        <td>Departamento</td>
                        <td><%= dpto.CreateSelectHTML("departamento") %></td>
                    </tr>
                    <tr>
                        <td>Marca</td>
                        <td><%= marca.CreateSelectHTML("marca") %></td>
                    </tr>
                    <tr>
                        <td>IMEI</td>
                        <td><input type="text" name="imei" id="imei" size=40></td>
                    </tr>
                    <tr>
                        <td>Visualizacion Ordenes</td>
                        <td>
                            <select name="tipo" id="tipo">
                                <option value="0">Todas las ordenes</option>
                                <option value="1">Por orden de visualizacion</option>
                            </select>
                        </td>
                    </tr>	
                    <tr>
                        <td>Restringir Envio Foto?</td>
                        <td>
                            <select name="foto" id="foto">
                                <option value="1">Si</option>
                                <option value="0">No</option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td>Estado</td>
                        <td>
                            <select name="estado" id="estado">
                                <option value="1">Activo</option>
                                <option value="0">Inactivo</option>
                            </select>
                        </td>
                    </tr>			
                </table>
                <input type="button" name="cmd_agregar" id="cmd_agregar" value="Agregar" onclick="javascript:agregar()" >
            </form>
            <h2>Buscar Smartphone</h2>
            <form action="" name="form2">
                Ingrese criterio: <input type="text" name="criterio" id="criterio" size=40>
                <input type="button" name="cmd_buscar" id="cmd_buscar" value="Buscar" onclick="javascript:Buscar()">

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