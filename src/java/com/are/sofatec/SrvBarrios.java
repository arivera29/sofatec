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
@WebServlet("/SrvBarrios")
public class SrvBarrios extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SrvBarrios() {
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
    			String localidad = (String)request.getParameter("localidad");
    			String nombre = (String)request.getParameter("nombre");
    			String peso = (String)request.getParameter("peso");
    			
    			conexion = new db();
    			Barrio barrio = new Barrio();
    			barrio.setLocalidad(localidad);
    			barrio.setNombre(nombre);
    			barrio.setPeso(Integer.parseInt(peso));
    			ManejadorBarrios manejador = new ManejadorBarrios(conexion);
    			manejador.setBarrio(barrio);
    			if (manejador.add()) {
    				out.print("OK");
    			}else {
    				out.print("Error al procesar la solicitud");
    			}
    			
    		}
    		
    		
    		if (operacion.equals("update")) {
    			String localidad = (String)request.getParameter("localidad");
    			String nombre = (String)request.getParameter("nombre");
    			String peso = (String)request.getParameter("peso");
    			String id = (String)request.getParameter("id");
    			conexion = new db();
    			
    			Barrio barrio = new Barrio();
    			barrio.setLocalidad(localidad);
    			barrio.setNombre(nombre);
    			barrio.setPeso(Integer.parseInt(peso));
    			
    			ManejadorBarrios manejador = new ManejadorBarrios(conexion);
    			manejador.setBarrio(barrio);
    			
    			if (manejador.update(id)) {
    				out.print("OK");
    			}else {
    				out.print("Error al procesar la solicitud");
    			}
    			
    		}
    		
    		if (operacion.equals("remove")) {
    			String id = (String)request.getParameter("id");
    			conexion = new db();
    			ManejadorBarrios manejador = new ManejadorBarrios(conexion);
    			if (manejador.remove(id)) {
    				out.print("OK");
    			}else {
    				out.print("Error al procesar la solicitud");
    			}
    			
    		}
    		
    		if (operacion.equals("list")) {
    			String localidad = (String)request.getParameter("localidad");
    			
    			ArrayList<Barrio> lista = new ArrayList<Barrio>();
    			conexion = new db();
    			ManejadorBarrios manejador = new ManejadorBarrios(conexion);
    			lista = manejador.list(localidad);
    			out.println("<table>");
    			out.println("<tr>");
    			out.println("<th>Id</th>");
    			out.println("<th>Barrio</th>");
    			out.println("<th>Peso</th>");
    			out.println("<th>Accion</th>");
    			out.println("</tr>");
    			if (lista.size() > 0) {
    				int fila = 1;
    				for (Barrio barrio : lista) {
    					out.println("<tr " + (fila%2==0?"class='odd'":"") + ">");
    					out.println("<td>" + barrio.getId() + "</td>");
    					out.println("<td>" + barrio.getNombre() + "</td>");
    					out.println("<td>" + barrio.getPeso() + "</td>");
    					out.println("<td><a href=\"javascript:eliminar('" + barrio.getId()  + "')\">Eliminar</a></td>");
    					out.println("</tr>");
    					fila++;
    				}
    				out.println("<tr><td colspan=4>Total registros encontrados: " + lista.size() + "</td></tr>");
    			}else {
    				out.println("<tr><td colspan=4>No se encontraron registros</td></tr>");
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
