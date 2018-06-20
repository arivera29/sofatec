<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="validausuario.jsp"%>
<%@ page import="com.are.sofatec.*" %>
<%@ page import="java.sql.*" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Supervisores</title>
<LINK REL="stylesheet" TYPE="text/css" HREF="main.css">
<script src="js/jquery-1.3.2.js" language="JavaScript"></script>
<link rel="Shortcut Icon" href="icono_tm.ico" type="image/x-icon" />
<script type="text/javascript" language="javascript">
	function buscar() {
		url = "BuscarRecurso.jsp";
		window.open(url , "BuscarRecurso" , "width=600,height=500,scrollbars=YES,menubar=No,toolbar=NO,status=YES"); 
	}
	
	function agregar() {
		var cedula = $("#cedula").val();

		if (cedula == "") {
			alert("falta ingresar informacion");
			return;
		}
			
			var cmd = document.getElementById("cmd_agregar");
			cmd.disabled = true;
			$("#info").html("<img src=\"images/loading.gif\" >Procesando solicitud");
			$.post(
				"srvRecurso",
				{
					operacion: "add_supervisor",
					cedula: cedula
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
			$("#info").html("<img src=\"images/ok.png\">Supervisor agregado correctamente");
			$("#cedula").val("");
			$("#nombre").val("");
			list();
		}
		
	}
	
	function eliminar(cedula) {
		if (confirm("Esta seguro de eliminar el supervisor " + cedula)) {
			
			$("#info").html("<img src=\"images/loading.gif\" >Procesando solicitud");
			$.post(
				"srvRecurso",
				{
					operacion: "remove_supervisor",
					cedula: cedula
				},
				procesarElimnar
			
			);
		}
		
	}
	
	function procesarElimnar(resultado) {
		if (resultado != 'OK') {
			$("#info").html("<img src=\"warning.jpg\">" + resultado);
		}else {
			$("#info").html("<img src=\"images/ok.png\">Supervisor eliminado correctamente");
			list();
		}
		
	}
	
	function list() {
		$("#list").load("srvRecurso?operacion=list_supervisor");
		
	}

</script>
</head>
<body onload="javascript:list();">
<%@ include file="header.jsp" %>
<h2>Supervisores</h2>
<div id="info"></div>
<form action="" name="form1">
	<table>
		<tr>
			<th colspan="4">Agregar Supervisor</th>
		</tr>
		<tr>
			<td>Recurso</td>
			<td colspan="3"><input type="text" name="cedula" id="cedula" readonly><input type="text" name="nombre" id="nombre" size=40 readonly> <input type="button" name="cmd_buscar" id="cmd_buscar" value="Buscar" onclick="javascript:buscar()" ></td>
		</tr>
		<tr>
			<td colspan="4"><input type="button" name="cmd_agregar" id="cmd_agregar" value="Agregar" onclick="javascript:agregar()" > </td>
		</tr>
	</table>

</form>
<div id="list"></div>
<%@ include file="foot.jsp" %>
</body>
</html>
