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
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class SrvListAssign
 */
@WebServlet("/SrvAsignar")
public class SrvAsignar extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SrvAsignar() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void ProcesarPeticion(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();

		HttpSession sesion = request.getSession();
		if (sesion.getAttribute("usuario") == null) {
			out.print("La sesion ha caducado... intente de nuevo por favor");
			return;
		}

		String operacion = (String) request.getParameter("operacion");
		db conexion = null;
		try {

			if (operacion.equals("list")) {

				String ciudad = (String) request.getParameter("localidad");
				String tipo = (String) request.getParameter("tipo");

				String sql = "select depadesc,locadesc,orden,orders.nom_via,nom_cli,ape1_cli,ape2_cli,"
						+ "orders.nom_calle,orders.placa,orders.crucero,orders.nic,"
						+ "orders.interior,tiordesc,ref_dir,nom_local,visita "
						+ "from orders,departamentos,localidad,tipo_orden "
						+ "where localidad.locadepa=depacodi "
						+ "and orders.localidad=locacodi "
						+ "and orders.localidad = ? "
						+ "and orders.estado != 99 "
						+ "and estado=1 and orders.tipo = tiorcodi ";
				if (!tipo.equals("all")) {
					sql += " and orders.tipo='" + tipo + "' ";
				}

				sql += "order by depadesc,locadesc,nom_local";

				conexion = new db();
				java.sql.PreparedStatement pst = conexion.getConnection()
						.prepareStatement(sql);
				pst.setString(1, ciudad);

				ResultSet rs = conexion.Query(pst);
				sql = "select distinct nom_local " + "from orders "
						+ "where localidad='" + ciudad + "' "
						+ "and orders.estado=1 ";
				if (!tipo.equals("all")) {
					sql += " and orders.tipo='" + tipo + "' ";
				}
				sql += "order by nom_local";
				ResultSet rsBarrios = conexion.Query(sql);
				boolean rsBarriosIsEmpty = !rsBarrios.next();
				out.println("<h2>Registros encontrados</h2>");
				out.println("<form name=\"form1\" action=\"\">");
				out.print("Personal: <input type='text' size=20 name='cedula' id='cedula' readonly /><input type='text' name='nombre' id='nombre' size=40 readonly/> <input type='button' name='cmd_buscar' id='cmd_buscar' onclick='javascript:BuscarPersonal()' value='Buscar Personal'  />");
				out.println("<input type=\"button\" class=\"boton\" name=\"cmd_asignar\" id=\"cmd_asignar\" onclick=\"javascript:validar();\" value=\"Asignar\">");
				out.println("<br />");
				out.println("Filtro por Barrio: ");
				out.println("<select name='cbo_barrio' id='cbo_barrio' >");
				if (!rsBarriosIsEmpty) {
					do {
						out.println("<option value=\""
								+ rsBarrios.getString("nom_local") + "\" >"
								+ rsBarrios.getString("nom_local")
								+ "</opcion>");
					} while (rsBarrios.next());
				}

				out.println("</select>");
				out.println("<input type='button' name='cmd_filtro' id='cmd_filtro' value='Filtrar' onclick=\"javascript:filtrar('"
						+ ciudad + "','" + tipo + "')\" />");
				out.println("<div id=\"error\"></div>");
				out.println("<div id='lista'>");
				out.println("<table>");
				out.println("<tr>");
				out.println("<th></th>");
				out.println("<th>Departamento</th>");
				out.println("<th>Municipio</th>");
				out.println("<th>Orden</th>");
				out.println("<th>Cliente</th>");
				out.println("<th>Direccion</th>");
				out.println("<th>Dir. Referencia</th>");
				out.println("<th>Barrio</th>");
				out.println("<th>Tipo Orden</th>");
				out.println("<th>Visitas</th>");
				out.println("<th>Accion</th>");
				out.println("</tr>");
				int cont = 0;
				if (rs.next()) { // existen departamentos
					do {
						out.println("<tr id=\"f" + rs.getString("orden")
								+ "\" " +(cont%2==0?"class='odd'":"") + ">");
						out.println("<td><input type=\"checkbox\" name=\"orden[]\" value=\""
								+ rs.getString("orden") + "\"></td>");
						out.println("<td>" + rs.getString("depadesc") + "</td>");
						out.println("<td>" + rs.getString("locadesc") + "</td>");
						out.println("<td>"+ rs.getString("orden") + "</td>");
						out.println("<td>" + rs.getString("nom_cli") + " "
								+ rs.getString("ape1_cli") + " "
								+ rs.getString("ape2_cli") + "</td>");
						out.println("<td>" + rs.getString("nom_via") + " "
								+ rs.getString("nom_calle") + " "
								+ rs.getString("crucero") + " "
								+ rs.getString("placa") + " "
								+ rs.getString("interior") + "</td>");
						out.println("<td>" + rs.getString("ref_dir") + "</td>");
						out.println("<td>" + rs.getString("nom_local")
								+ "</td>");
						out.println("<td>" + rs.getString("tiordesc") + "</td>");
						out.println("<td>" + rs.getString("visita") + "</td>");
						out.println("<td><a href=\"javascript:ShowMapa('"
								+ rs.getString("nic")
								+ "')\">Mapa</a></td>");
						out.println("</tr>");
						cont++;

					} while (rs.next());
					out.println("<tr>");
					out.println("<td colspan=\"11\">Total Ordenes: " + cont
							+ "</td>");
					out.println("</tr>");
				} else {
					out.println("<tr>");
					out.println("<td colspan=\"11\">No se encontraron registros</td>");
					out.println("</tr>");
				}
				out.println("</table>");
				out.println("</div>");
				out.println("<input type=\"button\" name=\"cmd_asignar\" id=\"cmd_asignar\" onclick=\"javascript:validar();\" value=\"Asignar\">");
				out.println("</form>");

			}

			if (operacion.equals("filtro")) {

				String ciudad = (String) request.getParameter("localidad");
				String barrio = (String) request.getParameter("barrio");
				String tipo = (String) request.getParameter("tipo");

				String sql = "select depadesc,locadesc,orden,orders.nom_via,nom_cli,ape1_cli,ape2_cli,"
						+ "orders.nom_calle,orders.placa,orders.crucero,"
						+ "orders.interior,tiordesc,ref_dir,nom_local,orders.nic,orders.visita "
						+ "from orders,departamentos,localidad,tipo_orden "
						+ "where localidad.locadepa=depacodi "
						+ "and orders.localidad=locacodi "
						+ "and orders.localidad = ? "
						+ "and estado=1 and orders.tipo = tiorcodi "
						+ "and orders.estado != 99 " + "and orders.nom_local=?";

				if (!tipo.equals("all")) {
					sql += " and orders.tipo='" + tipo + "' ";
				}

				sql += "order by depadesc,locadesc,nom_local";

				conexion = new db();
				java.sql.PreparedStatement pst = conexion.getConnection()
						.prepareStatement(sql);
				pst.setString(1, ciudad);
				pst.setString(2, barrio);
				ResultSet rs = conexion.Query(pst);

				out.println("<div id='lista'>");
				out.println("<table>");
				out.println("<tr>");
				out.println("<th></th>");
				out.println("<th>Departamento</th>");
				out.println("<th>Municipio</th>");
				out.println("<th>Orden</th>");
				out.println("<th>Cliente</th>");
				out.println("<th>Direccion</th>");
				out.println("<th>Dir. Referencia</th>");
				out.println("<th>Barrio</th>");
				out.println("<th>Tipo Orden</th>");
				out.println("<th>Vistas</th>");
				out.println("<th>Georeferencia</th>");
				out.println("</tr>");
				int cont = 0;
				if (rs.next()) { // existen departamentos
					do {
						out.println("<tr id=\"f" + rs.getString("orden")
								+ "\" " +(cont%2==0?"class='odd'":"") + ">");
						out.println("<td><input type=\"checkbox\" name=\"orden[]\" value=\""
								+ rs.getString("orden") + "\"></td>");
						out.println("<td>" + rs.getString("depadesc") + "</td>");
						out.println("<td>" + rs.getString("locadesc") + "</td>");
						out.println("<td>" + rs.getString("orden") + "</td>");
						out.println("<td>" + rs.getString("nom_cli") + " "
								+ rs.getString("ape1_cli") + " "
								+ rs.getString("ape2_cli") + "</td>");
						out.println("<td>" + rs.getString("nom_via") + " "
								+ rs.getString("nom_calle") + " "
								+ rs.getString("crucero") + " "
								+ rs.getString("placa") + " "
								+ rs.getString("interior") + "</td>");
						out.println("<td>" + rs.getString("ref_dir") + "</td>");
						out.println("<td>" + rs.getString("nom_local")
								+ "</td>");
						out.println("<td>" + rs.getString("tiordesc") + "</td>");
						out.println("<td>" + rs.getString("visita") + "</td>");
						out.println("<td><a href=\"javascript:ShowMapa('"
								+ rs.getString("nic")
								+ "')\">Mapa</a></td>");
						out.println("</tr>");
						cont++;

					} while (rs.next());
					out.println("<tr>");
					out.println("<td colspan=\"11\">Total Ordenes: " + cont
							+ "</td>");
					out.println("</tr>");
				} else {
					out.println("<tr>");
					out.println("<td colspan=\"11\">No se encontraron registros</td>");
					out.println("</tr>");
				}
				out.println("</table>");
				out.println("</div>");

			}
			
			if (operacion.equals("liberar")) {
				String departamento = (String)request.getParameter("departamento");
				String tipo = (String)request.getParameter("tipo");
				
				conexion = new db();
				if (tipo.equals("all")) {
					String sql = "UPDATE orders SET estado=1,recurso='-1' WHERE localidad IN (SELECT locacodi FROM localidad WHERE locadepa = ?) and (estado = '2')";
					java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
					pst.setString(1, departamento);
					if (conexion.Update(pst) > 0) {
						conexion.Commit();
						out.print("OK");
					}else {
						throw new SQLException("Error al procesar la solicitud, departamento: " + departamento + " Tipo: " + tipo);
					}
				}else {
					String sql = "UPDATE orders SET estado=1, recurso= '-1' WHERE localidad IN (SELECT locacodi FROM localidad WHERE locadepa = ?) and (estado='2') and (tipo = ?)";
					java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
					pst.setString(1, departamento);
					pst.setString(2, tipo);
					if (conexion.Update(pst) > 0) {
						conexion.Commit();
						out.print("OK");
					}else {
						throw new SQLException("Error al procesar la solicitud, departamento: " + departamento + " Tipo: " + tipo);
					}
				}
			}
			
			if (operacion.equals("asignar")) {
				String[] ordenes = request.getParameterValues("orden");
				String cedula = (String) request.getParameter("cedula");

				if (ordenes.length > 0) {
					conexion = new db();
					Ordenes orden = new Ordenes(conexion);
					int asignadas = 0;
					

					if (orden.CleanOrdenVisualizacion(cedula, false)) {
						for (int i = 0; i < ordenes.length; i++) {
							if (orden.AsignarOrdenVisualizacion(ordenes[i], cedula, i+1, false)) {
								asignadas++;
							}

						}
						if (asignadas == ordenes.length) {
							conexion.Commit();
							out.print("OK");
						} else {
							conexion.Rollback();
							throw new SQLException("Error al procesar las ordenes");
						}

					} else {
						conexion.Rollback();
						throw new Exception(
								"Error al limpiar orden de visualizacion");
					}

				} else {
					out.println("No hay ordenes seleccionadas");

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
