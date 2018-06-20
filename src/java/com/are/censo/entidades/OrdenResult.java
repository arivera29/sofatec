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
public class OrdenResult {
    private boolean sync;
    private String id;
    private boolean error;
    private String msgError;

    public OrdenResult() {
        this.sync = false;
        this.id = "";
        this.error = true;
        this.msgError = "";
    }

    public boolean isSync() {
        return sync;
    }

    public void setSync(boolean sync) {
        this.sync = sync;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
