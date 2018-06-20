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
import java.sql.SQLException;

import com.csvreader.CsvReader;

/**
 * Servlet implementation class ProcessOrders
 */
@WebServlet("/ProcessResource")
public class ProcessResource extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ProcessResource() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void ProcesarPeticion(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
    	response.setContentType("text/plain;charset=UTF-8");
    	response.setHeader("Content-Disposition", "attachment; filename=\"log_carga_recurso.txt\"");
		PrintWriter out = response.getWriter(); 
		ServletContext servletContext = getServletContext();
		HttpSession sesion=request.getSession();
		if (sesion.getAttribute("usuario") == null) {
			out.print("La sesión ha caducado... intente de nuevo por favor");
			return ;
		}
		
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
		 if (headers.length != 8) { // estan las columnas OK.
			  throw  new IOException("Archivo no cargado.  Numero de columnas no coinciden con la estructura del archivo de recurso");
		 }
		 for (int x=0; x<headers.length; x++) {
				out.print(headers[x] + reader.getDelimiter());
			}	
		 out.println("Resultado carga");  // fin de encabezados
		 
		 conexion = new db();
			 
		 while(reader.readRecord()){
			
			 int col = reader.getColumnCount();	
				for (int x=0; x<col; x++) {
					out.print(reader.get(x)+ reader.getDelimiter());
				}
					
				recursohumano recurso = new recursohumano(conexion);
				try {
					if (!recurso.Find(reader.get(0))) {
						recurso.setCodigo(reader.get(0));
						recurso.setNombre(reader.get(1));
						recurso.setDireccion(reader.get(2));
						recurso.setTelefono(reader.get(3));
						recurso.setCorreo(reader.get(4));
						recurso.setCargo(reader.get(5));
						recurso.setRol(reader.get(6));
						recurso.setEstado(reader.get(7));
						
						if (recurso.add()) {  // agregar cliente
							out.println("AGREGADO");
						}else {
							out.println("NO AGREGADO");
						}
					}else {
						recurso.setCodigo(reader.get(0));
						recurso.setNombre(reader.get(1));
						recurso.setDireccion(reader.get(2));
						recurso.setTelefono(reader.get(3));
						recurso.setCorreo(reader.get(4));
						recurso.setCargo(reader.get(5));
						recurso.setRol(reader.get(6));
						recurso.setEstado(reader.get(7));
						if (recurso.modify(reader.get(0))) {  // modificar cliente
							out.println("MODIFICADO");
						}else {
							out.println("NO MODIFICADO");
						}
					}
				}catch (SQLException e) {
					out.println(e.getMessage()); 
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
