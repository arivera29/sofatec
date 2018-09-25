/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.are.servlet;

import com.are.sofatec.db;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
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
@WebServlet(name = "SrvDownloadPhotoVisita", urlPatterns = {"/SrvDownloadPhotoVisita"})
public class SrvDownloadPhotoVisita extends HttpServlet {

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
        try {
            String id = (String) request.getParameter("id");
            String visita = (String) request.getParameter("visita");
            
            db conexion = null;
            try {
                conexion = new db();
                String sql = "SELECT path FROM camp_orden_fotos WHERE id=? and visita=?";
                java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
                pst.setInt(1, Integer.parseInt(id));
                pst.setInt(2, Integer.parseInt(visita));
                java.sql.ResultSet rs = conexion.Query(pst);
                if (rs.next()) {
                    String path = rs.getString("path");
                    //path = this.parserPath(path);
                    File file = new File(path);
                    if (file.exists()) {
                        FileInputStream inStream = new FileInputStream(file);
                        String mimeType = "application/octet-stream";
                        // modifies response
                        response.setContentType(mimeType);
                        response.setContentLength((int) file.length());

                        // forces download
                        String headerKey = "Content-Disposition";
                        String fname = file.getName();
                        String headerValue = String.format("attachment; filename=\"%s\"", fname);
                        response.setHeader(headerKey, headerValue);

                        // obtains response's output stream
                        OutputStream outStream = response.getOutputStream();

                        byte[] buffer = new byte[4096];
                        int bytesRead = -1;

                        while ((bytesRead = inStream.read(buffer)) != -1) {
                            outStream.write(buffer, 0, bytesRead);
                        }

                        inStream.close();
                        outStream.close();

                    }else {
                        
                        PrintWriter out = response.getWriter();
                        out.println(String.format("Archivo %s no se encuentra", path));
                        out.close();
                    } 

                }

            } catch (SQLException ex) {
                Logger.getLogger(SrvDownloadPhotoVisita.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    conexion.Close();
                } catch (SQLException ex) {
                    Logger.getLogger(SrvDownloadPhotoVisita.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        } finally {
           
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
