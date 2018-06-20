package com.are.sofatec;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Equipos {
	private String imei;
	private String marca;
	private int activo;
	private String recurso;
	private int tipo;
	private int foto;
	private String departamento;
	
	
	private db conexion = null;
	
	
	
	public int getFoto() {
		return foto;
	}

	public void setFoto(int foto) {
		this.foto = foto;
	}

	public String getDepartamento() {
		return departamento;
	}

	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}

	public String getRecurso() {
		return recurso;
	}

	public int getActivo() {
		return activo;
	}

	public void setActivo(int activo) {
		this.activo = activo;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	public db getConexion() {
		return conexion;
	}

	public void setConexion(db conexion) {
		this.conexion = conexion;
	}

	public int getTipo() {
		return tipo;
	}

	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

	public Equipos(db conexion) {
		super();
		this.conexion = conexion;
	}
	
	public boolean add() throws SQLException{
		boolean result = false;
			String sql = "insert into equipos (imei,marca,estado,tipo,foto,departamento) values (?,?,?,?,?,?)";
			java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
			pst.setString(1, this.imei);
			pst.setString(2, this.marca);
			pst.setInt(3, this.activo);
			pst.setInt(4, this.tipo);
			pst.setInt(5, this.foto);
			pst.setString(6, this.departamento);
			if (conexion.Update(pst) > 0) {
				conexion.Commit();
				result = true;
			}
		return result;
	}

	public boolean modify(String key) throws SQLException {
		boolean result = false;

			String sql = "update equipos set imei=?,marca=?,estado=?,tipo=?,foto=?,departamento=? where imei=?";
			java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
			pst.setString(1, this.imei);
			pst.setString(2, this.marca);
			pst.setInt(3, this.activo);
			pst.setInt(4, this.tipo);
			pst.setInt(5, this.foto);
			pst.setString(6, this.departamento);
			pst.setString(7, key);

			if (conexion.Update(pst) > 0) {
				conexion.Commit();
				result = true;
			}
			
		return result;	
	}
	
	public boolean remove(String key) throws SQLException {
		boolean result = false;

			String sql = "delete from equipos where imei=?";
			java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
			pst.setString(1, key);
			if (conexion.Update(pst) > 0) {
				conexion.Commit();
				result = true;
			}
			
		return result;	
	}
	
	public boolean asignarPDA(String imei, String recurso) throws SQLException {
		boolean result = false;

		String sql = "update equipos set recurso=?  where imei=?";
		java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		pst.setString(1, recurso);
		pst.setString(2, imei);
		if (conexion.Update(pst) > 0) {
			conexion.Commit();
			result = true;
		}
		
	return result;	
	}
	
	public boolean liberarPDA(String imei) throws SQLException {
		boolean result = false;

		String sql = "update equipos set recurso='-1'  where imei= ?";
		java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		pst.setString(1, imei);
		if (conexion.Update(pst) > 0) {
			conexion.Commit();
			result = true;
		}
		
	return result;	
	}
	
	public boolean Find(String key) throws SQLException {
		boolean result = false;
			String sql = "select imei,marca,estado,tipo,recurso,foto,departamento from equipos where imei= ?";
			java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
			pst.setString(1, key);
			ResultSet rs = conexion.Query(pst);
			if (rs.next()) {
				this.imei = (String)rs.getString("imei");
				this.marca = (String)rs.getString("marca");
				this.activo = rs.getInt("estado");
				this.tipo = rs.getInt("tipo");
				this.recurso = rs.getString("recurso");
				this.foto = rs.getInt("foto");
				this.departamento = rs.getString("departamento");
				result = true;
			}
			rs.close();
			
		return result;	
	}
	
	public ResultSet List() throws SQLException {
		ResultSet result = null;
			String sql = "select imei,marca,estado,tipo,recurso,foto,departamento from equipos order by marca";
			result = conexion.Query(sql);
			return result;	
	}
	public String CreateSelectHTML(String id) {
		String strHtml = "<select id='" + id + "' name='" + id + "'>" ;
		
		try {
			ResultSet rs = List();
			if (rs.next()) {
				do {
				strHtml += "<option value='" + rs.getString("imei") + "'>" + rs.getString("imei") + "</option>";
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
					if (key.equals(rs.getString("tiorcodi"))) c="selected";
					
				strHtml += "<option value='" + rs.getString("imei") + "' " + c + ">" + rs.getString("imei") + "</option>";
				}while(rs.next());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
		}
		
		strHtml += "<select>";
		return strHtml;
	}
	

}
