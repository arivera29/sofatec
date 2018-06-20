package com.are.sofatec;

public class visita {
	private String orden;
	private String usuario;
	private String causal;
	private String descripcionCausal;
	private String observacion;
	private String fecha;
	private String recurso;
	private String nombreRecurso;
	private String imei;
	
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	public String getRecurso() {
		return recurso;
	}
	public void setRecurso(String recurso) {
		this.recurso = recurso;
	}
	public String getNombreRecurso() {
		return nombreRecurso;
	}
	public void setNombreRecurso(String nombreRecurso) {
		this.nombreRecurso = nombreRecurso;
	}
	public String getOrden() {
		return orden;
	}
	public void setOrden(String orden) {
		this.orden = orden;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public String getCausal() {
		return causal;
	}
	public void setCausal(String causal) {
		this.causal = causal;
	}
	public String getDescripcionCausal() {
		return descripcionCausal;
	}
	public void setDescripcionCausal(String descripcionCausal) {
		this.descripcionCausal = descripcionCausal;
	}
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public visita(String orden, String usuario, String causal,
			String observacion, String fecha, String recurso,String imei) {
		super();
		this.orden = orden;
		this.usuario = usuario;
		this.causal = causal;
		this.observacion = observacion;
		this.fecha = fecha;
		this.recurso = recurso;
		this.imei = imei;
	}
	public visita(String orden, String usuario, String causal,
			String descripcionCausal, String observacion, String fecha,
			String recurso, String nombreRecurso,String imei) {
		super();
		this.orden = orden;
		this.usuario = usuario;
		this.causal = causal;
		this.descripcionCausal = descripcionCausal;
		this.observacion = observacion;
		this.fecha = fecha;
		this.recurso = recurso;
		this.nombreRecurso = nombreRecurso;
		this.imei = imei;
	}
	public visita() {
		super();
	}
	
	
}
