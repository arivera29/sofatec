<%@ page import="com.are.sofatec.*" %>

<%
	db conexion = new db();
	String recurso = (String)request.getParameter("recurso");
	String fecha_inicial = (String)request.getParameter("fecha_inicial");
	String fecha_final = (String)request.getParameter("fecha_final");
	
	String sql = "select reportes.orden,orders.nic,tiordesc,locadesc,reportes.tipo,date(reportes.fecha) fecha, time(reportes.fecha) hora,orders.nom_via,orders.nom_calle,orders.crucero,orders.placa,orders.interior " +
		" from reportes,orders,localidad,tipo_orden " +
		" where orders.orden = reportes.orden " +
		" and orders.tipo = tiorcodi " +
		" and orders.localidad = locacodi " +
		" and reportes.recurso = ? " +
		" and date(reportes.fecha) between   ? and ?" ;
			;
	java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
	pst.setString(1, recurso);
	pst.setString(2, fecha_inicial);
	pst.setString(3, fecha_final);
	
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
		<th>Tipo orden</th>
		<th>Tipo Reporte</th>
		<th>Fecha</th>
		<th>Hora</th>
	</tr>
	<% if (!rsIsEmpty) { 
	%>
	
	<% do { 
	%>
	<tr <%= (cont%2==0?"class=\"odd\"":"") %>>
		<td><%= rs.getString("locadesc") %></td>
		<td><%= rs.getString("orden") %></td>
		<td><%= rs.getString("nic") %></td>
		<td><%= rs.getString("nom_via") + " "+ rs.getString("nom_calle") + " " + rs.getString("crucero") + " " + rs.getString("placa") + " " + rs.getString("interior") %> </td>
		<td><%= rs.getString("tiordesc") %></td>
		<td><%= rs.getString("tipo") %></td>
		<td><%= rs.getString("fecha") %></td>
		<td><%= rs.getString("hora") %></td>
	</tr>
	<% 
		cont++;
	}while(rs.next()); %>
	<tr>
		<td colspan="8">Total de ordenes: <%= cont %></td>
	</tr>
	<% } else { %>
	<tr>
		<td colspan="8">No se encontraron registros</td>
	</tr>	
	
	<% } %>
	</table>
<%
	conexion.Close();
%>