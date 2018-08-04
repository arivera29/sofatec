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
public class CPasswordResult {
    private boolean changepassord;
    private boolean error;
    private String msgError;

    public CPasswordResult() {
        this.changepassord=false;
        this.error = false;
        this.msgError = "";
    }
    
    

    public boolean isChangepassord() {
        return changepassord;
    }

    public void setChangepassord(boolean changepassord) {
        this.changepassord = changepassord;
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

    
    
    
    
}
