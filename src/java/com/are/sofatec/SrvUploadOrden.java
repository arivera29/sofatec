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
 * Servlet implementation class SrvGetFile
 */
@WebServlet("/SrvUploadOrden")
public class SrvUploadOrden extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SrvUploadOrden() {
        super();
        // TODO Auto-generated constructor stub
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=ISO-8859-1");
        PrintWriter out = response.getWriter();
        db conexion = null;
        
        try {
            //InputStream in = request.getInputStream();
            
        	       	
        	
        	if (request.getParameter("orden") == null) {
        		out.print("Numero Orden de servicio no recibida");
        		return;
        		
        	}
        	
        	if (request.getParameter("cobro") == null) {
        		out.print("Opcion de cobro no recibida");
        		return;
        		
        	}
        	
        	if (request.getParameterValues("paso") == null || request.getParameterValues("observacion") == null  || request.getParameterValues("item_pago_paso") == null ) {
        		out.print("Informacion de pasos no recibida");
        		return;
        	}
        	
        	/*if (request.getParameterValues("medidor_lectura") == null || request.getParameterValues("lectura") == null ) {
        		out.print("Informacion de lectura medidor no recibida");
        		return;
        	}*/
        	
        	
        	String orden = (String)request.getParameter("orden");
        	String cobro = (String)request.getParameter("cobro");
        	
        	String[] pasos = request.getParameterValues("paso");
        	String[] observacion = request.getParameterValues("observacion");
        	String[] item_pago_paso = request.getParameterValues("item_pago_paso");
        	String[] cobro_paso = request.getParameterValues("cobro_paso");
        	
        	
        	
        	String[] medidor_lectura = null;
        	String[] lectura = null ;
        	
        	if (request.getParameterValues("medidor_lectura") != null) {
        		medidor_lectura = request.getParameterValues("medidor_lectura");
        		lectura = request.getParameterValues("lectura");
        		
        		if (medidor_lectura.length != lectura.length) {
            		out.print("Indices de lectura medidor no concuerda");
            		return;
            	}
        	
        	}
        	
        	if (pasos.length != observacion.length || pasos.length != item_pago_paso.length || pasos.length != cobro_paso.length) {
        		out.print("Indices de pasos no concuerda");
        		return;
        	}
        	
        	
        	
        	
        	//out.println("Validando nuevos pasos");
        	
        	String[] np_pasos = null;
        	if ( request.getParameterValues("np_paso") != null) {
        		np_pasos = request.getParameterValues("np_paso");
        	}
        	 
        	String[] np_observacion = null;
        	if (request.getParameterValues("np_observacion") != null) {
        		np_observacion = request.getParameterValues("np_observacion");
        	}
        	
        	String[] np_item_pago_paso = null;
        	if (request.getParameterValues("np_item_pago_paso") != null) {
        		np_item_pago_paso = request.getParameterValues("np_item_pago_paso");
        	}
        	String[] np_cobro_paso = null;
        	if (request.getParameterValues("np_cobro_paso") != null) {
        		np_cobro_paso = request.getParameterValues("np_cobro_paso");
        	}
        	
        	
        	if (np_pasos != null) {
        		if (np_pasos.length != np_observacion.length || np_pasos.length != np_item_pago_paso.length || np_pasos.length  != np_cobro_paso.length  ) {
        			out.print("Indices de nuevos pasos no concuerda");
            		return;
        		}
        	}
        	
        	//out.println("Validando materiales");
        	
        	String[] material = null;
        	if ( request.getParameterValues("material") != null) {
        		material = request.getParameterValues("material");
        	}
        	
        	String[] cantidad = null;
        	if (request.getParameterValues("cantidad") != null) {
        		cantidad = request.getParameterValues("cantidad");
        	}
        	String[] tipo = null;
        	if (request.getParameterValues("tipo") != null) {
        		tipo = request.getParameterValues("tipo");
        	}
        	
        	//out.println("Validando Item de pago");
        	
        	String[] item_pago_material = null;
        	
        	if (request.getParameterValues("item_pago_material") != null) {
        		item_pago_material = request.getParameterValues("item_pago_material");
        	}
        	
        	String[] cobro_material = null;
        	if (request.getParameterValues("cobro_material") != null) {
        		cobro_material = request.getParameterValues("cobro_material");
        	}
        	
        	
        	if (material != null) {
        		
        		if (cantidad == null) {
        			out.print("Cantidades no recibidas");
            		return;
        		}
        		
        		if (tipo == null) {
        			out.print("Tipo de material no recibido");
            		return;
        		}
        		
        		if (item_pago_material == null) {
        			out.print("Accion de materiales no recibida");
            		return;
        		}
        		
        		if (cobro_material == null) {
        			out.print("Cobro no recibido");
            		return;
        		}
        		
        		if (material.length != cantidad.length || material.length != tipo.length || material.length != item_pago_material.length || material.length != cobro_material.length  ) {
        			out.print("Indices de materiales no concuerda");
            		return;
        		}
        		
        		
        	}
        	
        	//out.println("Validando Precintos");
        	
        	String[] medidor_precinto = null;
        	if (request.getParameterValues("medidor_precinto") != null) {
        		medidor_precinto = request.getParameterValues("medidor_precinto");
        	}
        	
        	String[] serial = null;
        	if (request.getParameterValues("serial") != null) {
        		serial = request.getParameterValues("serial");
        	}
        	
        	String[] agregado = null;
        	if (request.getParameterValues("agregado") != null) {
        		agregado = request.getParameterValues("agregado");
        	}
        	
        	
        	
        	if (medidor_precinto != null) {
        		
        		if (serial == null) {
        			out.print("Seriales de precintos no recibidos");
            		return;
        		}
        		
        		if (agregado == null) {
        			out.print("Tipo de precintos no recibido");
            		return;
        		}
        		
        		if (medidor_precinto.length != serial.length || medidor_precinto.length != agregado.length) {
        			out.print("Indices de precintos no concuerda");
            		return;
        		}
        		
        	}
        	
        	//out.println("Iniciando transaccion DB");
        	
        	conexion = new db();
        	
        	if (medidor_lectura == null) {
        		ResultSet rs = conexion.Query("SELECT NUM_APA FROM QO_APARATOS WHERE NUM_OS='" + orden + "'");
        		if (rs.next()) {
        			throw new SQLException("Debe ingresar la lectura del medidor " + rs.getString("NUM_APA"));
        		}
        	}
        	
        	String sql = "SELECT NUM_OS,ESTADO_OPER,TIP_OS FROM QO_ORDENES WHERE NUM_OS=?";
        	java.sql.PreparedStatement ps = conexion.getConnection().prepareStatement(sql);
        	ps.setString(1, orden);
        	ResultSet rs = conexion.Query(ps);
        	if (!rs.next()) {
        		throw new SQLException("Orden de servicio no encontrada");
        	}
        	
        	if (rs.getInt("ESTADO_OPER") == 99) {
        		throw new SQLException("Orden de servicio ya se encuentra resuelta");
        	}
        	
        	boolean flag_pasos = true;
        	
        	for (int x= 0; x < pasos.length; x++) {
        		sql = "UPDATE QO_PASOS SET OBSERVACION=?, CUMPLIDO=1, CO_ACCEJE=?, COBRO =? WHERE NUM_OS=? AND NUM_PASO=?";
        		java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        		String obs = observacion[x];
        		obs = new String (obs.getBytes(),"ISO-8859-1");
        		pst.setString(1, removeAcentos(obs));
        		pst.setString(2, item_pago_paso[x]);
        		pst.setString(3, cobro_paso[x]);
        		pst.setString(4, orden);
        		pst.setString(5, pasos[x]);
        		
        		if (conexion.Update(pst) > 0) {
        			sql = "SELECT TIP_FLUJO FROM QO_OSFLUJO WHERE TIP_OS=? AND CO_ACCEJE=?";
        			pst = conexion.getConnection().prepareStatement(sql);
        			pst.setString(1, rs.getString("TIP_OS"));
        			pst.setString(2, item_pago_paso[x]);
        			ResultSet rsFlujo = conexion.Query(pst); 
        			if (rsFlujo.next()) {
        				sql = "SELECT TIP_FLUJO, NUM_PASO, OPCOND, DESCRIPCION, CONDICION, ELSEACCION, CO_ACCEJE FROM QO_PASOSFLUJO WHERE TIP_FLUJO=?";
            			pst = conexion.getConnection().prepareStatement(sql);
            			pst.setString(1, rsFlujo.getString("TIP_FLUJO"));
            			ResultSet rsPasosFlujo = conexion.Query(pst);
            			if (rsPasosFlujo.next()) {
            				do {
            					sql = "INSERT INTO QO_NUEVOS_PASOS (NUM_OS, NUM_PASO, OPCOND , DESCRIPCION, CONDICION, ELSEACCION, CUMPLIDO, CO_ACCEJE, IND_DECISOR, OBSERVACION, COBRO) VALUES (?, ?, ? , ?, ?, ?, 0, '', 1, '', 1)";
            					pst = conexion.getConnection().prepareStatement(sql);
                    			pst.setString(1, orden);
                    			pst.setString(2, rsPasosFlujo.getString("NUM_PASO"));
                    			pst.setString(3, rsPasosFlujo.getString("OPCOND"));
                    			pst.setString(4, rsPasosFlujo.getString("DESCRIPCION"));
                    			pst.setString(5, rsPasosFlujo.getString("CONDICION"));
                    			pst.setString(6, rsPasosFlujo.getString("ELSEACCION"));
                    			
                    			if (conexion.Update(pst) > 0) {
                    				
                    			}else {
                    				flag_pasos = false;
                        			break;
                    			}
                    			
            				}while(rsPasosFlujo.next());
            			}else {
            				throw new SQLException("No se encuentra informacion de nuevos pasos en la base de datos");
            			}
        			}
        			
        			
        		}else {
        			flag_pasos = false;
        			break;
        		}
        		
        	}
        	
        	boolean flag_np_pasos = true;
        	if (np_pasos != null) {
	        	for (int x= 0; x < np_pasos.length; x++) {
	        		sql = "UPDATE QO_NUEVOS_PASOS SET OBSERVACION=?, CUMPLIDO=1, CO_ACCEJE=?, COBRO =? WHERE NUM_OS=? AND NUM_PASO=?";
	        		java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
	        		String obs = np_observacion[x];
	        		obs = new String (obs.getBytes(),"ISO-8859-1");
	        		pst.setString(1, removeAcentos(obs));
	        		pst.setString(2, np_item_pago_paso[x]);
	        		pst.setString(3, np_cobro_paso[x]);
	        		pst.setString(4, orden);
	        		pst.setString(5, np_pasos[x]);
	        		
	        		if (conexion.Update(pst) > 0) {
	        			
	        		}else {
	        			flag_np_pasos = false;
	        			break;
	        		}
	        		
	        	}
        	}
        	
        	boolean flag_lectura = true;
        	if (medidor_lectura != null ) {
	        	for (int x= 0; x < medidor_lectura.length; x++) {
	        		sql = "INSERT INTO QO_ORDEN_LECTURA (NUM_OS,NUM_APA,LECTURA) VALUES (?,?,?)";
	        		java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
	        		pst.setString(1, orden);
	        		pst.setString(2, medidor_lectura[x]);
	        		pst.setString(3, lectura[x]);
	
	        		if (conexion.Update(pst) > 0) {
	        			
	        		}else {
	        			flag_lectura = false;
	        			break;
	        		}
	        		
	        	}
        	}
        	
        	boolean flag_precinto = true;
        	if (medidor_precinto != null) {
	        	for (int x= 0; x < medidor_precinto.length; x++) {
	        		sql = "INSERT INTO QO_ORDEN_PRECINTOS (NUM_OS,NUM_APA,NUM_PRECIN,AGREGADO) VALUES (?,?,?,?)";
	        		java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
	        		pst.setString(1, orden);
	        		pst.setString(2, medidor_precinto[x]);
	        		pst.setString(3, serial[x]);
	        		pst.setString(4, agregado[x]);
	
	        		if (conexion.Update(pst) > 0) {
	        			
	        		}else {
	        			flag_precinto = false;
	        			break;
	        		}
	        		
	        	}
        	}
        	
        	boolean flag_material = true;
        	if (material != null) {
	        	for (int x= 0; x < material.length; x++) {
	        		sql = "INSERT INTO QO_ORDEN_MATERIAL (NUM_OS,CO_ELEMENTO,CO_ACCEJE,CANTIDAD,TIPO,COBRO) VALUES (?,?,?,?,?,?)";
	        		java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
	        		pst.setString(1, orden);
	        		pst.setString(2, material[x]);
	        		pst.setString(3, item_pago_material[x]);
	        		pst.setString(4, cantidad[x]);
	        		pst.setString(5, tipo[x]);
	        		pst.setString(6, cobro_material[x]);
	
	        		if (conexion.Update(pst) > 0) {
	        			
	        		}else {
	        			flag_material = false;
	        			break;
	        		}
	        		
	        	}
        	}
        	
        	boolean flag_medidor = true;
        	
        	if (request.getParameter("n_num_apa") != null) {
        		
        		String n_num_apa = (String)request.getParameter("n_num_apa");
        		String n_co_marca = (String)request.getParameter("n_co_marca");
        		String n_tip_apa = (String)request.getParameter("n_tip_apa");
        		String n_co_prop_apa = (String)request.getParameter("n_co_prop_apa");
        		String n_aol_apa = (String)request.getParameter("n_aol_apa");
        		String n_tip_fase = (String)request.getParameter("n_tip_fase");
        		String n_tip_tension = (String)request.getParameter("n_tip_tension");
        		String n_tip_intensidad = (String)request.getParameter("n_tip_intensidad");
        		String n_cte_apa = (String)request.getParameter("n_cte_apa");
        		String n_f_fabric = (String)request.getParameter("n_f_fabric");
        		String n_f_urevis = (String)request.getParameter("n_f_urevis");
        		String n_regulador = (String)request.getParameter("n_regulador");
        		String n_dimen_conex = (String)request.getParameter("n_dimen_conex");
        		
        		String n_f_prox_calibracion = (String)request.getParameter("n_f_prox_calibracion");
        		String n_f_prox_verificacion = (String)request.getParameter("n_f_prox_verificacion");
        		
        		String n_tip_material = (String)request.getParameter("n_tip_material");
        		String n_tip_natur = (String)request.getParameter("n_tip_natur");
        		String n_diametro = (String)request.getParameter("n_diametro");
        		String n_alta = (String)request.getParameter("n_alta");
        		String n_lectura = (String)request.getParameter("n_lectura");
        		
        		String n_tip_csmo = (String)request.getParameter("n_tip_csmo");
        		String n_num_rue = (String)request.getParameter("n_num_rue");
        		String n_coef_per = (String)request.getParameter("n_coef_per");
        		
        		sql = "INSERT INTO QO_MEDIDOR (NUM_OS,NUM_APA,CO_MARCA,TIP_APA," +
					"CO_PROP_APA,AOL_APA,TIP_FASE,TIP_TENSION,TIP_INTENSIDAD," +
					"CTE_APA,F_FABRIC,F_UREVIS,REGULADOR,DIMEN_CONEX," +
					"F_PROX_CALIBRACION,F_PROX_VERIFICACION,TIP_MATERIAL," +
					"TIP_NATUR,DIAMETRO,ALTA,LECTURA,F_INST,TIP_CSMO,NUM_RUE,COEF_PER) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,SYSDATE(),?,?,?)";
        		
        		java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        		pst.setString(1, orden);
        		pst.setString(2, n_num_apa);
        		pst.setString(3, n_co_marca);
        		pst.setString(4, n_tip_apa);
        		pst.setString(5, n_co_prop_apa);
        		pst.setString(6, n_aol_apa);
        		pst.setString(7, n_tip_fase);
        		pst.setString(8, n_tip_tension);
        		pst.setString(9, n_tip_intensidad);
        		pst.setString(10, n_cte_apa);
        		pst.setString(11, n_f_fabric);
        		pst.setString(12, n_f_urevis);
        		pst.setString(13, n_regulador);
        		pst.setString(14, n_dimen_conex);
        		pst.setString(15, n_f_prox_calibracion);
        		pst.setString(16, n_f_prox_verificacion);
        		pst.setString(17, n_tip_material);
        		pst.setString(18, n_tip_natur);
        		pst.setString(19, n_diametro);
        		pst.setString(20, n_alta);
        		pst.setString(21, n_lectura);
        		pst.setString(22, n_tip_csmo);
        		pst.setString(23, n_num_rue);
        		pst.setString(24, n_coef_per);
        		
        		if (conexion.Update(pst) > 0) {
        			
        		}else {
        			flag_medidor = false;
        		}
        		
        	}
        	
        	
        	if (flag_pasos  && flag_lectura && flag_precinto && flag_material && flag_medidor && flag_np_pasos ) {
        		sql = "UPDATE QO_ORDENES SET ESTADO_OPER=99, FECHA_CIERRE=SYSDATE(), IND_COBRO=? WHERE ESTADO_OPER=1 AND NUM_OS=?";
        		java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        		pst.setString(1, cobro);
        		pst.setString(2, orden);
        		if (conexion.Update(pst) > 0 ) {
	        		conexion.Commit();
	        		out.print("OK");
        		}else {
        			String cadena = "";
        			
        			if (!flag_pasos) {
        				cadena += "\nER.PASOS";
        			}
        			if (!flag_lectura) {
        				cadena += "\nER.LECTURA";
        			}
        			if (!flag_precinto) {
        				cadena += "\nER.PRECINTO";
        			}
        			if (!flag_material) {
        				cadena += "\nER.MATERIAL";
        			}
        			if (!flag_medidor) {
        				cadena += "\nER.MEDIDOR";
        			}
        			if (!flag_np_pasos) {
        				cadena += "\nER.NUEVOS PASOS";
        			}
        			cadena += "\nERROR CAMBIAR ESTADO ORDEN DE SERVICIO";
        			out.print(cadena);
        		}
        	}else {
        		out.print("ERROR AL PROCESAR ORDEN DE SERVICIO " + pasos.length);
        	}
        	
        	
       
        }catch (SQLException e) {
          out.print(e.getMessage());
         }catch (Exception e) {
            out.print(e.getMessage());
        } finally {
            out.close();
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
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		processRequest(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		processRequest(request, response);
	}
	
	/**
	 * Funci�n que elimina acentos y caracteres especiales de
	 * una cadena de texto.
	 * @param input
	 * @return cadena de texto limpia de acentos y caracteres especiales.
	 */
	public String removeAcentos(String input) {
	    // Cadena de caracteres original a sustituir.
	    String original = "áÁéÉíÍóÓúÚñÑ";
	    // Cadena de caracteres ASCII que reemplazar�n los originales.
	    String ascii = "aaeeiioouunN";
	    String output = input;
	    for (int i=0; i<original.length(); i++) {
	        // Reemplazamos los caracteres especiales.
	        output = output.replace(original.charAt(i), ascii.charAt(i));
	    }//for i
	    return output;
	}

}
