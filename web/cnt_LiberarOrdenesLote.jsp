<%@ page import="com.are.sofatec.*" %>
<%@ page import="java.sql.*" %>
<%
        db conexion = new db();
	
        String operacion = (String)request.getParameter("operacion");
        try {
        if (operacion.equals("zona")) {
                String zona = (String)request.getParameter("zona");
                String sql = "UPDATE QO_ORDENES SET ESTADO_OPER=0 " +
                                " WHERE ESTADO_OPER=1 " ;
			
                        if (!zona.equals("all")) {
                                sql += " AND QO_ORDENES.NUM_ZONA = '" + zona + "'";
                        }
			
                        int filas_update = conexion.Update(sql);
                        if (filas_update > 0) {
                                conexion.Commit();
                                out.print("Ordenes liberadas "+ filas_update );
                        }else {
                                conexion.Rollback();
                                out.print("Error al liberar ordenes por zona");
                        }
		
		
        }
	
        if (operacion.equals("recurso")) {
                String recurso = (String)request.getParameter("recurso");
                String sql = "UPDATE QO_ORDENES SET ESTADO_OPER=0 " +
                                " WHERE ESTADO_OPER=1 AND TECNICO ='" + recurso + "'" ;
		
                        int filas_update = conexion.Update(sql);
                        if (filas_update > 0) {
                                conexion.Commit();
                                out.print("Ordenes liberadas "+ filas_update );
                        }else {
                                conexion.Rollback();
                                out.print("Error al liberar ordenes por recurso");
                        }
		
		
        }
	
        }catch (SQLException e) {
                out.print("Error SQL: " + e.getMessage() + " Error No. " + e.getErrorCode());
        } finally {
                try {
                        conexion.Close();
                }catch (Exception e) {
			
                }
        }
	
	
%>