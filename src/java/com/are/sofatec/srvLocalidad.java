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
 * Servlet implementation class srvLocalidad
 */
@WebServlet("/srvLocalidad")
public class srvLocalidad extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public srvLocalidad() {
        super();
        // TODO Auto-generated constructor stub
    }

    protected void ProcesarPeticion(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
    	response.setContentType("text/html;charset=ISO-8859-1");
		PrintWriter out = response.getWriter();

		String operacion = (String) request.getParameter("operacion");
		db conexion = null;



		if (operacion.equals("add")) { // agregar Localidad
			String codigo = (String) request.getParameter("codigo");
			String descripcion = (String) request.getParameter("descripcion");
			String departamento = (String) request.getParameter("departamento");
			String latitud = (String) request.getParameter("latitud");
			String longitud = (String) request.getParameter("longitud");
			
			try {
				conexion = new db();
				localidad loca = new localidad(conexion);
				loca.setCodigo(codigo);
				loca.setDescripcion(descripcion);
				loca.setDepartamento(departamento);
				loca.setKey(codigo);
				loca.setLatitud(latitud);
				loca.setLongitud(longitud);
				if (!loca.exist()) {
					if (loca.add()) {
						out.print("OK");
					} else {
						out.print(loca.getError());
					}
				} else {
					out.print("Prioridad ya existe: " + loca.getDescripcion());
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


		
		if (operacion.equals("modify")) { // Modificar localidad
			String codigo = (String) request.getParameter("codigo");
			String descripcion = (String) request.getParameter("descripcion");
			String departamento = (String) request.getParameter("departamento");
			String latitud = (String) request.getParameter("latitud");
			String longitud = (String) request.getParameter("longitud");
			String key = (String) request.getParameter("key");
			try {
				conexion = new db();
				localidad loca = new localidad(conexion);
				loca.setKey(key);
				if (loca.exist()) {
					loca.setCodigo(codigo);
					loca.setDescripcion(descripcion);
					loca.setDepartamento(departamento);
					loca.setLatitud(latitud);
					loca.setLongitud(longitud);
					
					
					if (loca.modify()) {
						out.print("OK");
					} else {
						out.print(loca.getError());
					}
				} else {
					out.print("Localidad no existe " + codigo);
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

		
		if (operacion.equals("remove")) { // Modificar localidad
			String codigo = (String) request.getParameter("codigo");

			try {
				conexion = new db();
				localidad loca = new localidad(conexion);
				loca.setKey(codigo);
				if (loca.exist()) {
					if (loca.remove()) {
						out.print("OK");
					} else {
						out.print(loca.getError());
					}
				} else {
					out.print("Localidad no existe " + codigo + " " + loca.getError());
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
				String departamento = "";
				if (request.getParameter("departamento") != null) {
					departamento = (String)request.getParameter("departamento");
				}
				conexion = new db();
				String sql = "select locacodi,locadesc,depadesc from localidad,departamentos where locadepa=depacodi";
				if (!departamento.equals("")) {
					sql += " and locadepa='" + departamento + "'";
				}
				sql += " order by depadesc,locadesc";
				ResultSet rs = conexion.Query(sql);
				if (rs.next()) { // existen departamentos
					out.println("<h2>Listado Localidades</h2>");
					out.println("<table>");
					out.println("<tr>");
					out.println("<th>Departamento</th>");
					out.println("<th>Codigo</th>");
					out.println("<th>Descripcion</th>");
					out.println("<th></th>");
					out.println("</tr>");
					do {
						out.println("<tr>");
						out.println("<td>" + rs.getString("depadesc") + "</td>");
						out.println("<td>" + rs.getString("locacodi") + "</td>");
						out.println("<td>" + rs.getString("locadesc") + "</td>");
						out.println("<td><a href=\"mod_localidad.jsp?codigo=" + rs.getString("locacodi") + "\" >Modificar</a><a href=\"Barrios.jsp?localidad=" + rs.getString("locacodi") +  "\">Barrios</a> <a href=\"RecursoLocalidad.jsp?localidad=" + rs.getString("locacodi") +  "\">Recurso</a></td>");
						out.println("</tr>");

					} while (rs.next());
					out.println("</table>");
				}else {
					out.println("<strong>No se encontraron registros. </strong>");
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
		
		if (operacion.equals("combo")) {
			String departamento = (String)request.getParameter("departamento");
			try {
				conexion = new db();
				String sql = "select locacodi,locadesc from localidad where locadepa='" + departamento + "'  order by locadesc";
				out.println("<select id='localidad' name='localidad'>");
				out.println("<option value=\"-1\">Seleccionar</option>");
				ResultSet rs = conexion.Query(sql);
				if (rs.next()) { // existen departamentos
					do {
						out.println("<option value='" + rs.getString("locacodi") + "'>" + rs.getString("locadesc") + "</option>");
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
