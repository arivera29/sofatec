<%@page import="com.are.entidades.Recurso"%>
<%@page import="com.are.manejadores.ManejadorRecurso"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.are.entidades.Visita"%>
<%@page import="com.are.manejadores.ManejadorVisita"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<%
    db conexion = new db();
    String id = (String) request.getParameter("id");
    ManejadorVisita manejador = new ManejadorVisita(conexion);
    ArrayList<Visita> lista = manejador.ListByBrigada(id, 1);
    SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy H:mm");
    int rows = 0;
    ManejadorRecurso controller = new ManejadorRecurso(conexion);
    String roles[] = {"1", "3", "4"};
    ArrayList<Recurso> lista2 = controller.FilterByRoles(roles);

%>
<!DOCTYPE html>
<%@ include file="validausuario.jsp"%>
<html>
    <head>
        <meta charset=ISO-8859-1">
        <title>VISITAS</title>
        <link rel="stylesheet" href="themes/redmond/jquery.ui.all.css">
        <LINK REL="stylesheet" TYPE="text/css" HREF="main.css">
        <script src="js/jquery.js" language="JavaScript"></script>
        <script src="ui/jquery.ui.core.js" language="JavaScript"></script>
        <script src="ui/jquery.ui.widget.js" language="JavaScript"></script>
        <script src="ui/jquery.ui.button.js"></script>

        <script>
            $(function () {
                $("input:submit, a, button", ".demo").button();
                $("input:button, a, button", ".demo").button();
            });
        </script>


        <script>
            function eliminarVisita(id) {
                if (confirm("Esta seguro de eliminar la visita seleccionada?")) {
                    var url = "SrvVisita";
                    $.post(url, {
                        operacion: "one",
                        id: id
                    }, function (data) {
                        if (data.trim() == "OK") {
                            row_id = "#row_" + id;
                            $(row_id).fadeOut(2000);
                            $("#info").html("<img src='images/ok.png'> Visita eliminada correctamente");
                        } else {
                            $("#info").html("<img src='images/error.png'> Error al eliminar la visita. " + data);
                        }
                    });
                }
            }

            function buscar() {
                url = "BuscarRecurso.jsp";
                window.open(url, "BuscarRecurso", "width=600,height=500,scrollbars=YES,menubar=No,toolbar=NO,status=YES");
            }

            function reasignar(brigada_old) {
                var brigada_new = $("#cedula").val();
                if (brigada_new == "") {
                    alert("Debe seleccionar el recurso destino");
                    return;
                }

                if (confirm("Esta seguro de transferir las visitas?")) {
                    var url = "SrvVisitas";
                    $.post(url, {
                        operacion: "reasignar",
                        brigada_old: brigada_old,
                        brigada_new: brigada_new
                    }, function (data) {
                        if (data.trim() == "OK") {
                            alert("Transferencia realizada correctamente");
                            window.location.href = "visitas.jsp";
                        } else {

                        }
                    });
                }

            }

        </script>
    </head>
    <body>
        <%@ include file="header.jsp" %>
        <div class="contencenter demo">
            <h1>Visitas pendientes brigaba <%= (String) request.getParameter("id")%></h1>
            <a href="SrvDownloadVisitas?id=<%= (String) request.getParameter("id")%>">Descargar</a> <a href="visitas.jsp">Cancelar</a>
            <div id="info"></div>
            <table class="table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>TIPO</th>
                        <th>NIC</th>
                        <th>DEPARTAMENTO</th>
                        <th>MUNICIPIO</th>
                        <th>DIRECCION</th>
                        <th>BARRIO</th>
                        <th>CLIENTE</th>
                        <th>FECHA ASIG.</th>
                        <th></th>
                    </tr>
                </thead>
                <tbody>
                    <% for (Visita visita : lista) {%>
                    <%  if (++rows % 2 == 0) {%>
                    <tr class="odd" id = "row_<%= visita.getId()%>">
                        <% } else {%>
                    <tr id = "row_<%= visita.getId()%>">
                        <% }%>
                        <td><%= visita.getId()%></td>
                        <td><%= visita.getTipo() == 1 ? "REVISION" : "CENSO"%></td>
                        <td><%= visita.getNic()%></td>
                        <td><%= visita.getDepartamento()%></td>
                        <td><%= visita.getMunicipio()%></td>
                        <td><%= visita.getDireccion()%></td>
                        <td><%= visita.getBarrio()%></td>
                        <td><%= visita.getCliente()%></td>
                        <td><%= formato.format(visita.getFechaAsignacion())%></td>
                        <td>
                            <a href="detallevisita.jsp?id=<%= visita.getId()%>">Ver</a>
                            <a href="javascript:eliminarVisita(<%= visita.getId()%>)">Eliminar</a>

                        </td>
                    </tr>
                    <%  }%>
                </tbody>
            </table>
            <p>Total registros: <%= lista.size()%></p>

            <h2>Transferir</h2>
            <div id="transferir">
                <form name="form1">
                    Recurso: 
                    <input type="text" name="cedula" id="cedula" disabled>
                    <input type="text" name="nombre" id="nombre" disabled size="30">
                    <button id="cmd_buscar" name="cmd_buscar" type="button" onclick="javascript:buscar();">Buscar</button>

                    <button name="btn_asignar" id="btn_asignar" onclick="javascript:reasignar('<%= (String) request.getParameter("id")%>')" type="button">Asignar</button>
                </form>
            </div>

        </div>
        <%@ include file="foot.jsp" %>
    </body>
</html>
<%
    conexion.Close();
%>