/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.are.censo.controlador;

import com.are.censo.entidades.EstadoOrden;
import com.are.censo.entidades.EstadoOrdenResult;
import com.are.sofatec.db;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author aimerrivera
 */
public class CtlEstadoOrden {
    private EstadoOrden estado;
    private EstadoOrdenResult estadoResult;

    public EstadoOrden getEstado() {
        return estado;
    }

    public void setEstado(EstadoOrden estado) {
        this.estado = estado;
    }

    public EstadoOrdenResult getEstadoResult() {
        return estadoResult;
    }

    public CtlEstadoOrden(EstadoOrden estado) {
        this.estado = estado;
        this.estadoResult= null;
    }
    
    
    
    public boolean Find() {
        boolean result = false;
        db conexion = null;
        
        try {
            conexion = new db();
            String sql = "SELECT ID,NUM_OS, FECHA_GEN_OS FROM camp_orden WHERE ID = ?";
            java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
            pst.setString(1, estado.getId());
            java.sql.ResultSet rs = conexion.Query(pst);
            if (rs.next()) {
                estadoResult = new EstadoOrdenResult();
                estadoResult.setEncontrado(true);
                estadoResult.setError(false);
                estadoResult.setId(Integer.toString(rs.getInt("ID")));
                estadoResult.setOrden(rs.getString("NUM_OS"));
                if (!rs.getString("NUM_OS").equals("")) {
                    estadoResult.setEstado("GENERADA");
                    estadoResult.setFecha(rs.getString("FECHA_GEN_OS"));
                }else {
                    estadoResult.setEstado("PENDIENTE");
                    estadoResult.setFecha("");
                }
                
                result = true;
                
            }else {
                throw new Exception("Id no encontrado");
            }
            
            
            
        } catch (SQLException ex) {
            Logger.getLogger(CtlEstadoOrden.class.getName()).log(Level.SEVERE, null, ex);
            SetError(ex.getMessage());
        } catch (Exception ex) {
            Logger.getLogger(CtlEstadoOrden.class.getName()).log(Level.SEVERE, null, ex);
            SetError(ex.getMessage());
        } finally {
            if (conexion != null) {
                try {
                    conexion.Close();
                } catch (SQLException ex) {
                    Logger.getLogger(CtlEstadoOrden.class.getName()).log(Level.SEVERE, null, ex);
                    SetError(ex.getMessage());
                }
            }
        }
        
        
        
        
        return result;
    }
    
    private void SetError(String error) {
        estadoResult = new EstadoOrdenResult();
        estadoResult.setEncontrado(false);
        estadoResult.setError(true);
        estadoResult.setMsgError(error);
    }
    
}
