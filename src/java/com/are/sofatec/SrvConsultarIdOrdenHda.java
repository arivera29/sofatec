package com.are.sofatec;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Servlet implementation class SrvGps
 */
@WebServlet("/SrvConsultarIdOrdenHda")
public class SrvConsultarIdOrdenHda extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SrvConsultarIdOrdenHda() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    protected void ProcesarPeticion(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		//response.setContentType("text/html;charset=UTF-8");
    	request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		
		
 
		db conexion =  null;
		
		String orden = request.getParameter("orden");
		String idActa = request.getParameter("id");
		String fecha = request.getParameter("fecha");
		
		
		try {
			
			conexion = new db();
			
			String sql = "UPDATE qo_ordenes SET ID_HDA=?, FECHA_CIERRE=? WHERE NUM_OS=?";
			java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
			pst.setString(1, idActa);
			pst.setString(2, fecha);
			pst.setString(3, orden);
			conexion.Update(pst);
			conexion.Commit();
			
						
			
		}catch (SQLException e) {
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
	
	private static String asociarOrdenesServicio(java.lang.String sarta) {
        wstest.ServicioX0020WebX0020SistemaX0020Comercial service = new wstest.ServicioX0020WebX0020SistemaX0020Comercial();
        wstest.ServicioX0020WebX0020SistemaX0020ComercialSoap port = service.getServicioX0020WebX0020SistemaX0020ComercialSoap();
        return port.asociarOrdenesServicio(sarta);
    }

}
