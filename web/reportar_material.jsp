<%@ page import="com.are.sofatec.*" %>
<%@ page import="java.sql.*" %>
<%
	db conexion = new db();
	String orden = (String)request.getParameter("orden");
	String accion = (String)request.getParameter("accion");
	String material = (String)request.getParameter("material");
	String cantidad = (String)request.getParameter("cantidad");
	String cobro = (String)request.getParameter("cobro");
	String tipo = (String)request.getParameter("tipo");
	
	String sql = "INSERT INTO QO_ORDEN_MATERIAL (NUM_OS,CO_ELEMENTO,CO_ACCEJE,CANTIDAD,TIPO,COBRO) VALUES (?,?,?,?,?,?)";
	PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
	pst.setString(1, orden);
	pst.setString(2, material);
	pst.setString(3, accion);
	pst.setString(4, cantidad);
	pst.setString(5, tipo);
	pst.setString(6, cobro);
	
	if (conexion.Update(pst) > 0) {
		conexion.Commit();
		out.print("OK");
	}else {
		out.print("Error al procesar la solicitud");
	}
	
	conexion.Close();

%>