<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<%
    db conexion = new db();
    String sql = "SELECT brigada, recunomb, count(*) as total "
            + " FROM visitas "
            + " INNER JOIN recurso ON recucodi = brigada "
            + " WHERE estado = 2 "
            + " AND DATE(fecha_ejecucion) = CURRENT_DATE()"
            + " AND tipo = 1"  // Inspeccion
            + " GROUP BY brigada "
            + " ORDER BY 2";
    java.sql.PreparedStatement pst1 = conexion.getConnection().prepareStatement(sql);
    java.sql.ResultSet rs1 = conexion.Query(pst1);
    int rows = 0;
    
    sql = "SELECT brigada, recunomb, count(*) as total "
            + " FROM visitas "
            + " INNER JOIN recurso ON recucodi = brigada "
            + " WHERE estado = 2 "
            + " AND tipo = 2"  // Censo
            + " AND DATE(fecha_ejecucion) = CURRENT_DATE()"
            + " GROUP BY brigada "
            + " ORDER BY 2";
    java.sql.PreparedStatement pst2 = conexion.getConnection().prepareStatement(sql);
    java.sql.ResultSet rs2 = conexion.Query(pst2);

    java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd H:mm");
%>
<!DOCTYPE html>
<%@ include file="validausuario.jsp"%>
<html>
    <head>
        <meta charset=ISO-8859-1">
        <title>REPORTE VISITAS</title>
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
            <h2>Visitas Reportadas <%= format.format(new java.util.Date()) %></h2>
            <a href="visitas.jsp">Regresar</a>
            <h2>INSPECCIONES</h2>
            <table class="table">
                <thead>
                    <tr>
                        <th></th>
                        <th>CODIGO</th>
                        <th>NOMBRE</th>
                        <th>INSPECCIONES</th>
                    </tr>
                </thead>
                <tbody>
                    <%  while (rs1.next()) { %>
                    
                    <tr <%= (+rows % 2 == 0)?"class='odd'":""   %>>
                        <td><img src="images/device.png"></td>
                        <td><%= rs1.getString("brigada")%></td>
                        <td><%= rs1.getString("recunomb")%></td>
                        <td><center><%= rs1.getString("total")%></center></td>
                    </tr>
                    <%  }  %>
                </tbody>
            </table>
            <% if (rows == 0) { %>
            <p>No hay registros</p>
            <%  } %>
            <% rows = 0; %>
            
            <h2>CENSOS</h2>
            <table class="table">
                <thead>
                    <tr>
                        <th></th>
                        <th>CODIGO</th>
                        <th>NOMBRE</th>
                        <th>CENSOS</th>
                    </tr>
                </thead>
                <tbody>
                    <%  while (rs2.next()) { %>
                    <tr <%= (+rows % 2 == 0)?"class='odd'":""   %>>
                        <td><img src="images/device.png"></td>
                        <td><%= rs2.getString("brigada")%></td>
                        <td><%= rs2.getString("recunomb")%></td>
                        <td><center><%= rs2.getString("total")%></center></td>
                    </tr>
                    <%  }  %>
                </tbody>
            </table>
            <% if (rows == 0) { %>
            <p>No hay registros</p>
            <%  } %>
        </div>
        <%@ include file="foot.jsp" %>
    </body>
</html>
<%
    conexion.Close();
%>