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
@WebServlet("/SrvTableroLote")
public class SrvTableroLote extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SrvTableroLote() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    protected void ProcesarPeticion(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
    	
    	response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        String fecha = (String)request.getParameter("fecha");
        String zona = (String)request.getParameter("zona");
        int total[] = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
        
        String sql = "select equipos.recurso,recurso.recunomb, " +
        			"sum(if(hour(IF(ISNULL(FECHA_CIERRE),FECHA_ANOMALIA,FECHA_CIERRE))>=0 AND hour(IF(ISNULL(FECHA_CIERRE),FECHA_ANOMALIA,FECHA_CIERRE))<=6,1,0)) R1," +
        			"sum(if(hour(IF(ISNULL(FECHA_CIERRE),FECHA_ANOMALIA,FECHA_CIERRE))=7,1,0)) H7," +
        			"sum(if(hour(IF(ISNULL(FECHA_CIERRE),FECHA_ANOMALIA,FECHA_CIERRE))=8,1,0)) H8," +
        			"sum(if(hour(IF(ISNULL(FECHA_CIERRE),FECHA_ANOMALIA,FECHA_CIERRE))=9,1,0)) H9," +
        			"sum(if(hour(IF(ISNULL(FECHA_CIERRE),FECHA_ANOMALIA,FECHA_CIERRE))=10,1,0)) H10," +
        			"sum(if(hour(IF(ISNULL(FECHA_CIERRE),FECHA_ANOMALIA,FECHA_CIERRE))=11,1,0)) H11," +
        			"sum(if(hour(IF(ISNULL(FECHA_CIERRE),FECHA_ANOMALIA,FECHA_CIERRE))=12,1,0)) H12," +
        			"sum(if(hour(IF(ISNULL(FECHA_CIERRE),FECHA_ANOMALIA,FECHA_CIERRE))=13,1,0)) H13," +
        			"sum(if(hour(IF(ISNULL(FECHA_CIERRE),FECHA_ANOMALIA,FECHA_CIERRE))=14,1,0)) H14," +
        			"sum(if(hour(IF(ISNULL(FECHA_CIERRE),FECHA_ANOMALIA,FECHA_CIERRE))=15,1,0)) H15," +
        			"sum(if(hour(IF(ISNULL(FECHA_CIERRE),FECHA_ANOMALIA,FECHA_CIERRE))=16,1,0)) H16," +
        			"sum(if(hour(IF(ISNULL(FECHA_CIERRE),FECHA_ANOMALIA,FECHA_CIERRE))=17,1,0)) H17," +
        			"sum(if(hour(IF(ISNULL(FECHA_CIERRE),FECHA_ANOMALIA,FECHA_CIERRE))=18,1,0)) H18," +
        			"sum(if(hour(IF(ISNULL(FECHA_CIERRE),FECHA_ANOMALIA,FECHA_CIERRE))=19,1,0)) H19," +
        			"sum(if(hour(IF(ISNULL(FECHA_CIERRE),FECHA_ANOMALIA,FECHA_CIERRE))=20,1,0)) H20," +
        			"sum(if(hour(IF(ISNULL(FECHA_CIERRE),FECHA_ANOMALIA,FECHA_CIERRE))>=21 AND hour(IF(ISNULL(FECHA_CIERRE),FECHA_ANOMALIA,FECHA_CIERRE))<=23,1,0)) R2," +
        			" count(QO_ORDENES.NUM_OS) total " +
        			"from equipos " +
        			"LEFT OUTER JOIN QO_ORDENES ON QO_ORDENES.TECNICO = EQUIPOS.RECURSO AND  date(IF(ISNULL(FECHA_CIERRE),FECHA_ANOMALIA,FECHA_CIERRE)) = ?  " +
        			"INNER JOIN RECURSO ON EQUIPOS.RECURSO = RECURSO.RECUCODI AND EQUIPOS.RECURSO != '-1'";
        			
        			if (!zona.equals("all")) {
        				sql += " AND QO_ORDENES.NUM_ZONA ='" + zona + "' AND QO_ORDENES.ESTADO_OPER IN(3,99) "; 
        			}
        			
        
        			sql += "GROUP BY EQUIPOS.RECURSO ORDER BY RECURSO.RECUNOMB";
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
				out.println("<td><a href=\"javascript:VerOrdenesRecurso('" + rs.getString("recurso") + "','" + fecha + "','" + zona + "')\">" + rs.getInt("total") + "<a/></td>");
				
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
