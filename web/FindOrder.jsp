<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="validausuario.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>BUSQUEDA ORDEN DE SERVICIO</title>
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

function cancelar() {
	window.location.href = "menu.jsp?menu=4";
}	
function buscar(opcion) {
	var criterio = $("#criterio").val();
	if (criterio == "") {
		alert("Faltan datos para realizar la busqueda");
		return;
	}
	var url = "SrvOrders?operacion=buscar&criterio=" + criterio + "&opcion=" + opcion;
	$("#list").html("<img src='images/loading.gif'> Procesando solicitud");
	$("#list").load(url,function(){
		$( "input:submit, a, button", ".demo" ).button();
	});
	
}
</script>
</head>
<body>
<%@ include file="header.jsp" %>
<div class="contencenter demo">
<h2>BUSQUEDA DE ORDEN DE SERVICIO</h2>
<form action="">
	Ingrese criterio de busqueda (ORDEN,NIC,MEDIDOR): <input type="text" name="criterio" id="criterio" size=40> 
	<input type="button" onclick="javascript:buscar('<%= (String)request.getParameter("operacion") %>');" name="cmd_buscar" id="cmd_buscar"  value="Buscar">
</form>
<div id="list"></div>
</div>

</body>
</html>