<%@page import="com.are.entidades.Zona"%>
<%@page import="com.are.manejadores.ManejadorZonas"%>
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
	
	ManejadorZonas manejador = new ManejadorZonas(conexion);
	ArrayList<Zona> lista = manejador.List((String)session.getAttribute("usuario"));
	
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
function Procesar()
{
  	var file = $("#file").val();
  	if (file == null ) {
  		alert("Faltan datos para procesar el archivo");
  		return false;
  	}
  	
  	var opt_ordenes = "0";
  	if ($("#chk_ordenes").attr("checked")) {
  		opt_ordenes = "1";
  	}
  	var opt_anomalias = "0";
  	if ($("#chk_anomalias").attr("checked")) {
  		opt_anomalias = "1";
  	}
  	var opt_material = "0";
  	if ($("#chk_material").attr("checked")) {
  		opt_material = "1";
  	}
  	var opt_tipoos = "0";
  	if ($("#chk_tipoos").attr("checked")) {
  		opt_tipoos = "1";
  	}
  	var opt_codigos = "0";
  	if ($("#chk_codigos").attr("checked")) {
  		opt_codigos = "1";
  	}
  	var opt_flujos = "0";
  	if ($("#chk_flujos").attr("checked")) {
  		opt_flujos = "1";
  	}
  	
  	var cedula = $("#cedula").val();
  	if (cedula == "") {
  		alert("Debe seleccionar cedula del recurso");
  		return false;
  	}
  	var zona = $("#zona").val();
  	if (zona == "") {
  		alert("Debe seleccionar la zona");
  		return false;
  	}
  	
  	$("#resultado").html("<img src=\"images/loading.gif\">");
  	$("#resultado").load("SrvProcesarLote",{ 
  		file : file ,
  		opt_ordenes : opt_ordenes,
  		opt_anomalias : opt_anomalias,
  		opt_material : opt_material,
  		opt_tipoos : opt_tipoos,
  		opt_codigos : opt_codigos,
  		opt_flujos : opt_flujos,
  		cedula : cedula,
  		zona : zona
  	});
  	
	return true;
}

function buscar() {
	url = "BuscarRecurso.jsp";
	window.open(url , "BuscarRecurso" , "width=600,height=500,scrollbars=YES,menubar=No,toolbar=NO,status=YES"); 
}

</script>
<title>PROCESAR ARCHIVO DE ORDENES DE SERVICIO</title>
</head>
<body>
<%@ include file="header.jsp"%>
<div class="contencenter demo">
<h2>PROCESAR ARCHIVO DE LOTES</h2>
<form name="form1" action="" method = "POST">
Archivo: <strong><%= archivo.getFilename() %><input type="hidden" name="file" id="file" value="<%= archivo.getFilename()  %>"></strong> 
<BR>
<BR>
<strong>Opciones:</strong>
<br>
<input type="checkbox" name="chk_ordenes" id="chk_ordenes"  checked > Procesar Ordenes
<BR>
<input type="checkbox" name="chk_material" id="chk_material" checked > Codificacion Materiales
<BR>
<input type="checkbox" name="chk_anomalias" id="chk_anomalias" checked > Codificacion Anomalias
<BR>
<input type="checkbox" name="chk_tipoos" id="chk_tipoos" checked > Tipos
<BR>
<input type="checkbox" name="chk_codigos" id="chk_codigos" checked > Codigos
<BR>
<input type="checkbox" name="chk_flujos" id="chk_flujos" checked > Flujos
<BR>

<BR>
<strong>Asignar Recurso</strong>
<BR>
<input type="text" name="cedula" id="cedula" readonly><input type="text" name="nombre" id="nombre" size=40 readonly> <input type="button" name="cmd_buscar" id="cmd_buscar" value="Buscar" onclick="javascript:buscar()" >
<BR>
<BR>
<p>
<label><strong>Seleccione Zona:</strong></label>
<select name="zona" id="zona">
	<% for (Zona zona : lista)  { %>
	<option value="<%= zona.getId() %>" ><%= zona.getNombre() %></option>
	<% } %>
</select>
</p>
<br />	
<input type="button" name="procesar" value="Procesar Archivo" onClick = "javascript:Procesar()">
</form>
<h2>Resultado Carga</h2>
<div id="resultado"></div>

</div>
<%@ include file="foot.jsp" %>
</body>
</html>
<% conexion.Close(); %>