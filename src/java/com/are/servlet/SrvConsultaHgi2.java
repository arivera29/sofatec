/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.are.servlet;

import com.are.entidades.InfoActa;
import com.are.sofatec.ConectorHttp;
import com.are.sofatec.db;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
@WebServlet(name = "SrvConsultaHgi2", urlPatterns = {"/SrvConsultaHgi2"})
public class SrvConsultaHgi2 extends HttpServlet {

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
            db conexion = null;

            try {
                conexion = new db();

                String sql = "SELECT ID, NUM_OS FROM camp_orden WHERE UPDATE_WS=0 ";
                java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
                java.sql.ResultSet rs = conexion.Query(pst);

                while (rs.next()) {
                    InfoActa acta = consultaHGI(rs.getString("NUM_OS"), out);
                    if (acta != null) {
                        if (acta.getNumActa() != 0) {

                            sql = "UPDATE camp_orden SET "
                                    + "FECHA_CIERRE=?, "
                                    + "ANOMALIA=?, "
                                    + "OBS_ACTA=?, "
                                    + "ACCION_IR=?, "
                                    + "FR=?,"
                                    + "F_IRREG=?,"
                                    + "ECDF=?, "
                                    + "ESTADO_HGI=?,"
                                    + "UPDATE_WS=? "
                                    + "WHERE ID=?  ";
                            java.sql.PreparedStatement pst1 = conexion.getConnection().prepareStatement(sql);
                            pst1.setDate(1, new java.sql.Date(acta.getFechaCierre().getTime()));
                            pst1.setString(2, acta.getAnomalia());
                            pst1.setString(3, acta.getObservacion());
                            pst1.setString(4, acta.getAccionIrregularidad());
                            pst1.setString(5, acta.getFR());
                            pst1.setDate(6, new java.sql.Date(acta.getFechaCierre().getTime()));
                            pst1.setDouble(7, acta.getECDF());
                            pst1.setString(8, acta.getDescEstado());
                            
                            if (acta.getEstado() == 10 || acta.getEstado() == 11 || acta.getEstado()== 15 ) {
                                pst1.setInt(9, 1);
                            }else {
                                pst1.setInt(9, 0);
                            }
                            
                            pst1.setInt(10, rs.getInt("ID"));

                            if (conexion.Update(pst1) > 0) {
                                conexion.Commit();
                                out.println("<br>" + rs.getString("NUM_OS") + ": Actualizada con información de la HGI2");
                                
                            }

                        } else {
                            out.println("<br>" + rs.getString("NUM_OS") + ": Sin respuesta de la HGI2");
                        }
                    }else {
                        out.println("<br>" + rs.getString("NUM_OS") + ": Error respuesta HGI2");
                    }

                }
                out.println("<br>Proceso finalizado");

            } catch (SQLException ex) {
                Logger.getLogger(SrvConsultaHgi2.class.getName()).log(Level.SEVERE, null, ex);
                out.println(ex.getMessage());
            }

        }
    }

    private InfoActa consultaHGI(String orden, PrintWriter out) {
        InfoActa acta = new InfoActa();
        InputStream in = null;
        Gson gson = new Gson();

        String url = "http://hgielectricaribe.com.co:8080/WsHgi/SrvConsultaSofatec";
        out.print("<br>" + url + " orden:" + orden );
        try {
            ConectorHttp con = new ConectorHttp();
            in = con.MetodoGET("acta=" + orden, url);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));

            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            out.println("<br>Respuesta:" + sb.toString());
            acta = gson.fromJson(sb.toString(), InfoActa.class);
            in.close();

        } catch (Exception ex) {
            Logger.getLogger(SrvConsultaHgi2.class.getName()).log(Level.SEVERE, null, ex);
            out.println(ex.getMessage());
        }

        return acta;
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
