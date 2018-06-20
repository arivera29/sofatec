package com.are.sofatec;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Marcas {
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
	public Marcas(db conexion) {
		super();
		this.conexion = conexion;
	}
	
	public boolean add() throws SQLException{
		boolean result = false;
			String sql = String.format("insert into marcas (marccodi,marcdesc) values ('%s','%s')",this.codigo,this.descripcion);
			if (conexion.Update(sql) > 0) {
				conexion.Commit();
				result = true;
			}
		return result;
	}

	public boolean modify(String key) throws SQLException {
		boolean result = false;

			String sql = String.format("update marcas set marccodi='%s',marcdesc='%s' where marccodi='%s'",this.codigo,this.descripcion,key);
			if (conexion.Update(sql) > 0) {
				conexion.Commit();
				result = true;
			}
			
		return result;	
	}
	
	public boolean remove(String key) throws SQLException {
		boolean result = false;

			String sql = String.format("delete from marcas where marccodi='%s'",key);
			if (conexion.Update(sql) > 0) {
				conexion.Commit();
				result = true;
			}
			
		return result;	
	}
	
	public boolean Find(String key) throws SQLException {
		boolean result = false;
			String sql = String.format ("select marccodi,marcdesc from marcas where marccodi='%s'",key);
			ResultSet rs = conexion.Query(sql);
			if (rs.next()) {
				this.codigo = (String)rs.getString("marccodi");
				this.descripcion = (String)rs.getString("marcdesc");
				result = true;
			}
			rs.close();
			
		return result;	
	}
	
	public ResultSet List() throws SQLException {
		ResultSet result = null;
			String sql = "select marccodi,marcdesc from marcas order by marcdesc";
			result = conexion.Query(sql);
			return result;	
	}
	public String CreateSelectHTML(String id) {
		String strHtml = "<select id='" + id + "' name='" + id + "'>" ;
		
		try {
			ResultSet rs = List();
			if (rs.next()) {
				do {
				strHtml += "<option value='" + rs.getString("marccodi") + "'>" + rs.getString("marcdesc") + "</option>";
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
					if (key.equals(rs.getString("marccodi"))) c="selected";
					
				strHtml += "<option value='" + rs.getString("marccodi") + "' " + c + ">" + rs.getString("marcdesc") + "</option>";
				}while(rs.next());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
		}
		
		strHtml += "<select>";
		return strHtml;
	}
	

}
