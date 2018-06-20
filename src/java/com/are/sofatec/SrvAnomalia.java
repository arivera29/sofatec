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
@WebServlet("/SrvAnomalia")
public class SrvAnomalia extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SrvAnomalia() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void ProcesarPeticion(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();

		String imei = (String) request.getParameter("imei");
		String anomalia = (String) request.getParameter("anomalia");
		String observacion = (String) request.getParameter("observacion");
		String orden = (String) request.getParameter("orden");

		db conexion = null;
		try {

			conexion = new db();

			Equipos eq = new Equipos(conexion);
			if (!eq.Find(imei)) {
				throw new Exception("IMEI no se encuentra registrado");
			}

			if (eq.getActivo() != 1) {
				throw new Exception("IMEI no se encuentra activo");
			}
			
			String sql = "select COD from qo_anom where cod=?";
			java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
			pst.setString(1, anomalia);
			ResultSet rs1 = conexion.Query(pst);
			if (!rs1.next()) {
				throw new Exception("Codigo de anomalia no se encuentra registrado");
			}
			
			sql = "select ESTADO_OPER from qo_ordenes where NUM_OS=?";
			pst = conexion.getConnection().prepareStatement(sql);
			pst.setString(1, orden);
			
			ResultSet rs2 = conexion.Query(pst);
			if (!rs2.next()) {
				throw new Exception("Orden de servicio no se encuentra registrada");
			}
			
			String estado = (String)rs2.getString("ESTADO_OPER");
			if (!estado.equals("1") ) {
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
			pst.setString(1, eq.getRecurso());
			pst.setString(2, imei);
			pst.setString(3, anomalia);
			pst.setString(4, removeAcentos(observacion));
			pst.setString(5, orden);
			
			if (conexion.Update(pst) > 0) {
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
	
	/**
	 * Funci�n que elimina acentos y caracteres especiales de
	 * una cadena de texto.
	 * @param input
	 * @return cadena de texto limpia de acentos y caracteres especiales.
	 */
	public String removeAcentos(String input) {
	    // Cadena de caracteres original a sustituir.
	    String original = "áéíóúñÑ";
	    // Cadena de caracteres ASCII que reemplazaran los originales.
	    String ascii = "aeiounn";
	    String output = input;
	    for (int i=0; i<original.length(); i++) {
	        // Reemplazamos los caracteres especiales.
	        output = output.replace(original.charAt(i), ascii.charAt(i));
	    }//for i
	    return output;
	}

}
