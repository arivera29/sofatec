<%@ page import="com.are.sofatec.*"%>
<%@ page import="java.sql.*"%>
<%
	request.setCharacterEncoding("UTF-8");
	String orden = (String)request.getParameter("orden");
	db conexion = new db();
	String sql ="SELECT QO_ORDEN_PRECINTOS.* FROM QO_ORDEN_PRECINTOS WHERE NUM_OS =?";
	PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
	pst.setString(1, orden);
	ResultSet rsPrecintosReporte= conexion.Query(pst);
	boolean rsPrecintosReporteIsEmpty = !rsPrecintosReporte.next();
	int contador=0;
	
	ManejadorUsuarios mu = new ManejadorUsuarios(conexion);
	mu.find((String)session.getAttribute("usuario"));
	Usuarios usuario = mu.getUsuario();
	
%>
<% if (!rsPrecintosReporteIsEmpty) { %>
			<table>
				<tr>
					<th>MEDIDOR</th>
					<th>PRECINTO</th>
					<th>AGREGADO</th>
					<th>ACCION</th>
				</tr>
				<% do { %>
				<tr <%= (++contador%2==0)?"class='odd'":"" %>>
					<td><%= (String)rsPrecintosReporte.getString("NUM_APA") %></td>
					<td><strong><%= (String)rsPrecintosReporte.getString("NUM_PRECIN") %></strong></td>
					<td><%= (String)rsPrecintosReporte.getString("AGREGADO") %></td>
					<td>
						<% if (usuario.getResolver()== 1) { %>
							<a href="javascript:EliminarSello('<%= (String)request.getParameter("orden") %>','<%= rsPrecintosReporte.getInt("ID") %>')" class="boton">Eliminar</a>
						<% } %>
					</td>
				</tr>
				<% }while(rsPrecintosReporte.next()); %>
			</table>
			<% } else { %>
				No Hay informacion de precintos.
			<% } %>
<%
	conexion.Close();
%>