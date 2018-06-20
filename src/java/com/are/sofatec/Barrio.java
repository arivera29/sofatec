package com.are.sofatec;

public class Barrio {
	private int id;
	private String nombre;
	private String localidad;
	
	private int peso;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public int getPeso() {
		return peso;
	}
	public void setPeso(int peso) {
		this.peso = peso;
	}
	
	public Barrio(int id, String nombre, String localidad, int peso) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.localidad = localidad;
		this.peso = peso;
	}
	public String getLocalidad() {
		return localidad;
	}
	public void setLocalidad(String localidad) {
		this.localidad = localidad;
	}
	
	public Barrio() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	

}
