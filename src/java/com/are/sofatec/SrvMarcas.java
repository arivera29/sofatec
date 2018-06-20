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
@WebServlet("/SrvMarcas")
public class SrvMarcas extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SrvMarcas() {
        super();
        // TODO Auto-generated constructor stub
    }

    protected void ProcesarPeticion(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
    	response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();

		String operacion = (String) request.getParameter("operacion");
		db conexion = null;

		

		if (operacion.equals("add")) { // Agregar Marca
			String codigo = (String) request.getParameter("codigo");
			String descripcion = (String) request.getParameter("descripcion");
			try {
				conexion = new db();
				Marcas marca = new Marcas(conexion);
				marca.setCodigo(codigo);
				marca.setDescripcion(descripcion);
				if (!marca.Find(codigo)) {
					if (marca.add()) {
						out.print("OK");
					} else {
						throw new Exception("Error al procesar la solicitud");
					}
				} else {
					throw new Exception("Marca ya se encuentra registrada. " + codigo);
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

		}



		if (operacion.equals("modify")) { // Modificar Tipo de Orden
			String codigo = (String) request.getParameter("codigo");
			String descripcion = (String) request.getParameter("descripcion");
			String key = (String) request.getParameter("key");
			
			try {
				conexion = new db();
				Marcas marca = new Marcas(conexion);
				if (marca.Find(key)) {
					marca.setCodigo(codigo);
					marca.setDescripcion(descripcion);
					if (marca.modify(key)) {
						out.print("OK");
					} else {
						throw new Exception("Error al procesar la solicitud");
					}
				} else {
					throw new Exception("Marca no encontrada. " + key);
				}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				out.print("Error de conexion con el servidor: "
						+ e.getMessage());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				out.print("Error:  "
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
		
		
		if (operacion.equals("remove")) { // Eliminar Tipo de Orden

			String key = (String) request.getParameter("key");
			
			try {
				conexion = new db();
				Marcas marca = new Marcas(conexion);
				if (marca.Find(key)) {
					if (marca.remove(key)) {
						out.print("OK");
					} else {
						throw new Exception("Error al procesar la solicitud");
					}
				} else {
					throw new Exception("Marca no encontrada. " + key);
				}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				out.print("Error de conexion con el servidor: "
						+ e.getMessage());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				out.print("Error:  "
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
		
		

		if (operacion.equals("list")) {  // Listado de Tipo de ordenes
			try {
				conexion = new db();
				String sql = "select marccodi,marcdesc from marcas order by marcdesc";
				ResultSet rs = conexion.Query(sql);
				if (rs.next()) { 
					out.println("<h2>Listado Marcas</h2>");
					out.println("<table>");
					out.println("<tr>");
					out.println("<th>CODIGO</th>");
					out.println("<th>DESCRIPCION</th>");
					out.println("<th>ACCION</th>");
					out.println("</tr>");
					int fila = 1;
					do {
						out.println("<tr "  +(fila%2==0?"class='odd'":"") + ">");
						out.println("<td>" + rs.getString("marccodi") + "</td>");
						out.println("<td>" + rs.getString("marcdesc") + "</td>");
						out.println("<td><a href=mod_marca.jsp?codigo="
								+ rs.getString("marccodi")
								+ ">Editar</a></td>");
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
			

		}

		
		if (operacion.equals("combo")) { // combo de tipos de orden
			try {
				conexion = new db();
				String sql = "select marccodi,marcdesc from marcas order by marcdesc";
				out.println("<select id='marca' name='marca'>");
				out.println("<option value=''>Seleccionar</option>");
				ResultSet rs = conexion.Query(sql);
				if (rs.next()) { // existen departamentos
					do {
						out.println("<option value='" + rs.getString("marccodi") + "'>" + rs.getString("marcdesc") + "</option>");
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
