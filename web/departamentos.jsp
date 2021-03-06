<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="validausuario.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>DEPARTAMENTOS</title>
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
		var codigo = $("#codigo").val();
		var descripcion = $("#descripcion").val();
		if (codigo == "" || descripcion == "") {
			alert("falta ingresar informacion");
			return;
		}
			var url = "srvDepartamento";
			$.post(url,{
				operacion : "add",
				codigo : codigo,
				descripcion : descripcion
			},procesar);
		
		
	}
	
	function procesar(resultado) {
		if (resultado != 'OK') {
			$("#info").html(resultado);
		}else {
			$("#info").html("Departamento agregado");
			$("#codigo").val("");
			$("#descripcion").val("");
			list();
		}
	}

	function list() {
		$("#list").load("srvDepartamento?operacion=list",function() {
			$( "input:submit, a, button", ".demo" ).button();
		});
	}
</script>
</head>
<body onload="list()">

<%@ include file="header.jsp" %>
<div class="contencenter demo">
<H2>Registro departamentos</H2>
<div id="info"></div>
<form action="" name="form1">
		<table id="lista" border=0>
		<tr>
			<th colspan="2">INFORMACION DEPARTAMENTO</th>
		</tr>
		<tr>
			<td>Id</td>
			<td><input type="text" name="codigo" id="codigo"></td>
		</tr>
		<tr>
			<td>Descripcion</td>
			<td><input type="text" name="descripcion" id="descripcion" size=40></td>
		</tr>
		<tr>
			<td colspan="2"></td>
		</tr>
		</table>
		<input type="button" onclick="javascript:agregar();" value="Agregar" id="cmd_agregar" name="cmd_agregar"  >
	</form>
	
	<div id="list"></div>
	</div>
	<%@ include file="foot.jsp" %>
</body>
</html>