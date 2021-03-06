<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<%
    db conexion = new db();
    String sql = "SELECT brigada, recunomb, count(*) as total "
            + " FROM visitas "
            + " INNER JOIN recurso ON recucodi = brigada "
            + " WHERE estado = 1 "
            + " GROUP BY brigada "
            + " ORDER BY 2";
    java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
    java.sql.ResultSet rs = conexion.Query(pst);
    int rows = 0;

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
            function remove_all() {
                if (confirm("Esta seguro de eliminar todas las visitas pendientes?")) {
                    var url ="SrvVisitas";
                    $.get(url, {
                        operacion : "all"
                    }, function (data) {
                        if (data.trim() == "OK") {
                            alert("Visitas Eliminadas correctamente");
                            location.reload();
                        }else {
                            alert(data);
                        }
                    });
                }               
            }
            
            function remove_brigada(brigada) {
                if (confirm("Esta seguro de eliminar todas las visitas pendientes de la birgada " + brigada + "?")) {
                    var url ="SrvVisitas";
                    $.get(url, {
                        operacion : "brigada",
                        brigada : brigada
                    }, function (data) {
                        if (data.trim() == "OK") {
                            alert("Visitas Eliminadas correctamente");
                            location.reload();
                        }else {
                            alert(data);
                        }
                    });
                }          
            }
            
        </script>
    </head>
    <body>
        <%@ include file="header.jsp" %>
        <div class="contencenter demo">
            <h1>Visitas</h1>
            <a href="uploadvisitas.jsp">Cargar visitas</a>
            <a href="reporte_visitas.jsp">Reportes</a>
            <a href="listado_visitas.jsp">Listado</a>
            <a href="listado_censos.jsp">Censos</a>
            <h2>Visitas pendientes</h2>
            
            <table class="table">
                <thead>
                    <tr>
                        <th>BRIGADA</th>
                        <th>NOMBRE</th>
                        <th>VISITAS</th>
                        <th></th>
                    </tr>
                </thead>
                <tbody>
                    <%  while (rs.next()) { %>
                    <%  if (++rows % 2 == 0) { %>
                    <tr class="odd">
                        <% } else { %>
                    <tr>
                        <% }%>
                        <td><%= rs.getString("brigada")%></td>
                        <td><%= rs.getString("recunomb")%></td>
                        <td><%= rs.getString("total")%></td>
                        <td>
                            <a href="visitasbrigada.jsp?id=<%= rs.getString("brigada")%>">Ver</a>
                            <a href="javascript:remove_brigada('<%= rs.getString("brigada") %>');">Eliminar</a>
                        </td>
                    </tr>
                    <%  }  %>
                </tbody>
            </table>
            <% if (rows == 0) { %>
            <p>No hay registros</p>
            <%  } else { %>
            <button name="cmd>_remove_all" type="button" onclick="javascript:remove_all();">Eliminar visitas pendientes</button>
            <% } %>
        </div>
        <%@ include file="foot.jsp" %>
    </body>
</html>
<%
    conexion.Close();
%>