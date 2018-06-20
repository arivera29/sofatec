<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="validausuario.jsp"%>
<%@ page import="com.are.sofatec.*" %>
<%@ page import="java.sql.*" %>

<%
	db conexion = new db();
	ManejadorZonas manejador = new ManejadorZonas(conexion);
	ArrayList<zonas> lista = manejador.List();
	
	
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>RENDIMIENTOS</title>
<link rel="stylesheet" href="themes/redmond/jquery.ui.all.css">
<LINK REL="stylesheet" TYPE="text/css" HREF="main.css">
<script src="js/jquery.js" language="JavaScript"></script>
<script src="ui/jquery.ui.core.js" language="JavaScript"></script>
<script src="ui/jquery.ui.widget.js" language="JavaScript"></script>
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
	function consultar() {
		$("#list").html("<img src='images/loading.gif'> Generando consulta, Espere porfavor...")
		var url = "SrvTableroLote?zona="  + $("#zona").val() + "&fecha=" + $("#fecha").val() ;
		$("#list").load(url);
	}
	
	
	function VerOrdenesRecurso(recurso,fecha,zona) {
		url = "DetalleRendiemientoRecurso.jsp?recurso=" + recurso + "&fecha=" + fecha + "&zona=" + zona;
		window.open(url , "DetalleRendiemientoRecurso" , "width=800,height=500,scrollbars=YES,menubar=No,toolbar=NO,status=YES"); 
	}
</script>
</head>
<body>
<%@ include file="header.jsp" %>
<div class="contencenter demo">
<h2>RENDIMIENTO</h2>
<div id="info"></div>
<form action="" name="form1">
	<table>
		<tr>
			<th colspan="4">FILTRO</th>
		</tr>
		<tr>
			<td>Zona</td>
		<td>
			<select name="zona" id="zona">
				<option value="all">Todos</option>
				<% for (zonas zona : lista) { %>
				<option value="<%= zona.getId() %>"><%= zona.getNombre() %></option>
				<% } %>
			</select> 
		</td>
			<td>Fecha</td>
			<td><input type="text" class="fecha" name="fecha" id="fecha" readonly value="<%= Utilidades.strDateServer()  %>"></td>
		</tr>
	</table>
	<input type="button" name="cmd_buscar" value="Consultar" onclick="javascript:consultar()" >
</form>
<div id="list"></div>
</div>
<%@ include file="foot.jsp" %>
</body>
</html>
<%
	if (conexion != null) {
		conexion.Close();
	}
%>