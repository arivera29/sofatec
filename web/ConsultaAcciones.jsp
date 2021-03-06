<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<%@ include file="validausuario.jsp"%>
<%@ page import="com.are.sofatec.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%
	db conexion = new db();
	ManejadorZonas manejador = new ManejadorZonas(conexion);
	ArrayList<zonas> lista = manejador.List((String)session.getAttribute("usuario"));
	
	
%>


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Reportes</title>
<link rel="stylesheet" href="themes/redmond/jquery.ui.all.css">
<LINK REL="stylesheet" TYPE="text/css" HREF="main.css">
<script src="js/jquery.js" language="JavaScript"></script>
<script src="ui/jquery.ui.core.js" language="JavaScript"></script>
<script src="ui/jquery.ui.widget.js" language="JavaScript"></script>
<script src="ui/jquery.ui.button.js"></script>
<script src="ui/jquery.effects.core.js"></script>
<script src="ui/jquery.effects.slide.js"></script>
<script src="ui/jquery.ui.datepicker.js"></script>
<script src="ui/jquery.effects.explode.js"></script>
<script src="ui/jquery.effects.fold.js"></script>
<script src="ui/jquery.effects.slide.js"></script>

<script type="text/javascript" language="javascript">
$(function() {
	$( ".boton" ).button();
	$( ".fecha" ).datepicker({
		showOn: "button",
		buttonImage: "images/calendar.gif",
		buttonImageOnly: true,
		dateFormat: "yy-mm-dd"
	});
	$( ".fecha" ).datepicker( "option", "showAnim", "slide" );
	});
	
	function ListadoSellos() {
		var zona = $("#zona").val();
		var fecha1 = $("#fecha_inicial").val();
		var fecha2 = $("#fecha_final").val();
		
		if (zona == "" || fecha1 == "" || fecha2 == "") {
			$("#info").html("<img src='images/alerta.gif'> Faltan datos");
			return;
		}
		
		$("#lista").html("<img src='images/loading.gif'> Generando reporte... espere por favor");
		var url = "ListadoSellos.jsp";
		$("#lista").load(url, {
			zona : zona,
			fecha_inicial : fecha1,
			fecha_final : fecha2
		}, function (data) {
			$("#lista").html(data);
		});
	}
	
	function ListadoAcciones() {
		var zona = $("#zona").val();
		var fecha1 = $("#fecha_inicial").val();
		var fecha2 = $("#fecha_final").val();
		
		if (zona == "" || fecha1 == "" || fecha2 == "") {
			$("#info").html("<img src='images/alerta.gif'> Faltan datos");
			return;
		}
		
		$("#lista").html("<img src='images/loading.gif'> Generando reporte... espere por favor");
		var url = "ListadoAcciones2.jsp";
		$("#lista").load(url, {
			zona : zona,
			fecha_inicial : fecha1,
			fecha_final : fecha2
		}, function (data) {
			$("#lista").html(data);
		});
	}
	
	function ListadoMaterial(tipo) {
		var zona = $("#zona").val();
		var fecha1 = $("#fecha_inicial").val();
		var fecha2 = $("#fecha_final").val();
		
		if (zona == "" || fecha1 == "" || fecha2 == "") {
			$("#info").html("<img src='images/alerta.gif'> Faltan datos");
			return;
		}
		
		$("#lista").html("<img src='images/loading.gif'> Generando reporte... espere por favor");
		var url = "ListadoMaterial.jsp";
		$("#lista").load(url, {
			zona : zona,
			fecha_inicial : fecha1,
			fecha_final : fecha2,
			tipo : tipo
		}, function (data) {
			$("#lista").html(data);
		});
	}
	
</script>

</head>
<body>
<%@ include file="header.jsp" %>
<div class="contencenter demo">
<h2>REPORTES</h2>
<div id="info"></div>
<form action="ListadoAcciones2.jsp" name="form1">
<table>
	<tr >
		<th colspan="2">Configuracion Consulta</th>
	</tr>	
	<tr>
		<td>Zona</td>
		<td>
			<select name="zona" id="zona">
				<% for (zonas zona : lista) { %>
				<option value="<%= zona.getId() %>"><%= zona.getNombre() %></option>
				<% } %>
			</select> 
		</td>
	</tr>
	<tr>
			<td>Fecha Inicial</td>
			<td><input type="text" name="fecha_inicial" id="fecha_inicial" readonly value="<%= Utilidades.strDateServer()  %>" class="fecha"></td>
		</tr>
		<tr>
			<td>Fecha Final</td>
			<td><input type="text" name="fecha_final" id="fecha_final" readonly value="<%= Utilidades.strDateServer()  %>" class="fecha"></td>
		</tr>
	</table>
	<button type="submit" name="cmd_acciones" id="cmd_acciones" class="boton">Descargar</button>
	
	
</form>
<div id="lista"></div>


</div>
<%@ include file="foot.jsp" %>
</body>
</html>

<%
	if (conexion != null) {
		conexion.Close();
	}

%>
