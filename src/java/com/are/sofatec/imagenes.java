package com.are.sofatec;

public class imagenes {
	private String orden;
	private String fecha;
	private String filename;
	private String recurso;
	private String nombrerecurso;
	public String getOrden() {
		return orden;
	}
	public void setOrden(String orden) {
		this.orden = orden;
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getRecurso() {
		return recurso;
	}
	public void setRecurso(String recurso) {
		this.recurso = recurso;
	}
	public String getNombrerecurso() {
		return nombrerecurso;
	}
	public void setNombrerecurso(String nombrerecurso) {
		this.nombrerecurso = nombrerecurso;
	}
	public imagenes(String orden, String fecha, String filename,
			String recurso, String nombrerecurso) {
		super();
		this.orden = orden;
		this.fecha = fecha;
		this.filename = filename;
		this.recurso = recurso;
		this.nombrerecurso = nombrerecurso;
	}
	public imagenes() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	

}
