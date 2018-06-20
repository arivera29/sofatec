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
    int rows=0;

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

        </script>
    </head>
    <body>
        <%@ include file="header.jsp" %>
        <div class="contencenter demo">
            <h1>Visitas pendientes brigaba <%= (String)request.getParameter("id")  %></h1>
            <a href="SrvDownloadVisitas?id=<%= (String)request.getParameter("id") %>">Descargar</a> <a href="visitas.jsp">Cancelar</a>
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
                    <%  if (++rows % 2 == 0) { %>
                    <tr class="odd">
                    <% } else { %>
                    <tr>
                    <% } %>
                        <td><%= visita.getId()%></td>
                        <td><%= visita.getTipo()==1?"REVISION":"CENSO" %></td>
                        <td><%= visita.getNic()%></td>
                        <td><%= visita.getDepartamento()%></td>
                        <td><%= visita.getMunicipio()%></td>
                        <td><%= visita.getDireccion()%></td>
                        <td><%= visita.getBarrio()%></td>
                        <td><%= visita.getCliente()%></td>
                        <td><%= formato.format(visita.getFechaAsignacion())%></td>
                        <td><a href="detallevisita.jsp?id=<%= visita.getId()%>">Ver</a></td>
                    </tr>
                    <%  }%>
                </tbody>
            </table>
            <p>Total registros: <%= lista.size()%></p>
        </div>
        <%@ include file="foot.jsp" %>
    </body>
</html>
<%
    conexion.Close();
%>