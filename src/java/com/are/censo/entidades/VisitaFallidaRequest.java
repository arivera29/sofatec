/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.are.censo.entidades;

import java.util.ArrayList;

/**
 *
 * @author aimer
 */
public class VisitaFallidaRequest {
    private String token;
    private int id;  // Id del registro de la Visita en la BD
    private String brigada;
    private String anomalia;
    private String observacion;
    private double latitud;
    private double longitud;
    private String imei;
    
    private ArrayList<Foto> fotos;

    public VisitaFallidaRequest() {
        this.id = -1;
        this.brigada= "";
        this.anomalia = "";
        this.observacion="";
        this.latitud=0;
        this.longitud=0;
        this.imei="";
        fotos = new ArrayList<Foto>();
        
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBrigada() {
        return brigada;
    }

    public void setBrigada(String brigada) {
        this.brigada = brigada;
    }

    public String getAnomalia() {
        return anomalia;
    }

    public void setAnomalia(String anomalia) {
        this.anomalia = anomalia;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public ArrayList<Foto> getFotos() {
        return fotos;
    }

    public void setFotos(ArrayList<Foto> fotos) {
        this.fotos = fotos;
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

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }
    
    
    
}
