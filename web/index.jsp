<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="com.are.sofatec.*"%>
<%
	Controlador controlador = new Controlador();
	if (!controlador.verify()) {
		response.sendRedirect("suspend.jsp");
		return;
	}

	int flag = 0;
	if (request.getParameter("usuario") != null
			&& request.getParameter("password") != null) {
		String usuario = (String) request.getParameter("usuario");
		String password = (String) request.getParameter("password");
		db conexion = new db();
		login lg = new login(conexion);
		if (lg.validLogin(usuario, password)) {
			conexion.Close();
			session.setAttribute("usuario", lg.getUsuario());
			session.setAttribute("perfil", lg.getPerfil());
			response.sendRedirect("EstadoLotes.jsp");
			return;
		} else {
			conexion.Close();
			response.sendRedirect("index.jsp?error=1");
			return;
		}

	}
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>SOFATEC</title>
<LINK REL="stylesheet" TYPE="text/css" HREF="login.css">
<link rel="stylesheet" href="themes/redmond/jquery.ui.all.css">
<script src="js/jquery.js" language="JavaScript"></script>
<script src="ui/jquery.ui.core.js" language="JavaScript"></script>
<script src="ui/jquery.ui.widget.js" language="JavaScript"></script>
<script src="ui/jquery.ui.button.js"></script>
<script type="text/javascript">
$(function() {
	$("#cmd_login").button();
});
</script>
<script type="text/javascript" language="javascript">
	function validar() {
		var usuario = $("#usuario").val();
		var clave = $("#password").val();
		if (usuario == "" || clave == "") {
			$("#info").html("Faltan datos");
			return false;
		}

		return true;
	}

</script>
</head>
<body>
	<br/>
	<div class="login">
	<div class="captura_login">
	<form action="" name="form1" id="form1" method="post" onsubmit="javascript: return validar()">
		<table>
			<tr>
				<td>Usuario</td>
				<td><input type="text" name="usuario" id="usuario" size="15">
				</td>
			</tr>
			<tr>
				<td>Clave</td>
				<td><input type="password" name="password" id="password" size="15">
				</td>
			</tr>
		</table>
		<br/>
		<input type="submit" name="cmd_login" id="cmd_login" value="Ingresar">
		<br/>
		<div id="info"></div>
	</form>
	</div>
	</div>
</body>
</html>