/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.are.servlet;

import com.are.sofatec.db;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author aimerrivera
 */
@WebServlet(name = "SrvInfoSuministro", urlPatterns = {"/SrvInfoSuministro"})
public class SrvInfoSuministro extends HttpServlet {

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
            /* TODO output your page here. You may use following sample code. */

            String nic = (String) request.getParameter("nic");
            db conexion = null;
            Informacion info = new Informacion();

            try {
                conexion = new db();
                String sql = "SELECT NIS_RAD, COD_TAR,MUNICIPIO,LOCALIDAD,"
                        + "DEPARTAMENTO,DIRECCION,APE1_CLI,APE2_CLI,NOM_CLI,REF_DIR,ACC_FINCA "
                        + " FROM qo_datosum "
                        + " WHERE NIC=? LIMIT 1 ";

                java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
                pst.setString(1, nic);
                java.sql.ResultSet rs = conexion.Query(pst);
                if (rs.next()) {
                    info.setCliente(rs.getString("NOM_CLI") + " " + rs.getString("APE1_CLI") + " " + rs.getString("APE2_CLI"));
                    info.setDepartamento(rs.getString("DEPARTAMENTO"));
                    info.setLocalidad(rs.getString("LOCALIDAD"));
                    info.setMunicipio(rs.getString("MUNICIPIO"));
                    info.setNis(rs.getString("NIS_RAD"));
                    info.setDireccion(rs.getString("DIRECCION"));
                }

                Gson gson = new Gson();
                out.print(gson.toJson(info));

            } catch (SQLException e) {
                out.print("Error: " + e.getMessage());
            } catch (Exception e) {
                out.print("Error: " + e.getMessage());
            } finally {

                if (conexion != null) {
                    try {
                        conexion.Close();
                    } catch (SQLException e) {
                        out.print("Error: " + e.getMessage());
                    }
                }
            }

        }
    }

    private class Informacion {

        private String cliente;
        private String nis;
        private String direccion;
        private String municipio;
        private String localidad;
        private String departamento;

        public Informacion() {
            this.cliente = "";
            this.departamento = "";
            this.direccion = "";
            this.localidad = "";
            this.municipio = "";
            this.nis = "";
        }

        public String getCliente() {
            return cliente;
        }

        public void setCliente(String cliente) {
            this.cliente = cliente;
        }

        public String getNis() {
            return nis;
        }

        public void setNis(String nis) {
            this.nis = nis;
        }

        public String getDireccion() {
            return direccion;
        }

        public void setDireccion(String direccion) {
            this.direccion = direccion;
        }

        public String getMunicipio() {
            return municipio;
        }

        public void setMunicipio(String municipio) {
            this.municipio = municipio;
        }

        public String getLocalidad() {
            return localidad;
        }

        public void setLocalidad(String localidad) {
            this.localidad = localidad;
        }

        public String getDepartamento() {
            return departamento;
        }

        public void setDepartamento(String departamento) {
            this.departamento = departamento;
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
