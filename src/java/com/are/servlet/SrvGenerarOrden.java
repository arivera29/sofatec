/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.are.servlet;

import com.are.sofatec.db;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author aimerrivera
 */
@WebServlet(name = "SrvGenerarOrden", urlPatterns = {"/SrvGenerarOrden"})
public class SrvGenerarOrden extends HttpServlet {

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
            
            HttpSession sesion=request.getSession();
            if (sesion.getAttribute("usuario") == null) {
                out.print("La sesion ha expirado, debe iniciar sesión nuevamente");
                return;
            }
            
            String usuario = (String)sesion.getAttribute("usuario");
            
            
            String nic = (String)request.getParameter("nic");
            String nis = (String)request.getParameter("nis");
            String orden = (String)request.getParameter("orden");
            String comentario = (String)request.getParameter("comentario");
            String contratista = (String)request.getParameter("contratista");
            String inspector = (String)request.getParameter("inspector");
            String ingeniero = (String)request.getParameter("ingeniero");
            String brigada = (String)request.getParameter("brigada");
            String tipo_brigada = (String)request.getParameter("tipo_brigada");
            String camp = (String)request.getParameter("camp");
            String zona = (String)request.getParameter("zona");
            
            String irreg = (String)request.getParameter("irreg");
            String corriente1 = (String)request.getParameter("corriente1");
            String voltaje1 = (String)request.getParameter("voltaje1");
            String corriente2 = (String)request.getParameter("corriente2");
            String voltaje2 = (String)request.getParameter("voltaje2");
            
            
            db conexion = null;
            
            try {
                conexion = new db();
                
                if (!orden.equals("")) {
                    String sql = "SELECT NUM_OS FROM camp_orden WHERE NUM_OS =?";
                    java.sql.PreparedStatement pst0 = conexion.getConnection().prepareStatement(sql);
                    pst0.setString(1, orden);
                    java.sql.ResultSet rs1 = conexion.Query(pst0);
                    if (rs1.next()) {
                        throw new SQLException("Ya se encuentra registrada la orden de servic¡o");
                    }
                }
                
                String sql = "SELECT NIC FROM camp_orden WHERE NIC=? AND ID_CAMP=? ";
                java.sql.PreparedStatement pst0 = conexion.getConnection().prepareStatement(sql);
                pst0.setString(1, nic);
                pst0.setString(2, camp);
                java.sql.ResultSet rs1 = conexion.Query(pst0);
                if (rs1.next()) {
                    throw new SQLException("Ya se ha generado una orden de servicio para el nic en la misma campaña");
                }
                
                sql = "INSERT INTO camp_orden " +
                    "(" +
                    "NIC," +
                    "NUM_OS," +
                    "COMENTARIO," +
                    "CONTRATISTA," +
                    "INSPECTOR," +
                    "INGENIERO," +
                    "BRIGADA," +
                    "TIPO_BRIGADA," +
                    "ID_CAMP," +
                    "USUARIO_CARGA," +
                    "FECHA_CARGA," +
                    "ZONA," +
                    "NIS," +
                    "IRREG," +
                    "CORRIENTE1," +
                    "VOLTAJE1," +
                    "CORRIENTE2," +
                    "VOLTAJE2," +
                    "FECHA_GEN_OS )" +
                    " VALUES (" +
                    "?," +
                    "?," +
                    "?," +
                    "?," +
                    "?," +
                    "?," +
                    "?," +
                    "?," +
                    "?," +
                    "?," +
                    "SYSDATE()," +
                    "?," +
                    "?," +
                    "?," +
                    "?," +
                    "?," +
                    "?," +
                    "?," ;
                
                if (!orden.equals("")) {
                    sql += "SYSDATE())";
                }else {
                    sql += "NULL)";
                }
                    
                
                java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
                pst.setString(1, nic);
                pst.setString(2, orden);
                pst.setString(3, comentario);
                pst.setString(4, contratista);
                pst.setString(5, inspector);
                pst.setString(6, ingeniero);
                pst.setString(7, brigada);
                pst.setString(8, tipo_brigada);
                pst.setString(9, camp);
                pst.setString(10, usuario);
                pst.setString(11, zona);
                pst.setString(12, nis);
                pst.setString(13, irreg);
                pst.setString(14, corriente1);
                pst.setString(15, voltaje1);
                pst.setString(16, corriente2);
                pst.setString(17, voltaje2);
                
                if (conexion.Update(pst) > 0) {
                    conexion.Commit();
                    out.print("OK");
                }else {
                    out.print("Error al guardar el registro");
                }
                
                
            }catch (SQLException e) {
                out.print("Error: " + e.getMessage());
                
            }catch (Exception e) {
                out.print("Error: " + e.getMessage());
                
            }finally {
                
                if (conexion != null) {
                    try {
                        conexion.Close();
                    }catch(SQLException e) {
                        out.print("Error: " + e.getMessage());
                        
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
