/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.are.manejadores;

import com.are.entidades.Camp;
import com.are.sofatec.db;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author aimerrivera
 */
public class ManejadorCamp {

    private db conexion;
    private Camp camp;

    public ManejadorCamp(db conexion) {
        this.conexion = conexion;
    }

    public ManejadorCamp(db conexion, Camp campania) {
        this.conexion = conexion;
        this.camp = campania;
    }

    public db getConexion() {
        return conexion;
    }

    public void setConexion(db conexion) {
        this.conexion = conexion;
    }

    public Camp getCampania() {
        return camp;
    }

    public void setCampania(Camp camp) {
        this.camp = camp;
    }

    public boolean Add(String codigo, String descripcion, int estado, String plan) throws SQLException {
        boolean result = false;
        String sql = "INSERT INTO camp (campcodi,campdesc,campesta,campplan) VALUES (?,?,?,?)";
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        pst.setString(1, codigo);
        pst.setString(2, descripcion);
        pst.setInt(3, estado);
        pst.setString(4, plan);

        if (conexion.Update(pst) > 0) {
            conexion.Commit();
            result = true;
        }

        return result;

    }

    public boolean Add(Camp camp) throws SQLException {
        boolean result = false;
        String sql = "INSERT INTO camp (campcodi,campdesc,campesta,campplan) VALUES (?,?,?,?)";
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        pst.setString(1, camp.getCodigo());
        pst.setString(2, camp.getDescripcion());
        pst.setInt(3, camp.getEstado());
        pst.setString(4, camp.getPlan());

        if (conexion.Update(pst) > 0) {
            conexion.Commit();
            result = true;
        }

        return result;

    }

    public boolean Add() throws SQLException {
        boolean result = false;
        String sql = "INSERT INTO camp (campcodi,campdesc,campesta,campplan) VALUES (?,?,?,?)";
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        pst.setString(1, camp.getCodigo());
        pst.setString(2, camp.getDescripcion());
        pst.setInt(3, camp.getEstado());
        pst.setString(4, camp.getPlan());

        if (conexion.Update(pst) > 0) {
            conexion.Commit();
            result = true;
        }

        return result;

    }

    public boolean Update(String codigo, String descripcion, int estado, String plan, int key) throws SQLException {
        boolean result = false;
        String sql = "UPDATE camp SET campcodi=?, campdesc=?, campesta=?, campplan=? WHERE campcodi = ?";
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        pst.setString(1, codigo);
        pst.setString(2, descripcion);
        pst.setInt(3, estado);
        pst.setString(4, plan);
        pst.setInt(5, key);

        if (conexion.Update(pst) > 0) {
            conexion.Commit();
            result = true;
        }

        return result;

    }

    public boolean Update(Camp camp, String key) throws SQLException {
        boolean result = false;
        String sql = "UPDATE camp SET campcodi=?, campdesc=?, campesta=?,campplan=? WHERE campcodi = ?";
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        pst.setString(1, camp.getCodigo());
        pst.setString(2, camp.getDescripcion());
        pst.setInt(3, camp.getEstado());
        pst.setString(4, camp.getPlan());
        pst.setString(5, key);

        if (conexion.Update(pst) > 0) {
            conexion.Commit();
            result = true;
        }

        return result;

    }

    public boolean Update(String key) throws SQLException {
        boolean result = false;
        String sql = "UPDATE camp SET campcodi=?, campdesc=?, campesta=?,campplan=? WHERE campcodi = ?";
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);

        pst.setString(1, camp.getCodigo());
        pst.setString(2, camp.getDescripcion());
        pst.setInt(3, camp.getEstado());
        pst.setString(4, camp.getPlan());
        pst.setString(5, key);

        if (conexion.Update(pst) > 0) {
            conexion.Commit();
            result = true;
        }

        return result;

    }

    public boolean Remove(int key) throws SQLException {
        boolean result = false;
        String sql = "DELETE FROM camp WHERE campcodi = ?";
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        pst.setInt(1, key);
        if (conexion.Update(pst) > 0) {
            conexion.Commit();
            result = true;
        }

        return result;

    }
    
    public boolean cambiarEstadoTodas(int estado) throws SQLException {
        boolean result = false;
        String sql = "UPDATE camp SET campesta = ?";
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        pst.setInt(1, estado);
        if (conexion.Update(pst) > 0) {
            conexion.Commit();
            result = true;
        }

        return result;

    }
    
    public boolean cambiarEstadoTodasCriterio(int estado, String criterio) throws SQLException {
        boolean result = false;
        String sql = "UPDATE camp SET campesta = ? WHERE camdesc like ?";
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        pst.setInt(1, estado);
        pst.setString(2, "%" + criterio + "%");
        if (conexion.Update(pst) > 0) {
            conexion.Commit();
            result = true;
        }

        return result;

    }

    public ArrayList<Camp> List() throws SQLException {
        ArrayList<Camp> lista = new ArrayList<Camp>();
        String sql = "SELECT * FROM camp ORDER BY campdesc";
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        java.sql.ResultSet rs = conexion.Query(pst);
        while (rs.next()) {
            Camp camp = new Camp();
            camp.setCodigo(rs.getString("campcodi"));
            camp.setDescripcion(rs.getString("campdesc"));
            camp.setEstado(rs.getInt("campesta"));
            camp.setPlan(rs.getString("campplan"));

            lista.add(camp);
        }

        return lista;
    }
    
    public ArrayList<Camp> ListByEstado(int estado) throws SQLException {
        ArrayList<Camp> lista = new ArrayList<Camp>();
        String sql = "SELECT * FROM camp WHERE campesta=? ORDER BY campdesc";
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        pst.setInt(1, estado);
        java.sql.ResultSet rs = conexion.Query(pst);
        while (rs.next()) {
            Camp camp = new Camp();
            camp.setCodigo(rs.getString("campcodi"));
            camp.setDescripcion(rs.getString("campdesc"));
            camp.setEstado(rs.getInt("campesta"));
            camp.setPlan(rs.getString("campplan"));

            lista.add(camp);
        }

        return lista;
    }
    
    public ArrayList<Camp> ListByCriterio(String criterio) throws SQLException {
        ArrayList<Camp> lista = new ArrayList<Camp>();
        String sql = "SELECT * FROM camp "
                + " WHERE campcodi like ? "
                + " OR campdesc like ? "
                + " OR campplan like ? "
                + " ORDER BY campdesc";
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        pst.setString(1, "%" + criterio + "%");
        pst.setString(2, "%" + criterio + "%");
        pst.setString(3, "%" + criterio + "%");
        java.sql.ResultSet rs = conexion.Query(pst);
        while (rs.next()) {
            Camp camp = new Camp();
            camp.setCodigo(rs.getString("campcodi"));
            camp.setDescripcion(rs.getString("campdesc"));
            camp.setEstado(rs.getInt("campesta"));
            camp.setPlan(rs.getString("campplan"));

            lista.add(camp);
        }

        return lista;
    }

    public boolean Find(String key) throws SQLException {
        boolean result = false;
        String sql = "SELECT * FROM  camp WHERE campcodi = ?";
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        pst.setString(1, key);
        java.sql.ResultSet rs = conexion.Query(pst);
        if (rs.next()) {
            camp = new Camp();
            camp.setCodigo(rs.getString("campcodi"));
            camp.setDescripcion(rs.getString("campdesc"));
            camp.setEstado(rs.getInt("campesta"));
            camp.setPlan(rs.getString("campplan"));

            result = true;
        }

        return result;

    }

}
