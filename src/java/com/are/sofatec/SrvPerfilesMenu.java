package com.are.sofatec;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Servlet implementation class SrvPerfilesMenu
 */
@WebServlet("/SrvPerfilesMenu")
public class SrvPerfilesMenu extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SrvPerfilesMenu() {
		super();
		// TODO Auto-generated constructor stub
	}

	db conexion = null;
	ManejadorPerfilesMenu manejador;
	ManejadorPerfiles MP;

	protected void ProcesarPeticion(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=ISO-8859-1");
		PrintWriter out = response.getWriter();

		String operacion = (String) request.getParameter("operacion");
		db conexion = null;
		try { 
		
			if (operacion.equals("add")) {
			conexion =new db();
			String perfil = (String)request.getParameter("perfil");
			String[] menu = request.getParameterValues("menu");
			if (menu.length > 0) {
				ArrayList<PerfilesMenu> perfiles = new ArrayList<PerfilesMenu>();
				for (int i=0; i< menu.length; i++) {
					PerfilesMenu pm = new PerfilesMenu();
					pm.setIdperfil(perfil);
					pm.setIdmenu(menu[i]);
					perfiles.add(pm);
				}
				
				ManejadorPerfilesMenu mpm = new ManejadorPerfilesMenu(conexion);
				if (mpm.removeAll(perfil)) {
					if (mpm.add(perfiles)) {
						conexion.Commit();
						out.print("OK");
					}else {
						throw new SQLException("Error al agregar el menu del perfil");
					}
					
				}else {
					throw new SQLException("Error al configurar el menu del perfil");
				}
				
				
			}else {
				out.println("Error, no se recibieron item de menu");
			}
			
			
		}
		
		if (operacion.equals("list_modulo")) {
			conexion = new db();
			String modulo = (String)request.getParameter("modulo");
			ArrayList<Menu> menu = new ArrayList<Menu>();
			ManejadorMenu mm = new ManejadorMenu(conexion);
			menu = mm.list(modulo);
			if (menu.size() > 0) {
				out.println("<div id ='tb_menu'>");
				out.println("<table>");
				int fila = 1;
				for (Menu m : menu) {
					
					if (m.getPadreid().equals(m.getMenuid())) {  // un menu padre
						out.println("<tr>");
						out.println("<th><input type='checkbox' id='menu[]' value='" + m.getMenuid() + "'></th>");
						out.println("<th>" + m.getTitulo() + "</th>");
						out.println("</tr>");
					}else {
						out.println("<tr " + (fila%2==0?"class='odd'":"") + ">");
						out.println("<td><input type='checkbox' id='menu[]' value='" + m.getMenuid() + "'></td>");
						out.println("<td>" + m.getTitulo() + "</td>");
						out.println("</tr>");
					}
					fila++;
					
				}
				
				out.println("</table>");
				out.println("</div>");
				
			}else {
				out.println("<strong>No hay item de menu en la base de datos, comunicarse con el administrador</strong>");
			}
			
			
		}
		
		}catch (SQLException e) {
    		out.println("Error con el Servidor " + e.getMessage());
    	}finally {
    		if (conexion != null) {
    			try {
					conexion.Close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					out.println("Error al cerrar la conexion " + e.getMessage());
				}
    		}
    	}
		
		out.close();
	}

	

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		ProcesarPeticion(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		ProcesarPeticion(request, response);
	}

}
