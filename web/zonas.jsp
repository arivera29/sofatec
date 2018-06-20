<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="validausuario.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Zonas de Gestion</title>
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
	function agregar() {
		var nombre = $("#nombre").val();
		if (nombre == "") {
			alert("falta ingresar informacion");
			return;
		}
		
		var url = "SrvZonas";
		$.post(url,{
			operacion: "add",
			nombre : nombre
		},procesar);
		
		
	}
	function procesar(result) {
		if (result != 'OK') {
			$("#info").html("<img src='images/alerta.gif'> " + resultado);
		}else {
			list();
			$("#info").html("<img src='images/alerta.gif'> Zona agregada correctamente");
			$("#nombre").val("");
			$("#nombre").focus();
		}
	}

	function list() {
		var url = "SrvZonas?operacion=list";
		$("#list").load(url,function() {
			$( "input:submit, a, button", ".demo" ).button();
		});
	}
</script>
</head>
<body onload="list()">
<%@ include file="header.jsp" %>
<div class="contencenter demo">
<H2>Registro de Zonas</H2>
<form action="" name="form1">
		<table id="lista" border=0>
		<tr>
			<th colspan="2">Información Zona</th>
		</tr>
		<tr>
			<td>Nombre</td>
			<td><input type="text" name="nombre" id="nombre" size=40></td>
		</tr>
		<tr>
			<td colspan="2"><input type="button" onclick="javascript:agregar();" value="Agregar" id="cmd_agregar" name="cmd_agregar"  ></td>
		</tr>
		</table>
	</form>
	<div id="info"></div>
	<div id="list"></div>
	
	</div>
	<%@ include file="foot.jsp" %>
</body>
</html>
