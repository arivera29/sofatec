<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ include file="validausuario.jsp"%>
<%@ page import="java.io.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.are.sofatec.*" %>
<%@ page import ="com.csvreader.CsvReader" %>
<%
	if ((String)request.getParameter("id") == null ) {
		response.sendRedirect("main.jsp");
		return;
	}
	
	String id = (String)request.getParameter("id");
	
	db conexion = new db();
	
	ManejadorArchivo ma = new ManejadorArchivo(conexion);
	if (!ma.Find(Integer.parseInt(id))) {
		conexion.Close();
		response.sendRedirect("main.jsp");
		return;
	}
	Archivo archivo = ma.getArchivo();	

	String operacion = "";
	if (request.getParameter("operacion") != null) {
		operacion = (String)request.getParameter("operacion");
	}
	
	departamento dpto = new departamento(conexion);

%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
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
function validar()
{
  	var file = $("#file").val();
  	var separador = $("#separador").val();
  	var departamento = $("#departamento").val();
  	
  	if (file == null || separador == null || departamento == "") {
  		alert("Faltan datos para procesar el archivo");
  		return false;
  	}
	
	return true;
}

</script>
<title>PROCESAR ARCHIVO DE ORDENES DE SERVICIO</title>
</head>
<body>
<%@ include file="header.jsp"%>
<div class="contencenter demo">
<h2>PROCESAR ARCHIVO DE ORDENES DE SERVICIO</h2>
<form name="form1" action="ProcessOrders" method = "POST" onsubmit="javascript:return validar();">
<table>
<tr>
	<th colspan="2">Archivo a procesar</th>
</tr>
<tr>
	<td>Archivo</td>
	<td><%= archivo.getFilename() %><input type="hidden" name="file" id="file" value="<%= archivo.getFilename()  %>"></td>
</tr>
<tr>
	<td>Caracter separador</td>
	<td>
		<select id="separador" name="separador" size=4>
			<option value="1">Tabulador</option>
			<option value="2">Coma (,)</option>
			<option value="3">Punto y Coma (;)</option>
			<option value="4">Pipe (|)</option>
		</select>
	</td>
</tr>
<tr>
	<td>Departamento</td>
	<td>
		<%= dpto.CreateSelectHTML("departamento") %>
	</td>
</tr>
</table>
<input type="submit" name="procesar" value="Procesar Archivo">
</form>
</div>
<%@ include file="foot.jsp" %>
</body>
</html>
<% conexion.Close(); %>