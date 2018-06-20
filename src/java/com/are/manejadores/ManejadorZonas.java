package com.are.manejadores;

import com.are.entidades.Zona;
import com.are.sofatec.db;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ManejadorZonas {
	
	private db conexion;
	private Zona zona;
	public db getConexion() {
		return conexion;
	}
	public void setConexion(db conexion) {
		this.conexion = conexion;
	}
	public Zona getZona() {
		return zona;
	}
	public void setZona(Zona zona) {
		this.zona = zona;
	}
	
	public boolean Add(String nombre) throws SQLException {
		boolean result = false;
		String sql = "insert into zonas (zonanomb) values (?)";
		java.sql.PreparedStatement pst = conexion.getConnection()
				.prepareStatement(sql);
		pst.setString(1, nombre);
		
		if (conexion.Update(pst) > 0) {
			conexion.Commit();
			result = true;
		}
		return result;
	}
	
	public boolean AddUsuario(int id, String usuario) throws SQLException {
		boolean result = false;
		String sql = "insert into zonas_usuario (zouszona,zoususua) values (?,?)";
		java.sql.PreparedStatement pst = conexion.getConnection()
				.prepareStatement(sql);
		pst.setInt(1, id);
		pst.setString(2, usuario);
		
		if (conexion.Update(pst) > 0) {
			conexion.Commit();
			result = true;
		}
		return result;
	}
	
	public boolean RemoveUsuario(int id, String usuario) throws SQLException {
		boolean result = false;
		String sql = "DELETE FROM zonas_usuario WHERE zouszona=? AND zoususua=?";
		java.sql.PreparedStatement pst = conexion.getConnection()
				.prepareStatement(sql);
		pst.setInt(1, id);
		pst.setString(2, usuario);
		
		if (conexion.Update(pst) > 0) {
			conexion.Commit();
			result = true;
		}
		return result;
	}
	
	public boolean Update(String nombre, int id) throws SQLException {
		boolean result = false;
		String sql = "update zonas set zonanomb =? where zonacodi =?";
		java.sql.PreparedStatement pst = conexion.getConnection()
				.prepareStatement(sql);
		pst.setString(1, nombre);
		pst.setInt(2, id);
		
		if (conexion.Update(pst) > 0) {
			conexion.Commit();
			result = true;
		}
		return result;
	}
	
	public ManejadorZonas(db conexion) {
		super();
		this.conexion = conexion;
	}
	public boolean Remove(int id) throws SQLException {
		boolean result = false;
		String sql = "delete from zonas where zonacodi =?";
		java.sql.PreparedStatement pst = conexion.getConnection()
				.prepareStatement(sql);
		pst.setInt(1, id);
		
		if (conexion.Update(pst) > 0) {
			conexion.Commit();
			result = true;
		}
		return result;
	}
	
	public ArrayList<Zona> List() throws SQLException {
		ArrayList<Zona> lista = new ArrayList<Zona>();
		String sql = "select * from zonas order by zonanomb";
		java.sql.PreparedStatement pst = conexion.getConnection()
				.prepareStatement(sql);
		ResultSet rs = conexion.Query(pst);
		while(rs.next()) {
			Zona zona = new Zona(rs.getInt("zonacodi"),rs.getString("zonanomb"));
			lista.add(zona);
		}
		
		return lista;
	}

	public ArrayList<Zona> List(String usuario) throws SQLException {
		ArrayList<Zona> lista = new ArrayList<Zona>();
		String sql = "select zonas.* "
				+ "FROM zonas,zonas_usuario "
				+ "WHERE zonas.zonacodi=zonas_usuario.zouszona "
				+ " AND zonas_usuario.zoususua=?"
				+ "order by zonanomb";
		java.sql.PreparedStatement pst = conexion.getConnection()
				.prepareStatement(sql);
		pst.setString(1, usuario);
		ResultSet rs = conexion.Query(pst);
		while(rs.next()) {
			Zona zona = new Zona(rs.getInt("zonacodi"),rs.getString("zonanomb"));
			lista.add(zona);
		}
		
		return lista;
	}

	

}
