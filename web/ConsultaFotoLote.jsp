<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="validausuario.jsp"%>
<%@ page import="com.are.sofatec.*" %>
<%@ page import="com.are.entidades.*" %>
<%@ page import="com.are.manejadores.*" %>
<%@ page import="java.sql.*" %>

<%
        db conexion = new db();
        ArrayList<TipoOrden> lista1 = new ArrayList<TipoOrden>();
        ManejadorTipoOrden controlador1 = new ManejadorTipoOrden(conexion);
        lista1 = controlador1.List();

        ManejadorZonas manejador = new ManejadorZonas(conexion);
        ArrayList<zonas> lista = manejador.List();

%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>Consulta Fotos</title>
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
                $(".boton").button();

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
                var zona = $("#zona").val();
                var hora1 = $("#hora1").val();
                ;
                var hora2 = $("#hora2").val();
                ;

                var tipo = $("#tipo").val();
                var url = "ListadoFotoLote.jsp";
                $("#list").html("<img src='images/loading.gif'>Procesando Solicitud");
                $("#list").load(url, {
                    fecha_inicial: fecha_inicial,
                    fecha_final: fecha_final,
                    zona: zona,
                    hora1: hora1,
                    hora2: hora2,
                    tipo: tipo
                }, function () {

                });
                $("#descargar").css("display", "block");
            }

            function descargar() {
                var fecha_inicial = $("#fecha_inicial").val();
                var fecha_final = $("#fecha_final").val();
                var zona = $("#zona").val();
                var tipo = $("#tipo").val();
                var hora1 = $("#hora1").val();
                ;
                var hora2 = $("#hora2").val();
                ;

                var url = "SrvZipImagesLotes";
                $("#info").html("<img src='images/loading.gif'> Generando archivo comprimido.  Espere Porfavor...");

                $.post(url, {
                    fecha_inicial: fecha_inicial,
                    fecha_final: fecha_final,
                    zona: zona,
                    tipo: tipo,
                    hora1: hora1,
                    hora2: hora2
                }, procesar);

            }

            function procesar(data) {
                $("#info").html(data);
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
            <h2>CONSULTA FOTOS</h2>
            <div id="info"></div>
            <form action="SrvZipImagesLotes" name="form1">
                <table>
                    <tr>
                        <th colspan="4">Seleccion Recurso</th>
                    </tr>
                    <tr>
                        <td>Zona</td>
                        <td>
                            <select name="zona" id="zona">
                                <option value="all">Todos</option>
                                <% for (zonas zona : lista) { %>
                                <option value="<%= zona.getId() %>"><%= zona.getNombre() %></option>
                                <% } %>
                            </select> 
                        </td>
                    </tr>
                    <tr>
                        <td>Tipo de Orden</td>
                        <td>
                            <select id="tipo" name="tipo">
                                <option value="all">Todos</option>

                                <% for (TipoOrden tipo : lista1) { %>
                                <option value="<%= tipo.getCodigo() %>"><%= tipo.getDescripcion() %></option>
                                <% } %>

                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td>Fecha Inicial</td>
                        <td><input type="text" name="fecha_inicial" id="fecha_inicial" readonly value="<%= Utilidades.strDateServer()  %>" class="fecha"> Hora:<input type="text" name="hora1" id="hora1" size=10 value="00:00:00"> </td>
                    </tr>
                    <tr>
                        <td>Fecha Final</td>
                        <td><input type="text" name="fecha_final" id="fecha_final" readonly value="<%= Utilidades.strDateServer()  %>" class="fecha"> Hora:<input type="text" name="hora2" id="hora2" size=10 value="<%= Utilidades.strTimeServer() %>"></td>
                    </tr>
                    <tr>
                        <td colspan="4"><input type="button" name="cmd_buscar" class="boton" value="Consultar" onclick="javascript:consultar()" > <input type="button" name="cmd_descargar" value="Descargar" class="boton" onClick="javascript:descargar();"></td>
                    </tr>
                </table>
                <div id="info"></div>
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

<%
        conexion.Close();
%>
