/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.are.censo.rest;

import com.are.censo.controlador.CtlCPassword;
import com.are.censo.entidades.CPassword;
import com.are.censo.entidades.CPasswordResult;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
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
@WebServlet(name = "RESTCPassword", urlPatterns = {"/auth/changepassword"})
public class RESTCPassword extends HttpServlet {

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
        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
            CPassword changepassword = null;
            CPasswordResult result = new CPasswordResult();
            String json = "";
            try {

                StringBuilder sb = new StringBuilder();
                BufferedReader br = request.getReader();
                String str = "";

                while ((str = br.readLine()) != null) {
                    sb.append(str);
                }

                json = sb.toString();

                if (json.equals("")) {
                    throw new Exception("Parametro JSON vacio");
                }
                
                changepassword = gson.fromJson(json, CPassword.class);
                
                if (changepassword == null) {
                    throw new Exception("JSON no válido");
                }
                
                if (changepassword.getToken().equals("")) {
                    throw new Exception("atributo token no válido");
                }
                
                if (changepassword.getUser().equals("")) {
                    throw new Exception("atributo user no válido");
                }

                if (changepassword.getOld_password().equals("")) {
                    throw new Exception("atributo old_password no válido");
                }
                
                if (changepassword.getNew_password().equals("")) {
                    throw new Exception("atributo new_password no válido");
                }
                
                if (changepassword.getOld_password().equals(changepassword.getNew_password())) {
                    throw new Exception("atributo old_password debe ser diferente new_password");
                }
                
                CtlCPassword controlador = new CtlCPassword(changepassword);
                controlador.ChangePassword();
                
                result = controlador.getChangepassword();
                
            } catch (Exception ex) {
                Logger.getLogger(RESTCPassword.class.getName()).log(Level.SEVERE, null, ex);
                result.setError(true);
                result.setMsgError(ex.getMessage());
            }
            
            out.print(gson.toJson(result));
   
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
