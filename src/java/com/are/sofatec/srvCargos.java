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
 * Servlet implementation class srvCargos
 */
@WebServlet("/srvCargos")
public class srvCargos extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public srvCargos() {
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

		if (operacion.equals("add")) { // agregar departamento
			String codigo = (String) request.getParameter("codigo");
			String descripcion = (String) request.getParameter("descripcion");
			try {
				conexion = new db();
				cargos cargo = new cargos(conexion);
				cargo.setCodigo(codigo);
				cargo.setDescripcion(descripcion);
				cargo.setKey(codigo);
				if (!cargo.exist()) {
					if (cargo.add()) {
						out.print("OK");
					} else {
						out.print(cargo.getError());
					}
				} else {
					out.print("Prioridad ya existe: " + cargo.getDescripcion());
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

		if (operacion.equals("modify")) { // agregar departamento
			String codigo = (String) request.getParameter("codigo");
			String descripcion = (String) request.getParameter("descripcion");
			String key = (String) request.getParameter("key");
			try {
				conexion = new db();
				cargos cargo = new cargos(conexion);
				cargo.setKey(key);
				if (cargo.exist()) {
					cargo.setCodigo(codigo);
					cargo.setDescripcion(descripcion);
					if (cargo.modify()) {
						out.print("OK");
					} else {
						out.print(cargo.getError());
					}
				} else {
					out.print("Cargo no existe: " + key);
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
		if (operacion.equals("remove")) { // agregar departamento
			String key = (String) request.getParameter("key");
			try {
				conexion = new db();
				cargos cargo = new cargos(conexion);
				cargo.setKey(key);
				if (cargo.exist()) {
					if (cargo.remove()) {
						out.print("OK");
					} else {
						out.print(cargo.getError());
					}
				} else {
					out.print("Cargo no existe: " + key);
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
				String sql = "select cargcodi,cargdesc from cargos order by cargdesc";
				ResultSet rs = conexion.Query(sql);
				if (rs.next()) { // existen departamentos
					out.println("<h2>Listado Cargos</h2>");
					out.println("<table>");
					out.println("<tr>");
					out.println("<th>ID</th>");
					out.println("<th>DESCRIPCION</th>");
					out.println("<th>ACCION</th>");
					out.println("</tr>");
					int fila=1;
					do {
						out.println("<tr " + (fila%2==0?"":"class='odd'") + ">");
						out.println("<td>" + rs.getString("cargcodi") + "</td>");
						out.println("<td>" + rs.getString("cargdesc") + "</td>");
						out.println("<td><a href=\"mod_cargo.jsp?codigo="
								+ rs.getString("cargcodi")
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
				String sql = "select cargcodi,cargdesc from cargos order by cargdesc";
				out.println("<select id='cargo' name='cargo'>");
				ResultSet rs = conexion.Query(sql);
				if (rs.next()) { // existen departamentos
					do {
						out.println("<option value='" + rs.getString("cargcodi") + "'>" + rs.getString("cargdesc") + "</option>");
					} while (rs.next());
					
				}else {
					
					out.println("<option value=''>Seleccionar</option>");
					
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
