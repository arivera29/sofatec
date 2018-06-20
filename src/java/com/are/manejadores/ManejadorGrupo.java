package com.are.manejadores;

import java.sql.SQLException;
import java.util.ArrayList;

import com.are.entidades.Grupo;
import com.are.sofatec.Personal;
import com.are.sofatec.db;

public class ManejadorGrupo {
	private Grupo grupo;
	private db conexion;
	public Grupo getGrupo() {
		return grupo;
	}
	public void setGrupo(Grupo grupo) {
		this.grupo = grupo;
	}
	public db getConexion() {
		return conexion;
	}
	public void setConexion(db conexion) {
		this.conexion = conexion;
	}
	public ManejadorGrupo(Grupo grupo, db conexion) {
		super();
		this.grupo = grupo;
		this.conexion = conexion;
	}
	public ManejadorGrupo(db conexion) {
		super();
		this.conexion = conexion;
	}
	
	public boolean Add(String nombre, int estado) throws SQLException {
		boolean result = false;
		String sql = "INSERT INTO grupos (grupnomb,grupesta) VALUES (?,?)";
		java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		
		pst.setString(1, nombre);
		pst.setInt(2, estado);
		
		if (conexion.Update(pst) > 0) {
			conexion.Commit();
			result = true;
		}
		
		return result;
		
	}
	
	public boolean Add(Grupo grupo) throws SQLException {
		boolean result = false;
		String sql = "INSERT INTO grupos (grupnomb,grupesta) VALUES (?,?)";
		java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		
		pst.setString(1, grupo.getNombre());
		pst.setInt(2, grupo.getEstado());
		if (conexion.Update(pst) > 0) {
			conexion.Commit();
			result = true;
		}
		
		return result;
		
	}
	public boolean Add() throws SQLException {
		boolean result = false;
		String sql = "INSERT INTO grupos (grupnomb,grupesta) VALUES (?,?)";
		java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		
		pst.setString(1, grupo.getNombre());
		pst.setInt(2, grupo.getEstado());
		
		if (conexion.Update(pst) > 0) {
			conexion.Commit();
			result = true;
		}
		
		return result;
		
	}
	
	public boolean Update(String nombre, int estado, int key) throws SQLException {
		boolean result = false;
		String sql = "UPDATE grupos SET grupnomb=?, grupesta=? WHERE grupcodi = ?";
		java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		
		pst.setString(1, nombre);
		pst.setInt(2, estado);
		pst.setInt(3, key);
		
		if (conexion.Update(pst) > 0) {
			conexion.Commit();
			result = true;
		}
		
		return result;
		
	}
	
	public boolean Update(Grupo grupo, int key) throws SQLException {
		boolean result = false;
		String sql = "UPDATE grupos SET grupnomb=?, grupesta=? WHERE grupcodi = ?";
		java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		
		pst.setString(1, grupo.getNombre());
		pst.setInt(2, grupo.getEstado());
		pst.setInt(3, key);
		
		if (conexion.Update(pst) > 0) {
			conexion.Commit();
			result = true;
		}
		
		return result;
		
	}
	
	public boolean Update(int key) throws SQLException {
		boolean result = false;
		String sql = "UPDATE grupos SET gruponomb=?,grupesta=? WHERE grupocodi = ?";
		java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		
		pst.setString(1, grupo.getNombre());
		pst.setInt(2, grupo.getEstado());
		pst.setInt(3, key);
		
		if (conexion.Update(pst) > 0) {
			conexion.Commit();
			result = true;
		}
		
		return result;
		
	}
	
	public boolean Remove(int key) throws SQLException {
		boolean result = false;
		String sql = "DELETE FROM grupos WHERE grupcodi = ?";
		java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		pst.setInt(1, key);
		if (conexion.Update(pst) > 0) {
			conexion.Commit();
			result = true;
		}
		
		return result;
		
	}
	
	public ArrayList<Grupo> List() throws SQLException {
		ArrayList<Grupo> lista = new ArrayList<Grupo>();
		String sql = "SELECT * FROM grupos ORDER BY grupnomb";
		java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		java.sql.ResultSet rs = conexion.Query(pst);
		while (rs.next()) {
			Grupo grupo = new Grupo();
			grupo.setId(rs.getInt("grupcodi"));
			grupo.setNombre(rs.getString("grupnomb"));
			grupo.setEstado(rs.getInt("grupesta"));
			
			lista.add(grupo);
		}
		
		return lista;
	}
	
	public boolean Find(int key) throws SQLException {
		boolean result = false;
		String sql = "SELECT * FROM  grupos WHERE grupcodi = ?";
		java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		pst.setInt(1, key);
		java.sql.ResultSet rs = conexion.Query(pst);
		if (rs.next()) {
			grupo = new Grupo();
			grupo.setId(rs.getInt("grupcodi"));
			grupo.setNombre(rs.getString("grupnomb"));
			grupo.setEstado(rs.getInt("grupesta"));
			
			result = true;
		}
		
		return result;
		
	}
	
	public boolean Add_Personal(String cedula, int grupo) throws SQLException {
		boolean result = false;
		String sql = "INSERT INTO grupos_personal (cedula,grupo) VALUES (?,?)";
		java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		
		pst.setString(1, cedula);
		pst.setInt(2, grupo);
		
		if (conexion.Update(pst) > 0) {
			conexion.Commit();
			result = true;
		}
		
		return result;
		
	}
	
	public boolean Remove_Personal(int key) throws SQLException {
		boolean result = false;
		String sql = "DELETE FROM grupos_personal WHERE id=?";
		java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		pst.setInt(1, key);
		
		if (conexion.Update(pst) > 0) {
			conexion.Commit();
			result = true;
		}
		
		return result;
		
	}
	
	public ArrayList<Personal> listaPersonal(int grupo) throws SQLException {
		ArrayList<Personal> lista = new ArrayList<Personal>();
		String sql = "SELECT id,cedula,recunomb FROM grupos_personal, recurso WHERE cedula=recucodi and grupo =? ORDER BY grupnomb";
		java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		pst.setInt(1, grupo);
		java.sql.ResultSet rs = conexion.Query(pst);
		while (rs.next()) {
			Personal personal = new Personal();
			personal.setCodigo(rs.getString("cedula"));
			personal.setNombre(rs.getString("recunomb"));
			
			lista.add(personal);
		}
		
		return lista;
		
	}

}
