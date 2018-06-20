package com.are.sofatec;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Estados {
	private String codigo;
	private String descripcion;
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
	public Estados(db conexion) {
		super();
		this.conexion = conexion;
	}
	
	public boolean Find(String key) throws SQLException {
		boolean result = false;

			String sql = "select estacodi,estadesc from estados where estacodi='" + key + "'";
			ResultSet rs = conexion.Query(sql);
			if (rs.next()) {
				this.codigo = (String)rs.getString("estacodi");
				this.descripcion = (String)rs.getString("estadesc");
				result = true;
			}
			rs.close();
			
		return result;	
	}
}
