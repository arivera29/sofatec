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
 * Servlet implementation class SrvPerfiles
 */
@WebServlet("/SrvPerfiles")
public class SrvPerfiles extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SrvPerfiles() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    protected void ProcesarPeticion(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
    	response.setContentType("text/html;charset=ISO-8859-1");
    	PrintWriter out = response.getWriter();
    	String operacion = (String)request.getParameter("operacion");
    	
    	db conexion = null;
    	try {
    		
    		if (operacion.equals("add")) {
    			String nombre = (String)request.getParameter("nombre");
    			conexion = new db();
    			Perfiles perfil = new Perfiles();
    			perfil.setPerfil(nombre);
    			ManejadorPerfiles mp = new ManejadorPerfiles(conexion);
    			mp.setPerfil(perfil);
    			if (mp.add()) {
    				out.print("OK");
    			}else {
    				out.print("Error al procesar la solicitud");
    			}
    			
    		}
    		
    		
    		if (operacion.equals("update")) {
    			String nombre = (String)request.getParameter("nombre");
    			String id = (String)request.getParameter("id");
    			conexion = new db();
    			Perfiles perfil = new Perfiles();
    			perfil.setPerfil(nombre);
    			ManejadorPerfiles mp = new ManejadorPerfiles(conexion);
    			mp.setPerfil(perfil);
    			if (mp.update(id)) {
    				out.print("OK");
    			}else {
    				out.print("Error al procesar la solicitud");
    			}
    			
    		}
    		
    		if (operacion.equals("remove")) {
    			String id = (String)request.getParameter("id");
    			conexion = new db();
    			ManejadorPerfiles mp = new ManejadorPerfiles(conexion);
    			if (mp.remove(id)) {
    				out.print("OK");
    			}else {
    				out.print("Error al procesar la solicitud");
    			}
    			
    		}
    		
    		if (operacion.equals("list")) {
    			ArrayList<Perfiles> lista = new ArrayList<Perfiles>();
    			conexion = new db();
    			ManejadorPerfiles mp = new ManejadorPerfiles(conexion);
    			lista = mp.list();
    			out.println("<table>");
    			out.println("<tr>");
    			out.println("<th>Id</th>");
    			out.println("<th>Perfil</th>");
    			out.println("<th>Accion</th>");
    			out.println("</tr>");
    			if (lista.size() > 0) {
    				int fila = 1;
    				for (Perfiles perfil : lista) {
    					out.println("<tr " + (fila%2==0?"class='odd'":"") + ">");
    					out.println("<td>" + perfil.getId() + "</td>");
    					out.println("<td>" + perfil.getPerfil() + "</td>");
    					out.println("<td><a href=\"javascript:eliminar('" + perfil.getId()  + "')\">Eliminar</a> " +
    							"<a href=\"PerfilMenu.jsp?id=" + perfil.getId() + "\">Menu</a></td>");
    					out.println("</tr>");
    					fila++;
    				}
    			}else {
    				out.println("<tr><td colspan=3></td></tr>");
    			}
    			out.println("</table>");
    			
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
    	
    	
    	
    	
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		ProcesarPeticion(request,
				response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		ProcesarPeticion(request,response);
	}

}
