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

import com.are.entidades.TipoOrden;
import com.are.manejadores.ManejadorTipoOrden;
import com.are.sofatec.db;

/**
 * Servlet implementation class SrvGrupos
 */
@WebServlet("/SrvTipoOrden")
public class SrvTipoOrden extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SrvTipoOrden() {
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

			if (operacion.equals("add")) { // Agregar Tipo de Orden
				
				String codigo = (String) request.getParameter("codigo");
				String descripcion = (String) request.getParameter("descripcion");
				String estado = (String) request.getParameter("estado");
				String tiempo_max_tercero = (String) request.getParameter("tiempo_max_tercero");
				String tiempo_max_interno = (String) request.getParameter("tipo_max_interno");

				conexion = new db();
				
				TipoOrden tipo = new TipoOrden();
				
				tipo.setCodigo(codigo);
				tipo.setDescripcion(descripcion);
				tipo.setEstado(Integer.parseInt(estado));
				tipo.setTiempo_max_tercero(Integer.parseInt(tiempo_max_tercero));
				tipo.setTiempo_max_interno(Integer.parseInt(tiempo_max_interno));
				
				ManejadorTipoOrden manejador = new ManejadorTipoOrden(conexion);
				
					if (manejador.Add(tipo)) {
						out.print("OK");
					} else {
						throw new Exception("Error al procesar la solicitud");
					}

			}

			if (operacion.equals("update")) { // Modificar Tipo de Orden
				
				String codigo = (String) request.getParameter("codigo");
				String descripcion = (String) request.getParameter("descripcion");
				String estado = (String) request.getParameter("estado");
				String tiempo_max_tercero = (String) request.getParameter("tiempo_max_tercero");
				String tiempo_max_interno = (String) request.getParameter("tipo_max_interno");
				String key = (String) request.getParameter("key");

				conexion = new db();
				
				TipoOrden tipo = new TipoOrden();
				
				tipo.setCodigo(codigo);
				tipo.setDescripcion(descripcion);
				tipo.setEstado(Integer.parseInt(estado));
				tipo.setTiempo_max_tercero(Integer.parseInt(tiempo_max_tercero));
				tipo.setTiempo_max_interno(Integer.parseInt(tiempo_max_interno));
				
				
				ManejadorTipoOrden manejador = new ManejadorTipoOrden(conexion);
				
				if (manejador.Find(key)) {
					if (manejador.Update(tipo,Integer.parseInt(key))) {
						out.print("OK");
					} else {
						throw new Exception("Error al procesar la solicitud");
					}
				} else {
					throw new Exception("Tipo de orden no se encuentra registrado. "
							+ key);
				}
			}

			

			
			if (operacion.equals("remove")) { // Eliminar Tipo de Orden
				String key = (String) request.getParameter("key");
				conexion = new db();
				
				ManejadorTipoOrden manejador = new ManejadorTipoOrden(conexion);
				
				if (manejador.Find(key)) {
					if (manejador.Remove(Integer.parseInt(key))) {
						out.print("OK");
					} else {
						throw new Exception("Error al procesar la solicitud");
					}
				} else {
					throw new Exception("Tipo de orden no se encuentra registrado. "
							+ key);
				}
			}

			if (operacion.equals("list")) { // Listado de Tipo de ordenes
				conexion = new db();
				ManejadorTipoOrden manejador = new ManejadorTipoOrden(conexion);
				ArrayList<TipoOrden> lista  = manejador.List();


				out.println("<h2>Listado Terceros</h2>");
				if (lista.size() > 0) {
				out.println("<table>");
				out.println("<tr>");
				out.println("<th>CODIGO</th>");
				out.println("<th>DESCRIPCION</th>");
				out.println("<th>TIEMPO MAX. TERC.</th>");
				out.println("<th>TIEMPO MAX. INT.</th>");
				out.println("<th>ESTADO</th>");
				out.println("<th></th>");
				out.println("</tr>");
				
				for (TipoOrden tipo : lista) { // existen registros
						out.println("<tr>");
						out.println("<td>" + tipo.getCodigo() + "</td>");
						out.println("<td>" + tipo.getDescripcion() + "</td>");
						out.println("<td>" + tipo.getTiempo_max_tercero() + "</td>");
						out.println("<td>" + tipo.getTiempo_max_interno() + "</td>");
						out.println("<td>"
								+ (tipo.getEstado()==1 ? "<img src=\"images/online.png\">"
										: "<img src=\"images/offline.png\">")
								+ "</td>");
						out.println("<td><a href='modificar_tipo_orden.jsp?codigo="
								+ tipo.getCodigo()  + "'>Modificar</a></td>");
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
