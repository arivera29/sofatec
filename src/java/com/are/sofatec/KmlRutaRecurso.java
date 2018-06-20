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
 * Servlet implementation class XmlOrders
 */
@WebServlet("/KmlRutaRecurso")
public class KmlRutaRecurso extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public KmlRutaRecurso() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    protected void ProcesarPeticion(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
    	
        PrintWriter out = response.getWriter();
        	response.setContentType("text/kml;charset=UTF-8");
        	response.setHeader("Content-Disposition", "attachment; filename=\"ruta.kml\"");
        out.println("<?xml version=\"1.0\" ?>");
        out.println("<kml xmlns=\"http://www.opengis.net/kml/2.2\" >");
        out.println("<Document>");
        out.println("<name>Ruta Recurso</name>");
        String cedula = (String)request.getParameter("cedula");
        db conexion = null;
        
        try {
        	
        	conexion = new db();
        	String sql ="select latitud,longitud,DATE_FORMAT(fecha_sistema,'%m-%d %H.%i') hora " +
        			"from gps " +
        			"where recurso=? " ;
        	if (request.getParameter("fecha") != null) {
        		sql += "and date(fecha_sistema) = ? " ;
        	}else {
        		sql += "and date(fecha_sistema) = current_date() " ;
        	}
        	sql +=	"order by fecha_sistema";
        	
        	java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
			pst.setString(1, cedula);
			if (request.getParameter("fecha") != null) {
				pst.setString(2, (String)request.getParameter("fecha"));
			}
			ResultSet rs = conexion.Query(pst);
			

			while (rs.next()) {
				out.println("<Placemark>");
				out.println("<name>" + rs.getString("hora") + "</name>");
				out.println("<Style>");
				out.println("<IconStyle>");
				out.println("<Icon>");
				out.println("<href>http://maps.google.com/mapfiles/kml/pal3/icon19.png</href>");
				out.println("</Icon>");
				out.println("</IconStyle>");
				out.println("<LineStyle>");
				out.println("<width>2</width>");
				out.println("</LineStyle>");
				out.println("</Style>");
				out.println("<Point>");
				out.println("<extrude>1</extrude>");
				out.println("<altitudeMode>relativeToGround</altitudeMode>");
		        out.print("<coordinates>");
	            out.print(rs.getString("longitud").replace(',','.') + "," + rs.getString("latitud").replace(',','.') + ",0");
	            out.println("</coordinates>");
	            out.println("</Point>");
				out.println("</Placemark>");
			}
			out.println("</Document>");
			out.println("</kml>");
            
        	
        	
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
