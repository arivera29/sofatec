package com.are.sofatec;

import com.are.manejadores.ManejadorZonas;
import com.are.entidades.Zona;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class srvZonas
 */
@WebServlet("/SrvZonas")
public class SrvZonas extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SrvZonas() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void ProcesarPeticion(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();

		String operacion = (String) request.getParameter("operacion");
		db conexion = null;

		/* Inicio operacion Agregar departamento */
		try {

			if (operacion.equals("add")) { // agregar departamento
				String nombre = (String) request.getParameter("nombre");
				conexion = new db();

				ManejadorZonas manejador = new ManejadorZonas(conexion);
				if (manejador.Add(nombre)) {
					conexion.Commit();
					out.print("OK");
				} else {
					out.print("Error al procesar la solicitud");

				}

			}

			if (operacion.equals("update")) { // agregar departamento
				String nombre = (String) request.getParameter("nombre");
				String id = (String) request.getParameter("id");
				conexion = new db();

				ManejadorZonas manejador = new ManejadorZonas(conexion);
				if (manejador.Update(nombre, Integer.parseInt(id))) {
					conexion.Commit();
					out.print("OK");
				} else {
					out.print("Error al procesar la solicitud");

				}

			}

			if (operacion.equals("remove")) { // agregar departamento
				String id = (String) request.getParameter("id");
				conexion = new db();

				ManejadorZonas manejador = new ManejadorZonas(conexion);
				if (manejador.Remove(Integer.parseInt(id))) {
					conexion.Commit();
					out.print("OK");
				} else {
					out.print("Error al procesar la solicitud");

				}

			}
			
			if (operacion.equals("add_user")) { // agregar usuario
				String id = (String) request.getParameter("id");
				String usuario = (String) request.getParameter("usuario");
				conexion = new db();

				ManejadorZonas manejador = new ManejadorZonas(conexion);
				if (manejador.AddUsuario(Integer.parseInt(id), usuario)) {
					conexion.Commit();
					out.print("OK");
				} else {
					out.print("Error al procesar la solicitud");

				}

			}
			
			if (operacion.equals("remove_user")) { // agregar departamento
				String id = (String) request.getParameter("id");
				String usuario = (String) request.getParameter("usuario");
				conexion = new db();

				ManejadorZonas manejador = new ManejadorZonas(conexion);
				if (manejador.RemoveUsuario(Integer.parseInt(id),usuario)) {
					conexion.Commit();
					out.print("OK");
				} else {
					out.print("Error al procesar la solicitud");

				}

			}


			if (operacion.equals("list")) { // agregar departamento
				conexion = new db();
				ManejadorZonas manejador = new ManejadorZonas(conexion);
				ArrayList<Zona> lista = manejador.List();
				out.println("<table>");
				out.println("<tr>");
				out.println("<th>Id</th>");
				out.println("<th>Nombre</th>");
				out.println("<th>Accion</th>");
				out.println("</tr>");
				if (lista.size() > 0) {
					int fila = 1;
					for (Zona zona : lista) {
						out.println("<tr "
								+ (fila % 2 == 0 ? "class='odd'" : "") + ">");
						out.println("<td>" + zona.getId() + "</td>");
						out.println("<td>" + zona.getNombre() + "</td>");
						out.println("<td><a href=\"javascript:eliminar('"
								+ zona.getId() + "')\">Eliminar</a> "
								+ "<a href=\"mod_zona.jsp?id=" + zona.getId()
								+ "\">Modificar</a></td>");
						out.println("</tr>");
						fila++;
					}
				} else {
					out.println("<tr><td colspan=3>No hay registros</td></tr>");
				}
				out.println("</table>");

			}
			
			if (operacion.equals("list_user")) { // agregar departamento
				conexion = new db();
				String usuario = (String)request.getParameter("usuario");
				ManejadorZonas manejador = new ManejadorZonas(conexion);
				ArrayList<Zona> lista = manejador.List(usuario);
				out.println("<table>");
				out.println("<tr>");
				out.println("<th>ID</th>");
				out.println("<th>NOMBRE</th>");
				out.println("<th>ACCION</th>");
				out.println("</tr>");
				if (lista.size() > 0) {
					int fila = 1;
					for (Zona zona : lista) {
						out.println("<tr "
								+ (fila % 2 == 0 ? "class='odd'" : "") + ">");
						out.println("<td>" + zona.getId() + "</td>");
						out.println("<td>" + zona.getNombre() + "</td>");
						out.println("<td><a href=\"javascript:eliminar('"
								+ zona.getId() + "','" + usuario + "')\">Eliminar</a> ");
						out.println("</tr>");
						fila++;
					}
				} else {
					out.println("<tr><td colspan=3>No hay registros</td></tr>");
				}
				out.println("</table>");

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			out.print("Error de conexion con el servidor: " + e.getMessage());
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

		/* Fin operacion listar departamentos */

		out.close();
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
