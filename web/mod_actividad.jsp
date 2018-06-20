<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="validausuario.jsp"%>
<%@ page import="com.are.sofatec.*" %>
<%
	if (request.getParameter("codigo") == null) {
		response.sendRedirect("Actividades.jsp");
		return;
	}
	
	String codigo = (String)request.getParameter("codigo");
	db conexion = new db();
	Actividades actividad = new Actividades(conexion);
	actividad.Find(codigo);

%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>MODIFICAR ACTIVIDAD</title>
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
<script type="text/javascript" language="javascript">
	function modificar(key) {
		var codigo = $("#codigo").val();
		var descripcion = $("#descripcion").val();
		var valor = $("#valor").val();
		var activo = $("#activo").val();
		var accion = $("#accion").val();
		
		if (codigo == "" || descripcion == "") {
			alert("falta ingresar informacion");
			return;
		}
		var cmd = document.getElementById("cmd_modificar");
		cmd.disabled = true;
		$("#info").html("<img src=\"images/loading.gif\" >Procesando solicitud");
		$.post(
			"SrvActividades",
			{
				operacion: "modify",
				codigo: codigo,
				descripcion: descripcion,
				valor: valor,
				activo: activo,
				accion: accion,
				key: key
			},
			procesar
		
		);
		
	}
	function procesar(resultado) {
		var cmd = document.getElementById("cmd_modificar");
		cmd.disabled = false;
		if (resultado != 'OK') {
			$("#info").html("<img src=\"warning.jpg\">" + resultado);
		}else {
			alert("Actividad modificada correctamente");
			window.location.href="Actividades.jsp";
		}
	}

	function eliminar(key) {
		if (confirm("Desea eliminar la Actividad " + key)) {
			var cmd = document.getElementById("cmd_eliminar");
			cmd.disabled = true;
			$("#info").html("<img src=\"images/loading.gif\" >Procesando solicitud");
			$.post(
				"SrvActividades",
				{
					operacion: "remove",
					key: key
				},
				procesarEliminar
			
			);
	}
		
	}
	
	function procesarEliminar(resultado) {
		var cmd = document.getElementById("cmd_eliminar");
		cmd.disabled = false;
		if (resultado != 'OK') {
			$("#info").html("<img src=\"warning.jpg\">" + resultado);
		}else {
			alert("Marca eliminada");
			window.location.href = "Actividades.jsp";
		}
		
	}
	
	function cancelar() {
		window.location.href="Actividades.jsp";
	}
	
</script>
</head>
<body>
<%@ include file="header.jsp" %>
<div class="contencenter demo">
<h2>MODIFICAR ACTIVIDAD</h2>
<div id="info"></div>
<form action="" name="form1">
		<table>
		<tr>
			<th colspan="2">DATOS</th>
		</tr>
		<tr>
			<td>Id</td>
			<td><input type="text" name="codigo" id="codigo" value="<%= actividad.getCodigo() %>"></td>
		</tr>
		<tr>
			<td>Descripcion</td>
			<td><input type="text" name="descripcion" id="descripcion" size=40 value = "<%= actividad.getDescripcion() %>"></td>
		</tr>
		
		<tr>
			<td>Valor</td>
			<td><input type="text" name="valor" id="valor" size=40  value="<%= actividad.getValor() %>"></td>
		</tr>
		<tr>
			<td>Accion</td>
			<td>
				<select name="accion" id="accion">
					<option value="R" <%= (actividad.getAccion().equals("R"))?"selected":"" %>>Resolver</option>
					<option value="A" <%= (actividad.getAccion().equals("A"))?"selected":"" %>>Anular</option>
				</select>
			</td>
		</tr>
		<tr>
			<td>Activo</td>
			<td>
				<select name="activo" id="activo">
					<option value="1" <%= (actividad.getActivo()==1)?"selected":"" %>>Si</option>
					<option value="0" <%= (actividad.getActivo()==0)?"selected":"" %>>No</option>
				</select>
			</td>
		</tr>
		
		</table>
		<input type="button" onclick="javascript:modificar('<%= (String)request.getParameter("codigo") %>');" value="Modificar" id="cmd_modificar" name="cmd_modificar" > 
		<input type="button" onclick="javascript:eliminar('<%= (String)request.getParameter("codigo") %>');" value="Eliminar" id="cmd_eliminar" name="cmd_eliminar" >  
		<input type="button" name="cmd_cancelar" id="cmd_cancelar" value ="Cancelar" onclick="javascript:cancelar()" >
	</form>
	</div>
	<%@ include file="foot.jsp" %>
</body>
</html>

<%
	conexion.Close();
%>