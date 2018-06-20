<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<%@ include file="validausuario.jsp"%>
<%@ page import="com.are.sofatec.*" %>
<%@ page import="java.util.*" %>
<%
	String userId = "";
	if (request.getParameter("usuario") == null) {
		response.sendRedirect("adduser.jsp");
		return;
	}
	String key = (String)request.getParameter("usuario");
	db conexion = new db();
	ManejadorUsuarios manejador = new ManejadorUsuarios(conexion);
	ArrayList<Perfiles> p = manejador.perfiles();
	
	if (!manejador.exist(key) ) {
		response.sendRedirect("adduser.jsp");
		return;
	}
	Usuarios usuario = manejador.getUsuario();
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>MODIFICAR USUARIO</title>
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
function modificar(key) {
	var usuario = $("#usuario").val();
	var nombre = $("#nombre").val();
	var clave = $("#clave").val();
	var clave2 = $("#re_clave").val();
	var perfil = $("#perfil").val();
	var estado = $("#estado").val();
	
	if (usuario == "" || nombre == "" || perfil == "" ) {
		$("#info").html("<img src=\"warning.jpg\">Falta ingresar informacion");
		return;
	}
	
	if (clave != "") {
		if (clave != clave2) {
			alert("Las contraseñas no son iguales, verifique porfavor");
			return;
		}
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
			operacion : "update",
			usuario : usuario,
			nombre: nombre,
			perfil: perfil,
			clave: clave,
			estado: estado,
			hda : hda,
			resolver : resolver,
			anomalias : anomalias,
			reportes : reportes,
			key:key
		},
		procesar);
	
	
}
function procesar(resultado) {
	if (resultado != 'OK') {
		$("#info").html(resultado);
	}else {
		$("#info").html("Usuario modificado");
	}
}
	
 	function generar_pswd() {
 		new Ajax.Updater("genpass","SrvGenPassword");
	}


</script>
</head>
<body>
<%@ include file="header.jsp"%>
<div class="contencenter demo">
<h2>Modificar Usuario</h2>
<div id="info"></div>
<form name="form1" action="">
<table>
	<tr>
		<th colspan="2">INFORMACION USUARIO</th>
	</tr>
	<tr>
		<td>Usuario</td>
		<td><input type="text" id="usuario" name="usuario" value="<%= usuario.getUsuario() %>"  readonly> </td>
	</tr>
	<tr>
		<td>Nombre</td>
		<td><input type="text" id="nombre" name="nombre" value="<%= usuario.getNombre() %>"> </td>
	</tr>
	<tr>
		<td>Perfil</td>
		<td>
		<select name="perfil" id="perfil">
		<option value="-1">Seleccionar</option>
		<% for (int i=0; i< p.size(); i++) { 
			Perfiles a =(Perfiles)p.get(i);
			
		%>
			<option value="<%= a.getId() %>" <%= a.getId().equals(usuario.getPerfil())?"selected":"" %> ><%= a.getPerfil() %></option>
		<% } %>
		</select></td>
	</tr>
	<tr>
		<td>Clave</td>
		<td><input type="password" id="clave" name="clave"> </td>
	</tr>
	<tr>
		<td>Repetir Clave</td>
		<td><input type="password" id="re_clave" name="re_clave"> </td>
	</tr>
	<tr>
			<td>Activo?</td>
			<td>
			<select id="estado" name="estado">
			<option value="1" <%= usuario.getEstado().equals("1")?"selected":"" %>>Si</option>
			<option value="0" <%= usuario.getEstado().equals("0")?"selected":"" %>>No</option>
			</select>
			</td>
		</tr>
	<tr>
			<td>Privilegios</td>
			<td>
				<input type="checkbox" value="1" id="hda" name="hda" <%= usuario.getHda()==1?"checked":"" %>> HDA
				<input type="checkbox" value="1" id="resolver" name="resolver" <%= usuario.getResolver()==1?"checked":"" %>> Resolver Ordenes
				<input type="checkbox" value="1" id="anomalias" name="anomalias" <%= usuario.getAnomalias()==1?"checked":"" %>> Anomalias
				<input type="checkbox" value="1" id="reportes" name="reportes" <%= usuario.getReportes()==1?"checked":"" %>> Reportes
			</td>
	</tr>
</table>
<a href="javascript:modificar('<%= (String)request.getParameter("usuario") %>');">Modificar</a> <a href="adduser.jsp">Cancelar</a>
</form>
</div>
<%@ include file="foot.jsp" %>
</body>
</html>

<%
	if (conexion != null) conexion.Close();
%>