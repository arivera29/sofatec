/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.are.censo.entidades;

import java.util.ArrayList;

/**
 *
 * @author aimerrivera
 */
public class Orden {
    private String token;
    private String id;  // Id del registro en la APP (SQLite)
    private String nic;
    private String nis;
    private String brigada;
    private String observacion;
    private String contrata;
    private String inspector;
    private String ingeniero;
    private String zona;
    private String irregularidad;
    private String corriente1;
    private String corriente2;
    private String voltaje1;
    private String voltaje2;
    private String fecha;
    private double latitud;
    private double longitud;
    private String imei;
    private String usuario;
    private ArrayList<Foto> fotos;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public String getNis() {
        return nis;
    }

    public void setNis(String nis) {
        this.nis = nis;
    }

    public String getBrigada() {
        return brigada;
    }

    public void setBrigada(String brigada) {
        this.brigada = brigada;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getContrata() {
        return contrata;
    }

    public void setContrata(String contrata) {
        this.contrata = contrata;
    }

    public String getInspector() {
        return inspector;
    }

    public void setInspector(String inspector) {
        this.inspector = inspector;
    }

    public String getIngeniero() {
        return ingeniero;
    }

    public void setIngeniero(String ingeniero) {
        this.ingeniero = ingeniero;
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    public String getIrregularidad() {
        return irregularidad;
    }

    public void setIrregularidad(String irregularidad) {
        this.irregularidad = irregularidad;
    }

    public String getCorriente1() {
        return corriente1;
    }

    public void setCorriente1(String corriente1) {
        this.corriente1 = corriente1;
    }

    public String getCorriente2() {
        return corriente2;
    }

    public void setCorriente2(String corriente2) {
        this.corriente2 = corriente2;
    }

    public String getVoltaje1() {
        return voltaje1;
    }

    public void setVoltaje1(String voltaje1) {
        this.voltaje1 = voltaje1;
    }

    public String getVoltaje2() {
        return voltaje2;
    }

    public void setVoltaje2(String voltaje2) {
        this.voltaje2 = voltaje2;
    }
    
    

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
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

    public ArrayList<Foto> getFotos() {
        return fotos;
    }

    public void setFotos(ArrayList<Foto> fotos) {
        this.fotos = fotos;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
    
    
    
}
