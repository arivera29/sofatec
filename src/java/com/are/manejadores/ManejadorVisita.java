/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.are.manejadores;

import com.are.entidades.Visita;
import com.are.sofatec.db;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author aimer
 */
public class ManejadorVisita {

    private db conexion;
    private Visita visita;

    public db getConexion() {
        return conexion;
    }

    public void setConexion(db conexion) {
        this.conexion = conexion;
    }

    public Visita getVisita() {
        return visita;
    }

    public void setVisita(Visita visita) {
        this.visita = visita;
    }

    public ManejadorVisita(db conexion) {
        this.conexion = conexion;
    }

    public ManejadorVisita(db conexion, Visita visita) {
        this.conexion = conexion;
        this.visita = visita;
    }

    public boolean Add(Visita visita) throws SQLException {
        boolean result = false;
        String sql = "INSERT INTO visitas (tipo,nic,observacion,departamento,"
                + "municipio,direccion,cliente,brigada,fecha_ingreso,"
                + "fecha_asignacion,fecha_carga,usuario_carga) "
                + "VALUES (?,?,?,?,"
                + "?,?,?,?,SYSDATE(),"
                + "SYSDATE(),SYSDATE(),?)";
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        pst.setInt(1, visita.getTipo());
        pst.setString(2, visita.getNic());
        pst.setString(3, visita.getObservacion());
        pst.setString(4, visita.getDepartamento());
        pst.setString(5, visita.getMunicipio());
        pst.setString(6, visita.getDireccion());
        pst.setString(7, visita.getCliente());
        pst.setString(8, visita.getBrigada());
        pst.setString(9, visita.getUsuario());

        if (conexion.Update(pst) > 0) {
            conexion.Commit();
            result = true;
        }

        return result;
    }

    public boolean Add(Visita visita, boolean commit) throws SQLException {
        boolean result = false;
        String sql = "INSERT INTO visitas (tipo,nic,observacion,departamento,"
                + "municipio,direccion,barrio,cliente,brigada,fecha_ingreso,"
                + "fecha_asignacion,fecha_carga,usuario_carga) "
                + "VALUES (?,?,?,?,"
                + "?,?,?,?,?,SYSDATE(),"
                + "SYSDATE(),SYSDATE(),?)";
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        pst.setInt(1, visita.getTipo());
        pst.setString(2, visita.getNic());
        pst.setString(3, visita.getObservacion());
        pst.setString(4, visita.getDepartamento());
        pst.setString(5, visita.getMunicipio());
        pst.setString(6, visita.getDireccion());
        pst.setString(7, visita.getBarrio());
        pst.setString(8, visita.getCliente());
        pst.setString(9, visita.getBrigada());
        pst.setString(10, visita.getUsuario());

        if (conexion.Update(pst) > 0) {
            if (commit) {
                conexion.Commit();
            }
            result = true;
        }

        return result;
    }

    public boolean Update(int key, Visita visita) throws SQLException {
        boolean result = false;
        String sql = "UPDATE visitas SET tipo=?,nic=?,observacion=?,departamento=?,"
                + "municipio=?,direccion=?, barrio=?,cliente=?,brigada=? WHERE id=?";
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        pst.setInt(1, visita.getTipo());
        pst.setString(2, visita.getNic());
        pst.setString(3, visita.getObservacion());
        pst.setString(4, visita.getDepartamento());
        pst.setString(5, visita.getMunicipio());
        pst.setString(6, visita.getDireccion());
        pst.setString(7, visita.getBarrio());
        pst.setString(8, visita.getCliente());
        pst.setString(9, visita.getBrigada());
        pst.setInt(10, key);

        if (conexion.Update(pst) > 0) {
            conexion.Commit();
            result = true;
        }

        return result;
    }

    public boolean Remove(int key) throws SQLException {
        boolean result = false;
        String sql = "DELETE FROM visitas WHERE id=?";
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        pst.setInt(1, key);
        if (conexion.Update(pst) > 0) {
            conexion.Commit();
            result = true;
        }

        return result;
    }

    public boolean Remove(int[] key) throws SQLException {
        boolean result = false;
        String sql = "DELETE FROM visitas WHERE id=?";
        int cont = 0;
        for (int x = 0; x < key.length; x++) {
            java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
            pst.setInt(1, key[x]);
            if (conexion.Update(pst) > 0) {
                cont++;
            }
        }

        if (cont == key.length) {
            conexion.Commit();
            result = true;
        }

        return result;
    }

    public boolean Asignar(int key, String brigada) throws SQLException {
        boolean result = false;
        String sql = "UPDATE visitas SET brigada=?, fechaAsignacion=SYSDATE() WHERE id=? AND estado IN(0,1)";
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        pst.setInt(1, key);
        pst.setString(2, brigada);
        if (conexion.Update(pst) > 0) {
            conexion.Commit();
            result = true;
        }

        return result;
    }

    public boolean Asignar(int[] key, String[] brigada) throws SQLException {
        boolean result = false;

        if (key.length != brigada.length) {
            return false;
        }

        String sql = "UPDATE visitas SET brigada=?, fechaAsignacion=SYSDATE() WHERE id=? AND estado IN(0,1)";
        int cont = 0;
        for (int x = 0; x < key.length; x++) {
            java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
            pst.setInt(1, key[x]);
            pst.setString(2, brigada[x]);
            if (conexion.Update(pst) > 0) {
                cont++;
            }

        }

        if (cont == key.length) {
            conexion.Commit();
            result = true;
        }

        return result;
    }

    public boolean Find(int key) throws SQLException {
        boolean result = false;
        String sql = "SELECT * FROM visitas WHERE id=? ORDER BY fecha_carga";
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        pst.setInt(1, key);
        java.sql.ResultSet rs = conexion.Query(pst);
        while (rs.next()) {
            visita = new Visita();
            visita.setId(rs.getInt("id"));
            visita.setTipo(rs.getInt("tipo"));
            visita.setNic(rs.getString("nic"));
            visita.setObservacion(rs.getString("observacion"));
            visita.setDepartamento(rs.getString("departamento"));
            visita.setMunicipio(rs.getString("municipio"));
            visita.setDireccion(rs.getString("direccion"));
            visita.setBarrio(rs.getString("barrio"));
            visita.setCliente(rs.getString("cliente"));
            visita.setEstado(rs.getInt("estado"));
            visita.setUsuario(rs.getString("usuario_carga"));
            visita.setFechaIngreso(new java.util.Date(rs.getTimestamp("fecha_ingreso").getTime()));
            visita.setFechaAsignacion(new java.util.Date(rs.getTimestamp("fecha_asignacion").getTime()));
            visita.setFechaCarga(new java.util.Date(rs.getTimestamp("fecha_carga").getTime()));
        }
        return result;
    }

    public ArrayList<Visita> List() throws SQLException {
        ArrayList<Visita> lista = new ArrayList<Visita>();
        String sql = "SELECT * FROM visitas ORDER BY fecha_carga";
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        java.sql.ResultSet rs = conexion.Query(pst);
        while (rs.next()) {
            Visita visita = new Visita();
            visita.setId(rs.getInt("id"));
            visita.setTipo(rs.getInt("tipo"));
            visita.setNic(rs.getString("nic"));
            visita.setObservacion(rs.getString("observacion"));
            visita.setDepartamento(rs.getString("departamento"));
            visita.setMunicipio(rs.getString("municipio"));
            visita.setDireccion(rs.getString("direccion"));
            visita.setBarrio(rs.getString("barrio"));
            visita.setCliente(rs.getString("cliente"));
            visita.setEstado(rs.getInt("estado"));
            visita.setUsuario(rs.getString("usuario_carga"));
            visita.setFechaIngreso(new java.util.Date(rs.getTimestamp("fecha_ingreso").getTime()));
            visita.setFechaAsignacion(new java.util.Date(rs.getTimestamp("fecha_asignacion").getTime()));
            visita.setFechaCarga(new java.util.Date(rs.getTimestamp("fecha_carga").getTime()));

            lista.add(visita);

        }

        return lista;
    }

    public ArrayList<Visita> ListByEstado(int estado) throws SQLException {
        ArrayList<Visita> lista = new ArrayList<Visita>();
        String sql = "SELECT * FROM visitas WHERE estado=? ORDER BY fecha_carga";
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        pst.setInt(1, estado);
        java.sql.ResultSet rs = conexion.Query(pst);
        while (rs.next()) {
            Visita visita = new Visita();
            visita.setId(rs.getInt("id"));
            visita.setTipo(rs.getInt("tipo"));
            visita.setNic(rs.getString("nic"));
            visita.setObservacion(rs.getString("observacion"));
            visita.setDepartamento(rs.getString("departamento"));
            visita.setMunicipio(rs.getString("municipio"));
            visita.setDireccion(rs.getString("direccion"));
            visita.setBarrio(rs.getString("barrio"));
            visita.setCliente(rs.getString("cliente"));
            visita.setEstado(rs.getInt("estado"));
            visita.setBrigada(rs.getString("brigada"));
            visita.setUsuario(rs.getString("usuario_carga"));
            visita.setFechaIngreso(new java.util.Date(rs.getTimestamp("fecha_ingreso").getTime()));
            visita.setFechaAsignacion(new java.util.Date(rs.getTimestamp("fecha_asignacion").getTime()));
            visita.setFechaCarga(new java.util.Date(rs.getTimestamp("fecha_carga").getTime()));

            lista.add(visita);

        }

        return lista;
    }

    public ArrayList<Visita> ListByBrigada(String brigada) throws SQLException {
        ArrayList<Visita> lista = new ArrayList<Visita>();
        String sql = "SELECT * FROM visitas WHERE brigada=? ORDER BY fecha_carga";
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        pst.setString(1, brigada);
        java.sql.ResultSet rs = conexion.Query(pst);
        while (rs.next()) {
            Visita visita = new Visita();
            visita.setId(rs.getInt("id"));
            visita.setTipo(rs.getInt("tipo"));
            visita.setNic(rs.getString("nic"));
            visita.setObservacion(rs.getString("observacion"));
            visita.setDepartamento(rs.getString("departamento"));
            visita.setMunicipio(rs.getString("municipio"));
            visita.setDireccion(rs.getString("direccion"));
            visita.setBarrio(rs.getString("barrio"));
            visita.setCliente(rs.getString("cliente"));
            visita.setEstado(rs.getInt("estado"));
            visita.setBrigada(rs.getString("brigada"));
            visita.setUsuario(rs.getString("usuario_carga"));
            visita.setFechaIngreso(new java.util.Date(rs.getTimestamp("fecha_ingreso").getTime()));
            visita.setFechaAsignacion(new java.util.Date(rs.getTimestamp("fecha_asignacion").getTime()));
            visita.setFechaCarga(new java.util.Date(rs.getTimestamp("fecha_carga").getTime()));

            lista.add(visita);

        }

        return lista;
    }

    public ArrayList<Visita> ListByBrigada(String brigada, int estado) throws SQLException {
        ArrayList<Visita> lista = new ArrayList<Visita>();
        String sql = "SELECT * FROM visitas WHERE brigada=? AND estado=? ORDER BY fecha_carga";
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        pst.setString(1, brigada);
        pst.setInt(2, estado);
        java.sql.ResultSet rs = conexion.Query(pst);
        while (rs.next()) {
            Visita visita = new Visita();
            visita.setId(rs.getInt("id"));
            visita.setTipo(rs.getInt("tipo"));
            visita.setNic(rs.getString("nic"));
            visita.setObservacion(rs.getString("observacion"));
            visita.setDepartamento(rs.getString("departamento"));
            visita.setMunicipio(rs.getString("municipio"));
            visita.setDireccion(rs.getString("direccion"));
            visita.setBarrio(rs.getString("barrio"));
            visita.setCliente(rs.getString("cliente"));
            visita.setEstado(rs.getInt("estado"));
            visita.setBrigada(rs.getString("brigada"));
            visita.setUsuario(rs.getString("usuario_carga"));
            visita.setFechaIngreso(new java.util.Date(rs.getTimestamp("fecha_ingreso").getTime()));
            visita.setFechaAsignacion(new java.util.Date(rs.getTimestamp("fecha_asignacion").getTime()));
            visita.setFechaCarga(new java.util.Date(rs.getTimestamp("fecha_carga").getTime()));

            lista.add(visita);

        }

        return lista;
    }
    
    public boolean removeAllVisitas() throws SQLException {
        String sql = "DELETE FROM visitas WHERE estado = 1";
        if (conexion.Update(sql) >= 0) {
            conexion.Commit();
        }
        return true;
    }
    
    public boolean removeVisitaById(int id) throws SQLException {
        String sql = "DELETE FROM visitas WHERE estado=1 AND id=?";
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        pst.setInt(1, id);
        
        if (conexion.Update(pst) > 0) {
            conexion.Commit();
            return true;
        } else {
            return false;
        }
        
        
       
    }
    
    public boolean removeVisitaByBrigada(String id) throws SQLException {
        String sql = "DELETE FROM visitas WHERE estado=1 AND brigada=?";
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        pst.setString(1, id);
        
        if (conexion.Update(pst) > 0) {
            conexion.Commit();
            return true;
        } else {
            return false;
        }
        
        
       
    }

}
