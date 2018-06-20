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
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class SrvListAssign
 */
@WebServlet("/SrvRuta")
public class SrvRuta extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SrvRuta() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void ProcesarPeticion(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();

		String operacion = (String) request.getParameter("operacion");
		db conexion = null;
		try {
			if (operacion.equals("confirmar")) {
				String[] ordenes = request.getParameterValues("orden");
				int confirmadas = 0;
				if (ordenes.length > 0) {
					conexion = new db();
						for (int i = 0; i < ordenes.length; i++) {
							String sql = "update orders set num_show = ? where orden=?";
							java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
							pst.setInt(1, i+1);
							pst.setString(2, ordenes[i]);
							if (conexion.Update(pst) > 0 ) {
								confirmadas++;
							}
							
						}
						
						if (confirmadas == ordenes.length) {
							conexion.Commit();
							out.print("OK");
						} else {
							conexion.Rollback();
							throw new SQLException("Error al procesar la ruta");
						}

					} else {
						out.print("No se recibieron ordenes para confirmar la ruta");
					}
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			out.print("Error de conexion con el servidor: " + e.getMessage());
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
