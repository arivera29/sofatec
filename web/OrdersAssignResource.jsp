<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="validausuario.jsp"%>
<%@ page import="com.are.sofatec.*" %>
<%@ page import="java.sql.*" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>CONSULTA ASIGNACION POR RECURSO</title>
<link rel="stylesheet" href="themes/redmond/jquery.ui.all.css">
<LINK REL="stylesheet" TYPE="text/css" HREF="main.css">
<script src="js/jquery.js" language="JavaScript"></script>
<script src="ui/jquery.ui.core.js" language="JavaScript"></script>
<script src="ui/jquery.ui.widget.js" language="JavaScript"></script>
<script src="ui/jquery.ui.button.js"></script>

<script type="text/javascript">
var tab;
var filas;

$(function() {
	$( "input:submit, a, button", ".demo" ).button();
	$( "input:button, a, button", ".demo" ).button();
	$("#descargar").css("display", "none");
});
</script>
<script type="text/javascript" language="javascript">
	function buscar() {
		url = "BuscarRecurso.jsp";
		window.open(url , "BuscarRecurso" , "width=600,height=500,scrollbars=YES,menubar=No,toolbar=NO,status=YES"); 
	}
	
	function consultar() {
		var url = "SrvListAssign?operacion=recurso&recurso=" + $("#cedula").val();
		$("#list").load(url);
		$("#descargar").css("display", "block");
	}
	
	function Liberar(cedula) {
		if (confirm("Esta seguro de liberar las ordenes ser servicio?")) {
			var url = "SrvOrders?operacion=liberar_tecnico&cedula=" + cedula;
			$("#list").load(url);
		}
	}
	
	function Mapa() {
		url = "OrdenesAsignadasTecnicoMapa.jsp?cedula=" + $("#cedula").val();;
		window.open(url , "MapaAsignadasTecnico" , "width=800,height=600,scrollbars=YES,menubar=No,toolbar=NO,status=YES"); 
	}
	
	function RutaMapa() {
		url = "AsignarTecnicoMapa.jsp?cedula=" + $("#cedula").val();;
		window.open(url , "MapaAsignadasTecnico" , "width=800,height=600,scrollbars=YES,menubar=No,toolbar=NO,status=YES"); 
	}
	
	function moverDown(id) {
		$("#"+id).next().after($("#" +id));
	}
	function moverUp(id) {
		$("#" + id).prev().before($("#" + id));
	}
	
	function ConfirmarRuta() {
		if (confirm("Esta seguro de confirmar la ruta?")) {
			var url = "SrvRuta?operacion=confirmar";
			var datos = new Array();
			$("#Exportar_a_Excel tbody tr").each(function (index) {
				var orden = $(this).find("td").eq(2).html();
				var cad = "orden=" + orden;
				datos.push(cad);
			});
			
			url += "&" + datos.join("&");
			$.get(url,procesar);
		}
		
	}

	function procesar(resultado) {
		if (resultado != 'OK') {
			$("#error").html("<img src=\"warning.jpg\">" + resultado);
		}else {
			$("#error").html("<img src=\"images/online.png\">Ruta confirmada");
			consultar();
		}
		
	}
	</script>
<script language="javascript"> 
$(document).ready(function() { 
     $(".botonExcel").click(function(event) { 
     $("#datos_a_enviar").val( $("<div>").append( $("#Exportar_a_Excel").eq(0).clone()).html()); 
     $("#FormularioExportacion").submit(); 
	}); 
}); 
</script>
</head>
<body>
<%@ include file="header.jsp" %>
<div class="contencenter demo">
<h2>CONSULTA ASIGNACION POR RECURSO</h2>
<div id="info"></div>
<form action="" name="form1">
	<table>
		<tr>
			<th colspan="4">RECURSO</th>
		</tr>
		<tr>
			<td>Recurso</td>
			<td colspan="3"><input type="text" name="cedula" id="cedula" readonly><input type="text" name="nombre" id="nombre" size=40 readonly> </td>
		</tr>
	</table>
	<input type="button" name="cmd_buscar" id="cmd_buscar" value="Buscar Recurso" onclick="javascript:buscar()" >  
	<input type="button" name="cmd_buscar" value="Consultar" onclick="javascript:consultar()" >
	<input type="button" name="cmd_ruta" value="Ruteo en Mapa" onclick="javascript:RutaMapa()" >
	<input type="button" name="cmd_mapa" value="Ver Mapa" onclick="javascript:Mapa()" >
</form>
<div id="descargar">
<form action="ExportExcel.jsp" method="post" target="_blank" id="FormularioExportacion">
	<p>Exportar a Excel  <img src="images/Download.png" class="botonExcel" /></p> 
	<input type="hidden" id="datos_a_enviar" name="datos_a_enviar" /> 
	</form> 
</div>
<div id="error"></div>
<div id="list"></div>
</div>
<%@ include file="foot.jsp" %>
</body>
</html>
