/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.are.servlet;

import com.are.sofatec.db;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author aimerrivera
 */
@WebServlet(name = "SrvPlantillaResolver", urlPatterns = {"/SrvPlantillaResolver"})
public class SrvPlantillaResolver extends HttpServlet {

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
            /* TODO output your page here. You may use following sample code. */
            String orden = (String) request.getParameter("orden");
            String tipo = (String) request.getParameter("tipo");

            db conexion = null;
            try {
                conexion = new db();
                String sql = "SELECT * FROM QO_PLANRESO WHERE TIP_OS=?";
                java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
                pst.setString(1, tipo);
                java.sql.ResultSet rs = conexion.Query(pst);
                
                if (rs.next()) {
                    InicializarOrden(orden, conexion);
                    this.AgregarLectura(orden, conexion);
                    conexion.Update("DELETE FROM QO_NUEVOS_PASOS WHERE NUM_OS='" + orden + "'");
                    boolean pasa = true;
                    int num_paso=0;
                    do {
                        
                        if (rs.getString("TIP_PASO").equals("1")) {
                            sql = "UPDATE QO_PASOS SET CUMPLIDO=1, CO_ACCEJE=?, OBSERVACION=? WHERE NUM_OS=? AND DESCRIPCION=? ";
                            java.sql.PreparedStatement pst2 = conexion.getConnection().prepareStatement(sql);
                            pst2.setString(1,rs.getString("CO_ACCEJE"));
                            pst2.setString(2,rs.getString("OBSERVACION"));
                            pst2.setString(3, orden);
                            pst2.setString(4,rs.getString("DESC_PASO"));
                            if (conexion.Update(pst2) > 0) {
                                
                            }else {
                                pasa = false;
                            }

                        }else { // Nuevo paso
                            num_paso++;
                            sql = "INSERT INTO QO_NUEVOS_PASOS " +
                                "(NUM_OS," +
                                "NUM_PASO," +
                                "OPCOND," +
                                "DESCRIPCION," +
                                "CONDICION," +
                                "ELSEACCION," +
                                "CUMPLIDO," +
                                "CO_ACCEJE," +
                                "IND_DECISOR," +
                                "OBSERVACION," +
                                "COBRO," +
                                "PARENT," +
                                "FLUJO)"+
                                " VALUES " +
                                "(?," +
                                "?," +
                                "2," +
                                "?," +
                                "2," +
                                "0," +
                                "1," +
                                "?," +
                                "1," +
                                "?," +
                                "1," +
                                "?," +
                                "?)";
                            java.sql.PreparedStatement pst2 = conexion.getConnection().prepareStatement(sql);
                            pst2.setString(1,orden);
                            pst2.setInt(2,num_paso);
                            pst2.setString(3,rs.getString("DESC_PASO"));
                            pst2.setString(4,rs.getString("CO_ACCEJE"));
                            pst2.setString(5,rs.getString("OBSERVACION"));
                            pst2.setString(6,rs.getString("PARENT"));
                            pst2.setString(7,rs.getString("FLUJO"));
                            if (conexion.Update(pst2) > 0) {
                                
                            }else {
                                pasa = false;
                            }
                            
                            
                        }

                    } while (rs.next() && pasa == true);
                    
                    if (pasa == true) {
                        conexion.Commit();
                        out.print("OK");
                    }

                } else {
                    out.print("No existe plantilla para el tipo de orden " + tipo);

                }

            } catch (SQLException ex) {
                out.print("Error: " + ex.getMessage());
            } finally {

                if (conexion != null) {
                    try {
                        conexion.Close();
                    } catch (SQLException ex) {
                        out.print("Error: " + ex.getMessage());
                    }

                }

            }
        }

    }
    
    private void InicializarOrden(String orden, db conexion) throws SQLException {
        String sql = "UPDATE QO_PASOS SET CUMPLIDO=0, CO_ACCEJE='', OBSERVACION='' WHERE NUM_OS=? ";
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        pst.setString(1,orden);
        conexion.Update(pst);
        
        sql = "DELETE FROM QO_NUEVOS_PASOS WHERE NUM_OS=? ";
        java.sql.PreparedStatement pst2 = conexion.getConnection().prepareStatement(sql);
        pst2.setString(1,orden);
        conexion.Update(pst);
        
        
    }
    
    private void AgregarLectura(String orden, db conexion) throws SQLException {
        conexion.Update("DELETE FROM QO_ORDEN_LECTURA WHERE NUM_OS='" + orden + "'");
        String sql = "SELECT NUM_APA FROM QO_APARATOS WHERE NUM_OS=?";
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        pst.setString(1, orden);
        java.sql.ResultSet rs = conexion.Query(pst);
        if (rs.next()) {
            sql = "INSERT INTO QO_ORDEN_LECTURA (NUM_OS,NUM_APA,LECTURA) VALUES (?,?,1)";
            java.sql.PreparedStatement pst2 = conexion.getConnection().prepareStatement(sql);
            pst2.setString(1, orden);
            pst2.setString(2, rs.getString("NUM_APA"));
            conexion.Update(pst2);
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
