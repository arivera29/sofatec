package com.are.sofatec;

import java.sql.ResultSet;
import java.sql.SQLException;

public class departamento {

	private db conexion = null;
	private String codigo;
	private String descripcion;
	private String error;
	private String key;
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public departamento() {
		this.codigo = "";
		this.descripcion = "";
	}
	public departamento(db cnt) {
		this.conexion = cnt;
		this.codigo = "";
		this.descripcion = "";
	}
	
	public db getConexion() {
		return conexion;
	}
	public void setConexion(db conexion) {
		this.conexion = conexion;
	}
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
	
	public boolean add(){
		boolean result = false;
		try {
			String sql = "insert into departamentos (depacodi,depadesc) values ('" + this.codigo + "','" + this.descripcion + "')";
			if (conexion.Update(sql) > 0) {
				conexion.Commit();
				result = true;
			}
			
		}catch (SQLException ex) {
			this.error = ex.getMessage();
		}
		return result;
	}
	public boolean modify() {
		boolean result = false;
		try {
			String sql = "update departamentos set depacodi='" + this.codigo + "',depadesc='" + this.descripcion + "' where depacodi='" + this.key + "'";
			if (conexion.Update(sql) > 0) {
				conexion.Commit();
				result = true;
			}
			
		}catch (SQLException ex) {
			this.error = ex.getMessage();
		}
		return result;	
	}
	
	public boolean remove() {
		boolean result = false;
		try {
			String sql = "delete from departamentos where depacodi='" + this.key + "'";
			if (conexion.Update(sql) > 0) {
				conexion.Commit();
				result = true;
			}
			
		}catch (SQLException ex) {
			this.error = ex.getMessage();
		}
		return result;	
	}
	
	public boolean exist() {
		boolean result = false;
		try {
			String sql = "select depacodi,depadesc from departamentos where depacodi='" + this.key + "'";
			ResultSet rs = conexion.Query(sql);
			if (rs.next()) {
				this.codigo = (String)rs.getString("depacodi");
				this.descripcion = (String)rs.getString("depadesc");
				result = true;
			}
			rs.close();
			
		}catch (SQLException ex) {
			this.error = ex.getMessage();
		}
		return result;	
	}
	
	public ResultSet list() {
		ResultSet result = null;
		try {
			String sql = "select depacodi,depadesc from departamentos order by depadesc";
			result = conexion.Query(sql);
		}catch (SQLException ex) {
			this.error = ex.getMessage();
		}
		return result;	
	}
	public String CreateSelectHTML(String id) {
		String strHtml = "<select id='" + id + "' name='" + id + "'>" ;
		ResultSet rs = list();
		try {
			if (rs.next()) {
				do {
				strHtml += "<option value='" + rs.getString("depacodi") + "'>" + rs.getString("depadesc") + "</option>";
				}while(rs.next());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			this.error = e.getMessage();
		}
		
		strHtml += "<select>";
		return strHtml;
	}
	public String CreateSelectHTML(String id,String key) {
		String strHtml = "<select id='" + id + "' name='" + id + "'>" ;
		ResultSet rs = list();
		try {
			if (rs.next()) {
				do {
					String c = "";
					if (key.equals(rs.getString("depacodi"))) c="selected";
					
				strHtml += "<option value='" + rs.getString("depacodi") + "' " + c + ">" + rs.getString("depadesc") + "</option>";
				}while(rs.next());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			this.error = e.getMessage();
		}
		
		strHtml += "<select>";
		return strHtml;
	}
	public boolean Find(String key) throws SQLException {
		boolean result = false;

			String sql = "select depacodi,depadesc from departamentos where depacodi='" + key + "'";
			ResultSet rs = conexion.Query(sql);
			if (rs.next()) {
				this.codigo = (String)rs.getString("depacodi");
				this.descripcion = (String)rs.getString("depadesc");
				result = true;
			}
			rs.close();

		return result;	
	}
	
	public boolean FindByLoca(String localidad)  throws SQLException {
		boolean result = false;

		String sql = "select depacodi,depadesc from departamentos,localidad where locadepa=depacodi and locacodi='" + localidad + "'";
		ResultSet rs = conexion.Query(sql);
		if (rs.next()) {
			this.codigo = (String)rs.getString("depacodi");
			this.descripcion = (String)rs.getString("depadesc");
			result = true;
		}
		rs.close();

	return result;	
		
	}
}
