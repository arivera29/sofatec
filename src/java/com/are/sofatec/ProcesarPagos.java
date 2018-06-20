package com.are.sofatec;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.csvreader.CsvReader;

/**
 * Servlet implementation class ProcessOrders
 */
@WebServlet("/ProcesarPagos")
public class ProcesarPagos extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ProcesarPagos() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void ProcesarPeticion(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
    	response.setContentType("text/plain;charset=UTF-8");
    	response.setHeader("Content-Disposition", "attachment; filename=\"log_procesamiento_pagos.txt\"");
		PrintWriter out = response.getWriter(); 
		ServletContext servletContext = getServletContext();
		
		HttpSession sesion=request.getSession();
		
		if (sesion.getAttribute("usuario") == null) {
			out.print("La sesion ha caducado... intente de nuevo por favor");
			return ;
		}
		String usuario = (String)sesion.getAttribute("usuario");
		String filename = (String)request.getParameter("file");
		String separador = (String)request.getParameter("separador");
		String path = servletContext.getRealPath("/upload") +   File.separator +filename;
		CsvReader reader = null;
		db conexion = null;
		try {
		 
			reader = new CsvReader(path);
		
		 
		 
		 if (separador.equals("1")) {
			 reader.setDelimiter('	');  // tabulador
		 }else if (separador.equals("2")) {
			 reader.setDelimiter(','); // coma
		 }else if (separador.equals("3")){
			 reader.setDelimiter(';');  // punto y coma
		 }else {
			 reader.setDelimiter(','); 
		 }
		 
		 reader.readHeaders();
		 
		 String[] headers = reader.getHeaders();
		 if (headers.length != 2) { // estan las columnas OK.
			  throw  new IOException("Archivo no cargado.  Numero de columnas no coinciden con la estructura del archivo de pagos (Col1: OS, Col2: NIC");
		 }
		 for (int x=0; x<headers.length; x++) {
				out.print(headers[x] + reader.getDelimiter());
			}	
		 out.print("Resultado carga\r\n");  // fin de encabezados
		 
		 conexion = new db();
			 
		 while(reader.readRecord()){
			
			 int col = reader.getColumnCount(); 
				for (int x=0; x<col; x++) {
					out.print(reader.get(x)+ reader.getDelimiter());
				}
					
				String orden = reader.get(0); // orden de servicio
				String nic = reader.get(1);  // nic
				try {
					String sql = "select orden,recurso,accion,estado from orders where orden=? and nic=?";
					java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
					pst.setString(1, orden);
					pst.setString(2, nic);
					
					ResultSet rs = conexion.Query(pst);
					if (rs.next()) {
						if (rs.getString("estado").equals("1") || rs.getString("estado").equals("2") ) {
							sql = "UPDATE ORDERS SET ESTADO=99,ACCION='A',ACTIVIDAD='110',OBSERVACION=CONCAT('ANULADA PROCESAMIENTO ARCHIVO DE PAGOS SOFATEC USUARIO ',?),LECTURA=1,FECHA_CIERRE=SYSDATE(),RECURSO=?,AN_WS=1 WHERE orden=? and estado IN(1,2)";
							pst = conexion.getConnection().prepareStatement(sql);
							pst.setString(1, usuario);
							pst.setString(2, rs.getString("recurso"));
							pst.setString(3, orden);
							
							if (conexion.Update(pst) > 0) {
								conexion.Commit();
								out.print("ANULADA\r\n");
							}else {
								out.print("ERROR\r\n");
							}
						}else if (rs.getString("estado").equals("99")) {
							if (rs.getString("accion").equals("A")) {
								out.print("ORDEN YA SE ENCUENTRA ANULADA\r\n");
							}else {
								out.print("ORDEN RESUELTA, VALIDAR PARA GENERAR RECONEXION\r\n");
							}
						}
					}else {
						out.print("ORDEN NO ENCONTRADA\r\n");
					}
				
				}catch (SQLException e) {
					out.print(e.getMessage() + "\r\n"); 
				}catch (NumberFormatException e) {
					out.print("Formato de numero incorrecto. " + e.getMessage() + "\r\n");
				}
		 }
		 
		 
		 
		 //out.println("</table>");
		 
		} catch (FileNotFoundException e) {
			out.println(e.getMessage());
		} catch (IOException e) {
			out.println(e.getMessage());
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			reader.close();
			if (conexion != null) {
				try {
					conexion.Close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
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
