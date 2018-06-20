package com.are.sofatec;


import java.io.BufferedReader;
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


/**
 * Servlet implementation class SrvGps
 */
@WebServlet("/SrvActualizarOrdenHgi")
public class SrvActualizarIdOrdenHgi extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SrvActualizarIdOrdenHgi() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    protected void ProcesarPeticion(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		//response.setContentType("text/html;charset=UTF-8");
    	request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		
		
 
		db conexion =  null;
		String respuesta = "";
		InputStream in = null;
		
		try {
			
			conexion = new db();
			
			String sql = "SELECT NUM_OS, ID_HDA FROM qo_ordenes WHERE ID_HDA = '-1' AND EST_HDA=0";
			
			ResultSet rs = conexion.Query(sql);
			while (rs.next()) {
			
				String url = "http://52.11.89.250/HGI2/consultarIdActa.aspx?numero=" + rs.getString("NUM_OS");
				out.println("Url: " + url);
				ConectorHttp con = new ConectorHttp();
				in = con.MetodoGET("", url);
				
				BufferedReader reader = new BufferedReader(new InputStreamReader(in,"utf-8"));
				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line);
				}
				
				out.println("Respuesta:" +sb.toString());
			
			
			
			} // fin While Recordset
			
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
