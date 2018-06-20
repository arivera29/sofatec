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
	TipoOrden tipo = new TipoOrden(conexion);
	ResultSet rsTipo = tipo.List();
	
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Generar archivo de cierres</title>
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

<script type="text/javascript" language="javascript">
	function cargar_localidad(combo) {
		var url = "srvLocalidad?operacion=combo&departamento=" + combo.value;
		$("#municipio").load(url);
	}
</script>
</head>
<body>
<%@ include file="header.jsp" %>
<div class="contencenter demo">
<h2>GENERAR ARCHIVO DE CIERRES</h2>
<div id="info"></div>
<form action="SrvGetFileOrders" name="form2">
	<table>
		<tr>
			<th colspan="6">FILTRO</th>
		</tr>
		<tr>
			<td>Departamento <input type="hidden" value="list" name="operacion" /></td>
			<td>
				<select id="departamento" onchange="javascript:cargar_localidad(this);" name="departamento">
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
					<option value="-1">Seleccionar</option>
				</select>
			</div></td>
			<td>Tipo de Orden</td>
			<td>
				<select id="tipo" name="tipo">
					<option value="all">Todos</option>
					
					<% while(rsTipo.next()) { %>
					<option value="<%= rsTipo.getString("tiorcodi") %>"><%= rsTipo.getString("tiordesc") %></option>
					<% } %>

				</select>
			</td>
		</tr>
		<tr>
			<td>Fecha inicial</td>
			<td><input type="text" class="fecha" name="fecha1" id="fecha1" readonly value="<%= Utilidades.strDateServer()  %>"></td>
			<td>Hora:<input type="text" name="hora1" id="hora1" size=10 value="00:00:00"></td>
			<td>Fecha Final</td>
			<td><input type="text" class="fecha" name="fecha2" id="fecha2" readonly value="<%= Utilidades.strDateServer()  %>"></td>
			<td>Hora:<input type="text" name="hora2" id="hora2" size=10 value="<%= Utilidades.strTimeServer() %>"></td>
		</tr>
		<tr>
			<td colspan="6"> <input type="submit" name="cmd_download" value="Descargar" > </td>
		</tr>
	</table>
	
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