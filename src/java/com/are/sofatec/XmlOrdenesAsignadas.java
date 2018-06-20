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
@WebServlet("/XmlOrdenesAsignadas")
public class XmlOrdenesAsignadas extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public XmlOrdenesAsignadas() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    protected void ProcesarPeticion(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
    	
    	response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        out.println("<?xml version=\"1.0\" ?>");
        String cedula = (String)request.getParameter("cedula");
        db conexion = null;
        
        try {
        	
        	conexion = new db();
        	String sql ="select depadesc,locadesc,orden,orders.nic,orders.nom_cli,orders.ape1_cli," +
					"orders.ape2_cli,orders.nom_via,orders.nom_calle,orders.placa,orders.crucero," +
					"orders.interior,tiordesc,orders.num_apa,orders.recurso," +
					"orders.fecha_asignacion,orders.num_show,orders.confirm," +
					"orders.ref_dir,orders.nis,orders.deuda, orders.desc_co_marca," +
					"orders.nom_local,orders.cant_fact,orders.tipo " +
					"from orders,departamentos,localidad,tipo_orden " +
					"where localidad.locadepa=depacodi " +
					"and orders.localidad=locacodi " +
					"and orders.tipo=tiorcodi " +
					"and orders.recurso = ? " +
					"and orders.estado=2 " +
					"order by num_show ";
        	
        	java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
			pst.setString(1, cedula);
			ResultSet rs = conexion.Query(pst);
			out.println("<ordenes>");
			while (rs.next()) {
				
	            out.println("<item>");
	            out.println("<orden>" + rs.getString("orden") + "</orden>");
	            out.println("<nic>" + rs.getString("nic") + "</nic>");
	            out.println("<nis>" + rs.getString("nis") + "</nis>");
	            out.println("<cliente>" + rs.getString("nom_cli") + " " + rs.getString("ape1_cli") + " " + rs.getString("ape2_cli") + "</cliente>");
	            out.println("<direccion>" + rs.getString("nom_via") + " "+ rs.getString("nom_calle") + " " + rs.getString("crucero") + " " + rs.getString("placa") + " " + rs.getString("interior")  + "</direccion>");
	            out.println("<dir_referencia>" + rs.getString("ref_dir") + "</dir_referencia>");
	            out.println("<medidor>" + rs.getString("num_apa") + "</medidor>");
	            out.println("<cod_tipo>" + rs.getString("tipo") + "</cod_tipo>");
	            out.println("<tipo>" + rs.getString("tiordesc") + "</tipo>");
	            out.println("<departamento>" + rs.getString("depadesc") + "</departamento>");
	            out.println("<municipio>" + rs.getString("locadesc") + "</municipio>");
	            out.println("<deuda>" + rs.getString("deuda") + "</deuda>");
	            out.println("<marca>" + rs.getString("desc_co_marca") + "</marca>");
	            out.println("<barrio>" + rs.getString("nom_local") + "</barrio>");
	            out.println("<facturas>" + rs.getString("cant_fact") + "</facturas>");
	            out.println("<confirm>" + rs.getString("confirm") + "</confirm>");
	            out.println("</item>");
			}
           
            out.println("</ordenes>");
            
        	
        	
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
