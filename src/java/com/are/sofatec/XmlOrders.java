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
@WebServlet("/XmlOrders")
public class XmlOrders extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public XmlOrders() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    protected void ProcesarPeticion(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
    	
    	response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        out.println("<?xml version=\"1.0\" ?>");
        String imei = (String)request.getParameter("imei");
        db conexion = null;
        
        try {
        	
        	conexion = new db();
        	
        	String sql = "select imei, tipo from equipos where imei=? and estado = 1";
        	java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        	pst.setString(1, imei);
        	ResultSet rsEquipo = conexion.Query(pst);
        	if (rsEquipo.next()) {
        	
        	
        	sql ="select depadesc,locadesc,orden,orders.nic,orders.nom_cli,orders.ape1_cli," +
					"orders.ape2_cli,orders.nom_via,orders.nom_calle,orders.placa,orders.crucero," +
					"orders.interior,tiordesc,orders.num_apa,orders.recurso," +
					"orders.fecha_asignacion,orders.num_show,orders.confirm," +
					"orders.ref_dir,orders.nis,orders.deuda, orders.desc_co_marca," +
					"orders.nom_local,orders.cant_fact,orders.tipo " +
					"from orders,departamentos,localidad,tipo_orden,equipos " +
					"where localidad.locadepa=depacodi " +
					"and orders.localidad=locacodi " +
					"and orders.tipo=tiorcodi " +
					"and equipos.recurso = orders.recurso " +
					"and equipos.imei = ? " +
					"and orders.estado=2 " +
					"and equipos.estado = 1 " ;
					
        	if (rsEquipo.getInt("tipo") == 1) {
        		sql += "order by num_show ";  
        		sql += " limit 1";
        	}else {
        		sql += " order by num_show ";
        	}
        	
        	pst = conexion.getConnection().prepareStatement(sql);
			pst.setString(1, imei);
			ResultSet rs = conexion.Query(pst);
			out.println("<ordenes>");
			while (rs.next()) {
				Clientes cliente = new Clientes(conexion);
				String latitud = "0";
				String longitud = "0";
				if(cliente.Find(rs.getString("nic"))) {
					latitud = cliente.getLatitud();
					longitud = cliente.getLongitud();
				}
	            out.println("<item>");
	            out.println("<orden>" + LimpiarCadena(rs.getString("orden")) + "</orden>");
	            out.println("<nic>" + LimpiarCadena(rs.getString("nic")) + "</nic>");
	            out.println("<nis>" + LimpiarCadena(rs.getString("nis")) + "</nis>");
	            out.println("<cliente>" + LimpiarCadena(rs.getString("nom_cli")) + " " + LimpiarCadena(rs.getString("ape1_cli")) + " " + LimpiarCadena(rs.getString("ape2_cli")) + "</cliente>");
	            out.println("<direccion>" + LimpiarCadena(rs.getString("nom_via")) + " "+ LimpiarCadena(rs.getString("nom_calle") + " " + rs.getString("crucero") + " " + rs.getString("placa") + " " + LimpiarCadena(rs.getString("interior")))  + "</direccion>");
	            //out.println("<dir_referencia>" + LimpiarCadena(rs.getString("ref_dir")) + "</dir_referencia>");
	            out.println("<dir_referencia></dir_referencia>");
	            out.println("<medidor>" + LimpiarCadena(rs.getString("num_apa")) + "</medidor>");
	            out.println("<cod_tipo>" + LimpiarCadena(rs.getString("tipo")) + "</cod_tipo>");
	            out.println("<tipo>" + LimpiarCadena(rs.getString("tiordesc")) + "</tipo>");
	            out.println("<departamento>" + LimpiarCadena(rs.getString("depadesc")) + "</departamento>");
	            out.println("<municipio>" + LimpiarCadena(rs.getString("locadesc")) + "</municipio>");
	            out.println("<deuda>" + LimpiarCadena(rs.getString("deuda")) + "</deuda>");
	            out.println("<marca>" + LimpiarCadena(rs.getString("desc_co_marca")) + "</marca>");
	            out.println("<barrio>" + LimpiarCadena(rs.getString("nom_local")) + "</barrio>");
	            out.println("<facturas>" + LimpiarCadena(rs.getString("cant_fact")) + "</facturas>");
	            out.println("<confirm>" + rs.getString("confirm") + "</confirm>");
	            out.println("<latitud>" + latitud + "</latitud>");
	            out.println("<longitud>" + longitud + "</longitud>");
	            out.println("</item>");
			}
           
            out.println("</ordenes>");
            
        	}else { // el imei no existe en la base de datos, no enviar ordenes
        		out.println("<ordenes>");
        		 out.println("<item>");
        		 out.println("</item>");
        		out.println("</ordenes>");
        		
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
	
	public String LimpiarCadena(String cadena) {
		cadena = cadena.trim();
		cadena = cadena.replace("\r", "");
		cadena = cadena.replace("\n", "");
		cadena = cadena.replace("\t","");
		cadena = cadena.replace("<","");
		cadena = cadena.replace(">","");
		cadena = cadena.replace("?","");
		cadena = cadena.replace("&","");
		return cadena;
	}

}
