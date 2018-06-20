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
public class VisitaCensoEfectivaRequest {

    private String token;
    private String usuario;
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
    
    private ArrayList<Foto> fotos;
    
    private int id_visita;
    private String nro_acta;
    private String fecha_acta;
    private String tarifa;
    private String uso;
    private String ct;
    private String mt;
    private String cliente;
    private String cedula;
    private ArrayList<Censo> censo;


    public VisitaCensoEfectivaRequest() {
        this.usuario = "";
        this.id="";
        this.token="";
        this.nic="";
        this.nis="";
        this.brigada="";
        this.observacion="";
        this.contrata="";
        this.inspector="";
        this.ingeniero="";
        this.zona="";
        this.irregularidad="";
        this.corriente1="";
        this.corriente2="";
        this.voltaje1="";
        this.voltaje2="";
        this.fecha="";
        this.latitud=0;
        this.longitud=0;
        this.imei="";
        this.nro_acta="";
        this.fecha_acta="";
        this.tarifa = "";
        this.uso ="";
        this.ct = "";
        this.mt ="";
        this.cliente="";
        this.cedula="";
        fotos = new ArrayList<Foto>();
        censo = new ArrayList<Censo>();
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    
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

    public int getId_visita() {
        return id_visita;
    }

    public void setId_visita(int id_visita) {
        this.id_visita = id_visita;
    }

    public String getNro_acta() {
        return nro_acta;
    }

    public void setNro_acta(String nro_acta) {
        this.nro_acta = nro_acta;
    }

    public String getFecha_acta() {
        return fecha_acta;
    }

    public void setFecha_acta(String fecha_acta) {
        this.fecha_acta = fecha_acta;
    }

    public String getTarifa() {
        return tarifa;
    }

    public void setTarifa(String tarifa) {
        this.tarifa = tarifa;
    }

    public String getUso() {
        return uso;
    }

    public void setUso(String uso) {
        this.uso = uso;
    }

    public String getCt() {
        return ct;
    }

    public void setCt(String ct) {
        this.ct = ct;
    }

    public String getMt() {
        return mt;
    }

    public void setMt(String mt) {
        this.mt = mt;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public ArrayList<Censo> getCenso() {
        return censo;
    }

    public void setCenso(ArrayList<Censo> censo) {
        this.censo = censo;
    }

    public com.are.censo.entidades.Orden ObtenerOrden() {
        com.are.censo.entidades.Orden orden = new com.are.censo.entidades.Orden();
        orden.setId(this.id);
        orden.setToken(this.token);
        orden.setNic(this.nic);
        orden.setNis(this.nis);
        orden.setBrigada(this.brigada);
        orden.setContrata(this.contrata);
        orden.setIngeniero(this.ingeniero);
        orden.setInspector(this.inspector);
        orden.setCorriente1(this.corriente1);
        orden.setCorriente2(this.corriente2);
        orden.setVoltaje1(this.voltaje1);
        orden.setVoltaje2(this.voltaje2);
        orden.setFecha(this.fecha);
        orden.setImei(this.imei);
        orden.setLatitud(this.latitud);
        orden.setLongitud(this.longitud);
        orden.setIrregularidad(this.irregularidad);
        orden.setObservacion(this.observacion);
        orden.setFotos(this.fotos);
        orden.setZona(this.zona);
        orden.setUsuario(this.usuario);
        
        return orden;
    }
    
}
