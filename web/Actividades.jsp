<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="validausuario.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>ACTIVIDADES</title>
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
		var descripcion =  $("#descripcion").val();
		var activo =  $("#activo").val();
		var valor =  $("#valor").val();
		var accion =  $("#accion").val();

		if (codigo == "" || descripcion == "") {
			alert("Falta ingresar informacion");
			return;
		}
			
			var cmd = document.getElementById("cmd_agregar");
			cmd.disabled = true;
			$("#info").html("<img src=\"images/loading.gif\" >Procesando solicitud");
			$.post(
				"SrvActividades",
				{
					operacion: "add",
					codigo: codigo,
					descripcion: descripcion,
					valor: valor,
					accion: accion,
					activo: activo
				},
				procesar
			
			);
	
	}
	
	function procesar(resultado) {
		var cmd = document.getElementById("cmd_agregar");
		cmd.disabled = false;
		if (resultado != 'OK') {
			$("#info").html("<img src=\"warning.jpg\">" + resultado);
		}else {
			$("#info").html("<img src=\"images/ok.png\">Actividad agregada correctamente");
			$("#codigo").val("");
			$("#descripcion").val("");
			$("#valor").val("0");
			document.getElementById("codigo").focus();
			list();
		}
		
	}

	function list() {
		$("#list").load("SrvActividades?operacion=list",function () {
			$( "input:submit, a, button", ".demo" ).button();
		});
	}
	
</script>
</head>
<body onload="list()">
<%@ include file="header.jsp" %>
<div class="contencenter demo">
<h2>ACTIVIDADES</h2>
<div id="info"></div>
<form action="" name="form1">
		<table id="lista" border=0>
		<tr>
			<th colspan="2">NUEVA ACTIVIDAD</th>
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
			<td>Valor</td>
			<td><input type="text" name="valor" id="valor" size=40></td>
		</tr>
		<tr>
			<td>Accion</td>
			<td>
				<select name="accion" id="accion">
					<option value="R">Resolver</option>
					<option value="A">Anular</option>
				</select>
			</td>
		</tr>	
		<tr>
			<td>Activo</td>
			<td>
				<select name="activo" id="activo">
					<option value="1">Si</option>
					<option value="0">No</option>
				</select>
			</td>
		</tr>	
		</table>
	</form>
	<input type="button" name="cmd_agregar" id="cmd_agregar" value="Agregar" onclick="javascript:agregar()" >
	<div id="list"></div>
	</div>
	<%@ include file="foot.jsp" %>
</body>
</html>