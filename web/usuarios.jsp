<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<%@ include file="validausuario.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Administrar Usuarios</title>
<LINK REL="stylesheet" TYPE="text/css" HREF="main.css">
<script src="js/prototype.js" language="JavaScript"> </script>
<link rel="Shortcut Icon" href="icono_tm.ico" type="image/x-icon" />
<script type="text/javascript" language="javascript">
	function list(letra) {
		new Ajax.Updater("list","SrvUsuarios?operacion=list&letra="+letra);
	}
</script>
</head>
<body onload="list('A')">
<%@ include file="header.jsp" %>
	<BR/>
	<a href="adduser.jsp" class="small button blue">Agregar Usuario</a>
	<div id="info"></div>
	<div id="list"></div>
	<%@ include file="foot.jsp" %>
</body>
</html>

