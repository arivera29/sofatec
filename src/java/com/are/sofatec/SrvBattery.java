package com.are.sofatec;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class SrvGps
 */
@WebServlet("/SrvBattery")
public class SrvBattery extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SrvBattery() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    protected void ProcesarPeticion(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		String imei = (String)request.getParameter("imei");
		String status = (String)request.getParameter("status");
		
		
		db conexion =  null;
		
		try {
			conexion = new db();
			String sql = "insert into battery (imei,status,fecha) values (?,?,sysdate())";
			java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
			pst.setString(1, imei);
			pst.setString(2, status);
			
			if (conexion.Update(pst) > 0) {
				conexion.Commit();
				out.print("OK");
			}else {
				out.print("Error al registrar status de bateria");
			}
			
			
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			out.print("Error de conexion con el servidor: "
					+ e.getMessage());
		} catch (NumberFormatException e) {
			out.println("Error.  Formato de numero no válido. " + e.getMessage());
			
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
