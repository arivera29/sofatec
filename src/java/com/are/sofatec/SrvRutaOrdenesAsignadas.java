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
 * Servlet implementation class SrvRutaOrdenesAsignadas
 */
@WebServlet("/SrvRutaOrdenesAsignadas")
public class SrvRutaOrdenesAsignadas extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SrvRutaOrdenesAsignadas() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void ProcesarPeticion(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=ISO-8859-1");
		PrintWriter out = response.getWriter();

		db conexion = null;
		try {
			conexion = new db();
			String start_latitud = "11.00791332927005";
			String start_longitud = "-74.78941372958225";
			String recurso = (String) request.getParameter("recurso");
			int orden = 1;

			String sql = "update orders set num_show =0 where recurso=? and estado=2";
			java.sql.PreparedStatement pst = conexion.getConnection()
					.prepareStatement(sql);
			pst.setString(1, recurso);
			conexion.Update(pst);
			conexion.Commit();
			boolean seguir = true;
			
			while (seguir) {
				sql = "SELECT orders.orden,latitud,longitud,(acos(sin(radians(?)) * sin(radians(latitud)) + "
						+ "cos(radians(?)) * cos(radians(latitud)) * "
						+ "cos(radians(?) - radians(longitud))) * 6378) as "
						+ "distancia "
						+ " FROM orders,clientes "
						+ " WHERE orders.nic = clientes.nic "
						+ " AND orders.estado = 2 "
						+ " AND orders.recurso = ? "
						+ " AND orders.num_show = 0 "
						+ " ORDER BY distancia ASC LIMIT 1";

				pst = conexion.getConnection()
						.prepareStatement(sql);
				pst.setString(1, start_latitud);
				pst.setString(2, start_latitud);
				pst.setString(3, start_longitud);
				pst.setString(4, recurso);

				ResultSet rs = conexion.Query(pst);
				if (rs.next()) {
					String num_orden = rs.getString("orden");
					sql = "update orders set num_show=? where orden=? and estado=2";
					pst = conexion.getConnection().prepareStatement(sql);
					pst.setInt(1, orden);
					pst.setString(2, num_orden);
					if (conexion.Update(pst) > 0) {
						conexion.Commit();
						orden++;
						start_latitud = rs.getString("latitud");
						start_longitud = rs.getString("longitud");
					}
				} else {
					seguir = false;
				}
			}
			out.print("OK");

		} catch (SQLException e) {
			out.println("Error: " + e.getMessage());
		} finally {
			if (conexion != null) {
				try {
					conexion.Close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
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
