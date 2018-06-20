/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.are.censo.rest;

import com.are.censo.controlador.CtlVisitas;
import com.are.censo.entidades.Foto;
import com.are.censo.entidades.VisitaResponse;
import com.are.censo.entidades.VisitaRevisionEfectivaRequest;
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
@WebServlet(name = "RESTVisitaRevisionEfectiva", urlPatterns = {"/inspeccion/efectiva"})
public class RESTVisitaRevisionEfectiva extends HttpServlet {

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
        //OrdenResult ordenResult = new OrdenResult();
        VisitaResponse visitaResponse = new VisitaResponse();
        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
            VisitaRevisionEfectivaRequest orden = null;
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
                
                orden = gson.fromJson(json, VisitaRevisionEfectivaRequest.class);
                
                if (orden == null) {
                    throw new Exception("JSON no válido");
                }
                
                if (orden.getToken().equals("")) {
                    throw new Exception("atributo token no válido");
                }
                
                if (orden.getId().equals("")) {
                    throw new Exception("atributo ID no válido");
                }
                if (orden.getNic().equals("")) {
                    throw new Exception("atributo NIC no válido");
                }
                if (orden.getFecha().equals("")) {
                    throw new Exception("atributo FECHA no válido");
                }

                if (orden.getContrata().equals("")) {
                    throw new Exception("atributo CONTRATA no válido");
                }
                
                if (orden.getBrigada().equals("")) {
                    throw new Exception("atributo BRIGADA no válido");
                }
                if (orden.getInspector().equals("")) {
                    throw new Exception("atributo INSPECTOR no válido");
                }
                if (orden.getIngeniero().equals("")) {
                    throw new Exception("atributo INGENIERO no válido");
                }
                if (orden.getZona().equals("")) {
                    throw new Exception("atributo ZONA no válido");
                }
                if (orden.getObservacion().equals("")) {
                    throw new Exception("atributo OBSERVACION no válido");
                }
                if (orden.getImei().equals("")) {
                    throw new Exception("atributo IMEI no válido");
                }
                
                if (orden.getUsuario().equals("")) {
                    throw new Exception("Atributo USUARIO no válido");
                }
                
                if (orden.getNro_acta().equals("")) {
                    throw new Exception("atributo NRO ACTA no válido");
                }
                
                if (orden.getFecha_acta().equals("")) {
                    throw new Exception("atributo FECHA ACTA no válido");
                }
                if (orden.getTarifa().equals("")) {
                    throw new Exception("atributo TARIFA no válido");
                }
                if (orden.getUso().equals("")) {
                    throw new Exception("atributo USO no válido");
                }
                
                if (orden.getFotos() != null) {
                    if (orden.getFotos().size() > 0) {
                        int contador = 0;
                        for (Foto foto : orden.getFotos()) {
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

                    orden.setFotos(new ArrayList<Foto>());
                }
                
                conexion = new db();
                CtlVisitas ctl = new CtlVisitas(conexion);
                ctl.setDirImages(directorio);
                if (ctl.UpdateVisitaRevisionEfectiva(orden)) {
                    visitaResponse.setError(false);
                    visitaResponse.setSync(true);
                    visitaResponse.setMsgError("");
                }else {
                    throw new Exception("Error al procesar la visita de revisión");
                }
                
                
            } catch (Exception ex) {
                Logger.getLogger(RESTVisitaRevisionEfectiva.class.getName()).log(Level.SEVERE, null, ex);
                visitaResponse.setError(true);
                visitaResponse.setSync(false);
                visitaResponse.setMsgError(ex.getMessage());
            } finally {
                if (conexion != null) {
                    try {
                        conexion.Close();
                    } catch (SQLException ex) {
                        Logger.getLogger(RESTVisitaRevisionEfectiva.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            
            out.print(gson.toJson(visitaResponse));
   
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
