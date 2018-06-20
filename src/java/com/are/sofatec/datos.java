package com.are.sofatec;

public class datos {
	private String orden;
	private String tipo;
	private String direccion;
	private String barrio;
	private String nic;
	private String nif;
	private double latitud;
	private double longitud;
	private String observacion;
	private int estado;
	private double distancia;
	private int segmento;
	private int aol;
	private int ruta;
	private int itinerario;
	
	
	public int getEstado() {
		return estado;
	}

	public void setEstado(int estado) {
		this.estado = estado;
	}

	public datos() {
		this.orden = "";
		this.tipo ="";
		this.direccion="";
		this.nic ="";
		this.nif ="";
		this.latitud = 0;
		this.longitud = 0;
		this.observacion = "";
		this.estado  = 0;
		this.barrio = "";
		this.distancia = 0;
		this.segmento = 0;
		this.aol = 0;
		this.ruta = 0;
		this.itinerario = 0;
	}
	
	
	public int getAol() {
		return aol;
	}

	public void setAol(int aol) {
		this.aol = aol;
	}

	public int getRuta() {
		return ruta;
	}

	public void setRuta(int ruta) {
		this.ruta = ruta;
	}

	public int getItinerario() {
		return itinerario;
	}

	public void setItinerario(int itinerario) {
		this.itinerario = itinerario;
	}

	public int getSegmento() {
		return segmento;
	}

	public void setSegmento(int segmento) {
		this.segmento = segmento;
	}

	public double getDistancia() {
		return distancia;
	}

	public void setDistancia(double distancia) {
		this.distancia = distancia;
	}

	public String getBarrio() {
		return barrio;
	}

	public void setBarrio(String barrio) {
		this.barrio = barrio;
	}

	public String getOrden() {
		return orden;
	}
	public void setOrden(String orden) {
		this.orden = orden;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public String getNic() {
		return nic;
	}
	public void setNic(String nic) {
		this.nic = nic;
	}
	public String getNif() {
		return nif;
	}
	public void setNif(String nif) {
		this.nif = nif;
	}
	public double getLatitud() {
		return latitud;
	}
	public void setLatitud(double latitud) {
		this.latitud = latitud;
	}
	public double getLongitud() {
		return longitud;
	}
	public void setLongitud(double longitud) {
		this.longitud = longitud;
	}
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	
}