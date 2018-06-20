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
 * Servlet implementation class srvCargos
 */
@WebServlet("/SrvOrders")
public class SrvOrders extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SrvOrders() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void ProcesarPeticion(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();

		HttpSession sesion = request.getSession();
		if (sesion.getAttribute("usuario") == null) {
			out.print("La sesión ha caducado... intente de nuevo por favor");
			return;
		}

		String operacion = (String) request.getParameter("operacion");

		db conexion = null;
		try {

			if (operacion.equals("add")) { // Agregar orden
				String num_orden = (String) request.getParameter("orden");
				String nic = (String) request.getParameter("nic");
				String departamento = (String) request
						.getParameter("departamento");
				String municipio = (String) request.getParameter("municipio");
				String direccion = (String) request.getParameter("direccion");
				String nis = (String) request.getParameter("nis");
				String tipo = (String) request.getParameter("tipo");
				String fecha = (String) request.getParameter("fecha");

				conexion = new db();
				Orders orden = new Orders(conexion);
				orden.setNic(nic);
				orden.setOrden(num_orden);
				orden.setNis(nis);
				orden.setDepartamento(departamento);
				orden.setMunicipio(municipio);
				orden.setLocalidad("");
				orden.setFecha_generacion(fecha);
				orden.setDireccion(direccion);
				orden.setRef_direccion("");
				orden.setTipo(tipo);
				orden.setUsuario((String) sesion.getAttribute("usuario"));

				if (!orden.ExistOrder(num_orden)) {
					if (orden.add()) {
						out.print("OK");
					} else {
						throw new Exception("Error al procesar la solicitud");
					}
				} else {
					throw new Exception("Orden ya se encuentra registrada. "
							+ num_orden);
				}

			}

			if (operacion.equals("modify")) { // Modificar Tipo de Orden
				String num_orden = (String) request.getParameter("orden");
				String nic = (String) request.getParameter("nic");
				String departamento = (String) request
						.getParameter("departamento");
				String municipio = (String) request.getParameter("municipio");
				String direccion = (String) request.getParameter("direccion");
				String nis = (String) request.getParameter("nis");
				String tipo = (String) request.getParameter("tipo");
				String fecha = (String) request.getParameter("fecha");
				String key = (String) request.getParameter("key");

				conexion = new db();
				Orders orden = new Orders(conexion);
				if (orden.ExistOrder(key)) {
					orden.setOrden(num_orden);
					orden.setNis(nis);
					orden.setNic(nic);
					orden.setDepartamento(departamento);
					orden.setMunicipio(municipio);
					orden.setLocalidad("");
					orden.setFecha_generacion(fecha);
					orden.setDireccion(direccion);
					orden.setRef_direccion("");
					orden.setTipo(tipo);
					orden.setUsuario((String) sesion.getAttribute("usuario"));
					if (orden.modify(key)) {
						out.print("OK");
					} else {
						throw new Exception("Error al procesar la solicitud");
					}
				} else {
					throw new Exception("Orden no encontrada. " + key);
				}

			}

			if (operacion.equals("remove")) { // Eliminar Tipo de Orden

				String key = (String) request.getParameter("key");

				conexion = new db();
				Orders orden = new Orders(conexion);
				if (orden.Find(key)) {
					if (orden.remove(key)) {
						out.print("OK");
					} else {
						throw new Exception("Error al procesar la solicitud");
					}
				} else {
					throw new Exception("Orden no encontrada. " + key);
				}

			}

			if (operacion.equals("buscar")) {
				String criterio = (String) request.getParameter("criterio");
				String opcion = (String) request.getParameter("opcion");

				String sql = "select depadesc,locadesc,orden,orders.nic,nom_cli,"
						+ "orders.ape1_cli,orders.ape2_cli,orders.nom_via,"
						+ "orders.nom_calle,orders.crucero,orders.placa,"
						+ "orders.interior,tiordesc,orders.num_apa,estadesc "
						+ "from orders,departamentos,localidad,tipo_orden,estados "
						+ "where localidad.locadepa=depacodi "
						+ "and orders.localidad=locacodi "
						+ "and orders.tipo=tiorcodi "
						+ "and estado = estacodi "
						+ " and (orden = ? OR nic = ? OR num_apa = ?)";

				if (opcion.equals("assign")) {
					sql += " and orders.estado != 99 ";
				}
				
				/*if (opcion.equals("visita")) {  // estado pendiente
					sql += " and orders.estado = 1 ";
				}*/

				sql += "order by depadesc,locadesc,orden,nic";

				conexion = new db();
				java.sql.PreparedStatement pst = conexion.getConnection()
						.prepareStatement(sql);

				pst.setString(1, criterio.trim());
				pst.setString(2, criterio.trim());
				pst.setString(3, criterio.trim());

				ResultSet rs = conexion.Query(pst);
				out.println("<h2>Registros encontrados</h2>");
				out.println("<table>");
				out.println("<tr>");
				out.println("<th>Departamento</th>");
				out.println("<th>Municipio</th>");
				out.println("<th>Orden</th>");
				out.println("<th>NIC</th>");
				out.println("<th>Cliente</th>");
				out.println("<th>Direccion</th>");
				out.println("<th>Tipo Orden</th>");
				out.println("<th>Medidor</th>");
				out.println("<th>Estado</th>");
				out.println("<th></th>");
				out.println("</tr>");
				if (rs.next()) { // existen departamentos
					do {
						out.println("<tr>");
						out.println("<td>" + rs.getString("depadesc") + "</td>");
						out.println("<td>" + rs.getString("locadesc") + "</td>");
						out.println("<td>" + rs.getString("orden") + "</td>");
						out.println("<td>" + rs.getString("nic") + "</td>");
						out.println("<td>" + rs.getString("nom_cli") + " "
								+ rs.getString("ape1_cli") + " "
								+ rs.getString("ape2_cli") + "</td>");
						out.println("<td>" + rs.getString("nom_via") + " "
								+ rs.getString("nom_calle") + " "
								+ rs.getString("crucero") + " "
								+ rs.getString("placa") + " "
								+ rs.getString("interior") + "</td>");
						out.println("<td>" + rs.getString("tiordesc") + "</td>");
						out.println("<td>" + rs.getString("num_apa") + "</td>");
						out.println("<td>" + rs.getString("estadesc") + "</td>");
						out.println("<td>");
						if (opcion.equals("modify")) { // Modificar orden
							out.print("<a href=ModifyOrder.jsp?codigo="
									+ rs.getString("orden") + ">Modificar</a>");
						}
						if (opcion.equals("assign")) { // Asignar orden
							out.print("<a href=AssignOrder.jsp?codigo="
									+ rs.getString("orden") + ">Asignar</a>");
						}
						if (opcion.equals("georeferencia")) { // Consulta
																// Georeferencia
							out.print("<a href=ViewOrder.jsp?codigo="
									+ rs.getString("orden")
									+ ">Georeferencia</a>");
						}
						if (opcion.equals("consulta")) { // Georeferencia
															
							out.print("<a href=ViewOrder.jsp?codigo="
									+ rs.getString("orden") + ">Consultar</a>");
						}
						if (opcion.equals("visita")) { // visita
							
							out.print("<a href=RegVisitaFallida.jsp?codigo="
								+ rs.getString("orden") + ">Visita Fallida</a>");
						}
						out.print("</td>");
						out.println("</tr>");

					} while (rs.next());
				} else {
					out.println("<tr>");
					out.println("<td colspan=\"10\">No se encontraron registros con el criterio="
							+ criterio + "</td>");
					out.println("</tr>");
				}

			}

			if (operacion.equals("asignar")) { // Asignar Orden a Recurso
				String orden = (String) request.getParameter("orden");
				String cedula = (String) request.getParameter("cedula");
				String sms = (String) request.getParameter("sms");
				int num_show = 0;
				if (request.getParameter("num_show") != null) {
					num_show = Integer.parseInt((String) request
							.getParameter("num_show"));
				}
				conexion = new db();
				Ordenes Orden = new Ordenes(conexion);

				String ra = Orden.AsignacionActual(orden);
				if (ra.equals(cedula)) {
					throw new Exception(
							"Ya se encuentra asignada al mismo recurso");
				}
				Orden.setUsuario((String) sesion.getAttribute("usuario"));
				if (Orden.AsignarRecurso(orden, cedula, num_show)) {
					if (sms.equals("S")) {
						Sms s = new Sms(conexion);
						s.enviar(cedula, orden);
					}
					out.print("OK");
				} else {
					out.print("Error al procesar la solicitud");
				}

			}

			if (operacion.equals("liberar")) { // liberar Orden
				String orden = (String) request.getParameter("orden");

				conexion = new db();
				Orders Orden = new Orders(conexion);

				if (Orden.Liberar(orden)) {
					out.print("OK");
				} else {
					out.print("Error al procesar la solicitud");
				}

			}
			
			if (operacion.equals("liberar_tecnico")) { // liberar Orden
				String cedula = (String) request.getParameter("cedula");

				conexion = new db();
				Orders Orden = new Orders(conexion);

				if (Orden.LiberarTecnico(cedula)) {
					out.print("<strong>Ordenes liberadas correctamente</strong>");
				} else {
					out.print("Error al procesar la solicitud");
				}

			}

			if (operacion.equals("lista_ejecucion")) {

				String recurso = (String) request.getParameter("recurso");
				String fecha = (String) request.getParameter("fecha");

				String sql = "select depadesc,locadesc,orders.orden,orders.nom_via,nom_cli,ape1_cli,ape2_cli,"
						+ "orders.nom_calle,orders.placa,orders.crucero,orders.nic,"
						+ "orders.interior,tiordesc,ref_dir,nom_local,visita,reportes.tipo tipo_reporte,reportes.fecha fecha "
						+ "from orders,departamentos,localidad,tipo_orden,reportes "
						+ "where localidad.locadepa=depacodi "
						+ "and orders.localidad=locacodi "
						+ "and date(reportes.fecha) = ? "
						+ "and orders.orden = reportes.orden "
						+ "and reportes.recurso = ? "
						+ "and orders.tipo = tiorcodi "
						+ "order by reportes.fecha ";

				conexion = new db();
				java.sql.PreparedStatement pst = conexion.getConnection()
						.prepareStatement(sql);
				pst.setString(1, fecha);
				pst.setString(2, recurso);

				ResultSet rs = conexion.Query(pst);
				out.println("<h2>Ordenes ejecutadas en " + fecha + "</h2>");

				out.println("<div id='lista'>");
				out.println("<table>");
				out.println("<tr>");
				out.println("<th>Departamento</th>");
				out.println("<th>Municipio</th>");
				out.println("<th>Orden</th>");
				out.println("<th>Cliente</th>");
				out.println("<th>Direccion</th>");
				out.println("<th>Dir. Referencia</th>");
				out.println("<th>Barrio</th>");
				out.println("<th>Tipo Orden</th>");
				out.println("<th>Visitas</th>");
				out.println("<th>fecha cierre</th>");
				out.println("<th>Tipo cierre</th>");
				out.println("</tr>");
				int cont = 0;
				if (rs.next()) { // existen departamentos
					do {
						out.println("<tr id=\"f" + rs.getString("orden")
								+ "\">");
						out.println("<td>" + rs.getString("depadesc") + "</td>");
						out.println("<td>" + rs.getString("locadesc") + "</td>");
						out.println("<td><a href=\"ViewOrder.jsp?codigo="
								+ rs.getString("orden")
								+ "\" target=\"_blank\">"
								+ rs.getString("orden") + "</a></td>");
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
						out.println("<td>" + rs.getString("fecha") + "</td>");
						out.println("<td>" + rs.getString("tipo_reporte")
								+ "</td>");
						out.println("</tr>");
						cont++;

					} while (rs.next());
					out.println("<tr>");
					out.println("<td colspan=\"12\">Total Ordenes: " + cont
							+ "</td>");
					out.println("</tr>");
				} else {
					out.println("<tr>");
					out.println("<td colspan=\"12\">No se encontraron registros</td>");
					out.println("</tr>");
				}
				out.println("</table>");
				out.println("</div>");

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
