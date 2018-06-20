/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.are.manejadores;

import com.are.entidades.Irregularidad;
import com.are.sofatec.db;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author aimer
 */
public class ManejadorIrregularidad {
    private db conexion;
    private Irregularidad irregularidad;

    public ManejadorIrregularidad(db conexion) {
        this.conexion = conexion;
    }

    public ManejadorIrregularidad(db conexion, Irregularidad irregularidad) {
        this.conexion = conexion;
        this.irregularidad = irregularidad;
    }

    public db getConexion() {
        return conexion;
    }

    public void setConexion(db conexion) {
        this.conexion = conexion;
    }

    public Irregularidad getIrregularidad() {
        return irregularidad;
    }

    public void setIrregularidad(Irregularidad irregularidad) {
        this.irregularidad = irregularidad;
    }
    
    public ArrayList<Irregularidad> List() throws SQLException {
        ArrayList<Irregularidad> lista = new ArrayList<Irregularidad>();
        String sql = "SELECT COD, DESC_IRREG FROM camp_irreg ORDER BY DESC_IRREG";
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        java.sql.ResultSet rs = conexion.Query(pst);
        
        while (rs.next()) {
            Irregularidad irreg = new Irregularidad();
            irreg.setCodigo(rs.getString("COD"));
            irreg.setDescripcion(rs.getString("DESC_IRREG"));
            lista.add(irreg);
        }
        
        
        return lista;
    }
    
}
