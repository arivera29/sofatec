<%@page import="com.are.sofatec.db"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="validausuario.jsp"%>
<%@ page import="java.sql.*"%>
<%
	db conexion = new db();
	
	String sql = "select * from sitios order by nombre";
	PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
	ResultSet rs = conexion.Query(pst);
	int count=0;
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Sitios</title>
<link rel="stylesheet" href="themes/redmond/jquery.ui.all.css">
<link rel="stylesheet" TYPE="text/css" HREF="main.css">
<script src="js/jquery.js" type="text/javascript"></script>
<script src="ui/jquery.ui.core.js" language="JavaScript"></script>
<script src="ui/jquery.ui.widget.js" language="JavaScript"></script>
<script src="ui/jquery.ui.button.js"></script>
<script>
$(function() {
	$( "input:submit, a, button", ".demo" ).button();
	$( "input:button, a, button", ".demo" ).button();
});

function AgregarSitio() {
	var url ="AgregarSitio.jsp";
	window.open(url , "AgregarSitio" , "width=800,height=600,scrollbars=YES,menubar=No,toolbar=NO,status=YES");
	
}

function ActualizarSitio(sitio) {
	var url ="ActualizarSitio.jsp?sitio=" + sitio;
	window.open(url , "ActualizarSitio" , "width=800,height=600,scrollbars=YES,menubar=No,toolbar=NO,status=YES");
	
}

</script>
</head>
<body>

<%@ include file="header.jsp"%>
<div class="contencenter demo">
<h2>Sitios</h2>
<a href="javascript:AgregarSitio()">Agregar Sitio</a>
<table>
	<tr>
		<th>Id</th>
		<th>Nombre</th>
		<th>Descripcion</th>
		<th>Latitud</th>
		<th>Longitud</th>
		<th>Fecha Creacion</th>
		<th>Usuario creacion</th>
		<th></th>
	</tr>
	<% while (rs.next()) { %>
		<tr>	
			<td><%= rs.getString("id") %></td>
			<td><%= rs.getString("nombre") %></td>
			<td><%= rs.getString("descripcion") %></td>
			<td><%= rs.getString("latitud") %></td>
			<td><%= rs.getString("longitud") %></td>
			<td><%= rs.getString("fecha_creacion") %></td>
			<td><%= rs.getString("usuario_creacion") %></td>
			<td>
				<a href="javascript:ActualizarSitio('<%= rs.getString("id") %>')">Editar</a>
				<a href="javascript:Eliminar('<%= rs.getString("id") %>')">Eliminar</a>
			
			</td>
		</tr>
		<% count++; %>
	<% } %>
	<tr>
		<td colspan="8">Total Sitios: <%= count %></td>
	</tr>
</table>


</div>

</body>
</html>
<%
	conexion.Close();
%>
