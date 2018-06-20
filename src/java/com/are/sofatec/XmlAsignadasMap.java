package com.are.sofatec;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class XmlOrders
 */
@WebServlet("/XmlAsignadasMap")
public class XmlAsignadasMap extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public XmlAsignadasMap() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    protected void ProcesarPeticion(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
    	
    	response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<?xml version=\"1.0\" ?>");
        String cedula = (String)request.getParameter("cedula");
        
        db conexion = null;
        
        try {
        	
        	conexion = new db();
        	String sql ="select orders.orden,clientes.latitud,clientes.longitud " +
        			"from orders,clientes " +
        			"where recurso=? and orders.nic = clientes.nic and orders.estado=2 " ;
        			
 
        	
        	java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
			pst.setString(1, cedula);
			ResultSet rs = conexion.Query(pst);
			out.println("<coordenadas>");
			while (rs.next()) {
	            out.println("<coordenada>");
	            out.println("<latitud>" + rs.getString("latitud") + "</latitud>");
	            out.println("<longitud>" + rs.getString("longitud") + "</longitud>");
	            out.println("</coordenada>");
			}
           
            out.println("</coordenadas>");
            
        	
        	
        } catch (SQLException e) {
			// TODO Auto-generated catch block
			out.print("Error de conexion con el servidor: "
					+ e.getMessage());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			out.print("Error:  "
					+ e.getMessage());
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

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		this.ProcesarPeticion(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		this.ProcesarPeticion(request, response);
	}

}
