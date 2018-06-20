<%@page import="com.are.entidades.CampBandeja"%>
<%@page import="com.are.manejadores.ManejadorCampBandeja"%>
<%@page import="com.are.entidades.Contratista"%>
<%@page import="com.are.manejadores.ManejadorContratista"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<%@ include file="validausuario.jsp"%>
<%    if (request.getParameter("id") == null) {
        response.sendRedirect("bandejas.jsp");
        return;
    }

    db conexion = new db();

    String id = (String) request.getParameter("id");
    ManejadorCampBandeja manejador = new ManejadorCampBandeja(conexion);
    manejador.Find(Integer.parseInt(id));
    CampBandeja bandeja = manejador.getBandeja();

    ManejadorContratista manejador1 = new ManejadorContratista(conexion);
    ArrayList<Contratista> lista = manejador1.List();

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
            function agregar(bandeja) {
                var contrata = $("#contratista").val();
                if (contrata == "") {
                    alert("Debe seleccionar el contratista asociado a la bandeja");
                    return;
                }

                var url = "SrvCampBandeja";
                $.post(url, {
                    operacion: "add_contrata",
                    bandeja: bandeja,
                    contrata: contrata
                }, function (result) {
                    if (result.trim() != "OK") {
                        $("#info").html("<img src='images/alerta.gif'> " + result);
                    } else {
                        list(bandeja);
                        $("#info").html("<img src='images/alerta.gif'> Contratista agregado correctamente");
                    }
                });


            }


            function list(bandeja) {
                var url = "SrvCampBandeja";
                $("#list").load(url, {
                    operacion: "list_contrata",
                    bandeja: bandeja
                }, function (data) {
                    $("input:submit, a, button", "#list").button();
                });
            }

            

            function Eliminar(key,bandeja) {
                if (confirm("Esta seguro de eliminar el contratista seleccionado?")) {
                    var url = "SrvCampBandeja";
                    $.get(url, {
                        operacion: "remove_contrata",
                        key: key
                    }, function (data) {
                        if (data.trim() == "OK") {
                            $("#info").html("<img src='images/alerta.gif'> Contratista eliminado correctamente");
                            list(bandeja);
                        } else {
                            $("#info").html("<img src='images/alerta.gif'>" + data);
                        }
                    });
                }
            }
        </script>
    </head>
    <body onload="list(<%= (String)request.getParameter("id")   %>)">
        <%@ include file="header.jsp" %>
        <div class="contencenter demo">
            <H2>Registro de Bandeja-Contratista</H2>
            <form action="" name="form1">
                <table id="lista" border=0>
                    <tr>
                        <th colspan="2">Información</th>
                    </tr>
                    <tr>
                        <td>Bandeja</td>
                        <td><%= bandeja.getId()%></td>
                    </tr>
                    <tr>
                        <td>Usuario</td>
                        <td><%= bandeja.getUsuario()%></td>
                    </tr>
                    <tr>
                        <td>Contratistas</td>
                        <td>
                            <select name="contratista" id="contratista">
                                <option value="">Seleccionar</option>
                                <% for (Contratista contratista : lista) {%>
                                <option value="<%= contratista.getCodigo()%>"><%= contratista.getNombre()%></option>
                                <% }%>

                            </select>


                        </td>
                    </tr>
                    <tr>
                        <td colspan="2"><input type="button" onclick="javascript:agregar(<%= (String) request.getParameter("id")%>);" value="Agregar" id="cmd_agregar" name="cmd_agregar"  ></td>
                    </tr>
                </table>
            </form>
            <div id="info"></div>
            <div id="list"></div>

        </div>
        <%@ include file="foot.jsp" %>
    </body>
</html>
<%
    conexion.Close();
%>
