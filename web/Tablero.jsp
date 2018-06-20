<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="validausuario.jsp"%>
<%@ page import="com.are.sofatec.*" %>
<%@ page import="java.sql.*" %>

<%
	db conexion = new db();
	departamento dpto = new departamento(conexion);
	ResultSet rs = dpto.list();
	boolean rsIsEmpty = !rs.next();
	
	
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
		var url = "SrvTablero?departamento="  + $("#departamento").val() + "&ciudad=" + $("#localidad").val() + "&fecha=" + $("#fecha").val() ;
		$("#list").load(url);
	}
	
	
	
	function cargar_localidad(combo) {
		var url = "srvLocalidad?operacion=combo&departamento=" + combo.value;
		$("#municipio").load(url);
	}
	function VerOrdenesRecurso(recurso,fecha,ciudad) {
		url = "OrdenesEjecutadasRecurso.jsp?recurso=" + recurso + "&fecha=" + fecha + "&ciudad=" + ciudad;
		window.open(url , "OrdenesEjecutadasRecurso" , "width=600,height=500,scrollbars=YES,menubar=No,toolbar=NO,status=YES"); 
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
			<th colspan="6">FILTRO</th>
		</tr>
		<tr>
			<td>Departamento</td>
			<td>
				<select id="departamento" onchange="javascript:cargar_localidad(this);">
					<option value="">Seleccionar departamento</option>
					<%  if (!rsIsEmpty) { %>
					<% do { %>
					<option value="<%= rs.getString("depacodi") %>"><%= rs.getString("depadesc") %></option>
					<% }while(rs.next()); %>
					<% } %>
				</select>
			</td>
			<td>Municipio</td>
			<td><div id="municipio">
				<select id="localidad">
					<option value="">Seleccionar</option>
				</select>
			</div></td>
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