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
@WebServlet("/SrvEquipos")
public class SrvEquipos extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SrvEquipos() {
        super();
        // TODO Auto-generated constructor stub
    }

    protected void ProcesarPeticion(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
    	response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();

		String operacion = (String) request.getParameter("operacion");
		db conexion = null;

		

		if (operacion.equals("add")) { // Agregar PDA
			String imei = (String) request.getParameter("imei");
			String marca = (String) request.getParameter("marca");
			String estado = (String) request.getParameter("estado");
			String tipo = (String)request.getParameter("tipo");
			String foto = (String)request.getParameter("foto");
			String departamento = (String)request.getParameter("departamento");
			
			try {
				conexion = new db();
				Equipos eq = new Equipos(conexion);
				eq.setImei(imei);
				eq.setMarca(marca);
				eq.setActivo(Integer.parseInt(estado));
				eq.setTipo(Integer.parseInt(tipo));
				eq.setFoto(Integer.parseInt(foto));
				eq.setDepartamento(departamento);
				
				if (!eq.Find(imei)) {
					if (eq.add()) {
						out.print("OK");
					} else {
						throw new Exception("Error al procesar la solicitud");
					}
				} else {
					throw new Exception("IMEI ya se encuentra registrado. " + imei);
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



		if (operacion.equals("modify")) { // Modificar PDA
			String imei = (String) request.getParameter("imei");
			String marca = (String) request.getParameter("marca");
			String estado = (String) request.getParameter("estado");
			String tipo = (String)request.getParameter("tipo");
			String key = (String) request.getParameter("key");
			String foto = (String)request.getParameter("foto");
			String departamento = (String)request.getParameter("departamento");
			
			try {
				conexion = new db();
				Equipos eq = new Equipos(conexion);
				if (eq.Find(key)) {
					eq.setImei(imei);
					eq.setMarca(marca);
					eq.setActivo(Integer.parseInt(estado));
					eq.setTipo(Integer.parseInt(tipo));
					eq.setFoto(Integer.parseInt(foto));
					eq.setDepartamento(departamento);
					if (eq.modify(key)) {
						out.print("OK");
					} else {
						throw new Exception("Error al procesar la solicitud");
					}
				} else {
					throw new Exception("IMEI no encontrado. " + key);
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
		
		
		if (operacion.equals("remove")) { // Eliminar PDA

			String key = (String) request.getParameter("key");
			
			try {
				conexion = new db();
				Equipos eq = new Equipos(conexion);
				if (eq.Find(key)) {
					if (eq.remove(key)) {
						out.print("OK");
					} else {
						throw new Exception("Error al procesar la solicitud");
					}
				} else {
					throw new Exception("IMEI no encontrado. " + key);
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
		
		if (operacion.equals("asignar")) { // Asignar PDA a Recurso

			String imei = (String) request.getParameter("imei");
			String recurso = (String) request.getParameter("recurso");
			
			try {
				conexion = new db();
				Equipos eq = new Equipos(conexion);
				if (eq.Find(imei)) {
					if (eq.asignarPDA(imei, recurso)) {
						out.print("OK");
					} else {
						throw new Exception("Error al procesar la solicitud");
					}
				} else {
					throw new Exception("IMEI no encontrado. " + imei);
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
		
		if (operacion.equals("liberar")) { // Eliminar PDA

			String imei = (String) request.getParameter("imei");
			
			try {
				conexion = new db();
				Equipos eq = new Equipos(conexion);
				if (eq.Find(imei)) {
					if (eq.liberarPDA(imei)) {
						out.print("OK");
					} else {
						throw new Exception("Error al procesar la solicitud");
					}
				} else {
					throw new Exception("IMEI no encontrado. " + imei);
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

		if (operacion.equals("list")) {  // Listado de PDA's
			try {
				conexion = new db();
				String criterio = (String)request.getParameter("criterio");
				String sql = "select imei,marca,marcdesc,estado,recunomb " +
						"from equipos,marcas,recurso " +
						"where marca=marccodi " +
						"and recurso=recucodi " +
						"and (imei like ? OR marcdesc like ? OR recunomb like ?) " +
						"order by marcdesc,imei";
				java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
				pst.setString(1, "%" + criterio + "%");
				pst.setString(2, "%" + criterio + "%");
				pst.setString(3, "%" + criterio + "%");
				ResultSet rs = conexion.Query(pst);
				
					out.println("<h2>Listado Smartphones</h2>");
					out.println("<table>");
					out.println("<tr>");
					out.println("<th>Marca</th>");
					out.println("<th>IMEI</th>");
					out.println("<th>Estado</th>");
					out.println("<th>Asignada</th>");
					out.println("<th></th>");
					out.println("</tr>");
					if (rs.next()) { // existen departamentos
					int fila = 0;
					do {
						out.println("<tr "  +(fila%2==0?"class='odd'":"") + ">");
						out.println("<td>" + rs.getString("marcdesc") + "</td>");
						out.println("<td>" + rs.getString("imei") + "</td>");
						out.println("<td>" + rs.getString("estado") + "</td>");
						out.println("<td>" + rs.getString("recunomb") + "</td>");
						out.println("<td><a href=mod_equipo.jsp?codigo="
								+ rs.getString("imei")
								+ ">Editar</a>  <a href=\"asignarpda.jsp?codigo=" + rs.getString("imei") +"\">Asignar</a></td>");
						out.println("</tr>");
						fila++;
					} while (rs.next());
					
				}else {
					out.println("<tr><td colspan=5>No se encontraron registros</td></tr>");
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
				String sql = "select imei from equipos where estado=1 order by imei";
				out.println("<select id='equipo' name='equipo'>");
				out.println("<option value=''>Seleccionar</option>");
				ResultSet rs = conexion.Query(sql);
				if (rs.next()) { // existen PDA
					do {
						out.println("<option value='" + rs.getString("imei") + "'>" + rs.getString("imei") + "</option>");
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
