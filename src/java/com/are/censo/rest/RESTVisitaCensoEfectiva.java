/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.are.censo.rest;

import com.are.censo.controlador.CtlVisitas;
import com.are.censo.entidades.Censo;
import com.are.censo.entidades.Foto;
import com.are.censo.entidades.VisitaCensoEfectivaRequest;
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
@WebServlet(name = "RESTVisitaCensoEfectiva", urlPatterns = {"/censo/efectiva"})
public class RESTVisitaCensoEfectiva extends HttpServlet {

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
            VisitaCensoEfectivaRequest visita = null;
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

                visita = gson.fromJson(json, VisitaCensoEfectivaRequest.class);

                if (visita == null) {
                    throw new Exception("JSON no válido");
                }

                if (visita.getId().equals("")) {
                    throw new Exception("Atributo ID no válido");
                }

                if (visita.getToken().equals("")) {
                    throw new Exception("Atributo token no válido");
                }

                if (visita.getNic().equals("")) {
                    throw new Exception("Atributo NIC no válido");
                }
                if (visita.getFecha().equals("")) {
                    throw new Exception("Atributo FECHA no válido");
                }

                if (visita.getContrata().equals("")) {
                    throw new Exception("Atributo CONTRATA no válido");
                }

                if (visita.getBrigada().equals("")) {
                    throw new Exception("Atributo BRIGADA no válido");
                }
                if (visita.getInspector().equals("")) {
                    throw new Exception("Atributo INSPECTOR no válido");
                }
                if (visita.getIngeniero().equals("")) {
                    throw new Exception("atributo INGENIERO no válido");
                }
                if (visita.getZona().equals("")) {
                    throw new Exception("Atributo ZONA no válido");
                }
                if (visita.getObservacion().equals("")) {
                    throw new Exception("Atributo OBSERVACION no válido");
                }
                if (visita.getImei().equals("")) {
                    throw new Exception("Atributo IMEI no válido");
                }

                if (visita.getNro_acta().equals("")) {
                    throw new Exception("Atributo NRO ACTA no válido");
                }

                if (visita.getFecha_acta().equals("")) {
                    throw new Exception("Atributo FECHA ACTA no válido");
                }

                if (visita.getBrigada().equals("")) {
                    throw new Exception("Atributo BRIGADA no válido");
                }
                if (visita.getTarifa().equals("")) {
                    throw new Exception("Atributo TARIFA no válido");
                }
                if (visita.getUso().equals("")) {
                    throw new Exception("Atributo USO no válido");
                }
                if (visita.getCenso() == null) {
                    throw new Exception("Atributo CENSO no válido");
                } else if (visita.getCenso().isEmpty()) {
                    throw new Exception("Atributo CENSO vacío no válido");
                }

                if (visita.getImei().equals("")) {
                    throw new Exception("Atributo IMEI no válido");
                }
                
                if (visita.getUsuario().equals("")) {
                    throw new Exception("Atributo USUARIO no válido");
                }

                if (visita.getFotos() != null) {
                    if (visita.getFotos().size() > 0) {
                        int contador = 0;
                        for (Foto foto : visita.getFotos()) {
                            contador++;
                            if (foto.getFilename().equals("")) {
                                throw new Exception("Atributo FILENAME del listado de fotos no válido. Item " + contador);
                            }
                            if (foto.getStrBase64().equals("")) {
                                throw new Exception("Atributo STRBASE64 del listado de fotos no válido. Item " + contador);
                            }
                        }
                    }
                } else {
                    visita.setFotos(new ArrayList<Foto>());
                }
                
                if (visita.getCenso() != null) {
                    if (visita.getCenso().size() > 0 ) {
                        int contador = 0;
                        for (Censo censo : visita.getCenso()) {
                            contador++;
                            if (censo.getEquipo().equals("")) {
                                throw new Exception("Atributo EQUIPO del listado de censo no válido. Item " + contador);
                            }
                            
                            if (censo.getCantidad() == 0) {
                                throw new Exception("Atributo CANTIDAD del listado de censo no válido. Item " + contador);
                            }
                            
                        }
                    }
                }
                conexion = new db();
                CtlVisitas controlador = new CtlVisitas(conexion);
                controlador.setDirImages(directorio);
                if (controlador.UpdateVisitaCensoEfectiva(visita)) {
                    visitaResponse.setError(false);
                    visitaResponse.setMsgError("");
                    visitaResponse.setSync(true);
                } else {
                    visitaResponse.setError(true);
                    visitaResponse.setMsgError("Error al actualizar el registro");
                    visitaResponse.setSync(false);
                }

            } catch (SQLException ex) {
                Logger.getLogger(RESTVisitaCensoEfectiva.class.getName()).log(Level.SEVERE, null, ex);
                visitaResponse.setError(true);
                visitaResponse.setMsgError(ex.getMessage());
                visitaResponse.setSync(false);
            } catch (Exception ex) {
                Logger.getLogger(RESTVisitaCensoEfectiva.class.getName()).log(Level.SEVERE, null, ex);
                visitaResponse.setError(true);
                visitaResponse.setMsgError(ex.getMessage());
                visitaResponse.setSync(false);
            }finally {
                if (conexion != null) {
                    try {
                        conexion.Close();
                    } catch (SQLException ex) {
                        Logger.getLogger(RESTVisitaCensoEfectiva.class.getName()).log(Level.SEVERE, null, ex);
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
