package com.are.sofatec;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class GestionImagenes {
	private imagenes imagen;
	private db conexion = null;
	
	public GestionImagenes(db conexion) {
		super();
		this.conexion = conexion;
	}
	public GestionImagenes(imagenes imagen, db conexion) {
		super();
		this.imagen = imagen;
		this.conexion = conexion;
	}
	public imagenes getImagen() {
		return imagen;
	}
	public void setImagen(imagenes imagen) {
		this.imagen = imagen;
	}
	public db getConexion() {
		return conexion;
	}
	public void setConexion(db conexion) {
		this.conexion = conexion;
	}
	
	public boolean add() throws SQLException {
		boolean result = false;
		String sql = "insert into imagenes (orden,filename,recurso,fecha) values (?,?,?,sysdate())";
		java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		pst.setString(1, imagen.getOrden());
		pst.setString(2, imagen.getFilename());
		pst.setString(3, imagen.getRecurso());
		if (conexion.Update(pst) > 0) {
			conexion.Commit();
			result = true;
		}
	return result;
	}
	
	public boolean remove(String key) throws SQLException {
		boolean result = false;
		String sql = "delete from imagenes where id=?";
		java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		pst.setString(1, key);
		if (conexion.Update(pst) > 0) {
			conexion.Commit();
			result = true;
		}
	return result;
	}
	
	public boolean removeAll(String orden) throws SQLException {
		boolean result = false;
		String sql = "delete from imagenes where orden=?";
		java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		pst.setString(1, orden);
		if (conexion.Update(pst) > 0) {
			conexion.Commit();
			result = true;
		}
	return result;
	}
	
	public ArrayList<imagenes> List(String orden) throws SQLException {
		ArrayList<imagenes> lista = new ArrayList<imagenes>();
		
		String sql = "select orden,fecha,recurso,recunomb,filename from imagenes,recurso where recurso=recucodi and orden=?";
		java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		pst.setString(1, orden);
		ResultSet rs = conexion.Query(pst);
		while (rs.next()) {
			imagenes img = new imagenes();
			img.setOrden(rs.getString("orden"));
			img.setFecha(rs.getString("fecha"));
			img.setFilename(rs.getString("filename"));
			img.setRecurso(rs.getString("recurso"));
			img.setNombrerecurso(rs.getString("recunomb"));
			lista.add(img);
		}
		
		
		return lista;
	}
	
}
