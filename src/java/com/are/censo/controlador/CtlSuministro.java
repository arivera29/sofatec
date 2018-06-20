/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.are.censo.controlador;

import com.are.censo.entidades.Suministro;
import com.are.censo.entidades.SuministroResult;
import com.are.sofatec.db;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author aimerrivera
 */
public class CtlSuministro {

    private Suministro suministro;
    private SuministroResult suministroResult;

    public Suministro getSuministro() {
        return suministro;
    }

    public void setSuministro(Suministro suministro) {
        this.suministro = suministro;
    }

    public SuministroResult getSuministroResult() {
        return suministroResult;
    }

    public CtlSuministro(Suministro suministro) {
        this.suministro = suministro;
        this.suministroResult = null;
    }

    public boolean Find()  {
        boolean result = false;

        db conexion = null;
        try {
            conexion = new db();
        String sql = "SELECT NIS_RAD, COD_TAR,MUNICIPIO,LOCALIDAD,"
                + "DEPARTAMENTO,DIRECCION,APE1_CLI,APE2_CLI,NOM_CLI,REF_DIR,ACC_FINCA "
                + " FROM qo_datosum "
                + " WHERE NIC=? LIMIT 1 ";

        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        pst.setString(1, suministro.getNic());
        java.sql.ResultSet rs = conexion.Query(pst);
        if (rs.next()) {
            suministroResult = new SuministroResult();
            suministroResult.setNombre(rs.getString("NOM_CLI") + " " + rs.getString("APE1_CLI") + " " + rs.getString("APE2_CLI"));
            suministroResult.setDepartamento(rs.getString("DEPARTAMENTO"));
            suministroResult.setLocalidad(rs.getString("LOCALIDAD"));
            suministroResult.setMunicipio(rs.getString("MUNICIPIO"));
            suministroResult.setNis(rs.getString("NIS_RAD"));
            suministroResult.setDireccion(rs.getString("DIRECCION"));
            suministroResult.setMedidor("");
            suministroResult.setEncontrado(true);
        } else {
            suministroResult = new SuministroResult();
            suministroResult.setEncontrado(false);
        }

        }catch (SQLException ex) {
            Logger.getLogger(CtlSuministro.class.getName()).log(Level.SEVERE, null, ex);
            SetError(ex.getMessage());
            
        }finally {
            if (conexion != null) {
                try {
                    conexion.Close();
                } catch (SQLException ex) {
                    SetError(ex.getMessage());
                    Logger.getLogger(CtlSuministro.class.getName()).log(Level.SEVERE, null, ex);
                    
                }
            }
        }
        
        return result;
    }
    
    private void SetError(String error) {
        suministroResult = new SuministroResult();
        suministroResult.setError(true);
        suministroResult.setMsgError(error);
    }

}
