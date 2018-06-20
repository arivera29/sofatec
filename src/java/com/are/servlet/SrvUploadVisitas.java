/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.are.servlet;

import com.are.entidades.Visita;
import com.are.manejadores.ManejadorRecurso;
import com.are.manejadores.ManejadorVisita;
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
import javax.servlet.http.HttpSession;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 *
 * @author aimer
 */
@WebServlet(name = "SrvUploadVisitas", urlPatterns = {"/SrvUploadVisitas"})
public class SrvUploadVisitas extends HttpServlet {

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

        HttpSession sesion = request.getSession();
        if (sesion.getAttribute("usuario") == null) {
            throw new ServletException("Sesion ha caducado");
        }

        String usuario = (String) sesion.getAttribute("usuario");

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
                        if (headers.length != 9) { // estan las columnas OK.
                            throw new IOException("Archivo no cargado.  Numero de columnas no coinciden con la estructura (col=9). Columnas archivo " + headers.length);
                        }
                        conexion = new db();
                        ManejadorVisita manejador = new ManejadorVisita(conexion);
                        ManejadorRecurso manejador2 = new ManejadorRecurso(conexion);
                        
                        cont = 0;
                        rowCount = 0;
                        while (reader.readRecord()) {
                            rowCount++;
                            try {
                                
                                String brigada = reader.get(8);
                                if (brigada.equals("-1")) {
                                    throw new SQLException("Brigada codigo " + brigada + " no está permitida para asignar visitas");
                                }
                                if (!manejador2.Find(brigada)) {
                                    throw new SQLException("Brigada codigo " + brigada + " no se encuentra registrada");
                                }
                                
                                
                                
                                Visita visita = new Visita();
                                visita.setTipo(Integer.parseInt(reader.get(0)));
                                visita.setNic(reader.get(1));
                                visita.setObservacion(reader.get(2));
                                visita.setDepartamento(reader.get(3));
                                visita.setMunicipio(reader.get(4));
                                visita.setDireccion(reader.get(5));
                                visita.setBarrio(reader.get(6));
                                visita.setCliente(reader.get(7));
                                visita.setBrigada(reader.get(8));
                                visita.setUsuario(usuario);

                                if (manejador.Add(visita, false)) {
                                    cont++;
                                }

                            } catch (SQLException ex) {
                                Logger.getLogger(SrvUploadVisitas.class.getName()).log(Level.SEVERE, null, ex);
                                error.add(ex.getMessage());
                            }
                        }
                        
                        if (rowCount == cont) {
                            conexion.Commit();
                        }else {
                            conexion.Rollback();
                            //error.add("La cantidad de registros del archivo es diferente a la cantidad de registros cargados a la base de datos");
                        }

                        archivo_server.delete();  // Eliminamos el archivo del servidor

                    }

                } catch (SQLException ex) {
                    Logger.getLogger(SrvUploadVisitas.class.getName()).log(Level.SEVERE, null, ex);
                    error.add(ex.getMessage());

                } catch (Exception ex) {
                    Logger.getLogger(SrvUploadVisitas.class.getName()).log(Level.SEVERE, null, ex);
                    error.add(ex.getMessage());
                } finally {
                    if (conexion != null) {
                        try {
                            conexion.Close();
                        } catch (SQLException ex) {
                            Logger.getLogger(SrvUploadVisitas.class.getName()).log(Level.SEVERE, null, ex);
                            error.add(ex.getMessage());
                        }
                    }
                }
            }

        } catch (FileUploadException ex) {
            Logger.getLogger(SrvUploadVisitas.class.getName()).log(Level.SEVERE, null, ex);
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
