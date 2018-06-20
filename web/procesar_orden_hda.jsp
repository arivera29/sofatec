<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ include file="validausuario.jsp"%>
<%@ page import="com.are.sofatec.*"%>
<%@ page import="java.sql.*"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>PROCESAR ORDEN HDA</title>
<link rel="stylesheet" href="themes/redmond/jquery.ui.all.css">
<LINK REL="stylesheet" TYPE="text/css" HREF="main.css">
<script src="js/jquery.js" language="JavaScript"></script>
<script src="ui/jquery.ui.core.js" language="JavaScript"></script>
<script src="ui/jquery.ui.widget.js" language="JavaScript"></script>
<script src="ui/jquery.ui.button.js"></script>

<script type="text/javascript">
	$(function() {
		$("input:submit, a, button", ".demo").button();
		$("input:button, a, button", ".demo").button();
	});
</script>
<script type="text/javascript" language="javascript">
	function procesar(orden) {
		if (confirm("Esta seguro de procesar la orden de servicio No. " + orden)) {
			$("#info").html("<img src='images/loading.gif'> Procesando solicitud, espere por favor...");
			var url = "SrvOrdenHda";
			$.get(url,{
				orden: orden
			},function (data) {
				$("#info").html(data);
			});
		}
	}
	
	
	function cancelar(lote) {
		document.location.href="asignadas.jsp?num_lote=" + lote 
	}
</script>
</head>
<body>
	<%@ include file="header.jsp"%>
	<div class="contencenter demo">
		<h2>PROCESAR ORDEN DE SERVICIO # <%= (String)request.getParameter("orden") %></h2>
		<p>
			<img src="images/alerta.gif">Este proceso consultara las ordenes pendiente de cierre en la herramienta HDA de ELECTRICARIBE.
		</p>
		<button type="button" name="procesar" onclick="javascript:procesar('<%= (String)request.getParameter("orden") %>')">Procesar ordenes desde HDA</button>		
		<button type="button" name="cancelar" onclick="javascript:cancelar('<%= (String)request.getParameter("lote") %>');">Cancelar</button>
		
		<div id="info">
		
		</div>
	</div>
	<%@ include file="foot.jsp"%>
</body>
</html>
