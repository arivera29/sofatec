/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.are.servlet;

import com.are.entidades.Visita;
import com.are.manejadores.ManejadorVisita;
import com.are.sofatec.db;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
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
 * @author aimer
 */
@WebServlet(name = "SrvDownloadVisitas", urlPatterns = {"/SrvDownloadVisitas"})
public class SrvDownloadVisitas extends HttpServlet {

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
        db conexion = null;
        String id = (String)request.getParameter("id");
        SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy H:mm");
        try (PrintWriter out = response.getWriter()) {
            response.setContentType("application/vnd.ms-excel");
                response.setHeader("Content-Disposition", "attachment; filename=\"visitas.csv\""); 
            try {
                conexion = new db();
            
                ManejadorVisita manejador = new ManejadorVisita(conexion);
                ArrayList<Visita> lista = manejador.ListByBrigada(id, 1);
                
                out.println("ID;TIPO;NIC;DPTO;MUNICIPIO;DIRECCION;BARRIO;CLIENTE;BRIGADA;ESTADO;FECHA_ASIG;USUARIO;FECHA_CARGA;");
                if (lista.size() > 0) {
                    

                    for (Visita visita : lista) { 
                        String linea = visita.getId() + ";";
                        linea += visita.getTipo() + ";";
                        linea += visita.getNic() + ";";
                        linea += visita.getDepartamento() + ";";
                        linea += visita.getMunicipio() + ";";
                        linea += visita.getDireccion() + ";";
                        linea += visita.getBarrio() + ";";
                        linea += visita.getCliente() + ";";
                        linea += visita.getBrigada() + ";";
                        linea += visita.getEstado() + ";";
                        linea += formato.format(visita.getFechaAsignacion()) + ";";
                        linea += visita.getUsuario() + ";";
                        linea += formato.format(visita.getFechaCarga()) + ";";
                        out.println(linea);

                    }

                }
            
            } catch (SQLException ex) {
                Logger.getLogger(SrvDownloadVisitas.class.getName()).log(Level.SEVERE, null, ex);
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
