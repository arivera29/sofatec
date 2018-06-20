package com.are.sofatec;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class SrvAsignarSeleccion
 */
@WebServlet("/SrvAsignarSeleccion")
public class SrvAsignarSeleccion extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SrvAsignarSeleccion() {
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
		
		String cedula = (String)request.getParameter("recurso");
		
		db conexion = null;
		try {
			
		conexion = new db();
		
		if (!cedula.equals("")) {
			String[] ordenes = request.getParameterValues("orden");
			if (ordenes.length > 0) {
				Ordenes orden = new Ordenes(conexion);
				int asignadas = 0;
				for (int i=0; i< ordenes.length; i++) {
					if (orden.AsignarRecurso(ordenes[i], cedula, 0,false)) {
						asignadas ++;
					}
					
				}
				if (asignadas == ordenes.length) {
					conexion.Commit();
					out.print("OK");
				}else {
					conexion.Rollback();
					throw	new  Exception ("Error al procesar las ordenes");
				}
				
			}else {
				out.println("No hay ordenes seleccionadas");
				
			}
		}else {
			out.println("Cedula no recibida");
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

}
