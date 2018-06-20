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
@WebServlet(name = "SrvReporteGeneral", urlPatterns = {"/SrvReporteGeneral"})
public class SrvReporteGeneral extends HttpServlet {

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
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=\"reporte.xls\"");
        request.setCharacterEncoding("UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            
            String contratista = (String)request.getParameter("contratista");
            String camp = (String)request.getParameter("camp");
            String fecha_inicial = (String)request.getParameter("fecha_inicial");
            String fecha_final = (String)request.getParameter("fecha_final");
            
            db conexion = null;
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Reporte General</title>");
            out.println("</head>");

            try {

                conexion = new db();
                out.println("<body>");
                out.println("<h1>Reporte General Generacion OS</h1>");
                
                String sql = "SELECT c.NIC," +
                    "c.NUM_OS," +
                    "c.ID_CAMP AS NUM_CAMP," +
                    "camp.campdesc AS DESC_CAMP," +
                    "camp.campplan AS TIPO_PLAN," +
                    "COMENTARIO," +
                    "contnomb AS CONTRATA," +
                    "CONVERT(c.FECHA_CARGA,DATE) AS F_GEN," +
                    "MONTH(c.FECHA_CARGA) AS MES_CAMP," +
                    "qds.MUNICIPIO," +
                    "qds.LOCALIDAD," +
                    "qds.DEPARTAMENTO," +
                    "qds.COD_TAR," +
                    "r1.recunomb AS INGENIERO," +
                    "c.BRIGADA," +
                    "r2.recunomb AS NOMBRE_BRIGADA," +
                    "CONVERT(c.FECHA_CIERRE, DATE) AS FECHA_CIERRE," +
                    "MONTH(c.FECHA_CIERRE) AS MES_CIERRE," +
                    "c.ANOMALIA," +
                    "c.OBS_ACTA OBSERVACION_ANOMALIA," +
                    "c.ACCION_IR AS ACCION_IRREG," +
                    "c.ECDF AS ENERGIA," +
                    "c.ESTADO_HGI " +
                    " FROM camp_orden c" +
                    " INNER JOIN camp ON camp.campcodi = c.ID_CAMP" +
                    " INNER JOIN contratistas ON contratistas.contcodi = c.CONTRATISTA" +
                    " INNER JOIN recurso r1 ON r1.recucodi = c.INGENIERO" +
                    " INNER JOIN recurso r2 ON r2.recucodi = c.BRIGADA" +
                    " LEFT JOIN qo_ordenes qo ON qo.NUM_OS = c.NUM_OS" +
                    " LEFT JOIN qo_datosum qds ON qds.NIC = qo.NIC AND qds.NIS_RAD = qo.NIS_RAD AND qds.SEC_NIS = qo.SEC_NIS" +
                    " WHERE CONVERT(c.FECHA_CARGA,DATE) BETWEEN CONVERT(?,DATE) AND CONVERT(?,DATE)" +
                    " AND c.NUM_OS != ''";
                
                if (!contratista.equals("all")) {
                    
                    sql += " AND c.CONTRATISTA = '" + contratista + "'";
                }
                
                if (!camp.equals("all")) {
                    sql += " AND c.ID_CAMP = '" + camp + "'";
                }
                
                java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
                pst.setString(1, fecha_inicial);
                pst.setString(2, fecha_final);
                java.sql.ResultSet rs = conexion.Query(pst);
                
                out.println("<table>");
                out.println("<thead>");
                
                out.println("<tr style='background: gray'>");
                out.println("<th>NIC</th>");
                out.println("<th>NUM_OS</th>");
                out.println("<th>NUM_CAMP</th>");
                out.println("<th>DESC_CAMP</th>");
                out.println("<th>TIPO_PLAN</th>");
                out.println("<th>COMENTARIO_OS</th>");
                out.println("<th>CONTRATA</th>");
                out.println("<th>FECHA_GENERACION</th>");
                out.println("<th>MES_CAMP</th>");
                out.println("<th>MUNICIPIO</th>");
                out.println("<th>LOCALIDAD</th>");
                out.println("<th>DEPARTAMENTO</th>");
                out.println("<th>COD_TARIFA</th>");
                out.println("<th>INGENIERO</th>");
                out.println("<th>COD_BIRGADA</th>");
                out.println("<th>NOM_BRIGADA</th>");
                out.println("<th>FECHA_CIERRE</th>");
                out.println("<th>MES_CIERRE</th>");
                out.println("<th>ANOMALIA</th>");
                out.println("<th>OBS_ANOMALIA</th>");
                out.println("<th>ACCION_IRREG</th>");
                out.println("<th>ENERGIA</th>");
                out.println("<th>ESTADO_HGI</th>");

                
                out.println("</tr>");
                
                out.println("</thead>");
                out.println("<tbody>");
                int cont=0;
                String style1 = "<tr style=\"background: #E6E6E6;\" >";
                String style2 = "<tr>";
                while (rs.next()) {
                    
                    if (cont%2 == 0) {
                        out.println(style2);
                    }else {
                        out.println(style1);
                    }

                    out.println("<td>" + rs.getString("NIC") +  "</td>");
                    out.println("<td>" + rs.getString("NUM_OS") +  "</td>");
                    out.println("<td>" + rs.getString("NUM_CAMP") +  "</td>");
                    out.println("<td>" + rs.getString("DESC_CAMP") +  "</td>");
                    out.println("<td>" + rs.getString("TIPO_PLAN") +  "</td>");
                    out.println("<td>" + rs.getString("COMENTARIO") +  "</td>");
                    out.println("<td>" + rs.getString("CONTRATA") +  "</td>");
                    out.println("<td>" + rs.getString("F_GEN") +  "</td>");
                    out.println("<td>" + rs.getString("MES_CAMP") +  "</td>");
                    out.println("<td>" + rs.getString("MUNICIPIO") +  "</td>");
                    out.println("<td>" + rs.getString("LOCALIDAD") +  "</td>");
                    out.println("<td>" + rs.getString("DEPARTAMENTO") +  "</td>");
                    out.println("<td>" + rs.getString("COD_TAR") +  "</td>");
                    out.println("<td>" + rs.getString("INGENIERO") +  "</td>");
                    out.println("<td>" + rs.getString("BRIGADA") +  "</td>");
                    out.println("<td>" + rs.getString("NOMBRE_BRIGADA") +  "</td>");
                    out.println("<td>" + rs.getString("FECHA_CIERRE") +  "</td>");
                    out.println("<td>" + rs.getString("MES_CIERRE") +  "</td>");
                    out.println("<td>" + rs.getString("ANOMALIA") +  "</td>");
                    out.println("<td>" + rs.getString("OBSERVACION_ANOMALIA") +  "</td>");
                    out.println("<td>" + rs.getString("ACCION_IRREG") +  "</td>");
                    out.println("<td>" + rs.getString("ENERGIA") +  "</td>");
                    out.println("<td>" + rs.getString("ESTADO_HGI") +  "</td>");
                    
                    out.println("</tr>");
                    cont++;
                }
                out.println("</tbody>");
                
                out.println("</table>");
                
                out.println("</body>");

            } catch (SQLException ex) {
                Logger.getLogger(SrvReporteGeneral.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                if (conexion != null) {
                    try {
                        conexion.Close();
                    } catch (SQLException ex) {
                        Logger.getLogger(SrvReporteGeneral.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

            out.println("</html>");
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
