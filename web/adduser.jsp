<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<%@ include file="validausuario.jsp"%>
<%@ page import="com.are.sofatec.*" %>
<%@ page import="java.util.*" %>

<%
	db conexion = new db();
	ManejadorUsuarios manejador = new ManejadorUsuarios(conexion);
	ArrayList<Perfiles> p = manejador.perfiles();
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>USUARIOS</title>
<link rel="stylesheet" href="themes/redmond/jquery.ui.all.css">
<LINK REL="stylesheet" TYPE="text/css" HREF="main.css">
<script src="js/jquery.js"></script>
<script src="ui/jquery.ui.core.js"></script>
<script src="ui/jquery.ui.widget.js"></script>
<script src="ui/jquery.ui.button.js"></script>

<script type="text/javascript">
$(function() {
	$( "input:submit, a, button", ".demo" ).button();
	$( "input:button, a, button", ".demo" ).button();
});
</script>
<script type="text/javascript">
function agregar() {
	var usuario = $("#usuario").val();
	var nombre = $("#nombre").val();
	var clave = $("#clave").val();
	var clave2 = $("#re_clave").val();
	var perfil = $("#perfil").val();
	var estado = $("#estado").val();
	
	if (usuario == "" || nombre == "" || perfil == "-1" || clave == "" || clave2 == "") {
		$("#info").html("<img src=\"alerta.gif\">Falta ingresar informacion");
		return;
	}
	
	if (clave2 != clave) {
		$("#info").html("<img src=\"alerta.gif\">Las contraseñas deben ser iguales");
		return;
	}
	var hda = 0;
	if($("#hda").is(':checked')) {
		hda = 1;
	}
	var resolver = 0;
	if($("#resolver").is(':checked')) {
		resolver = 1;
	}
	var anomalias = 0;
	if($("#anomalias").is(':checked')) {
		anomalias = 1;
	}
	var reportes = 0;
	if($("#reportes").is(':checked')) {
		reportes = 1;
	}
	
		var url = "SrvUsuarios";
		$.post(url,{
			operacion : "add",
			usuario : usuario,
			nombre: nombre,
			perfil: perfil,
			clave: clave,
			estado: estado,
			hda : hda,
			resolver : resolver,
			anomalias : anomalias,
			reportes : reportes
		},
		procesar);
	
	
}
function procesar(resultado) {
	if (resultado != 'OK') {
		$("#info").html(resultado);
	}else {
		$("#info").html("Usuario agregado");
		$("#usuario").val("");
		$("#nombre").val("");
		$("#clave").val("");
		$("#re_clave").val("");
		lista();
	}
}
	
 	
 	
 	function buscar() {
 		var criterio = $("#criterio").val();
 		if (criterio == "") {
 			alert("Debe ingresar el criterio de busqueda");
 			return;
 		}
 		$("#list").load("SrvUsuarios?operacion=list&criterio=" + criterio, function() {
 			$( "input:button, a, button", ".demo" ).button();
 		});
 	}
 	
 	function lista() {
 		$("#list").load("SrvUsuarios", {
 			operacion : "list"
 		}, function (data) {
 			$( "input:button, a, button", ".demo" ).button();
 		});
 	}


</script>
</head>
<body onload="javascript:lista();">
<%@ include file="header.jsp"%>
<div class="contencenter demo">
<h2>Agregar Usuario</h2>
<div id="info"></div>
<form action="adduser.jsp?operacion=add" method="POST" id="form1">
<table>
	<tr>
		<th colspan="2">NUEVO USUARIO</th>
	</tr>
	<tr>
		<td>Usuario</td>
		<td><input type="text" id="usuario" name="usuario"> </td>
	</tr>
	<tr>
		<td>Nombre</td>
		<td><input type="text" id="nombre" name="nombre"> </td>
	</tr>
	<tr>
		<td>Perfil</td>
		<td>
		<select name="perfil" id="perfil">
		<option value="-1">Seleccionar</option>
		<% for (int i=0; i< p.size(); i++) { 
			Perfiles a =(Perfiles)p.get(i);
			
		%>
			<option value="<%= a.getId() %>"><%= a.getPerfil() %></option>
		<% } %>
		</select></td>
	</tr>
	<tr>
		<td>Clave</td>
		<td><input type="password" id="clave" name="clave"></td>
	</tr>
	<tr>
		<td>Repetir Clave</td>
		<td><input type="password" id="re_clave" name="re_clave"> </td>
	</tr>
	<tr>
			<td>Activo?</td>
			<td>
			<select id="estado" name="estado">
			<option value="1">Si</option>
			<option value="0">No</option>
			</select>
			</td>
	</tr>
	<tr>
			<td>Privilegios</td>
			<td>
				<input type="checkbox" value="1" name="hda" id="hda"> HDA
				<input type="checkbox" value="1" name="resolver" id="resolver"> Resolver Ordenes
				<input type="checkbox" value="1" name="anomalia" id="anomalia"> Anomalias
				<input type="checkbox" value="1" name="reportes" id="reportes"> Reportes
			</td>
	</tr>
</table>
<a href="javascript:agregar();">Agregar</a> 
</form>
<h2>Buscar Usuario</h2>
<form name="form2" action="">
Criterio de busqueda: <input type="text" name="criterio" id="criterio" value="" size="40"> <input type="button" onclick="javascript:buscar();" value="Buscar">
</form>
<div id="list"></div>
</div>
<%@ include file="foot.jsp" %>
</body>
</html>

<%
	if (conexion != null) conexion.Close();
%>