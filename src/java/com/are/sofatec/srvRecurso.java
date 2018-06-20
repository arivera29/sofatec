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
 * Servlet implementation class srvRecurso
 */
@WebServlet("/srvRecurso")
public class srvRecurso extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public srvRecurso() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void ProcesarPeticion(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=ISO-8859-1");
		PrintWriter out = response.getWriter();

		String operacion = (String) request.getParameter("operacion");
		db conexion = null;

		/* Inicio operacion Agregar departamento */
		try {
			
		if (operacion.equals("add")) { // agregar departamento
			String codigo = (String) request.getParameter("codigo");
			String nombre = (String) request.getParameter("nombre");
			String direccion = (String) request.getParameter("direccion");
			String telefono = (String) request.getParameter("telefono");
			String cargo = (String) request.getParameter("cargo");
			String correo = (String) request.getParameter("correo");
			String estado = (String) request.getParameter("estado");
			String rol = (String) request.getParameter("rol");

			
				conexion = new db();
				recursohumano recurso = new recursohumano(conexion);
				recurso.setCodigo(codigo);
				recurso.setNombre(nombre);
				recurso.setDireccion(direccion);
				recurso.setTelefono(telefono);
				recurso.setCorreo(correo);
				recurso.setCargo(cargo);
				recurso.setEstado(estado);
				recurso.setRol(rol);

				recurso.setKey(codigo);
				if (!recurso.exist()) {
					if (recurso.add()) {
						out.print("OK");
					} else {
						out.print(recurso.getError());
					}
				} else {
					out.print("Recurso humano ya existe: "
							+ recurso.getNombre());
				}

			

		}

		if (operacion.equals("update")) { // agregar departamento
			String codigo = (String) request.getParameter("codigo");
			String nombre = (String) request.getParameter("nombre");
			String direccion = (String) request.getParameter("direccion");
			String telefono = (String) request.getParameter("telefono");
			String cargo = (String) request.getParameter("cargo");
			String correo = (String) request.getParameter("correo");
			String estado = (String) request.getParameter("estado");
			String key = (String) request.getParameter("key");
			String rol = (String) request.getParameter("rol");
			// out.print("Recibe: " + codigo +"," + nombre + "," + direccion +
			// "," + telefono + "," + contratista + "," + cargo + "," + proyecto
			// + "," + correo + "," + estado + "," + rol + "," + key );

				conexion = new db();
				recursohumano recurso = new recursohumano(conexion);
				// if (recurso.exist(key)) {
				recurso.setCodigo(codigo);
				recurso.setNombre(nombre);
				recurso.setDireccion(direccion);
				recurso.setTelefono(telefono);
				recurso.setCorreo(correo);
				recurso.setCargo(cargo);
				recurso.setEstado(estado);
				recurso.setRol(rol);
				if (recurso.modify(key)) {
					out.print("OK");
				} else {
					out.print("Error al modificar: " + recurso.getError());
				}
				/*
				 * } else { out.print("Recurso humano no existe: " + key + ". "
				 * + recurso.getError());
				 * 
				 * }
				 */



		}

		if (operacion.equals("remove")) { // agregar departamento
			String key = (String) request.getParameter("key");

				conexion = new db();
				recursohumano recurso = new recursohumano(conexion);
				recurso.setKey(key);
				if (recurso.exist(key)) {
					recurso.setKey(key);
					if (recurso.remove()) {
						out.print("OK");
					} else {
						out.print("Error al eliminar: " + recurso.getError());
					}
				} else {
					out.print("Recurso humano no existe: " + key + ". "
							+ recurso.getError());

				}



		}
		/* Inicio operacion combo de departamentos */
		if (operacion.equals("combo")) {
				conexion = new db();
				String sql = "select recucodi,recunomb from recurso order by recunomb";
				out.println("<select id='recurso' name='recurso'>");
				ResultSet rs = conexion.Query(sql);
				if (rs.next()) { // existen departamentos
					do {
						out.println("<option value='"
								+ rs.getString("recucodi") + "'>"
								+ rs.getString("recunomb") + "</option>");
					} while (rs.next());

				} else {

					out.println("<option value=''>Seleccionar</option>");

				}
				out.println("</select>");


		}

		if (operacion.equals("list")) {
				conexion = new db();

				String criterio = "";
				if (request.getParameter("criterio") != null) {
					criterio = (String) request.getParameter("criterio");
				}

				String sql = "select recucodi,recunomb,cargdesc,recuesta from recurso,cargos where (recurso.recucarg=cargos.cargcodi) ";
				if (!criterio.equals("")) {
					sql += " and (recunomb like '%" + criterio+ "%')";
				}

				sql += " order by recunomb";
				ResultSet rs = conexion.Query(sql);
				out.println("<h2>Registros encontrado</h2>");
				out.println("<table>");
				out.println("<tr>");
				out.println("<th>ID</th>");
				out.println("<th>NOMBRE</th>");
				out.println("<th>CARGO</th>");
				out.println("<th>ESTADO</th>");
				out.println("<th></th>");
				out.println("</tr>");
				
				if (rs.next()) { // existen departamentos
					int fila = 0;
					do {
						out.println("<tr " +(fila%2==0?"class='odd'":"")  +">");
						out.println("<td>" + rs.getString("recucodi") + "</td>");
						out.println("<td>" + rs.getString("recunomb") + "</td>");
						out.println("<td>" + rs.getString("cargdesc") + "</td>");
						out.println("<td>" + (rs.getString("recuesta").equals("1")?"<img src=\"images/online.png\">":"<img src=\"images/offline.png\">") + "</td>");
						out.println("<td><a href=\"mod_recurso.jsp?codigo="
								+ rs.getString("recucodi")
								+ "\">Editar</a></td>");
						out.println("</tr>");
						fila++;
					} while (rs.next());
					out.println("<tr>");
					out.print("<td colspan=6>Total registros: <strong>" + fila + "</strong></td>");
					out.println("</tr>");
				} else {
					out.println("<tr>");
					out.print("<td colspan=6>Cero (0) registros encontrados con el criterio de busqueda</td>");
					out.println("</tr>");
				}
				out.println("</table>");


		}

		if (operacion.equals("add_localidad")) { 
			String recurso = (String)request.getParameter("recurso");
			String localidad = (String)request.getParameter("localidad");
			conexion = new db();
			
			recursohumano rh = new recursohumano(conexion);
			if (rh.AddRecursoLocalidad(recurso, localidad)) {
				out.print("OK");
			}else {
				throw new Exception(
						"Error al procesa la solicitud");
			}
			
		}
		
		if (operacion.equals("remove_localidad")) { 
			String id = (String)request.getParameter("id");
			conexion = new db();
			
			recursohumano rh = new recursohumano(conexion);
			if (rh.RemoveRecursoLocalidad(id)) {
				out.print("OK");
			}else {
				throw new Exception(
						"Error al procesa la solicitud");
			}
			
		}
		

		

		if (operacion.equals("buscar")) {
				conexion = new db();
				String codigo = "";
				if (request.getParameter("codigo") != null) {
					codigo = (String) request.getParameter("codigo");
				}

				String sql = "select recucodi,recunomb from recurso where recuesta=1 and recurol = 1 and recucodi='"
						+ codigo + "'";
				ResultSet rs = conexion.Query(sql);
				if (rs.next()) { // existe recurso
					out.println(rs.getString("recunomb"));
				} else {
					out.print("No found");
				}

		}

		if (operacion.equals("add_supervisor")) { // agregar departamento
			String cedula = (String) request.getParameter("cedula");
				conexion = new db();
				recursohumano recurso = new recursohumano(conexion);
				if (recurso.Find(cedula)) {
					if (!recurso.isSupervisor(cedula)) {
						if (recurso.AddSupervisor(cedula)) {
							out.print("OK");
						} else {
							out.print(recurso.getError());
						}
					} else {
						throw new Exception(
								"Cedula ya se encuentra registrada como Supervisor");
					}
				} else {
					throw new Exception("Cedula no se encuentra registrada");
				}

			

		}

		if (operacion.equals("remove_supervisor")) { // agregar departamento
			String cedula = (String) request.getParameter("cedula");

				conexion = new db();
				recursohumano recurso = new recursohumano(conexion);
				if (recurso.Find(cedula)) {
					if (recurso.isSupervisor(cedula)) {
						if (recurso.RemoveSupervisor(cedula)) {
							out.print("OK");
						} else {
							out.print(recurso.getError());
						}
					} else {
						throw new Exception(
								"Cedula no se encuentra registrada como Supervisor");
					}
				} else {
					throw new Exception("Cedula no se encuentra registrada");
				}

			

		}

		if (operacion.equals("add_recurso_supervisor")) { // agregar
															// departamento
			String supervisor = (String) request.getParameter("supervisor");
			String cedula = (String) request.getParameter("recurso");
				conexion = new db();
				recursohumano recurso = new recursohumano(conexion);
				if (recurso.Find(cedula) && recurso.Find(supervisor)) {
					if (!recurso.isSupervisor(cedula)) {
						if (recurso.AddRecursoSupervisor(supervisor, cedula)) {
							out.print("OK");
						} else {
							out.print(recurso.getError());
						}
					} else {
						throw new Exception(
								"Cedula se encuentra registrada como Supervisor");
					}
				} else {
					throw new Exception("Cedula no se encuentra registrada");
				}



		}

		if (operacion.equals("list_supervisor")) {

				conexion = new db();
				String sql = "select recucodi,recunomb from recurso where recusupe=1 order by recunomb";
				ResultSet rs = conexion.Query(sql);

				out.println("<table>");
				out.println("<tr>");
				out.println("<th>Id</th>");
				out.println("<th>Nombre</th>");
				out.println("<th>Accion</th>");
				out.println("</tr>");
				if (rs.next()) { // existen departamentos
					do {
						out.println("<tr>");
						out.println("<td>" + rs.getString("recucodi") + "</td>");
						out.println("<td>" + rs.getString("recunomb") + "</td>");
						out.println("<td><a href=\"RecursoSupervisor.jsp?supervisor="
								+ rs.getString("recucodi")
								+ "\">Recurso a Cargo</a>  <a href=\"javascript:eliminar('"
								+ rs.getString("recucodi")
								+ "')\">Eliminar</a> </td>");

					} while (rs.next());
				} else {
					out.println("<tr>");
					out.print("<td colspan=3>No se encontraron registros</td>");
					out.println("</tr>");
				}
				out.println("</table>");
				rs.close();


		}

		if (operacion.equals("list_recurso_supervisor")) {

				conexion = new db();
				String supervisor = (String) request.getParameter("supervisor");
				String sql = "select recucodi,recunomb from recurso where recucesu='"
						+ supervisor + "' order by recunomb";
				ResultSet rs = conexion.Query(sql);

				out.println("<table>");
				out.println("<tr>");
				out.println("<th>Id</th>");
				out.println("<th>Nombre</th>");
				out.println("<th>Accion</th>");
				out.println("</tr>");
				if (rs.next()) { // existen departamentos
					do {
						out.println("<tr>");
						out.println("<td>" + rs.getString("recucodi") + "</td>");
						out.println("<td>" + rs.getString("recunomb") + "</td>");
						out.println("<td><a href=\"javascript:eliminar('"
								+ rs.getString("recucodi")
								+ "')\">Eliminar</a> </td>");

					} while (rs.next());
				} else {
					out.println("<tr>");
					out.print("<td colspan=3>No se encontraron registros</td>");
					out.println("</tr>");
				}
				out.println("</table>");
				rs.close();

			
		}
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			out.print("Error de conexion con el servidor: "
					+ e.getMessage());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
