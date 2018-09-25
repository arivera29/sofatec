<%@page import="com.are.sofatec.db"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<%@ include file="validausuario.jsp"%>
<%@ page import="java.sql.*"%>
<%    db conexion = new db();
    String id = (String) request.getParameter("id");
    String visita = (String) request.getParameter("visita");
    

    
    String sql = "SELECT id FROM camp_orden_fotos WHERE id_orden=? AND visita=?";

    java.sql.PreparedStatement pst2 = conexion.getConnection().prepareStatement(sql);
    pst2.setString(1, id);
    pst2.setString(2, visita);
    
    java.sql.ResultSet rsFotos = conexion.Query(pst2);


%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>Fotos Visita</title>
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
            <h2>Fotos</h2>
            <%  while (rsFotos.next()) {  %>
            <img src='SrvDownloadPhotoVisita?visita=<%= visita  %>&id=<%= rsFotos.getInt("id") %>'>
            <% } %>
    </body>
</html>
<%
    conexion.Close();
%>
