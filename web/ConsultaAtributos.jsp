<%@page import="com.are.sofatec.db"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="validausuario.jsp"%>
<%@ page import="java.sql.*"%>
<%
	db conexion = new db();
	ResultSet rs = null;
	boolean rsIsEmpty = true;
	
	if (request.getParameter("cmd_consultar") != null) {
		String departamento = (String)request.getParameter("departamento");
		String atributo = (String)request.getParameter("atributo");
		
		String sql = "SELECT QO_ORDENES.*,QO_ATRIBUTOS.DESCRIPCION,RECURSO.RECUNOMB " 
				+ " FROM QO_ORDENES,QO_ATRIBUTOS,QO_CLIENTE_ATRIBUTO,RECURSO,QO_DATOSUM " 
				+ " WHERE QO_ORDENES.NIC = QO_CLIENTE_ATRIBUTO.NIC AND QO_ORDENES.NIC = QO_DATOSUM.NIC "
						+ " AND QO_ORDENES.NIS_RAD = QO_DATOSUM.NIS_RAD " 
				+ " AND QO_ATRIBUTOS.ID = QO_CLIENTE_ATRIBUTO.ID_ATRIBUTO";
			
		if (!atributo.equals("all")) {
				sql += " AND QO_ATRIBUTOS.ID ='" + atributo + "'";
		}
		if (!departamento.equals("all")) {
			sql += " AND QO_DATOSUM.DEPARTAMENTO = '" + departamento + "'";
		}
		sql += " AND ESTADO_OPER IN (0,1) AND TECNICO = RECUCODI";
		
		PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		rs = conexion.Query(sql);
		rsIsEmpty = !rs.next();
	}
	
	String sql = "SELECT DISTINCT DEPARTAMENTO FROM QO_DATOSUM";
	ResultSet rsDpto = conexion.Query(sql);	

	sql = "SELECT ID,DESCRIPCION FROM QO_ATRIBUTOS ORDER BY DESCRIPCION";
	PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
	ResultSet rsAtributos = conexion.Query(pst);
	
	int count=0;
	
	
	
	
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Observaciones Movil</title>
<link rel="stylesheet" href="themes/redmond/jquery.ui.all.css">
<link rel="stylesheet" TYPE="text/css" HREF="main.css">
<script src="js/jquery.js" type="text/javascript"></script>
<script src="ui/jquery.ui.core.js" language="JavaScript"></script>
<script src="ui/jquery.ui.widget.js" language="JavaScript"></script>
<script src="ui/jquery.ui.button.js"></script>
<script>
$(function() {
	$( "input:submit, a, button", ".demo" ).button();
	$( "input:button, a, button", ".demo" ).button();
});

</script>
<script type="text/javascript">

function selectAll() {
	$("input:checkbox").prop('checked', true);
}
function clearAll() {
	$("input:checkbox").prop('checked', false);	
}
function buscar() {
	url = "BuscarRecurso.jsp";
	window.open(url , "BuscarRecurso" , "width=600,height=500,scrollbars=YES,menubar=No,toolbar=NO,status=YES"); 
}
</script>
</head>
<body>

<%@ include file="header.jsp"%>
<div class="contencenter demo">
<h2>CONSULTA ORDENES CON ATRIBUTOS DEL CLIENTE</h2>
<form action="" name="form2">
<table>
	<tr>
		<th colspan="2"></th>
	</tr>
	<tr>
		<td>Departamento</td>
		<td>
			<select name="departamento" id="departamento">
		<option value="all">Todos</option>
			<% while (rsDpto.next()) { %>
			<option value="<%= rsDpto.getString("DEPARTAMENTO") %>"><%= rsDpto.getString("DEPARTAMENTO") %></option>
			<% } %>
		</select> 
		
		</td>
	</tr>
	<tr>
		<td>Atributos</td>
		<td>
			<select name="atributo" id="atributo">
			<option value="all">Todos</option>
			<% while (rsAtributos.next()) { %>
			<option value="<%= rsAtributos.getString("ID") %>"><%= rsAtributos.getString("DESCRIPCION") %> (<%= rsAtributos.getString("ID") %>)</option>
			<% } %>
		</select> 
		
		</td>
	</tr>
</table>
<input type="submit" name="cmd_consultar" value="Consultar">
</form>

<% if (request.getParameter("cmd_consultar") != null) { %>
<% if (!rsIsEmpty) { %>
<h2>Listado de Ordenes encontradas</h2>
<a href="javascript:selectAll()" class="boton">Seleccionar Todo</a> 
<a href="javascript:clearAll()" class="boton">Quitar Seleccion</a> 
<table>
	<tr>
		<th></th>
		<th>ORDEN</th>
		<th>LOTE</th>
		<th>DIRECCION</th>
		<th>TIPO</th>
		<th>ESTADO</th>
		<th>TECNICO</th>
		<th>ATRIBUTO</th>
	</tr>
	<% do { %>
		<tr <%= ((count%2==0)?"class='odd'":"") %>>
			<td><input type="checkbox" name="orden" value ="<%= rs.getString("NUM_OS") %>" ></td>	
			<td><%= rs.getString("NUM_OS") %></td>
			<td><%= rs.getString("NUM_LOTE") %></td>
			<td><%= rs.getString("DIRECCION") %></td>
			<td><%= rs.getString("TIP_OS") %> <%= rs.getString("DESC_TIPO_ORDEN") %></td>
			<td><%= rs.getString("ESTADO_OPER") %></td>
			<td><%= rs.getString("TECNICO") %> <%= rs.getString("RECUNOMB") %></td>
			<td><%= rs.getString("DESCRIPCION") %></td>
		</tr>
		<% count++; %>
	<% }while (rs.next()); %>
	<tr>
		<td colspan="8">Registros: <%= count %></td>
	</tr>
</table>
<h2>Asignar Recurso</h2>
<form name="form1" action="">
Tecnico: <input type="text" name="cedula" id="cedula" readonly><input type="text" name="nombre" id="nombre" size=40 readonly> <input type="button" name="cmd_buscar" id="cmd_buscar" value="Buscar" onclick="javascript:buscar()" >
</form>
<% }else { %>
No se encontraron ordenes de servicio.
<% } %>
<% } %>
</div>

</body>
</html>
<%
	conexion.Close();
%>
