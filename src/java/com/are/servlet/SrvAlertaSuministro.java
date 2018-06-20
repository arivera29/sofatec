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
@WebServlet(name = "SrvAlertaSuministro", urlPatterns = {"/SrvAlertaSuministro"})
public class SrvAlertaSuministro extends HttpServlet {

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

            String nic = (String)request.getParameter("nic");
            db conexion = null;

            try {
                conexion = new db();
                String sql = "SELECT NUM_OS, DATE(FECHA_CARGA) FECHA_CARGA FROM camp_orden "
                        + " WHERE NIC=? "
                        + "AND DATEDIFF(DATE(FECHA_CARGA),CURRENT_DATE()) <= 40 "
                        + "ORDER BY FECHA_CARGA DESC "
                        + "LIMIT 1";
                        
                java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
                pst.setString(1, nic);
                java.sql.ResultSet rs = conexion.Query(pst);
                if (rs.next()) {
                    out.print("Este nic presenta una OS generada antes de 40 días: OS:" + rs.getString("NUM_OS") + " fecha:" + rs.getString("FECHA_CARGA"));
                }
                        
                        
            } catch (SQLException ex) {
                Logger.getLogger(SrvAlertaSuministro.class.getName()).log(Level.SEVERE, null, ex);
                out.print("Error: " + ex.getMessage());
            } finally {
                
                if (conexion != null) {
                    try {
                        conexion.Close();
                    } catch (SQLException ex) {
                        Logger.getLogger(SrvAlertaSuministro.class.getName()).log(Level.SEVERE, null, ex);
                        out.print("Error: " + ex.getMessage());
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
