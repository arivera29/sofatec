package com.are.sofatec;

import java.sql.ResultSet;
import java.sql.SQLException;

public class localidad {

	private db conexion = null;
	private String codigo;
	private String descripcion;
	private String departamento;
	private String latitud;
	private String longitud;
	
	public String getLatitud() {
		return latitud;
	}
	public void setLatitud(String latitud) {
		this.latitud = latitud;
	}
	public String getLongitud() {
		return longitud;
	}
	public void setLongitud(String longitud) {
		this.longitud = longitud;
	}
	public String getDepartamento() {
		return departamento;
	}
	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}

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
	public localidad() {
		this.codigo = "";
		this.descripcion = "";
		this.departamento = "";
		this.latitud = "0";
		this.longitud = "0";
	}
	public localidad(db cnt) {
		this.conexion = cnt;
		this.codigo = "";
		this.descripcion = "";
		this.departamento = "";
		this.latitud = "0";
		this.longitud = "0";
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
			String sql = "insert into localidad (locacodi,locadesc,locadepa,localati,localong) values (?,?,?,?,?)";
			java.sql.PreparedStatement pst = conexion.getConnection()
					.prepareStatement(sql);
			pst.setString(1, this.codigo);
			pst.setString(2, this.descripcion);
			pst.setString(3, this.departamento);
			pst.setString(4, this.latitud);
			pst.setString(5, this.longitud);
			
			if (conexion.Update(pst) > 0) {
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
			String sql = "update localidad set locacodi=?,locadesc=?,locadepa=?,localati=?,localong=? where locacodi=?";
			java.sql.PreparedStatement pst = conexion.getConnection()
					.prepareStatement(sql);
			pst.setString(1, this.codigo);
			pst.setString(2, this.descripcion);
			pst.setString(3, this.departamento);
			pst.setString(4, this.latitud);
			pst.setString(5, this.longitud);
			pst.setString(6, this.key);
			if (conexion.Update(pst) > 0) {
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
			String sql = "delete from localidad where locacodi=?";
			java.sql.PreparedStatement pst = conexion.getConnection()
					.prepareStatement(sql);
			pst.setString(1, this.key );
			if (conexion.Update(pst) > 0) {
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
			String sql = "select locacodi,locadesc,locadepa,localati,localong from localidad where locacodi=?";
			java.sql.PreparedStatement pst = conexion.getConnection()
					.prepareStatement(sql);
			pst.setString(1, this.key );
			ResultSet rs = conexion.Query(pst);
			if (rs.next()) {
				this.codigo = (String)rs.getString("locacodi");
				this.descripcion = (String)rs.getString("locadesc");
				this.departamento = (String)rs.getString("locadepa");
				this.latitud = (String)rs.getString("localati");
				this.longitud = (String)rs.getString("localong");
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
			String sql = "select locacodi,locadesc,depadesc from localidad,departamentos where locadepa = depacodi order by locadesc";
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
				strHtml += "<option value='" + rs.getString("locacodi") + "'>" + rs.getString("locadesc") +" (" + rs.getString("depadesc") + ")</option>";
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
					if (key.equals(rs.getString("locacodi"))) c="selected";
					
				strHtml += "<option value='" + rs.getString("locacodi") + "' " + c + ">" + rs.getString("locadesc") + " (" + rs.getString("depadesc") + ")</option>";
				}while(rs.next());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			this.error = e.getMessage();
		}
		
		strHtml += "<select>";
		return strHtml;
	}
	public String CreateSelectHTML(String id,String departamento,String key) {
		String strHtml = "<select id='" + id + "' name='" + id + "'>" ;
		
		try {
			ResultSet rs = conexion.Query("select locacodi,locadesc from localidad where locadepa='" + departamento +"' order by locadesc");
			if (rs.next()) {
				do {
					String c = "";
					if (key.equals(rs.getString("locacodi"))) c="selected";
					
				strHtml += "<option value='" + rs.getString("locacodi") + "' " + c + ">" + rs.getString("locadesc") + "</option>";
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

			String sql = "select locacodi,locadesc,locadepa,localati,localong from localidad where locacodi=?";
			java.sql.PreparedStatement pst = conexion.getConnection()
					.prepareStatement(sql);
			pst.setString(1, key );
			ResultSet rs = conexion.Query(pst);
			if (rs.next()) {
				this.codigo = (String)rs.getString("locacodi");
				this.descripcion = (String)rs.getString("locadesc");
				this.departamento = (String)rs.getString("locadepa");
				this.latitud = (String)rs.getString("localati");
				this.longitud = (String)rs.getString("localong");
				result = true;
			}
			rs.close();
			
		return result;	
	}
}
