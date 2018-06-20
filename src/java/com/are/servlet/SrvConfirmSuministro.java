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
import javax.servlet.http.HttpSession;

/**
 *
 * @author aimerrivera
 */
@WebServlet(name = "SrvConfirmSuministro", urlPatterns = {"/SrvConfirmSuministro"})
public class SrvConfirmSuministro extends HttpServlet {

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

            String orden = (String)request.getParameter("orden");
            
            
            HttpSession sesion=request.getSession();
            if (sesion.getAttribute("usuario") == null) {
                out.print("La sesion ha expirado, debe iniciar sesión nuevamente");
                return;
            }
            
            String usuario = (String)sesion.getAttribute("usuario");
            
            db conexion = null;

            try {
                conexion = new db();
                String sql = "UPDATE camp_orden SET CONFIRMADA=1, FECHA_CONFIRM=SYSDATE(), USER_CONFIRM=? "
                        + " WHERE NUM_OS = ? AND CONFIRMADA=0";
                        
                java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
                pst.setString(1, usuario);
                pst.setString(2, orden);
                
                if (conexion.Update(pst) > 0) {
                    conexion.Commit();
                    out.print("OK");
                }else {
                    out.print("Error al confirmar la orden");
                }
                        
                        
            } catch (SQLException ex) {
                Logger.getLogger(SrvConfirmSuministro.class.getName()).log(Level.SEVERE, null, ex);
                out.print("Error: " + ex.getMessage());
            } finally {
                
                if (conexion != null) {
                    try {
                        conexion.Close();
                    } catch (SQLException ex) {
                        Logger.getLogger(SrvConfirmSuministro.class.getName()).log(Level.SEVERE, null, ex);
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
