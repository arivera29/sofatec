package com.are.sofatec;

public class Usuarios {

	private db conexion = null;
	private String usuario;
	private String nombre;
	private String perfil;
	private String estado;
	private String clave;
	private String error;
	private int hda;
	private int resolver;
	private int anomalias;
	private int reportes;
	
	
	
	public String getClave() {
		return clave;
	}
	public void setClave(String clave) {
		this.clave = clave;
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
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getPerfil() {
		return perfil;
	}
	public void setPerfil(String perfil) {
		this.perfil = perfil;
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
	public int getHda() {
		return hda;
	}
	public void setHda(int hda) {
		this.hda = hda;
	}
	public int getResolver() {
		return resolver;
	}
	public void setResolver(int resolver) {
		this.resolver = resolver;
	}
	public int getAnomalias() {
		return anomalias;
	}
	public void setAnomalias(int anomalias) {
		this.anomalias = anomalias;
	}
	public int getReportes() {
		return reportes;
	}
	public void setReportes(int reportes) {
		this.reportes = reportes;
	}
	
}