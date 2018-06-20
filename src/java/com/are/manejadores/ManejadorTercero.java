package com.are.manejadores;

import java.sql.SQLException;
import java.util.ArrayList;
import com.are.entidades.*;

import com.are.sofatec.db;

public class ManejadorTercero {
	
	private Tercero tercero;
	
	private db conexion;
	
	public db getConexion() {
		return conexion;
	}
	public void setConexion(db conexion) {
		this.conexion = conexion;
	}
	
	public Tercero getTercero() {
		return tercero;
	}

	public void setTercero(Tercero tercero) {
		this.tercero = tercero;
	}
	
	public ManejadorTercero(Tercero tercero, db conexion) {
		super();
		this.tercero = tercero;
		this.conexion = conexion;
	}
	
	public ManejadorTercero(db conexion) {
		super();
		this.conexion = conexion;
	}
	
	public boolean Add(String codigo, String nombre, int estado) throws SQLException {
		boolean result = false;
		String sql = "INSERT INTO terceros (terccodi,tercnomb,tercesta) VALUES (?,?,?)";
		java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		pst.setString(1, codigo);
		pst.setString(2, nombre);
		pst.setInt(3, estado);
		
		if (conexion.Update(pst) > 0) {
			conexion.Commit();
			result = true;
		}
		
		return result;
		
	}
	
	public boolean Add(Tercero tercero) throws SQLException {
		boolean result = false;
		String sql = "INSERT INTO terceros (terccodi,tercnomb,tercesta) VALUES (?,?,?)";
		java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		pst.setString(1, tercero.getCodigo());
		pst.setString(2, tercero.getNombre());
		pst.setInt(3, tercero.getEstado());
		
		if (conexion.Update(pst) > 0) {
			conexion.Commit();
			result = true;
		}
		
		return result;
		
	}
	public boolean Add() throws SQLException {
		boolean result = false;
		String sql = "INSERT INTO terceros (terccodi,tercnomb,tercesta) VALUES (?,?,?)";
		java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		pst.setString(1, tercero.getCodigo());
		pst.setString(2, tercero.getNombre());
		pst.setInt(3, tercero.getEstado());
		
		if (conexion.Update(pst) > 0) {
			conexion.Commit();
			result = true;
		}
		
		return result;
		
	}
	
	public boolean Update(String codigo,String nombre, int estado, int key) throws SQLException {
		boolean result = false;
		String sql = "UPDATE terceros SET terccodi=?, tercnomb=?, tercesta=? WHERE terccodi = ?";
		java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		pst.setString(1, codigo);
		pst.setString(2, nombre);
		pst.setInt(3, estado);
		pst.setInt(4, key);
		
		if (conexion.Update(pst) > 0) {
			conexion.Commit();
			result = true;
		}
		
		return result;
		
	}
	
	public boolean Update(Tercero tercero, int key) throws SQLException {
		boolean result = false;
		String sql = "UPDATE terceros SET terccodi=?, tercnomb=?, tercesta=? WHERE terccodi = ?";
		java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		pst.setString(1, tercero.getCodigo());
		pst.setString(2, tercero.getNombre());
		pst.setInt(3, tercero.getEstado());
		pst.setInt(4, key);
		
		if (conexion.Update(pst) > 0) {
			conexion.Commit();
			result = true;
		}
		
		return result;
		
	}
	
	public boolean Update(int key) throws SQLException {
		boolean result = false;
		String sql = "UPDATE terceros SET terccodi=?, tercnomb=?, tercesta=? WHERE terccodi = ?";
		java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		
		pst.setString(1, tercero.getCodigo());
		pst.setString(2, tercero.getNombre());
		pst.setInt(3, tercero.getEstado());
		pst.setInt(4, key);
		
		if (conexion.Update(pst) > 0) {
			conexion.Commit();
			result = true;
		}
		
		return result;
		
	}
	
	public boolean Remove(int key) throws SQLException {
		boolean result = false;
		String sql = "DELETE FROM terceros WHERE terccodi = ?";
		java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		pst.setInt(1, key);
		if (conexion.Update(pst) > 0) {
			conexion.Commit();
			result = true;
		}
		
		return result;
		
	}
	
	public ArrayList<Tercero> List() throws SQLException {
		ArrayList<Tercero> lista = new ArrayList<Tercero>();
		String sql = "SELECT * FROM terceros ORDER BY tercnomb";
		java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		java.sql.ResultSet rs = conexion.Query(pst);
		while (rs.next()) {
			Tercero tercero = new Tercero();
			tercero.setCodigo(rs.getString("terccodi"));
			tercero.setNombre(rs.getString("tercnomb"));
			tercero.setEstado(rs.getInt("tercesta"));
			
			lista.add(tercero);
		}
		
		return lista;
	}
	
	public boolean Find(int key) throws SQLException {
		boolean result = false;
		String sql = "SELECT * FROM  terceros WHERE terccodi = ?";
		java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		pst.setInt(1, key);
		java.sql.ResultSet rs = conexion.Query(pst);
		if (rs.next()) {
			tercero = new Tercero();
			tercero.setCodigo(rs.getString("terccodi"));
			tercero.setNombre(rs.getString("tercnomb"));
			tercero.setEstado(rs.getInt("tercesta"));
			
			result = true;
		}
		
		return result;
		
	}

}
