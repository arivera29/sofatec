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
@WebServlet("/SrvClientes")
public class SrvClientes extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SrvClientes() {
        super();
        // TODO Auto-generated constructor stub
    }

    protected void ProcesarPeticion(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
    	response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();

		String operacion = (String) request.getParameter("operacion");
		db conexion = null;

		try {

		if (operacion.equals("add")) { // Agregar Cliente
			String nic = (String) request.getParameter("nic");
			String latitud = (String) request.getParameter("latitud");
			String longitud = (String) request.getParameter("longitud");

				conexion = new db();
				Clientes cliente = new Clientes(conexion);
				cliente.setNic(nic);
				cliente.setLongitud(longitud);
				cliente.setLatitud(latitud);
				
				if (!cliente.Find(nic)) {
					if (cliente.add()) {
						out.print("OK");
					} else {
						throw new Exception("Error al procesar la solicitud");
					}
				} else {
					throw new Exception("Cliente ya se encuentra registrado. " + nic);
				}

			
		}



		if (operacion.equals("modify")) { // Modificar Tipo de Orden
			String nic = (String) request.getParameter("nic");
			String latitud = (String) request.getParameter("latitud");
			String longitud = (String) request.getParameter("longitud");
			String key = (String) request.getParameter("key");

				conexion = new db();
				Clientes cliente = new Clientes(conexion);
				if (cliente.Find(key)) {
					cliente.setNic(nic);
					cliente.setLongitud(longitud);
					cliente.setLatitud(latitud);
					if (cliente.modify(key)) {
						out.print("OK");
					} else {
						throw new Exception("Error al procesar la solicitud");
					}
				} else {
					throw new Exception("Cliente no encontrado. " + key);
				}
		}
		
		if (operacion.equals("UpdateInfo")) { // Modificar Tipo de Orden
			String nic = (String) request.getParameter("nic");
			String cliente_agresivo = (String) request.getParameter("cliente_agresivo");
			String predio_enrejado = (String) request.getParameter("predio_enrejado");
			String conf_especial = (String) request.getParameter("conf_especial");
			String med_alto = (String) request.getParameter("med_alto");
			
			String key = (String) request.getParameter("key");

				conexion = new db();
				Clientes cliente = new Clientes(conexion);
				if (cliente.Find(key)) {
					if (cliente.UpdateInfo(key, cliente_agresivo, predio_enrejado,conf_especial,med_alto)) {
						out.print("OK");
					} else {
						throw new Exception("Error al procesar la solicitud");
					}
				} else {
					cliente.setNic(nic);
					cliente.setLongitud("0");
					cliente.setLatitud("0");
					cliente.setClienteAgresivo(cliente_agresivo);
					cliente.setPredioEnrejado(predio_enrejado);
					cliente.setConf_especial(conf_especial);
					cliente.setMed_alto(med_alto);
					if (cliente.add()) {
						out.print("OK");
					}else {
						throw new Exception("Error al procesar la solicitud");
					}
				}
		}

		if (operacion.equals("UpdateUbicacion")) { // Modificar Tipo de Orden
			String nic = (String) request.getParameter("nic");
			String latitud = (String) request.getParameter("latitud");
			String longitud = (String) request.getParameter("longitud");
			String key = (String) request.getParameter("key");

				conexion = new db();
				Clientes cliente = new Clientes(conexion);
				if (cliente.Find(key)) {
					cliente.setNic(nic);
					cliente.setLongitud(longitud);
					cliente.setLatitud(latitud);
					if (cliente.UpdateUbicacion(key, latitud, longitud)) {
						out.print("OK");
					} else {
						throw new Exception("Error al procesar la solicitud");
					}
				} else {
					cliente.setNic(nic);
					cliente.setLongitud(longitud);
					cliente.setLatitud(latitud);
					cliente.setClienteAgresivo("0");
					cliente.setPredioEnrejado("0");
					if (cliente.add()) {
						out.print("OK");
					}else {
						throw new Exception("Error al procesar la solicitud");
					}
				}
		}
		
		if (operacion.equals("remove")) { // Eliminar Tipo de Orden
			String key = (String) request.getParameter("key");
			conexion = new db();
				Clientes cliente = new Clientes(conexion);
				if (cliente.Find(key)) {
					if (cliente.remove(key)) {
						out.print("OK");
					} else {
						throw new Exception("Error al procesar la solicitud");
					}
				} else {
					throw new Exception("Cliente no encontrado. " + key);
				}
		}
		
		

		if (operacion.equals("list")) {  // Listado de Tipo de ordenes
			String criterio = (String)request.getParameter("criterio");
			criterio = "%" + criterio + "%";

				conexion = new db();
				String sql = "select clientes.* from clientes where nic like ? order by nic limit 50";
				// Aplicando criterio de busqueda
				java.sql.PreparedStatement pst =  conexion.getConnection().prepareStatement(sql);
				pst.setString(1, criterio);
				ResultSet rs = conexion.Query(pst);
				
					out.println("<h2>Registros encontrados</h2>");
					out.println("<table>");
					out.println("<tr>");
					out.println("<th>NIC</th>");
					out.println("<th>Latitud</th>");
					out.println("<th>Longitud</th>");
					out.println("<th></th>");
					out.println("</tr>");
					if (rs.next()) { // existen registros
					do {
						out.println("<tr>");
						out.println("<td>" + rs.getString("nic") + "</td>");
						out.println("<td>" + rs.getString("latitud") + "</td>");
						out.println("<td>" + rs.getString("longitud") + "</td>");
						out.println("<td><a href=mod_cliente.jsp?codigo="
								+ rs.getString("nic")
								+ ">Modificar</a></td>");
						out.println("</tr>");

					} while (rs.next());
					
				}else {
					out.println("<tr>");
					out.println("<td colspan=\"4\">No se encontraron registros con el criterio=" + criterio + "</td>");	
					out.println("</tr>");
				}
					out.println("</table>");

			
			

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
