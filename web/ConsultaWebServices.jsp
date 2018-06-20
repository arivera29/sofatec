<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="validausuario.jsp"%>
<%@ page import="com.are.sofatec.*" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Consulta Web Services de ECA</title>
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
	
	
	
	function consultar() {
		var nic = $("#nic").val();
		var metodo = $("#metodo").val();
		
		if (nic == "") {
			alert("Faltan datos");
			return;
		}
		var url ="SrvConsultaWS";
		$("#info").html("<img src='images/loading.gif'> Procesando solicitud");
		$.post(url,{
			nic : nic,
			metodo : metodo
		},procesar);
		
	}
	
		
		function procesar (result) {
			
			$("#resultado").val(result);
	}
	
</script>
</head>
<body>
<%@ include file="header.jsp" %>
<div class="contencenter demo">
<h2>CONSULTA WEB SERVICES ECA</h2>
<form action="" name="form1">
<table>
	<tr>
		<th colspan="2">Datos WS</th>
	</tr>
	<tr>
		<td>NIC</td>
		<td><input type="text" value="" size=40 name="nic" id="nic"></td>
	</tr>
	<tr>
		<td>METODO</td>
		<td>
			<select name="metodo" id="metodo">
				<option value="1">Id de Cobro</option>
				<option value="2">Es Cliente Moroso</option>
				<option value="3">Valor Factura Actual</option>
			</select>
		</td>
	</tr>
	<tr>
		<td colspan="2"><input type="button" value="Consultar" name="cmd_consultar" onclick="javascript:consultar()"></td>
	</tr>
	<tr>
		<td>RESULTADO</td>
		<td>
			<textarea rows="10" cols="60" id="resultado" readonly></textarea>
		</td>
	</tr>

</table>
</form>


</div>
<%@ include file="foot.jsp" %>
</body>
</html>

