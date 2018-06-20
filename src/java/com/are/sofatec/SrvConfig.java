package com.are.sofatec;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class srvLocalidad
 */
@WebServlet("/SrvConfig")
public class SrvConfig extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SrvConfig() {
        super();
        // TODO Auto-generated constructor stub
    }

    protected void ProcesarPeticion(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
    	response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();

		String operacion = (String) request.getParameter("operacion");
		db conexion = null;



		if (operacion.equals("save")) { // agregar Localidad
			String user = (String) request.getParameter("user");
			String sello1 = (String) request.getParameter("sello1");
			String sello2 = (String) request.getParameter("sello2");
			String acometida1 = (String) request.getParameter("acometida1");
			String acometida2 = (String) request.getParameter("acometida2");
			String empleado = (String) request.getParameter("empleado");
			try {
				conexion = new db();
				Config config = new Config();
				config.setUser(user);
				config.setCodigoSello1(sello1);
				config.setCodigoSello2(sello2);
				config.setCodigoAcometida1(acometida1);
				config.setCodigoAcometida2(acometida2);
				config.setNumEmpleado(empleado);
				ManejadorConfig MC = new ManejadorConfig(conexion);
					if (MC.Save(config)) {
						out.print("OK");
					} else {
						out.print("Error al procesar la solicitud");
					}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				out.print("Error de conexion con el servidor: "
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

		out.close();
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
