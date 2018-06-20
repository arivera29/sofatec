/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.are.censo.rest;

import com.are.censo.entidades.Tarifa;
import com.are.sofatec.db;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
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
@WebServlet(name = "RESTTarifa", urlPatterns = {"/parametros/tarifas"})
public class RESTTarifa extends HttpServlet {

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
        response.setContentType("text/json;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            ArrayList<Tarifa> tarifas = new ArrayList<Tarifa>();
            db conexion = null;
            
            try {
                conexion = new db();
                String sql = "SELECT COD_TAR,DESC_TAR FROM qo_tarifas ORDER BY DESC_TAR";
                java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
                java.sql.ResultSet rs = conexion.Query(pst);
                while (rs.next()) {
                    Tarifa t = new Tarifa();
                    t.setCodigo(rs.getString("COD_TAR"));
                    t.setNombre(rs.getString("DESC_TAR"));
                    
                    tarifas.add(t);
                    
                }
                
            }catch (SQLException ex) {
                Logger.getLogger(RESTTarifa.class.getName()).log(Level.SEVERE, null, ex);
                response.sendError(500, "Error de base de datos:" + ex.getMessage());
            }finally {
                if (conexion != null) {
                    try {
                        conexion.Close();
                    } catch (SQLException ex) {
                        Logger.getLogger(RESTTarifa.class.getName()).log(Level.SEVERE, null, ex);
                        response.sendError(500, "Error de base de datos:" + ex.getMessage());
                    }
                }
            }
            
            Gson gson = new Gson();
            out.print(gson.toJson(tarifas));
            
            
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
