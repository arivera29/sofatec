/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.are.censo.controlador;

import com.are.censo.entidades.Foto;
import com.are.censo.entidades.Orden;
import com.are.censo.entidades.OrdenResult;
import com.are.sofatec.Base64;
import com.are.sofatec.db;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author aimerrivera
 */
public class CtlOrden {

    private Orden orden;
    private OrdenResult ordenResult;
    private String dirImages;

    public String getDirImages() {
        return dirImages;
    }

    public void setDirImages(String dirImages) {
        this.dirImages = dirImages;
    }

    public Orden getOrden() {
        return orden;
    }

    public void setOrden(Orden orden) {
        this.orden = orden;
    }

    public OrdenResult getOrdenResult() {
        return ordenResult;
    }

    public CtlOrden(Orden orden, String dirImages) {
        this.orden = orden;
        this.ordenResult = null;
        this.dirImages = dirImages;
    }

    public boolean Add() {
        boolean result = false;
        db conexion = null;
        try {
            conexion = new db();
            com.are.manejadores.ManejadorCampBandeja manejador = new com.are.manejadores.ManejadorCampBandeja(conexion);
            com.are.entidades.CampBandeja bandeja = manejador.BandejaDisponible(Integer.parseInt(orden.getContrata()));
            if (bandeja == null) {
                throw new SQLException("No hay bandeja disponible");
            }

            if (!VerifyId(conexion, orden.getId(), orden.getImei())) {
                String sql = "INSERT INTO camp_orden (NIC,NUM_OS,COMENTARIO,"
                        + "CONTRATISTA,INSPECTOR,INGENIERO,BRIGADA,"
                        + "TIPO_BRIGADA,ID_CAMP,USUARIO_CARGA,FECHA_CARGA,"
                        + "ZONA,NIS,IMEI_APP, ID_APP, LATITUD_APP, "
                        + "LONGITUD_APP, IRREG,CORRIENTE1,CORRIENTE2,"
                        + "VOLTAJE1,VOLTAJE2,ID_VISITA,USUARIO_APP) "
                        + "VALUES (?,?,?,?,?,?,?,?,?,?,SYSDATE(),?,?,?,?,?,?,?,?,?,?,?,0,?)";
                java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
                pst.setString(1, orden.getNic());
                pst.setString(2, "");  // Numero de orden
                pst.setString(3, orden.getObservacion());
                pst.setString(4, orden.getContrata());
                pst.setString(5, orden.getInspector());
                pst.setString(6, orden.getIngeniero());
                pst.setString(7, orden.getBrigada());
                if (orden.getBrigada().equals(orden.getUsuario())) {
                    pst.setInt(8, 2);
                }else {
                    pst.setInt(8, 1);
                }
                
                pst.setString(9, "-1"); // Id campaña
                pst.setString(10, bandeja.getUsuario()); // Usuario de carga
                pst.setString(11, orden.getZona());
                pst.setString(12, orden.getNis());
                pst.setString(13, orden.getImei());
                pst.setString(14, orden.getId());
                pst.setDouble(15, orden.getLatitud());
                pst.setDouble(16, orden.getLongitud());
                pst.setString(17, orden.getIrregularidad());
                pst.setString(18, orden.getCorriente1());
                pst.setString(19, orden.getCorriente2());
                pst.setString(20, orden.getVoltaje1());
                pst.setString(21, orden.getVoltaje2());
                pst.setString(22, orden.getUsuario());   
                

                if (conexion.Update(pst) > 0) {
                    int id = conexion.getLastId();
                    if (this.AddImage(conexion, id, orden.getFotos())) {
                        ordenResult = new OrdenResult();
                        ordenResult.setError(false);
                        ordenResult.setMsgError("");
                        ordenResult.setId(Integer.toString(id));
                        ordenResult.setSync(true);
                        conexion.Commit();
                        result = true;

                    } else {
                        conexion.Rollback();
                        setError("Error al agregar las fotos");

                    }
                } else {
                    conexion.Rollback();
                    setError("Error al agregar el registro");
                }

            } else {
                throw new Exception("Id ya se encuentra registrado");
            }

        } catch (SQLException ex) {
            Logger.getLogger(CtlOrden.class.getName()).log(Level.SEVERE, null, ex);
            setError(ex.getMessage());
        } catch (Exception ex) {
            Logger.getLogger(CtlOrden.class.getName()).log(Level.SEVERE, null, ex);
            setError(ex.getMessage());
        } finally {
            if (conexion != null) {
                try {
                    conexion.Close();
                } catch (SQLException ex) {
                    setError(ex.getMessage());
                    Logger.getLogger(CtlOrden.class.getName()).log(Level.SEVERE, null, ex);

                }
            }
        }

        return result;
    }

    public boolean Add(db conexion, int id_visita, boolean commit) {
        boolean result = false;
        try {
            if (!VerifyId(conexion, orden.getId(), orden.getImei())) {

                com.are.manejadores.ManejadorCampBandeja manejador = new com.are.manejadores.ManejadorCampBandeja(conexion);
                com.are.entidades.CampBandeja bandeja = manejador.BandejaDisponible(Integer.parseInt(orden.getContrata()));
                if (bandeja == null) {
                    throw new SQLException("No hay bandeja disponible");
                }

                String sql = "INSERT INTO camp_orden (NIC,NUM_OS,COMENTARIO,"
                        + "CONTRATISTA,INSPECTOR,INGENIERO,BRIGADA,"
                        + "TIPO_BRIGADA,ID_CAMP,USUARIO_CARGA,FECHA_CARGA,"
                        + "ZONA,NIS,IMEI_APP, ID_APP, LATITUD_APP, "
                        + "LONGITUD_APP, IRREG,CORRIENTE1,CORRIENTE2,"
                        + "VOLTAJE1,VOLTAJE2,ID_VISITA,USUARIO_APP) "
                        + "VALUES (?,?,?,?,?,?,?,?,?,?,SYSDATE(),?,?,?,?,?,?,?,?,?,?,?,?,?)";
                java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
                pst.setString(1, orden.getNic());
                pst.setString(2, "");  // Numero de orden
                pst.setString(3, orden.getObservacion());
                pst.setString(4, orden.getContrata());
                pst.setString(5, orden.getInspector());
                pst.setString(6, orden.getIngeniero());
                pst.setString(7, orden.getBrigada());
                pst.setString(8, "1"); // Tipo de brigada
                pst.setString(9, "-1"); // Id campaña
                pst.setString(10, bandeja.getUsuario()); // Usuario de carga
                pst.setString(11, orden.getZona());
                pst.setString(12, orden.getNis());
                pst.setString(13, orden.getImei());
                pst.setString(14, orden.getId());
                pst.setDouble(15, orden.getLatitud());
                pst.setDouble(16, orden.getLongitud());
                pst.setString(17, orden.getIrregularidad());
                pst.setString(18, orden.getCorriente1());
                pst.setString(19, orden.getCorriente2());
                pst.setString(20, orden.getVoltaje1());
                pst.setString(21, orden.getVoltaje2());
                pst.setInt(22, id_visita);  // identificador de la visita asociado
                                            // a la generación de la OS
                pst.setString(23, orden.getUsuario());                          

                if (conexion.Update(pst) > 0) {
                    int id = conexion.getLastId();
                    if (this.AddImage(conexion, id, orden.getFotos())) {
                        ordenResult = new OrdenResult();
                        ordenResult.setError(false);
                        ordenResult.setMsgError("");
                        ordenResult.setId(Integer.toString(id));
                        ordenResult.setSync(true);
                        if (commit) {
                            conexion.Commit();
                        }
                        result = true;

                    } else {
                        //conexion.Rollback();
                        setError("Error al agregar las fotos");

                    }
                } else {
                    //conexion.Rollback();
                    setError("Error al agregar el registro");
                }

            } else {
                throw new Exception("Id ya se encuentra registrado");
            }

        } catch (SQLException ex) {
            Logger.getLogger(CtlOrden.class.getName()).log(Level.SEVERE, null, ex);
            setError(ex.getMessage());
        } catch (Exception ex) {
            Logger.getLogger(CtlOrden.class.getName()).log(Level.SEVERE, null, ex);
            setError(ex.getMessage());
        }

        return result;
    }

    public void setError(String error) {
        ordenResult = new OrdenResult();
        ordenResult.setError(true);
        ordenResult.setMsgError(error);
        ordenResult.setId("");
        ordenResult.setSync(false);

    }

    public boolean VerifyId(db conexion, String id, String imei) throws SQLException {
        boolean result = false;
        String sql = "SELECT id_app "
                + " FROM camp_orden "
                + " WHERE id_app = ? "
                + " AND imei_app = ? ";
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        pst.setString(1, id);
        pst.setString(2, imei);
        java.sql.ResultSet rs = conexion.Query(pst);
        if (rs.next()) {
            result = true;
        } else {
            result = false;
        }

        return result;
    }

    public boolean AddImage(db conexion, int id, ArrayList<Foto> fotos) throws FileNotFoundException, IOException, SQLException {
        boolean result = false;
        int contador = 0;
        ArrayList<String> files = new ArrayList<String>();
        for (Foto foto : fotos) {
            String filename = java.util.UUID.randomUUID().toString() + ".jpg";
            filename = this.dirImages + File.separator + filename;
            FileOutputStream fos = new FileOutputStream(filename);
            byte[] bytes = Base64.decode(foto.getStrBase64());
            fos.write(bytes);
            fos.close();

            File file = new File(filename);
            if (file.exists() && file.length() > 0) {
                files.add(filename);
                String sql = "INSERT INTO camp_orden_fotos (id_orden,path, filename,fecha,visita) VALUES (?,?,?, SYSDATE(),0)";
                java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
                pst.setInt(1, id);
                pst.setString(2, filename);
                pst.setString(3, foto.getFilename());
                if (conexion.Update(pst) > 0) {
                    contador++;
                }

            }
        }

        if (contador == fotos.size()) {
            result = true;
        } else {
            if (files.size() > 0) {
                for (String filename : files) {
                    File file = new File(filename);
                    if (file.exists()) {
                        file.delete();
                    }
                }
            }
        }

        return result;
    }

    private String AhoraToString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
        java.util.Date fechaActual = new java.util.Date();
        String fecha = dateFormat.format(fechaActual);
        return fecha;
    }
    
    public static long ContadorPhoto(int id, db conexion) throws SQLException {
        long contador = 0;
        String sql = "SELECT count(*) as total FROM camp_orden_fotos WHERE id_orden=? AND visita=0";
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        pst.setInt(1, id);
        java.sql.ResultSet rs = conexion.Query(pst);
        if (rs.next()) {
            contador = rs.getLong("total");
        }
        
        return contador;
    }

}
