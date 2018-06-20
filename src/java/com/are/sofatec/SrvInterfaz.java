package com.are.sofatec;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class SrvListAssign
 */
@WebServlet("/SrvInterfaz")
public class SrvInterfaz extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final int BUFFER_SIZE = 1024;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SrvInterfaz() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void ProcesarPeticion(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=ISO-8859-1");
		PrintWriter out = response.getWriter();

		ServletContext servletContext = getServletContext();
		HttpSession sesion = request.getSession();
		if (sesion.getAttribute("usuario") == null) {
			out.print("La sesion ha caducado... intente de nuevo por favor");
			return;
		}

		String usuario = (String) sesion.getAttribute("usuario");
		String lote = (String) request.getParameter("lote");

		db conexion = null;
		try {

			

			String path = servletContext.getRealPath("/generated")
					+ File.separator + usuario.trim();
			File folder = new File(path);
			if (!folder.exists()) {
				folder.mkdirs();

			} else {
				
				if (!folder.isDirectory()) {
					folder.mkdirs();
				}
			}
			
			
			EliminarArchivos(path);
			
			conexion = new db();
			
			String sql = "select NUM_OS,ANOMALIA,NIC,NIS_RAD,SEC_NIS,NUM_LOTE,ESTADO,COD_EMP_ASIG,"
					+ "DATE_FORMAT(fecha_anomalia,'%d/%m/%Y %H:%i:%s') FECHA_ANOMALIA, "
					+ "OBSERVACION "
					+ " FROM QO_ORDENES "
					+ " WHERE NUM_LOTE = ? "
					+ " AND ESTADO_OPER = '3'";
			java.sql.PreparedStatement pst = conexion.getConnection()
					.prepareStatement(sql);
			pst.setString(1, lote);
			ResultSet rs = conexion.Query(pst);
			
			if (rs.next()) {
				//File archivo1 = new File(path + File.separator + "ANOMALIA.ODB");
				//archivo1.setWritable(true, false);
				//FileWriter fout1 = new FileWriter(archivo1, false);
	
				File archivo1 = new File(path + File.separator + "ANOMALIA.ODB");
				archivo1.setWritable(true, false);
				OutputStreamWriter fout1 = new OutputStreamWriter(new FileOutputStream(archivo1) , "ISO-8859-1");
				
				File archivo2 = new File(path + File.separator + "PASOS.ODB");
				archivo2.setWritable(true, false);
				FileWriter fout2 = new FileWriter(archivo2, false);
	
				File archivo3 = new File(path + File.separator + "RESULORD.ODB");
				archivo3.setWritable(true, false);
				FileWriter fout3 = new FileWriter(archivo3, false);
	
				do {
					
					
					String cadena = rs.getString("NUM_OS")
							+ rs.getString("ANOMALIA")
							+ rs.getString("FECHA_ANOMALIA");
					//String obs = new String (rs.getString("OBSERVACION").getBytes(),"ISO-8859-1");
					String obs = formatoString(rs.getString("OBSERVACION")).toUpperCase();
					if (!obs.equals("")) {
						if (obs.length() > 250) {
							cadena += obs.substring(0, 249);
						} else {
							cadena += obs;
						}
					}else {
						obs = Utilidades.Espacios(249);
					}
					fout1.write(cadena + "\r\n");
	
					cadena = rs.getString("NUM_OS");
					cadena += rs.getString("NIS_RAD");
					cadena += rs.getString("NIC");
					cadena += rs.getString("SEC_NIS");
					cadena += rs.getString("NUM_LOTE");
					cadena += rs.getString("FECHA_ANOMALIA");
					cadena += Utilidades.Espacios(10);
					cadena += "2";
					cadena += "1";
					cadena += rs.getInt("COD_EMP_ASIG");
					cadena += Utilidades.Espacios(32);
					fout3.write(cadena + "\r\n");
	
					sql = "SELECT QO_PASOS.* FROM QO_PASOS WHERE NUM_OS = ?";
					pst = conexion.getConnection().prepareStatement(sql);
					pst.setString(1, rs.getString("NUM_OS"));
					ResultSet rsPasos = conexion.Query(pst);
					while (rsPasos.next()) {
						cadena = rsPasos.getString("NUM_OS");
						cadena += Utilidades.LeftPad(rsPasos.getString("NUM_PASO"),	2, '0');
						cadena += rsPasos.getString("OPCOND");
						cadena += Utilidades.RightPad(rsPasos.getString("DESCRIPCION"), 8, ' ');
						cadena += rsPasos.getString("CONDICION");
						cadena += "000";
						cadena += Utilidades.Espacios(5);
						fout2.write(cadena + "\r\n");
	
					}
	
				}while (rs.next());
				
				fout1.close();
				fout2.close();
				fout3.close();
			}
			

			

			sql = "select NUM_OS,NIC,NIS_RAD,SEC_NIS,NUM_LOTE,ESTADO,COD_EMP_ASIG,"
					+ " DATE_FORMAT(fecha_cierre,'%d/%m/%Y %H:%i:%s') FECHA_CIERRE, " 
					+ " DATE_FORMAT(fecha_cierre,'%d/%m/%Y') FECHA, "
					+ " DATE_FORMAT(fecha_cierre,'%H:%i') HORA, "
					+ " IND_COBRO, CUOTAS, OBSERVACION "
					+ " FROM QO_ORDENES "
					+ " WHERE NUM_LOTE= ? "
					+ " AND ESTADO_OPER = '99'";
			pst = conexion.getConnection()
					.prepareStatement(sql);
			pst.setString(1, lote);

			ResultSet rsCierres = conexion.Query(pst);
			
			if (rsCierres.next()) {
				
				String cadena = "";
				
				File archivo0 = new File(path + File.separator + "log.txt");
				archivo0.setWritable(true, false);
				FileWriter fout0 = new FileWriter(archivo0, true);
				
				fout0.write("Archivos creados\r\n");
				
				File archivo1 = new File(path + File.separator + "RESULORD.ODB");
				archivo1.setWritable(true, false);
				FileWriter fout1 = new FileWriter(archivo1, true);
				
				File archivo2 = new File(path + File.separator + "PASOS.ODB");
				archivo2.setWritable(true, false);
				FileWriter fout2 = new FileWriter(archivo2, true);
				
				//File archivo3 = new File(path + File.separator + "OBSERVA.ODB");
				//archivo3.setWritable(true, false);
				//FileWriter fout3 = new FileWriter(archivo3, true);
				
				File archivo3 = new File(path + File.separator + "OBSERVA.ODB");
				archivo3.setWritable(true, false);
				OutputStreamWriter fout3 = new OutputStreamWriter(new FileOutputStream(archivo3) , "ISO-8859-1");
				
				File archivo4 = new File(path + File.separator + "LECTUAPA.ODB");
				archivo4.setWritable(true, false);
				FileWriter fout4 = new FileWriter(archivo4, true);
				
				File archivo5 = new File(path + File.separator + "VISITA.ODB");
				archivo5.setWritable(true, false);
				FileWriter fout5 = new FileWriter(archivo5, false);
				
				File archivo6 = new File(path + File.separator + "PRECIREL.ODB");
				archivo6.setWritable(true, false);
				FileWriter fout6 = new FileWriter(archivo6, true);
				
				File archivo7 = new File(path + File.separator + "SCONSTR.ODB");
				archivo7.setWritable(true, false);
				FileWriter fout7 = new FileWriter(archivo7, true);
				
				File archivo8 = new File(path + File.separator + "TIPPAGO.ODB");
				archivo8.setWritable(true, false);
				FileWriter fout8 = new FileWriter(archivo8, true);
				
				File archivo9 = new File(path + File.separator + "NUEVOAPA.ODB");
				archivo9.setWritable(true, false);
				FileWriter fout9 = new FileWriter(archivo9, false);
				
				File archivo10 = new File(path + File.separator + "NUEVOFLUJOPASOS.ODB");
				archivo10.setWritable(true, false);
				FileWriter fout10 = new FileWriter(archivo10, false);
				
				File archivo11 = new File(path + File.separator + "CONAPANU.ODB");
				archivo11.setWritable(true, false);
				FileWriter fout11 = new FileWriter(archivo11, false);
				
				File archivo12 = new File(path + File.separator + "ANOMALIA.ODB");
				archivo12.setWritable(true, false);
				FileWriter fout12 = new FileWriter(archivo12, true);
				
				
				fout0.write("Leyendo ordenes de servicio\r\n");
				
				do {
					fout0.write("Leyendo orden de servicio " + rsCierres.getString("NUM_OS") + "\r\n");
					cadena = rsCierres.getString("NUM_OS");
					cadena += rsCierres.getString("NIS_RAD");
					cadena += rsCierres.getString("NIC");
					cadena += Utilidades.LeftPad(rsCierres.getString("NUM_LOTE"),14,'0');
					cadena += rsCierres.getString("FECHA_CIERRE");
					cadena += Utilidades.Espacios(10);
					cadena += "1";
					cadena += "1";
					cadena += rsCierres.getInt("COD_EMP_ASIG");
					cadena += Utilidades.Espacios(32);
					fout1.write(cadena + "\r\n"); // ARCHIVO RESULORD.ODB
					
					fout0.write("Seleccionando pasos OS " + rsCierres.getString("NUM_OS") + "\r\n");
					
					sql = "SELECT QO_PASOS.* FROM QO_PASOS WHERE NUM_OS = ?";
					pst = conexion.getConnection().prepareStatement(sql);
					pst.setString(1, rsCierres.getString("NUM_OS"));
					ResultSet rsPasos = conexion.Query(pst);
					while (rsPasos.next()) {
						cadena = rsPasos.getString("NUM_OS");
						cadena += Utilidades.LeftPad(rsPasos.getString("NUM_PASO"),2, '0');
						cadena += rsPasos.getString("OPCOND");
						cadena += Utilidades.RightPad(rsPasos.getString("DESCRIPCION"), 8, ' ');
						cadena += rsPasos.getString("CONDICION");
						cadena += "001";
						cadena += rsPasos.getString("CO_ACCEJE");
						fout2.write(cadena + "\r\n"); // ARCHIVO PASOS.DBO
						
						String obs = "";
						if (rsPasos.getString("OBSERVACION") != null) {
							obs = formatoString(rsPasos.getString("OBSERVACION")).toUpperCase();
						}
						
						if (!obs.equals("")) {
							cadena = rsPasos.getString("NUM_OS");
							cadena += Utilidades.LeftPad(rsPasos.getString("NUM_PASO"),2, '0');
							if (obs.length() > 200) {
								cadena += obs.substring(0, 199);
							} else {
								cadena += Utilidades.RightPad(obs,200,' ');
							}
							cadena += Utilidades.RightPad(rsPasos.getString("DESCRIPCION"),8,' ');
							fout3.write(cadena + "\r\n"); // ARCHIVO DE OBSERVACION.ODB
						}else {
							obs = Utilidades.Espacios(249);
						}
						
						cadena = rsPasos.getString("NUM_OS");
						cadena += rsCierres.getString("FECHA");
						cadena += Utilidades.RightPad(rsPasos.getString("DESCRIPCION"), 5, ' ');
						cadena += rsCierres.getString("HORA");
						cadena += rsCierres.getString("HORA");
						cadena += Utilidades.LeftPad(rsCierres.getString("COD_EMP_ASIG"),6,'0');
						if (rsPasos.getInt("COBRO") == 1) {
							cadena += "1";
						}else {
							cadena += "2";
						}
						fout5.write(cadena + "\r\n");  // ARCHIVO DE VISITAS.ODB
						
					}
					
					fout0.write("Seleccionando NUEVOS pasos OS " + rsCierres.getString("NUM_OS") + "\r\n");
					sql = "SELECT QO_NUEVOS_PASOS.* FROM QO_NUEVOS_PASOS WHERE NUM_OS = ?";
					pst = conexion.getConnection().prepareStatement(sql);
					pst.setString(1, rsCierres.getString("NUM_OS"));
					ResultSet rsNuevoPasos = conexion.Query(pst);
					while (rsNuevoPasos.next()) {
						cadena = rsNuevoPasos.getString("NUM_OS");
						cadena += Utilidades.LeftPad(rsNuevoPasos.getString("NUM_PASO"),2, '0');
						cadena += rsNuevoPasos.getString("OPCOND");
						cadena += Utilidades.RightPad(rsNuevoPasos.getString("DESCRIPCION"), 8, ' ');
						cadena += rsNuevoPasos.getString("CONDICION");
						cadena += "001";
						cadena += rsNuevoPasos.getString("CO_ACCEJE");
						fout10.write(cadena + "\r\n"); // ARCHIVO NUEVOFLUJOPASOS.DBO
						
						String obs = formatoString(rsNuevoPasos.getString("OBSERVACION")).toUpperCase();
						
						if (!obs.equals("")) {
							cadena = rsNuevoPasos.getString("NUM_OS");
							cadena += Utilidades.LeftPad(rsNuevoPasos.getString("NUM_PASO"),2, '0');
							if (obs.length() > 200) {
								cadena += obs.substring(0, 199);
							} else {
								cadena += Utilidades.RightPad(obs,200,' ');
							}
							cadena += Utilidades.RightPad(rsNuevoPasos.getString("DESCRIPCION"),8,' ');
							fout3.write(cadena + "\r\n"); // ARCHIVO DE OBSERVACION.ODB
						}else {
							obs = Utilidades.Espacios(249);
						}
					
					cadena = rsNuevoPasos.getString("NUM_OS");
					cadena += rsCierres.getString("FECHA");
					cadena += Utilidades.RightPad(rsNuevoPasos.getString("DESCRIPCION"), 5, ' ');
					cadena += rsCierres.getString("HORA");
					cadena += rsCierres.getString("HORA");
					cadena += Utilidades.LeftPad(rsCierres.getString("COD_EMP_ASIG"),6,'0');
					
					if (rsNuevoPasos.getInt("COBRO") == 1) {
						cadena += "1";
					}else {
						cadena += "2";
					}
					fout5.write(cadena + "\r\n");  // ARCHIVO DE VISITAS.ODB
					}
					
					fout0.write("Seleccionando lectura OS " + rsCierres.getString("NUM_OS") + "\r\n");
					
					sql = "SELECT QO_ORDENES.NUM_OS,QO_ORDENES.NIS_RAD ,"
							+ "QO_ORDEN_LECTURA.NUM_APA, QO_ORDEN_LECTURA.LECTURA, "
							+ "QO_APACONEN.CO_MARCA,QO_APARATOS.TIP_APA, QO_APACONEN.TIP_CSMO,"
							+ "QO_APACONEN.NUM_RUE,QO_APACONEN.CTE "
							+ " FROM QO_ORDENES,QO_ORDEN_LECTURA,QO_APARATOS,QO_APACONEN"
							+ " WHERE QO_ORDENES.NUM_OS = ? " 
							+ " AND QO_ORDEN_LECTURA.NUM_OS = QO_ORDENES.NUM_OS"
							+ " AND QO_ORDEN_LECTURA.NUM_APA = QO_APARATOS.NUM_APA "
							+ " AND QO_ORDEN_LECTURA.NUM_OS = QO_APARATOS.NUM_OS "
							+ " AND QO_APARATOS.NUM_APA = QO_APACONEN.NUM_APA "
							+ " AND QO_APARATOS.NUM_OS = QO_APACONEN.NUM_OS";
					pst = conexion.getConnection().prepareStatement(sql);
					pst.setString(1, rsCierres.getString("NUM_OS"));
					ResultSet rsLectura = conexion.Query(pst);
					while (rsLectura.next()) {
						cadena = rsLectura.getString("NUM_OS");
						cadena += rsLectura.getString("NIS_RAD");
						cadena +=  Utilidades.RightPad(rsLectura.getString("NUM_APA").trim(),20,' ');
						cadena += rsLectura.getString("CO_MARCA");
						cadena += rsLectura.getString("TIP_CSMO");
						cadena += rsLectura.getString("TIP_APA");
						cadena += Utilidades.LeftPad(rsLectura.getString("NUM_RUE"),2,'0');
						cadena += "00";
						cadena += "0000000000001.0";
						cadena += Utilidades.LeftPad(rsLectura.getString("LECTURA").trim(),10,'0');
						fout4.write(cadena + "\r\n");  // ARCHIVO DE LECTURAS.ODB
					}
					
					fout0.write("Seleccionando presintos OS " + rsCierres.getString("NUM_OS") + "\r\n");
					
					sql = "SELECT QO_ORDEN_PRECINTOS.*,QO_APARATOS.CO_MARCA " 
							+ "FROM QO_ORDEN_PRECINTOS,QO_APARATOS "
							+ " WHERE QO_ORDEN_PRECINTOS.NUM_OS = ? " 
							+ " AND QO_ORDEN_PRECINTOS.NUM_OS = QO_APARATOS.NUM_OS "
							+ " AND QO_ORDEN_PRECINTOS.NUM_APA = QO_APARATOS.NUM_APA ";
					pst = conexion.getConnection().prepareStatement(sql);
					pst.setString(1, rsCierres.getString("NUM_OS"));
					ResultSet rsSellos = conexion.Query(pst);
					while (rsSellos.next()) {
						cadena = rsSellos.getString("NUM_OS");
						cadena +=  Utilidades.RightPad(rsSellos.getString("NUM_APA").trim(),20,' ');
						cadena += rsSellos.getString("CO_MARCA");
						cadena += Utilidades.RightPad(rsSellos.getString("NUM_PRECIN").trim(),10,' ');
						cadena += rsCierres.getString("FECHA");
						cadena += Utilidades.Espacios(10);
						//cadena += rsSellos.getString("AGREGADO").trim();
						fout6.write(cadena + "\r\n");  // ARCHIVO DE PRECIREL.ODB
					}
					
					fout0.write("Seleccionando material OS " + rsCierres.getString("NUM_OS") + "\r\n");
					sql = "SELECT QO_ORDEN_MATERIAL.*" 
							+ "FROM QO_ORDEN_MATERIAL "
							+ " WHERE QO_ORDEN_MATERIAL.NUM_OS = ? ";
					pst = conexion.getConnection().prepareStatement(sql);
					pst.setString(1, rsCierres.getString("NUM_OS"));
					ResultSet rsMaterial = conexion.Query(pst);
					while (rsMaterial.next()) {
						cadena = rsMaterial.getString("NUM_OS");
						cadena += rsMaterial.getString("CO_ACCEJE");
						cadena += Utilidades.RightPad(rsMaterial.getString("CO_ELEMENTO"),10,' ');
						if (rsMaterial.getInt("TIPO") == 1) {
							//cadena += Utilidades.LeftPad(formatter.format(rsMaterial.getDouble("CANTIDAD")),9,' ');
							cadena += Utilidades.LeftPad(String.format("%.2f", rsMaterial.getDouble("CANTIDAD")).replace(',', '.'),9,' ');
						}else {
							//cadena += Utilidades.LeftPad(formatter.format(rsMaterial.getDouble("CANTIDAD")*-1),9,' ');
							cadena += Utilidades.LeftPad(String.format("%.2f", rsMaterial.getDouble("CANTIDAD")*-1).replace(',', '.'),9,' ');
							
						}
						if (rsMaterial.getInt("COBRO")== 1) {
							cadena += "S";
						}else {
							cadena += "N";
						}
						fout7.write(cadena + "\r\n");  // ARCHIVO DE PRECIREL.ODB
					}
					
					if (!rsCierres.getString("IND_COBRO").equals("0")) {
						cadena = rsCierres.getString("NUM_OS");
						cadena += rsCierres.getString("IND_COBRO").trim();
						cadena += Utilidades.LeftPad(rsCierres.getString("CUOTAS").trim(),3,'0');
						fout8.write(cadena + "\r\n");  // ARCHIVO DE TIPPAGO.ODB
					}
					
					fout0.write("Seleccionando Medidor OS " + rsCierres.getString("NUM_OS") + "\r\n");
					
					sql = "SELECT QO_MEDIDOR.*" 
							+ "FROM QO_MEDIDOR "
							+ " WHERE QO_MEDIDOR.NUM_OS = ? ";
					pst = conexion.getConnection().prepareStatement(sql);
					pst.setString(1, rsCierres.getString("NUM_OS"));
					ResultSet rsMedidor = conexion.Query(pst);
					if (rsMedidor.next()) {
						
						cadena = rsCierres.getString("NUM_OS");
						cadena += rsCierres.getString("NIS_RAD");
						cadena += Utilidades.RightPad(rsMedidor.getString("NUM_APA").trim(),20,' ');
						cadena += rsMedidor.getString("CO_MARCA");
						cadena += rsMedidor.getString("TIP_APA");
						cadena += rsMedidor.getString("CO_PROP_APA");
						cadena += Utilidades.LeftPad(rsMedidor.getString("AOL_APA"),4,'0');
						cadena += rsMedidor.getString("TIP_FASE");
						cadena += rsMedidor.getString("TIP_TENSION");
						cadena += rsCierres.getString("FECHA");
						cadena += rsMedidor.getString("TIP_INTENSIDAD");
						cadena += Utilidades.LeftPad(rsMedidor.getString("CTE_APA"),7,'0') + ".000";
						cadena += rsMedidor.getString("F_FABRIC");
						cadena += rsMedidor.getString("F_UREVIS");
						cadena += rsMedidor.getString("REGULADOR");
						cadena += Utilidades.LeftPad(rsMedidor.getString("DIMEN_CONEX"),3,'0') + ".00";
						cadena += rsMedidor.getString("F_PROX_CALIBRACION");
						cadena += rsMedidor.getString("F_PROX_VERIFICACION");
						cadena += rsMedidor.getString("TIP_MATERIAL");
						cadena += rsMedidor.getString("TIP_NATUR");
						cadena += Utilidades.LeftPad(rsMedidor.getString("DIAMETRO").trim(),3,'0');
						cadena += Utilidades.LeftPad(rsMedidor.getString("ALTA").trim(),3,'0') + ".00";
						fout9.write(cadena + "\r\n");
						
						cadena = rsCierres.getString("NUM_OS");
						cadena += rsCierres.getString("NIS_RAD");
						cadena += Utilidades.RightPad(rsMedidor.getString("NUM_APA").trim(),20,' ');
						cadena += rsMedidor.getString("CO_MARCA");
						cadena += "CO011";
						cadena += Utilidades.LeftPad(rsMedidor.getString("NUM_RUE"),2,'0');
						cadena += Utilidades.LeftPad(rsMedidor.getString("COEF_PER"),5,'0') + ".0";
						cadena += Utilidades.LeftPad(rsMedidor.getString("CTE_APA"),8,'0') + ".0";
						cadena += Utilidades.LeftPad(rsMedidor.getString("LECTURA").trim(),10,'0');
						fout11.write(cadena + "\r\n");
						
						// AGREGAR ANOMALIA
						cadena = rsCierres.getString("NUM_OS")
								+ "ON002"
								+ rsCierres.getString("FECHA_CIERRE");
						String obs="";
						if (rsCierres.getString("OBSERVACION") != null) {
							obs = formatoString(rsCierres.getString("OBSERVACION")).toUpperCase();
						}
						if (obs.length() > 250) {
							cadena += obs.substring(0, 249);
						} else {
							cadena += obs;
						}
						
						fout12.write(cadena + "\r\n");
						
						fout0.write("Seleccionando precintos OS " + rsCierres.getString("NUM_OS") + "\r\n");
						
						sql = "SELECT * FROM QO_ORDEN_PRECINTOS WHERE NUM_OS=? AND NUM_APA=?";
						java.sql.PreparedStatement pst1 = conexion.getConnection().prepareStatement(sql);
						pst1.setString(1, rsCierres.getString("NUM_OS"));
						pst1.setString(2, rsMedidor.getString("NUM_APA"));
						ResultSet rsSellos2 = conexion.Query(pst1);
						
						while (rsSellos2.next()) {
							cadena = rsSellos2.getString("NUM_OS");
							cadena +=  Utilidades.RightPad(rsSellos2.getString("NUM_APA").trim(),20,' ');
							cadena += rsMedidor.getString("CO_MARCA");
							cadena += Utilidades.RightPad(rsSellos2.getString("NUM_PRECIN").trim(),10,' ');
							cadena += rsCierres.getString("FECHA");
							cadena += Utilidades.Espacios(10);
							//cadena += rsSellos.getString("AGREGADO").trim();
							fout6.write(cadena + "\r\n");  // ARCHIVO DE PRECIREL.ODB
							
							
						}
						
						
					}
				
				
				}while(rsCierres.next());
				
				fout0.write("Finalizando proceso\r\n");
				
				fout0.close();
				fout1.close();
				fout2.close();
				fout3.close();
				fout4.close();
				fout5.close();
				fout6.close();
				fout7.close();
				fout8.close();
				fout9.close();
				fout10.close();
				fout11.close();
				fout12.close();
				
			}
			
			
			
			
			
			
			
			ComprimirDirectorio(usuario);
			
			out.print("Proceso Finalizado, puede descargar el archivo desde el este <strong><a href='FolderUsuario.jsp'>link</a></strong>");

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
	
	public String formatoString(String c) {
		
		String nueva_cadena = "";
		Character caracter = null;
		boolean valido = true;
		
		int[] caracteres = {13,9,11,10,205,193,211,34,241,209,39,218,250}; 
		
		for (int i=0; i<c.length(); i++)
	      {
	       valido = true;
	       caracter = c.charAt(i);
	       for (int j=0; j < caracteres.length; j++) {
	    	   if (caracter == (char)caracteres[j]) {
	    		   valido = false;
	    		   break;
	    	   }
	    	   
	       }
	       if (valido)
	           nueva_cadena += c.charAt(i);
	       }
		
		
		nueva_cadena = nueva_cadena.replace('ó', 'O');
		nueva_cadena = nueva_cadena.replace('Ó', 'o');
		nueva_cadena = nueva_cadena.replace('á', 'a');
		nueva_cadena = nueva_cadena.replace('Á', 'A');
		nueva_cadena = nueva_cadena.replace('é', 'e');
		nueva_cadena = nueva_cadena.replace('É', 'E');
		nueva_cadena = nueva_cadena.replace('í', 'i');
		nueva_cadena = nueva_cadena.replace('Í', 'I');
		nueva_cadena = nueva_cadena.replace('ú', 'u');
		nueva_cadena = nueva_cadena.replace('Ú', 'U');
		nueva_cadena = nueva_cadena.replace('Ñ', 'N');
		nueva_cadena = nueva_cadena.replace('ñ', 'n');
		nueva_cadena = nueva_cadena.replace('\r', ' ');
		nueva_cadena = nueva_cadena.replace('\n', ' ');
		nueva_cadena = nueva_cadena.replace('\t', ' ');
		return nueva_cadena;
		
	}

	public void ComprimirDirectorio(String usuario) {
		ServletContext servletContext = getServletContext();
		String path = servletContext.getRealPath("/generated") + File.separator
				+ usuario.trim();

		String FilenameZip = path + File.separator + "DOWNLOAD.ZIP";
		File filezip = new File(path + File.separator + "DOWNLOAD.ZIP");
		if (filezip.exists()) {
			filezip.setWritable(true, false);
			filezip.delete();
		}
		
		File f = new File(path);
		File[] ficheros = f.listFiles();

		if (ficheros.length > 0) {
			// Comprimiendo los archivos
			try {
				
				
				
				ZipOutputStream os = new ZipOutputStream(new FileOutputStream(
						FilenameZip));
				byte[] buffer = new byte[BUFFER_SIZE];

				for (int x = 0; x < ficheros.length; x++) {

					ZipEntry entrada = new ZipEntry(ficheros[x].getName());
					os.putNextEntry(entrada);

					FileInputStream fis = new FileInputStream(
							ficheros[x].getPath());

					int leido = 0;
					while (0 < (leido = fis.read(buffer))) {
						os.write(buffer, 0, leido);
					}
					fis.close();

					os.closeEntry();

				}
				os.close();
			
			}catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void EliminarArchivos(String path) {
		File f = new File(path);
		File[] ficheros = f.listFiles();
		if (ficheros.length > 0) {
			for (int x = 0; x < ficheros.length; x++) {
				File file = ficheros[x];
				file.setWritable(true, false);
				file.delete();
			}
			
		}
		
	}

}
