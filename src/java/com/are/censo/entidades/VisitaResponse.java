/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.are.censo.entidades;

/**
 *
 * @author aimer
 */
public class VisitaResponse {
    private boolean error;
    private boolean sync;
    private String msgError;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public boolean isSync() {
        return sync;
    }

    public void setSync(boolean sync) {
        this.sync = sync;
    }

    public String getMsgError() {
        return msgError;
    }

    public void setMsgError(String msgError) {
        this.msgError = msgError;
    }

    public VisitaResponse() {
        this.error=false;
        this.sync=false;
        this.msgError="";
    }

    public VisitaResponse(boolean error, boolean sync, String msgError) {
        this.error = error;
        this.sync = sync;
        this.msgError = msgError;
    }
    
    
    
}
