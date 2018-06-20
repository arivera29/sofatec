<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ include file="validausuario.jsp"%>
<%@ page import="com.are.sofatec.*" %>
<%
	db conexion = new db();
	ManejadorConfig MC= new ManejadorConfig(conexion); 
	MC.Find();
	Config cfg = MC.getConfig();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>CONFIGURACION</title>
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
	function guardar() {
		var user = $("#user").val();
		var sello1 = $("#sello1").val();
		var sello2 = $("#sello2").val();
		var acometida1 = $("#acometida1").val();
		var acometida2 = $("#acometida2").val();
		var empleado = $("#empleado").val();

		var url = "SrvConfig";
			$.post(url,{
				operacion : "save",
				user : user,
				sello1 : sello1,
				sello2 : sello2,
				acometida1 : acometida1,
				acometida2 : acometida2,
				empleado : empleado
			},procesar);
	
		
	}
	function procesar(resultado) {
		if (resultado != 'OK') {
			$("#info").html("<img src=\"warning.jpg\"" + resultado);
		}else {
			$("#info").html("Configuracion Guardada");
		}
	}
	
</script>
</head>
<body>
<%@ include file="header.jsp" %>
<div class="contencenter demo">
<h2>CONFIGURACION</h2>
<div id="info"></div>
<form action="" name="form1">
		<table id="lista" border=0>
		<tr>
			<th colspan="2">CONFIGURACION REPORTE CIERRES</th>
		</tr>
		<tr>
			<td>Usuario de cierre OPEN</td>
			<td><input type="text" name="user" id="user" value="<%= cfg.getUser() %>"></td>
		</tr>
		<tr>
			<td>Numero de empleado</td>
			<td><input type="text" name="empleado" id="empleado" value="<%= cfg.getNumEmpleado() %>"></td>
		</tr>
		<tr>
			<td>Codigo Sello 1</td>
			<td><input type="text" name="sello1" id="sello1" size=40 value="<%= cfg.getCodigoSello1() %>"></td>
		</tr>
		<tr>
			<td>Codigo Sello 2</td>
			<td><input type="text" name="sello2" id="sello2" size=40 value="<%= cfg.getCodigoSello2() %>"></td>
		</tr>
		<tr>
			<td>Codigo Acometida 1</td>
			<td><input type="text" name="acometida1" id="acometida1" size=40 value="<%= cfg.getCodigoAcometida1() %>"></td>
		</tr>
		<tr>
			<td>Codigo Acometida 2</td>
			<td><input type="text" name="acometida2" id="acometida2" size=40 value="<%= cfg.getCodigoAcometida2() %>"></td>
		</tr>
		</table>
		<input type="button" onclick="javascript:guardar();" value="Guardar" id="cmd_agregar" name="cmd_agregar"  >
	</form>
	</div>
	<%@ include file="foot.jsp" %>
</body>
</html>
<%
	conexion.Close();
%>