package com.are.sofatec;

public class GeoOrden {
	private String orden;
	private String direccion;
	private String tipo;
	private String latitud;
	private String longitud;
	private String enrejado;
	private String agresivo;
	private String nic;
	private String medidor;
	private String visitas;
	
	public String getVisitas() {
		return visitas;
	}
	public void setVisitas(String visitas) {
		this.visitas = visitas;
	}
	public String getOrden() {
		return orden;
	}
	public void setOrden(String orden) {
		this.orden = orden;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
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
	public String getEnrejado() {
		return enrejado;
	}
	public void setEnrejado(String enrejado) {
		this.enrejado = enrejado;
	}
	public String getAgresivo() {
		return agresivo;
	}
	public void setAgresivo(String agresivo) {
		this.agresivo = agresivo;
	}
	public String getNic() {
		return nic;
	}
	public void setNic(String nic) {
		this.nic = nic;
	}
	public String getMedidor() {
		return medidor;
	}
	public void setMedidor(String medidor) {
		this.medidor = medidor;
	}
	
	public boolean isValid() {
		if (this.latitud.equals("0") || this.longitud.equals("0")) {
			return false;
		}
		return true;
	}

}
