<%@ page import="com.are.sofatec.*"%>
<%@ page import="java.sql.*"%>
<%
	request.setCharacterEncoding("UTF-8");
	String orden = (String)request.getParameter("orden");
	db conexion = new db();
	String sql ="SELECT QO_NUEVOS_PASOS.* "
			+ " FROM QO_NUEVOS_PASOS " 
			+ " WHERE QO_NUEVOS_PASOS.NUM_OS =? ";
	PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
	pst.setString(1, orden);
	ResultSet rsPasos= conexion.Query(pst);
	boolean rsPasosIsEmpty = !rsPasos.next();
	int contador=0;
	
	ManejadorUsuarios mu = new ManejadorUsuarios(conexion);
	mu.find((String)session.getAttribute("usuario"));
	Usuarios usuario = mu.getUsuario();
	
%>
<h2>Nuevos Pasos</h2>
<% if (!rsPasosIsEmpty) { %>
		<table>
			<tr>
				<th>PASO</th>
				<th>CONFIRMADO</th>
				<th>ACCION</th>
				<th>DESCRIPCION</th>
				<th>OBSERVACION</th>
				<th colspan="3"></th>
			</tr>
			<%
				do {
			%>
			<tr <%= (++contador%2==0)?"class='odd'":"" %>>
				<td>
					<div class="pasos"><%=(String)rsPasos.getString("DESCRIPCION")%></div>
					<% 
						if (rsPasos.getString("CO_ACCEJE").equals("")) {
							out.println("<img src='images/alerta.png'>");
						}
					%>
				</td>
				<td><%=rsPasos.getInt("CUMPLIDO") == 1 ? "Si" : "No"%></td>
				<td><%=(String) rsPasos.getString("CO_ACCEJE")%></td>
				<% if (!rsPasos.getString("CO_ACCEJE").equals("")) { %>
				<%
					sql = "SELECT QO_OSACCIONFLUJO.DESC_COD FROM QO_OSACCIONFLUJO WHERE NUM_PASO =? AND TIP_OS=? AND CO_ACCEJE=?";
									pst = conexion.getConnection()
											.prepareStatement(sql);
									pst.setString(1, rsPasos.getString("NUM_PASO"));
									pst.setString(2, (String) request.getParameter("tipo"));
									pst.setString(3, rsPasos.getString("CO_ACCEJE"));
									ResultSet rsAccion = conexion.Query(pst);
									if (rsAccion.next()) {
										out.print("<td>" + rsAccion.getString("DESC_COD") + "</td>");
									} else {
										out.print("<td></td>");
									}
				%>
				<% } else { %>
				<td></td>
				<% } %>
				<td>
					<%=(String) rsPasos.getString("OBSERVACION")%>
					<% 	if (!rsPasos.getString("CO_ACCEJE").equals("") && rsPasos.getInt("EDIT_OBS")==0) { %>
					<a href="javascript:ModificarObservacion(<%= rsPasos.getString("ID") %>,2,'<%=(String) request.getParameter("tipo")%>')">Modificar</a>
					<% } %>
					</td>
				<td>
					<% if (usuario.getResolver()== 1) { %>
						<a 	href="javascript:AccionNuevoPasos('<%=(String) request.getParameter("orden")%>','<%=(String) rsPasos.getString("NUM_PASO")%>','<%=(String) request.getParameter("tipo")%>','<%= rsPasos.getString("FLUJO") %>');" class="boton" title="Acciones">ACC</a>
					<% } %> 
					
				</td>
				<td>
					<% if (usuario.getResolver()== 1) { %>
					<% 	if (!rsPasos.getString("CO_ACCEJE").equals("")) { %>
						<a href="javascript:Materiales('<%=(String) request.getParameter("orden")%>','<%=rsPasos.getString("CO_ACCEJE")%>',1);" class="boton" title="Material Instalado">MI</a>
					<% } %>
					<% } %>
				</td>
				<td>
				<% if (usuario.getResolver()== 1) { %>
					<% 	if (!rsPasos.getString("CO_ACCEJE").equals("")) { %>
						<a href="javascript:Materiales('<%=(String) request.getParameter("orden")%>','<%=rsPasos.getString("CO_ACCEJE")%>',2);" class="boton" title="Material Retirado">MR</a> 
					<% 	} %>
				<% } %>
				</td>
			</tr>
			<% } while (rsPasos.next());%>
		</table>
		<% 	} else {%>
			<p>No Hay informacion de Pasos.</p>
		<% }%>
		
<%
	conexion.Close();
%>