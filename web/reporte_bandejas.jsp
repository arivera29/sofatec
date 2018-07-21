<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<%
    db conexion = new db();
    String sql = "SELECT USUARIO_APP, recunomb, count(*) as total "
            + " FROM camp_orden "
            + " INNER JOIN recurso ON recucodi = USUARIO_APP "
            + " WHERE DATE(FECHA_CARGA) = CURRENT_DATE()"
            + " GROUP BY USUARIO_APP "
            + " ORDER BY 2";
    java.sql.PreparedStatement pst1 = conexion.getConnection().prepareStatement(sql);
    java.sql.ResultSet rs1 = conexion.Query(pst1);
    int rows = 0;
    
    

    java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd H:mm");
%>
<!DOCTYPE html>
<%@ include file="validausuario.jsp"%>
<html>
    <head>
        <meta charset=ISO-8859-1">
        <title>REPORTES BANDEJAS</title>
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
            <h2>Solicitudes Generación OS <%= format.format(new java.util.Date()) %></h2>
            <a href="bandejas.jsp">Regresar</a>
            <table class="table">
                <thead>
                    <tr>
                        <th>BRIGADA</th>
                        <th>NOMBRE</th>
                        <th>SOLICITUDES</th>
                    </tr>
                </thead>
                <tbody>
                    <%  while (rs1.next()) { %>
                    <tr <%= (++rows % 2 == 0)?"class='odd'":""   %>>
                        <td><%= rs1.getString("USUARIO_APP")%></td>
                        <td><%= rs1.getString("recunomb")%></td>
                        <td><center><%= rs1.getString("total")%></center></td>
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