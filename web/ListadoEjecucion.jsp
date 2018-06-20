<%@ page import="com.are.sofatec.*" %>

<%
	db conexion = new db();
	String fecha_inicial = (String)request.getParameter("fecha_inicial");
	String fecha_final = (String)request.getParameter("fecha_final");
	
	String sql = "SELECT * FROM VIEW_EJECUCION WHERE FECHA BETWEEN ? AND ?";
			
	java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
	pst.setString(1, fecha_inicial);
	pst.setString(2, fecha_final);
	
	java.sql.ResultSet rs = conexion.Query(pst);
	boolean rsIsEmpty = !rs.next();
	int cont=0;
	
%>
<table id="Exportar_a_Excel">
	<tr>
		<th>Municipio</th>
		<th>Orden</th>
		<th>NIC</th>
		<th>Direccion</th>
		<th>Recurso</th>
		<th>Tipo orden</th>
		<th>Accion</th>
		<th>Actividad</th>
		<th>Descripcion Actividad</th>
		<th>Observacion</th>
		<th>Fecha</th>
		<th>Hora</th>
	</tr>
	<% if (!rsIsEmpty) { 
	%>
	
	<% do { 
	%>
	<tr <%= (cont%2==0?"class=\"odd\"":"") %>>
		<td><%= rs.getString("DESCRIPCION_LOCALIDAD") %></td>
		<td><%= rs.getString("ORDEN") %></td>
		<td><%= rs.getString("NIC") %></td>
		<td><%= rs.getString("DIRECCION") %> </td>
		<td><%= rs.getString("NOMBRE_TECNICO") %></td>
		<td><%= rs.getString("TIPO_ORDEN") %></td>
		<td><%= rs.getString("ACCION") %></td>
		<td><%= rs.getString("CODIGO_ACTIVIDAD") %></td>
		<td><%= rs.getString("DESCRIPCION_ACTIVIDAD") %></td>
		<td><%= rs.getString("OBSERVACION")==null?"":rs.getString("OBSERVACION") %></td>
		<td><%= rs.getString("FECHA") %></td>
		<td><%= rs.getString("HORA") %></td>
	</tr>
	<% 
		cont++;
	}while(rs.next()); %>
	<tr>
		<td colspan="12">Total de ordenes: <%= cont %></td>
	</tr>
	<% } else { %>
	<tr>
		<td colspan="12">No se encontraron registros</td>
	</tr>	
	
	<% } %>
	</table>
<%
	conexion.Close();
%>