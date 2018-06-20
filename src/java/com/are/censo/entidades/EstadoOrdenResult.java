/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.are.censo.entidades;

/**
 *
 * @author aimerrivera
 */
public class EstadoOrdenResult {
    private boolean encontrado;
    private boolean error;
    private String msgError;
    private String id;
    private String orden;
    private String estado;
    private String fecha;
    private String generador;

    public EstadoOrdenResult() {
        this.encontrado = false;
        this.error = false;
        this.msgError = "";
        this.id = "";
        this.orden = "";
        this.estado = "";
        this.fecha = "";
        this.generador = "";
    }
    
    

    public boolean isEncontrado() {
        return encontrado;
    }

    public void setEncontrado(boolean encontrado) {
        this.encontrado = encontrado;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getMsgError() {
        return msgError;
    }

    public void setMsgError(String msgError) {
        this.msgError = msgError;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrden() {
        return orden;
    }

    public void setOrden(String orden) {
        this.orden = orden;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getGenerador() {
        return generador;
    }

    public void setGenerador(String generador) {
        this.generador = generador;
    }
    
    
    
}
