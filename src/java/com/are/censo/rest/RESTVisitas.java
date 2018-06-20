/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.are.censo.rest;

import com.are.censo.controlador.CtlVisitas;
import com.are.censo.entidades.VisitaRequest;
import com.are.entidades.Visita;
import com.are.sofatec.db;
import com.google.gson.Gson;
import java.io.BufferedReader;
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
@WebServlet(name = "RESTVisitas", urlPatterns = {"/censo/visitas"})
public class RESTVisitas extends HttpServlet {

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
        Gson gson = new Gson();
        ArrayList<Visita> lista = new ArrayList<Visita>();
        db conexion = null;
        try (PrintWriter out = response.getWriter()) {

            VisitaRequest visitaRequest = null;
            String json = "";
            
            try {

                StringBuilder sb = new StringBuilder();
                BufferedReader br = request.getReader();
                String str = "";

                while ((str = br.readLine()) != null) {
                    sb.append(str);
                }

                json = sb.toString();

                if (json.equals("")) {
                    throw new Exception("Parametro JSON vacio");
                }

                visitaRequest = gson.fromJson(json, VisitaRequest.class);

                if (visitaRequest == null) {
                    throw new Exception("JSON no válido");
                }

                if (visitaRequest.getToken().equals("")) {
                    throw new Exception("atributo TOKEN no válido");
                }

                if (visitaRequest.getBrigada().equals("")) {
                    throw new Exception("atributo Brigada no válido");
                }

                
                conexion = new db();
                CtlVisitas ctl = new CtlVisitas(conexion);
                ctl.setVisitaRequest(visitaRequest);
                ctl.List();
                lista = ctl.getVisitas();

            } catch (SQLException ex) {
                Logger.getLogger(RESTVisitas.class.getName()).log(Level.SEVERE, null, ex);
                response.sendError(500, "Error de base de datos:" + ex.getMessage());
            } catch (Exception ex) {
                Logger.getLogger(RESTVisitas.class.getName()).log(Level.SEVERE, null, ex);
                response.sendError(500, "Error de base de datos:" + ex.getMessage());
            } finally {
                if (conexion != null) {
                    conexion.Close();
                }
            }

            out.print(gson.toJson(lista));

        } catch (Exception ex) {
            Logger.getLogger(RESTVisitas.class.getName()).log(Level.SEVERE, null, ex);
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
