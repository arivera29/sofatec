/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.are.servlet;

import com.are.censo.controlador.CtlVisitas;
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
@WebServlet(name = "SrvVisitas", urlPatterns = {"/SrvVisitas"})
public class SrvVisitas extends HttpServlet {

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
            String operacion = (String) request.getParameter("operacion");

            db conexion = null;

            try {

                if (operacion.equals("all")) {
                    conexion = new db();
                    CtlVisitas controlador = new CtlVisitas(conexion);
                    if (controlador.removeAllVisitas()) {
                        out.print("OK");
                    }else {
                        out.print("Error al eliminar las visitas");
                    }
                }

                if (operacion.equals("brigada")) {
                    String brigada = (String)request.getParameter("brigada");
                    conexion = new db();
                    CtlVisitas controlador = new CtlVisitas(conexion);
                    if (controlador.removeVisitaByBrigada(brigada)) {
                        out.print("OK");
                    }else {
                        out.print("Error al eliminar las visitas de la brigada " + brigada);
                    }

                }

                if (operacion.equals("one")) {
                    String id = (String)request.getParameter("id");
                    conexion = new db();
                    CtlVisitas controlador = new CtlVisitas(conexion);
                    if (controlador.removeVisitaById(Integer.parseInt(id))) {
                        out.print("OK");
                    }else {
                        out.print("Error al eliminar la visita ID " + id);
                    }

                }
                
                if (operacion.equals("reasignar")) {
                    String brigada_old = (String)request.getParameter("brigada_old");
                    String brigada_new = (String)request.getParameter("brigada_new");
                    conexion = new db();
                    CtlVisitas controlador = new CtlVisitas(conexion);
                    if (controlador.reasignar(brigada_old, brigada_new)) {
                        out.print("OK");
                    }else {
                        out.print("Error al transferir visitas.");
                    }

                }

            } catch (SQLException ex) {
                Logger.getLogger(SrvVisitas.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                if (conexion != null) {
                    try {
                        conexion.Close();
                    } catch (SQLException ex) {
                        Logger.getLogger(SrvVisitas.class.getName()).log(Level.SEVERE, null, ex);
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
