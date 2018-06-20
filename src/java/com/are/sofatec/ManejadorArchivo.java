package com.are.sofatec;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ManejadorArchivo {
	private db conexion = null;
	private Archivo archivo;
	public db getConexion() {
		return conexion;
	}
	public void setConexion(db conexion) {
		this.conexion = conexion;
	}
	public Archivo getArchivo() {
		return archivo;
	}
	public void setArchivo(Archivo archivo) {
		this.archivo = archivo;
	}
	
	
	public ManejadorArchivo(db conexion) {
		super();
		this.conexion = conexion;
	}
	public ManejadorArchivo(Archivo archivo, db conexion) {
		super();
		this.archivo = archivo;
		this.conexion = conexion;
	}
	public boolean add(Archivo archivo) throws SQLException {
		boolean result = false;
		String sql = "insert into archivos (usuario,fecha,filename,ruta) values (?,SYSDATE(),?,?)";
		java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		pst.setString(1, archivo.getUsuario());
		pst.setString(2, archivo.getFilename());
		pst.setString(3, archivo.getRuta());
		
		if (conexion.Update(pst) > 0) {
			conexion.Commit();
			result = true;
		}
		return result;
	}
	
	public boolean add() throws SQLException {
		boolean result = false;
		String sql = "insert into archivos (usuario,fecha,filename,ruta) values (?,SYSDATE(),?,?)";
		java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		pst.setString(1, archivo.getUsuario());
		pst.setString(2, archivo.getFilename());
		pst.setString(3, archivo.getRuta());
		
		if (conexion.Update(pst) > 0) {
			conexion.Commit();
			result = true;
		}
		return result;
	}
	
	public boolean remove(int id) throws SQLException {
		boolean result = false;
		String sql = "delete from archivos where id=?";
		java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		pst.setInt(1, id);
		
		if (conexion.Update(pst) > 0) {
			conexion.Commit();
			result = true;
		}
		return result;
	}

	public boolean removeAllUser(String usuario) throws SQLException {
		boolean result = false;
		String sql = "delete from archivos where usuario=?";
		java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		pst.setString(1, usuario);
		
		if (conexion.Update(pst) > 0) {
			conexion.Commit();
			result = true;
		}
		return result;
	}

	public boolean removeAll() throws SQLException {
		boolean result = false;
		String sql = "delete from archivos";
		java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		if (conexion.Update(pst) > 0) {
			conexion.Commit();
			result = true;
		}
		return result;
	}
	
	public ArrayList<Archivo> listUser(String usuario) throws SQLException {
		ArrayList<Archivo> lista = new ArrayList<Archivo>();
		String sql = "select * from archivos where usuario = ? order by fecha desc";
		java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		pst.setString(1, usuario);
		ResultSet rs = conexion.Query(pst);
		
		while(rs.next()) {
			Archivo archivo = new Archivo();
			archivo.setId(rs.getInt("id"));
			archivo.setFecha(rs.getString("fecha"));
			archivo.setUsuario(rs.getString("usuario"));
			archivo.setFilename(rs.getString("filename"));
			archivo.setRuta(rs.getString("ruta"));
			lista.add(archivo);
		}
		return lista;
	}
	
	public ArrayList<Archivo> listUser(String usuario, int limite) throws SQLException {
		ArrayList<Archivo> lista = new ArrayList<Archivo>();
		String sql = "select * from archivos where usuario = ? order by fecha desc LIMIT ?";
		java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		pst.setString(1, usuario);
		pst.setInt(2, limite);
		ResultSet rs = conexion.Query(pst);
		
		while(rs.next()) {
			Archivo archivo = new Archivo();
			archivo.setId(rs.getInt("id"));
			archivo.setFecha(rs.getString("fecha"));
			archivo.setUsuario(rs.getString("usuario"));
			archivo.setFilename(rs.getString("filename"));
			archivo.setRuta(rs.getString("ruta"));
			lista.add(archivo);
		}
		return lista;
	}
	
	public ArrayList<Archivo> listUserToday(String usuario) throws SQLException {
		ArrayList<Archivo> lista = new ArrayList<Archivo>();
		String sql = "select * from archivos where usuario = ? and date(fecha) = current_date() order by fecha desc";
		java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		pst.setString(1, usuario);
		ResultSet rs = conexion.Query(pst);
		
		while(rs.next()) {
			Archivo archivo = new Archivo();
			archivo.setId(rs.getInt("id"));
			archivo.setFecha(rs.getString("fecha"));
			archivo.setUsuario(rs.getString("usuario"));
			archivo.setFilename(rs.getString("filename"));
			archivo.setRuta(rs.getString("ruta"));
			lista.add(archivo);
		}
		return lista;
	}
	
	public boolean Find(int id) throws SQLException {
		boolean result = false;
		String sql = "select * from archivos where id = ?";
		java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		pst.setInt(1, id);
		ResultSet rs = conexion.Query(pst);
		
		if (rs.next()) {
			archivo = new Archivo();
			archivo.setId(rs.getInt("id"));
			archivo.setFecha(rs.getString("fecha"));
			archivo.setUsuario(rs.getString("usuario"));
			archivo.setFilename(rs.getString("filename"));
			archivo.setRuta(rs.getString("ruta"));
			result = true;
		}
		return result;
	}

}
