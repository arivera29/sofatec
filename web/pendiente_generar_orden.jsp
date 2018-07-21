<%@page import="com.are.sofatec.db"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<%@ include file="validausuario.jsp"%>
<%@ page import="java.sql.*"%>
<%    db conexion = new db();
    String usuario = (String) session.getAttribute("usuario");
    String perfil_usuario = (String) session.getAttribute("perfil");

    String sql = "SELECT ID,NIC, NIS, NUM_OS, COMENTARIO, CONTRATISTA, CONTNOMB, "
            + " BRIGADA,RECUNOMB, ID_CAMP,CAMPDESC, ZONA, ZONANOMB, FECHA_CARGA, USUARIO_CARGA, USUARIO_APP "
            + " FROM camp_orden "
            + " INNER JOIN contratistas ON CONTRATISTA = CONTCODI "
            + " INNER JOIN recurso ON BRIGADA = RECUCODI "
            + " INNER JOIN camp ON ID_CAMP = campcodi "
            + " INNER JOIN zonas ON ZONA = zonacodi "
            + " WHERE NUM_OS = ''";
    
    if (!perfil_usuario.equals("1") && !perfil_usuario.equals("6")) {
        sql += "AND USUARIO_CARGA = '" + usuario + "'";
    }

    java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
    //pst.setString(1, usuario);
    java.sql.ResultSet rs = conexion.Query(pst);

    int count = 0;


%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>Pendiente Generar OS</title>
        <link rel="stylesheet" href="themes/redmond/jquery.ui.all.css">
        <link rel="stylesheet" TYPE="text/css" HREF="main.css">
        <script src="js/jquery.js" type="text/javascript"></script>
        <script src="ui/jquery.ui.core.js" language="JavaScript"></script>
        <script src="ui/jquery.ui.widget.js" language="JavaScript"></script>
        <script src="ui/jquery.ui.button.js"></script>
        <script>
            $(function () {
                $("input:submit, a, button", ".demo").button();
                $("input:button, a, button", ".demo").button();
            });

        </script>
        
    </head>
    <body>

        <%@ include file="header.jsp"%>
        <div class="contencenter demo">
            <h2>Pendiente generación orden de servicio</h2>

            <table>
                <tr>
                    <th></th>
                    <th>FECHA</th>
                    <th>NIC</th>
                    <th>NIS</th>
                    <th>COMENTARIO</th>
                    <th>CONTRATISTA</th>
                    <th>BRIGADA</th>
                    <th>CAMPAÑA</th>
                    <th>ZONA</th>
                    <th>BANDEJA</th>
                    <th>ACCION</th>
                </tr>
                <% while (rs.next()) {%>
                <tr <%= ((count % 2 == 0) ? "class='odd'" : "")%>>
                    <td>
                        <%  if (!rs.getString("USUARIO_APP").equals("")) {  %>
                        <img src="images/device.png">
                        <%  }  %>
                    </td>
                    <td><img src='images/calendario.png'><%= rs.getString("FECHA_CARGA")%></td>
                    <td><%= rs.getString("NIC")%></td>
                    <td><%= rs.getString("NIS")%></td>
                    <td><%= rs.getString("COMENTARIO")%></td>
                    <td><%= rs.getString("CONTNOMB")%></td>
                    <td><img src='images/recurso.png'><strong><%= rs.getString("BRIGADA")%></strong> <%= rs.getString("RECUNOMB")%></td>
                    <td><strong><%= rs.getString("ID_CAMP")%></strong> <%= rs.getString("CAMPDESC")%></td>
                    <td><%= rs.getString("ZONANOMB")%></td>
                    <td><%= rs.getString("USUARIO_CARGA")%></td>
                    <td><a href = "actualizar_orden.jsp?id=<%= rs.getInt("ID")  %>">Generar</a></td>
                </tr>
                <% count++; %>
                <% } %>
                <tr>
                    <td colspan="11">Registros: <%= count%></td>
                </tr>
            </table>
            <% if (count == 0) { %>
            <p>No hay registros pendientes de orden de servicio</p>
            <%  }  %>
        </div>

    </body>
</html>
<%
    conexion.Close();
%>
