<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="validausuario.jsp"%>
<%@ page import="com.are.sofatec.*" %>
<%@ page import="java.sql.*" %>

<%
	db conexion = new db();
	departamento dpto = new departamento(conexion);
	ResultSet rs = dpto.list();
	boolean rsIsEmpty = !rs.next();
	TipoOrden tipo = new TipoOrden(conexion);
	ResultSet rsTipo = tipo.List();

%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Consulta Fotos</title>
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
	$( "input:submit, a, button", ".demo" ).button();
	$( "input:button, a, button", ".demo" ).button();
	$("#descargar").css("display", "none");
	$( ".fecha" ).datepicker({
		showOn: "button",
		buttonImage: "images/calendar.gif",
		buttonImageOnly: true,
		dateFormat: "yy-mm-dd"
	});
	$( ".fecha" ).datepicker( "option", "showAnim", "slide" );
	});
	
	function consultar() {
		var fecha_inicial = $("#fecha_inicial").val();
		var fecha_final = $("#fecha_final").val();
		var departamento = $("#departamento").val();
		var hora1 = $("#hora1").val();;
		var hora2 = $("#hora2").val();;
		
		var tipo = $("#tipo").val();
		var url = "ListadoFoto.jsp"; 
		$("#list").html("<img src='images/reading.gif'>Procesando Solicitud");
		$("#list").load(url,{
			fecha_inicial : fecha_inicial,
			fecha_final : fecha_final,
			departamento : departamento,
			hora1 : hora1,
			hora2 : hora2,
			tipo : tipo
		},function() {
			$( "input:submit, a, button", ".demo" ).button();
		});
		$("#descargar").css("display", "block");
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
<h2>Listado Ejecucion Ordenes de Servicio</h2>
<div id="info"></div>
<form action="SrvZipImages" name="form1">
	<table>
		<tr>
			<th colspan="4">Seleccion Recurso</th>
		</tr>
		<tr>
		<td>Departamento</td>
			<td>
				<select id="departamento" name="departamento" >
					<option value="all">Todos</option>
					<%  if (!rsIsEmpty) { %>
					<% do { %>
					<option value="<%= rs.getString("depacodi") %>"><%= rs.getString("depadesc") %></option>
					<% }while(rs.next()); %>
					<% } %>
				</select>
			</td>
		</tr>
		<tr>
			<td>Tipo de Orden</td>
			<td>
				<select id="tipo" name="tipo">
					<option value="all">Todos</option>
					
					<% while(rsTipo.next()) { %>
					<option value="<%= rsTipo.getString("tiorcodi") %>"><%= rsTipo.getString("tiordesc") %></option>
					<% } %>

				</select>
			</td>
		</tr>
		<tr>
			<td>Fecha Inicial</td>
			<td><input type="text" name="fecha_inicial" id="fecha_inicial" readonly value="<%= Utilidades.strDateServer()  %>" class="fecha"> Hora:<input type="text" name="hora1" id="hora1" size=10 value="00:00:00"> </td>
		</tr>
		<tr>
			<td>Fecha Final</td>
			<td><input type="text" name="fecha_final" id="fecha_final" readonly value="<%= Utilidades.strDateServer()  %>" class="fecha"> Hora:<input type="text" name="hora2" id="hora2" size=10 value="<%= Utilidades.strTimeServer() %>"></td>
		</tr>
		<tr>
			<td colspan="4"><input type="button" name="cmd_buscar" value="Consultar" onclick="javascript:consultar()" > <input type="submit" name="cmd_descargar" value="Descargar"></td>
		</tr>
	</table>

</form>
<div id="descargar">
<form action="ExportExcel.jsp" method="post" target="_blank" id="FormularioExportacion">
	<p>Exportar a Excel  <img src="images/Download.png" class="botonExcel" /></p> 
	<input type="hidden" id="datos_a_enviar" name="datos_a_enviar" /> 
	</form> 
</div>
<div id="list"></div>
</div>
<%@ include file="foot.jsp" %>
</body>
</html>

<%
	conexion.Close();
%>
