<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ include file="validausuario.jsp"%>

<%
	db conexion = new db();
	ManejadorArchivo ma = new ManejadorArchivo(conexion);
	String usuario = (String)session.getAttribute("usuario");
	ArrayList<Archivo> lista = new ArrayList<Archivo>();
	if (request.getParameter("todos") != null) {
		lista = ma.listUser(usuario);	
	}else {
		lista = ma.listUserToday(usuario);
	}
	
	
	String operacion = (String)request.getParameter("operacion");
	int contador=0;

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>MIS ARCHIVOS</title>
<link rel="stylesheet" href="themes/redmond/jquery.ui.all.css">
<LINK REL="stylesheet" TYPE="text/css" HREF="main.css">
<script src="js/jquery.js" language="JavaScript"></script>
<script src="ui/jquery.ui.core.js" language="JavaScript"></script>
<script src="ui/jquery.ui.widget.js" language="JavaScript"></script>
<script src="ui/jquery.ui.button.js"></script>

<script type="text/javascript">
$(function() {
	$( "input:submit, a, button", ".demo" ).button();
	$( "input:button, a, button", ".demo" ).button();
});
</script>
</head>
<body>
	<%@ include file="header.jsp"%>
	<div class="contencenter demo">
	<h2>MIS ARCHIVOS</h2>
	<a href="misarchivos.jsp?operacion=<%= operacion %>">Hoy</a><a href="misarchivos.jsp?operacion=<%= operacion %>&todos=true">Todos</a>
	<table>
		<tr>
			<th>Fecha</th>
			<th>Archivo</th>
			<th>Accion</th>
		</tr>
		<% for (Archivo archivo : lista)  {%>
		<tr <%= contador%2==0?"class='odd'":"" %>>
			<td><%= archivo.getFecha() %></td>
			<td><%= archivo.getFilename() %></td>
			<td>
				<% if (operacion.equals("procesar")) { %>
					<a href="procesar_ordenes.jsp?id=<%= archivo.getId() %>">Procesar Ordenes</a>
				<% } %>
				<% if (operacion.equals("lote")) { %>
					<a href="procesar_lote.jsp?id=<%= archivo.getId() %>">CARGAR LOTE</a>
				<% } %>
				<% if (operacion.equals("asignar")) { %>
					<a href="procesar_asignacion.jsp?id=<%= archivo.getId() %>">Asignacion Masiva</a>
				<% } %>
				<% if (operacion.equals("asignarLote")) { %>
					<a href="procesar_asignacionLote.jsp?id=<%= archivo.getId() %>">Asignacion Masiva</a>
				<% } %>
                                <% if (operacion.equals("plantilla")) { %>
					<a href="procesar_plantilla.jsp?id=<%= archivo.getId() %>">Plantilla</a>
				<% } %>
				<% if (operacion.equals("pagos")) { %>
					<a href="procesar_pagos.jsp?id=<%= archivo.getId() %>">Pagos</a>
				<% } %>
				<% if (operacion.equals("pagos_lote")) { %>
					<a href="procesar_pagos_lote.jsp?id=<%= archivo.getId() %>">Pagos</a>
				<% } %>
				<% if (operacion.equals("ruta")) { %>
					<a href="procesar_ruta.jsp?id=<%= archivo.getId() %>">Generar Ruta</a>
					<a href="procesar_ruta_mapa.jsp?id=<%= archivo.getId() %>">Generar Mapa</a>
				<% } %>
			</td>
		</tr>
		<% contador++; %>
		<% } %>
	</table>
	Total archivos: <%= lista.size() %>
	</div>
	<%@ include file="foot.jsp"%>
</body>
</html>
<%
	if (conexion != null ){
		conexion.Close();
	}
	
%>