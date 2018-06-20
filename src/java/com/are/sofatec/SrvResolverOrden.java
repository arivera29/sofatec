package com.are.sofatec;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class SrvListAssign
 */
@WebServlet("/SrvResolverOrden")
public class SrvResolverOrden extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SrvResolverOrden() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void ProcesarPeticion(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();

		String operacion = (String) request.getParameter("operacion");
		db conexion = null;
		try {
			if (operacion.equals("add_sello")) {
				conexion = new db();
				String orden = (String)request.getParameter("orden");
				String medidor = (String)request.getParameter("medidor");
				String precinto = (String)request.getParameter("precinto");
				
				String sql = "INSERT INTO QO_ORDEN_PRECINTOS (NUM_OS, NUM_APA,NUM_PRECIN,AGREGADO) VALUES (?,?,?,1)";
				PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
				pst.setString(1, orden);
				pst.setString(2, medidor);
				pst.setString(3, precinto);

					if (conexion.Update(pst) > 0) {
						conexion.Commit();
						out.print("OK");
					}
			}
			
			if (operacion.equals("add_lectura")) {
				conexion = new db();
				String orden = (String)request.getParameter("orden");
				String medidor = (String)request.getParameter("medidor");
				String lectura = (String)request.getParameter("lectura");
				
				String sql = "INSERT INTO QO_ORDEN_LECTURA (NUM_OS, NUM_APA,LECTURA) VALUES (?,?,?)";
				PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
				pst.setString(1, orden);
				pst.setString(2, medidor);
				pst.setString(3, lectura);

					if (conexion.Update(pst) > 0) {
						conexion.Commit();
						out.print("OK");
					}
			}
			
			if (operacion.equals("update_orden")) {
				conexion = new db();
				String orden = (String)request.getParameter("orden");
				
				String sql = "UPDATE QO_ORDENES SET ESTADO_OPER=99 WHERE NUM_OS=?";
				PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
				pst.setString(1, orden);
					if (conexion.Update(pst) > 0) {
						conexion.Commit();
						out.print("OK");
					}
			}
			
			if (operacion.equals("remove_lectura")) {
				conexion = new db();
				String orden = (String)request.getParameter("orden");
				String medidor = (String)request.getParameter("medidor");
				
				String sql = "DELETE FROM QO_ORDEN_LECTURA WHERE NUM_OS=? AND NUM_APA=?";
				PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
				pst.setString(1, orden);
				pst.setString(2, medidor);

					if (conexion.Update(pst) > 0) {
						conexion.Commit();
						out.print("OK");
					}
			}
			
			if (operacion.equals("remove_sello")) {
				conexion = new db();
				String id = (String)request.getParameter("id");
				String sql = "DELETE FROM QO_ORDEN_PRECINTOS WHERE ID=?";
				PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
				pst.setString(1, id);
				if (conexion.Update(pst) > 0) {
					conexion.Commit();
					out.print("OK");
				}
					
			}
			
			if (operacion.equals("update_observacion")) {
				conexion = new db();
				String id = (String)request.getParameter("id");
				String tipo = (String)request.getParameter("tipo");
				String observacion = (String)request.getParameter("observacion");
				String sql = "UPDATE QO_PASOS SET OBSERVACION=? WHERE ID=?";
				if (tipo.equals("2")) {
					sql = "UPDATE QO_NUEVOS_PASOS SET OBSERVACION=? WHERE ID=?";
				}
				PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
				pst.setString(1, observacion);
				pst.setString(2, id);
				if (conexion.Update(pst) > 0) {
					conexion.Commit();
					out.print("OK");
				}
					
			}
			
			if (operacion.equals("remove_material")) {
				conexion = new db();
				String id = (String)request.getParameter("id");
				String sql = "DELETE FROM QO_ORDEN_MATERIAL WHERE ID=?";
				PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
				pst.setString(1, id);
					if (conexion.Update(pst) > 0) {
						conexion.Commit();
						out.print("OK");
					}
					
				
			}
			
			if (operacion.equals("update_medidor")) {
				conexion = new db();

				String medidor = (String)request.getParameter("medidor"); 
				String marca = (String)request.getParameter("marca"); 
				String propiedad = (String)request.getParameter("propiedad");
				String tipo = (String)request.getParameter("tipo");
				String aol = (String)request.getParameter("aol");
				String fases = (String)request.getParameter("fases");
				String tension = (String)request.getParameter("tension");
				String fecha_inst = (String)request.getParameter("fecha_inst");
				String intensidad = (String)request.getParameter("intensidad");
				String constante = (String)request.getParameter("constante");
				String fecha_fab = (String)request.getParameter("fecha_fab");
				String fecha_rev = (String)request.getParameter("fecha_rev");
				String regulador = (String)request.getParameter("regulador");
				String dimension = (String)request.getParameter("dimension");
				String fecha_cal = (String)request.getParameter("fecha_cal");
				String fecha_ver = (String)request.getParameter("fecha_ver");
				String material = (String)request.getParameter("material");
				String naturaleza = (String)request.getParameter("naturaleza");
				String diametro = (String)request.getParameter("diametro");
				String alta = (String)request.getParameter("alta");
				String lectura = (String)request.getParameter("lectura");
				String consumo = (String)request.getParameter("consumo");
				String ruedas = (String)request.getParameter("ruedas");
				String coeficiente = (String)request.getParameter("coeficiente"); 
				String orden = (String)request.getParameter("orden");
				String num_apa = (String)request.getParameter("key");
				
				String sql = "UPDATE QO_MEDIDOR SET NUM_APA=?,CO_MARCA=?,TIP_APA=?," +
						"CO_PROP_APA=?,AOL_APA=?,TIP_FASE=?,TIP_TENSION=?,TIP_INTENSIDAD=?," +
						"CTE_APA=?,F_FABRIC=?,F_UREVIS=?,REGULADOR=?,DIMEN_CONEX=?," +
						"F_PROX_CALIBRACION=?,F_PROX_VERIFICACION=?,TIP_MATERIAL=?," +
						"TIP_NATUR=?,DIAMETRO=?,ALTA=?,LECTURA=?,F_INST=?,TIP_CSMO=?,"
						+ "NUM_RUE=?,COEF_PER=? WHERE NUM_OS=? AND NUM_APA=?";
	        		
	        		java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
	        		pst.setString(1, medidor);
	        		pst.setString(2, marca);
	        		pst.setString(3, tipo);
	        		pst.setString(4, propiedad);
	        		pst.setString(5, aol);
	        		pst.setString(6, fases);
	        		pst.setString(7, tension);
	        		pst.setString(8, intensidad);
	        		pst.setString(9, constante);
	        		pst.setString(10, fecha_fab);
	        		pst.setString(11, fecha_rev);
	        		pst.setString(12, regulador);
	        		pst.setString(13, dimension);
	        		pst.setString(14, fecha_cal);
	        		pst.setString(15, fecha_ver);
	        		pst.setString(16, material);
	        		pst.setString(17, naturaleza);
	        		pst.setString(18, diametro);
	        		pst.setString(19, alta);
	        		pst.setString(20, lectura);
	        		pst.setString(21, fecha_inst);
	        		pst.setString(22, consumo);
	        		pst.setString(23, ruedas);
	        		pst.setString(24, coeficiente);
	        		pst.setString(25, orden);
	        		pst.setString(26, num_apa);
	        		
					if (conexion.Update(pst) >= 0) {
						conexion.Commit();
						out.print("OK");
					}
					
				
			}
			
			if (operacion.equals("add_medidor")) {
				conexion = new db();

				String medidor = (String)request.getParameter("medidor"); 
				String marca = (String)request.getParameter("marca"); 
				String propiedad = (String)request.getParameter("propiedad");
				String tipo = (String)request.getParameter("tipo");
				String aol = (String)request.getParameter("aol");
				String fases = (String)request.getParameter("fases");
				String tension = (String)request.getParameter("tension");
				String fecha_inst = (String)request.getParameter("fecha_inst");
				String intensidad = (String)request.getParameter("intensidad");
				String constante = (String)request.getParameter("constante");
				String fecha_fab = (String)request.getParameter("fecha_fab");
				String fecha_rev = (String)request.getParameter("fecha_rev");
				String regulador = (String)request.getParameter("regulador");
				String dimension = (String)request.getParameter("dimension");
				String fecha_cal = (String)request.getParameter("fecha_cal");
				String fecha_ver = (String)request.getParameter("fecha_ver");
				String material = (String)request.getParameter("material");
				String naturaleza = (String)request.getParameter("naturaleza");
				String diametro = (String)request.getParameter("diametro");
				String alta = (String)request.getParameter("alta");
				String lectura = (String)request.getParameter("lectura");
				String consumo = (String)request.getParameter("consumo");
				String ruedas = (String)request.getParameter("ruedas");
				String coeficiente = (String)request.getParameter("coeficiente"); 
				String orden = (String)request.getParameter("orden");
				
				String sql = "INSERT INTO QO_MEDIDOR (NUM_APA,CO_MARCA,TIP_APA," +
					"CO_PROP_APA,AOL_APA,TIP_FASE,TIP_TENSION,TIP_INTENSIDAD," +
					"CTE_APA,F_FABRIC,F_UREVIS,REGULADOR,DIMEN_CONEX," +
					"F_PROX_CALIBRACION,F_PROX_VERIFICACION,TIP_MATERIAL," +
					"TIP_NATUR,DIAMETRO,ALTA,LECTURA,F_INST,TIP_CSMO,NUM_RUE,COEF_PER,NUM_OS) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	        		
	        		java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
	        		pst.setString(1, medidor);
	        		pst.setString(2, marca);
	        		pst.setString(3, tipo);
	        		pst.setString(4, propiedad);
	        		pst.setString(5, aol);
	        		pst.setString(6, fases);
	        		pst.setString(7, tension);
	        		pst.setString(8, intensidad);
	        		pst.setString(9, constante);
	        		pst.setString(10, fecha_fab);
	        		pst.setString(11, fecha_rev);
	        		pst.setString(12, regulador);
	        		pst.setString(13, dimension);
	        		pst.setString(14, fecha_cal);
	        		pst.setString(15, fecha_ver);
	        		pst.setString(16, material);
	        		pst.setString(17, naturaleza);
	        		pst.setString(18, diametro);
	        		pst.setString(19, alta);
	        		pst.setString(20, lectura);
	        		pst.setString(21, fecha_inst);
	        		pst.setString(22, consumo);
	        		pst.setString(23, ruedas);
	        		pst.setString(24, coeficiente);
	        		pst.setString(25, orden);
	        		
					if (conexion.Update(pst) >= 0) {
						conexion.Commit();
						out.print("OK");
					}
					
				
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			out.print("Error de conexion con el servidor: " + e.getMessage());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			out.print("Error:  " + e.getMessage());
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
