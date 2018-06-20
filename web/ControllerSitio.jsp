<%@page import="com.are.sofatec.db"%>
<%@page import="java.sql.PreparedStatement"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="java.sql.*" %>    
<%
	
	
	if (session.getAttribute("usuario") == null) {
		out.print("La sesión ha caducado...");
		return;
	}
	
	db conexion = new db();
	String operacion = (String)request.getParameter("operacion");
	String usuario = (String)session.getAttribute("usuario");
	
	
	
	
	if (operacion.equals("agregar")) {
		
		String nombre = (String)request.getParameter("nombre");
		String descripcion = (String)request.getParameter("descripcion");
		String latitud = (String)request.getParameter("latitud");
		String longitud = (String)request.getParameter("longitud");
		
		String sql = "insert into sitios (nombre,descripcion,latitud,longitud,fecha_creacion,usuario_creacion) values (?,?,?,?,sysdate(),?)";
		PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		pst.setString(1, nombre);
		pst.setString(2, descripcion);
		pst.setString(3, latitud);
		pst.setString(4, longitud);
		pst.setString(5, usuario);
		
		if (conexion.Update(pst) > 0) {
			conexion.Commit();
			out.print("OK");
		}else {
			conexion.Rollback();
			out.print("Error al procesar la solicitud");
		}
	}
	
	if (operacion.equals("actualizar")) {
		
		String id = (String)request.getParameter("id");
		String nombre = (String)request.getParameter("nombre");
		String descripcion = (String)request.getParameter("descripcion");
		String latitud = (String)request.getParameter("latitud");
		String longitud = (String)request.getParameter("longitud");
		
		String sql = "update sitios set nombre=?,descripcion=?,latitud=?,longitud=? where id=?";
		PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		pst.setString(1, nombre);
		pst.setString(2, descripcion);
		pst.setString(3, latitud);
		pst.setString(4, longitud);
		pst.setString(5, id);
		
		if (conexion.Update(pst) > 0) {
			conexion.Commit();
			out.print("OK");
		}else {
			conexion.Rollback();
			out.print("Error al procesar la solicitud");
		}
	}
	
	if (operacion.equals("eliminar")) {
		
		String id = (String)request.getParameter("id");
		
		String sql = "delete from sitios where id=?";
		PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		pst.setString(1, id);
		
		if (conexion.Update(pst) > 0) {
			conexion.Commit();
			out.print("OK");
		}else {
			conexion.Rollback();
			out.print("Error al procesar la solicitud");
		}
	}
	

	conexion.Close();

%>