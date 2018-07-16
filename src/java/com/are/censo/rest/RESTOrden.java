/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.are.censo.rest;

import com.are.censo.controlador.CtlOrden;
import com.are.censo.entidades.Foto;
import com.are.censo.entidades.Orden;
import com.are.censo.entidades.OrdenResult;
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
@WebServlet(name = "RESTOrden", urlPatterns = {"/os/generar"})
public class RESTOrden extends HttpServlet {

    private final static Logger LOGGER = Logger.getLogger(RESTOrden.class.getName());

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
        OrdenResult ordenResult = new OrdenResult();
        try (PrintWriter out = response.getWriter()) {
            LOGGER.log(Level.INFO, "[REQUEST_GENERATION_OS] -> Init");
            Gson gson = new Gson();
            Orden orden = null;
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
                LOGGER.log(Level.INFO, "[REQUEST_GENERATION_OS] -> Receive JSON");
                //LOGGER.log(Level.SEVERE, "JSON recibido: " + json);

                if (json.equals("")) {
                    LOGGER.log(Level.INFO, "[REQUEST_GENERATION_OS] -> Parameter JSON is empty");
                    throw new Exception("Parametro JSON vacio");
                }

                orden = gson.fromJson(json, Orden.class);

                if (orden == null) {
                    LOGGER.log(Level.INFO, "[REQUEST_GENERATION_OS] -> Struct JSON no valid!");
                    throw new Exception("JSON no válido");
                }

                if (orden.getToken().equals("")) {
                    LOGGER.log(Level.INFO, "[REQUEST_GENERATION_OS] -> [JSON] attr <token> no valid!");
                    throw new Exception("atributo token no válido");
                }

                if (orden.getId().equals("")) {
                    LOGGER.log(Level.INFO, "[REQUEST_GENERATION_OS] -> [JSON] attr <id> no valid!");
                    throw new Exception("atributo ID no válido");
                }
                if (orden.getNic().equals("")) {
                    LOGGER.log(Level.INFO, "[REQUEST_GENERATION_OS] -> [JSON] attr <nic> no valid!");
                    throw new Exception("atributo NIC no válido");
                }
                if (orden.getFecha().equals("")) {
                    LOGGER.log(Level.INFO, "[REQUEST_GENERATION_OS] -> [JSON] attr <fecha> no valid!");
                    throw new Exception("atributo FECHA no válido");
                }

                if (orden.getContrata().equals("")) {
                    LOGGER.log(Level.INFO, "[REQUEST_GENERATION_OS] -> [JSON] attr <contrata> no valid!");
                    throw new Exception("atributo CONTRATA no válido");
                }

                if (orden.getBrigada().equals("")) {
                    LOGGER.log(Level.INFO, "[REQUEST_GENERATION_OS] -> [JSON] attr <brigada> no valid!");
                    throw new Exception("atributo BRIGADA no válido");
                }
                if (orden.getInspector().equals("")) {
                    LOGGER.log(Level.INFO, "[REQUEST_GENERATION_OS] -> [JSON] attr <inspector> no valid!");
                    throw new Exception("atributo INSPECTOR no válido");
                }
                if (orden.getIngeniero().equals("")) {
                    LOGGER.log(Level.INFO, "[REQUEST_GENERATION_OS] -> [JSON] attr <ingeniero> no valid!");
                    throw new Exception("atributo INGENIERO no válido");
                }
                if (orden.getZona().equals("")) {
                    LOGGER.log(Level.INFO, "[REQUEST_GENERATION_OS] -> [JSON] attr <zona> no valid!");
                    throw new Exception("atributo ZONA no válido");
                }
                if (orden.getObservacion().equals("")) {
                    LOGGER.log(Level.INFO, "[REQUEST_GENERATION_OS] -> [JSON] attr <observacion> no valid!");
                    throw new Exception("atributo OBSERVACION no válido");
                }
                if (orden.getImei().equals("")) {
                    LOGGER.log(Level.INFO, "[REQUEST_GENERATION_OS] -> [JSON] attr <imei> no valid!");
                    throw new Exception("atributo IMEI no válido");
                }
                if (orden.getUsuario().equals("")) {
                    LOGGER.log(Level.INFO, "[REQUEST_GENERATION_OS] -> [JSON] attr <usuario> no valid!");
                    throw new Exception("atributo USUARIO no válido");
                }

                if (orden.getFotos() != null) {
                    if (orden.getFotos().size() > 0) {
                        LOGGER.log(Level.INFO, "[REQUEST_GENERATION_OS] -> [JSON] attr <fotos> item count {0}", orden.getFotos().size());
                        int contador = 0;
                        for (Foto foto : orden.getFotos()) {
                            contador++;
                            if (foto.getFilename().equals("")) {
                                LOGGER.log(Level.INFO, "[REQUEST_GENERATION_OS] -> [JSON] attr <fotos> item {0} attr <filename> no valid!!", contador);
                                throw new Exception("atributo FILENAME del listado de fotos no válido. Item " + contador);
                            }
                            if (foto.getStrBase64().equals("")) {
                                LOGGER.log(Level.INFO, "[REQUEST_GENERATION_OS] -> [JSON] attr <fotos> item {0} attr <strbase64> no valid!!", contador);
                                throw new Exception("atributo STRBASE64 del listado de fotos no válido. Item " + contador);
                            }
                        }
                    } else {
                        LOGGER.log(Level.INFO, "[REQUEST_GENERATION_OS] -> [JSON] attr <fotos> empty");
                    }

                } else {
                    LOGGER.log(Level.INFO, "[REQUEST_GENERATION_OS] -> [JSON] attr <fotos> empty");
                    orden.setFotos(new ArrayList<Foto>());
                }
                LOGGER.log(Level.INFO, "[REQUEST_GENERATION_OS] -> [JSON] Valid OK");
                LOGGER.log(Level.INFO, "[REQUEST_GENERATION_OS] -> [JSON] ID .............. {0}", orden.getId());
                LOGGER.log(Level.INFO, "[REQUEST_GENERATION_OS] -> [JSON] NIC ............. {0}", orden.getNic());
                LOGGER.log(Level.INFO, "[REQUEST_GENERATION_OS] -> [JSON] FECHA ........... {0}", orden.getFecha());
                LOGGER.log(Level.INFO, "[REQUEST_GENERATION_OS] -> [JSON] CONTRATA ........ {0}", orden.getContrata());
                LOGGER.log(Level.INFO, "[REQUEST_GENERATION_OS] -> [JSON] BRIGADA ......... {0}", orden.getBrigada());
                LOGGER.log(Level.INFO, "[REQUEST_GENERATION_OS] -> [JSON] INSPECTOR ....... {0}", orden.getInspector());
                LOGGER.log(Level.INFO, "[REQUEST_GENERATION_OS] -> [JSON] INGENIERO ....... {0}", orden.getIngeniero());
                LOGGER.log(Level.INFO, "[REQUEST_GENERATION_OS] -> [JSON] ZONA ............ {0}", orden.getZona());
                LOGGER.log(Level.INFO, "[REQUEST_GENERATION_OS] -> [JSON] OBSERVACION ..... {0}", orden.getObservacion());
                LOGGER.log(Level.INFO, "[REQUEST_GENERATION_OS] -> [JSON] IMEI ............ {0}", orden.getImei());
                LOGGER.log(Level.INFO, "[REQUEST_GENERATION_OS] -> [JSON] USUARIO ......... {0}", orden.getUsuario());
                LOGGER.log(Level.INFO, "[REQUEST_GENERATION_OS] -> [JSON] CNT. FOTOS ...... {0}", orden.getFotos().size());

                LOGGER.log(Level.INFO, "[REQUEST_GENERATION_OS] -> [VALIDATING] Validating repeat NIC and USER in database.  NIC {0} by user {1}", new Object[]{orden.getNic(), orden.getUsuario()});

                conexion = new db();
                String sql = "SELECT NIC FROM camp_orden "
                        + " WHERE NIC=? "
//                        + " AND USUARIO_APP=? "
                        + " AND DATE(FECHA_CARGA) = DATE(CURRENT_DATE()) ";
                java.sql.PreparedStatement pst0 = conexion.getConnection().prepareStatement(sql);
                pst0.setString(1, orden.getNic());
//                pst0.setString(2, orden.getUsuario());
                java.sql.ResultSet rs1 = conexion.Query(pst0);
                if (rs1.next()) {
                    LOGGER.log(Level.INFO, "[REQUEST_GENERATION_OS] -> [VALIDATING] OS repeat today on Server for NIC {0}", orden.getNic());
                    throw new Exception("Ya se ha creado una solicitud de generacion de orden por para el NIC " + orden.getNic() + " el dia de hoy");
                }

                CtlOrden controlador = new CtlOrden(orden, directorio);
                controlador.setDirImages(directorio);
                LOGGER.log(Level.INFO, "[REQUEST_GENERATION_OS] -> [ADD] Creating OS in Server for NIC {0} by user {1}", new Object[]{orden.getNic(), orden.getUsuario()});
                controlador.Add();

                ordenResult = controlador.getOrdenResult();

                if (ordenResult.isSync()) {
                    LOGGER.log(Level.INFO, "[REQUEST_GENERATION_OS] -> [ADD] OS Created in Server for NIC {0} by user {1}", new Object[]{orden.getNic(), orden.getUsuario()});
                } else {
                    LOGGER.log(Level.INFO, "[REQUEST_GENERATION_OS] -> [ADD] Error creating OS in Server for NIC {0} by user {1}. ERRROR->{2}", new Object[]{orden.getNic(), orden.getUsuario(), ordenResult.getMsgError()});
                }

            } catch (SQLException ex) {
                Logger.getLogger(RESTOrden.class.getName()).log(Level.SEVERE, null, ex);
                ordenResult.setError(true);
                ordenResult.setMsgError(ex.getMessage());
                ordenResult.setSync(false);

            } catch (Exception ex) {
                Logger.getLogger(RESTOrden.class.getName()).log(Level.SEVERE, null, ex);
                ordenResult.setError(true);
                ordenResult.setMsgError(ex.getMessage());
                ordenResult.setSync(false);

            } finally {
                if (conexion != null) {
                    try {
                        conexion.Close();
                    } catch (SQLException ex) {
                        Logger.getLogger(RESTOrden.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

            out.print(gson.toJson(ordenResult));

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
