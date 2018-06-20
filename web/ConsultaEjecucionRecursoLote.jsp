<%@ page language="java" contentType="text/html; charset=utf-8"  pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="validausuario.jsp"%>
<%@ page import="com.are.sofatec.*" %>
<%@ page import="java.sql.*" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <title>Consulta Ejecucion Ordenes Recurso</title>
        <link rel="stylesheet" href="themes/redmond/jquery.ui.all.css">
        <LINK REL="stylesheet" TYPE="text/css" HREF="main.css">
        <script src="js/jquery.js" language="JavaScript"></script>
        <script src="ui/jquery.ui.core.js" language="JavaScript"></script>
        <script src="ui/jquery.ui.widget.js" language="JavaScript"></script>
        <script src="ui/jquery.ui.button.js"></script>
        <script src="ui/jquery.effects.core.js"></script>
        <script src="ui/jquery.effects.slide.js"></script>
        <script src="ui/jquery.ui.datepicker.js"></script>
        <script src="ui/jquery.effects.explode.js"></script>
        <script src="ui/jquery.effects.fold.js"></script>
        <script src="ui/jquery.effects.slide.js"></script>

        <script type="text/javascript" language="javascript">
            $(function () {
                $("input:submit, a, button", ".demo").button();
                $("input:button, a, button", ".demo").button();
                $("#descargar").css("display", "none");
                $(".fecha").datepicker({
                    showOn: "button",
                    buttonImage: "images/calendar.gif",
                    buttonImageOnly: true,
                    dateFormat: "yy-mm-dd"
                });
                $(".fecha").datepicker("option", "showAnim", "slide");
            });

            function buscar() {
                url = "BuscarRecurso.jsp";
                window.open(url, "BuscarRecurso", "width=600,height=500,scrollbars=YES,menubar=No,toolbar=NO,status=YES");
            }

            function consultar() {
                var recurso = $("#cedula").val();
                var fecha_inicial = $("#fecha_inicial").val();
                var fecha_final = $("#fecha_final").val();
                $("#list").html("<img src='images/loading.gif'> Generando consulta, espere un momento porfavaor...");
                var url = "ListadoEjecucionRecursoLote.jsp";
                $("#list").load(url, {
                    recurso: recurso,
                    fecha_inicial: fecha_inicial,
                    fecha_final: fecha_final
                });
                $("#descargar").css("display", "block");
            }


        </script>

        <script language="javascript">
            $(document).ready(function () {
                $(".botonExcel").click(function (event) {
                    $("#datos_a_enviar").val($("<div>").append($("#Exportar_a_Excel").eq(0).clone()).html());
                    $("#FormularioExportacion").submit();
                });
            });
        </script>
    </head>
    <body>
        <%@ include file="header.jsp" %>
        <div class="contencenter demo">
            <h2>Listado Ejecucion Ordenes de Servicio</h2>
            <div id="info"></div>
            <form action="" name="form1">
                <table>
                    <tr>
                        <th colspan="4">Seleccion Recurso</th>
                    </tr>
                    <tr>
                        <td>Recurso</td>
                        <td colspan="3"><input type="text" name="cedula" id="cedula" readonly> Nombre: <input type="text" name="nombre" id="nombre" size=40 readonly> <input type="button" name="cmd_buscar" id="cmd_buscar" value="Buscar" onclick="javascript:buscar()" ></td>
                    </tr>
                    <tr>
                        <td>Fecha Inicial</td>
                        <td><input type="text" name="fecha_inicial" id="fecha_inicial" readonly value="<%= Utilidades.strDateServer()  %>" class="fecha"></td>
                    </tr>
                    <tr>
                        <td>Fecha Final</td>
                        <td><input type="text" name="fecha_final" id="fecha_final" readonly value="<%= Utilidades.strDateServer()  %>" class="fecha"></td>
                    </tr>
                    <tr>
                        <td colspan="4"><input type="button" name="cmd_buscar" value="Consultar" onclick="javascript:consultar()" ></td>
                    </tr>
                </table>

            </form>
            <div id="descargar">
                <form action="ExportExcel.jsp" method="post" target="_blank" id="FormularioExportacion">
                    <p>Exportar a Excel  <img src="images/Download.png" class="botonExcel" /></p> 
                    <input type="hidden" id="datos_a_enviar" name="datos_a_enviar" /> 
                </form> 
            </div>
            <div id="list"></div>
        </div>
        <%@ include file="foot.jsp" %>
    </body>
</html>
