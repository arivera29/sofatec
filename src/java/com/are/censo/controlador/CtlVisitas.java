/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.are.censo.controlador;

import com.are.censo.entidades.Censo;
import com.are.censo.entidades.Foto;
import com.are.censo.entidades.VisitaCensoEfectivaRequest;
import com.are.censo.entidades.VisitaFallidaRequest;
import com.are.censo.entidades.VisitaRequest;
import com.are.entidades.Visita;
import com.are.manejadores.ManejadorVisita;
import com.are.sofatec.Base64;
import com.are.sofatec.db;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author aimerrivera
 */
public class CtlVisitas {

    private VisitaRequest visitaRequest;
    private ArrayList<Visita> visitas;
    private String dirImages;
    private db conexion;
    private final static Logger LOGGER = Logger.getLogger(CtlVisitas.class.getName());

    public CtlVisitas(db conexion) {
        this.conexion = conexion;
    }
    
    
    

    public String getDirImages() {
        return dirImages;
    }

    public void setDirImages(String dirImages) {
        this.dirImages = dirImages;
    }
    
    

    public VisitaRequest getVisitaRequest() {
        return visitaRequest;
    }

    public void setVisitaRequest(VisitaRequest visitaResult) {
        this.visitaRequest = visitaResult;
    }

    public ArrayList<Visita> getVisitas() {
        return visitas;
    }

    public void setVisitas(ArrayList<Visita> visitas) {
        this.visitas = visitas;
    }

    

    public void List() {
        ManejadorVisita manejador = new ManejadorVisita(conexion);
        try {
            visitas = manejador.ListByBrigada(visitaRequest.getBrigada(), 1);
        } catch (SQLException ex) {
            Logger.getLogger(CtlVisitas.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    public boolean UpdateVisitaCensoEfectiva(VisitaCensoEfectivaRequest request) throws SQLException, IOException {
        boolean result = false;
        String sql = "UPDATE visitas SET "
                + " estado = 2, "
                + "nro_acta=?, "
                + "fecha_acta=?, "
                + "tarifa=?, "
                + "uso=?, "
                + "ct=?, "
                + "mt=?, "
                + "latitud=?,"
                + "longitud=?, "
                + "imei=?, "
                + "fecha_ejecucion=SYSDATE(), "
                + "estado_ejecucion=1, "
                + "cliente=?, "
                + "cedula=? "
                + " WHERE id=?";
        
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        pst.setString(1, request.getNro_acta());
        pst.setString(2, request.getFecha_acta());
        pst.setString(3, request.getTarifa());
        pst.setString(4, request.getUso());
        pst.setString(5, request.getCt());
        pst.setString(6, request.getMt());
        pst.setDouble(7, request.getLatitud());
        pst.setDouble(8, request.getLongitud());
        pst.setString(9, request.getImei());
        pst.setString(10, request.getCliente());
        pst.setString(11, request.getCedula());
        pst.setInt(12, request.getId_visita());
        
        if (conexion.Update(pst) > 0  
                && this.AddCensoVisita(request.getId_visita(), request.getCenso(), request.getNic())
                && this.AddImage(request.getId_visita(), request.getFotos()) ) {
                
                // Creamos una orden asociada al identificador de la visita
                LOGGER.log(Level.INFO, "[REQUEST_CENSO_EFECTIVA] -> [{0}] Generating OS request...", request.getNic());
                CtlOrden controlador = new CtlOrden(request.ObtenerOrden(),this.dirImages);
                controlador.setDirImages(this.dirImages);
                if (controlador.Add(conexion, request.getId_visita(), false)) {
                    conexion.Commit();
                    LOGGER.log(Level.INFO, "[REQUEST_CENSO_EFECTIVA] -> [{0}] Generated OS request OK", request.getNic());
                    result = true;
                }else {
                    conexion.Rollback();
                    LOGGER.log(Level.INFO, "[REQUEST_CENSO_EFECTIVA] -> [{0}] Error generating OS request. {1}", new Object[] { request.getNic(), controlador.getOrdenResult().getMsgError() });
                    throw new SQLException(controlador.getOrdenResult().getMsgError());
                }
            
                

        }else {
            LOGGER.log(Level.INFO, "[REQUEST_VISITA_CENSO_EFECTIVA] -> [{0}] Error Generating OS request... ", request.getNic());
            throw new SQLException("Error al actualizar la visita como efectiva");
        }
        
        
        return result;
        
    }
    
    public boolean UpdateVisitaRevisionEfectiva(com.are.censo.entidades.VisitaRevisionEfectivaRequest orden) throws SQLException, IOException {
        boolean result = false;
        String sql = "UPDATE visitas SET "
                + "estado = 2, "
                + "nro_acta=?, "
                + "fecha_acta=?, "
                + "tarifa=?, "
                + "uso=?, "
                + "ct=?, "
                + "mt=?, "
                + "latitud=?,"
                + "longitud=?, "
                + "imei=?, "
                + "fecha_ejecucion=SYSDATE(), "
                + "estado_ejecucion=1, "
                + "cliente=?, "
                + "cedula=? "
                + "WHERE id=?";
        java.sql.PreparedStatement  pst = conexion.getConnection().prepareStatement(sql);
        pst.setString(1,orden.getNro_acta());
        pst.setString(2, orden.getFecha_acta());
        pst.setString(3, orden.getTarifa());
        pst.setString(4, orden.getUso());
        pst.setString(5, orden.getCt());
        pst.setString(6, orden.getMt());
        pst.setDouble(7, orden.getLatitud());
        pst.setDouble(8, orden.getLongitud());
        pst.setString(9, orden.getImei());
        pst.setString(10, orden.getCliente());
        pst.setString(11, orden.getCedula());
        pst.setInt(12, orden.getId_visita());
        
        if (conexion.Update(pst)>0 
                && this.AddImage(orden.getId_visita(), orden.getFotos())) {
                // Creamos una orden asociada al identificador de la visita
                LOGGER.log(Level.INFO, "[REQUEST_VISITA_INSPECCION_EFECTIVA] -> [{0}] Generating OS request...", new Object[] { orden.getNic()});
                CtlOrden controlador = new CtlOrden(orden.ObtenerOrden(),this.dirImages);
                controlador.setDirImages(this.dirImages);
                if (controlador.Add(conexion, orden.getId_visita(), false)) {
                    
                    LOGGER.log(Level.INFO, "[REQUEST_VISITA_INSPECCION_EFECTIVA] -> [{0}] Generated OS request OK", new Object[] { orden.getNic()});conexion.Commit();
                    result = true;
                }else {
                    conexion.Rollback();
                    LOGGER.log(Level.INFO, "[REQUEST_VISITA_INSPECCION_EFECTIVA] -> [{0}] Error generating OS request. {1}", new Object[] { orden.getNic(), controlador.getOrdenResult().getMsgError()});
                    throw new SQLException(controlador.getOrdenResult().getMsgError());
                }
        }else {
            LOGGER.log(Level.INFO, "[REQUEST_VISITA_INSPECCION_EFECTIVA] -> [{0}] Error generating OS request. [Rollback]", new Object[] { orden.getNic()}); 
            conexion.Rollback();
        }
        
        return result;
    }
    
    public boolean UpdateVisitaFallida(VisitaFallidaRequest request) throws SQLException, IOException {
        boolean result = false;
        db conexion = new db();
        String sql ="UPDATE visitas SET "
                + "anomalia=?, "
                + "obs_anomalia=?, "
                + "latitud=?, "
                + "longitud=?, "
                + "imei=?, "
                + "fecha_ejecucion=SYSDATE(), "
                + "estado_ejecucion=3, "
                + "estado = 2 "
                + "WHERE id=?";
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        pst.setString(1, request.getAnomalia());
        pst.setString(2, request.getObservacion());
        pst.setDouble(3, request.getLatitud());
        pst.setDouble(4, request.getLongitud());
        pst.setString(5, request.getImei());
        pst.setInt(6, request.getId());
        
        if (conexion.Update(pst) > 0  
                && this.AddImage(request.getId(), request.getFotos())) {
            conexion.Commit();
            result = true;
            
        }
        
        conexion.Close();
        return result;
    }
    
    private boolean AddCensoVisita(int id, ArrayList<Censo> censo,String nic) throws SQLException {
        boolean result = false;
        int cont=0;
        if (censo.isEmpty() ) return true;
        LOGGER.log(Level.INFO, "[REQUEST_VISITA_CENSO_EFECTIVA] -> [{0}] Add equipos. Items {1} id {2}", new Object[] {nic, censo.size(), id});
        String sql ="INSERT INTO qo_censo (NUM_OS,ID_EQUIPO,CARGA,CANTIDAD,VISITA) VALUES (?,?,0,?,?)"; 
        for (Censo c : censo) {
            java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
            pst.setInt(1, id);
            pst.setString(2, c.getEquipo());
            pst.setInt(3, c.getCantidad());
            pst.setInt(4, id);
            if (conexion.Update(pst) > 0) {
                LOGGER.log(Level.INFO, "[REQUEST_VISITA_CENSO_EFECTIVA] -> [{0}] Add equipo {1} cantidad {2} id {3} ", new Object[] {nic, c.getEquipo(), c.getCantidad(), id});
                cont++;
            }
            
        }
        if (cont==censo.size()) {
            LOGGER.log(Level.INFO, "[REQUEST_VISITA_CENSO_EFECTIVA] -> [{0}] Equipos saved OK. Items {1}", new Object[]{nic, cont});
            result = true;
        }else {
            throw new SQLException("Error al guardar items del censo");
        }
        
        return result;
    }
    
    public boolean AddImage(int id, ArrayList<Foto> fotos) throws FileNotFoundException, IOException, SQLException {
        
        if (fotos.isEmpty()) return true;
        
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
                String sql = "INSERT INTO camp_orden_fotos (id_orden,path, filename,fecha,visita) VALUES (?,?,?, SYSDATE(),1)";
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
        }else {
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
    
    public boolean isOrdenCenso(String orden) throws SQLException {
        boolean result = false;
        String sql = "SELECT DISTINCT NUM_OS FROM QO_CENSO WHERE NUM_OS=?";
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        pst.setString(1, orden);
        java.sql.ResultSet rs = conexion.Query(pst);
        if (rs.next()) {
            result = true;
        }
        rs.close();
        
        return false;
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
    
    public boolean reasignar(String brigada_old, String brigada_new) throws SQLException {
        String sql = "UPDATE visitas SET brigada=? where brigada=? and estado=1";
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        pst.setString(1, brigada_new);
        pst.setString(1, brigada_old);
        
        if (conexion.Update(pst) > 0) {
            conexion.Commit();
            return true;
        } else {
            return false;
        }
        
        
       
    }
    
    public long ContadorPhoto(int id) throws SQLException {
        long contador = 0;
        String sql = "SELECT count(*) as total FROM camp_orden_fotos WHERE id_orden=? AND visita=1";
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        pst.setInt(1, id);
        java.sql.ResultSet rs = conexion.Query(pst);
        if (rs.next()) {
            contador = rs.getLong("total");
        }
        
        return contador;
    }
    

}
