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
    private final static Logger LOGGER = Logger.getLogger(CtlSuministro.class.getName());

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

    public boolean Find() {
        boolean result = false;
        LOGGER.log(Level.INFO, "QUERY_INFO_SUMINISTRO [{0}] -> Find suministro", suministro.getNic());
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
                LOGGER.log(Level.INFO, "QUERY_INFO_SUMINISTRO [{0}] -> Find suministro OK", suministro.getNic());
                suministroResult = new SuministroResult();
                suministroResult.setNic(suministro.getNic());
                suministroResult.setNombre(rs.getString("NOM_CLI") + " " + rs.getString("APE1_CLI") + " " + rs.getString("APE2_CLI"));
                suministroResult.setDepartamento(rs.getString("DEPARTAMENTO"));
                suministroResult.setLocalidad(rs.getString("LOCALIDAD"));
                suministroResult.setMunicipio(rs.getString("MUNICIPIO"));
                suministroResult.setNis(rs.getString("NIS_RAD"));
                suministroResult.setDireccion(rs.getString("DIRECCION"));
                suministroResult.setMedidor("");
                suministroResult.setEncontrado(true);
                suministroResult.setAlerta(this.getAlerta(suministro.getNic(), conexion));
            } else {
                LOGGER.log(Level.INFO, "QUERY_INFO_SUMINISTRO [{0}] -> No Find suministro", suministro.getNic());
                suministroResult = new SuministroResult();
                suministroResult.setEncontrado(false);
                
            }

        } catch (SQLException ex) {
            Logger.getLogger(CtlSuministro.class.getName()).log(Level.SEVERE, null, ex);
            SetError(ex.getMessage());

        } finally {
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

    private String getAlerta(String nic, db conexion) throws SQLException {
        String alerta = "";
        String sql = "SELECT NUM_OS, DATE(FECHA_CARGA) FECHA_CARGA FROM camp_orden "
                + " WHERE NIC=? "
                + "AND DATEDIFF(DATE(FECHA_CARGA),CURRENT_DATE()) <= 40 "
                + "ORDER BY FECHA_CARGA DESC "
                + "LIMIT 1";

        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        pst.setString(1, nic);
        java.sql.ResultSet rs = conexion.Query(pst);
        if (rs.next()) {
            LOGGER.log(Level.INFO, "QUERY_INFO_SUMINISTRO [{0}] -> Alerta 40 dias generacion OS", nic);
            alerta = "Este nic presenta una OS generada antes de 40 días: OS:" + rs.getString("NUM_OS") + " fecha:" + rs.getString("FECHA_CARGA");
        }

        return alerta;
    }

}
