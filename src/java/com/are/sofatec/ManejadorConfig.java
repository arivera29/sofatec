package com.are.sofatec;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ManejadorConfig {
	private db conexion;
	private Config config;
	public db getConexion() {
		return conexion;
	}
	public void setConexion(db conexion) {
		this.conexion = conexion;
	}
	public Config getConfig() {
		return config;
	}
	public void setConfig(Config config) {
		this.config = config;
	}
	public ManejadorConfig(Config config, db conexion) {
		super();
		this.config = config;
		this.conexion = conexion;
	}
	public ManejadorConfig(db conexion) {
		super();
		this.conexion = conexion;
	}
	
	public boolean Save(Config config) throws SQLException {
		boolean result = false;
		if (!this.Exist()) { // insertar nuevo registro
			String sql = "insert into config (id,user,sello1,sello2,acometida1,acometida2,empleado) values (1,?,?,?,?,?,?)";
			java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
			pst.setString(1, config.getUser());
			pst.setString(2, config.getCodigoSello1());
			pst.setString(3, config.getCodigoSello2());
			pst.setString(4, config.getCodigoAcometida1());
			pst.setString(5, config.getCodigoAcometida2());
			pst.setString(6, config.getNumEmpleado());
			
			if (conexion.Update(pst) > 0) {
				conexion.Commit();
				result = true;
			}
			
		}else { // Actualizar registro
			String sql = "update config set user=?,sello1=?,sello2=?,acometida1=?,acometida2=?, empleado=? where id=1";
			java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
			pst.setString(1, config.getUser());
			pst.setString(2, config.getCodigoSello1());
			pst.setString(3, config.getCodigoSello2());
			pst.setString(4, config.getCodigoAcometida1());
			pst.setString(5, config.getCodigoAcometida2());
			pst.setString(6, config.getNumEmpleado());
			if (conexion.Update(pst) > 0) {
				conexion.Commit();
				result = true;
			}
			
			
		}
	
		
		return result;
	}
	
	public boolean Exist() throws SQLException {
		boolean result = false;
		String sql = "select id from config where id=1";
		java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		ResultSet rs = conexion.Query(pst);
		if (rs.next()) {
			result = true;
		}
		rs.close();
		return result;
	}
	
	public boolean Find() throws SQLException {
		boolean result = false;
		String sql = "select * from config where id=1";
		java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		ResultSet rs = conexion.Query(pst);
		if (rs.next()) {
			config = new Config();
			config.setUser(rs.getString("user"));
			config.setCodigoSello1(rs.getString("sello1"));
			config.setCodigoSello2(rs.getString("sello2"));
			config.setCodigoAcometida1(rs.getString("acometida1"));
			config.setCodigoAcometida2(rs.getString("acometida2"));
			config.setNumEmpleado(rs.getString("empleado"));
			
			result = true;
		}else {
			config = new Config();
			config.setUser("");
			config.setCodigoSello1("");
			config.setCodigoSello2("");
			config.setCodigoAcometida1("");
			config.setCodigoAcometida2("");
			config.setNumEmpleado("");
		}
		rs.close();
		return result;
	}

}
