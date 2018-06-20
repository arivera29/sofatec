<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%-- <%@ page import="com.are.sofatec.*"%> --%>
<%@include file="validausuario.jsp"%>
<%
	db conexion = new db();
	String localidad = (String)request.getParameter("localidad");
	
	localidad l = new localidad(conexion);
	if (!l.Find(localidad)) {
		conexion.Close();
		response.sendRedirect("localidades.jsp");
		return;
	}
	
	
	

%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Barrios</title>
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

<script type="text/javascript">
	function lista(localidad) {
		$("#lista").load("SrvBarrios?operacion=list&localidad=" + localidad, function() {
			$( "input:submit, a, button", ".demo" ).button();
		});
	}
	
	function add(localidad) {
		var nombre = $("#nombre").val();
		var peso = $("#peso").val();
		
		if (nombre == "") {
			alert("Debe ingresar el nombre del barrio");
			return;
		}
		
		var url = "SrvBarrios";
		$.post(url, {
			operacion: "add",
			nombre: nombre,
			localidad : localidad,
			peso : peso
		}, procesar);
	}

	function procesar(result) {
		if (result == "OK") {
			$("#info").html("Barrio creado correctamente");
			lista('<%= (String)request.getParameter("localidad") %>');  // actualizar lista
			$("#nombre").val("");
			$("#peso").val("0");
		}else {
			$("#info").html(result);
		}
	}
	
	function eliminar(id) {
		if (confirm("Esta seguro de eliminar el barrio id " + id)) {
			var url = "SrvBarrios";
			$.post(url, {
				operacion: "remove",
				id : id
			}, procesarEliminar);
		}
	}
	
	function procesarEliminar(result) {
		if (result == "OK") {
			$("#info").html("Barrio eliminado correctamente");
			lista('<%= (String)request.getParameter("localidad") %>');  // actualizar lista
		}else {
			$("#info").html(result);
		}
	}
	
</script>
</head>
<body onload="lista('<%= (String)request.getParameter("localidad") %>');">
<%@include file="header.jsp"%>
<div class="contencenter demo">
<h2>Barrios</h2>
<div id="info"></div>
<form action="" name="form1">
	<table>
		<tr>
			<th colspan="2">Informacion</th>
		</tr>
		<tr>
			<td>Localidad</td>
			<td><%= l.getCodigo() %> <%= l.getDescripcion() %></td>
		</tr>
	
		<tr>
			<td>Nombre</td>
			<td><input type="text" value="" size = "40" name="nombre" id="nombre" /></td>
		</tr>
		<tr>
			<td>Peso</td>
			<td><input type="text" value="0" size = "40" name="peso" id="peso" /></td>
		</tr>
		<tr>
			<td colspan="2"><input type="button" value="Agregar" name="cmd_agregar" id="cmd_agregar" onclick="javascript:add('<%= l.getCodigo() %>')" /></td>
		</tr>
	</table>
</form>
<div id="lista"></div>
</div>
<%@include file="foot.jsp"%>
</body>
</html>
<%
	conexion.Close();
%>