<%@page import="com.are.sofatec.db"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="validausuario.jsp"%>
<%@ page import="java.sql.*"%>
<%
	String imei = (String)request.getParameter("imei");
	db conexion = new db();
	
	String sql = "select * from eventos where date(fecha) = current_date() and imei=? order by fecha";
	PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
	pst.setString(1, imei);
	ResultSet rs = conexion.Query(pst);
	
	
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Eventos Equipo <%= (String)request.getParameter("imei") %></title>
<link rel="stylesheet" href="themes/redmond/jquery.ui.all.css">
<link rel="stylesheet" TYPE="text/css" HREF="main.css">
<script src="js/jquery.js" type="text/javascript"></script>
<script src="ui/jquery.ui.core.js" language="JavaScript"></script>
<script src="ui/jquery.ui.widget.js" language="JavaScript"></script>
<script src="ui/jquery.ui.button.js"></script>
<script type="text/javascript" src="https://maps.google.com/maps/api/js?sensor=false"></script>
</head>
<body onload="javascript:initialize();">
<h2>Eventos IMEI <%= (String)request.getParameter("imei") %></h2>
<a href = "javascript:window.close()" id ="btn_close">Cerrar</a>
<table>
	<tr>
		<th>IMEI</th>
		<th>EVENTO</th>
		<th>FECHA</th>
	</tr>
	
	<% while (rs.next()) { %>
		<tr>
			<td><img src="images/ok.png"><%= rs.getString("IMEI") %></td>
			<td><%= rs.getString("EVENTO") %></td>
			<td><%= rs.getString("FECHA") %></td>
		</tr>
	
	<% } %>
</table>

</body>
</html>
<%
	conexion.Close();
%>

