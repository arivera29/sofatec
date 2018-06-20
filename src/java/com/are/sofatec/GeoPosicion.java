package com.are.sofatec;

public class GeoPosicion {
	String cedula;
	String nombre;
	String fecha;
	String direccion;
	String telefono;
	String latitud;
	String longitud;
	String IMEI;
	
	public GeoPosicion() {
		super();
		// TODO Auto-generated constructor stub
		this.cedula = "";
		this.nombre= "";
		this.fecha = "";
		this.direccion = "";
		this.telefono = "";
		this.latitud = "0";
		this.longitud = "0";
		this.IMEI = "";
	}
	public String getCedula() {
		return cedula;
	}
	public void setCedula(String cedula) {
		this.cedula = cedula;
	}
	public String getNombre() {
		return nombre;
	}
	public String getIMEI() {
		return IMEI;
	}
	public void setIMEI(String iMEI) {
		IMEI = iMEI;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public String getLatitud() {
		return latitud;
	}
	public void setLatitud(String latitud) {
		this.latitud = latitud;
	}
	public String getLongitud() {
		return longitud;
	}
	public void setLongitud(String longitud) {
		this.longitud = longitud;
	}
	
	public boolean isValid() {
	
		if (this.latitud.equals("0") || this.longitud.equals("0")) {
			return false;
		}
		return true;
	}
	

}
