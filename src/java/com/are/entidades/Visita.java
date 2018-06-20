/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.are.entidades;

import java.util.Date;

/**
 *
 * @author aimer
 */
public class Visita {
    private int id;
    private int tipo; // 1-INSPECCION 2-CENSO
    private String nic;
    private String observacion;
    private String departamento;
    private String municipio;
    private String direccion;
    private String barrio;
    private String cliente;
    private java.util.Date fechaIngreso;
    private int estado;
    private String brigada;
    private java.util.Date fechaAsignacion;
    private String usuario;
    private java.util.Date fechaCarga;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getBarrio() {
        return barrio;
    }

    public void setBarrio(String barrio) {
        this.barrio = barrio;
    }

    
    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public String getBrigada() {
        return brigada;
    }

    public void setBrigada(String brigada) {
        this.brigada = brigada;
    }

    public Date getFechaAsignacion() {
        return fechaAsignacion;
    }

    public void setFechaAsignacion(Date fechaAsignacion) {
        this.fechaAsignacion = fechaAsignacion;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Date getFechaCarga() {
        return fechaCarga;
    }

    public void setFechaCarga(Date fechaCarga) {
        this.fechaCarga = fechaCarga;
    }

    public Visita() {
        this.id=0;
        this.tipo = -1;
        this.nic = "";
        this.observacion="";
        this.departamento="";
        this.municipio="";
        this.direccion="";
        this.barrio = "";
        this.cliente="";
        this.brigada="";
        this.estado=0;
        this.usuario="";
        this.fechaIngreso=null;
        this.fechaCarga=null;
        this.fechaAsignacion= null;
    }
    
    
    
    
}
