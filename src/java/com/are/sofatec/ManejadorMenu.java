package com.are.sofatec;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ManejadorMenu {
private db conexion;
private Menu men;

public db getConexion() {
	return conexion;
}
public void setConexion(db conexion) {
	this.conexion = conexion;
}
public Menu getMen() {
	return men;
}
public void setMen(Menu men) {
	this.men = men;
}

public static final int ORDER_ID = 1;
public static final int ORDER_PAD = 2;

public ManejadorMenu(db conexion) {
	super();
	this.setConexion(conexion);
	this.setMen(new Menu());
}
public ManejadorMenu(db conexion, Menu men) {
	super();
	this.setConexion(conexion);
	this.setMen(men);
}

public boolean add() throws SQLException{
	boolean result = false;

		String sql = "insert into menu (menuid,titulo,descripcion,url,imagen,rol,padreid) values (?,?,?,?,?,?,?)";
		java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		pst.setString(1, men.getMenuid());
		pst.setString(2, men.getTitulo());
		pst.setString(3, men.getDescripcion());
		pst.setString(4, men.getUrl());
		pst.setString(5, men.getImagen());
		pst.setString(6, men.getRol());
		pst.setString(7, men.getPadreid());
		if (conexion.Update(pst) > 0) {
			conexion.Commit();
			result = true;
		}
	return result;
}

public boolean update(String key) throws SQLException{
	boolean result = false;

		String sql = "update menu set menuid=?,titulo=?,descripcion=?,url=?,imagen=?,rol=?,padreid=? where menuid=?";
		
		java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		pst.setString(1, men.getMenuid());
		pst.setString(2, men.getTitulo());
		pst.setString(3, men.getDescripcion());
		pst.setString(4, men.getUrl());
		pst.setString(5, men.getImagen());
		pst.setString(6, men.getRol());
		pst.setString(7, men.getPadreid());
		pst.setString(8, key);
		
		if (conexion.Update(pst) > 0) {
			conexion.Commit();
			result = true;
		}
		
	return result;
}

public boolean remove(String key) throws SQLException {
	boolean result = false;

		String sql = "delete from menu where menuid=?";
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

		String sql = "select * from menu where menuid = ?";
		java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		pst.setString(1,key);
		ResultSet rs = conexion.Query(pst);
		if (rs.next()) {
			result = true;
		}
		rs.close();
		
	
	return result;	
}

public boolean Find(String key) throws SQLException {
	boolean result = false;

		String sql = "select * from menu where menuid=?";
		java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		pst.setString(1,key);
		ResultSet rs = conexion.Query(pst);
		if (rs.next()) {
			men = new Menu();
			men.setMenuid(rs.getString("menuid"));
			men.setTitulo(rs.getString("titulo"));
			men.setDescripcion(rs.getString("descripcion"));
			men.setUrl(rs.getString("url"));
			men.setImagen(rs.getString("imagen"));
			men.setRol(rs.getString("rol"));
			men.setPadreid(rs.getString("padreid"));
			result = true;
		}
		rs.close();
	return result;	
}

public ArrayList<Menu> list() throws SQLException {
	ArrayList<Menu> menu = new ArrayList<Menu>();

		String sql = "select * from menu order by menuid,padreid,orden";
		ResultSet rs  = conexion.Query(sql);
		while(rs.next()) {
			Menu m = new Menu();
			m.setMenuid(rs.getString("menuid"));
			m.setTitulo(rs.getString("titulo"));
			m.setDescripcion(rs.getString("descripcion"));
			m.setUrl(rs.getString("url"));
			m.setImagen(rs.getString("imagen"));
			m.setRol(rs.getString("rol"));
			m.setPadreid(rs.getString("padreid"));
			menu.add(m);
		}
		rs.close();
	return menu;
}

public ArrayList<Menu> list(int orden) throws SQLException {
	ArrayList<Menu> menu = new ArrayList<Menu>();

		String sql = "select * from menu";
		
		switch(orden) {
		case ORDER_ID:
			sql += " order by menuid";
		break;
		case ORDER_PAD:
			sql += " order by padreid";
		break;
		}
		ResultSet rs  = conexion.Query(sql);
		while(rs.next()) {
			Menu m = new Menu();
			m.setMenuid(rs.getString("menuid"));
			m.setTitulo(rs.getString("titulo"));
			m.setDescripcion(rs.getString("descripcion"));
			m.setUrl(rs.getString("url"));
			m.setImagen(rs.getString("imagen"));
			m.setRol(rs.getString("rol"));
			m.setPadreid(rs.getString("padreid"));
			menu.add(m);
		}
		rs.close();
	return menu;
}

	public ArrayList<Menu> list(int usuario, int id, int orden, String mod)
			throws SQLException {
		ArrayList<Menu> menu = new ArrayList<Menu>();

		String sql = "";

		if (id == 0) {
			sql = "select * from menu where menuid=padreid and modulo='" + mod
					+ "'";
		} else {
			if (usuario == 0) {
				sql = "select * from menu where padreid=" + id
						+ " and menuid<>padreid and modulo='" + mod + "'";
			} else {
				sql = "select * from menu where rol in (select rol from usuariosroles where usuario="
						+ usuario
						+ ") and padreid="
						+ id
						+ " and menuid<>padreid and modulo='" + mod + "'";
			}
		}
		switch (orden) {
		case ORDER_ID:
			sql += " order by ORDEN";
			break;
		case ORDER_PAD:
			sql += " order by padreid";
			break;
		}
		ResultSet rs = conexion.Query(sql);
		while (rs.next()) {
			Menu m = new Menu();
			m.setMenuid(rs.getString("menuid"));
			m.setTitulo(rs.getString("titulo"));
			m.setDescripcion(rs.getString("descripcion"));
			m.setUrl(rs.getString("url"));
			m.setImagen(rs.getString("imagen"));
			m.setRol(rs.getString("rol"));
			m.setPadreid(rs.getString("padreid"));
			menu.add(m);
		}
		rs.close();
		return menu;
	}
	
	public ArrayList<Menu> list(String modulo)
			throws SQLException {
		ArrayList<Menu> menu = new ArrayList<Menu>();

		String sql = "select * from menu where modulo=? order by menuid,padreid, orden";
		java.sql.PreparedStatement pst = conexion.getConnection()
				.prepareStatement(sql);
		pst.setString(1, modulo);
		ResultSet rs = conexion.Query(pst);
		while (rs.next()) {
			Menu m = new Menu();
			m.setMenuid(rs.getString("menuid"));
			m.setTitulo(rs.getString("titulo"));
			m.setDescripcion(rs.getString("descripcion"));
			m.setUrl(rs.getString("url"));
			m.setImagen(rs.getString("imagen"));
			m.setRol(rs.getString("rol"));
			m.setPadreid(rs.getString("padreid"));
			menu.add(m);
		}
		rs.close();
		return menu;
	}
	
	public ArrayList<Menu> ListMenuUserParent(String perfil)
			throws SQLException {
		ArrayList<Menu> menu = new ArrayList<Menu>();

		String sql = "select * from menu,perfiles_menu where menu.menuid=perfiles_menu.id_menu and modulo='INI' and perfiles_menu.id_perfil = ? and menu.menuid=menu.padreid  order by orden";
		java.sql.PreparedStatement pst = conexion.getConnection()
				.prepareStatement(sql);
		pst.setString(1, perfil);
		ResultSet rs = conexion.Query(pst);
		while (rs.next()) {
			Menu m = new Menu();
			m.setMenuid(rs.getString("menuid"));
			m.setTitulo(rs.getString("titulo"));
			m.setDescripcion(rs.getString("descripcion"));
			m.setUrl(rs.getString("url"));
			m.setImagen(rs.getString("imagen"));
			m.setRol(rs.getString("rol"));
			m.setPadreid(rs.getString("padreid"));
			menu.add(m);
		}
		rs.close();
		return menu;
	}
	
	public ArrayList<Menu> ListMenuUserChild(String Perfil, String Parent)
			throws SQLException {
		ArrayList<Menu> menu = new ArrayList<Menu>();

		String sql = "select * from menu,perfiles_menu where menu.menuid=perfiles_menu.id_menu and modulo='INI' and perfiles_menu.id_perfil = ? and menu.padreid = ? and menu.menuid != menu.padreid  order by orden";
		java.sql.PreparedStatement pst = conexion.getConnection()
				.prepareStatement(sql);
		pst.setString(1, Perfil);
		pst.setString(2, Parent);
		ResultSet rs = conexion.Query(pst);
		while (rs.next()) {
			Menu m = new Menu();
			m.setMenuid(rs.getString("menuid"));
			m.setTitulo(rs.getString("titulo"));
			m.setDescripcion(rs.getString("descripcion"));
			m.setUrl(rs.getString("url"));
			m.setImagen(rs.getString("imagen"));
			m.setRol(rs.getString("rol"));
			m.setPadreid(rs.getString("padreid"));
			menu.add(m);
		}
		rs.close();
		return menu;
	}
	
	public ArrayList<Menu> ListMenuManagerParent()
			throws SQLException {
		ArrayList<Menu> menu = new ArrayList<Menu>();

		String sql = "select * from menu where modulo='MAN' and menu.menuid=menu.padreid  order by orden";
		ResultSet rs = conexion.Query(sql);
		while (rs.next()) {
			Menu m = new Menu();
			m.setMenuid(rs.getString("menuid"));
			m.setTitulo(rs.getString("titulo"));
			m.setDescripcion(rs.getString("descripcion"));
			m.setUrl(rs.getString("url"));
			m.setImagen(rs.getString("imagen"));
			m.setRol(rs.getString("rol"));
			m.setPadreid(rs.getString("padreid"));
			menu.add(m);
		}
		rs.close();
		return menu;
	}
	
	public ArrayList<Menu> ListMenuManagerChild(
			String Parent)
			throws SQLException {
		ArrayList<Menu> menu = new ArrayList<Menu>();

		String sql = "select * from menu where modulo='MAN' and menu.padreid = ? and menu.menuid != menu.padreid  order by orden";
		java.sql.PreparedStatement pst = conexion.getConnection()
				.prepareStatement(sql);
		pst.setString(1, Parent);
		ResultSet rs = conexion.Query(pst);
		while (rs.next()) {
			Menu m = new Menu();
			m.setMenuid(rs.getString("menuid"));
			m.setTitulo(rs.getString("titulo"));
			m.setDescripcion(rs.getString("descripcion"));
			m.setUrl(rs.getString("url"));
			m.setImagen(rs.getString("imagen"));
			m.setRol(rs.getString("rol"));
			m.setPadreid(rs.getString("padreid"));
			menu.add(m);
		}
		rs.close();
		return menu;
	}

}
