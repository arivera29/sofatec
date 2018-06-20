package com.are.sofatec;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Causales {
	private String codigo;
	private String descripcion;
	private int activo;
	
	private db conexion = null;
	
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public db getConexion() {
		return conexion;
	}
	public void setConexion(db conexion) {
		this.conexion = conexion;
	}
	public Causales(db conexion) {
		super();
		this.conexion = conexion;
	}
	
	
	public int getActivo() {
		return activo;
	}
	public void setActivo(int activo) {
		this.activo = activo;
	}
	public boolean add() throws SQLException{
		boolean result = false;
		String sql = "insert into causales (causcodi,causdesc,causacti) values (?,?,?)";
		java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		pst.setString(1, this.codigo);
		pst.setString(2, this.descripcion);
		pst.setInt(3, this.activo);
		if (conexion.Update(pst) > 0) {
			conexion.Commit();
			result = true;
		}
	return result;
	}

	public boolean modify(String key) throws SQLException {
		boolean result = false;

		String sql = "update causales set causcodi=?,causdesc=?, causacti=? where causcodi=?";
		java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		pst.setString(1, this.codigo);
		pst.setString(2, this.descripcion);
		pst.setInt(3, this.activo);
		pst.setString(4, key);
		if (conexion.Update(pst) > 0) {
			conexion.Commit();
			result = true;
		}
		
	return result;	
	}
	
	public boolean remove(String key) throws SQLException {
		boolean result = false;

		String sql = "delete from causales where causcodi=?";
		java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		pst.setString(1, key);
		if (conexion.Update(pst) > 0) {
			conexion.Commit();
			result = true;
		}
		
	return result;
	}
	
	public boolean Find(String key) throws SQLException {
		boolean result = false;
			String sql = "select causcodi,causdesc,causacti from causales where causcodi=?";
			java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
			pst.setString(1, key);
			ResultSet rs = conexion.Query(pst);
			if (rs.next()) {
				this.codigo = (String)rs.getString("causcodi");
				this.descripcion = (String)rs.getString("causdesc");
				this.activo = rs.getInt("causacti");
				result = true;
			}
			rs.close();
			
		return result;	
	}
	
	public ResultSet List() throws SQLException {
		ResultSet result = null;
			String sql = "select causcodi,causdesc, causacti from causales order by causdesc";
			result = conexion.Query(sql);
			return result;	
	}
	
	public ResultSet ListActivas() throws SQLException {
		ResultSet result = null;
			String sql = "select causcodi,causdesc, causacti from causales where causacti = 1 order by causdesc";
			result = conexion.Query(sql);
			return result;	
	}
	
	public boolean isActiva(String codigo) throws SQLException {
		boolean result = false;
		String sql = "select causcodi from causales where causcodi=? and causacti=1";
		java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		pst.setString(1, codigo);
		ResultSet rs = conexion.Query(pst);
		if (rs.next()) {
			result = true;
		}
		rs.close();
		return result;
	}
	
	public String CreateSelectHTML(String id) {
		String strHtml = "<select id='" + id + "' name='" + id + "'>" ;
		
		try {
			ResultSet rs = List();
			if (rs.next()) {
				do {
				strHtml += "<option value='" + rs.getString("causcodi") + "'>" + rs.getString("causdesc") + "</option>";
				}while(rs.next());
			}
		} catch (SQLException e) {
			
		}
		
		strHtml += "<select>";
		return strHtml;
	}
	public String CreateSelectHTML(String id,String key) {
		String strHtml = "<select id='" + id + "' name='" + id + "'>" ;
		try {
			ResultSet rs = List();
			if (rs.next()) {
				do {
					String c = "";
					if (key.equals(rs.getString("causcodi"))) c="selected";
					
				strHtml += "<option value='" + rs.getString("causcodi") + "' " + c + ">" + rs.getString("causdesc") + "</option>";
				}while(rs.next());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
		}
		
		strHtml += "<select>";
		return strHtml;
	}
	

}
