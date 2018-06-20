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
 * Servlet implementation class SrvListAssign
 */
@WebServlet("/SrvCambioLote")
public class SrvCambioLote extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SrvCambioLote() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void ProcesarPeticion(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();

		String lote = (String) request.getParameter("lote");
		String nuevo_lote = (String)request.getParameter("nuevo_lote");
		
		String[] ordenes = request.getParameterValues("ordenes");
		
		

		db conexion = null;
		try {

			if (ordenes == null) {
				throw new Exception("Error al recibir parametro ordenes");
			}
			if (ordenes.length == 0) {
				throw new Exception("Error indice parametro ordenes");
			}
			
			conexion = new db();
			int cont=0;
			
			for (int i=0 ; i < ordenes.length; i++) {
			
			
								
				String sql = "UPDATE QO_ORDENES SET " +
						"NUM_LOTE=? WHERE NUM_LOTE=? AND NUM_OS =?";
				java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
				pst.setString(1, nuevo_lote);
				pst.setString(2, lote);
				pst.setString(3, ordenes[i]);
				
				if (conexion.Update(pst)> 0) {
					cont++;
				}
			
			
			}
			
			
			if (cont == ordenes.length) {
				conexion.Commit();
				out.print("OK");
			}else {
				
				out.print("Error al actualizar el registro, total ordenes: " + ordenes.length + " Lote: " + lote + " Nuevo Lote: " + nuevo_lote);
			}
			
			
			

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			out.print(e.getMessage());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			out.print(e.getMessage());
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
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		this.ProcesarPeticion(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		this.ProcesarPeticion(request, response);
	}

}
