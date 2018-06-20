/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.are.entidades;

import java.util.Date;

/**
 *
 * @author aimerrivera
 */
public class InfoActa {
    private int numActa;
    private java.util.Date fechaCierre;
    private String anomalia;
    private String observacion;
    private String accionIrregularidad;
    private String FR;
    private java.util.Date fechaIrregularidad;
    private double ECDF;
    private int estado;
    private String descEstado;

    public int getNumActa() {
        return numActa;
    }

    public void setNumActa(int numActa) {
        this.numActa = numActa;
    }

    public Date getFechaCierre() {
        return fechaCierre;
    }

    public void setFechaCierre(Date fechaCierre) {
        this.fechaCierre = fechaCierre;
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

    public String getAccionIrregularidad() {
        return accionIrregularidad;
    }

    public void setAccionIrregularidad(String accionIrregularidad) {
        this.accionIrregularidad = accionIrregularidad;
    }

    public String getFR() {
        return FR;
    }

    public void setFR(String FR) {
        this.FR = FR;
    }

    public Date getFechaIrregularidad() {
        return fechaIrregularidad;
    }

    public void setFechaIrregularidad(Date fechaIrregularidad) {
        this.fechaIrregularidad = fechaIrregularidad;
    }

    public double getECDF() {
        return ECDF;
    }

    public void setECDF(double ECDF) {
        this.ECDF = ECDF;
    }

    public InfoActa() {
        this.numActa = 0;
        this.ECDF = 0;
        this.FR = "";
        this.accionIrregularidad = "";
        this.anomalia= "NA";
        this.fechaCierre = null;
        this.fechaIrregularidad = null;
        this.observacion = "";
        this.estado = -1;
        this.descEstado = "";
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public String getDescEstado() {
        return descEstado;
    }

    public void setDescEstado(String descEstado) {
        this.descEstado = descEstado;
    }
    
    
    
}
