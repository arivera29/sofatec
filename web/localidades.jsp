<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ include file="validausuario.jsp"%>
<%@ page import="com.are.sofatec.*" %>
<%
	db conexion = new db();
	departamento dpto = new departamento(conexion);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>CIUADES</title>
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
		var departamento = $("#departamento").val();
		var latitud = $("#latitud").val();
		var longitud = $("#longitud").val();
		
		if (codigo == "" || descripcion == "" || departamento == "") {
			alert("falta ingresar informacion");
			return;
		}
			var url = "srvLocalidad";
			$.post(url,{
				operacion : "add",
				codigo : codigo,
				descripcion : descripcion,
				departamento : departamento,
				latitud : latitud,
				longitud : longitud
			},procesar);
	
		
	}
	function procesar(resultado) {
		if (resultado != 'OK') {
			$("#info").html("<img src=\"warning.jpg\"" + resultado);
		}else {
			$("#info").html("Ciudad agregada");
			$("#codigo").val("");
			$("#descripcion").val("");
			list();
		}
	}

	function lista() {
		$("#list").load("srvLocalidad?operacion=list",function () {
			$( "input:button, a, button", ".demo" ).button();
		});
	}
	function filtrar() {
		var departamento = document.getElementById("filtro_dpto").value;
		$("#list").load("srvLocalidad?operacion=list&departamento=" + departamento, function(){
			$( "input:button, a, button", ".demo" ).button();
		});
		
	}
</script>
</head>
<body onload="lista()">
<%@ include file="header.jsp" %>
<div class="contencenter demo">
<h2>CIUDADES</h2>
<div id="info"></div>
<form action="" name="form1">
		<table id="lista" border=0>
		<tr>
			<th colspan="2">NUEVA CIUDAD</th>
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
			<td>Departamento</td>
			<td><%= dpto.CreateSelectHTML("departamento") %></td>
		</tr>
		<tr>
			<th colspan="2">COORDENADAS PUNTO INICIAL SALIDA TECNICOS</th>
		</tr>
		<tr>
			<td>Latitud</td>
			<td><input type="text" name="latitud" id="latitud" size=40></td>
		</tr>
		<tr>
			<td>Longitud</td>
			<td><input type="text" name="longitud" id="longitud" size=40></td>
		</tr>
		</table>
		<input type="button" onclick="javascript:agregar();" value="Agregar" id="cmd_agregar" name="cmd_agregar"  >
	</form>
	<br>
	Filtrar por departamento: <%= dpto.CreateSelectHTML("filtro_dpto") %> <input type="button" name="cmd_filtro" id="cmd_filtro" value="Filtrar" onclick="javascript:filtrar()"> <input type="button" name="cmd_todos" id="cmd_todos" value="Mostrar todos" onclick="javascript:lista()">
	
	<div id="list"></div>
	</div>
	<%@ include file="foot.jsp" %>
</body>
</html>
<%
	conexion.Close();
%>