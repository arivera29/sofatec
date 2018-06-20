<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="validausuario.jsp"%>
<%@ page import="com.are.sofatec.*" %>

<%
	db conexion = new db();
	String recurso = (String)request.getParameter("recurso");
	
	recursohumano rh = new recursohumano(conexion);
	boolean existe = rh.Find(recurso);
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Informacion recurso</title>
<LINK REL="stylesheet" TYPE="text/css" HREF="main.css">
<script src="js/prototype.js" language="JavaScript"> </script>
<link rel="Shortcut Icon" href="icono_tm.ico" type="image/x-icon" />
</head>
<body>
<img src="images/logo_mobile.png">
<h2>Recurso</h2>
<% if (existe) { %>
<table>
	<tr>
		<th colspan="2">Información Recurso Humano</th>
	</tr>
	<tr>
		<td>Cedula</td>
		<td><%= rh.getCodigo() %></td>
	</tr>
	<tr>
		<td>Nombre</td>
		<td><%= rh.getNombre() %></td>
	</tr>
	<tr>
		<td>Direccion</td>
		<td><%= rh.getDireccion() %></td>
	</tr>
	<tr>
		<td>Telefono</td>
		<td><%= rh.getDireccion() %></td>
	</tr>
	<tr>
		<td>Correo</td>
		<td><%= rh.getCorreo() %></td>
	</tr>

</table>
<% } else { %>
El recurso <%= recurso %> no se encuentra en la base de datos.  Verifique porfavor.
<% } %>
</body>
</html>