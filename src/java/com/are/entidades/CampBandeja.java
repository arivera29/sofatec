/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.are.entidades;

/**
 *
 * @author aimer
 */
public class CampBandeja {
    private int id;
    private String usuario;
    private int estado;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public CampBandeja(int id, String usuario, int estado) {
        this.id = id;
        this.usuario = usuario;
        this.estado = estado;
    }

    public CampBandeja() {
    }
    
    
}
