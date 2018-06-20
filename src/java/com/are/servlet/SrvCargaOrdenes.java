package com.are.servlet;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.are.sofatec.db;
import com.csvreader.CsvReader;

/**
 * Servlet implementation class ProcessOrders
 */
@WebServlet("/SrvCargaOrdenes")
public class SrvCargaOrdenes extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SrvCargaOrdenes() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void ProcesarPeticion(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/plain;charset=ISO-8859-1");
		PrintWriter out = response.getWriter();
		ServletContext servletContext = getServletContext();
		HttpSession sesion = request.getSession();

		if (sesion.getAttribute("usuario") == null) {
			out.print("La sesion ha caducado... intente de nuevo por favor");
			return;
		}

		String tercero = (String) request.getParameter("tercero");
		String filename = (String) request.getParameter("archivo");
		String path = servletContext.getRealPath("/upload") + File.separator
				+ filename;
		CsvReader reader = null;
		db conexion = null;

		try {

			reader = new CsvReader(path);
			reader.setDelimiter('	'); // tabulador

			reader.readHeaders();

			String[] headers = reader.getHeaders();
			if (headers.length != 22) { // estan las columnas OK.
				throw new IOException(
						"Archivo no cargado.  Numero de columnas no coinciden con la estructura requerida (22). Columnas en el archivo " + headers.length );
			}
			out.println("<h2>Resultado</h2>"); // fin de encabezados

			conexion = new db();
			out.println("<table>");
			out.println("<tr>");
			out.println("<th>ORDEN</th>");
			out.println("<th>RESULTADO</th>");
			out.println("</tr>");
			while (reader.readRecord()) {
				String departamento = reader.get(0);
				String localidad = reader.get(1);
				String orden = reader.get(2);
				String servicio = reader.get(3);
				String cliente = reader.get(4);
				String sector = reader.get(6);
				String barrio = reader.get(7);
				String direccion = reader.get(8);
				String medidor = reader.get(9);
				String ruta = reader.get(10);
				String tipo = reader.get(11);
				String fecha_orden = reader.get(14);
				String fecha_at = reader.get(15);
				String sello = reader.get(17);
				String lectura = reader.get(20);
				String observacion = reader.get(21);
				
				out.println("<tr>");
				out.println("<td>" + orden + "</td>");
				
				try {
					
					String sql = "select odscodi from ordenes where odscodi=?";
					java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
					pst.setString(1, orden);
					ResultSet rs = conexion.Query(pst);
					if (rs.next()) {
						throw new SQLException("ORDEN YA EXISTE");
					}
					
					
					
					
				} catch (SQLException e) {
					out.println("<td><img src='images/error.png'><strong>" + orden + "</strong> Error: " + e.getMessage() + "</td>");
				} catch (NumberFormatException e) {
					out.println("<td><img src='images/error.png'><strong>" + orden + "</strong> Error: " + e.getMessage() + "</td>");
				}
				out.println("<tr>");
			}

			out.println("</table>");

		} catch (FileNotFoundException e) {
			out.println("<img src='images/error.png'><strong>" + e.getMessage() + "</strong>");
		} catch (IOException e) {
			out.println("<img src='images/error.png'><strong>" + e.getMessage() + "</strong>");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			out.println("<img src='images/error.png'><strong>" + e.getMessage() + "</strong>");
		} finally {
			if (reader != null) {
				reader.close();
			}
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
