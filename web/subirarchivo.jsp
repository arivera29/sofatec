<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ include file="validausuario.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>CARGAR ARCHIVO SERVIDOR</title>
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
</head>
<body>
	<%@ include file="header.jsp"%>
	<div class="contencenter demo">
	<h2>SUBIR ARCHIVO AL SERVIDOR</h2>
	<form action="subir.jsp" enctype="MULTIPART/FORM-DATA" method="post">
            <input type="file" name="file" size=60/><br/>
            <input type="submit" value="Upload" />
    </form>
	</div>
	<%@ include file="foot.jsp"%>
</body>
</html>