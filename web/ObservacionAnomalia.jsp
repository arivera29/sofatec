<%@page import="com.are.sofatec.db"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="validausuario.jsp"%>
<%@ page import="java.sql.*"%>
<%
	db conexion = new db();

	if (request.getParameter("cmd_agregar") != null) {
		String anomalia = (String)request.getParameter("anomalia");
		String observacion = (String)request.getParameter("observacion");
		
		String sql = "INSERT INTO QO_OBSERVACION (CODIGO,OBSERVACION,TIPO) VALUES (?,?,2)";
		PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		pst.setString(1, anomalia);
		pst.setString(2, observacion);
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
			String sql = "DELETE FROM QO_OBSERVACION WHERE ID=?";
			PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
			pst.setString(1, (String)request.getParameter("id"));
			if (conexion.Update(pst) > 0) {
				conexion.Commit();
			}
			
		}
		
	}

	
	String sql = "SELECT * FROM QO_OBSERVACION WHERE TIPO=2 ORDER BY ID";
	PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
	ResultSet rs = conexion.Query(pst);
	
	sql = "SELECT COD,DESC_COD FROM QO_ANOM ORDER BY COD";
	pst = conexion.getConnection().prepareStatement(sql);
	ResultSet rsAnomalia = conexion.Query(pst);
	
	int count=0;
	
	
	
	
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Observaciones Anomalias Movil</title>
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
	var accion = $("#accion").val();
	var observacion = $("#observacion").val();
	
	if (accion == "" || observacion == "") {
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
<h2>OBSERVACIONES ANOMALIAS SMARTPHONE</h2>
<form action="" name="form1" method="post" onsubmit="javascript:return validar();">
<table>
	<tr>
		<th colspan="2"></th>
	</tr>
	<tr>
		<td>Anomalia</td>
		<td>
			<select name="anomalia" id="anomalia">
				<% while(rsAnomalia.next())  {%>
			<option value="<%= rsAnomalia.getString("COD") %>" >(<%= rsAnomalia.getString("COD") %>) <%= rsAnomalia.getString("DESC_COD") %></option>
				<% } %>
			</select>
		
		</td>
	</tr>
	<tr>
		<td>Observacion</td>
		<td><textarea rows="8" cols="40" name="observacion" id="observacion"></textarea></td>
	</tr>
</table>
<input type="submit" name="cmd_agregar" value="Agregar">
</form>


<table>
	<tr>
		<th>Id</th>
		<th>ANOMALIA</th>
		<th>OBSERVACION</th>
		<th></th>
	</tr>
	<% while (rs.next()) { %>
		<tr <%= ((count%2==0)?"class='odd'":"") %>>	
			<td><%= rs.getString("ID") %></td>
			<td><%= rs.getString("CODIGO") %></td>
			<td><%= rs.getString("OBSERVACION") %></td>
			<td>
				<a href="ObservacionAnomalia.jsp?id=<%= rs.getString("ID") %>&operacion=eliminar">Eliminar</a>
			</td>
		</tr>
		<% count++; %>
	<% } %>
	<tr>
		<td colspan="4">Registros: <%= count %></td>
	</tr>
</table>


</div>

</body>
</html>
<%
	conexion.Close();
%>
