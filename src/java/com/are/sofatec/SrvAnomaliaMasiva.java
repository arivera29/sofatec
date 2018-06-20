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
 * Servlet implementation class SrvListAssign
 */
@WebServlet("/SrvAnomaliaMasiva")
public class SrvAnomaliaMasiva extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SrvAnomaliaMasiva() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void ProcesarPeticion(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();

		String anomalia = (String) request.getParameter("anomalia");
		String recurso = (String)request.getParameter("recurso");
		String observacion = (String)request.getParameter("observacion");
		
		String[] ordenes = request.getParameterValues("ordenes");
		
		

		db conexion = null;
		try {

			if (ordenes == null) {
				throw new Exception("Error al recibir parametro ordenes");
			}
			if (ordenes.length == 0) {
				throw new Exception("Error indice parametro ordenes");
			}
			
			conexion = new db();
			int cont=0;
			
			for (int i=0 ; i < ordenes.length; i++) {
			
			
				String sql = "select ESTADO_OPER from qo_ordenes where NUM_OS=?";
				java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
				pst.setString(1, ordenes[i]);
				
				ResultSet rs = conexion.Query(pst);
				if (!rs.next()) {
					throw new Exception("Orden de servicio no se encuentra registrada");
				}
				
				String estado = (String)rs.getString("ESTADO_OPER");
				if (estado.equals("3") || estado.equals("99") ) {
					throw new Exception("Estado de la OS no permitido para registrar anomalia");
				}
				
				sql = "UPDATE QO_ORDENES SET " +
						"ESTADO_OPER=3, " +
						"TECNICO=?, " +
						"FECHA_ANOMALIA=sysdate()," +
						"IMEI=?," +
						"ANOMALIA=?, " +
						"OBSERVACION=? " +
						" WHERE NUM_OS = ? " +
						" AND ESTADO_OPER IN ('0','1')";
				pst = conexion.getConnection().prepareStatement(sql);
				pst.setString(1, recurso);
				pst.setString(2, "");
				pst.setString(3, anomalia);
				pst.setString(4, observacion);
				pst.setString(5, ordenes[i]);
				
				if (conexion.Update(pst)> 0) {
					cont++;
				}
			
			
			}
			
			
			if (cont == ordenes.length) {
				conexion.Commit();
				out.print("OK");
			}else {
				
				out.print("Error al actualizar el registro");
			}
			
			
			

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			out.print(e.getMessage());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			out.print(e.getMessage());
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
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		this.ProcesarPeticion(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		this.ProcesarPeticion(request, response);
	}

}
