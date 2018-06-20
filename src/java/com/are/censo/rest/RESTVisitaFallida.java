/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.are.censo.rest;

import com.are.censo.controlador.CtlVisitas;
import com.are.censo.entidades.Foto;
import com.are.censo.entidades.VisitaFallidaRequest;
import com.are.censo.entidades.VisitaResponse;
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
@WebServlet(name = "RESTVisitaFallida", urlPatterns = {"/visita/fallida"})
public class RESTVisitaFallida extends HttpServlet {

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
        String directorio = this.getServletContext().getRealPath("/imagenes");  // Directorio de las imagenes
        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
            VisitaFallidaRequest visita = null;
            VisitaResponse visitaResponse = new VisitaResponse();
            String json = "";
            db conexion = null;
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

                visita = gson.fromJson(json, VisitaFallidaRequest.class);

                if (visita == null) {
                    throw new Exception("JSON no válido");
                }

                if (visita.getToken().equals("")) {
                    throw new Exception("atributo token no válido");
                }

                if (visita.getId() == 0) {
                    throw new Exception("atributo ID no válido");
                }

                if (visita.getAnomalia().equals("")) {
                    throw new Exception("atributo ANOMALIA no válido");
                }

                if (visita.getBrigada().equals("")) {
                    throw new Exception("atributo BRIGADA no válido");
                }

                if (visita.getImei().equals("")) {
                    throw new Exception("atributo IMEI no válido");
                }

                if (visita.getFotos() != null) {

                    if (visita.getFotos().size() > 0) {
                        int contador = 0;
                        for (Foto foto : visita.getFotos()) {
                            contador++;
                            if (foto.getFilename().equals("")) {
                                throw new Exception("atributo FILENAME del listado de fotos no válido. Item " + contador);
                            }
                            if (foto.getStrBase64().equals("")) {
                                throw new Exception("atributo STRBASE64 del listado de fotos no válido. Item " + contador);
                            }
                        }
                    }
                }else {
                    visita.setFotos(new ArrayList<Foto>());
                }
                conexion = new db();
                CtlVisitas controlador = new CtlVisitas(conexion);
                controlador.setDirImages(directorio);
                if (controlador.UpdateVisitaFallida(visita)) {
                    visitaResponse.setError(false);
                    visitaResponse.setMsgError("");
                    visitaResponse.setSync(true);
                } else {
                    visitaResponse.setError(true);
                    visitaResponse.setMsgError("Error al actualizar el registro");
                    visitaResponse.setSync(false);
                }

            } catch (SQLException ex) {
                Logger.getLogger(RESTVisitaFallida.class.getName()).log(Level.SEVERE, null, ex);
                visitaResponse.setError(true);
                visitaResponse.setMsgError(ex.getMessage());
                visitaResponse.setSync(false);
            } catch (IOException ex ) {
                Logger.getLogger(RESTVisitaFallida.class.getName()).log(Level.SEVERE, null, ex);
                visitaResponse.setError(true);
                visitaResponse.setMsgError(ex.getMessage());
                visitaResponse.setSync(false);
            } catch (Exception ex) {
                Logger.getLogger(RESTVisitaFallida.class.getName()).log(Level.SEVERE, null, ex);
                visitaResponse.setError(true);
                visitaResponse.setMsgError(ex.getStackTrace().toString());
                visitaResponse.setSync(false);
                
            }finally {
                if (conexion != null) {
                    try {
                        conexion.Close();
                    } catch (SQLException ex) {
                        Logger.getLogger(RESTVisitaFallida.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                out.print(gson.toJson(visitaResponse));
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
