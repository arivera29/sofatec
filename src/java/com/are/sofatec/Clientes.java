package com.are.sofatec;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Clientes {
	private String nic;
	private String longitud;
	private String latitud;
	private String clienteAgresivo;
	private String predioEnrejado;
	private String conf_especial;
	private String med_alto;
	
	
	private db conexion = null;
	
	public String getNic() {
		return nic;
	}
	public void setNic(String nic) {
		this.nic = nic;
	}
	
	public String getLongitud() {
		return longitud;
	}
	public void setLongitud(String longitud) {
		this.longitud = longitud;
	}
	public String getLatitud() {
		return latitud;
	}
	public void setLatitud(String latitud) {
		this.latitud = latitud;
	}
	
	public String getClienteAgresivo() {
		return clienteAgresivo;
	}
	public void setClienteAgresivo(String clienteAgresivo) {
		this.clienteAgresivo = clienteAgresivo;
	}
	public String getPredioEnrejado() {
		return predioEnrejado;
	}
	public void setPredioEnrejado(String predioEnrejado) {
		this.predioEnrejado = predioEnrejado;
	}
	
	public String getConf_especial() {
		return conf_especial;
	}
	public void setConf_especial(String conf_especial) {
		this.conf_especial = conf_especial;
	}
	public String getMed_alto() {
		return med_alto;
	}
	public void setMed_alto(String med_alto) {
		this.med_alto = med_alto;
	}
	public db getConexion() {
		return conexion;
	}
	public void setConexion(db conexion) {
		this.conexion = conexion;
	}
	
	public Clientes(db conexion) {
		super();
		this.conexion = conexion;
		this.latitud= "0";
		this.longitud="0";
		this.clienteAgresivo="";
		this.predioEnrejado="";
		this.conf_especial ="";
		this.med_alto = "";
	}
	
	public boolean add() throws SQLException{
		boolean result = false;
			String sql = "insert into clientes (nic,latitud,longitud,cliente_agresivo,predio_enrejado,conf_especial,med_alto) values (?,?,?,?,?,?,?)";
			java.sql.PreparedStatement pst =  this.getConexion().getConnection().prepareStatement(sql);
			pst.setString(1, this.nic);
			pst.setString(2, this.latitud);
			pst.setString(3, this.longitud);
			pst.setString(4, this.clienteAgresivo);
			pst.setString(5, this.predioEnrejado);
			pst.setString(6, this.conf_especial);
			pst.setString(7, this.med_alto);
			
			if (conexion.Update(pst) > 0) {
				conexion.Commit();
				result = true;
			}
		return result;
	}
	public boolean modify(String key) throws SQLException{
		boolean result = false;
			String sql = "update clientes set nic=?, latitud =?, longitud=?, cliente_agresivo=?, predio_enrejado=?, conf_espeacial=?, med_alto=? where nic=?";
			java.sql.PreparedStatement pst =  this.getConexion().getConnection().prepareStatement(sql);
			pst.setString(1, this.nic);
			pst.setString(2, this.latitud);
			pst.setString(3,this.longitud);
			pst.setString(4, this.clienteAgresivo);
			pst.setString(5, this.predioEnrejado);
			pst.setString(6, this.conf_especial);
			pst.setString(7, this.med_alto);
			pst.setString(8,key);
			if (conexion.Update(pst) > 0) {
				conexion.Commit();
				result = true;
			}
		return result;
	}
	
	public boolean UpdateInfo(String key, String clienteAgresivo, String predioEnrejado, String conf_especial, String med_alto) throws SQLException{
		boolean result = false;
			String sql = "update clientes set cliente_agresivo=?, predio_enrejado=?,conf_especial=?,med_alto=? where nic=?";
			java.sql.PreparedStatement pst =  this.getConexion().getConnection().prepareStatement(sql);
			pst.setString(1, clienteAgresivo);
			pst.setString(2, predioEnrejado);
			pst.setString(3, conf_especial);
			pst.setString(4, med_alto);
			pst.setString(5,key);
			if (conexion.Update(pst) > 0) {
				conexion.Commit();
				result = true;
			}
		return result;
	}
	
	public boolean UpdateUbicacion(String key, String latitud, String longitud) throws SQLException{
		boolean result = false;
			String sql = "update clientes set cliente_agresivo=?, predio_enrejado=? where nic=?";
			java.sql.PreparedStatement pst =  this.getConexion().getConnection().prepareStatement(sql);
			pst.setString(1, latitud);
			pst.setString(2, longitud);
			pst.setString(3,key);
			if (conexion.Update(pst) > 0) {
				conexion.Commit();
				result = true;
			}
		return result;
	}
	
	
	public boolean remove(String key) throws SQLException {
		boolean result = false;

			String sql = "delete from clientes where nic=?";
			java.sql.PreparedStatement pst =  this.getConexion().getConnection().prepareStatement(sql);
			pst.setString(1, key);
			if (conexion.Update(pst) > 0) {
				conexion.Commit();
				result = true;
			}
			
		return result;	
	}
	
	public boolean Find(String key) throws SQLException {
		boolean result = false;
			String sql = String.format ("select * from clientes where nic='%s'",key);
			ResultSet rs = conexion.Query(sql);
			if (rs.next()) {
				this.nic = (String)rs.getString("nic");
				this.longitud  = (String)rs.getString("longitud");
				this.latitud = (String)rs.getString("latitud");
				this.clienteAgresivo = (String)rs.getString("cliente_agresivo");
				this.predioEnrejado = (String)rs.getString("predio_enrejado");
				this.conf_especial = (String)rs.getString("conf_especial");
				this.med_alto = (String)rs.getString("med_alto");
				result = true;
			}
			rs.close();
			
		return result;	
	}
	
	public ResultSet List() throws SQLException {
		ResultSet result = null;
			String sql = "select * from clientes order by nic";
			result = conexion.Query(sql);
			return result;	
	}
	
}
