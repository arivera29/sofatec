/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.are.manejadores;

import com.are.entidades.CampBandeja;
import com.are.entidades.CampBandejaContrata;
import com.are.sofatec.db;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author aimer
 */
public class ManejadorCampBandeja {
    private db conexion;
    private CampBandeja bandeja;

    public ManejadorCampBandeja(db conexion) {
        this.conexion = conexion;
    }

    public ManejadorCampBandeja(db conexion, CampBandeja bandeja) {
        this.conexion = conexion;
        this.bandeja = bandeja;
    }

    public db getConexion() {
        return conexion;
    }

    public void setConexion(db conexion) {
        this.conexion = conexion;
    }

    public CampBandeja getBandeja() {
        return bandeja;
    }

    public void setBandeja(CampBandeja bandeja) {
        this.bandeja = bandeja;
    }
    
    
    public boolean Add(CampBandeja bandeja) throws SQLException {
        boolean result = false;
        String sql = "INSERT INTO camp_bandeja (cabauser,cabaesta) VALUES (?,?)";
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        pst.setString(1, bandeja.getUsuario());
        pst.setInt(2, bandeja.getEstado());
        if (conexion.Update(pst) > 0) {
            conexion.Commit();
            result = true;
        }
        
        return result;
    }
    public boolean Update(CampBandeja bandeja, int key) throws SQLException {
        boolean result = false;
        String sql = "UPDATE camp_bandeja SET cabauser=?,cabaesta=? WHERE cabanuid=?";
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        pst.setString(1, bandeja.getUsuario());
        pst.setInt(2, bandeja.getEstado());
        pst.setInt(3, key);
        if (conexion.Update(pst) > 0) {
            conexion.Commit();
            result = true;
        }
        
        return result;
    }
    
    public boolean Remove(int key) throws SQLException {
        boolean result = false;
        String sql = "DELETE FROM camp_bandeja WHERE cabanuid = ? ";
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        pst.setInt(1, key);
        if (conexion.Update(pst) > 0) {
            conexion.Commit();
            result = true;
        }
        
        return result;
    }
    
    public ArrayList<CampBandeja> List() throws SQLException {
        ArrayList<CampBandeja> lista = new ArrayList<CampBandeja>();
        String sql = "SELECT * FROM camp_bandeja ORDER BY cabauser";
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        java.sql.ResultSet rs = conexion.Query(pst);
        while (rs.next()) {
            CampBandeja bandeja = new CampBandeja(rs.getInt("cabanuid"),rs.getString("cabauser"),rs.getInt("cabaesta"));
            lista.add(bandeja);
        }
        return lista;
    }
    
    public boolean Find(int key) throws SQLException {
        boolean result = false;
        String sql = "SELECT * FROM camp_bandeja WHERE cabanuid=? ORDER BY cabauser";
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        pst.setInt(1, key);
        java.sql.ResultSet rs = conexion.Query(pst);
        
        if (rs.next()) {
            bandeja = new CampBandeja(rs.getInt("cabanuid"),rs.getString("cabauser"),rs.getInt("cabaesta"));
            result = true;
        }
        
        
        return result;
    }
    
    public boolean CambiarEstadoTodos(int estado) throws SQLException {
        boolean result = false;
        String sql = "UPDATE camp_bandeja SET cabaesta=?";
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        pst.setInt(1, estado);
        if (conexion.Update(pst) > 0) {
            conexion.Commit();
            result = true;
        }
        
        return result;
    }
    
    public boolean CambiarEstado(int key, int estado) throws SQLException {
        boolean result = false;
        String sql = "UPDATE camp_bandeja SET cabaesta=? WHERE cabanuid=?";
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        pst.setInt(1, estado);
        pst.setInt(2, key);
        if (conexion.Update(pst) > 0) {
            conexion.Commit();
            result = true;
        }
        
        return result;
    }
    
    public CampBandeja  BandejaDisponible(int contrata) throws SQLException {
        CampBandeja bandeja = null;
        
        String sql = "SELECT c1.cabanuid, count(c2.id) "
                + " FROM camp_bandeja c1 "
                + " LEFT JOIN camp_orden c2 ON c1.cabauser = c2.USUARIO_CARGA AND c2.NUM_OS = '' "
                + " INNER JOIN camp_bandeja_contrata c3 ON c3.cbcocont = ? AND  c1.cabanuid = c3.cbcocaba "
                + " WHERE c1.cabaesta=1 "
                + " GROUP BY c1.cabanuid "
                + " ORDER BY 2 ASC"
                + " LIMIT 1";
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        pst.setInt(1, contrata);
        java.sql.ResultSet rs = conexion.Query(pst);
        
        if (rs.next()) {
            if (this.Find(rs.getInt("cabanuid"))) {
                bandeja = this.bandeja;
            }
            
        }

        return bandeja;
    }
    
    public boolean AddContrata(int key, int contrata) throws SQLException {
        boolean result = true;
        String sql = "INSERT INTO camp_bandeja_contrata (cbcocaba,cbcocont) VALUES (?,?)";
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        pst.setInt(1, key);
        pst.setInt(2, contrata);
        
        if (conexion.Update(pst) >0) {
            conexion.Commit();
            result = true;
        }
        
        return result;
    }
    
    public boolean RemoveContrata(int key) throws SQLException {
        boolean result = true;
        String sql = "DELETE FROM camp_bandeja_contrata WHERE cbconuid = ?";
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        pst.setInt(1, key);
       
        if (conexion.Update(pst) >0) {
            conexion.Commit();
            result = true;
        }
        
        return result;
    }
    
    public ArrayList<CampBandejaContrata> ListContratista(int key) throws SQLException {
        ArrayList<CampBandejaContrata> lista = new ArrayList<CampBandejaContrata>();
        String sql = "SELECT c1.cbconuid,c1.cbcocont, c2.* "
                + " FROM camp_bandeja_contrata c1, contratistas c2 "
                + " WHERE c1.cbcocont = c2.contcodi "
                + " AND c1.cbcocaba = ? "
                + " ORDER BY c2.contnomb ";
        
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        pst.setInt(1, key);
        java.sql.ResultSet rs = conexion.Query(pst);
        while (rs.next()) {
            CampBandejaContrata contratista = new CampBandejaContrata();
            contratista.setId(rs.getInt("cbconuid"));
            contratista.setCodigo(rs.getInt("contcodi"));
            contratista.setNombre(rs.getString("contnomb"));
            contratista.setEstado(rs.getInt("contesta"));
            contratista.setCorreo(rs.getString("contmail"));

            lista.add(contratista);
        }
        
        return lista;
    }
    
    public long Count(int bandeja ) throws SQLException {
        long cont=0;
        String sql = "SELECT count(*) as total "
                + " FROM camp_orden "
                + "WHERE USUARIO_CARGA = (SELECT cabauser FROM camp_bandeja WHERE cabanuid=?) "
                + "AND NUM_OS = '' ";
        
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        pst.setInt(1, bandeja);
        java.sql.ResultSet rs = conexion.Query(pst);
        if (rs.next()) {
            cont = rs.getLong("total");
        }
        
        
        return cont;
    }
    
    public boolean Transferir(int source, int destination) throws SQLException {
        boolean result = false;
        String sql = "UPDATE camp_orden "
                + " SET USUARIO_CARGA =? "
                + " WHERE USUARIO_CARGA=? "
                + " AND NUM_OS=?";
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        pst.setInt(1, destination);
        pst.setInt(2, source);
        
        if (conexion.Update(pst) > 0) {
            conexion.Commit();
            result = true;
        }
        
        
        return result;
    }
    
}
