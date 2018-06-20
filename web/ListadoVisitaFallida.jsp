<%@ page import="com.are.sofatec.*" %>

<%
	db conexion = new db();
	String fecha_inicial = (String)request.getParameter("fecha_inicial");
	String fecha_final = (String)request.getParameter("fecha_final");
	
	String sql = "select visitafallida.orden,orders.nic,tiordesc,locadesc, " +
		"date(visitafallida.fecha) fecha, time(visitafallida.fecha) hora," +
		"orders.nom_via,orders.nom_calle,orders.crucero,orders.placa,orders.interior, " +
		"recurso.recunomb,visitafallida.causal,causales.causdesc,visitafallida.observacion " +
		" from visitafallida,orders,localidad,tipo_orden,recurso,causales " +
		" where visitafallida.orden = orders.orden " +
		" and orders.tipo = tiorcodi " +
		" and orders.localidad = locacodi " +
		" and visitafallida.recurso = recurso.recucodi " +
		" and visitafallida.causal = causales.causcodi " +
		" and date(visitafallida.fecha) between   ? and ? " +
		" order by fecha,locadesc,recunomb";
			
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
		<th>Causal</th>
		<th>Descripcion causal</th>
		<th>Observacion</th>
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
		<td><%= rs.getString("recunomb") %></td>
		<td><%= rs.getString("tiordesc") %></td>
		<td><%= rs.getString("causal") %></td>
		<td><%= rs.getString("causdesc") %></td>
		<td><%= rs.getString("observacion") %></td>
		<td><%= rs.getString("fecha") %></td>
		<td><%= rs.getString("hora") %></td>
	</tr>
	<% 
		cont++;
	}while(rs.next()); %>
	<tr>
		<td colspan="11">Total de ordenes: <%= cont %></td>
	</tr>
	<% } else { %>
	<tr>
		<td colspan="11">No se encontraron registros</td>
	</tr>	
	
	<% } %>
	</table>
<%
	conexion.Close();
%>