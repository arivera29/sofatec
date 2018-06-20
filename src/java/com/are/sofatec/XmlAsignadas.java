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
 * Servlet implementation class XmlSupervisor
 */
@WebServlet("/XmlAsignadas")
public class XmlAsignadas extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public XmlAsignadas() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    protected void ProcesarPeticion(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		String imei = (String)request.getParameter("imei");
		out.println("<?xml version=\"1.0\" ?>");
		out.println("<supervisor>");
		
		db conexion = null;
		
		try {
			conexion = new db();
			Equipos equipo = new Equipos(conexion);
			if (equipo.Find(imei)) {
				if (equipo.getActivo() == 1 && !equipo.getRecurso().equals("-1")) {
					
					recursohumano recurso = new recursohumano(conexion);
					if (recurso.isSupervisor(equipo.getRecurso())) {
						String sql = "select recucodi,recunomb,count(orders.orden) cnt_asignadas,sum(orders.confirm) cnt_confirmadas from recurso LEFT OUTER JOIN orders ON (recurso.recucodi = orders.recurso AND orders.estado = 2 )where recucesu=? group by recucodi";
						java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
						pst.setString(1, equipo.getRecurso());
						ResultSet rs = conexion.Query(pst);
						while (rs.next()) {
							out.println("<tecnico>");
							out.println("<codigo>" + rs.getString("recucodi") + "</codigo>");
							out.println("<nombre>" + rs.getString("recunomb") + "</nombre>");
							out.println("<asignadas>" + (rs.getString("cnt_asignadas")==null?"0":rs.getString("cnt_asignadas")) + "</asignadas>");
							out.println("<confirmadas>" + (rs.getString("cnt_confirmadas")==null?"0":rs.getString("cnt_confirmadas")) + "</confirmadas>");
							out.println("</tecnico>");
							
						}
						
						
						
					}else {
						out.println("<tecnico>");
						out.println("<error>Equipo no se encuentra asignado a un supervisor</error>");
						out.println("</tecnico>");
					}
				
				
				}else { // Equipo se encuentra inactivo
					out.println("<tecnico>");
					out.println("<error>Equipo no se encuentra activo o asignado a un recurso</error>");
					out.println("</tecnico>");
				}
			}else { // Equipo no se encuentra registrado
				out.println("<tecnico>");
				out.println("<error>Equipo no se encuentra registrado</error>");
				out.println("</tecnico>");
			}
			out.println("</supervisor>");
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
