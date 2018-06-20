package com.are.entidades;

public class TipoOrden {
	private String codigo;
	private String descripcion;
	private int estado;
	private int tiempo_max_tercero;
	private int tiempo_max_interno;
	
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public int getEstado() {
		return estado;
	}
	public void setEstado(int estado) {
		this.estado = estado;
	}
	public int getTiempo_max_tercero() {
		return tiempo_max_tercero;
	}
	public void setTiempo_max_tercero(int tiempo_max_tercero) {
		this.tiempo_max_tercero = tiempo_max_tercero;
	}
	public int getTiempo_max_interno() {
		return tiempo_max_interno;
	}
	public void setTiempo_max_interno(int tiempo_max_interno) {
		this.tiempo_max_interno = tiempo_max_interno;
	}
	
	

}
