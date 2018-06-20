package com.are.sofatec;

import java.sql.ResultSet;
import java.sql.SQLException;

public class login {
	private String usuario;
	private String clave;
	private String perfil;
	private String nombre;
	private String estado;
	private String error;
	private String key;
	private db conexion;
	
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public login(db conexion) {
		this.conexion = conexion;
	}
	
	public login() {
		
	}
	
	public db getConexion() {
		return conexion;
	}
	public void setConexion(db conexion) {
		this.conexion = conexion;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public String getClave() {
		return clave;
	}
	public void setClave(String clave) {
		this.clave = clave;
	}
	public String getPerfil() {
		return perfil;
	}
	public void setPerfil(String perfil) {
		this.perfil = perfil;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	
	public boolean validLogin(String usuario,String password) {
		boolean result= false;
		try {
			String sql = "select usuario,perfil from usuarios where usuario='" + usuario + "' and clave=password('" + password + "') and estado = 1";
			ResultSet rs = conexion.Query(sql);
			if (rs.next()) {  // usuario existe
				this.usuario = rs.getString("usuario");
				this.perfil = rs.getString("perfil");
				result = true;
			}
			
		}catch (SQLException e) {
			this.error = e.getMessage();
		}
		
		
		
		return result;
	}
	
}
