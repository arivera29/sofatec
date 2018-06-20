package com.are.manejadores;

import java.sql.SQLException;
import java.util.ArrayList;
import com.are.entidades.*;

import com.are.sofatec.db;

public class ManejadorTipoOrden {
	
	private TipoOrden tipo;
	
	private db conexion;
	
	public db getConexion() {
		return conexion;
	}
	public void setConexion(db conexion) {
		this.conexion = conexion;
	}
	
	
	public TipoOrden getTipo() {
		return tipo;
	}
	public void setTipo(TipoOrden tipo) {
		this.tipo = tipo;
	}
	public ManejadorTipoOrden(TipoOrden tipo, db conexion) {
		super();
		this.tipo = tipo;
		this.conexion = conexion;
	}
	
	public ManejadorTipoOrden(db conexion) {
		super();
		this.conexion = conexion;
	}
	
		
	public boolean Add(TipoOrden tipo) throws SQLException {
		boolean result = false;
		String sql = "INSERT INTO tipo_orden (tiorcodi,tiordesc,tioresta,tiortite,tiortiin) VALUES (?,?,?,?,?)";
		java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		pst.setString(1, tipo.getCodigo());
		pst.setString(2, tipo.getDescripcion());
		pst.setInt(3, tipo.getEstado());
		pst.setInt(4, tipo.getTiempo_max_tercero());
		pst.setInt(5, tipo.getTiempo_max_interno());
		
		if (conexion.Update(pst) > 0) {
			conexion.Commit();
			result = true;
		}
		
		return result;
		
	}
	public boolean Add() throws SQLException {
		boolean result = false;
		String sql = "INSERT INTO tipo_orden (tiorcodi,tiordesc,tioresta,tiortite,tiortiin) VALUES (?,?,?,?,?)";
		java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		pst.setString(1, tipo.getCodigo());
		pst.setString(2, tipo.getDescripcion());
		pst.setInt(3, tipo.getEstado());
		pst.setInt(4, tipo.getTiempo_max_tercero());
		pst.setInt(5, tipo.getTiempo_max_interno());
		
		if (conexion.Update(pst) > 0) {
			conexion.Commit();
			result = true;
		}
		
		return result;
		
	}
	
		
	public boolean Update(TipoOrden tipo, int key) throws SQLException {
		boolean result = false;
		String sql = "UPDATE tipo_orden SET tiorcodi=?, tiordesc=?, tioresta=?, tiortite=?, tiortiin=? WHERE tiorcodi = ?";
		java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		pst.setString(1, tipo.getCodigo());
		pst.setString(2, tipo.getDescripcion());
		pst.setInt(3, tipo.getEstado());
		pst.setInt(4, tipo.getTiempo_max_tercero());
		pst.setInt(5, tipo.getTiempo_max_interno());
		pst.setInt(6, key);
		
		if (conexion.Update(pst) > 0) {
			conexion.Commit();
			result = true;
		}
		
		return result;
		
	}
	
	public boolean Update(int key) throws SQLException {
		boolean result = false;
		String sql = "UPDATE tipo_orden SET tiorcodi=?, tiordesc=?, tioresta=?, tiortite=?, tiortiin=? WHERE tiorcodi = ?";
		java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		pst.setString(1, tipo.getCodigo());
		pst.setString(2, tipo.getDescripcion());
		pst.setInt(3, tipo.getEstado());
		pst.setInt(4, tipo.getTiempo_max_tercero());
		pst.setInt(5, tipo.getTiempo_max_interno());
		pst.setInt(6, key);
		
		if (conexion.Update(pst) > 0) {
			conexion.Commit();
			result = true;
		}
		
		return result;
		
	}
	
	public boolean Remove(int key) throws SQLException {
		boolean result = false;
		String sql = "DELETE FROM tipo_orden WHERE tiorcodi = ?";
		java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		pst.setInt(1, key);
		if (conexion.Update(pst) > 0) {
			conexion.Commit();
			result = true;
		}
		
		return result;
		
	}
	
	public ArrayList<TipoOrden> List() throws SQLException {
		ArrayList<TipoOrden> lista = new ArrayList<TipoOrden>();
		String sql = "SELECT * FROM tipo_orden ORDER BY tiordesc";
		java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		java.sql.ResultSet rs = conexion.Query(pst);
		while (rs.next()) {
			TipoOrden tipo = new TipoOrden();
			tipo.setCodigo(rs.getString("tiorcodi"));
			tipo.setDescripcion(rs.getString("tiordesc"));
			tipo.setEstado(rs.getInt("tioresta"));
			tipo.setTiempo_max_tercero(rs.getInt("tiortite"));
			tipo.setTiempo_max_interno(rs.getInt("tiortiin"));
			lista.add(tipo);
		}
		
		return lista;
	}
	
	public boolean Find(String key) throws SQLException {
		boolean result = false;
		String sql = "SELECT * FROM tipo_orden WHERE tiorcodi=?";
		java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		pst.setString(1, key);
		java.sql.ResultSet rs = conexion.Query(pst);
		if (rs.next()) {
			tipo = new TipoOrden();
			tipo.setCodigo(rs.getString("tiorcodi"));
			tipo.setDescripcion(rs.getString("tiordesc"));
			tipo.setEstado(rs.getInt("tioresta"));
			tipo.setTiempo_max_tercero(rs.getInt("tiortite"));
			tipo.setTiempo_max_interno(rs.getInt("tiortiin"));
			
			result = true;
		}
		
		return result;
		
	}

}
