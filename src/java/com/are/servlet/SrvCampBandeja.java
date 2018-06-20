/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.are.servlet;

import com.are.entidades.CampBandeja;
import com.are.entidades.CampBandejaContrata;
import com.are.manejadores.ManejadorCampBandeja;
import com.are.sofatec.ManejadorUsuarios;
import com.are.sofatec.db;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author aimer
 */
@WebServlet(name = "SrvCampBandeja", urlPatterns = {"/SrvCampBandeja"})
public class SrvCampBandeja extends HttpServlet {

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
            String operacion = (String) request.getParameter("operacion");
        db conexion = null;

        /* Inicio operacion Agregar departamento */
        try {

            if (operacion.equals("add")) { // Agregar bandeja

                String usuario = (String) request.getParameter("usuario");
                

                conexion = new db();
                CampBandeja bandeja = new CampBandeja(0,usuario,1);

                ManejadorCampBandeja manejador = new ManejadorCampBandeja(conexion);

                if (manejador.Add(bandeja)) {
                    out.print("OK");
                } else {
                    out.print("Error al procesar la solicitud");
                }

            }

            if (operacion.equals("update")) { // Modificar bandeja

                String key = (String) request.getParameter("key");
                String usuario = (String) request.getParameter("usuario");
                String estado = (String) request.getParameter("estado");

                conexion = new db();
                CampBandeja bandeja = new CampBandeja(Integer.parseInt(key),usuario,Integer.parseInt(estado));

                ManejadorCampBandeja manejador = new ManejadorCampBandeja(conexion);

                if (manejador.Find(Integer.parseInt(key))) {
                    if (manejador.Update(bandeja, Integer.parseInt(key))) {
                        out.print("OK");
                    } else {
                        throw new Exception("Error al procesar la solicitud");
                    }
                } else {
                    throw new Exception("Bandeja no se encuentra registrada. "+ key);
                }
            }

            if (operacion.equals("remove")) { // Eliminar bandeja
                String key = (String) request.getParameter("key");
                conexion = new db();

                ManejadorCampBandeja manejador = new ManejadorCampBandeja(conexion);

                if (manejador.Find(Integer.parseInt(key))) {
                    if (manejador.Remove(Integer.parseInt(key))) {
                        out.print("OK");
                    } else {
                        throw new Exception("Error al procesar la solicitud");
                    }
                } else {
                    throw new Exception("Bandeja no se encuentra registrado. " + key);
                }
            }
            
            if (operacion.equals("estado_todos")) {  // Cambiar estado a todas las bandejas
                String estado = (String) request.getParameter("estado");
                conexion = new db();

                ManejadorCampBandeja manejador = new ManejadorCampBandeja(conexion);

                    if (manejador.CambiarEstadoTodos(Integer.parseInt(estado))) {
                        out.print("OK");
                    } else {
                        throw new Exception("Error al procesar la solicitud");
                    }
            }
            
            if (operacion.equals("estado")) { // Cambiar estado a una bandeja específica
                String estado = (String) request.getParameter("estado");
                String key = (String)request.getParameter("key");
                conexion = new db();

                ManejadorCampBandeja manejador = new ManejadorCampBandeja(conexion);

                    if (manejador.CambiarEstado(Integer.parseInt(key),Integer.parseInt(estado))) {
                        out.print("OK");
                    } else {
                        throw new Exception("Error al procesar la solicitud");
                    }
            }
            

            if (operacion.equals("list")) { // Listado de Bandejas
                conexion = new db();
                ManejadorCampBandeja manejador = new ManejadorCampBandeja(conexion);
                ArrayList<CampBandeja> lista = manejador.List();
                ManejadorUsuarios manejador2 = new ManejadorUsuarios(conexion);
                

                out.println("<h2>Listado Bandejas</h2>");
                if (lista.size() > 0) {
                    out.println("<table>");
                    out.println("<tr>");
                    out.println("<th>ID</th>");
                    out.println("<th>USUARIO</th>");
                    out.println("<th>NOMBRE</th>");
                    out.println("<th>ESTADO</th>");
                    out.println("<th></th>");
                    out.println("</tr>");
                    int cont = 1;
                    for (CampBandeja bandeja : lista) { // existen registros
                        String id_tr = "fila_" + bandeja.getId();
                        if (cont % 2 == 0) {
                            out.println(String.format("<tr class=\"%s\" id=\"%s\">","odd",id_tr));
                        }else{
                            out.println(String.format("<tr id=\"%s\">",id_tr));
                        }
                        
                        out.println("<td>" + bandeja.getId() + "</td>");
                        out.println("<td>" + bandeja.getUsuario() + "</td>");
                        String nombre = "";
                        if (manejador2.find(bandeja.getUsuario())) {
                            nombre = manejador2.getUsuario().getNombre();
                        }
                        out.println("<td>" + nombre + "</td>");
                        out.println("<td>"
                                + (bandeja.getEstado() == 1 ? "<img src=\"images/online.png\">"
                                : "<img src=\"images/offline.png\">")
                                + "</td>");
                        
                        out.println("<td>");
                        
                        String url1 = "<a href=\"javascript:Activar("+ bandeja.getId() + ")\">Activar</a>";
                        String url2 = "<a href=\"javascript:Inactivar("+ bandeja.getId() + ")\">Inactivar</a>";
                        
                        if (bandeja.getEstado() == 1) {
                            out.print(url2);
                        }else {
                            out.print(url1);
                        }
                        String url3 = "<a href=\"javascript:Eliminar("+ bandeja.getId() + ")\">Eliminar</a>";
                        String url4 = "<a href=\"bandeja_contrata.jsp?id="+ bandeja.getId() + "\">Contratas</a>";
                        out.print(url3);
                        out.print(url4);
                        out.println("</td>");
                        out.println("</tr>");
                        cont++;
                    }

                    out.println("</table>");
                } else {
                    out.println("<strong>No hay registros</strong>");

                }

            }
            
            if (operacion.equals("list_contrata")) { // Listado de Bandejas
                conexion = new db();
                String bandeja = (String)request.getParameter("bandeja");
                ManejadorCampBandeja manejador = new ManejadorCampBandeja(conexion);
                ArrayList<CampBandejaContrata> lista = manejador.ListContratista(Integer.parseInt(bandeja));

                out.println("<h2>Listado Contratistas asociados a la bandeja</h2>");
                if (lista.size() > 0) {
                    out.println("<table>");
                    out.println("<tr>");
                    out.println("<th>CODIGO</th>");
                    out.println("<th>NOMBRE</th>");
                    out.println("<th></th>");
                    out.println("</tr>");
                    int cont = 1;
                    for (CampBandejaContrata contratista : lista) { // existen registros
                        String id_tr = "fila_" + contratista.getCodigo();
                        if (cont % 2 == 0) {
                            out.println(String.format("<tr class=\"%s\" id=\"%s\">","odd",id_tr));
                        }else{
                            out.println(String.format("<tr id=\"%s\">",id_tr));
                        }
                        
                        out.println("<td>" + contratista.getCodigo() + "</td>");
                        out.println("<td>" + contratista.getNombre() + "</td>");
                        
                        out.println("<td>");
                        
                        String url = String.format("<a href=\"javascript:Eliminar(%s,%s)\">Eliminar</a>",contratista.getId(),bandeja);
                        out.print(url);
                        out.println("</td>");
                        out.println("</tr>");
                        cont++;
                    }

                    out.println("</table>");
                } else {
                    out.println("<strong>No hay registros</strong>");

                }

            }
            
            if (operacion.equals("add_contrata")) { // Agregar bandeja

                String contrata = (String) request.getParameter("contrata");
                String bandeja = (String) request.getParameter("bandeja"); 
                

                conexion = new db();

                ManejadorCampBandeja manejador = new ManejadorCampBandeja(conexion);

                if (manejador.AddContrata(Integer.parseInt(bandeja),Integer.parseInt(contrata))) {
                    out.print("OK");
                } else {
                    out.print("Error al procesar la solicitud");
                }

            }
            
            if (operacion.equals("remove_contrata")) { // Agregar bandeja

                String key = (String) request.getParameter("key");
                conexion = new db();

                ManejadorCampBandeja manejador = new ManejadorCampBandeja(conexion);

                if (manejador.RemoveContrata(Integer.parseInt(key))) {
                    out.print("OK");
                } else {
                    out.print("Error al procesar la solicitud");
                }

            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            if (e.getErrorCode() == 1062) {  // Duplicidad de indice
                out.print("Error de duplicidad del registro");
            }else {
                out.print("Error BD: " + e.getMessage() + ". Err Nro: " + e.getErrorCode());
            }
            
        } catch (Exception e) {
            // TODO Auto-generated catch block
            out.print("Error:  " + e.getMessage());
        } finally {
            if (conexion != null) {
                try {
                    conexion.Close();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    out.print(e.getMessage());
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
