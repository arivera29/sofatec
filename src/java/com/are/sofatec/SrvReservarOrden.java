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
 * Servlet implementation class SrvGps
 */
@WebServlet("/SrvReservarOrden")
public class SrvReservarOrden extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SrvReservarOrden() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    protected void ProcesarPeticion(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		String orden = (String)request.getParameter("orden");
		String imei = (String)request.getParameter("imei");
		
		
		
		db conexion =  null;
		
		try {
			conexion = new db();
			
			Equipos equipo = new Equipos(conexion);
			if (!equipo.Find(imei)) {
				throw new SQLException("Equipo no registrado");
			}
			
			String recurso = equipo.getRecurso();
			
			String sql = "SELECT RESERVA FROM QO_ORDENES WHERE TECNICO = ? AND ESTADO_OPER=1 AND RESERVA=1 AND NUM_OS != ?";
			java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
			pst.setString(1, recurso);
			pst.setString(2, orden);
			ResultSet rsReservadas = conexion.Query(pst);
			if (rsReservadas.next()) {
				throw new SQLException("Ya tiene ordenes reservadas, debe liberarlas para poder realizar la reserva");
			}
			
			sql = "SELECT ESTADO_OPER,RESERVA FROM QO_ORDENES WHERE NUM_OS=?";
			pst = conexion.getConnection().prepareStatement(sql);
			pst.setString(1, orden);
			
			ResultSet rs = conexion.Query(pst);
			if (rs.next()) {
				if (rs.getInt("ESTADO_OPER") == 99) {
					throw new SQLException("Orden de servicio se encuentra cerrada");
				}
				if (rs.getInt("ESTADO_OPER") == 0) {
					throw new SQLException("Orden de servicio se encuentra generada");
				}
				if (rs.getInt("ESTADO_OPER") == 3) {
					throw new SQLException("Orden de servicio se encuentra reportada como ANOMALA");
				}
				
				int reserva = 1;
				if (rs.getInt("RESERVA") == 1) {
					reserva = 0;
				}
				
				sql = "UPDATE QO_ORDENES SET RESERVA=? WHERE NUM_OS=? AND ESTADO_OPER = 1";
				pst = conexion.getConnection().prepareStatement(sql);
				pst.setInt(1, reserva);
				pst.setString(2, orden);
				
				if (conexion.Update(pst) > 0) {
					conexion.Commit();
					out.print("OK");
				}else {
					out.print("Error al cambiar estado de reserva");
				}
			
			}else {
				throw new SQLException("Orden de servicio no encontrada");
			}
			
			
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			out.print(e.getMessage());
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
