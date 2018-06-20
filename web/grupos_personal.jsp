<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="validausuario.jsp"%>
<%@ page import="com.are.sofatec.*" %>
<%@ page import="com.are.manejadores.*" %>
<%@ page import="com.are.entidades.*" %>
<%@ page import="java.sql.*" %>

<%
	db conexion = new db();
	String id = (String)request.getParameter("id");
	ManejadorGrupo manejador = new ManejadorGrupo(conexion);
	
	if (!manejador.Find(Integer.parseInt(id))) {
		conexion.Close();
		response.sendRedirect("grupos.jsp");
		return;
	}
	
	if (request.getParameter("cmd_agregar") != null ) {
		String cedula = (String)request.getParameter("cedula");
		if (manejador.Add_Personal(cedula, Integer.parseInt(id))) {
			conexion.Commit();
		}
		
	}
	
	if (request.getParameter("operacion")!= null) {
		String operacion = (String)request.getParameter("operacion");
		if (operacion.equals("eliminar")) {
			String id_personal = (String)request.getParameter("id_personal");
			if (manejador.Remove_Personal(Integer.parseInt(id_personal))) {
				conexion.Commit();
			}
		}
	}
	
	Grupo grupo = manejador.getGrupo();
	String sql = "select id,cedula,recunomb from grupos_personal,recurso where cedula=recucodi and grupo=? order by recunomb";
	PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
	pst.setString(1, id);
	ResultSet rs = conexion.Query(pst);
	boolean rsIsEmpty = !rs.next();

%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>GRUPOS PERSONAL</title>
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
	function buscar() {
		url = "BuscarRecurso.jsp";
		window.open(url , "BuscarRecurso" , "width=600,height=500,scrollbars=YES,menubar=No,toolbar=NO,status=YES"); 
	}

	function validar() {
		var cedula = $("#cedula").val();
		if (cedula == "") {
			alert("Debe seleccionar el recurso");
			return false;
		}
		return true;
	}


</script>
</head>
<body>
<%@ include file="header.jsp" %>
<div class="contencenter demo">
<h2>PERSONAL POR GRUPO</h2>
<div id="info"></div>
<form action="" name="form1" onsubmit="javascript: return validar();" > 
		<table id="lista" border=0>
		<input Type="hidden" value="<%= (String)request.getParameter("id") %>" name="id">
		<tr>
			<th colspan="2">AGREGAR PERSONAL</th>
		</tr>
		<tr>
			<td>Nombre</td>
			<td><%= grupo.getNombre() %></td>
		</tr>
		<tr>
			<td>Recurso: </td>
			<td><input type="text" name="cedula" id="cedula" readonly><input type="text" name="nombre" id="nombre" size=40 readonly> <input type="button" name="cmd_buscar" id="cmd_buscar" value="Buscar" onclick="javascript:buscar()" ></td>
		</tr>
		</table>
	
	<input type="submit" name="cmd_agregar" id="cmd_agregar" value="Agregar">
	</form>
	<h2>Listado Personal</h2>
	<% if (!rsIsEmpty) { %>
		<table>
		<tr>
			<th>CEDULA</th>
			<th>NOMBRE</th>
			<th>ACCION</th>
		</tr>
		<% do { %>
		<tr>
			<td><%= rs.getString("cedula") %></td>
			<td><%= rs.getString("recunomb") %></td>
			<td><a href="grupos_personal.jsp?operacion=eliminar&id=<%= (String)request.getParameter("id") %>&id_personal=<%= rs.getInt("id") %>">Eliminar</a></td>
		</tr>
		
		<% }while(rs.next()); %>
		</table>
	<% } else { %>
	No hay registros.
	<% } %>
	<%@ include file="foot.jsp" %>
</body>
</html>