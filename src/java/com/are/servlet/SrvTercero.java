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

import com.are.entidades.Tercero;
import com.are.manejadores.ManejadorTercero;
import com.are.sofatec.db;

/**
 * Servlet implementation class SrvGrupos
 */
@WebServlet("/SrvTercero")
public class SrvTercero extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SrvTercero() {
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

			if (operacion.equals("add")) { // Agregar tercero
				
				String codigo = (String) request.getParameter("codigo");
				String nombre = (String) request.getParameter("nombre");
				String estado = (String) request.getParameter("estado");

				conexion = new db();
				Tercero tercero = new Tercero();
				tercero.setCodigo(codigo);
				tercero.setNombre(nombre);
				tercero.setEstado(Integer.parseInt(estado));
				
				ManejadorTercero manejador = new ManejadorTercero(conexion);
				
					if (manejador.Add(tercero)) {
						out.print("OK");
					} else {
						throw new Exception("Error al procesar la solicitud");
					}

			}

			if (operacion.equals("update")) { // Modificar Tipo de Orden
				
				String codigo = (String) request.getParameter("codigo");
				String nombre = (String) request.getParameter("nombre");
				String estado = (String) request.getParameter("estado");
				String key = (String) request.getParameter("key");

				conexion = new db();
				Tercero tercero = new Tercero();
				tercero.setCodigo(codigo);
				tercero.setNombre(nombre);
				tercero.setEstado(Integer.parseInt(estado));
				
				ManejadorTercero manejador = new ManejadorTercero(conexion);
				
				if (manejador.Find(Integer.parseInt(key))) {
					if (manejador.Update(tercero,Integer.parseInt(key))) {
						out.print("OK");
					} else {
						throw new Exception("Error al procesar la solicitud");
					}
				} else {
					throw new Exception("Tercero no se encuentra registrado. "
							+ key);
				}
			}

			

			
			if (operacion.equals("remove")) { // Eliminar Tipo de Orden
				String key = (String) request.getParameter("key");
				conexion = new db();
				
				ManejadorTercero manejador = new ManejadorTercero(conexion);
				
				if (manejador.Find(Integer.parseInt(key))) {
					if (manejador.Remove(Integer.parseInt(key))) {
						out.print("OK");
					} else {
						throw new Exception("Error al procesar la solicitud");
					}
				} else {
					throw new Exception("Tercero no se encuentra registrado. "
							+ key);
				}
			}

			if (operacion.equals("list")) { // Listado de Tipo de ordenes
				conexion = new db();
				ManejadorTercero manejador = new ManejadorTercero(conexion);
				ArrayList<Tercero> lista  = manejador.List();


				out.println("<h2>Listado Terceros</h2>");
				if (lista.size() > 0) {
				out.println("<table>");
				out.println("<tr>");
				out.println("<th>CODIGO</th>");
				out.println("<th>NOMBRE</th>");
				out.println("<th>ESTADO</th>");
				out.println("<th></th>");
				out.println("</tr>");
				
				for (Tercero tercero : lista) { // existen registros
						out.println("<tr>");
						out.println("<td>" + tercero.getCodigo() + "</td>");
						out.println("<td>" + tercero.getNombre() + "</td>");
						out.println("<td>"
								+ (tercero.getEstado()==1 ? "<img src=\"images/online.png\">"
										: "<img src=\"images/offline.png\">")
								+ "</td>");
						out.println("<td><a href='modificar_tercero.jsp?codigo="
								+ tercero.getCodigo()  + "'>Modificar</a></td>");
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
