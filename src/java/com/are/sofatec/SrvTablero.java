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
 * Servlet implementation class SrvTablero
 */
@WebServlet("/SrvTablero")
public class SrvTablero extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SrvTablero() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    protected void ProcesarPeticion(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
    	
    	response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        String fecha = (String)request.getParameter("fecha");
        String ciudad = (String)request.getParameter("ciudad");
        String departamento = (String)request.getParameter("departamento");
        int total[] = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
        
        String sql = "select equipos.recurso,recurso.recunomb, " +
        			"sum(if(hour(fecha)>=0 AND hour(fecha)<=6,1,0)) R1," +
        			"sum(if(hour(fecha)=7,1,0)) H7," +
        			"sum(if(hour(fecha)=8,1,0)) H8," +
        			"sum(if(hour(fecha)=9,1,0)) H9," +
        			"sum(if(hour(fecha)=10,1,0)) H10," +
        			"sum(if(hour(fecha)=11,1,0)) H11," +
        			"sum(if(hour(fecha)=12,1,0)) H12," +
        			"sum(if(hour(fecha)=13,1,0)) H13," +
        			"sum(if(hour(fecha)=14,1,0)) H14," +
        			"sum(if(hour(fecha)=15,1,0)) H15," +
        			"sum(if(hour(fecha)=16,1,0)) H16," +
        			"sum(if(hour(fecha)=17,1,0)) H17," +
        			"sum(if(hour(fecha)=18,1,0)) H18," +
        			"sum(if(hour(fecha)=19,1,0)) H19," +
        			"sum(if(hour(fecha)=20,1,0)) H20," +
        			"sum(if(hour(fecha)>=21 AND hour(fecha)<=23,1,0)) R2," +
        			" count(reportes.orden) total " +
        			"from equipos " +
        			"LEFT OUTER JOIN reportes ON reportes.recurso = equipos.recurso AND  date(reportes.fecha) = ?  " +
        			"INNER JOIN orders ON orders.orden = reportes.orden " +
        			"INNER JOIN localidad ON orders.localidad = localidad.locacodi " +
        			"INNER JOIN recurso ON equipos.recurso = recurso.recucodi ";
        			
        			if (!ciudad.equals("-1")) {
        				sql += "AND orders.localidad ='" + ciudad + "' "; 
        			}
        			
        			if (!departamento.equals("-1")) {
        				sql += "AND localidad.locadepa ='" + departamento + "' "; 
        			}
        
        			sql += "group by equipos.recurso "
        			
        		;
        db conexion = null;
        
        try {
			conexion = new db();
			java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
			pst.setString(1, fecha);
			
			
			
			ResultSet rs = conexion.Query(pst);
			
			out.println("<h2>REDIMIENTO FECHA:  " + fecha + "</h2>");
			out.println("<table>");
			out.println("<tr>");
			out.println("<th>CEDULA TECNICO</th>");
			out.println("<th>NOMBRE TECNICO</th>");
			out.println("<th>1 am - 6 am</th>");
			out.println("<th>7 am</th>");
			out.println("<th>8 am</th>");
			out.println("<th>9 am</th>");
			out.println("<th>10 am</th>");
			out.println("<th>11 am</th>");
			out.println("<th>12 md</th>");
			out.println("<th>1 pm</th>");
			out.println("<th>2 pm</th>");
			out.println("<th>3 pm</th>");
			out.println("<th>4 pm</th>");
			out.println("<th>5 pm</th>");
			out.println("<th>6 pm</th>");
			out.println("<th>7 pm</th>");
			out.println("<th>8 pm</th>");
			out.println("<th>9 pm - 12 pm</th>");
			out.println("<th>TOTAL</th>");
			
			if (rs.next()) { // existen registros
				
			do {
				out.println("<tr>");
				out.println("<td>" + rs.getString("recurso") + "</td>");
				out.println("<td>" + rs.getString("recunomb") + "</td>");
				out.println("<td>" + (rs.getInt("R1")==0?"-":rs.getInt("R1")) + "</td>");
				out.println("<td>" + (rs.getInt("H7")==0?"-":rs.getInt("H7"))+ "</td>");
				out.println("<td>" + (rs.getInt("H8")==0?"-":rs.getInt("H8")) + "</td>");
				out.println("<td>" + (rs.getInt("H9")==0?"-":rs.getInt("H9")) + "</td>");
				out.println("<td>" + (rs.getInt("H10")==0?"-":rs.getInt("H10")) + "</td>");
				out.println("<td>" + (rs.getInt("H11")==0?"-":rs.getInt("H11")) + "</td>");
				out.println("<td>" + (rs.getInt("H12")==0?"-":rs.getInt("H12")) + "</td>");
				out.println("<td>" + (rs.getInt("H13")==0?"-":rs.getInt("H13")) + "</td>");
				out.println("<td>" + (rs.getInt("H14")==0?"-":rs.getInt("H14")) + "</td>");
				out.println("<td>" + (rs.getInt("H15")==0?"-":rs.getInt("H15")) + "</td>");
				out.println("<td>" + (rs.getInt("H16")==0?"-":rs.getInt("H16")) + "</td>");
				out.println("<td>" + (rs.getInt("H17")==0?"-":rs.getInt("H17")) + "</td>");
				out.println("<td>" + (rs.getInt("H18")==0?"-":rs.getInt("H18")) + "</td>");
				out.println("<td>" + (rs.getInt("H19")==0?"-":rs.getInt("H19")) + "</td>");
				out.println("<td>" + (rs.getInt("H20")==0?"-":rs.getInt("H20")) + "</td>");
				out.println("<td>" + (rs.getInt("R2")==0?"-":rs.getInt("R2")) + "</td>");
				out.println("<td><a href=\"javascript:VerOrdenesRecurso('" + rs.getString("recurso") + "','" + fecha + "','" + ciudad + "')\">" + rs.getInt("total") + "<a/></td>");
				
				out.println("</tr>");
				total[0] = total[0] +  rs.getInt("R1");
				total[1] = total[1] +  rs.getInt("H7");
				total[2] = total[2] +  rs.getInt("H8");
				total[3] = total[3] +  rs.getInt("H9");
				total[4] = total[4] +  rs.getInt("H10");
				total[5] = total[5] +  rs.getInt("H11");
				total[6] = total[6] +  rs.getInt("H12");
				total[7] = total[7] +  rs.getInt("H13");
				total[8] = total[8] +  rs.getInt("H14");
				total[9] = total[9] +  rs.getInt("H15");
				total[10] = total[10] +  rs.getInt("H16");
				total[11] = total[11] +  rs.getInt("H17");
				total[12] = total[12] +  rs.getInt("H18");
				total[13] = total[13] +  rs.getInt("H19");
				total[14] = total[14] +  rs.getInt("H20");
				total[15] = total[15] +  rs.getInt("R2");
				total[16] = total[16] +  rs.getInt("total");
				
			}while (rs.next());
        	out.println("<tr>");
        	out.println("<td colspan=\"2\">Total</td>");
        	for (int x=0; x< total.length; x++) {
        		out.println("<td>" + total[x] + "</td>");
        	}
			out.println("</tr>");
			
        }else {
        	out.println("<tr>");
			out.println("<td colspan=\"19\">No se encontraron registros</td>");	
			out.println("</tr>");
        }
			
			out.println("</table>");	
			
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
