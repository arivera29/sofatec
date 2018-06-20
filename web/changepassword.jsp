<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="validausuario.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Cambiar Password</title>
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

<script language="javascript">

function changepassword(usuario) {
	var clave = $("#clave").val();
	var clave2 = $("#re_clave").val();
	
	if (clave != clave2) {
		alert("Las claves ingresadas son diferentes");
		return
	}
	
	var url = "SrvUsuarios?operacion=changepassword&usuario="+encodeURIComponent(usuario) + "&clave=" + encodeURIComponent(clave);
	$.get(url,procesar);
}
function procesar(resultado) {
	if (resultado != 'OK') {
		document.getElementById("info").innerHTML = resultado;
	}else {
		alert("Clave cambiada correctamente");
		window.location.href = "main.jsp";
	}
}
</script>
</head>
<body>
<%@ include file="header.jsp"%>
<div class="contencenter demo">
<h2>CAMBIAR CLAVE</h2>

<div id="info" class="warning"></div>
<form action="" method="POST">
	<table>
		<tr>
			<th colspan=2>Datos de Usuario</th>
		</tr>
		<tr>
			<td>Usuario</td>
			<td><%= (String)session.getAttribute("usuario") %></td>
		</tr>
		<tr>
		<td>Nueva Clave</td>
		<td><input type="password" id="clave" name="clave"> 
		</td>
	</tr>
	<tr>
		<td>Repetir Clave</td>
		<td><input type="password" id="re_clave" name="re_clave"> </td>
	</tr>
	<tr>
		<td colspan=2><a href="javascript:changepassword('<%= (String)session.getAttribute("usuario") %>');">Cambiar</a></td>
	</tr>
	</table>

</form>
</div>
<%@ include file="foot.jsp"%>
</body>
</html>