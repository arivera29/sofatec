<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<%@ include file="validausuario.jsp"%>
<%    
    db conexion = new db();
    String sql = "SELECT A.cabauser BANDEJA, count(B.ID) CANT "
            + " FROM camp_bandeja A "
            + " LEFT JOIN camp_orden B ON A.cabauser = B.usuario_carga AND B.NUM_OS = '' "
            + " GROUP BY A.cabauser "
            + " ORDER BY A.cabauser ";
    java.sql.ResultSet rs = conexion.Query(sql);
    int fila=1;
    
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
            
        </script>
    </head>
    <body onload="list()">
        <%@ include file="header.jsp" %>
        <div class="contencenter demo">
            <H2>Estado de Bandejas</H2>
            <a href="bandejas.jsp">Regresar</a>
            <div id="lista">
                <table class="table">
                    <thead>
                        <tr>
                            <th>BANDEJA</th>
                            <th>ORDENES PENDIENTES</th>
                        </tr>
                    </thead>
                    
                    <tbody>
                        <%  while (rs.next()) { %>
                        <tr <%= (++fila % 2 == 0) ?"class='odd'":""  %>>
                            <td><%= rs.getString("BANDEJA") %></td>
                            <td><center><%= rs.getLong("CANT") %></center></td>
                        </tr>
                        
                        <% }  %>
                        
                    </tbody>
                    
                </table>
            </div>
                
            

        </div>
        <%@ include file="foot.jsp" %>
    </body>
</html>
<%
    conexion.Close();
%>
