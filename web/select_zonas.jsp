<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="validausuario.jsp"%>
<%@ page import="com.are.sofatec.*"%>
<%@ page import="java.sql.*"%>
<%
	db conexion = new db();
	String sql = "select zonacodi,zonadesc,zonaproy,proydesc from zonas,proyectos where zonaproy=proycodi order by proydesc,zonadesc";
	ResultSet rs = conexion.Query(sql);
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Seleccionar Zonas</title>
<LINK REL="stylesheet" TYPE="text/css" HREF="main.css">
<link rel="Shortcut Icon" href="icono_tm.ico" type="image/x-icon" />
</head>
<body>
	<%@ include file="header.jsp"%>
	
	<h2>Zonas</h2>
	Para configurar las localidades por zona se debe seleccionar primero la zona de gestión
	<table id="lista" border=0>
		<tr>
			<th>Proyecto</th>
			<th>Codigo</th>
			<th>Descripcion</th>
			<th>Acción localidades</th>
		</tr>
		
		<% while (rs.next()) { %>
		<tr>
			<td><%= rs.getString("proydesc") %></td>
			<td><%= rs.getString("zonacodi") %></td>
			<td><%= rs.getString("zonadesc") %></td>
			<td><a href="zonaloca.jsp?zona=<%= rs.getString("zonacodi") %>&operacion=add" class="small button blue">Agregar</a> <a href="zonaloca.jsp?zona=<%= rs.getString("zonacodi") %>&operacion=delete" class="small button blue">Eliminar</a></td>
		</tr>
		<% } %>
		<tr>
			<td colspan="4"><a href="menu.jsp?menu=1" class="small button yellow">Regresar</a></td>
		</tr>
	</table>

	<%@ include file="foot.jsp"%>
</body>
</html>
<%
	conexion.Close();
%>