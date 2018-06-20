/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.are.censo.rest;

import com.are.censo.controlador.CtlLogin;
import com.are.censo.entidades.Login;
import com.are.censo.entidades.LoginResult;
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
@WebServlet(name = "RESTLogin", urlPatterns = {"/auth/login"})
public class RESTLogin extends HttpServlet {

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
            Login login = null;
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
                
                login = gson.fromJson(json, Login.class);
                
                if (login == null) {
                    throw new Exception("JSON no válido");
                }
                
                if (login.getToken().equals("")) {
                    throw new Exception("atributo token no válido");
                }
                
                if (login.getUser().equals("")) {
                    throw new Exception("atributo user no válido");
                }

                if (login.getPassword().equals("")) {
                    throw new Exception("atributo password no válido");
                }
                
                CtlLogin controlador = new CtlLogin(login);
                controlador.Login();
                LoginResult result = controlador.getResultLogin();
                
                out.print(gson.toJson(result));
                
            } catch (Exception ex) {
                Logger.getLogger(RESTLogin.class.getName()).log(Level.SEVERE, null, ex);
                response.sendError(500, "Error:" + ex.getMessage());
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
