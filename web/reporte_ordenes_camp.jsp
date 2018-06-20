<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="validausuario.jsp"%>
<%@ page import="com.are.sofatec.*" %>
<%@ page import="com.are.manejadores.*" %>
<%@ page import="com.are.entidades.*" %>
<%@ page import="java.sql.*" %>
<%    db conexion = new db();

    ManejadorContratista manejador = new ManejadorContratista(conexion);
    ArrayList<Contratista> lista = manejador.List();

    ManejadorCamp m1 = new ManejadorCamp(conexion);
    ArrayList<Camp> lista1 = m1.List();

%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>Reporte de Ordenes Campaña</title>
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
                var contratista = $("#contratista").val();
                var estado = $("#estado").val();
                var camp = $("#camp").val();


                $("#list").html("<img src='images/loading.gif'>Generando reporte, espere porfavor...");

                var url = "listado_ordenes_camp.jsp";

                $("#list").load(url, {
                    contratista: contratista,
                    estado : estado,
                    camp : camp,
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
            <h2>Listado de ordenes campañas</h2>
            <div id="info"></div>
            <form action="" name="form1">
                <table>
                    <tr>
                        <th colspan="4">Filtro</th>
                    </tr>
                    <tr>
                        <td>Contratista</td>
                        <td>
                            <select name="contratista" id="contratista">
                                <option value="all">Todos</option>
                                <% for (Contratista contratista : lista) {%>
                                <option value="<%= contratista.getCodigo()%>"><%= contratista.getNombre()%></option>
                                <% } %>
                            </select> 
                        </td>
                    </tr>
                    <tr>
                        <td>Campaña</td>
                        <td>
                            <select name="camp" id="camp">
                                <option value="all">Todas</option>
                                <% for (Camp camp : lista1) {%>
                                <option value="<%= camp.getCodigo()%>"><%= camp.getDescripcion()%></option>
                                <% }%>
                            </select> 
                        </td>
                    </tr>
                    <tr>
                        <td>Estado</td>
                        <td>
                            <select name="estado" id="estado">
                                <option value="all">Todos</option>
                                <option value="1">Con orden</option>
                                <option value="2">Pendiente de Orden</option>

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
                    <tr>
                        <td colspan="4"><input type="button" name="cmd_buscar" value="Consultar" onclick="javascript:consultar()" > </td>
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
