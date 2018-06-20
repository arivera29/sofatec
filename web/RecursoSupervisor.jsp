<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="validausuario.jsp"%>
<%@ page import="com.are.sofatec.*" %>
<%@ page import="java.sql.*" %>
<%
	db conexion = new db();

	String cedula = (String)request.getParameter("supervisor");
	recursohumano supervisor = new recursohumano(conexion);
	supervisor.Find(cedula);
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Recursos por Supervisor</title>
<LINK REL="stylesheet" TYPE="text/css" HREF="main.css">
<script src="js/jquery-1.3.2.js" language="JavaScript"></script>
<link rel="Shortcut Icon" href="icono_tm.ico" type="image/x-icon" />
<script type="text/javascript">
function buscar() {
	url = "BuscarRecurso.jsp";
	window.open(url , "BuscarRecurso" , "width=600,height=500,scrollbars=YES,menubar=No,toolbar=NO,status=YES"); 
}

function agregar(supervisor) {
	var recurso = $("#cedula").val(); // cedula recurso

	if (recurso == "") {
		alert("falta ingresar informacion");
		return;
	}
		
		var cmd = document.getElementById("cmd_agregar");
		cmd.disabled = true;
		$("#info").html("<img src=\"images/loading.gif\" >Procesando solicitud");
		$.post(
			"srvRecurso",
			{
				operacion: "add_recurso_supervisor",
				recurso: recurso,
				supervisor: supervisor
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
		$("#info").html("<img src=\"images/ok.png\">Recurso agregado correctamente");
		$("#cedula").val("");
		$("#nombre").val("");
		list('<%= (String)request.getParameter("supervisor") %>');
	}
	
}

function list(supervisor) {
	$("#list").load("srvRecurso?operacion=list_recurso_supervisor&supervisor=" + supervisor);
	
}
function cancelar() {
	window.location.href = "Supervisores.jsp"
}
</script>
</head>
<body onload ="javascript:list('<%= (String)request.getParameter("supervisor") %>')">
<%@ include file="header.jsp" %>
<h2>Recurso a cargo del Supervisor</h2>
<div id="info"></div>
<form action="" name="form1">
	<table>
		<tr>
			<th colspan="4">Agregar recurso</th>
		</tr>
		<tr>
			<td>Supervisor</td>
			<td colspan="3"><input type="text" name="texto1" id="texto1" readonly value="<%= supervisor.getCodigo() %>"><input type="text" name="texto2" id="texto2" size=40 readonly value="<%= supervisor.getNombre() %>"></td>
		</tr>
		<tr>
			<td>Recurso</td>
			<td colspan="3"><input type="text" name="cedula" id="cedula" readonly><input type="text" name="nombre" id="nombre" size=40 readonly> <input type="button" name="cmd_buscar" id="cmd_buscar" value="Buscar" onclick="javascript:buscar()" ></td>
		</tr>
		<tr>
			<td colspan="4"><input type="button" name="cmd_agregar" id="cmd_agregar" value="Agregar" onclick="javascript:agregar('<%= (String)request.getParameter("supervisor") %>')" > <input type="button" name="cmd_cancelar" id="cmd_cancelar" value="Cancelar" onclick="javascript:cancelar()" ></td>
		</tr>
	</table>

</form>
<div id="list"></div>
<%@ include file="foot.jsp" %>

</body>
</html>
<%
	conexion.Close();
%>