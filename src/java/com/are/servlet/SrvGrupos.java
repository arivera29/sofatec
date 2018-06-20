package com.are.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.are.entidades.Grupo;
import com.are.manejadores.ManejadorGrupo;
import com.are.sofatec.db;

/**
 * Servlet implementation class SrvGrupos
 */
@WebServlet("/SrvGrupos")
public class SrvGrupos extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SrvGrupos() {
        super();
        // TODO Auto-generated constructor stub
    }

    protected void ProcesarPeticion(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
    	response.setContentType("text/html;charset=ISO-8859-1");
		PrintWriter out = response.getWriter();

		String operacion = (String) request.getParameter("operacion");
		db conexion = null;

		/* Inicio operacion Agregar departamento */
		try {

			if (operacion.equals("add")) { // Agregar Cliente
				
				String nombre = (String) request.getParameter("nombre");
				String estado = (String) request.getParameter("estado");

				conexion = new db();
				Grupo grupo = new Grupo();
				grupo.setNombre(nombre);
				grupo.setEstado(Integer.parseInt(estado));
				
				ManejadorGrupo manejador = new ManejadorGrupo(conexion);
				
					if (manejador.Add(grupo)) {
						out.print("OK");
					} else {
						throw new Exception("Error al procesar la solicitud");
					}

			}

			if (operacion.equals("update")) { // Modificar Tipo de Orden

				String nombre = (String) request.getParameter("nombre");
				String estado = (String) request.getParameter("estado");
				String key = (String) request.getParameter("key");

				conexion = new db();
				Grupo grupo = new Grupo();
				grupo.setNombre(nombre);
				grupo.setEstado(Integer.parseInt(estado));
				
				ManejadorGrupo manejador = new ManejadorGrupo(conexion);
				
				if (manejador.Find(Integer.parseInt(key))) {
					if (manejador.Update(grupo,Integer.parseInt(key))) {
						out.print("OK");
					} else {
						throw new Exception("Error al procesar la solicitud");
					}
				} else {
					throw new Exception("Grupo no se encuentra registrado. "
							+ key);
				}
			}

			

			
			if (operacion.equals("remove")) { // Eliminar Tipo de Orden
				String key = (String) request.getParameter("key");
				conexion = new db();
				
				ManejadorGrupo manejador = new ManejadorGrupo(conexion);
				
				if (manejador.Find(Integer.parseInt(key))) {
					if (manejador.Remove(Integer.parseInt(key))) {
						out.print("OK");
					} else {
						throw new Exception("Error al procesar la solicitud");
					}
				} else {
					throw new Exception("Grupo no se encuentra registrado. "
							+ key);
				}
			}

			if (operacion.equals("list")) { // Listado de Tipo de ordenes
				conexion = new db();
				ManejadorGrupo manejador = new ManejadorGrupo(conexion);
				ArrayList<Grupo> lista  = manejador.List();


				out.println("<h2>Listado Grupos</h2>");
				if (lista.size() > 0) {
				out.println("<table>");
				out.println("<tr>");
				out.println("<th>CODIGO</th>");
				out.println("<th>NOMBRE</th>");
				out.println("<th>ESTADO</th>");
				out.println("<th></th>");
				out.println("</tr>");
				
				for (Grupo grupo : lista) { // existen registros
						out.println("<tr>");
						out.println("<td>" + grupo.getId() + "</td>");
						out.println("<td>" + grupo.getNombre() + "</td>");
						out.println("<td>"
								+ (grupo.getEstado()==1 ? "<img src=\"images/online.png\">"
										: "<img src=\"images/offline.png\">")
								+ "</td>");
						out.println("<td><a href='grupos_personal.jsp?id="
								+ grupo.getId()  + "'>Personal</a></td>");
						out.println("</tr>");

					}

				
				out.println("</table>");
				} else {
					out.println("<strong>No hay registros</strong>");
					
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

		out.close();
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
