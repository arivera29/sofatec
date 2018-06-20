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
 * Servlet implementation class SrvCordenadasCliente
 */
@WebServlet("/SrvCoordenadasCliente")
public class SrvCoordenadasCliente extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SrvCoordenadasCliente() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    protected void ProcesarPeticion(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		String nic = (String)request.getParameter("nic");
		String imei = (String)request.getParameter("imei");
		
		db conexion = null;
		
		try {
			conexion = new db();
			
			Equipos equipo = new Equipos(conexion);
			if (equipo.Find(imei)) {
				if (equipo.getActivo() == 1) {
					String sql = "select longitud,latitud from georeferencia where nic=?";
					java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
					pst.setString(1, nic);
					ResultSet rs = conexion.Query(pst);
					if (rs.next()) {
						out.print("OK;" + rs.getString("latitud") + ";" + rs.getString("longitud"));
					}else {
						out.println("ERROR;Nic no tiene coordenadas definidas. " + nic);
					}
				
				}else {
					out.println("ERROR;Equipo no se encuentra activo");
				}
				
			}else {
				out.println("ERROR;Equipo no se encuentra registrado");
			}
			
			
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
