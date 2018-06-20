<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="validausuario.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Buscar recurso</title>
<LINK REL="stylesheet" TYPE="text/css" HREF="main.css">
<script src="js/prototype.js" language="JavaScript"> </script>
<link rel="Shortcut Icon" href="icono_tm.ico" type="image/x-icon" />
<script language="JavaScript">

function busqueda(tipo) {
	var criterio = $F("criterio");
	if (criterio == "") {
		alert("Debe ingresar el criterio de busqueda");
		return;
	}
	
	var url="srvRecurso?operacion=busqueda&criterio=" + encodeURIComponent(criterio);
	if (tipo == "contratista") {
		url="srvContratista?operacion=busqueda&criterio=" + encodeURIComponent(criterio);
	}
	
	new Ajax.Updater("lista",url);
}


function paginacion (letra) {
	var url="srvRecurso?operacion=paginacion&letra=" + encodeURIComponent(letra);
	new Ajax.Updater("lista",url);
}
function filtro (criterio,pagina) {
	var url="srvRecurso?operacion=busqueda&criterio=" + encodeURIComponent(criterio) + "&pagina=" + pagina;
	new Ajax.Updater("lista",url);
}
function mostrar(id_div) {

	div = document.getElementById(id_div);
	div.style.display = "";
}
function ocultar(id_div) {

	div = document.getElementById(id_div);
	div.style.display = "none";
}
function agregar() {
	window.location.href="recurso.jsp";
}
</script>
</head>
<body onload="javascript:paginacion('A');">
<%@ include file="header.jsp" %>
<h2>Personal</h2>
<input type="button" onclick="javascript:agregar()" name="cmd_agregar" id="cmd_agregar" value="Agregar" > <input type="button" onclick="javascript:mostrar('filtro');" name="cmd_filtro" id="cmd_filtro" value="Filtrar...">
<div id="filtro" style="display:none"> 
<table>
<tr>
	<th colspan=2>Filtro</th>
</tr>
<tr>
	<td colspan=2><div id="info"></div></td>
</tr>

<tr>
	<td>Opciones de Filtro</td>
	<td><input type="text" name="criterio" id="criterio" size=40></td>
</tr>
<tr>
	<td colspan=2><input type="button" onclick="javascript:busqueda('<%= (String)request.getParameter("tipo") %>');" name="cmd_filtrar" id="cmd_filtrar"  value="Filtrar">  <input type="button" onclick="javascript:paginacion('A');" name="cmd_todos" id="cmd_todos" value="Quintar filtro" > <input type="button" onclick="javascript:ocultar('filtro');" name="cmd_cancelar" id="cmd_cancelar" value="Cancelar"></td>
</tr>

</table>
</div> 
<div id="lista"></div>
<%@ include file="foot.jsp" %>
</body>
</html>