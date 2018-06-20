/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.are.manejadores;
import com.are.entidades.Contratista;
import com.are.sofatec.db;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author aimerrivera
 */
public class ManejadorContratista {

    private db conexion;
    private Contratista contratista;

    public ManejadorContratista(db conexion) {
        this.conexion = conexion;
    }

    public ManejadorContratista(db conexion, Contratista contratista) {
        this.conexion = conexion;
        this.contratista = contratista;
    }

    public db getConexion() {
        return conexion;
    }

    public void setConexion(db conexion) {
        this.conexion = conexion;
    }

    public Contratista getContratista() {
        return contratista;
    }

    public void setContratista(Contratista contratista) {
        this.contratista = contratista;
    }

    public boolean Add(String nombre, int estado, String correo) throws SQLException {
        boolean result = false;
        String sql = "INSERT INTO contratistas (contnomb,contesta,contmail) VALUES (?,?,?)";
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);

        pst.setString(1, nombre);
        pst.setInt(2, estado);
        pst.setString(3, correo);

        if (conexion.Update(pst) > 0) {
            conexion.Commit();
            result = true;
        }

        return result;

    }

    public boolean Add(Contratista contratista) throws SQLException {
        boolean result = false;
        String sql = "INSERT INTO contratistas (contnomb,contesta,contmail) VALUES (?,?,?)";
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);

        pst.setString(1, contratista.getNombre());
        pst.setInt(2, contratista.getEstado());
        pst.setString(3, contratista.getCorreo());
        if (conexion.Update(pst) > 0) {
            conexion.Commit();
            result = true;
        }

        return result;

    }

    public boolean Add() throws SQLException {
        boolean result = false;
        String sql = "INSERT INTO contratistas (contnomb,contesta,contmail)  VALUES (?,?,?)";
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);

        pst.setString(1, contratista.getNombre());
        pst.setInt(2, contratista.getEstado());
        pst.setString(3, contratista.getCorreo());

        if (conexion.Update(pst) > 0) {
            conexion.Commit();
            result = true;
        }

        return result;

    }

    public boolean Update(String nombre, int estado, String correo, int key) throws SQLException {
        boolean result = false;
        String sql = "UPDATE contratistas SET contnomb=?, contesta=?, contmail=? WHERE contcodi = ?";
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);

        pst.setString(1, nombre);
        pst.setInt(2, estado);
        pst.setString(3, correo);
        pst.setInt(4, key);

        if (conexion.Update(pst) > 0) {
            conexion.Commit();
            result = true;
        }

        return result;

    }

    public boolean Update(Contratista contratista, int key) throws SQLException {
        boolean result = false;
        String sql = "UPDATE contratistas SET contnomb=?, contesta=?, contmail=? WHERE contcodi = ?";
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);

        pst.setString(1, contratista.getNombre());
        pst.setInt(2, contratista.getEstado());
        pst.setString(3, contratista.getCorreo());
        pst.setInt(4, key);

        if (conexion.Update(pst) > 0) {
            conexion.Commit();
            result = true;
        }

        return result;

    }

    public boolean Update(int key) throws SQLException {
        boolean result = false;
        String sql = "UPDATE contratistas SET contnomb=?, contesta=?,contmail=? WHERE contcodi = ?";
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);

        pst.setString(1, contratista.getNombre());
        pst.setInt(2, contratista.getEstado());
        pst.setString(3, contratista.getCorreo());
        pst.setInt(4, key);

        if (conexion.Update(pst) > 0) {
            conexion.Commit();
            result = true;
        }

        return result;

    }

    public boolean Remove(int key) throws SQLException {
        boolean result = false;
        String sql = "DELETE FROM contratistas WHERE contcodi = ?";
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        pst.setInt(1, key);
        if (conexion.Update(pst) > 0) {
            conexion.Commit();
            result = true;
        }

        return result;

    }
    
    public boolean AddUsuario(int key, String usuario) throws SQLException {
        boolean result = false;
        String sql = "INSERT INTO contratista_usuario (couscont,coususua) VALUES (?,?)";
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        pst.setInt(1, key);
        pst.setString(2, usuario);
        if (conexion.Update(pst) > 0) {
            conexion.Commit();
            result = true;
        }

        return result;

    }

    
    public boolean RemoveUsuario(int key, String usuario) throws SQLException {
        boolean result = false;
        String sql = "DELETE FROM contratista_usuario WHERE couscont = ? AND coususua=?";
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        pst.setInt(1, key);
        pst.setString(2, usuario);
        if (conexion.Update(pst) > 0) {
            conexion.Commit();
            result = true;
        }

        return result;

    }
    
    public ArrayList<Contratista> List() throws SQLException {
        ArrayList<Contratista> lista = new ArrayList<Contratista>();
        String sql = "SELECT * FROM contratistas ORDER BY contnomb";
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        java.sql.ResultSet rs = conexion.Query(pst);
        while (rs.next()) {
            Contratista contratista = new Contratista();
            contratista.setCodigo(rs.getInt("contcodi"));
            contratista.setNombre(rs.getString("contnomb"));
            contratista.setEstado(rs.getInt("contesta"));
            contratista.setCorreo(rs.getString("contmail"));

            lista.add(contratista);
        }

        return lista;
    }
    
    public ArrayList<Contratista> ListByUsuario(String usuario) throws SQLException {
        ArrayList<Contratista> lista = new ArrayList<Contratista>();
        String sql = "SELECT * FROM contratistas "
                + " INNER JOIN contratista_usuario ON contcodi = couscont AND coususua=?"
                + " ORDER BY contnomb";
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        pst.setString(1, usuario);
        java.sql.ResultSet rs = conexion.Query(pst);
        while (rs.next()) {
            Contratista contratista = new Contratista();
            contratista.setCodigo(rs.getInt("contcodi"));
            contratista.setNombre(rs.getString("contnomb"));
            contratista.setEstado(rs.getInt("contesta"));
            contratista.setCorreo(rs.getString("contmail"));

            lista.add(contratista);
        }

        return lista;
    }
    
    public ArrayList<Contratista> ListByEstado(int estado) throws SQLException {
        ArrayList<Contratista> lista = new ArrayList<Contratista>();
        String sql = "SELECT * FROM contratistas WHERE contesta=? ORDER BY contnomb";
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        pst.setInt(1, estado);
        java.sql.ResultSet rs = conexion.Query(pst);
        while (rs.next()) {
            Contratista contratista = new Contratista();
            contratista.setCodigo(rs.getInt("contcodi"));
            contratista.setNombre(rs.getString("contnomb"));
            contratista.setEstado(rs.getInt("contesta"));
            contratista.setCorreo(rs.getString("contmail"));

            lista.add(contratista);
        }

        return lista;
    }

    public boolean Find(int key) throws SQLException {
        boolean result = false;
        String sql = "SELECT * FROM  contratistas WHERE contcodi = ?";
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        pst.setInt(1, key);
        java.sql.ResultSet rs = conexion.Query(pst);
        if (rs.next()) {
            contratista = new Contratista();
            contratista.setCodigo(rs.getInt("contcodi"));
            contratista.setNombre(rs.getString("contnomb"));
            contratista.setEstado(rs.getInt("contesta"));
            contratista.setCorreo(rs.getString("contmail"));

            result = true;
        }

        return result;

    }

}
