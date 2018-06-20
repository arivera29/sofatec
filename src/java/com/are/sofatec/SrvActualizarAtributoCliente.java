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
 * Servlet implementation class SrvCordenadasCliente
 */
@WebServlet("/SrvActualizarAtributoCliente")
public class SrvActualizarAtributoCliente extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SrvActualizarAtributoCliente() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    protected void ProcesarPeticion(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		String nic = (String)request.getParameter("nic");
		String atributo = (String)request.getParameter("atributo");
		
		db conexion = null;
		
		try {
			conexion = new db();
			String sql ="SELECT NIC, ID_ATRIBUTO FROM qo_cliente_atributo WHERE NIC=? and ID_ATRIBUTO=?";
			java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
			pst.setString(1, nic);
			pst.setString(2, atributo);
			
			java.sql.ResultSet rs = conexion.Query(pst);
			if (rs.next()) {
				out.print("Atributo ya se encuentra registrado");
			}else {
				sql = "INSERT INTO qo_cliente_atributo (NIC, ID_ATRIBUTO) VALUES (?,?)";
				pst = conexion.getConnection().prepareStatement(sql);
				pst.setString(1, nic);
				pst.setString(2, atributo);
				if (conexion.Update(pst) > 0) {
					conexion.Commit();
					out.print("OK");
				}else {
					out.print("ERROR.  Procesando el registro en la BD");
				}
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
