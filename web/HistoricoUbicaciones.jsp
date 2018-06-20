<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ include file="validausuario.jsp"%>
<%@ page import="com.are.sofatec.*" %>
<%@ page import="java.sql.*" %>
<%
	db conexion = null;
	ResultSet rs = null;
	boolean rsIsEmpty = false;
	if (request.getParameter("cedula") != null) {
		String cedula = (String)request.getParameter("cedula");
		String fecha = (String)request.getParameter("fecha");
		conexion = new db();
		String sql = "select gps.*, recurso.recunomb from gps,recurso where recurso = ? and recurso = recucodi and date(fecha_sistema) = ? order by fecha_sistema";
		PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		pst.setString(1, cedula );
		pst.setString(2, fecha );
		rs = conexion.Query(pst);
		rsIsEmpty = !rs.next();
	}

%>
<html>
<head>
<title>HISTORICO UBICACIONES</title>
<link rel="stylesheet" href="themes/redmond/jquery.ui.all.css">
<LINK REL="stylesheet" TYPE="text/css" HREF="main.css">
<script src="js/jquery.js"></script>
<script src="ui/jquery.ui.core.js"></script>
<script src="ui/jquery.ui.widget.js"></script>
<script src="ui/jquery.ui.button.js"></script>
<script src="ui/jquery.effects.core.js"></script>
<script src="ui/jquery.effects.slide.js"></script>
<script src="ui/jquery.ui.datepicker.js"></script>
<script src="ui/jquery.effects.explode.js"></script>
<script src="ui/jquery.effects.fold.js"></script>
<script src="ui/jquery.effects.slide.js"></script>
<script type="text/javascript">
$(function() {
	$( "input:submit, a, button", ".demo" ).button();
	$( "input:button, a, button", ".demo" ).button();
	
	$( ".fecha" ).datepicker({
		showOn: "button",
		buttonImage: "images/calendar.gif",
		buttonImageOnly: true,
		dateFormat: "yy-mm-dd"
	});
	$( ".fecha" ).datepicker( "option", "showAnim", "slide" );
});
</script>

<script type="text/javascript">

	function buscar() {
		url = "BuscarRecurso.jsp";
		window.open(url , "BuscarRecurso" , "width=600,height=500,scrollbars=YES,menubar=No,toolbar=NO,status=YES"); 
	}
	
	function VerMapa(latitud,longitud) {
		var url ="UbicacionRecurso.jsp?latitud=" + latitud+"&longitud=" + longitud;
		window.open(url , "UbicacionRecurso" , "width=800,height=500,scrollbars=YES,menubar=No,toolbar=NO,status=YES");
		
	}
	
</script>
</head>
<body onload="initialize()">
<%@ include file="header.jsp" %>
<div class="contencenter demo">
<h2>HISTORICO UBICACIONES</h2>
<div id="info"></div>
<form action="" name="form1">
	<table>
		<tr>
			<th colspan="6">Seleccion Recurso</th>
		</tr>
		<tr>
			<td>Recurso</td>
			<td><input type="text" name="cedula" id="cedula" readonly> </td>
			<td>Nombre</td>
			<td><input type="text" name="nombre" id="nombre" size=30 readonly></td>
			<td>Fecha</td>
			<td><input type="text" class="fecha" name="fecha" id="fecha" readonly value="<%= Utilidades.strDateServer()  %>"></td>
		</tr>
	</table>
	<input type="button" name="cmd_buscar" id="cmd_buscar" value="Buscar Recurso" onclick="javascript:buscar()" > <input type="submit" name="cmd_buscar" value="Consultar" >
</form>
<% if (request.getParameter("cedula") != null) { %>
<table>
	<tr>
		<th>Recuro</th>
		<th>IMEI</th>
		<th>Fecha</th>
		<th>Latitud</th>
		<th>Longitud</th>
		<th>Accion</th>
	</tr>
<% if (!rsIsEmpty) { %>
<% do { %>
	<tr>
		<td><%= rs.getString("recunomb") %></td>
		<td><img src="images/device.png" width=16 height=16><%= rs.getString("imei") %></td>
		<td><%= rs.getString("fecha_sistema") %></td>
		<td><%= rs.getString("latitud") %></td>
		<td><%= rs.getString("longitud") %></td>
		<td><a href="javascript:VerMapa('<%= rs.getString("latitud") %>','<%= rs.getString("longitud") %>')">Mapa</a></td>
	</tr>
<% }while(rs.next()); %>
<% } %>
</table>
<% } %>
</div>
</body>
</html>
<%
	if (conexion != null) {
		conexion.Close();
		
	}

%>