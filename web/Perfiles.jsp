<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%-- <%@ page import="com.are.sofatec.*"%> --%>
<%@include file="validausuario.jsp"%>
<%-- <%
	db conexion = new db();
	ManejadorPerfiles mp = new ManejadorPerfiles(conexion);
	if (!mp.AllowAccessPage((String)session.getAttribute("perfil"),"PER")) {
		conexion.Close();
		response.sendRedirect("accesodenegado.jsp");
		return;
	}

%> --%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Perfiles</title>
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
	function lista() {
		$("#lista").load("SrvPerfiles?operacion=list", function() {
			$( "input:submit, a, button", ".demo" ).button();
		});
	}
	
	function add() {
		var nombre = $("#nombre").val();
		
		if (nombre == "") {
			alert("Debe ingresar el nombre de la bandeja");
			return;
		}
		
		var url = "SrvPerfiles";
		$.post(url, {
			operacion: "add",
			nombre: nombre
		}, procesar);
	}

	function procesar(result) {
		if (result == "OK") {
			$("#info").html("Perfil creado correctamente");
			lista();  // actualizar lista
			$("#nombre").val("");
		}else {
			$("#info").html(result);
		}
	}
	
	function eliminar(id) {
		if (confirm("Esta seguro de eliminar el perfil?")) {
			$.get("SrvPerfiles",{
				operacion: "remove",
				id: id
			},function (data) {
				if (data == "OK") {
					$("#info").html("<p><img src='images/alerta.gif'> Perfil eliminado correctamente</p>");
					lista();
				}else {
					$("#info").html("<p><img src='images/alerta.gif'> Error: " + data + "</p>");
				}
			});
		}
	}
	
</script>
</head>
<body onload="lista();">
<%@include file="header.jsp"%>
<div class="contencenter demo">
<h2><img alt="Perfiles" src="images/perfiles.png">Perfiles</h2>
<div id="info"></div>
<form action="" name="form1">
	<table>
		<tr>
			<th colspan="2">Perfil</th>
		</tr>
		<tr>
			<td>Nombre</td>
			<td><input type="text" value="" size = "40" name="nombre" id="nombre" /></td>
		</tr>
		<tr>
			<td colspan="2"><input type="button" value="Agregar" name="cmd_agregar" id="cmd_agregar" onclick="javascript:add()" /></td>
		</tr>
	</table>
</form>
<div id="lista"></div>
</div>
<%@include file="foot.jsp"%>
</body>
</html>
<%
	//conexion.Close();
%>