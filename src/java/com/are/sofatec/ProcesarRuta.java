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
import java.util.ArrayList;
import java.util.List;

import com.csvreader.CsvReader;

/**
 * Servlet implementation class ProcessOrders
 */
@WebServlet("/ProcesarRuta")
public class ProcesarRuta extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ProcesarRuta() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void ProcesarPeticion(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
    	response.setContentType("text/plain;charset=UTF-8");
    	response.setHeader("Content-Disposition", "attachment; filename=\"ruta.txt\"");
		PrintWriter out = response.getWriter(); 
		ServletContext servletContext = getServletContext();
		HttpSession sesion=request.getSession();
		if (sesion.getAttribute("usuario") == null) {
			out.print("La sesion ha caducado... intente de nuevo por favor");
			return ;
		}
		
		String filename = (String)request.getParameter("file");
		String separador = (String)request.getParameter("separador");
		String segmentar = (String)request.getParameter("segmentar");
		String radio = (String)request.getParameter("radio");
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
		 if (headers.length != 1) { // estan las columnas OK.
			  throw  new IOException("El archivo solo debe contener una (1) columna con la orden de servicio");
		 }

		 out.println("NUM_OS|TIPO_ORDEN|DIRECCION|BARRIO|NIC|NIF|RUTA|AOL|ITINERARIO|LATITUD|LONGITUD|DISTANCIA(KM)|OBSERVACION");  // fin de encabezados
		 
		 conexion = new db();
		 ArrayList<datos> ordenes = new ArrayList<datos>();
		 
		 while(reader.readRecord()){
			 	
				datos os = new datos();
			 
			 	os.setOrden(reader.get(0));  // Numero de la orden de servicio
				
				Ordenes orden = new Ordenes(conexion);
				try {
					if (orden.Find(reader.get(0))) {
						
						os.setNic(orden.getNic());
						os.setTipo(orden.getTipo());
						os.setDireccion(orden.getDireccion()); 
						os.setBarrio(orden.getLocalidad());
						os.setRuta(Integer.parseInt(orden.getRuta()));
						os.setItinerario(Integer.parseInt(orden.getItin()));
						os.setAol(Integer.parseInt(orden.getAol()));
						
						// Consultando las coordenadas del cliente
						String sql = "SELECT CLIENTE.NIF,COORDENADAS.LATITUD,COORDENADAS.LONGITUD,COORDENADAS.AOL FROM CLIENTE,COORDENADAS WHERE NIC=? AND CLIENTE.NIF = COORDENADAS.NIF AND LONGITUD !=0 AND LATITUD != 0";
						java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
						pst.setString(1, orden.getNic());
						ResultSet rs = conexion.Query(pst);
						if (rs.next()) {
							os.setNif((String)rs.getString("NIF"));
							os.setLatitud(rs.getDouble("LONGITUD"));
							os.setLongitud(rs.getDouble("LATITUD"));
							os.setAol(rs.getInt("AOL"));
							os.setEstado(0);
						}else {
							os.setObservacion("INFORMACION GEO-REFERENCIA NO ENCONTRADA");
							os.setEstado(1);
						}
						
					}else {
						os.setObservacion("ORDEN DE SERVICIO NO ENCONTRADA");
						os.setEstado(1);
					}
				}catch (SQLException e) {
					os.setObservacion(e.getMessage());
					os.setEstado(1);
				}catch (NumberFormatException e) {
					os.setObservacion(e.getMessage());
					os.setEstado(1);
				}
				
				ordenes.add(os);
				
				//out.println(String.format("%s|%s|%s|%s|%s|%s|%s|%s", num_os,tipo_os,direccion,nic,nif,latitud,longitud,observacion));
				
			 
		 }
		 
		 ArrayList<datos> ruta = new ArrayList<datos>();
		 double lat_inicial = 11.00791332927005;
		 double lon_inicial = -74.78941372958225;
		 String Barrio = "";
		 int Ruta = 0;
		 
		 boolean seguir = true; 
		 // Procesando ruta
		 while (seguir) {
			 
			 // Calculando Distancias
				ordenes = Utilidades.CalcularDistancia(lat_inicial, lon_inicial, ordenes);
				// Buscar la mas cercana
				int index = Utilidades.MasCercana(ordenes,Barrio);
				
						
				if (index != -1) {
					ordenes.get(index).setEstado(2);
					ruta.add(ordenes.get(index));
					
					// Nueva posicion de referencia
					lat_inicial = ordenes.get(index).getLatitud();
					lon_inicial = ordenes.get(index).getLongitud();
					Barrio = ordenes.get(index).getBarrio();
					Ruta = ordenes.get(index).getRuta();
					
					if (segmentar.equals("1")) {
					Utilidades.QuitarSegmentos(ordenes);
					// Calculando Distancias con la nueva poscion de referencia
					double R = Double.parseDouble(radio);
					ordenes = Utilidades.CalcularDistancia(lat_inicial, lon_inicial, ordenes);
					// Segmentar
					int cnt_segmento = 0;
					for (int x=0;x < ordenes.size(); x++ ) {
					 if (ordenes.get(x).getEstado() == 0 ) {
							if (ordenes.get(x).getDistancia() <= R) { // ordenes cercanas en un radio de 2 Km.
								ordenes.get(x).setSegmento(1);
								cnt_segmento++;
							}
					 }
					}
					 
					 //Calcular distancia segmento
					 	if (cnt_segmento > 0) {
						 boolean pasa = true;
						 while (pasa) {
							
								// Calculando Distancias
								ordenes = Utilidades.CalcularDistanciaSegmento(lat_inicial, lon_inicial, ordenes);
								
								// Buscar la mas cercana
								
								int i = Utilidades.MasCercanaSegmento(ordenes);
								
								if (i != -1) {
									ordenes.get(i).setEstado(2);
									ordenes.get(i).setSegmento(0);
									ruta.add(ordenes.get(i));
									
									lat_inicial = ordenes.get(i).getLatitud();
									lon_inicial = ordenes.get(i).getLongitud();
									
								}
							 
							 
							 
							 int contador = 0;
							 for (datos os : ordenes) {
								 if (os.getEstado() == 0 && os.getSegmento()== 1) {
									 contador ++;
								 }
							 }
							 if (contador == 0) {
								 pasa = false;
							 }
						 }
					 
					 	}
				  
					
					
					}
				}
				 
				 

				 int contador = 0;
				 for (datos os : ordenes) {
					 if (os.getEstado() == 0) {
						 contador ++;
					 }
				 }
				 if (contador == 0) {
					 seguir = false;
				 }
			 
		 }
		 
		 for (datos os : ordenes) {
			 if (os.getEstado() == 1) {
				 ruta.add(os);
			 }
		 }

		 for (datos os : ruta) {
			 out.println(String.format("%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s", os.getOrden(),os.getTipo(),os.getDireccion(),os.getBarrio(),os.getNic(),os.getNif(),os.getRuta(),os.getAol(),os.getItinerario(),os.getLatitud(),os.getLongitud(),os.getDistancia(),os.getObservacion()));
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


