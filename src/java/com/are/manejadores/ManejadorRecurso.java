/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.are.manejadores;

import com.are.entidades.Recurso;
import com.are.sofatec.db;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author aimerrivera
 */
public class ManejadorRecurso {
    private db conexion;
    private Recurso recurso;

    public ManejadorRecurso(db conexion) {
        this.conexion = conexion;
    }

    public ManejadorRecurso(db conexion, Recurso recurso) {
        this.conexion = conexion;
        this.recurso = recurso;
    }

    public db getConexion() {
        return conexion;
    }

    public void setConexion(db conexion) {
        this.conexion = conexion;
    }

    public Recurso getRecurso() {
        return recurso;
    }

    public void setRecurso(Recurso recurso) {
        this.recurso = recurso;
    }
    
    public boolean Add(String codigo, String nombre, String direccion, String telefono,
            String correo, String cargo, int estado, int rol, int contratista, int zona, int auth) throws SQLException {
        boolean result = false;
        String sql = "INSERT INTO recurso (recucodi,recunomb,recudire,recutele,recumail,recucarg,recuesta,recurol,recucont,recuzona,recuaapp)"
                + " VALUES (?,?,?,?,?,?,?,?,?,?,?) ";
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        pst.setString(1, codigo);
        pst.setString(2, nombre);
        pst.setString(3, direccion);
        pst.setString(4, telefono);
        pst.setString(5, correo);
        pst.setString(6, cargo);
        pst.setInt(7, estado);
        pst.setInt(8, rol);
        pst.setInt(9, contratista);
        pst.setInt(10, zona);
        pst.setInt(11, auth); // Autorizado para ingresar a la APP

        if (conexion.Update(pst) > 0) {
            conexion.Commit();
            result = true;
        }

        return result;

    }

    public boolean Add(Recurso recurso) throws SQLException {
        boolean result = false;
        String sql = "INSERT INTO recurso (recucodi,recunomb,recudire,recutele,recumail,recucarg,recuesta,recurol,recucont,recuzona,recuaapp)"
                + " VALUES (?,?,?,?,?,?,?,?,?,?,?) ";
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        pst.setString(1, recurso.getCodigo());
        pst.setString(2, recurso.getNombre());
        pst.setString(3, recurso.getDireccion());
        pst.setString(4, recurso.getTelefono());
        pst.setString(5, recurso.getCorreo());
        pst.setString(6, recurso.getCargo());
        pst.setInt(7, recurso.getEstado());
        pst.setInt(8, recurso.getRol());
        pst.setInt(9, recurso.getContratista());
        pst.setInt(10, recurso.getZona());
        pst.setInt(11, recurso.getAuthAPP()); // Autorizado para ingresar a la APP

        if (conexion.Update(pst) > 0) {
            conexion.Commit();
            result = true;
        }

        return result;

    }

    public boolean Add() throws SQLException {
        boolean result = false;
        String sql = "INSERT INTO recurso (recucodi,recunomb,recudire,recutele,recumail,recucarg,recuesta,recurol,recucont,recuzona,recuaapp)"
                + " VALUES (?,?,?,?,?,?,?,?,?,?,?) ";
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        pst.setString(1, recurso.getCodigo());
        pst.setString(2, recurso.getNombre());
        pst.setString(3, recurso.getDireccion());
        pst.setString(4, recurso.getTelefono());
        pst.setString(5, recurso.getCorreo());
        pst.setString(6, recurso.getCargo());
        pst.setInt(7, recurso.getEstado());
        pst.setInt(8, recurso.getRol());
        pst.setInt(9, recurso.getContratista());
        pst.setInt(10, recurso.getZona());
        pst.setInt(11, recurso.getAuthAPP());   // Autorizado para ingresar a la APP

        if (conexion.Update(pst) > 0) {
            conexion.Commit();
            result = true;
        }

        return result;

    }

    public boolean Update(String codigo, String nombre, String direccion, String telefono,
            String correo, String cargo, int estado, int rol, int contratista, int zona, int auth, String key) throws SQLException {
        boolean result = false;
        String sql = "UPDATE recurso SET " +
                    " recucodi = ? "  +
                    ",recunomb = ? "  +
                    ",recudire = ? "  +
                    ",recutele = ? "  +
                    ",recumail = ? "  +
                    ",recucarg = ? "  +
                    ",recuesta = ? "  +
                    ",recurol  = ? "  +
                    ",recucont = ? " +
                    ",recuzona = ? " +
                    ",recuaapp = ? " +
                    " WHERE recucodi=? ";
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        pst.setString(1, codigo);
        pst.setString(2, nombre);
        pst.setString(3, direccion);
        pst.setString(4, telefono);
        pst.setString(5, correo);
        pst.setString(6, cargo);
        pst.setInt(7, estado);
        pst.setInt(8, rol);
        pst.setInt(9, contratista);
        pst.setInt(10, zona);
        pst.setInt(11, auth);
        pst.setString(12, key);

        if (conexion.Update(pst) > 0) {
            conexion.Commit();
            result = true;
        }

        return result;

    }

    public boolean Update(Recurso recurso, String key) throws SQLException {
        boolean result = false;
        String sql = "UPDATE recurso SET " +
                    " recucodi = ? "  +
                    ",recunomb = ? "  +
                    ",recudire = ? "  +
                    ",recutele = ? "  +
                    ",recumail = ? "  +
                    ",recucarg = ? "  +
                    ",recuesta = ? "  +
                    ",recurol  = ? "  +
                    ",recucont = ? " +
                    ",recuzona = ? " +
                    ",recuaapp = ? " +
                    " WHERE recucodi=? ";
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        pst.setString(1, recurso.getCodigo());
        pst.setString(2, recurso.getNombre());
        pst.setString(3, recurso.getDireccion());
        pst.setString(4, recurso.getTelefono());
        pst.setString(5, recurso.getCorreo());
        pst.setString(6, recurso.getCargo());
        pst.setInt(7, recurso.getEstado());
        pst.setInt(8, recurso.getRol());
        pst.setInt(9, recurso.getContratista());
        pst.setInt(10, recurso.getZona());
        pst.setInt(11, recurso.getAuthAPP());
        pst.setString(12, key);

        if (conexion.Update(pst) > 0) {
            conexion.Commit();
            result = true;
        }

        return result;

    }

    public boolean Update(String key) throws SQLException {
        boolean result = false;
        String sql = "UPDATE recurso SET " +
                    " recucodi = ? "  +
                    ",recunomb = ? "  +
                    ",recudire = ? "  +
                    ",recutele = ? "  +
                    ",recumail = ? "  +
                    ",recucarg = ? "  +
                    ",recuesta = ? "  +
                    ",recurol  = ? "  +
                    ",recucont = ? " +
                    ",recuzona = ? " +
                    ",recuaapp = ? " +
                    " WHERE recucodi=? ";
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        pst.setString(1, recurso.getCodigo());
        pst.setString(2, recurso.getNombre());
        pst.setString(3, recurso.getDireccion());
        pst.setString(4, recurso.getTelefono());
        pst.setString(5, recurso.getCorreo());
        pst.setString(6, recurso.getCargo());
        pst.setInt(7, recurso.getEstado());
        pst.setInt(8, recurso.getRol());
        pst.setInt(9, recurso.getContratista());
        pst.setInt(10, recurso.getZona());
        pst.setInt(11, recurso.getAuthAPP());
        pst.setString(12, key);


        if (conexion.Update(pst) > 0) {
            conexion.Commit();
            result = true;
        }

        return result;

    }

    public boolean Remove(String key) throws SQLException {
        boolean result = false;
        String sql = "DELETE FROM recurso WHERE recucodi = ?";
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        pst.setString(1, key);
        if (conexion.Update(pst) > 0) {
            conexion.Commit();
            result = true;
        }

        return result;

    }

    public ArrayList<Recurso> List() throws SQLException {
        ArrayList<Recurso> lista = new ArrayList<Recurso>();
        String sql = "SELECT * FROM recurso ORDER BY recunomb";
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        java.sql.ResultSet rs = conexion.Query(pst);
        while (rs.next()) {
            Recurso recurso = new Recurso();
            recurso.setCodigo(rs.getString("recucodi"));
            recurso.setNombre(rs.getString("recunomb"));
            recurso.setDireccion(rs.getString("recudire"));
            recurso.setTelefono(rs.getString("recutele"));
            recurso.setCorreo(rs.getString("recumail"));
            recurso.setCargo(rs.getString("recucarg"));
            recurso.setRol(rs.getInt("recurol"));
            recurso.setContratista(rs.getInt("recucont"));
            recurso.setEstado(rs.getInt("recuesta"));
            recurso.setZona(rs.getInt("recuzona"));
            recurso.setAuthAPP(rs.getInt("recuaapp"));

            lista.add(recurso);
        }

        return lista;
    }
    
    public ArrayList<Recurso> List(String criterio) throws SQLException {
        ArrayList<Recurso> lista = new ArrayList<Recurso>();
        String sql = "SELECT * "
                + " FROM recurso "
                + " WHERE recucodi like ? "
                + " OR recunomb like ? "
                + " ORDER BY recunomb";
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        pst.setString(1, "%" + criterio +"%");
        pst.setString(2, "%" + criterio +"%");
        java.sql.ResultSet rs = conexion.Query(pst);
        while (rs.next()) {
            Recurso recurso = new Recurso();
            recurso.setCodigo(rs.getString("recucodi"));
            recurso.setNombre(rs.getString("recunomb"));
            recurso.setDireccion(rs.getString("recudire"));
            recurso.setTelefono(rs.getString("recutele"));
            recurso.setCorreo(rs.getString("recumail"));
            recurso.setCargo(rs.getString("recucarg"));
            recurso.setRol(rs.getInt("recurol"));
            recurso.setContratista(rs.getInt("recucont"));
            recurso.setEstado(rs.getInt("recuesta"));
            recurso.setZona(rs.getInt("recuzona"));
            recurso.setAuthAPP(rs.getInt("recuaapp"));

            lista.add(recurso);
        }

        return lista;
    }
    
    public ArrayList<Recurso> FilterByContratista(int contratista) throws SQLException {
        ArrayList<Recurso> lista = new ArrayList<Recurso>();
        String sql = "SELECT * "
                + " FROM recurso "
                + " WHERE recucont = ? "
                + " ORDER BY recunomb";
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        pst.setInt(1, contratista);
        java.sql.ResultSet rs = conexion.Query(pst);
        while (rs.next()) {
            Recurso recurso = new Recurso();
            recurso.setCodigo(rs.getString("recucodi"));
            recurso.setNombre(rs.getString("recunomb"));
            recurso.setDireccion(rs.getString("recudire"));
            recurso.setTelefono(rs.getString("recutele"));
            recurso.setCorreo(rs.getString("recumail"));
            recurso.setCargo(rs.getString("recucarg"));
            recurso.setRol(rs.getInt("recurol"));
            recurso.setContratista(rs.getInt("recucont"));
            recurso.setEstado(rs.getInt("recuesta"));
            recurso.setZona(rs.getInt("recuzona"));
            recurso.setAuthAPP(rs.getInt("recuaapp"));

            lista.add(recurso);
        }

        return lista;
    }
    
    public ArrayList<Recurso> FilterByZona(int zona) throws SQLException {
        ArrayList<Recurso> lista = new ArrayList<Recurso>();
        String sql = "SELECT * "
                + " FROM recurso "
                + " WHERE recuzona = ? "
                + " ORDER BY recunomb";
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        pst.setInt(1, zona);
        java.sql.ResultSet rs = conexion.Query(pst);
        while (rs.next()) {
            Recurso recurso = new Recurso();
            recurso.setCodigo(rs.getString("recucodi"));
            recurso.setNombre(rs.getString("recunomb"));
            recurso.setDireccion(rs.getString("recudire"));
            recurso.setTelefono(rs.getString("recutele"));
            recurso.setCorreo(rs.getString("recumail"));
            recurso.setCargo(rs.getString("recucarg"));
            recurso.setRol(rs.getInt("recurol"));
            recurso.setContratista(rs.getInt("recucont"));
            recurso.setEstado(rs.getInt("recuesta"));
            recurso.setZona(rs.getInt("recuzona"));
            recurso.setAuthAPP(rs.getInt("recuaapp"));

            lista.add(recurso);
        }

        return lista;
    }

    public boolean Find(String key) throws SQLException {
        boolean result = false;
        String sql = "SELECT * FROM  recurso WHERE recucodi = ?";
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        pst.setString(1, key);
        java.sql.ResultSet rs = conexion.Query(pst);
        if (rs.next()) {
            recurso = new Recurso();
            recurso.setCodigo(rs.getString("recucodi"));
            recurso.setNombre(rs.getString("recunomb"));
            recurso.setDireccion(rs.getString("recudire"));
            recurso.setTelefono(rs.getString("recutele"));
            recurso.setCorreo(rs.getString("recumail"));
            recurso.setCargo(rs.getString("recucarg"));
            recurso.setRol(rs.getInt("recurol"));
            recurso.setContratista(rs.getInt("recucont"));
            recurso.setEstado(rs.getInt("recuesta"));
            recurso.setZona(rs.getInt("recuzona"));
            recurso.setAuthAPP(rs.getInt("recuaapp"));

            result = true;
        }

        return result;

    }
    
    public boolean AsignarPassword(String key, String password) throws SQLException {
        boolean result = false;
        String sql = "UPDATE recurso SET recupass=MD5(?) WHERE recucodi=?";
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        pst.setString(1, password);
        pst.setString(2, key);
        
        if (conexion.Update(pst) > 0) {
            conexion.Commit();
            result = true;
        }
        
        return result;
    }
}
