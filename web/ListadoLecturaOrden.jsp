<%@ page import="com.are.sofatec.*"%>
<%@ page import="java.sql.*"%>
<%
	request.setCharacterEncoding("UTF-8");
	String orden = (String) request.getParameter("orden");
	db conexion = new db();
	String sql = "SELECT QO_ORDEN_LECTURA.* FROM QO_ORDEN_LECTURA WHERE NUM_OS =? ";
	PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
	pst.setString(1, orden);
	ResultSet rs = conexion.Query(pst);
	boolean rsIsEmpty = !rs.next();

	int contador = 0;
	
	ManejadorUsuarios mu = new ManejadorUsuarios(conexion);
	mu.find((String)session.getAttribute("usuario"));
	Usuarios usuario = mu.getUsuario();
%>
<h2>Lectura Reportada</h2>
<%
	if (!rsIsEmpty) {
%>
<table>
	<tr>
		<th>MEDIDOR</th>
		<th>LECTURA</th>
		<th>ACCION</th>
	</tr>
	<%
		do {
	%>
	<tr <%= (++contador%2==0)?"class='odd'":"" %>>
		<td><%=(String) rs.getString("NUM_APA")%></td>
		<td><%=(String) rs.getString("LECTURA")%></td>
		<td>
			<% if (usuario.getResolver()== 1) { %>
			<a href="javascript:EliminarLectura('<%= (String)request.getParameter("orden") %>','<%= rs.getString("NUM_APA") %>');" class="boton">Eliminar</a>
			<% } %>	
		</td>
	</tr>
	<%
		} while (rs.next());
	%>
</table>
<%
	} else {
%>
No Hay informacion de Lectura.
<%
	}
	conexion.Close();
%>