<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="validausuario.jsp"%>
<%@ page import="java.sql.*" %>
<%@ page import="com.are.sofatec.*" %>
<%
	if (request.getParameter("codigo") == null) {
		response.sendRedirect("Equipos.jsp");
		return;
	}
	
	String codigo = (String)request.getParameter("codigo");
	db conexion = new db();
	String sql ="select imei,marcdesc,recurso,recunomb from equipos,marcas,recurso where imei='" + codigo + "' and marca = marccodi and recurso=recucodi";
	ResultSet rs = conexion.Query(sql);
	boolean rsIsEmpty = !rs.next();

%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>ASIGNAR SMARTPHONE</title>
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
	function asignar(key) {
		var cedula = $("#cedula").val();

		
		if (cedula == "") {
			alert("Debe seleccionar el recurso a asignar");
			return;
		}
		var cmd = document.getElementById("cmd_asignar");
		cmd.disabled = true;
		$("#info").html("<img src=\"images/loading.gif\" >Procesando solicitud");
		$.post(
			"SrvEquipos",
			{
				operacion: "asignar",
				imei: key,
				recurso: cedula
			},
			procesar
		
		);
		
	}
	function procesar(resultado) {
		var cmd = document.getElementById("cmd_asignar");
		cmd.disabled = false;
		if (resultado != 'OK') {
			$("#info").html("<img src=\"warning.jpg\">" + resultado);
		}else {
			$("#info").html("<img src=\"images/ok.png\">Equipo asignado correctamente");
			$("#recurso").html($("#cedula").val() + " " + $("#nombre").val() );
			$("#cedula").val("");
			$("#nombre").val("");
		}
	}

	function liberar(key) {
		if (confirm("Desea liberar el Equipo " + key)) {
			var cmd = document.getElementById("cmd_liberar");
			cmd.disabled = true;
			$("#info").html("<img src=\"images/loading.gif\" >Procesando solicitud");
			$.post(
				"SrvEquipos",
				{
					operacion: "liberar",
					imei: key
				},
				procesarLiberar
			
			);
	}
		
	}
	
	function procesarLiberar(resultado) {
		var cmd = document.getElementById("cmd_liberar");
		cmd.disabled = false;
		if (resultado != 'OK') {
			$("#info").html("<img src=\"warning.jpg\">" + resultado);
		}else {
			$("#info").html("<img src=\"images/ok.png\">Equipo liberado correctamente");
			$("#recurso").html("-1 NO APLICA");
			$("#cedula").val("");
			$("#nombre").val("");
		}
		
	}
	
	function cancelar() {
		window.location.href="Equipos.jsp";
	}
	
	function buscar() {
		url = "BuscarRecurso.jsp";
		window.open(url , "BuscarRecurso" , "width=600,height=500,scrollbars=YES,menubar=No,toolbar=NO,status=YES"); 
	}
</script>
</head>
<body>
<%@ include file="header.jsp" %>
<div class="contencenter demo">
<h2>Asignar Smartphone</h2>
<div id="info"></div>
<form action="" name="form1">
		<table>
		<tr>
			<th colspan="2">Datos Equipo</th>
		</tr>
		<tr>
			<td>Marca</td>
			<td><%= rs.getString("marcdesc") %></td>
		</tr>
		<tr>
			<td>IMEI</td>
			<td><%= rs.getString("imei") %></td>
		</tr>
		<tr>
			<td>Asignacion Actual</td>
			<td><div id="recurso"><%= rs.getString("recurso") %> <%= rs.getString("recunomb") %></div></td>
		</tr>
		<tr>
			<td>Asigna a:</td>
			<td><input type="text" name="cedula" id="cedula" value="" size="20" disabled> <input type="text" name="nombre" id="nombre" value="" size="40" disabled> <input type="button" name="cmd_buscar" id="cmd_buscar" value="Buscar" onclick="javascript:buscar()" ></td>
		</tr>	
		</table>
		<input type="button" onclick="javascript:asignar('<%= (String)request.getParameter("codigo") %>');" value="Asignar" id="cmd_asignar" name="cmd_asignar" > <input type="button" onclick="javascript:liberar('<%= (String)request.getParameter("codigo") %>');" value="Liberar" id="cmd_liberar" name="cmd_liberar" >  <input type="button" name="cmd_cancelar" id="cmd_cancelar" value ="Cancelar" onclick="javascript:cancelar()" >
	</form>
	</div>
	<%@ include file="foot.jsp" %>
</body>
</html>

<%
	conexion.Close();
%>