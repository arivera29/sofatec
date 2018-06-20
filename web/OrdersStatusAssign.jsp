<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="validausuario.jsp"%>
<%@ page import="com.are.sofatec.*" %>
<%@ page import="java.sql.*" %>
<%
        db conexion = new db();
        departamento dpto = new departamento(conexion);
        ResultSet rs = dpto.list();
        boolean rsIsEmpty = !rs.next();
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>CONSULTA ASIGNACION</title>
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
                $("#descargar").css("display", "none");
            });
        </script>
        <script type="text/javascript" language="javascript">
            function consultar() {
                var url = "SrvListAssign?operacion=ciudad&ciudad=" + $("#localidad").val();
                $("#list").load(url);
                $("#descargar").css("display", "block");
            }

            function cargar_localidad(combo) {
                var url = "srvLocalidad?operacion=combo&departamento=" + combo.value;
                $("#municipio").load(url);
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
            <H2>LISTADO ORDENES ASIGNADAS FECHA: <%= Utilidades.strDateServer() %></H2>
            <div id="info"></div>
            <form action="" name="form1">
                <table>
                    <tr>
                        <th colspan="4">FILTRO</th>
                    </tr>
                    <tr>
                        <td>Departamento</td>
                        <td>
                            <select id="departamento" onchange="javascript:cargar_localidad(this);">
                                <option value="">Seleccionar departamento</option>
                                <%  if (!rsIsEmpty) { %>
                                <% do { %>
                                <option value="<%= rs.getString("depacodi") %>"><%= rs.getString("depadesc") %></option>
                                <% }while(rs.next()); %>
                                <% } %>
                            </select>
                        </td>
                        <td>Municipio</td>
                        <td><div id="municipio">
                                <select id="localidad">
                                    <option value="-1">Seleccionar</option>
                                </select>
                            </div></td>
                    </tr>
                </table>
                <input type="button" name="cmd_buscar" value="Consultar" onclick="javascript:consultar()" >
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
        if (conexion != null) {
                conexion.Close();
        }
%>