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

import com.are.entidades.Contratista;
import com.are.manejadores.ManejadorContratista;
import com.are.sofatec.db;

/**
 * Servlet implementation class SrvContratistas
 */
@WebServlet("/SrvContratistas")
public class SrvContratistas extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SrvContratistas() {
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
                                String correo = (String) request.getParameter("correo");
				String estado = (String) request.getParameter("estado");

				conexion = new db();
				Contratista contratista = new Contratista();
				contratista.setNombre(nombre);
                                contratista.setCorreo(correo);
				contratista.setEstado(Integer.parseInt(estado));
				
				ManejadorContratista manejador = new ManejadorContratista(conexion);
				
					if (manejador.Add(contratista)) {
						out.print("OK");
					} else {
						throw new Exception("Error al procesar la solicitud");
					}

			}

			if (operacion.equals("update")) { // Modificar Tipo de Orden

				String nombre = (String) request.getParameter("nombre");
				String estado = (String) request.getParameter("estado");
                                String correo = (String) request.getParameter("correo");
				String key = (String) request.getParameter("key");

				conexion = new db();
				Contratista contratista = new Contratista();
				contratista.setNombre(nombre);
                                contratista.setCorreo(correo);
				contratista.setEstado(Integer.parseInt(estado));
				
				ManejadorContratista manejador = new ManejadorContratista(conexion);
				
				if (manejador.Find(Integer.parseInt(key))) {
					if (manejador.Update(contratista,Integer.parseInt(key))) {
						out.print("OK");
					} else {
						throw new Exception("Error al procesar la solicitud");
					}
				} else {
					throw new Exception("Contratista no se encuentra registrado. "
							+ key);
				}
			}

			

			
			if (operacion.equals("remove")) { // Eliminar Tipo de Orden
				String key = (String) request.getParameter("key");
				conexion = new db();
				
				ManejadorContratista manejador = new ManejadorContratista(conexion);
				
				if (manejador.Find(Integer.parseInt(key))) {
					if (manejador.Remove(Integer.parseInt(key))) {
						out.print("OK");
					} else {
						throw new Exception("Error al procesar la solicitud");
					}
				} else {
					throw new Exception("Contratista no se encuentra registrado. "
							+ key);
				}
			}
                        
                        if (operacion.equals("add_user")) {
                            String key = (String) request.getParameter("key");
                            String usuario = (String)request.getParameter("usuario");
				conexion = new db();
				
				ManejadorContratista manejador = new ManejadorContratista(conexion);
				
				if (manejador.Find(Integer.parseInt(key))) {
					if (manejador.AddUsuario(Integer.parseInt(key), usuario)) {
						out.print("OK");
					} else {
						throw new Exception("Error al procesar la solicitud");
					}
				} else {
					throw new Exception("Contratista no se encuentra registrado. "
							+ key);
				}
                            
                            
                        }
                        
                        if (operacion.equals("remove_user")) {
                            String key = (String) request.getParameter("key");
                            String usuario = (String)request.getParameter("usuario");
				conexion = new db();
				
				ManejadorContratista manejador = new ManejadorContratista(conexion);
				
				if (manejador.Find(Integer.parseInt(key))) {
					if (manejador.RemoveUsuario(Integer.parseInt(key), usuario)) {
						out.print("OK");
					} else {
						throw new Exception("Error al procesar la solicitud");
					}
				} else {
					throw new Exception("Contratista no se encuentra registrado. "
							+ key);
				}
                            
                            
                        }

			if (operacion.equals("list")) { // Listado de Tipo de ordenes
				conexion = new db();
				ManejadorContratista manejador = new ManejadorContratista(conexion);
				ArrayList<Contratista> lista  = manejador.List();


				out.println("<h2>Listado Contratistas</h2>");
				if (lista.size() > 0) {
				out.println("<table>");
				out.println("<tr>");
				out.println("<th>CODIGO</th>");
				out.println("<th>NOMBRE</th>");
                                out.println("<th>CORREO</th>");
				out.println("<th>ESTADO</th>");
				out.println("<th></th>");
				out.println("</tr>");
				int fila = 0;
				for (Contratista contratista : lista) { // existen registros
						out.println("<tr " +(fila%2==0?"class='odd'":"")  +">");
						out.println("<td>" + contratista.getCodigo() + "</td>");
						out.println("<td>" + contratista.getNombre() + "</td>");
                                                out.println("<td>" + contratista.getCorreo() + "</td>");
						out.println("<td>" + (contratista.getEstado()==1 ? "<img src=\"images/online.png\">" : "<img src=\"images/offline.png\">") + "</td>");
						out.println("<td><a href='mod_contratista.jsp?id=" + contratista.getCodigo()  + "'>Editar</a></td>");
						out.println("</tr>");
                                                fila++;
					}

				
				out.println("</table>");
				} else {
					out.println("<strong>No hay registros</strong>");
					
				}

			}
			
			if (operacion.equals("list_user")) { // Listado de Tipo de ordenes
				String usuario = (String)request.getParameter("usuario");
                                conexion = new db();
				ManejadorContratista manejador = new ManejadorContratista(conexion);
				ArrayList<Contratista> lista  = manejador.ListByUsuario(usuario);


				out.println("<h2>Listado Contratistas Asignados</h2>");
				if (lista.size() > 0) {
				out.println("<table>");
				out.println("<tr>");
				out.println("<th>CODIGO</th>");
				out.println("<th>NOMBRE</th>");
				out.println("<th></th>");
				out.println("</tr>");
				int fila = 0;
				for (Contratista contratista : lista) { // existen registros
						out.println("<tr " +(fila%2==0?"class='odd'":"")  +">");
						out.println("<td>" + contratista.getCodigo() + "</td>");
						out.println("<td>" + contratista.getNombre() + "</td>");
						out.println("<td><a href=\"javascript:eliminar(" + contratista.getCodigo()  + ",'" + usuario +"')\">Eliminar</a></td>");
						out.println("</tr>");
                                                fila++;
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
