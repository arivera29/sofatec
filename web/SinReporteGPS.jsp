<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ include file="validausuario.jsp"%>
<%@ page import="com.are.sofatec.*" %>
<%@ page import="com.are.manejadores.*" %>
<%@ page import="com.are.entidades.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.sql.*" %>
<%
	
	db conexion = new db();
	ManejadorGrupo manejador = new ManejadorGrupo(conexion);
	ArrayList<Grupo> grupos = manejador.List();

	boolean rsIsEmpty = true;
	ResultSet rs = null;
	
	if (request.getParameter("enviar")!= null) {
		String grupo = (String)request.getParameter("grupo");
		String sql = "SELECT imei,recurso,recunomb,SinReporte(imei,recurso) tiempo " 
				+ " FROM equipos,recurso " 
				+ " WHERE recurso = recucodi "
				+ " AND estado=1 "
				+ " ORDER BY recunomb";
		
		if (!grupo.equals("all")) {
			sql = "SELECT imei,recurso,recunomb,SinReporte(imei,recurso) tiempo "
					+ " FROM equipos,recurso,grupos_personal "
					+ " WHERE recurso = recucodi "
					+ " AND cedula=recurso " 
					+ " AND estado=1 "
					+ " AND grupo = '" + grupo + "' "
					+ " ORDER BY recunomb";
		}
		
		PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		rs = conexion.Query(pst);
		rsIsEmpty = !rs.next();
	
	}
	int fila=0;
%>
<html>
<head>
<title>EQUIPOS SIN REPORTE DE GPS</title>
<link rel="stylesheet" href="themes/redmond/jquery.ui.all.css">
<LINK REL="stylesheet" TYPE="text/css" HREF="main.css">
<script src="js/jquery.js" language="JavaScript"></script>
<script src="ui/jquery.ui.core.js" language="JavaScript"></script>
<script src="ui/jquery.ui.widget.js" language="JavaScript"></script>
<script src="ui/jquery.ui.button.js"></script>

<script type="text/javascript">
$(function() {
	$( ".boton" ).button();
	
});
</script>
<script>
function verMapa(imei) {
	var url ="UbicacionEquipo.jsp?imei=" + imei;
	window.open(url , "MapaUbicacion" , "width=800,height=500,scrollbars=YES,menubar=No,toolbar=NO,status=YES");
	
}
function Eventos(imei) {
	var url ="EventosEquipo.jsp?imei=" + imei;
	window.open(url , "EventosEquipo" , "width=800,height=500,scrollbars=YES,menubar=No,toolbar=NO,status=YES");
	
}

</script>
</head>
<body>
<%@ include file="header.jsp" %>
<div class="contencenter demo">
<h2>ESTADO REPORTES POSICION</h2>
<form name="form1" action="" method="get">
Grupos : 
<select name="grupo">
	<option value="all">Todos</option>
	<%  for (Grupo grupo : grupos) { %>
	<option value="<%= grupo.getId() %>"><%= grupo.getNombre() %></option>	
	<% } %>
</select>
<input type="submit" name="enviar" value="Consultar" class="boton">
</form>
<% if (request.getParameter("enviar") != null) { %>
<% if (!rsIsEmpty) { %>
<table>
<tr>
	<th></th>
	<th>IMEI</th>
	<th>Cedula </th>
	<th>Nombre</th>
	<th>Tiempo (min)</th>
	<th>Ubicacion</th>
</tr>
<% do { %>
	<tr <%=fila % 2 == 0 ? "class='odd'" : ""%>>
	<td><img src="images/tecnico.png"></td>
	<td><%= rs.getString("imei") %></td>
	<td><%= rs.getString("recurso") %></td>
	<td><%= rs.getString("recunomb") %></td>
	<td>
		
		<% 
			if (rs.getInt("tiempo") == -1) {
				out.print("SIN REPORTE");
			}else {
				out.print(rs.getInt("tiempo"));
			}
		
		%>
		<% if (rs.getInt("tiempo") >= 15 || rs.getInt("tiempo") == -1 ) { %>
			<img src="images/alerta.gif">
		<% } %>
	
	</td>
	<td>
		<a href="javascript:verMapa('<%= rs.getString("imei") %>')" class="boton">Ubicacion</a>
		<a href="javascript:Eventos('<%= rs.getString("imei") %>')" class="boton">Eventos</a>
	</td>
	</tr>
	<% fila++; %>
<% }while(rs.next()); %>
<tr>
	<td colspan="6">Total tecnicos: <%= fila %></td>
</tr>
</table>
<% }  %>
<% }  %>
</div>
</body>
</html>
<%  conexion.Close(); %>
