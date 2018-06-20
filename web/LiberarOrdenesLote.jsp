<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="validausuario.jsp"%>
<%@ page import="com.are.sofatec.*"%>
<%@ page import="java.sql.*"%>
<%
	db conexion = new db();
	
	ManejadorZonas manejador = new ManejadorZonas(conexion);
	ArrayList<zonas> lista = manejador.List();
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>LIBERAR ORDENES ASIGNADAS</title>
<link rel="stylesheet" href="themes/redmond/jquery.ui.all.css">
<LINK REL="stylesheet" TYPE="text/css" HREF="main.css">
<script src="js/jquery.js" language="JavaScript"></script>
<script src="ui/jquery.ui.core.js" language="JavaScript"></script>
<script src="ui/jquery.ui.widget.js" language="JavaScript"></script>
<script src="ui/jquery.ui.button.js"></script>

<script type="text/javascript">
	$(function() {
		$("input:submit, a, button", ".demo").button();
		$("input:button, a, button", ".demo").button();
	});
</script>
<script type="text/javascript" language="javascript">
	function LiberarZona() {
		var zona = $("#zona").val();
		if (zona == "") {
			$("#info_zona").html("<img src='images/alerta.gif'> Debe seleccionar la zona");
			return;
		}
		
		
		if (!confirm("Esta seguro de liberar la ordenes de servicios?")) {
			return false;
		}
		
		$("#info_zona").load("cnt_LiberarOrdenesLote?operacion=zona&zona=" + zona);
		
	}
	
	function LiberarRecurso() {
		var recurso = $("#cedula").val();
		if (recurso == "") {
			$("#info_recurso").html("<img src='images/alerta.gif'> Debe seleccionar el recurso");
			return;
		}
		
		
		if (!confirm("Esta seguro de liberar la ordenes de servicios?")) {
			return false;
		}
		
		$("#info_recurso").load("cnt_LiberarOrdenesLote?operacion=recurso&recurso=" + recurso);
		
	}
	
	function buscar() {
		url = "BuscarRecurso.jsp";
		window.open(url , "BuscarRecurso" , "width=600,height=500,scrollbars=YES,menubar=No,toolbar=NO,status=YES"); 
	}
</script>
</head>
<body>
	<%@ include file="header.jsp"%>
	<div class="contencenter demo">
	<h2>LIBERAR ORDENES DE SERVICIO</h2>
	<fieldset class="grupo">
		<legend>LIBERAR ORDENES POR ZONA</legend>
		
		<div id="info"></div>
		<form action="" name="form2" onsubmit="javascript:return confirmar();">
		Seleccionar Zona: 
		<select name="zona" id="zona">
			<option value="all">Todas</option>
			<% for (zonas zona : lista) { %>
				<option value="<%= zona.getId() %>"><%= zona.getNombre() %></option>
			<% } %>
		</select> 
		
		<input type="button" name="cmd_liberar"	id="cmd_liberar" value="Liberar" onClick="javascript:LiberarZona();">
		</form>
		<div id="info_zona"></div>
		</fieldset>
		<br />
		<br />
		<fieldset class="grupo">
		<legend>LIBERAR ORDENES POR RECURSO</legend>
		<form name="form1" action="">
			<table>
			<tr>
				<td>Recurso</td>
				<td colspan="3"><input type="text" name="cedula" id="cedula" readonly> Nombre: <input type="text" name="nombre" id="nombre" size=40 readonly> </td>
			</tr>
			</table>
			<input type="button" name="cmd_buscar" id="cmd_buscar" value="Buscar Recurso" onclick="javascript:buscar()" >  
			<input type="button" name="cmd_liberar" value="Liberar" OnClick="javascript:LiberarRecurso();">
		</form>
		<div id="info_recurso"></div>
		</fieldset>
		
	</div>
	<%@ include file="foot.jsp"%>
</body>
</html>
<%
	if (conexion != null) {
		conexion.Close();
	}
%>