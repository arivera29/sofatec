package com.are.sofatec;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ManejadorMaterial {

	private db conexion = null;
	private Material material;
	public db getConexion() {
		return conexion;
	}
	public void setConexion(db conexion) {
		this.conexion = conexion;
	}
	public Material getMaterial() {
		return material;
	}
	public void setMaterial(Material material) {
		this.material = material;
	}
	public ManejadorMaterial(db conexion) {
		super();
		this.conexion = conexion;
	}
	
	public boolean Add(Material material) throws SQLException {
		boolean result = false;
		String sql = "insert into material (matecodi,matedesc,mateunid,mateesta) values (?,?,?,?)";
		java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		pst.setString(1, material.getCodigo());
		pst.setString(2, material.getDescripcion());
		pst.setString(3, material.getUnidad());
		pst.setInt(4, material.getEstado());
		if (conexion.Update(pst) > 0) {
			conexion.Commit();
			result = true;
		}
		return result;
	}
	
	public boolean Update(Material material, String key) throws SQLException {
		boolean result = false;
		String sql = "insert into material set matecodi=?,matedesc=?,mateunid=?,mateesta=? where matecodi=?";
		java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		pst.setString(1, material.getCodigo());
		pst.setString(2, material.getDescripcion());
		pst.setString(3, material.getUnidad());
		pst.setInt(4, material.getEstado());
		pst.setString(5, key);
		if (conexion.Update(pst) > 0) {
			conexion.Commit();
			result = true;
		}
		return result;
	}
	
	public boolean Remove(String key) throws SQLException {
		boolean result = false;
		String sql = "delete from material where matecodi=?";
		java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		pst.setString(1, key);
		if (conexion.Update(pst) > 0) {
			conexion.Commit();
			result = true;
		}
		return result;
	}
	
	public boolean Exist(String key) throws SQLException {
		boolean result = false;
		String sql = "select matecodi from material where matecodi=?";
		java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		pst.setString(1, key);
		ResultSet rs = conexion.Query(pst);
		if (rs.next()) {
			result = true;
		}
		rs.close();
		
		return result;
	
	}
	
	public boolean Find(String key) throws SQLException {
		boolean result = false;
		String sql = "select * from material where matecodi=?";
		java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		pst.setString(1, key);
		ResultSet rs = conexion.Query(pst);
		if (rs.next()) {
			material = new Material();
			material.setCodigo(rs.getString("matecodi"));
			material.setDescripcion(rs.getString("matedesc"));
			material.setUnidad(rs.getString("mateunid"));
			material.setEstado(rs.getInt("mateesta"));
			
			result = true;
		}
		rs.close();
		
		return result;
	
	}
	
	public ArrayList<Material> List() throws SQLException {
		ArrayList<Material> lista = new ArrayList<Material>();
		String sql = "select * from material where matecodi=?";
		java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		ResultSet rs = conexion.Query(pst);
		while (rs.next()) {
			Material material = new Material();
			material.setCodigo(rs.getString("matecodi"));
			material.setDescripcion(rs.getString("matedesc"));
			material.setUnidad(rs.getString("mateunid"));
			material.setEstado(rs.getInt("mateesta"));
			lista.add(material);
		}
		rs.close();
		
		return lista;
	
	}
}
