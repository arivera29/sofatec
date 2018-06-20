<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="validausuario.jsp"%>
<%@ page import="com.are.sofatec.*" %>

<%
	db conexion = new db();
	String recurso = (String)request.getParameter("recurso");
	String ciudad = (String)request.getParameter("ciudad");
	String fecha = (String)request.getParameter("fecha");
	
	String sql = "select reportes.orden,tiordesc,locadesc,reportes.tipo,reportes.fecha " +
		" from reportes,orders,localidad,tipo_orden " +
		" where orders.orden = reportes.orden " +
		" and orders.tipo = tiorcodi " +
		" and orders.localidad = locacodi " +
		" and reportes.recurso = ? " +
		" and date(reportes.fecha) = ?" ;
	if (!ciudad.equals("-1")) {
		sql += " and orders.localidad = ?";
	}
			;
	java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
	pst.setString(1, recurso);
	pst.setString(2, fecha);
	if (!ciudad.equals("-1")) {
		pst.setString(3, ciudad);
	}
	
	java.sql.ResultSet rs = conexion.Query(pst);
	boolean rsIsEmpty = !rs.next();
	int cont=0;
	
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
<h2>Ordenes Ejecutadas</h2>
<table>
	<tr>
		<th>Municipio</th>
		<th>Orden</th>
		<th>Tipo orden</th>
		<th>Tipo Reporte</th>
		<th>Fecha Reporte</th>
		<th>Tiempo</th>
	</tr>
	<% if (!rsIsEmpty) { 
		java.sql.Timestamp anterior = rs.getTimestamp("fecha");
	%>
	
	<% do { 
		java.sql.Timestamp actual = rs.getTimestamp("fecha");
		
		
	%>
	<tr>
		<td><%= rs.getString("locadesc") %></td>
		<td><%= rs.getString("orden") %></td>
		<td><%= rs.getString("tiordesc") %></td>
		<td><%= rs.getString("tipo") %></td>
		<td><%= rs.getTimestamp("fecha") %></td>
		<td><%= Utilidades.diferenciaDates(anterior, actual) %></td>
	</tr>
	<% 
		anterior = rs.getTimestamp("fecha");
		cont++;
	}while(rs.next()); %>
	<tr>
		<td colspan="6">Total de ordenes: <%= cont %></td>
	</tr>
	<% } else { %>
	<tr>
		<td colspan="6">No se encontraron registros</td>
	</tr>	
	
	<% } %>

</table>
</body>
</html>
<%
	conexion.Close();
%>