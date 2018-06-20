package com.are.sofatec;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ManejadorUsuarios {
	private db conexion;
	private Usuarios usuario;
	public db getConexion() {
		return conexion;
	}
	public void setConexion(db conexion) {
		this.conexion = conexion;
	}
	public Usuarios getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuarios usuario) {
		this.usuario = usuario;
	}
	public ManejadorUsuarios(db conexion) {
		super();
		this.conexion = conexion;
	}
	public ManejadorUsuarios(db conexion, Usuarios usuario) {
		super();
		this.conexion = conexion;
		this.usuario = usuario;
	}
	
	public boolean add() throws SQLException{
		boolean result = false;
			String sql = "INSERT INTO usuarios (usuario,nombre,perfil,clave,estado,hda,resolver,anomalias,reportes) "
					+ "VALUES (?,?,?,password(?),?,?,?,?,?)";
			java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
			pst.setString(1, usuario.getUsuario());
			pst.setString(2, usuario.getNombre());
			pst.setString(3, usuario.getPerfil());
			pst.setString(4, usuario.getClave());
			pst.setString(5, usuario.getEstado());
			pst.setInt(6, usuario.getHda());
			pst.setInt(7, usuario.getResolver());
			pst.setInt(8, usuario.getAnomalias());
			pst.setInt(9, usuario.getReportes());
			
			if (conexion.Update(pst) > 0) {
				conexion.Commit();
				result = true;
			}
			
		return result;
	}
	
	public boolean update(String key) throws SQLException{
		boolean result = false;
			String sql = "UPDATE usuarios SET " +
					"usuario=?," +
					"nombre=?," +
					"perfil=?," +
					"estado=?," +
					"hda=?," +
					"resolver=?," +
					"anomalias=?," +
					"reportes=?";
			
			if (!usuario.getClave().equals("")) {
				sql += ",clave=password('" + usuario.getClave() + "') ";
			}
			
			sql += " WHERE usuario=?";
			
			java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
			pst.setString(1, usuario.getUsuario());
			pst.setString(2, usuario.getNombre());
			pst.setString(3, usuario.getPerfil());
			pst.setString(4, usuario.getEstado());
			pst.setInt(5, usuario.getHda());
			pst.setInt(6, usuario.getResolver());
			pst.setInt(7, usuario.getAnomalias());
			pst.setInt(8, usuario.getReportes());
			pst.setString(9,key);
			
			if (conexion.Update(pst) > 0) {
				conexion.Commit();
				result = true;
			}
			
		return result;
	}
	public boolean remove(String key) throws SQLException {
		boolean result = false;

			String sql = "DELETE FROM usuarios WHERE usuario=?";
			java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
			pst.setString(1,key);
			if (conexion.Update(pst) > 0) {
				conexion.Commit();
				result = true;
			}
			
		return result;	
	}
	
	
	public boolean exist(String key) throws SQLException {
		boolean result = false;

			String sql = "SELECT usuario, nombre, estado, clave, "
						+ "perfil, hda, resolver, anomalias,reportes "
						+ "FROM usuarios "
						+ "WHERE usuario=?";
			java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
			pst.setString(1,key);
			usuario = new Usuarios();
			ResultSet rs = conexion.Query(pst);
			if (rs.next()) {
				usuario.setUsuario((String)rs.getString("usuario"));
				usuario.setNombre((String)rs.getString("nombre"));
				usuario.setPerfil((String)rs.getString("perfil"));
				usuario.setEstado((String)rs.getString("estado"));
				usuario.setHda((int)rs.getInt("hda"));
				usuario.setResolver((int)rs.getInt("resolver"));
				usuario.setAnomalias((int)rs.getInt("anomalias"));
				usuario.setReportes((int)rs.getInt("reportes"));
				result = true;
			}
			rs.close();
		return result;	
	}
	
	public boolean find(String key) throws SQLException {
		boolean result = false;

			String sql = "SELECT usuario, nombre, estado, clave, "
						+ "perfil, hda, resolver, anomalias,reportes "
						+ "FROM usuarios "
						+ "WHERE usuario=?";
			java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
			pst.setString(1,key);
			usuario = new Usuarios();
			ResultSet rs = conexion.Query(pst);
			if (rs.next()) {
				usuario.setUsuario((String)rs.getString("usuario"));
				usuario.setNombre((String)rs.getString("nombre"));
				usuario.setPerfil((String)rs.getString("perfil"));
				usuario.setEstado((String)rs.getString("estado"));
				usuario.setHda((int)rs.getInt("hda"));
				usuario.setResolver((int)rs.getInt("resolver"));
				usuario.setAnomalias((int)rs.getInt("anomalias"));
				usuario.setReportes((int)rs.getInt("reportes"));
				result = true;
			}
			rs.close();
		return result;	
	}
	
	
	public ArrayList<Usuarios> list() throws SQLException {
		ArrayList<Usuarios> lista = new ArrayList<Usuarios>();
			String sql = "SELECT usuario, nombre, estado, clave, "
					+ "perfil, hda, resolver, anomalias,reportes "
					+ "FROM usuarios "
					+ "ORDER BY usuario";
			ResultSet rs = conexion.Query(sql);
			while(rs.next()) {
				Usuarios usuario = new Usuarios();
				usuario.setUsuario((String)rs.getString("usuario"));
				usuario.setNombre((String)rs.getString("nombre"));
				usuario.setPerfil((String)rs.getString("perfil"));
				usuario.setEstado((String)rs.getString("estado"));
				usuario.setHda((int)rs.getInt("hda"));
				usuario.setResolver((int)rs.getInt("resolver"));
				usuario.setAnomalias((int)rs.getInt("anomalias"));
				usuario.setReportes((int)rs.getInt("reportes"));
				lista.add(usuario);
			}
		return lista;	
	}
	
	public ArrayList<Usuarios> Busqueda(String criterio) throws SQLException {
		ArrayList<Usuarios> lista = new ArrayList<Usuarios>();
			String sql = "SELECT usuario, nombre, estado, clave, "
					+ "perfil, hda, resolver, anomalias,reportes "
					+ "FROM usuarios "
					+ "WHERE usuario like ? OR nombre like ?"
					+ "ORDER BY usuario";
			java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
			pst.setString(1,"%" + criterio + "%");
			pst.setString(2,"%" + criterio + "%");
			
			ResultSet rs = conexion.Query(pst);
			while(rs.next()) {
				Usuarios usuario = new Usuarios();
				usuario.setUsuario((String)rs.getString("usuario"));
				usuario.setNombre((String)rs.getString("nombre"));
				usuario.setPerfil((String)rs.getString("perfil"));
				usuario.setEstado((String)rs.getString("estado"));
				usuario.setHda((int)rs.getInt("hda"));
				usuario.setResolver((int)rs.getInt("resolver"));
				usuario.setAnomalias((int)rs.getInt("anomalias"));
				usuario.setReportes((int)rs.getInt("reportes"));
				lista.add(usuario);
			}
		return lista;	
	}
	
	
	
	public boolean login(String user, String clave) throws SQLException {
		boolean ret = false;

			String sql = "SELECT usuario, nombre, estado, clave, "
					+ "perfil, hda, resolver, anomalias,reportes "
					+ "FROM usuarios "
					+ "WHERE usuario=? "
					+ "AND clave=password(?)";
			java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
			pst.setString(1,user);
			pst.setString(2,clave);
			
			ResultSet rs = conexion.Query(pst);
			if (rs.next()) {
				usuario = new Usuarios();
				usuario.setUsuario((String)rs.getString("usuario"));
				usuario.setNombre((String)rs.getString("nombre"));
				usuario.setPerfil((String)rs.getString("perfil"));
				usuario.setEstado((String)rs.getString("estado"));
				usuario.setHda((int)rs.getInt("hda"));
				usuario.setResolver((int)rs.getInt("resolver"));
				usuario.setAnomalias((int)rs.getInt("anomalias"));
				usuario.setReportes((int)rs.getInt("reportes"));
				ret = true;
			}
			
		return ret;
	}
	
	public ArrayList<Perfiles> perfiles() throws SQLException {
		ArrayList <Perfiles> p = new ArrayList<Perfiles>();
		String sql = "SELECT * FROM perfiles ORDER BY perfil";

			ResultSet rs = conexion.Query(sql);
			while(rs.next()) {
				Perfiles a = new Perfiles();
				a.setId(rs.getString("id"));
				a.setPerfil(rs.getString("perfil"));
				p.add(a);
			}
		return p;
	}
	
	public boolean changepassword(String user,String clave) throws SQLException {
		boolean ret = false;

			String sql = "update usuarios set clave=password(?) where usuario=?";
			java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
			pst.setString(1,clave);
			pst.setString(2,user);
			
			if (conexion.Update(pst)>0) {
				conexion.Commit();
				ret = true;
			}
		return ret;
	}

}
