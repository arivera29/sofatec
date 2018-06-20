package com.are.sofatec;

public class Menu {
private String menuid;
private String titulo;
private String descripcion;
private String url;
private String imagen;
private String rol;
private String padreid;
public String getMenuid() {
	return menuid;
}
public void setMenuid(String menuid) {
	this.menuid = menuid;
}
public String getTitulo() {
	return titulo;
}
public void setTitulo(String titulo) {
	this.titulo = titulo;
}
public String getDescripcion() {
	return descripcion;
}
public void setDescripcion(String descripcion) {
	this.descripcion = descripcion;
}
public String getUrl() {
	return url;
}
public void setUrl(String url) {
	this.url = url;
}
public String getImagen() {
	return imagen;
}
public void setImagen(String imagen) {
	this.imagen = imagen;
}
public String getRol() {
	return rol;
}
public void setRol(String rol) {
	this.rol = rol;
}
public String getPadreid() {
	return padreid;
}
public void setPadreid(String padreid) {
	this.padreid = padreid;
}

public Menu(){
	this.menuid="";
	this.titulo="";
	this.descripcion="";
	this.imagen="";
	this.url="";
	this.rol="";
	this.padreid="";
}

public Menu(String menuid,String titulo,String descripcion,String url,String imagen,String rol,String padreid){
	this.menuid=menuid;
	this.titulo=titulo;
	this.descripcion=descripcion;
	this.imagen=imagen;
	this.url=url;
	this.rol=rol;
	this.padreid=padreid;
}
}
