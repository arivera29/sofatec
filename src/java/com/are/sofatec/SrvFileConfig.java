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
 * Servlet implementation class XmlOrders
 */
@WebServlet("/SrvFileConfig")
public class SrvFileConfig extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SrvFileConfig() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    protected void ProcesarPeticion(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
    	
    	response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        
        String archivo = (String)request.getParameter("archivo");
        db conexion = null;
        
        try {
        	
        	conexion = new db();
        	
        	if (archivo.equals("anomalia")) {
        	
	        	String sql = "SELECT * FROM QO_ANOM";
	        	java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
	        	//pst.setString(1, imei);
	        	ResultSet rs = conexion.Query(pst);
	        	out.print("3ANOM\r\n");
	        	while(rs.next()) {
	        		String cadena = "1" + rs.getString("COD") + ",";
	        		cadena += rs.getString("DESC_COD") ;
	        		out.print(cadena+"\r\n");	
	        	}
        	
        	}
        	
        	if (archivo.equals("materiali")) {
            	
	        	String sql = "SELECT * FROM QO_DESC_MI";
	        	java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
	        	//pst.setString(1, imei);
	        	ResultSet rs = conexion.Query(pst);
	        	out.print("3DESC_MI\r\n");
	        	while(rs.next()) {
	        		String cadena = "1" + rs.getString("CO_ELEMENTO") + ",";
	        		cadena += rs.getString("DESC_ELEMENTO").replace(',', '.') + ",";
	        		cadena += rs.getString("NUEVO_FLUJO") ;
	        		out.print(cadena+"\r\n");	
	        	}
	        	
	        	sql = "SELECT * FROM QO_MATER_I";
	        	pst = conexion.getConnection().prepareStatement(sql);
	        	//pst.setString(1, imei);
	        	ResultSet rs1 = conexion.Query(pst);
	        	out.print("3MATER_I\r\n");
	        	while(rs1.next()) {
	        		String cadena = "1" + rs1.getString("COD_CLASE") + ",";
	        		cadena += rs1.getString("CO_ACCEJE") + ",";
	        		cadena += rs1.getString("CO_ELEMENTO") + ",";
	        		cadena += rs1.getString("TIP_ELEMENTO") + ",";
	        		cadena += rs1.getString("PREF_ELEMENTO") + ",";
	        		cadena += rs1.getString("IND_FORMATO") + ",";
	        		cadena += rs1.getString("NUEVO_FLUJO") ;
	        		out.print(cadena+"\r\n");	
	        	}
        	
        	}
        	
        	if (archivo.equals("materialr")) {
            	
	        	String sql = "SELECT * FROM QO_DESC_MR";
	        	java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
	        	//pst.setString(1, imei);
	        	ResultSet rs = conexion.Query(pst);
	        	out.print("3DESC_MR\r\n");
	        	while(rs.next()) {
	        		String cadena = "1" + rs.getString("CO_ELEMENTO") + ",";
	        		cadena += rs.getString("DESC_ELEMENTO").replace(',', '.') + ",";
	        		cadena += rs.getString("NUEVO_FLUJO") ;
	        		out.print(cadena+"\r\n");	
	        	}
	        	
	        	sql = "SELECT * FROM QO_MATER_R";
	        	pst = conexion.getConnection().prepareStatement(sql);
	        	//pst.setString(1, imei);
	        	ResultSet rs1 = conexion.Query(pst);
	        	out.print("3MATER_R\r\n");
	        	while(rs1.next()) {
	        		String cadena = "1" + rs1.getString("COD_CLASE") + ",";
	        		cadena += rs1.getString("CO_ACCEJE") + ",";
	        		cadena += rs1.getString("CO_ELEMENTO") + ",";
	        		cadena += rs1.getString("TIP_ELEMENTO") + ",";
	        		cadena += rs1.getString("PREF_ELEMENTO") + ",";
	        		cadena += rs1.getString("IND_FORMATO") + ",";
	        		cadena += rs1.getString("NUEVO_FLUJO") ;
	        		out.print(cadena+"\r\n");	
	        	}
        	
        	}
        	
        	if (archivo.equals("acciones")) {
            	
	        	String sql = "SELECT * FROM QO_OSACCION";
	        	java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
	        	//pst.setString(1, imei);
	        	ResultSet rs = conexion.Query(pst);
	        	out.print("3DESC_MR\r\n");
	        	while(rs.next()) {
	        		String cadena = "1" + rs.getString("TIP_OS") + ",";
	        		cadena += rs.getString("NUM_PASO") + ",";
	        		cadena += rs.getString("CO_ACCEJE") + ",";
	        		cadena += rs.getString("IND_LIQUIDABLE") + ",";
	        		cadena += rs.getString("DESC_COD");
	        		out.print(cadena+"\r\n");	
	        	}
        	
        	}
        	
        	if (archivo.equals("observaciones")) {
            	
	        	String sql = "SELECT * FROM QO_OBSERVACION";
	        	java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
	        	//pst.setString(1, imei);
	        	ResultSet rs = conexion.Query(pst);
	        	out.print("3OBSERVACION\r\n");
	        	while(rs.next()) {
	        		String cadena = "1" + rs.getString("ID") + ",";
	        		cadena += rs.getString("CODIGO") + ",";
	        		cadena += rs.getString("OBSERVACION").trim().replace("\r\n","") + ",";
	        		cadena += rs.getString("TIPO");
	        		out.print(cadena+"\r\n");	
	        	}
        	
        	}
        	
        	if (archivo.equals("atributos")) {
            	
	        	String sql = "SELECT * FROM QO_ATRIBUTOS";
	        	java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
	        	//pst.setString(1, imei);
	        	ResultSet rs = conexion.Query(pst);
	        	out.print("3ATRIBUTOS\r\n");
	        	while(rs.next()) {
	        		String cadena = "1" + rs.getString("ID") + ",";
	        		cadena += rs.getString("DESCRIPCION").replace(",", ".") ;
	        		out.print(cadena+"\r\n");	
	        	}
        	
        	}
        	
        	if (archivo.equals("codigos")) {
            	
	        	String sql = "SELECT * FROM QO_CODIGOS";
	        	java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
	        	//pst.setString(1, imei);
	        	ResultSet rs = conexion.Query(pst);
	        	out.print("3CODIGOS\r\n");
	        	while(rs.next()) {
	        		String cadena = "1" + rs.getString("COD") + "	";
	        		cadena += rs.getString("DESC_COD") ;
	        		out.print(cadena+"\r\n");	
	        	}
        	
        	}
        	
        	if (archivo.equals("tipos")) {
            	
	        	String sql = "SELECT * FROM QO_TIPOS";
	        	java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
	        	//pst.setString(1, imei);
	        	ResultSet rs = conexion.Query(pst);
	        	out.print("3TIPOS\r\n");
	        	while(rs.next()) {
	        		String cadena = "1" + rs.getString("TIPO") + "	";
	        		cadena += rs.getString("DESC_TIPO") ;
	        		out.print(cadena+"\r\n");	
	        	}
        	
        	}
        	
        	
        	if (archivo.equals("flujos")) {
            	
	        	String sql = "SELECT * FROM QO_OSFLUJO";
	        	java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
	        	//pst.setString(1, imei);
	        	ResultSet rs = conexion.Query(pst);
	        	out.print("3OSFLUJOS\r\n");
	        	while(rs.next()) {
	        		String cadena = "1" + rs.getString("TIP_FLUJO") + "	";
	        		cadena += rs.getString("TIP_OS") + "	" ;
	        		cadena += rs.getString("CO_ACCEJE") ;
	        		out.print(cadena+"\r\n");	
	        	}
	        	rs.close();
	        	
	        	sql = "SELECT * FROM QO_PASOSFLUJO";
	        	pst = conexion.getConnection().prepareStatement(sql);
	        	//pst.setString(1, imei);
	        	rs = conexion.Query(pst);
	        	out.print("3PASOSFLUJO\r\n");
	        	while(rs.next()) {
	        		String cadena = "1" + rs.getString("TIP_FLUJO") + "	";
	        		cadena += rs.getString("NUM_PASO") + "	" ;
	        		cadena += rs.getString("OPCOND") + "	" ;
	        		cadena += rs.getString("DESCRIPCION") + "	" ;
	        		cadena += rs.getString("CONDICION") + "	" ;
	        		cadena += rs.getString("ELSEACCION") + "	" ;
	        		cadena += rs.getString("CO_ACCEJE") ;
	        		out.print(cadena+"\r\n");	
	        	}
	        	
	        	rs.close();
	        	
	        	sql = "SELECT * FROM QO_OSACCIONFLUJO";
	        	pst = conexion.getConnection().prepareStatement(sql);
	        	//pst.setString(1, imei);
	        	rs = conexion.Query(pst);
	        	out.print("3OSACCIONFLUJO\r\n");
	        	while(rs.next()) {
	        		String cadena = "1" + rs.getString("TIP_OS") + "	";
	        		cadena += rs.getString("NUM_PASO") + "	" ;
	        		cadena += rs.getString("CO_ACCEJE") + "	" ;
	        		cadena += rs.getString("IND_LIQUIDABLE") + "	" ;
	        		cadena += rs.getString("DESC_COD") + "	" ;
	        		cadena += rs.getString("TIP_FLUJO") ;
	        		out.print(cadena+"\r\n");	
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
	
	public String LimpiarCadena(String cadena) {
		cadena = cadena.trim();
		cadena = cadena.replace("\r", "");
		cadena = cadena.replace("\n", "");
		cadena = cadena.replace("\t","");
		cadena = cadena.replace("<","");
		cadena = cadena.replace(">","");
		cadena = cadena.replace("?","");
		cadena = cadena.replace("&","");
		return cadena;
	}

}
