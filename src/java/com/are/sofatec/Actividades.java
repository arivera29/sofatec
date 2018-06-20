package com.are.sofatec;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Actividades {
	private String codigo;
	private String descripcion;
	private int activo;
	private double valor;
	private String accion;
	
	
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
	public int getActivo() {
		return activo;
	}
	public void setActivo(int activo) {
		this.activo = activo;
	}
	public double getValor() {
		return valor;
	}
	public void setValor(double valor) {
		this.valor = valor;
	}
	public Actividades(db conexion) {
		super();
		this.conexion = conexion;
	}
	
	
	
	public String getAccion() {
		return accion;
	}
	public void setAccion(String accion) {
		this.accion = accion;
	}
	public boolean add() throws SQLException{
		boolean result = false;
			String sql = "insert into actividades (acticodi,actidesc,actiesta,activalo,actitiac) values (?,?,?,?,?)";
			java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
			pst.setString(1, this.codigo);
			pst.setString(2, this.descripcion);
			pst.setInt(3, this.activo);
			pst.setDouble(4,this.valor);
			pst.setString(5, this.accion);
			
			if (conexion.Update(pst) > 0) {
				conexion.Commit();
				result = true;
			}
		return result;
	}

	public boolean modify(String key) throws SQLException {
		boolean result = false;

			String sql = "update actividades set acticodi=?, actidesc=? , actiesta=?, activalo=?, actitiac=? where acticodi=?";
			java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
			pst.setString(1, this.codigo);
			pst.setString(2, this.descripcion);
			pst.setInt(3, this.activo);
			pst.setDouble(4,this.valor);
			pst.setString(5, this.accion);
			pst.setString(6, key);
			if (conexion.Update(pst) > 0) {
				conexion.Commit();
				result = true;
			}
			
		return result;	
	}
	
	public boolean remove(String key) throws SQLException {
		boolean result = false;

			String sql = "delete from actividades where acticodi= ?";
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
			String sql = "select acticodi,actidesc,actiesta,activalo,actitiac from actividades where acticodi= ?";
			java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
			pst.setString(1, key);
			ResultSet rs = conexion.Query(pst);
			if (rs.next()) {
				this.codigo = (String)rs.getString("acticodi");
				this.descripcion = (String)rs.getString("actidesc");
				this.activo = rs.getInt("actiesta");
				this.valor = rs.getDouble("activalo");
				this.accion = (String)rs.getString("actitiac");
				result = true;
			}
			rs.close();
			
		return result;	
	}
	
	public ResultSet List() throws SQLException {
		ResultSet result = null;
			String sql = "select * from actividades order by actidesc";
			result = conexion.Query(sql);
			return result;	
	}
	public String CreateSelectHTML(String id) {
		String strHtml = "<select id='" + id + "' name='" + id + "'>" ;
		
		try {
			ResultSet rs = List();
			if (rs.next()) {
				do {
				strHtml += "<option value='" + rs.getString("acticodi") + "'>" + rs.getString("actidesc") + "</option>";
				}while(rs.next());
			}
		} catch (SQLException e) {
			
		}
		
		strHtml += "<select>";
		return strHtml;
	}
	
	public boolean isActiva(String codigo) throws SQLException {
		boolean result = false;
		String sql = "select acticodi from actividades where acticodi= ? and actiesta=1";
		java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		pst.setString(1, codigo);
		ResultSet rs = conexion.Query(pst);
		if (rs.next()) {
			result = true;
		}
		rs.close();
		
		return result;
	}
	
	
	public String CreateSelectHTML(String id,String key) {
		String strHtml = "<select id='" + id + "' name='" + id + "'>" ;
		try {
			ResultSet rs = List();
			if (rs.next()) {
				do {
					String c = "";
					if (key.equals(rs.getString("acticodi"))) c="selected";
					
				strHtml += "<option value='" + rs.getString("acticodi") + "' " + c + ">" + rs.getString("actidesc") + "</option>";
				}while(rs.next());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
		}
		
		strHtml += "<select>";
		return strHtml;
	}
	

}
