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
@WebServlet(name = "SrvRecursoRol", urlPatterns = {"/SrvRecursoRol"})
public class SrvRecursoRol extends HttpServlet {

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
            String rol = (String)request.getParameter("rol");
            String contratista = (String)request.getParameter("contratista");
            String id = (String)request.getParameter("id");
            
            db conexion = null;
            
            try {
                conexion = new db();
                String sql = "SELECT recucodi, recunomb FROM recurso WHERE recurol=? AND recucont=? and recuesta=1";
                java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
                pst.setString(1, rol);
                pst.setString(2, contratista);
                java.sql.ResultSet rs = conexion.Query(pst);
                out.println(String.format("<select name='%s' id='%s'> ",id,id));
                out.println("<option value=''>Seleccionar</option>");
                out.println("<option value='-1'>NO APLICA</option>");
                while (rs.next()) {
                    out.println(String.format("<option value='%s'>%s (%s)</option>",rs.getString("recucodi"),rs.getString("recunomb"),rs.getString("recucodi")));
                }
                out.println("</select> ");
                
                
            }catch (SQLException e) {
                
            }catch (Exception e) {
                
            }finally {
                
                if (conexion != null) {
                    try {
                        conexion.Close();
                    }catch(SQLException e) {
                        
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
