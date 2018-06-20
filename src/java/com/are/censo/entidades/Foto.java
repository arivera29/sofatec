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
public class Foto {
    
    private String filename;
    private String strBase64;

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getStrBase64() {
        return strBase64;
    }

    public void setStrBase64(String strBase64) {
        this.strBase64 = strBase64;
    }

    public Foto() {
        this.filename = "";
        this.strBase64 = "";
    }
    
    
    
}
