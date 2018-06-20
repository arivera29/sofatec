<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%-- <%@ page import="com.are.sofatec.*"%> --%>
<%@include file="validausuario.jsp"%>
<%
	db conexion = new db();
	String localidad = (String)request.getParameter("localidad");
	
	localidad l = new localidad(conexion);
	if (!l.Find(localidad)) {
		conexion.Close();
		response.sendRedirect("localidades.jsp");
		return;
	}
	
	
	

%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Recurso por localidad</title>
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

<script type="text/javascript">
	function lista(localidad) {
		$("#lista").load("srvRecurso?operacion=list_localidad&localidad=" + localidad, function() {
			$( "input:submit, a, button", ".demo" ).button();
		});
	}
	
	function add(localidad) {
		var cedula = $("#cedula").val();
		
		if (cedula == "") {
			alert("Debe seleccionar el recurso");
			return;
		}
		
		var url = "srvRecurso";
		$.post(url, {
			operacion: "add_localidad",
			recurso: cedula,
			localidad : localidad
		}, procesar);
	}

	function procesar(result) {
		if (result == "OK") {
			$("#info").html("Recurso agregado correctamente");
			lista('<%= (String)request.getParameter("localidad") %>');  // actualizar lista
			$("#cedula").val("");
			$("#nombre").val("");
		}else {
			$("#info").html(result);
		}
	}
	
	function eliminar(id) {
		if (confirm("Esta seguro de eliminar el recurso id " + id)) {
			var url = "srvRecurso";
			$.post(url, {
				operacion: "remove_localidad",
				id : id
			}, procesarEliminar);
		}
	}
	
	function procesarEliminar(result) {
		if (result == "OK") {
			$("#info").html("Recurso eliminado correctamente");
			lista('<%= (String)request.getParameter("localidad") %>');  // actualizar lista
		}else {
			$("#info").html(result);
		}
	}
	
	function buscar() {
		url = "BuscarRecurso.jsp";
		window.open(url , "BuscarRecurso" , "width=600,height=500,scrollbars=YES,menubar=No,toolbar=NO,status=YES"); 
	}
	
</script>
</head>
<body onload="lista('<%= (String)request.getParameter("localidad") %>');">
<%@include file="header.jsp"%>
<div class="contencenter demo">
<h2>Recurso</h2>
<div id="info"></div>
<form action="" name="form1">
	<table>
		<tr>
			<th colspan="2">Informacion</th>
		</tr>
		<tr>
			<td>Localidad</td>
			<td><%= l.getCodigo() %> <%= l.getDescripcion() %></td>
		</tr>
	
		<tr>
			<td>Recurso</td>
			<td colspan="3"><input type="text" name="cedula" id="cedula" readonly><input type="text" name="nombre" id="nombre" size=40 readonly> <input type="button" name="cmd_buscar" id="cmd_buscar" value="Buscar Recurso" onclick="javascript:buscar()" > </td>
		</tr>
		<tr>
			<td colspan="2"><input type="button" value="Agregar" name="cmd_agregar" id="cmd_agregar" onclick="javascript:add('<%= l.getCodigo() %>')" /> </td>
		</tr>
	</table>
</form>
<div id="lista"></div>
</div>
<%@include file="foot.jsp"%>
</body>
</html>
<%
	conexion.Close();
%>