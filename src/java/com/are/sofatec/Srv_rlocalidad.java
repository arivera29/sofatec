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
 * Servlet implementation class Srv_rlocalidad
 */
@WebServlet("/Srv_rlocalidad")
public class Srv_rlocalidad extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Srv_rlocalidad() {
        super();
        // TODO Auto-generated constructor stub
    }

    protected void ProcesarPeticion(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
    	response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		String operacion = (String)request.getParameter("operacion");
		
		if (operacion.equals("agregarrecurso")) {
			String recurso = (String)request.getParameter("recurso");
			String localidad = (String)request.getParameter("localidad");
			db conexion  = null;
			try {
				conexion = new db();
				String sql = "insert into recurso_localidad (recurso,localidad) values ('" + recurso + "','" + localidad + "')";
				if (conexion.Update(sql)>0) {
					conexion.Commit();
					out.print("OK");
				}else {
					out.print("ERROR.  No se pudo agregar el registro, intente de nuevo por favor");
				}
				
			}catch (SQLException e) {
				out.print("ERROR. " + e.getMessage());
			}finally {
				if (conexion != null)
					try {
						conexion.Close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						out.print("ERROR." + e.getMessage());
					}
			}
				
			
		}
		
		if (operacion.equals("eliminarrecurso")) {
			String recurso = (String)request.getParameter("recurso");
			String localidad = (String)request.getParameter("localidad");
			db conexion  = null;
			try {
				conexion = new db();
				String sql = "delete from recurso_localidad where recurso='" + recurso + "' and localidad='" + localidad + "'";
				if (conexion.Update(sql)>0) {
					conexion.Commit();
					out.print("OK");
				}else {
					out.print("ERROR.  No se pudo eliminar el registro, intente de nuevo por favor");
				}
				
			}catch (SQLException e) {
				out.print("ERROR. " + e.getMessage());
			}finally {
				if (conexion != null)
					try {
						conexion.Close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						out.print("ERROR." + e.getMessage());
					}
			}
				
			
		}
		
		if (operacion.equals("list")) {
			String localidad = (String)request.getParameter("localidad");
			db conexion = null;
			try {
				conexion = new db();
				String sql = "select recurso,recunomb,cargdesc from recurso_localidad,recurso,cargos where recurso=recucodi and localidad='" + localidad + "' and recucarg=cargcodi order by recunomb";
				ResultSet rs = conexion.Query(sql);
				
					out.println("<table>");
					out.println("<tr>");
					out.println("<th>Codigo</th>");
					out.println("<th>Nombre</th>");
					out.println("<th>Cargo</th>");
					out.println("<th></th>");
					out.println("</tr>");
					int reg = 0;
					if (rs.next()) { // existen departamentos
					do {
						out.println("<tr id=\"fila_" + reg +"\">");
						out.println("<td>" + rs.getString("recurso") + "</td>");
						out.println("<td>" + rs.getString("recunomb") + "</td>");
						out.println("<td>" + rs.getString("cargdesc") + "</td>");
						out.println("<td><input type=\"button\" onclick=\"javascript:eliminar('" + rs.getString("recurso") + "','" + localidad + "','fila_" + reg +"')\" value=\"Eliminar\" name=\"cmd_eliminar\" /></td>");
						out.println("</tr>");
						reg++;

					} while (rs.next());
					}
					out.println("</tr><td colspan=4>Total registros:" + reg + "</td></tr>");
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
			/* Fin operacion listar departamentos */

		}
			
		
		
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
