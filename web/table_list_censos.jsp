<%@page import="java.text.SimpleDateFormat"%>
<%@ page import="com.are.sofatec.*" %>

<%
	db conexion = new db();
	String fecha_inicial = (String)request.getParameter("fecha_inicial");
	String fecha_final = (String)request.getParameter("fecha_final");
        String zona = (String)request.getParameter("zona");
        
	
	String sql = "SELECT C.FECHA_CIERRE, A.NUM_OS, C.NIC, A.ID_EQUIPO, B.DESC_EQ, A.CANTIDAD, "
                + "B.CARGA, A.CANTIDAD * B.CARGA TOTAL, C.NUM_CAMP" 
                    + " FROM QO_CENSO A "
                    + " INNER JOIN QO_EQUIPOS B ON B.ID = A.ID_EQUIPO "
                    + " INNER JOIN QO_ORDENES C ON A.NUM_OS = C.NUM_OS"
                    + " WHERE CONVERT(C.FECHA_CIERRE,DATE) BETWEEN ? AND ? ";
        
        
        
        sql += " ORDER BY FECHA_CIERRE";
			
	java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
	pst.setString(1, fecha_inicial);
	pst.setString(2, fecha_final);
	
	java.sql.ResultSet rs = conexion.Query(pst);
	boolean rsIsEmpty = !rs.next();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	int cont=0;
	
%>
<table id="Exportar_a_Excel">
	<tr>
		<th>FECHA</th>
		<th>NUM_OS</th>
		<th>NIC</th>
		<th>ID_EQUIPO</th>
		<th>EQUIPO</th>
                <th>CANTIDAD</th>
		<th>CARGA</th>
		<th>TOTAL CARGA</th>
                <th>CAMPAÑA</th>
        </tr>
	<% if (!rsIsEmpty) { 
	%>
	
	<% do { 
	%>
	<tr <%= (cont%2==0?"class=\"odd\"":"") %>>
            <td><%= format.format(rs.getTime("FECHA_CIERRE")) %></td>
                <td><%= rs.getString("NUM_OS") %></td>
		<td><%= rs.getString("NIC") %></td>
                <td><%= rs.getString("ID_EQUIPO") %></td>
		<td><%= rs.getString("DESC_EQ") %></td>
                <td><%= rs.getString("CANTIDAD") %></td>
		<td><%= rs.getString("CARGA") %></td>
                <td><%= rs.getString("TOTAL") %></td>
		<td><%= rs.getString("NUM_CAMP") %></td>

	</tr>
	<% 
		cont++;
	}while(rs.next()); %>
        <% }  %>
	</table>
        <p>
            Total Registros: <%= cont %>
        </p>
<%
	conexion.Close();
%>