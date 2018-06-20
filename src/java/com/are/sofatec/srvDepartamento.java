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
 * Servlet implementation class srvDepartamento
 */
@WebServlet("/srvDepartamento")
public class srvDepartamento extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public srvDepartamento() {
		// TODO Auto-generated constructor stub
	}

	protected void ProcesarPeticion(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();

		String operacion = (String) request.getParameter("operacion");
		db conexion = null;

		/* Inicio operacion Agregar departamento */

		if (operacion.equals("add")) { // agregar departamento
			String codigo = (String) request.getParameter("codigo");
			String descripcion = (String) request.getParameter("descripcion");
			try {
				conexion = new db();
				departamento dpto = new departamento(conexion);
				dpto.setCodigo(codigo);
				dpto.setDescripcion(descripcion);
				dpto.setKey(codigo);
				if (!dpto.exist()) {
					if (dpto.add()) {
						out.print("OK");
					} else {
						out.print(dpto.getError());
					}
				} else {
					out.print("Prioridad ya existe: " + dpto.getDescripcion());
				}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				out.print("Error de conexion con el servidor: "
						+ e.getMessage());
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

		/* Fin operacion Agregar departamento */

		if (operacion.equals("modify")) { // modificar departamento
			String codigo = (String) request.getParameter("codigo");
			String descripcion = (String) request.getParameter("descripcion");
			String key = (String) request.getParameter("key");
			try {
				conexion = new db();
				departamento dpto = new departamento(conexion);
				dpto.setKey(key);
				if (dpto.exist()) {
					dpto.setCodigo(codigo);
					dpto.setDescripcion(descripcion);
					
					if (dpto.modify()) {
						out.print("OK");
					} else {
						out.print(dpto.getError());
					}
				} else {
					out.print("Departamento no existe: " + key);
				}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				out.print("Error de conexion con el servidor: "
						+ e.getMessage());
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
		
		if (operacion.equals("remove")) { // modificar departamento
			String key = (String) request.getParameter("key");
			try {
				conexion = new db();
				departamento dpto = new departamento(conexion);
				dpto.setKey(key);
				if (dpto.exist()) {
					if (dpto.remove()) {
						out.print("OK");
					} else {
						out.print(dpto.getError());
					}
				} else {
					out.print("Departamento no existe: " + key);
				}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				out.print("Error de conexion con el servidor: "
						+ e.getMessage());
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


		/* Inicio operacion listar departamentos */
		if (operacion.equals("list")) {
			try {
				conexion = new db();
				String sql = "select depacodi,depadesc from departamentos order by depadesc";
				ResultSet rs = conexion.Query(sql);
				if (rs.next()) { // existen departamentos
					out.println("<h2>Listado departamentos</h2>");
					out.println("<table>");
					out.println("<tr>");
					out.println("<th>CODIGO</th>");
					out.println("<th>DESCRIPCION</th>");
					out.println("<th>ACCION</th>");
					out.println("</tr>");
					int fila = 0;
					do {
						out.println("<tr "  +(fila%2==0?"class='odd'":"") + ">");
						out.println("<td>" + rs.getString("depacodi") + "</td>");
						out.println("<td>" + rs.getString("depadesc") + "</td>");
						out.println("<td><a href=\"mod_departamento.jsp?codigo="
								+ rs.getString("depacodi")
								+ "\">Editar</a></td>");
						out.println("</tr>");
						fila++;
					} while (rs.next());
					out.println("</table>");
				}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				out.print(e.getMessage());
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

		}

		/* Inicio operacion combo de departamentos */
		if (operacion.equals("combo")) {
			try {
				conexion = new db();
				String sql = "select depacodi,depadesc from departamentos order by depadesc";
				out.println("<select id='departamento' name='departamento'>");
				out.println("<option value=''>Seleccionar</option>");
				ResultSet rs = conexion.Query(sql);
				if (rs.next()) { // existen departamentos
					do {
						out.println("<option value='" + rs.getString("depacodi") + "'>" + rs.getString("depadesc") + "</option>");
					} while (rs.next());
					
				}
				out.println("</select>");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				out.print(e.getMessage());
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
