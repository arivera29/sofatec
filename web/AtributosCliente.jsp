<%@page import="com.are.sofatec.db"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="validausuario.jsp"%>
<%@ page import="java.sql.*"%>
<%
	db conexion = new db();

	if (request.getParameter("cmd_agregar") != null) {
		String atributo = (String)request.getParameter("atributo");
		
		String sql = "INSERT INTO QO_ATRIBUTOS (DESCRIPCION) VALUES (?)";
		PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		pst.setString(1, atributo);
		try {
			if (conexion.Update(pst) > 0) {
				conexion.Commit();
			}
		}catch(SQLException e) {
			
		}
	}
	
	if (request.getParameter("operacion") != null) {
		String operacion = (String)request.getParameter("operacion");
		if (operacion.equals("eliminar")) {
			String sql = "DELETE FROM QO_ATRIBUTOS WHERE ID=?";
			PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
			pst.setString(1, (String)request.getParameter("id"));
			if (conexion.Update(pst) > 0) {
				conexion.Commit();
			}
			
		}
		
	}

	
	String sql = "SELECT * FROM QO_ATRIBUTOS ORDER BY ID";
	PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
	ResultSet rs = conexion.Query(pst);
	
	int count=0;
	
	
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Atributos Cliente</title>
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
function validar() {
	var atributo = $("#atributo").val();

	if (atributo == "") {
		alert("Faltan datos");
		return false;
	}
	
	return true;
}
</script>
</head>
<body>

<%@ include file="header.jsp"%>
<div class="contencenter demo">
<h2>ATRIBUTOS CLIENTE</h2>
<form action="" name="form1" method="post" onsubmit="javascript:return validar();">
<table>
	<tr>
		<th colspan="2"></th>
	</tr>
	<tr>
		<td>Atributo</td>
		<td><input type="text" value="" size=40 name="atributo" id="atributo"></td>
	</tr>
</table>
<input type="submit" name="cmd_agregar" value="Agregar">
</form>


<table>
	<tr>
		<th>Id</th>
		<th>ATRIBUTO</th>
		<th></th>
	</tr>
	<% while (rs.next()) { %>
		<tr <%= ((count%2==0)?"class='odd'":"") %>>	
			<td><%= rs.getString("ID") %></td>
			<td><%= rs.getString("DESCRIPCION") %></td>
			<td>
				<a href="AtributosCliente.jsp?id=<%= rs.getString("ID") %>&operacion=eliminar">Eliminar</a>
			</td>
		</tr>
		<% count++; %>
	<% } %>
	<tr>
		<td colspan="3">Registros: <%= count %></td>
	</tr>
</table>


</div>

</body>
</html>
<%
	conexion.Close();
%>
