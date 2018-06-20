<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ include file="validausuario.jsp"%>
<%@ page import="com.are.sofatec.*" %>
<%
	if (request.getParameter("codigo") == null) {
		response.sendRedirect("localidades.jsp");
		return;
	}

	String codigo = (String)request.getParameter("codigo");
	db conexion = new db();
	departamento dpto = new departamento(conexion);
	
	localidad loc = new localidad(conexion);
	loc.setKey(codigo);
	loc.exist();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>CIUDADES</title>
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
<script type="text/javascript" language="javascript">
	function modificar(key) {
		var codigo = $("#codigo").val();
		var descripcion = $("#descripcion").val();
		var departamento = $("#departamento").val();
		var latitud = $("#latitud").val();
		var longitud = $("#longitud").val();
		
		if (codigo == "" || descripcion == "" || departamento == "") {
			alert("falta ingresar informacion");
			return;
		}
			var url = "srvLocalidad"; 
			$.post(url,{
				operacion : "modify",
				codigo : codigo,
				descripcion : descripcion,
				departamento : departamento,
				latitud : latitud,
				longitud : longitud,
				key : key
			},procesar);
	}
	
	function eliminar(key) {
		if (confirm("Esta seguro de eliminar la Ciudad ID=" + key)) {
			var url = "srvLocalidad"; 
			$.post(url,{
				operacion : "remove",
				codigo : key
			},ProcesarEliminar);

		}
	}
	function procesar(resultado) {
		if (resultado != 'OK') {
			$("#info").html( "<img src=\"warning.jpg\">" + resultado);
		}else {
			alert("Ciudad modificada");
			window.location.href = "localidades.jsp";
		}
	}
	function ProcesarEliminar(resultado) {
		if (resultado != 'OK') {
			$("#info").html("<img src=\"warning.jpg\">" + resultado);
		}else {
			alert("Ciudad eliminada");
			window.location.href = "localidades.jsp";
		}
	}
	function cancelar() {
		window.location.href="localidades.jsp";
	}

</script>
</head>
<body onload="lista()">
<%@ include file="header.jsp" %>
<div class="contencenter demo">
<h2>MODIFICAR CIUDAD</h2>
<div id="info"></div>
<form action="" name="form1">
		<table id="lista" border=0>
		<tr>
			<th colspan="2">DATOS</th>
		</tr>
		<tr>
			<td>Id</td>
			<td><input type="text" name="codigo" id="codigo" value ="<%= loc.getCodigo() %>" disabled></td>
		</tr>
		<tr>
			<td>Descripcion</td>
			<td><input type="text" name="descripcion" id="descripcion" size=40 value ="<%= loc.getDescripcion() %>"></td>
		</tr>
		<tr>
			<td>Departamento</td>
			<td><%= dpto.CreateSelectHTML("departamento",loc.getDepartamento()) %></td>
		</tr>
		<tr>
			<th colspan="2">COORDENADAS PUNTO INICIAL SALIDA TECNICOS</th>
		</tr>
		<tr>
			<td>Latitud</td>
			<td><input type="text" name="latitud" id="latitud" size=40 value ="<%= loc.getLatitud() %>"></td>
		</tr>
		<tr>
			<td>Longitud</td>
			<td><input type="text" name="longitud" id="longitud" size=40 value ="<%= loc.getLongitud() %>"></td>
		</tr>
		</table>
		<input type="button" onclick="javascript:modificar('<%= (String)request.getParameter("codigo") %>');" value="Modificar" id="cmd_modificar" name="cmd_modificar" > 
		<input type="button" onclick="javascript:eliminar('<%= (String)request.getParameter("codigo") %>');" value="Eliminar" id="cmd_eliminar" name="cmd_eliminar" >  
		<input type="button" name="cmd_cancelar" id="cmd_cancelar" value ="Cancelar" onclick="javascript:cancelar()" >
	</form>
</div>
	<%@ include file="foot.jsp" %>
</body>
</html>
<%
	conexion.Close();
%>