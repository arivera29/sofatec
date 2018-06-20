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
 * Servlet implementation class SrvListAssign
 */
@WebServlet("/SrvListAssign")
public class SrvListAssign extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SrvListAssign() {
        super();
        // TODO Auto-generated constructor stub
    }

    
	protected void ProcesarPeticion(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();

		HttpSession sesion=request.getSession();
		if (sesion.getAttribute("usuario") == null) {
			out.print("La sesión ha caducado... intente de nuevo por favor");
			return ;
		}
		
		String operacion = (String)request.getParameter("operacion");
		db conexion = null;
		
		if (operacion.equals("ciudad")) {
			String ciudad = (String)request.getParameter("ciudad");
			
			String sql ="select depadesc,locadesc,orden,orders.nic,orders.nom_cli,orders.ape1_cli," +
					"orders.ape2_cli,orders.nom_via,orders.nom_calle,orders.placa,orders.crucero," +
					"orders.interior,tiordesc,orders.num_apa,orders.recurso,nom_local," +
					"orders.fecha_asignacion,recurso.recunomb,orders.num_show,confirm " +
					"from orders,departamentos,localidad,tipo_orden,recurso " +
					"where localidad.locadepa=depacodi " +
					"and orders.localidad=locacodi " +
					"and orders.tipo=tiorcodi " ;
					
					if (!ciudad.equals("-1")) {
						sql += "and orders.localidad = ? " ;
						
					}
					sql += "and recucodi = orders.recurso " +
					"and estado='2' " +
					"order by depadesc,locadesc,recurso,num_show";
			
			
			try {
				conexion = new db();
				java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
				if (!ciudad.equals("-1")) {
					pst.setString(1, ciudad);
				}
				
				ResultSet rs = conexion.Query(pst);
				out.println("<h2>Registros encontrados</h2>");
				out.println("<table id=\"Exportar_a_Excel\">");
				out.println("<tr>");
				out.println("<th>DEPARTAMENTO</th>");
				out.println("<th>CIUDAD</th>");
				out.println("<th>ORDEN</th>");
				out.println("<th>NIC</th>");
				out.println("<th>CLIENTE</th>");
				out.println("<th>DIRECCION</th>");
				out.println("<th>BARRIO</th>");
				out.println("<th>TIPO ORDEN</th>");
				out.println("<th>MEDIDOR</th>");
				out.println("<th>CEDULA TECNICO</th>");
				out.println("<th>NOMBRE TECNICO</th>");
				out.println("<th>FECHA ASIGNACION</th>");
				out.println("<th>ORDEN EJECUCION</th>");
				
				int cont = 0;
				out.println("</tr>");
				if (rs.next()) { // existen departamentos
				do {
					out.println("<tr " + (cont % 2==0?"class=''":"class='odd'") + " id='" +  rs.getString("orden") + "'>");
					out.println("<td>" + rs.getString("depadesc") + "</td>");
					out.println("<td>" + rs.getString("locadesc") + "</td>");
					out.println("<td>" + rs.getString("orden") + "</td>");
					out.println("<td>" + rs.getString("nic") + "</td>");
					out.println("<td>" + rs.getString("nom_cli") + " " +  rs.getString("ape1_cli") + " " + rs.getString("ape2_cli") + "</td>");
					out.println("<td>" + rs.getString("nom_via") + " "+ rs.getString("nom_calle") + " " + rs.getString("crucero") + " " + rs.getString("placa") + " " + rs.getString("interior") + "</td>");
					out.println("<td>" + rs.getString("nom_local") + "</td>");
					out.println("<td>" + rs.getString("tiordesc") + "</td>");
					out.println("<td>" + rs.getString("num_apa") + "</td>");
					out.println("<td>" + rs.getString("recurso") + "</td>");
					out.println("<td>" + rs.getString("recunomb") + "</td>");
					out.println("<td>" + rs.getString("fecha_asignacion") + "</td>");
					out.println("<td>" + rs.getString("num_show") + "</td>");
					
					out.println("</tr>");
					cont++;
					
				}while(rs.next());
					out.println("<tr>");
					out.println("<td colspan=\"14\">Total ordenes: " + cont + "</td>");	
					out.println("</tr>");
				}else {
					out.println("<tr>");
					out.println("<td colspan=\"14\">No se encontraron registros</td>");	
					out.println("</tr>");
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
		
		if (operacion.equals("recurso")) {
			
			String recurso = (String)request.getParameter("recurso");
			
			String sql ="select depadesc,locadesc,orden,orders.nic,orders.nom_cli,orders.ape1_cli," +
					"orders.ape2_cli,orders.nom_via,orders.nom_calle,orders.placa,orders.crucero," +
					"orders.interior,tiordesc,orders.num_apa,orders.recurso,nom_local," +
					"orders.fecha_asignacion,recurso.recunomb,orders.num_show,confirm " +
					"from orders,departamentos,localidad,tipo_orden,recurso " +
					"where localidad.locadepa=depacodi " +
					"and orders.localidad=locacodi " +
					"and orders.tipo=tiorcodi " +
					"and recucodi = orders.recurso " +
					"and orders.recurso = ? " +
					"and estado='2' " +
					"order by num_show";
			try {
				conexion = new db();
				java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
				pst.setString(1, recurso);
				ResultSet rs = conexion.Query(pst);
				out.println("<h2>Registros encontrados</h2>");
				out.println("<a href='javascript:ConfirmarRuta()'>Confirmar Ruta</a>");
				out.println("<a href=\"javascript:Liberar('" + recurso + "')\">Liberar Ordenes</a>");
				out.println("<table id=\"Exportar_a_Excel\">");
				out.println("<thead>");
				out.println("<tr>");
				out.println("<th>DEPARTAMENTO</th>");
				out.println("<th>CIUDAD</th>");
				out.println("<th>ORDEN</th>");
				out.println("<th>NIC</th>");
				out.println("<th>CLIENTE</th>");
				out.println("<th>DIRECCION</th>");
				out.println("<th>BARRIO</th>");
				out.println("<th>TIPO DE ORDEN</th>");
				out.println("<th>MEDIDOR</th>");
				out.println("<th>CEDULA TECNICO</th>");
				out.println("<th>NOMBRE TECNICO</th>");
				out.println("<th>FECHA ASIGNACION</th>");
				out.println("<th>ORD.</th>");
				out.println("<th>CONF.</th>");
				out.println("<th>Mover</th>");
				int cont = 0;
				out.println("</tr>");
				out.println("</thead>");
				if (rs.next()) { // existen departamentos
					out.println("<tbody>");
				do {
					out.println("<tr " + (cont % 2==0?"class=''":"class='odd'") +  " id='orden_" +  rs.getString("orden") + "'>");
					out.println("<td>" + rs.getString("depadesc") + "</td>");
					out.println("<td>" + rs.getString("locadesc") + "</td>");
					out.println("<td>" + rs.getString("orden") + "</td>");
					out.println("<td>" + rs.getString("nic") + "</td>");
					out.println("<td>" + rs.getString("nom_cli") + " " +  rs.getString("ape1_cli") + " " + rs.getString("ape2_cli") + "</td>");
					out.println("<td>" + rs.getString("nom_via") + " "+ rs.getString("nom_calle") + " " + rs.getString("crucero") + " " + rs.getString("placa") + " " + rs.getString("interior") + "</td>");
					out.println("<td>" + rs.getString("nom_local") + "</td>");
					out.println("<td>" + rs.getString("tiordesc") + "</td>");
					out.println("<td>" + rs.getString("num_apa") + "</td>");
					out.println("<td>" + rs.getString("recurso") + "</td>");
					out.println("<td>" + rs.getString("recunomb") + "</td>");
					out.println("<td>" + rs.getString("fecha_asignacion") + "</td>");
					out.println("<td>" + rs.getString("num_show") + "</td>");
					out.println("<td>" + (rs.getInt("confirm")==1?"Si":"No") + "</td>");
					out.println("<td><a href=\"javascript:moverUp('orden_" + rs.getString("orden") + "')\" title=\"Subir\"><img src=\"images/arrow_up.png\" width=24 height=24></a> <a href=\"javascript:moverDown('orden_"  +  rs.getString("orden") +"')\" title=\"Bajar\"><img src=\"images/arrow_down.png\" width=24 height=24></a></td>");
					out.println("</tr>");
					cont++;
					
				}while(rs.next());
				out.println("</tbody>");
				out.println("<tfoot>");
				out.println("<tr>");
				out.println("<td colspan=\"15\">Total ordenes: " + cont + "</td>");	
				out.println("</tr>");
				out.println("<tfoot>");
				}else {
					out.println("<tr>");
					out.println("<td colspan=\"15\">No se encontraron registros</td>");	
					out.println("</tr>");
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
