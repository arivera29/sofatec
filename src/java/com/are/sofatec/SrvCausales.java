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
@WebServlet("/SrvCausales")
public class SrvCausales extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SrvCausales() {
        super();
        // TODO Auto-generated constructor stub
    }

    protected void ProcesarPeticion(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
    	response.setContentType("text/html;charset=ISO-8859-1");
		PrintWriter out = response.getWriter();

		String operacion = (String) request.getParameter("operacion");
		db conexion = null;

		

		if (operacion.equals("add")) { // Agregar Causal
			String codigo = (String) request.getParameter("codigo");
			String descripcion = (String) request.getParameter("descripcion");
			String activo = (String) request.getParameter("activo");
			try {
				conexion = new db();
				Causales causal = new Causales(conexion);
				causal.setCodigo(codigo);
				causal.setDescripcion(descripcion);
				causal.setActivo(Integer.parseInt(activo));
				
				if (!causal.Find(codigo)) {
					if (causal.add()) {
						out.print("OK");
					} else {
						throw new Exception("Error al procesar la solicitud");
					}
				} else {
					throw new Exception("Causal ya se encuentra registrada. " + codigo);
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
			String activo = (String) request.getParameter("activo");
			String key = (String) request.getParameter("key");
			
			try {
				conexion = new db();
				Causales causal = new Causales(conexion);
				if (causal.Find(key)) {
					causal.setCodigo(codigo);
					causal.setDescripcion(descripcion);
					causal.setActivo(Integer.parseInt(activo));
					if (causal.modify(key)) {
						out.print("OK");
					} else {
						throw new Exception("Error al procesar la solicitud");
					}
				} else {
					throw new Exception("Causal no encontrada. " + key);
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
				Causales causal = new Causales(conexion);
				if (causal.Find(key)) {
					if (causal.remove(key)) {
						out.print("OK");
					} else {
						throw new Exception("Error al procesar la solicitud");
					}
				} else {
					throw new Exception("Causal no encontrada. " + key);
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
				Causales causales = new Causales(conexion);
				ResultSet rs = causales.List();
				
					out.println("<h2>LISTADO TIPO DE VISITA FALLIDA</h2>");
					out.println("<table>");
					out.println("<tr>");
					out.println("<th>CODIGO</th>");
					out.println("<th>DESCRIPCION</th>");
					out.println("<th>ACTIVO</th>");
					out.println("<th></th>");
					out.println("</tr>");
					int fila = 0;
					if (rs.next()) { // existen departamentos
						
					do {
						out.println("<tr " + (fila%2==0?"":"class='odd'") + ">");
						out.println("<td>" + rs.getString("causcodi") + "</td>");
						out.println("<td>" + rs.getString("causdesc") + "</td>");
						out.println("<td>" + (rs.getInt("causacti")==1?"Si":"No") + "</td>");
						out.println("<td><a href=mod_causal.jsp?codigo="
								+ rs.getString("causcodi")
								+ ">Editar</a></td>");
						out.println("</tr>");
						fila++;
					} while (rs.next());
					out.println("<tr><td colspan=3>Total registros: " + fila + "</td></tr>");
				}else {
					out.println("<tr><td colspan=3>No hay registros</td></tr>");
				}
					out.println("</table>");

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
				Causales causales = new Causales(conexion);
				ResultSet rs = causales.List();
				out.println("<select id='causal' name='causal'>");
				out.println("<option value=''>Seleccionar</option>");
				if (rs.next()) { // existen departamentos
					do {
						out.println("<option value='" + rs.getString("causcodi") + "'>" + rs.getString("causdesc") + "</option>");
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
