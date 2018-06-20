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
@WebServlet("/SrvActividades")
public class SrvActividades extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SrvActividades() {
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

		if (operacion.equals("add")) { // Agregar Marca
			String codigo = (String) request.getParameter("codigo");
			String descripcion = (String) request.getParameter("descripcion");
			String activo = (String)request.getParameter("activo");
			String valor = (String)request.getParameter("valor");
			String accion = (String)request.getParameter("accion");

				conexion = new db();
				Actividades actividad = new Actividades(conexion);
				actividad.setCodigo(codigo);
				actividad.setDescripcion(descripcion);
				actividad.setActivo(Integer.parseInt(activo));
				actividad.setValor(Double.parseDouble(valor));
				actividad.setAccion(accion);
				
				if (!actividad.Find(codigo)) {
					if (actividad.add()) {
						out.print("OK");
					} else {
						throw new Exception("Error al procesar la solicitud");
					}
				} else {
					throw new Exception("Actividad ya se encuentra registrada. " + codigo);
				}
		}



		if (operacion.equals("modify")) { // Modificar Tipo de Orden
			String codigo = (String) request.getParameter("codigo");
			String descripcion = (String) request.getParameter("descripcion");
			String activo = (String)request.getParameter("activo");
			String valor = (String)request.getParameter("valor");
			String accion = (String)request.getParameter("accion");
			String key = (String) request.getParameter("key");
			
				conexion = new db();
				Actividades actividad = new Actividades(conexion);
				if (actividad.Find(key)) {
					actividad.setCodigo(codigo);
					actividad.setDescripcion(descripcion);
					actividad.setActivo(Integer.parseInt(activo));
					actividad.setValor(Double.parseDouble(valor));
					actividad.setAccion(accion);
					if (actividad.modify(key)) {
						out.print("OK");
					} else {
						throw new Exception("Error al procesar la solicitud");
					}
				} else {
					throw new Exception("Actividad no encontrada. " + key);
				}

			

		}
		
		
		if (operacion.equals("remove")) { // Eliminar Tipo de Orden

			String key = (String) request.getParameter("key");
			

				conexion = new db();
				Actividades actividad = new Actividades(conexion);
				if (actividad.Find(key)) {
					if (actividad.remove(key)) {
						out.print("OK");
					} else {
						throw new Exception("Error al procesar la solicitud");
					}
				} else {
					throw new Exception("Actividad no encontrada. " + key);
				}

			

		}
		
		

		if (operacion.equals("list")) {  // Listado de Tipo de ordenes

				conexion = new db();
				Actividades actividades = new Actividades(conexion);
				ResultSet rs = actividades.List();
				
					out.println("<h2>LISTADO ACTIVIDADES</h2>");
					out.println("<table>");
					out.println("<tr>");
					out.println("<th>CODIGO</th>");
					out.println("<th>DESCRIPCION</th>");
					out.println("<th>VALOR</th>");
					out.println("<th>ACCION</th>");
					out.println("<th>ACTIVO</th>");
					out.println("<th></th>");
					out.println("</tr>");
					int fila = 0;
					if (rs.next()) { // existen departamentos
						
					do {
						out.println("<tr "  +(fila%2==0?"class='odd'":"") + ">");
						out.println("<td>" + rs.getString("acticodi") + "</td>");
						out.println("<td>" + rs.getString("actidesc") + "</td>");
						out.println("<td>" + rs.getString("activalo") + "</td>");
						out.println("<td>" + rs.getString("actitiac") + "</td>");
						out.println("<td>" + (rs.getInt("actiesta")==1?"Si":"No") + "</td>");
						
						out.println("<td><a href=mod_actividad.jsp?codigo="
								+ rs.getString("acticodi")
								+ ">Editar</a></td>");
						out.println("</tr>");
						fila++;
					} while (rs.next());
					
				}else {
					out.println("<tr><td colspan=5>No se encontraron registros</td></tr>");
				}
					out.println("</table>");

			
			

		}

		
		if (operacion.equals("combo")) { // combo de tipos de orden

				conexion = new db();
				out.println("<select id='marca' name='marca'>");
				out.println("<option value=''>Seleccionar</option>");
				Actividades actividades = new Actividades(conexion);
				ResultSet rs = actividades.List();
				if (rs.next()) { // existen departamentos
					do {
						out.println("<option value='" + rs.getString("acticodi") + "'>" + rs.getString("actidesc") + "</option>");
					} while (rs.next());
					
				}
				out.println("</select>");
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
