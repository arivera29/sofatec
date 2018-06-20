<%@ page import="com.are.sofatec.*"%>
<%@ page import="java.sql.*"%>
<%
	request.setCharacterEncoding("UTF-8");
	String orden = (String) request.getParameter("orden");
	db conexion = new db();
	String sql = "SELECT QO_ORDEN_MATERIAL.*, QO_DESC_MR.DESC_ELEMENTO "
			+ " FROM QO_ORDEN_MATERIAL,QO_DESC_MR "
			+ " WHERE NUM_OS =? "
			+ " AND QO_ORDEN_MATERIAL.CO_ELEMENTO = QO_DESC_MR.CO_ELEMENTO "
			+ " AND QO_ORDEN_MATERIAL.TIPO=2";
	PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
	pst.setString(1, orden);
	ResultSet rsMaterial = conexion.Query(pst);
	boolean rsMaterialIsEmpty = !rsMaterial.next();

	int contador = 0;
	
	ManejadorUsuarios mu = new ManejadorUsuarios(conexion);
	mu.find((String)session.getAttribute("usuario"));
	Usuarios usuario = mu.getUsuario();
	
%>
<h2>Material Retirado</h2>
<%
	if (!rsMaterialIsEmpty) {
%>
<table>
	<tr>
		<th>ACCION</th>
		<th>COD ELEMENTO</th>
		<th>DESCRIPCION</th>
		<th>CANTIDAD</th>
		<th>TIPO</th>
		<th>COBRO</th>
		<th>ACCION</th>
	</tr>
	<%
		do {
	%>
	<tr <%= (++contador%2==0)?"class='odd'":"" %>>
		<td><%=(String) rsMaterial.getString("CO_ACCEJE")%></td>
		<td><%=(String) rsMaterial.getString("CO_ELEMENTO")%></td>
		<td><%=(String) rsMaterial.getString("DESC_ELEMENTO")%></td>
		<td><%=rsMaterial.getDouble("CANTIDAD")%></td>
		<td><%=rsMaterial.getInt("TIPO") == 1 ? "Instalado": "Retirado"%></td>
		<td><%=rsMaterial.getInt("COBRO") == 1 ? "Si" : "No"%></td>
		<td>
			<% if (usuario.getResolver()== 1) { %>
				<a href="javascript:EliminarMaterialRetirado('<%= (String)request.getParameter("orden") %>','<%= rsMaterial.getString("ID") %>');" class="boton">Eliminar</a>
			<% } %>
		</td>
	</tr>
	<%
		} while (rsMaterial.next());
	%>
</table>
<%
	} else {
%>
No Hay informacion de Materiales Retirados.
<%
	}
	conexion.Close();
%>