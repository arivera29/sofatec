/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.are.servlet;

import com.are.entidades.Camp;
import com.are.manejadores.ManejadorCamp;
import com.are.sofatec.db;
import com.csvreader.CsvReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 *
 * @author aimer
 */
@WebServlet(name = "SrvUploadOrdenesCamp", urlPatterns = {"/SrvUploadOrdenesCamp"})
public class SrvUploadOrdenesCamp extends HttpServlet {

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

        List<String> error = new ArrayList<String>();
        FileItem file = null;
        int rowCount = 0;
        int cont = 0;
        db conexion = null;

        ServletContext servletContext = getServletContext();
        UUID idFilename = UUID.randomUUID();
        String nombreReal = idFilename + ".txt";
        String filename = servletContext.getRealPath("/upload") + File.separator + nombreReal;
        FileItemFactory file_factory = new DiskFileItemFactory();
        ServletFileUpload servlet_up = new ServletFileUpload(file_factory);
        try {
            List items = servlet_up.parseRequest(request);

            Iterator iter = items.iterator();

            while (iter.hasNext()) {
                FileItem item = (FileItem) iter.next();
                if (!item.isFormField()) {
                    /*cual sera la ruta al archivo en el servidor*/
                    file = item;
                }

            }

            if (file != null) {  // Si lo que se envió es un archivo
                try {

                    File archivo_server = new File(filename);
                    file.write(archivo_server);
                    if (archivo_server.exists()) {
                        CsvReader reader = new CsvReader(filename);
                        reader.setDelimiter('\t'); // tabulador
                        reader.readHeaders();
                        String[] headers = reader.getHeaders();
                        if (headers.length != 18) { // estan las columnas OK.
                            throw new IOException("Archivo no cargado.  Numero de columnas no coinciden con la estructura (col=18). Columnas archivo " + headers.length);
                        }
                        conexion = new db();

                        cont = 0;
                        rowCount = 0;
                        while (reader.readRecord()) {
                            rowCount++;

                            try {

                                String query = "INSERT INTO camp_orden( "
                                        + "NIC, NUM_OS, COMENTARIO, CONTRATISTA, INSPECTOR, INGENIERO, "
                                        + "BRIGADA, TIPO_BRIGADA, ID_CAMP, USUARIO_CARGA, FECHA_CARGA, ZONA, NIS, FECHA_CIERRE, ANOMALIA, "
                                        + "OBS_ACTA, ACCION_IR, FR, F_IRREG, ECDF, UPDATE_WS, ESTADO_HGI, CONFIRMADA, FECHA_CONFIRM, "
                                        + "FECHA_GEN_OS, USER_CONFIRM, IMEI_APP, ID_APP, LATITUD_APP, LONGITUD_APP, IRREG, "
                                        + "CORRIENTE1, CORRIENTE2, VOLTAJE1, VOLTAJE2, ID_VISITA, ESTADO, USUARIO_APP "
                                        + ") VALUES ( "
                                        + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NULL, 'NA', NULL, '', '', "
                                        + "NULL, 0, 0, '', 0, NULL, NULL, '', '', '', '0.00', '0.00', '-1', '0', '0', '0', '0', 0, 0, '');";

                                java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(query);
                                pst.setString(1, reader.get(0));
                                pst.setString(2, reader.get(2));
                                pst.setString(3, reader.get(3));
                                pst.setString(4, reader.get(9));
                                pst.setString(5, reader.get(11));
                                pst.setString(6, reader.get(10));
                                pst.setString(7, reader.get(12));
                                pst.setString(8, reader.get(13));
                                pst.setString(9, reader.get(14));
                                pst.setString(10, reader.get(16));
                                pst.setString(11, reader.get(17)); // fecha
                                pst.setString(12, reader.get(15));
                                pst.setString(13, reader.get(1));
                                
                                if (conexion.Update(pst) > 0) {
                                    cont++;
                                    conexion.Commit();
                                }

                            } catch (SQLException ex) {
                                Logger.getLogger(SrvUploadOrdenesCamp.class.getName()).log(Level.SEVERE, null, ex);
                                error.add(ex.getMessage());
                            }
                        }

                        archivo_server.delete();  // Eliminamos el archivo del servidor

                    }

                } catch (SQLException ex) {
                    Logger.getLogger(SrvUploadOrdenesCamp.class.getName()).log(Level.SEVERE, null, ex);
                    error.add(ex.getMessage());

                } catch (Exception ex) {
                    Logger.getLogger(SrvUploadOrdenesCamp.class.getName()).log(Level.SEVERE, null, ex);
                    error.add(ex.getMessage());
                } finally {
                    if (conexion != null) {
                        try {
                            conexion.Close();
                        } catch (SQLException ex) {
                            Logger.getLogger(SrvUploadOrdenesCamp.class.getName()).log(Level.SEVERE, null, ex);
                            error.add(ex.getMessage());
                        }
                    }
                }
            }

        } catch (FileUploadException ex) {
            Logger.getLogger(SrvUploadOrdenesCamp.class.getName()).log(Level.SEVERE, null, ex);
            error.add(ex.getMessage());
        }

        try (PrintWriter out = response.getWriter()) {

            out.println("</br><img src='images/ok.png'>Total registros del archivo:" + rowCount + ", registros procesados: " + cont);

            if (error.size() > 0) {
                out.println("</br>");
                for (String mensaje : error) {
                    out.println("</br><img src='images/alerta.png'> " + mensaje);
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
