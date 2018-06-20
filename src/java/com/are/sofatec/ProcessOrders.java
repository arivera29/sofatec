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
@WebServlet("/ProcessOrders")
public class ProcessOrders extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ProcessOrders() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void ProcesarPeticion(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
    	response.setContentType("text/plain;charset=UTF-8");
    	response.setHeader("Content-Disposition", "attachment; filename=\"log_carga_ordenes.txt\"");
		PrintWriter out = response.getWriter(); 
		ServletContext servletContext = getServletContext();
		HttpSession sesion=request.getSession();
		if (sesion.getAttribute("usuario") == null) {
			out.print("La sesion ha caducado... intente de nuevo por favor");
			return ;
		}
		
		String filename = (String)request.getParameter("file");
		String separador = (String)request.getParameter("separador");
		String path = servletContext.getRealPath("/upload") +   File.separator +filename;
		String departamento = (String)request.getParameter("departamento");
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
		 }else if (separador.equals("4")){
			 reader.setDelimiter('|');  // punto y coma
		 }else {
			 reader.setDelimiter(','); 
		 }
		 
		 reader.readHeaders();
		 
		 String[] headers = reader.getHeaders();
		 if (headers.length != 68) { // estan las columnas OK.
			  throw  new IOException("Archivo no cargado.  Numero de columnas no coinciden con la estructura del archivo de ordenes " + headers.length);
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
					
				Ordenes orden = new Ordenes(conexion);
				orden.setOrden(reader.get(0));
				orden.setTipo(reader.get(1));
				orden.setNic(reader.get(2));
				orden.setNis(reader.get(3));
				orden.setFtratamiento(reader.get(4));
				orden.setMunicipio(reader.get(5));
				orden.setLocalidad(reader.get(6));
				orden.setVia(reader.get(7));
				orden.setCalle(reader.get(8));
				orden.setCrucero(reader.get(9));
				orden.setPlaca(reader.get(10));
				orden.setInterior(reader.get(11));
				orden.setRdireccion(reader.get(12));
				orden.setCodigocliente(reader.get(13));
				orden.setCuenta(reader.get(14));
				orden.setNombre(reader.get(15));
				orden.setApellido1(reader.get(16));
				orden.setApellido2(reader.get(17));
				orden.setDeuda(reader.get(18));
				orden.setCantfactura(reader.get(19));
				orden.setTarifa(reader.get(20));
				orden.setNum_apa(reader.get(21));
				orden.setCodmarca(reader.get(22));
				orden.setDescmarca(reader.get(23));
				orden.setTipo_apa(reader.get(24));
				orden.setDesc_tipo_apa(reader.get(25));
				orden.setNum_rue(reader.get(26));
				orden.setAol_apa(reader.get(27));
				orden.setAmperios(reader.get(28));
				orden.setFinstalacion(reader.get(29));
				orden.setFfabricacion(reader.get(30));
				orden.setTipo_tension(reader.get(31));
				orden.setDesc_tipo_tension(reader.get(32));
				orden.setTipo_csmo(reader.get(33));
				orden.setDesc_tipo_csmo(reader.get(34));
				orden.setRuta(reader.get(35));
				orden.setItin(reader.get(36));
				orden.setAol(reader.get(37));
				orden.setUsuario((String)sesion.getAttribute("usuario"));
				orden.setDepartamento(departamento);
				try {
					if (orden.AgregarOrden()) {
						out.println("AGREGADA");
					}else {
						out.println("NO AGREGADA");
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
