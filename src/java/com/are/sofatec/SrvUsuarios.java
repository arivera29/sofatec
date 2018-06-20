package com.are.sofatec;

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
 * Servlet implementation class SrvUsuarios
 */
@WebServlet("/SrvUsuarios")
public class SrvUsuarios extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SrvUsuarios() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void ProcesarPeticion(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=ISO-8859-1");
		PrintWriter out = response.getWriter();

		String operacion = (String) request.getParameter("operacion");
		db conexion = null;

		try {

			if (operacion.equals("add")) { // agregar departamento
				String usuario = (String) request.getParameter("usuario");
				String nombre = (String) request.getParameter("nombre");
				String perfil = (String) request.getParameter("perfil");
				String clave = (String) request.getParameter("clave");
				String estado = (String) request.getParameter("estado");

				String hda = (String) request.getParameter("hda");
				String resolver = (String) request.getParameter("resolver");
				String anomalias = (String) request.getParameter("anomalias");
				String reportes = (String) request.getParameter("reportes");

				conexion = new db();
				Usuarios user = new Usuarios();
				user.setUsuario(usuario);
				user.setNombre(nombre);
				user.setPerfil(perfil);
				user.setClave(clave);
				user.setEstado(estado);

				user.setHda(Integer.parseInt(hda));
				user.setResolver(Integer.parseInt(resolver));
				user.setAnomalias(Integer.parseInt(anomalias));
				user.setReportes(Integer.parseInt(reportes));

				ManejadorUsuarios manejador = new ManejadorUsuarios(conexion);
				
				if (!manejador.exist(usuario)) {
					manejador.setUsuario(user);
					if (manejador.add()) {
						out.print("OK");
					} else {
						out.print(user.getError());
					}
				} else {
					out.print("Usuario ya existe: " + user.getNombre());
				}

			}

			if (operacion.equals("update")) { // Update departamento
				String usuario = (String) request.getParameter("usuario");
				String key = (String) request.getParameter("key");
				String nombre = (String) request.getParameter("nombre");
				String perfil = (String) request.getParameter("perfil");
				String clave = (String) request.getParameter("clave");
				String estado = (String) request.getParameter("estado");

				String hda = (String) request.getParameter("hda");
				String resolver = (String) request.getParameter("resolver");
				String anomalias = (String) request.getParameter("anomalias");
				String reportes = (String) request.getParameter("reportes");

				conexion = new db();
				Usuarios user = new Usuarios();
				user.setUsuario(usuario);
				user.setNombre(nombre);
				user.setPerfil(perfil);
				user.setClave(clave);
				user.setEstado(estado);

				user.setHda(Integer.parseInt(hda));
				user.setResolver(Integer.parseInt(resolver));
				user.setAnomalias(Integer.parseInt(anomalias));
				user.setReportes(Integer.parseInt(reportes));
				ManejadorUsuarios manejador = new ManejadorUsuarios(conexion);

				if (manejador.exist(key)) {
					manejador.setUsuario(user);
					if (manejador.update(key)) {
						out.print("OK");
					} else {
						out.print(user.getError());
					}
				} else {
					out.print("Usuario no existe: " + usuario);
				}

			}

			
			if (operacion.equals("list")) {

				conexion = new db();
				String criterio = "";
				ArrayList<Usuarios> lista = new ArrayList<Usuarios>();
				ManejadorUsuarios manejador = new ManejadorUsuarios(conexion);
				
				if (request.getParameter("criterio") != null) {
					criterio = (String) request.getParameter("criterio");
					lista = manejador.Busqueda(criterio);
				}else {
					lista = manejador.list();
				}

				

				out.println("<h2>Usuarios encontrados</h2>");

				out.println("<table>");
				out.println("<tr>");
				out.println("<th>Usuario</th>");
				out.println("<th>Nombre</th>");
				out.println("<th>Perfil</th>");
				out.println("<th>Estado</th>");
				out.println("<th></th>");
				out.println("</tr>");
				int contador = 0;
				for (Usuarios usuario : lista) {
						if (contador % 2 == 0) {
							out.println("<tr class='odd'>");
						} else {
							out.println("<tr>");
						}
						out.println("<td>" + usuario.getUsuario() + "</td>");
						out.println("<td>" + usuario.getNombre() + "</td>");
						out.println("<td>" + usuario.getPerfil() + "</td>");
						out.println("<td>");
						if (usuario.getEstado().equals("1")) {
							out.print("<img src=\"images/online.png\">");
						}else {
							out.print("<img src=\"images/offline.png\">");
						}
						out.print("</td>");
						out.println("<td>");
						out.print("<a href=\"updateuser.jsp?usuario="
								+ usuario.getUsuario() + "\" >Editar</a>");
						out.print("<a href=\"agregar_zonausuario.jsp?usuario="
								+ usuario.getUsuario() + "\" >Zonas</a>");
                                                out.print("<a href=\"agregar_contratista_usuario.jsp?usuario="
								+ usuario.getUsuario() + "\" >Contratistas</a>");
						out.println("</td>");
						out.println("</tr>");
						contador++;

					}
				out.println("</table>");
				out.println("<p>Total usuarios: " + contador + "</p>");
				
				if (contador == 0) {
					out.println("<p>No se encontraron registros</p>");
				}

			}
			

			if (operacion.equals("changepassword")) {
				String user = (String) request.getParameter("usuario");
				String clave = (String) request.getParameter("clave");

				conexion = new db();
				ManejadorUsuarios manejador = new ManejadorUsuarios(conexion);
				if (manejador.changepassword(user, clave)) {
					out.print("OK");
				} 

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
