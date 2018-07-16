/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.are.servlet;

import com.are.manejadores.SendMail;
import com.are.sofatec.db;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author aimerrivera
 */
@WebServlet(name = "SrvActualizarOrden", urlPatterns = {"/SrvActualizarOrden"})
public class SrvActualizarOrden extends HttpServlet {
    private final static Logger LOGGER = Logger.getLogger(SrvActualizarOrden.class.getName());
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            String id = (String)request.getParameter("id");
            String orden = (String)request.getParameter("orden");
            String camp = (String)request.getParameter("camp");

            
            db conexion = null;
            LOGGER.log(Level.INFO, "UPDATE ORDEN -> Peticion actualizar orden ... OS: {0} ID: {1} CAMP: {2}", new Object[]{orden, id, camp});
            
            try {
                conexion = new db();
                
                String sql = "SELECT NUM_OS,ID_VISITA,BRIGADA FROM camp_orden WHERE NUM_OS =?";
                java.sql.PreparedStatement pst0 = conexion.getConnection().prepareStatement(sql);
                pst0.setString(1, orden);
                java.sql.ResultSet rs1 = conexion.Query(pst0);
                if (rs1.next()) {
                    LOGGER.log(Level.INFO, "UPDATE ORDEN -> Ya se encuentra registrada la OS {0}", orden);
                    throw new SQLException("Ya se encuentra registrada la orden de servic¡o");
                }
                
                sql = "SELECT NUM_OS,ID_VISITA,BRIGADA FROM camp_orden WHERE ID =?";
                java.sql.PreparedStatement pst5 = conexion.getConnection().prepareStatement(sql);
                pst5.setString(1, id);
                java.sql.ResultSet rs2 = conexion.Query(pst5);
                if (!rs2.next()) {
                    LOGGER.log(Level.INFO, "UPDATE ORDEN -> No se encuentra de registro de Generacion de OS ID: {0}", id);
                    throw new SQLException("No se encuentra el registro ID " + id);
                }
                
                sql = "UPDATE camp_orden SET NUM_OS=?, FECHA_GEN_OS=SYSDATE(), ID_CAMP=? WHERE id=? AND NUM_OS=''";
                java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
                pst.setString(1, orden);
                pst.setString(2, camp);
                pst.setString(3, id);
                if (conexion.Update(pst) > 0) {
                    
                    if (rs2.getInt("ID_VISITA")!= 0) {
                        LOGGER.log(Level.INFO, "UPDATE ORDEN -> Orden asociada a visita ... OS: {0} ID_VISITA:{1}", new Object[]{orden, rs2.getInt("ID_VISITA")});
                        sql = "UPDATE qo_censo SET NUM_OS=? WHERE VISITA=?";
                        java.sql.PreparedStatement pst2 = conexion.getConnection().prepareStatement(sql);
                        pst2.setString(1, orden);
                        pst2.setInt(2, rs2.getInt("ID_VISITA"));
                        if (conexion.Update(pst2) > 0) {
                            LOGGER.log(Level.INFO, "UPDATE ORDEN -> [CENSO_UPDATED] OS: {0} ID_VISITA: {1}", new Object[]{orden, rs2.getInt("ID_VISITA")});
                        }
        
                    }
                    
                    
                    conexion.Commit();
                    LOGGER.log(Level.INFO, "UPDATE ORDEN -> Commit OK ... OS {0}", orden);
                    LOGGER.log(Level.INFO, "UPDATE ORDEN -> Sending Mail ... OS {0}", orden);
                    
                    sql = "SELECT contcodi,contmail,c.NUM_OS, c.BRIGADA FROM contratistas "
                            + " INNER JOIN camp_orden c ON c.CONTRATISTA = contcodi AND  c.ID =?";
                    java.sql.PreparedStatement pst1 = conexion.getConnection().prepareStatement(sql);
                    pst1.setString(1, id);
                    java.sql.ResultSet rs = conexion.Query(pst1);
                    if (rs.next()) {
                        String correo = rs.getString("contmail");
                        if (!correo.equals("") && !correo.toUpperCase().equals("NA")) {
                            String cuerpo = "Buen dia\n\n\nSe ha generado la OS " + orden 
                                    + " en el sistema OPEN, favor crear lote y subir en la HDA asignando la brigada " 
                                    + rs.getString("BRIGADA")
                                    + "\n\n\nMensaje generado automáticamente, no responder.";
                            SendMail.enviarMail(correo, "Notificación SOFATEC Software", cuerpo);
                            LOGGER.log(Level.INFO, "UPDATE ORDEN -> Send Mail OK ... OS {0}", orden);
                        }
                    }else {
                        
                    }
                    
                    out.print("OK");
                }else {
                    LOGGER.log(Level.INFO, "UPDATE ORDEN -> Error al actualizar registro ... OS {0}", orden);
                    out.print("Error al actualizar el registro");
                }
                
                
                
            }catch (SQLException e) {
                out.print("Error " + e.getMessage());
            }catch (Exception e) {
                out.print("Error " + e.getMessage());
                e.printStackTrace(out);
            }finally {
                
                if (conexion != null) {
                    try {
                        conexion.Close();
                    }catch(SQLException e) {
                        out.print("Error " + e.getMessage());
                    }
                }
            }
            
            
            
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
