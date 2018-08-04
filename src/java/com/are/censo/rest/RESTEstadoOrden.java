/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.are.censo.rest;

import com.are.censo.controlador.CtlEstadoOrden;
import com.are.censo.entidades.EstadoOrden;
import com.are.censo.entidades.EstadoOrdenResult;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
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
@WebServlet(name = "RESTEstadoOrden", urlPatterns = {"/os/estado"})
public class RESTEstadoOrden extends HttpServlet {
    
    private final static Logger LOGGER = Logger.getLogger(RESTEstadoOrden.class.getName());
    
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
            LOGGER.log(Level.INFO, "[REQUEST_STATE_OS] -> Init");
            Gson gson = new Gson();
            EstadoOrden estado = null;
            EstadoOrdenResult estadoResult = new EstadoOrdenResult();
            String json = "";
            try {

                StringBuilder sb = new StringBuilder();
                BufferedReader br = request.getReader();
                String str = "";

                while ((str = br.readLine()) != null) {
                    sb.append(str);
                }

                json = sb.toString();

                LOGGER.log(Level.INFO, "[REQUEST_STATE_OS] -> Validating JSON");
                if (json.equals("")) {
                    LOGGER.log(Level.INFO, "[REQUEST_STATE_OS] -> SON empty");
                    throw new Exception("Parametro JSON vacio");
                }
                
                estado = gson.fromJson(json, EstadoOrden.class);
                
                
                if (estado == null) {
                    LOGGER.log(Level.INFO, "[REQUEST_STATE_OS] -> JSON no valid!");
                    throw new Exception("JSON no válido");
                }
                
                if (estado.getToken().equals("")) {
                    LOGGER.log(Level.INFO, "[REQUEST_STATE_OS] -> Field TOKEN empty");
                    throw new Exception("atributo TOKEN no válido");
                }
                
                if (estado.getId().equals("")) {
                    LOGGER.log(Level.INFO, "[REQUEST_STATE_OS] -> Filed ID empty");
                    throw new Exception("atributo ID no válido");
                }

                
                LOGGER.log(Level.INFO, "[REQUEST_STATE_OS] -> Valid state OS of ID {0}", estado.getId());
                CtlEstadoOrden controlador = new CtlEstadoOrden(estado);
                controlador.Find();
                estadoResult =controlador.getEstadoResult();
                if (!estadoResult.isError()) {
                    LOGGER.log(Level.INFO, "[REQUEST_STATE_OS] -> Find {0}", estadoResult.isEncontrado()?"SI":"NO");
                    LOGGER.log(Level.INFO, "[REQUEST_STATE_OS] -> State {0}", estadoResult.getEstado());
                    LOGGER.log(Level.INFO, "[REQUEST_STATE_OS] -> OS number {0}", estadoResult.getOrden());
                }else {
                    LOGGER.log(Level.INFO, "[REQUEST_STATE_OS] -> Find Error " );
                    LOGGER.log(Level.INFO, "[REQUEST_STATE_OS] -> Error  {0}", estadoResult.getMsgError());
                }
                
            } catch (Exception ex) {
                Logger.getLogger(RESTEstadoOrden.class.getName()).log(Level.SEVERE, null, ex);
                estadoResult.setError(true);
                estadoResult.setMsgError(ex.getMessage());
            }
   
            out.print(gson.toJson(estadoResult));
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
