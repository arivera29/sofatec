<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="validausuario.jsp"%>
<%@ page import="com.are.sofatec.*" %>
<%@ page import="java.sql.*" %>
<%
	db conexion = new db();
	departamento dpto = new departamento(conexion);
	ResultSet rs = dpto.list();
	boolean rsIsEmpty = !rs.next();
	TipoOrden tipo = new TipoOrden(conexion);
	ResultSet rsTipo = tipo.List();
	
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>LIBERAR ORDENES ASIGNADAS</title>
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
	function liberar() {
		var departamento = $("#departamento").val();
		var tipo = $("#tipo").val();
		
		if (departamento == "-1") {
			alert("Debe seleccionar el departamento");
			return;
		}
		if (confirm("Esta seguro de realizar el proceso de liberacion de ordenes asignadas?")) {
			var cmd = document.getElementById("cmd_liberar");
			cmd.disabled = true;
			var url = "SrvAsignar";
			$.post(url,{
				operacion: "liberar",
				departamento: departamento,
				tipo: tipo
			},procesar);
		}
	}
	
	function procesar(resultado) {
		var cmd = document.getElementById("cmd_liberar");
		cmd.disabled = false;
		if (resultado == "OK") {
			$("#info").html("Ordenes de servicios liberadas");
		}else {
			$("#info").html("<img src=\"warning.jpg\">" + resultado);
		}
	}
</script>
</head>
<body>
<%@ include file="header.jsp" %>
<div class="contencenter demo">
<h2>LIBERAR ORDENES ASIGNADAS</h2>
<div id="info"></div>
<form action="" name="form2">
	<table>
		<tr>
			<th colspan="4">FILTRO</th>
		</tr>
		<tr>
			<td>Departamento <input type="hidden" value="list" name="operacion" /></td>
			<td>
				<select id="departamento" name="departamento">
					<option value="-1">Seleccionar departamento</option>
					<%  if (!rsIsEmpty) { %>
					<% do { %>
					<option value="<%= rs.getString("depacodi") %>"><%= rs.getString("depadesc") %></option>
					<% }while(rs.next()); %>
					<% } %>
				</select>
			</td>
			<td>Tipo de Orden</td>
			<td>
				<select id="tipo" name="tipo">
					<option value="all">Todos</option>
					
					<% while(rsTipo.next()) { %>
					<option value="<%= rsTipo.getString("tiorcodi") %>"><%= rsTipo.getString("tiordesc") %></option>
					<% } %>

				</select>
			</td>
		</tr>
		<tr>
			<td colspan="4"><input type="button" name="cmd_liberar" id="cmd_liberar" value="Liberar" onclick="javascript:liberar()" ></td>
		</tr>
	</table>
	
</form>
<div id="list"></div>
</div>
<%@ include file="foot.jsp" %>
</body>
</html>
<%
	if (conexion != null) {
		conexion.Close();
	}
%>