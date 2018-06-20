package com.are.sofatec;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


/**
 * Servlet implementation class SrvGps
 */
@WebServlet("/SrvOrdenHda")
public class SrvOrdenHda extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SrvOrdenHda() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    protected void ProcesarPeticion(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		//response.setContentType("text/html;charset=UTF-8");
    	request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		
		
 
		db conexion =  null;
		String orden = (String)request.getParameter("orden");
		
		
		InputStream in = null;
		
		try {
			
			conexion = new db();
			
			String sql = "SELECT NUM_OS,TIP_OS FROM qo_ordenes WHERE EST_HDA=0 AND NUM_OS=?";
			java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
			pst.setString(1, orden);
			
			ResultSet rs = conexion.Query(pst);
			while (rs.next()) {
				int nError=0;
				String ObsGeneral = "";
				out.println("<hr>");
				out.println("<h3>Procesando OS: " +  rs.getString("NUM_OS") + "</h3>");
				out.println("<br/>Iniciando consulta en la HDA...");
				String url = "http://hgiservice.herramientaselectricaribe.co:8080/api/serviceOrders/byNumber/" + rs.getString("NUM_OS");
				
				ConectorHttp con = new ConectorHttp();
				
				in = con.MetodoGET("", url,"hgi","G4QfmjUStWjVpGB8");
				
				out.println("<br/>Recibiendo informacion de la HDA");
				BufferedReader reader = new BufferedReader(new InputStreamReader(in,"utf-8"));
				
				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line);
				}
			
			
				
				//out.println(sb.toString());
				
				JSONParser parser = new JSONParser();
				
				try {
					out.println("<br/>Procesando JSON");
					Object obj = parser.parse(sb.toString());
					JSONObject jsonObject = (JSONObject) obj;
					
					if (jsonObject.get("error")!= null) {
						if ((boolean)jsonObject.get("error")== true) {
							out.println("<br/><img src='images/alerta.gif'>Error consultando OS");
							out.println("<br/>Mensaje: " + (String)jsonObject.get("message"));
							out.println("<br/>Code: " + (String)jsonObject.get("code"));
							continue;
						}
					}
					
					//long acta = (long) jsonObject.get("_number");
					//out.println("<h3>Procesando OS: " + acta + "</h3>");
					
					
					String fecha = (String)jsonObject.get("_clientCloseTs");
					out.println("<br/>Fecha de cierre en HDA: " + fecha);
					//String lote = (String) jsonObject.get("numeroLote");
					//out.println("No. Lote: " + lote);
					
					String observacion1 = (String) jsonObject.get("anomalias_observaciones");
					if (observacion1 == null) {
						observacion1 = "SIN OBSERVACION EN HDA";
					}
					out.println("<br/>Observaciones Anomalia: " + observacion1);
					
					String observacion2 = (String) jsonObject.get("observaciones");
					if (observacion2 == null) {
						observacion2 = "SIN OBSERVACION EN HDA";
					}
					out.println("<br/>Observaciones Generales: " + observacion2);
					
				
					String cedulaOperario = (String) jsonObject.get("cedulaOperario");
					String nombreOperario = (String) jsonObject.get("nombreOperario") + " " + (String) jsonObject.get("apellido1Operario") + " " + (String) jsonObject.get("apellido2Operario") ;
					
					out.println("<br/>cedula Operario: " + cedulaOperario);
					out.println("<br/>Nombre Operario: " + nombreOperario);
					
					ObsGeneral += "OPER. " + cedulaOperario + " " + nombreOperario;
					
					long modoPagoCuotas = 0;
					String modoPago =  "0";  
					
					JSONObject modoPagoTipo=(JSONObject)jsonObject.get("modoPagoTipo");
					if (modoPagoTipo != null) {
						
						if (modoPagoTipo.get("id").toString().equals("__mod_pag_con")) {
							modoPago = "1";  // Contado
						}else if (modoPagoTipo.get("id").toString().equals("__mod_pag_fin")) {
							modoPago = "2";  // Financiado
							if (jsonObject.get("modoPagoCuotas") != null) {
								modoPagoCuotas = (long)jsonObject.get("modoPagoCuotas");
								if (modoPagoCuotas == 0) {
									modoPagoCuotas = 48;
								}
							}
						}else {
							modoPago = "3";  // Pendiente Laboratorio
						}
						
					}
					
					out.println("<br/>Modo de Pago: " + modoPago);
					out.println("<br/>Cuotas: " + modoPagoCuotas);
					
					this.ActualizarOrden(rs.getString("NUM_OS"), observacion1, fecha.replace(" COT", ""),cedulaOperario,nombreOperario,modoPago, modoPagoCuotas,conexion);
					
					try {
					JSONArray aparatosExistentes=(JSONArray)jsonObject.get("aparatosExistentes");
					if (aparatosExistentes != null) {
						out.println("<br/>Read info Aparatos Existentes");
						if (aparatosExistentes.size() > 0) {
							JSONObject item = (JSONObject)aparatosExistentes.get(0);
							String medidor = item.get("numero").toString();
							out.println("<br/>Numero medidor:" + medidor);
							ObsGeneral += " Med. " + medidor;
							if (item.get("marca")  != null) {
								JSONObject oMarca = (JSONObject)item.get("marca");
								String marca = oMarca.get("id").toString().replace("_", "");
								out.println("<br/>Marca:" + marca);
							}else {
								out.println("<br/>Marca No reportada");
							}
							if (item.get("lecturaActual") != null) {
								double lectura = (double)item.get("lecturaActual");
								out.println("<br/>Lectura:" + lectura );
								ObsGeneral += " Lect. " + lectura;
								this.InsertarLecturaOrden(rs.getString("NUM_OS"), medidor, lectura, conexion);
							}else {
								out.println("<br/><img src='images/alerta.gif'>Lectura no valida");
								nError++;
							}
							
							JSONArray sellosInstalados=(JSONArray)item.get("sellosInstalados");
							
							if (sellosInstalados != null) {
								ObsGeneral += " Sellos: ";
								for (int x=0; x < sellosInstalados.size(); x++ ) {
									JSONObject itemSello = (JSONObject)sellosInstalados.get(x);
									String precinto = itemSello.get("numeroSerie").toString();
									out.println("<br/>Serial sello:" + precinto);
									ObsGeneral += " " + precinto;
									this.InsertarSelloOrden(rs.getString("NUM_OS"), medidor, precinto,1, conexion);
									
								}
							}
							
							
						}
					}
					
					}catch (Exception e) {
						out.println("<br/><img src='images/alerta.gif'>Error al procesar informacion de Medidor Existente");
						nError++;
					}
					try {
					JSONArray aparatosInstalados=(JSONArray)jsonObject.get("aparatosInstalados");
					if (aparatosInstalados != null) {
						out.println("<br/>Read info APARATOS INSTALADOS");
						if (aparatosInstalados.size() > 0) {
							JSONObject item = (JSONObject)aparatosInstalados.get(0);
							
							String medidor = item.get("numero").toString();
							out.println("<br/>Numero medidor:" + medidor);
							ObsGeneral += " Med. Inst. " + medidor; 
							double lectura=0;
							if (item.get("lecturaActual") != null) {
								lectura = (double)item.get("lecturaActual");
								out.println("<br/>Lectura:" + lectura);
								ObsGeneral += " Lect. " + lectura;
							}else {
								out.println("<br/><img src='images/alerta.gif'>Lectura no valida");
								nError++;
							}
							
							JSONObject Nodomarca = (JSONObject)item.get("marca");
							String marca = Nodomarca.get("id").toString().replace("_", "");
							out.println("<br/>Marca:" + marca);
							
							JSONObject Nodotipo = (JSONObject)item.get("tipo");
							String tipo = Nodotipo.get("id").toString().replace("_", "");
							out.println("<br/>Tipo:" + tipo);
							
							JSONObject Nododigitos = (JSONObject)item.get("digitos");
							String digitos = Nododigitos.get("displayName").toString();
							out.println("<br/>Digitos:" + digitos );
							
							JSONObject Nododecimales = (JSONObject)item.get("decimales");
							String decimales = Nododecimales.get("displayName").toString();
							out.println("<br/>Decimales:" + decimales);
							
							JSONObject Nodofases = (JSONObject)item.get("nFases");
							String fases = Nodofases.get("displayName").toString();
							out.println("<br/>Fases:" + fases);
							
							this.InsertarNuevoMedidorOrden(rs.getString("NUM_OS"), medidor, marca, tipo, lectura,fecha,conexion);
							
							out.println("<br/>Sellos instalados");
							JSONArray sellosInstalados=(JSONArray)item.get("sellosInstalados");
							
							if (sellosInstalados != null) {
								ObsGeneral += " Sellos: ";
								for (int x=0; x < sellosInstalados.size(); x++ ) {
									JSONObject itemSello = (JSONObject)sellosInstalados.get(x);
									String precinto = itemSello.get("numeroSerie").toString();
									out.println("<br/>Serial sello:" + precinto);
									ObsGeneral += " " + precinto;
									this.InsertarSelloOrden(rs.getString("NUM_OS"), medidor, precinto,1, conexion);
									
								}
							}else {
								out.println("<br/>No hay Sellos instalados");
							}
							
						}
					}
					
					}catch (Exception e) {
						out.println("<br/><img src='images/alerta.gif'>Error al procesar informacion de Medidor Instalado");
						nError++;
					}
					JSONArray array=(JSONArray)jsonObject.get("trabajos_ejecutados");
					if (array != null) {
						for (int x=0; x < array.size(); x++ ) {
							JSONObject item = (JSONObject)array.get(x);
							String paso = item.get("accion").toString();
							out.println("<br/>Paso: " + paso);
							if ((boolean)item.get("ejecutada") == true) {
								String accion = item.get("codigo").toString();
								boolean cobro = (boolean)item.get("cobrable");
								out.println("<br/>Accion: " + accion );
								out.println("<br/>Cobro: " + cobro);
								out.println("<br/>Actualizando info paso " + paso + " Orden " + rs.getString("NUM_OS"));
								if (paso.trim().toUpperCase().equals("VS")) {
									this.ActualizarPasoOrden(rs.getString("NUM_OS"), paso, accion, ObsGeneral,cobro, conexion);
								}else {
									this.ActualizarPasoOrden(rs.getString("NUM_OS"), paso, accion, paso.trim().equals("RI")?observacion1:observacion2,cobro, conexion);
								}
								
								JSONArray children = (JSONArray)item.get("children");
								if (children != null) {
									out.println("<br/>Read object Children");
									if (children.size() > 0) {
										String flujo = this.BuscarFlujo(rs.getString("TIP_OS"), accion, conexion);
										int consecutivo = 1;
										for (int i=0; i < children.size(); i++) {
											JSONObject itemChildren = (JSONObject)children.get(i);
											String NuevoPaso = itemChildren.get("accion").toString();
											out.println("<br/>NUEVO PASO " + NuevoPaso);
											if ((boolean)itemChildren.get("ejecutada") == true) {
												String NuevaAccion = itemChildren.get("codigo").toString();
												boolean cobroNuevaAccion = (boolean)itemChildren.get("cobrable");
												out.println("<br/>Accion: " + NuevaAccion);
												out.println("<br/>Cobro: " + cobroNuevaAccion);
												
												this.InsertarNuevoPasoOrden(rs.getString("NUM_OS"), consecutivo, NuevoPaso, NuevaAccion, NuevoPaso.trim().equals("RI")?observacion1:observacion2,cobroNuevaAccion,accion,flujo,conexion);
												consecutivo++;
												JSONArray materiales = (JSONArray)itemChildren.get("materiales");
												if (materiales.size()> 0) {
													out.println("<br/>Read object Materiales of Children");
													for (int j=0; j < materiales.size(); j++ ) {
														JSONObject itemMateriales = (JSONObject)materiales.get(j);
														String material = itemMateriales.get("codigo").toString();
														out.println("<br/>Codigo: " + material);
														long cantidad = (long)itemMateriales.get("cantidad");
														out.println("<br/>Cantidad: " + cantidad);
														boolean cobroMaterial = (boolean)itemMateriales.get("cobrable");
														out.println("<br/>Cobro: " + cobroMaterial);
														this.InsertarMaterialOrden(rs.getString("NUM_OS"), material, NuevaAccion, Math.abs(cantidad), cantidad>0?1:2,cobroMaterial, conexion);
														
													}
												}else {
													out.println("<br/>NO hay Materiales");
												}
											}else {
												out.println("<br/><img src='images/alerta.gif'>Paso no resuelto");
												nError++;
											}
										
										}
									}
								}else {
									out.println("<br/>NO hay Nuevos pasos");
								}
								
								out.println("<br/>Read object Materiales item Trabajos Ejecutados");
								JSONArray materiales = (JSONArray)item.get("materiales");
								if (materiales.size() > 0) {
									for (int j=0; j < materiales.size(); j++ ) {
										JSONObject itemMateriales = (JSONObject)materiales.get(j);
										String material = itemMateriales.get("codigo").toString();
										out.println("<br/>Codigo: " + material);
										long cantidad = (long)itemMateriales.get("cantidad");
										out.println("<br/>Cantidad: " + cantidad);
										boolean cobroMaterial = (boolean)itemMateriales.get("cobrable");
										out.println("<br/>Cobro: " + cobroMaterial);
										this.InsertarMaterialOrden(rs.getString("NUM_OS"), material, accion, Math.abs(cantidad), cantidad>0?1:2,cobroMaterial, conexion);
									}
								}else {
									out.println("<br/>NO hay Materiales");
								}
							}else {
								out.println("<br/><img src='images/alerta.gif'>Paso no resuelto");
								nError++;
							}
						}
						
						
					}
					if (nError == 0) {
						conexion.Commit();
						out.println("<br/><img src='images/upload.png'>Orden de Servicio TRANSFERIDA");
					}else {
						out.println("<br/><img src='images/alerta.gif'>Orden de Servicio NO TRANSFERIDA");
					}
					//manejo de error
				} catch (ParseException e) {
					out.print("<br/><img src='images/alerta.gif'>Error:  " + e.getMessage());
				}
				out.println("<br/>Fin proceso de lectura acta ");
				
			} // fin While Recordset
			
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			out.print("<br/><img src='images/alerta.gif'>Error SQL: "+ e.getMessage());
		} catch (NumberFormatException e) {
			out.println("<br/><img src='images/alerta.gif'>Error.  Formato de numero no valido. " + e.getMessage());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			out.print("<br/><img src='images/alerta.gif'>Error:  " + e.getMessage());
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
	
	
	protected boolean ActualizarOrden(String orden, 
			String observacion, 
			String fecha,
			String cedulaOperario,
			String nombreOperario,
			String modoPago, 
			long modoPagoCuotas, 
			db conexion) throws SQLException {
		
		SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date f = null;
		
		try {
			f = formatter1.parse(fecha);
			fecha = formatter2.format(f);
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		boolean resultado=false;
		if (conexion != null) {
			String sql = "UPDATE QO_ORDENES SET OBSERVACION=?, "
					+ " FECHA_CIERRE=?, "
					+ " CEDULA_OPERARIO_HDA=?, "
					+ " NOMBRE_OPERARIO_HDA=?, "
					+ " IND_COBRO=?, "
					+ " CUOTAS=?, "
					+ " EST_HDA=1, "
					+ " ESTADO_OPER=99 "
					+ " WHERE NUM_OS=?";
			java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
			pst.setString(1, observacion);
			pst.setString(2, fecha);
			pst.setString(3, cedulaOperario);
			pst.setString(4, nombreOperario);
			pst.setString(5, modoPago);
			pst.setLong(6, modoPagoCuotas);
			pst.setString(7, orden);
			if (conexion.Update(pst) > 0) {
				resultado = true;
			}
			
		}
		
		
		return resultado;
	}
	
	protected boolean InsertarSelloOrden(String orden, String medidor,String precinto, int agregado,db conexion) throws SQLException {
		boolean resultado=false;
		if (conexion != null) {
			String sql = "INSERT INTO qo_orden_precintos (NUM_OS,NUM_APA,NUM_PRECIN,AGREGADO) VALUES(?,?,?,?)";
			java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
			pst.setString(1, orden);
			pst.setString(2, medidor);
			pst.setString(3, precinto);
			pst.setInt(4, agregado);
			if (conexion.Update(pst) > 0) {
				resultado = true;
			}
			
		}
		
		
		return resultado;
	}
	
	protected boolean InsertarLecturaOrden(String orden, String medidor,double lectura,db conexion) throws SQLException {
		boolean resultado=false;
		if (conexion != null) {
			String sql = "INSERT INTO qo_orden_lectura (NUM_OS,NUM_APA,LECTURA) VALUES(?,?,?)";
			java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
			pst.setString(1, orden);
			pst.setString(2, medidor);
			pst.setDouble(3, lectura);
			if (conexion.Update(pst) > 0) {
				resultado = true;
			}
			
		}
		
		
		return resultado;
	}
	
	protected boolean InsertarMaterialOrden(String orden, String material,String accion,long cantidad, int tipo,boolean cobro,db conexion) throws SQLException {
		boolean resultado=false;
		if (conexion != null) {
			String sql = "INSERT INTO qo_orden_material (NUM_OS,CO_ELEMENTO,CO_ACCEJE,CANTIDAD,TIPO,COBRO) VALUES(?,?,?,?,?,?)";
			java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
			pst.setString(1, orden);
			pst.setString(2, material);
			pst.setString(3, accion);
			pst.setLong(4, cantidad);
			pst.setInt(5, tipo);
			pst.setInt(6, cobro==true?1:0);
			if (conexion.Update(pst) > 0) {
				resultado = true;
			}
			
		}
		
		
		return resultado;
	}
	
	protected boolean ActualizarPasoOrden(String orden, String paso,String accion,String observacion,boolean cobro,db conexion) throws SQLException {
		boolean resultado=false;
		if (conexion != null) {
			if (observacion.length() > 200) {
				observacion = observacion.substring(0,198);
			}
			String sql = "UPDATE qo_pasos SET CUMPLIDO=1, CO_ACCEJE=?,OBSERVACION=?, COBRO=? WHERE NUM_OS=? AND DESCRIPCION=?";
			java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
			pst.setString(1, accion);
			pst.setString(2, observacion);
			pst.setInt(3, cobro==true?1:0);
			pst.setString(4, orden);
			pst.setString(5, paso);
			if (conexion.Update(pst) > 0) {
				resultado = true;
			}
			
		}
		
		
		return resultado;
	}
	
	protected boolean InsertarNuevoPasoOrden(String orden, int consecutivo,String paso,String accion,String observacion,boolean cobro,String parent,String flujo, db conexion) throws SQLException {
		boolean resultado=false;
		if (conexion != null) {
			if (observacion.length() > 200) {
				observacion = observacion.substring(0,198);
			}
			String sql = "INSERT INTO qo_nuevos_pasos (NUM_OS,NUM_PASO,OPCOND,DESCRIPCION,CONDICION,ELSEACCION,CUMPLIDO,CO_ACCEJE,IND_DECISOR,OBSERVACION,COBRO, PARENT,FLUJO) VALUES(?,?,2,?,2,0,1,?,2,?,?,?,?)";
			java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
			pst.setString(1, orden);
			pst.setInt(2, consecutivo);
			pst.setString(3, paso);
			pst.setString(4, accion);
			pst.setString(5, observacion);
			pst.setInt(6, cobro==true?1:0);
			pst.setString(7, parent);
			pst.setString(8, flujo);
			if (conexion.Update(pst) > 0) {
				resultado = true;
			}
			
		}
		
		
		return resultado;
	}
	
	protected String BuscarFlujo(String tipo, String accion, db conexion) throws SQLException {
		String flujo = "";
		String sql = "SELECT TIP_FLUJO FROM QO_OSFLUJO WHERE CO_ACCEJE=? AND TIP_OS=?";
		PreparedStatement pst1 = conexion.getConnection().prepareStatement(sql);
		pst1.setString(1, accion);
		pst1.setString(2, tipo);
		ResultSet rs1 = conexion.Query(pst1);
		if (rs1.next()) {
			flujo = rs1.getString("TIP_FLUJO");
		}
		
		
		return flujo;
	}
	
	protected boolean InsertarNuevoMedidorOrden(String orden, String medidor,String marca,String tipo, double lectura, String fecha, db conexion) throws SQLException {
		boolean resultado=false;
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date f = null;
		
		try {
			f = formatter.parse(fecha);
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (conexion != null) {
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
			String sql = "INSERT INTO qo_medidor (NUM_OS,"
					+ "NUM_APA,"
					+ "CO_MARCA,"
					+ "TIP_APA,"
					+ "CO_PROP_APA,"
					+ "AOL_APA,"
					+ "TIP_FASE,"
					+ "TIP_TENSION,"
					+ "F_INST,"
					+ "TIP_INTENSIDAD,"
					+ "CTE_APA,"
					+ "F_FABRIC,"
					+ "F_UREVIS,"
					+ "REGULADOR,"
					+ "DIMEN_CONEX,"
					+ "F_PROX_CALIBRACION,"
					+ "F_PROX_VERIFICACION,"
					+ "TIP_MATERIAL,"
					+ "TIP_NATUR,"
					+ "DIAMETRO,"
					+ "ALTA,"
					+ "LECTURA,"
					+ "TIP_CSMO,"
					+ "NUM_RUE,"
					+ "COEF_PER) VALUES(?,?,?,?,'PA001','10','FA001','TT001',?,'AP003','1.000',?,?,'RE000',10,?,?,'MA015','RF001',0,'0',?,'CO011',5,0)";
			java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
			pst.setString(1, orden);
			pst.setString(2, medidor);
			pst.setString(3, marca);
			pst.setString(4, tipo);
			pst.setString(5, format.format(f));
			pst.setString(6, format.format(f));
			pst.setString(7, format.format(f));
			pst.setString(8, format.format(f));
			pst.setString(9, format.format(f));
			pst.setDouble(10, lectura);
			if (conexion.Update(pst) > 0) {
				resultado = true;
			}
			
		}
		
		
		return resultado;
	}
	
}
