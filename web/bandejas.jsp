<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<%@ include file="validausuario.jsp"%>
<%    
    db conexion = new db();
    ManejadorUsuarios manejador = new ManejadorUsuarios(conexion);
    ArrayList<Usuarios> lista = manejador.list();
    
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>Bandejas</title>
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
                var usuario = $("#usuario").val();
                if (usuario == "") {
                    alert("Debe seleccionar el usuario asociado a la bandeja");
                    return;
                }

                var url = "SrvCampBandeja";
                $.post(url, {
                    operacion: "add",
                    usuario: usuario
                }, procesar);


            }
            function procesar(result) {
                if (result.trim() != "OK") {
                    $("#info").html("<img src='images/alerta.gif'> " + result);
                } else {
                    $("#info").html("<img src='images/alerta.gif'> Bandeja agregada correctamente");
                    list();
                }
            }

            function list() {
                var url = "SrvCampBandeja?operacion=list";
                $("#list").load(url, function () {
                    $("input:submit, a, button", "#list").button();
                });
            }
            
            function Activar(key) {
                if (confirm("Esta seguro de activar la bandeja?")) {
                    var url = "SrvCampBandeja";
                    $.get(url,{
                        operacion : "estado",
                        estado : "1",
                        key : key
                    }, function (data) {
                        if (data.trim() == "OK") {
                            $("#info").html("<img src='images/alerta.gif'> Bandeja activada correctamente");
                            list();
                        }else {
                            $("#info").html("<img src='images/alerta.gif'>" + data);
                        }
                    });
                }
            }
            function Inactivar(key) {
                if (confirm("Esta seguro de inactivar la bandeja?")) {
                    var url = "SrvCampBandeja";
                    $.get(url,{
                        operacion : "estado",
                        estado : "0",
                        key : key
                    }, function (data) {
                        if (data.trim() == "OK") {
                            $("#info").html("<img src='images/alerta.gif'> Bandeja inactivada correctamente");
                            list();
                        }else {
                            $("#info").html("<img src='images/alerta.gif'>" + data);
                        }
                    });
                }
            }
            
            function Eliminar(key) {
                if (confirm("Esta seguro de eliminar la bandeja?")) {
                    var url = "SrvCampBandeja";
                    $.get(url,{
                        operacion : "remove",
                        key : key
                    }, function (data) {
                        if (data.trim() == "OK") {
                            $("#info").html("<img src='images/alerta.gif'> Bandeja eliminada correctamente");
                            list();
                        }else {
                            $("#info").html("<img src='images/alerta.gif'>" + data);
                        }
                    });
                }
            }
        </script>
    </head>
    <body onload="list()">
        <%@ include file="header.jsp" %>
        <div class="contencenter demo">
            <H2>Registro de Bandejas</H2>
            <form action="" name="form1">
                <table id="lista" border=0>
                    <tr>
                        <th colspan="2">Información Bandeja</th>
                    </tr>
                    <tr>
                        <td>Usuario</td>
                        <td>
                            <select name="usuario" id="usuario">
                                <option value="">Seleccionar</option>
                                <% for (Usuarios usuario : lista) {  %>
                                    <option value="<%= usuario.getUsuario() %>"><%= usuario.getNombre() %> (<%= usuario.getUsuario() %>)</option>
                                <% } %>
                                
                            </select>
                            
                            
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2"><input type="button" onclick="javascript:agregar();" value="Agregar" id="cmd_agregar" name="cmd_agregar"  ></td>
                    </tr>
                </table>
            </form>
            <div id="info"></div>
            <a href="estado_bandejas.jsp" name="cmd_estado_bandeja">Estado bandejas</a>
            <a href="reporte_bandejas.jsp" name="cmd_estado_bandeja">Reportes APP</a>
            <div id="list"></div>

        </div>
        <%@ include file="foot.jsp" %>
    </body>
</html>
<%
    conexion.Close();
%>
