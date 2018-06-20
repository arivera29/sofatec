<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Descargar Aplicaciones</title>
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
</script>
</head>
<body>
<%@include file="header.jsp"%>
<div class="contencenter demo">
<h2>Descargas aplicaciones moviles</h2>
<div id="info"></div>
<table>
	<tr>
		<th>Item</th>
		<th>Descargar</th>
		
	</tr>
	<tr>
		<td>Sofatec QO</td>
		<td><a href="SofatecQO.apk">Descargar</a></td>
	</tr>
		<tr>
		<td>Localizador GPS</td>
		<td><a href="Localizador.apk">Descargar</a></td>
	</tr>
	
		<tr>
		<td>ES Manager</td>
		<td><a href="SofatecQO.apk">Descargar</a></td>
	</tr>
	
</table>


</div>
<%@include file="foot.jsp"%>
</body>
</html>
