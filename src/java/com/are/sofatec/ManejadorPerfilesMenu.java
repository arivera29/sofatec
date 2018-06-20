package com.are.sofatec;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ManejadorPerfilesMenu{
	private PerfilesMenu perfil;
	private db conexion = null;

	public ManejadorPerfilesMenu(db conexion) {
		this.conexion = conexion;
		// TODO Auto-generated constructor stub
	}

	
	public PerfilesMenu getPerfil() {
		return perfil;
	}


	public void setPerfil(PerfilesMenu perfil) {
		this.perfil = perfil;
	}


	public boolean add() throws SQLException {
		boolean result = false;
		String sql = "insert into perfiles_menu (id_perfil,id_menu) values (?,?)";
		java.sql.PreparedStatement pst = conexion.getConnection()
				.prepareStatement(sql);
		pst.setString(1, perfil.getIdperfil());
		pst.setString(2, perfil.getIdmenu());
		if (conexion.Update(pst) > 0) {
			result = true;
		}
		return result;
	}
	
	public boolean removeAll(String perfil) throws SQLException {
		boolean result = false;
		String sql = "delete from perfiles_menu where id_perfil = ?";
		java.sql.PreparedStatement pst = conexion.getConnection()
				.prepareStatement(sql);
		pst.setString(1, perfil);
		if (conexion.Update(pst) >= 0) {
			result = true;
		}
		return result;
		
	}
	public boolean add(ArrayList<PerfilesMenu> perfiles) throws SQLException {
		boolean result = false;
		if (perfiles.size() > 0 ) {
			boolean pasa = true;
			for (PerfilesMenu m : perfiles) {
				String sql = "insert into perfiles_menu (id_perfil,id_menu) values (?,?)";
				java.sql.PreparedStatement pst = conexion.getConnection()
						.prepareStatement(sql);
				pst.setString(1, m.getIdperfil());
				pst.setString(2, m.getIdmenu());
				if (conexion.Update(pst) <= 0) {
					pasa = false;
					break;
				}
			}
			if (pasa) {
				result = true;
			}
		}
		return result;
	}
	
	public boolean isCreated(String perfil, String menu) throws SQLException {
		boolean result = false;
		String sql = "select id from perfiles_menu where id_perfil=? and id_menu=?";
		java.sql.PreparedStatement pst = conexion.getConnection()
				.prepareStatement(sql);
		pst.setString(1, perfil);
		pst.setString(2, menu);
		ResultSet rs = conexion.Query(pst);
		if (rs.next()) {
			result = true;
		}
		rs.close();
		return result;
	}
	
	
	

}
