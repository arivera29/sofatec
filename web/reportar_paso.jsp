<%@ page import="com.are.sofatec.*" %>
<%@ page import="java.sql.*" %>
<%
	db conexion = new db();
	String orden = (String)request.getParameter("orden");
	String paso = (String)request.getParameter("paso");
	String accion = (String)request.getParameter("accion");
	String tipo = (String)request.getParameter("tipo");
	String observacion = (String)request.getParameter("observacion");
	String parent = (String)request.getParameter("parent");
	
	
	String sql = "UPDATE QO_PASOS SET CO_ACCEJE=?, OBSERVACION=?, CUMPLIDO=1, EDIT_OBS=0 WHERE NUM_OS=? AND NUM_PASO=?";
	PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
	pst.setString(1, accion);
	pst.setString(2, observacion);
	pst.setString(3, orden);
	pst.setString(4, paso);
	
	if (conexion.Update(pst) > 0) {
		// INSERTAR NUEVOS PASOS
		
				
			sql = "SELECT TIP_FLUJO FROM QO_OSFLUJO WHERE CO_ACCEJE=? AND TIP_OS=?";
			PreparedStatement pst1 = conexion.getConnection().prepareStatement(sql);
			pst1.setString(1, accion);
			pst1.setString(2, tipo);
			ResultSet rs1 = conexion.Query(pst1);
			if (rs1.next()) {
				sql = "DELETE FROM QO_NUEVOS_PASOS WHERE NUM_OS='" + orden + "' AND PARENT='" + parent + "'";
				if (conexion.Update(sql) >= 0) {
				
					String flujo = rs1.getString("TIP_FLUJO");
					sql = "SELECT * FROM QO_PASOSFLUJO WHERE TIP_FLUJO=?";
					PreparedStatement pst2 = conexion.getConnection().prepareStatement(sql);
					pst2.setString(1, flujo);
					ResultSet rs2 = conexion.Query(pst2);
					while (rs2.next()) {
						sql = "INSERT INTO QO_NUEVOS_PASOS (NUM_OS, NUM_PASO, OPCOND, DESCRIPCION, CONDICION, ELSEACCION, CUMPLIDO, CO_ACCEJE, IND_DECISOR, OBSERVACION, COBRO,PARENT,FLUJO, EDIT_OBS) VALUES (?, ?, ?, ?, ?, ?, 0, '', 1, '', 1,?,?,0)";
						PreparedStatement pst3 = conexion.getConnection().prepareStatement(sql);
						pst3.setString(1, orden);
						pst3.setString(2, rs2.getString("NUM_PASO"));
						pst3.setString(3, rs2.getString("OPCOND"));
						pst3.setString(4, rs2.getString("DESCRIPCION"));
						pst3.setString(5, rs2.getString("CONDICION"));
						pst3.setString(6, rs2.getString("ELSEACCION"));
						pst3.setString(7, accion);
						pst3.setString(8, flujo);
						conexion.Update(pst3);
					}
				}
			}
			
		
	
	
		conexion.Commit();
		out.print("OK");
	}else {
		out.print("Error al procesar la solicitud");
	}
	
	conexion.Close();

%>