<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<%@ include file="validausuario.jsp"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>Listado Visitas</title>
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

            function consultar() {
                var fecha_inicial = $("#fecha_inicial").val();
                var fecha_final = $("#fecha_final").val();
                var tipo = $("#tipo").val();
                var reporte = $("#reporte").val();


                $("#list").html("<img src='images/loading.gif'>Generando reporte, espere porfavor...");

                var url = "table_list_visitas.jsp";

                $("#list").load(url, {
                    fecha_inicial: fecha_inicial,
                    fecha_final: fecha_final,
                    tipo : tipo,
                    reporte : reporte
                });

                //$("#descargar").css("display", "block");
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
            <h2>Listado Visitas</h2>
            <div id="info"></div>
            <form action="SrvDownloadListVisitas" name="form1">
                <table>
                    <tr>
                        <th colspan="4">Filtro</th>
                    </tr>
                    <tr>
                        <td>Tipo</td>
                        <td>
                            <select id="tipo">
                                <option value="all">Todos</option>
                                <option value="1">Inspección</option>
                                <option value="2">Censo</option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td>Reporte</td>
                        <td>
                            <select id="reporte">
                                <option value="all">Todos</option>
                                <option value="1">Efectiva</option>
                                <option value="2">Anomalia</option>
                            </select>
                        </td>
                    </tr>
                    
                    <tr>
                        <td>Fecha Inicial</td>
                        <td><input type="text" name="fecha_inicial" id="fecha_inicial" readonly value="<%= Utilidades.strDateServer()%>" class="fecha"></td>
                    </tr>
                    <tr>
                        <td>Fecha Final</td>
                        <td><input type="text" name="fecha_final" id="fecha_final" readonly value="<%= Utilidades.strDateServer()%>" class="fecha"></td>
                    </tr>
                    
                </table>
                    
                    <input type="button" name="cmd_consultar" value="Consultar" onclick="javascript:consultar();" >
<!--                    <input type="submit" name="cmd_descargar" value="Descargar" >-->

            </form>

            <div id="list"></div>

        </div>
        <%@ include file="foot.jsp" %>
    </body>
</html>
