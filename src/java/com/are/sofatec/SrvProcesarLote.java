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

/**
 * Servlet implementation class ProcessOrders
 */
@WebServlet("/SrvProcesarLote")
public class SrvProcesarLote extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SrvProcesarLote() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void ProcesarPeticion(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
    	response.setContentType("text/plain;charset=UTF-8");
    	PrintWriter out = response.getWriter(); 
		ServletContext servletContext = getServletContext();
		HttpSession sesion=request.getSession();
		if (sesion.getAttribute("usuario") == null) {
			out.print("La sesion ha caducado... intente de nuevo por favor");
			return ;
		}
		
		String usuario = (String)sesion.getAttribute("usuario");
		String filename = (String)request.getParameter("file");
		String path = servletContext.getRealPath("/upload") +   File.separator +filename;
		String opt_ordenes = (String)request.getParameter("opt_ordenes");
		String opt_anomalias = (String)request.getParameter("opt_anomalias");
		String opt_material = (String)request.getParameter("opt_material");
		String opt_tipoos = (String)request.getParameter("opt_tipoos");
		String opt_codigos = (String)request.getParameter("opt_codigos");
		String opt_flujos = (String)request.getParameter("opt_flujos");
		String cedula = (String)request.getParameter("cedula");
		String zona = (String)request.getParameter("zona");
		
		
		
		db conexion = null;
		
		try {
			
			conexion = new db();
			
			// Abrimos el archivo
            FileInputStream fstream = new FileInputStream(path);
            // Creamos el objeto de entrada
            DataInputStream entrada = new DataInputStream(fstream);
            // Creamos el Buffer de Lectura
            BufferedReader buffer = new BufferedReader(new InputStreamReader(entrada));
            String strLinea =buffer.readLine();
            // Leer el archivo linea por linea
            while (strLinea != null)   {
                // Imprimimos la línea por pantalla
                
            	char[] cArray = strLinea.toCharArray();
            	if (cArray[0] == '3') {
            		
            		if (opt_ordenes.trim().equals("1")) {
	            		if (strLinea.equals("3SELECT * FROM PASOS")) {
	            			out.println("<img src='images/upload.png'><strong>Subiendo Tabla QO_PASOS</strong><br>");
	            			int contador = 0;
	            			int reg = 0;
	            			strLinea = buffer.readLine();
	            			cArray = strLinea.toCharArray();
	            			
	            			while (strLinea != null && cArray[0] == '1') {
	            				contador++;
	            				String[] registro = strLinea.split("	");
		            			String sql = "INSERT INTO QO_PASOS (NUM_OS,NUM_PASO,OPCOND,DESCRIPCION,CONDICION,ELSEACCION,CUMPLIDO,CO_ACCEJE,IND_DECISOR) " +
		                                "VALUES (" ;
		            			for (int x=0; x < registro.length ; x++) {
		            				if (x==0) {
		            					sql += "'" + registro [x].substring(1) + "'";
		            				}else {
		            					sql += "'" + registro [x].replace("'", "") + "'";
		            				}
		            				
		            				if ((x+1) != registro.length){
		            					sql += ",";
		            				}
		            			}
		            			sql += ")";
		            			//out.print(sql);
		            			try {
		            				
		            				if (conexion.Update(sql) > 0 ) {
		            					conexion.Commit();
		            					reg++;
		            				}
		            			}catch (SQLException e) {
		            				if (e.getErrorCode() != 1062) { // registro repetido
		            					out.println("<img src='images/alerta.gif'>" +e.getMessage() + ", Error No. " + e.getErrorCode() + "<br>");
		            				}
		            			}
	            				strLinea = buffer.readLine();
	            				if (strLinea != null) {
	            					cArray = strLinea.toCharArray();
	            				}
	            			}
	            			
	            			out.println("<strong>Total registros Leidos:</strong> " + contador + " <strong>Total Insertados:</strong> " + reg + "<br>");
	            			
	            		}
            		}
            		
            		
            		if (opt_ordenes.trim().equals("1")) { 
	            		if (strLinea.equals("3SELECT * FROM ORDENES")) {
	            			out.println("<img src='images/upload.png'><strong>Subiendo Tabla QO_ORDENES</strong><br>");
	            			int contador = 0;
	            			int reg = 0;
	            			strLinea = buffer.readLine();
	            			cArray = strLinea.toCharArray();
	            			
	            			while (strLinea != null && cArray[0] == '1') {
	            				contador++;
	            				String[] registro = strLinea.split("	");
	            				String orden = registro [0].substring(1);
	            				String lote = registro [10];
	            				
	            				String sql = "SELECT NUM_OS,ESTADO_OPER,NUM_LOTE FROM QO_ORDENES WHERE NUM_OS=?";
	            				java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
	            				pst.setString(1, orden);
	            				ResultSet rs = conexion.Query(pst);
	            				if (rs.next()) {
	            					
	            					if (rs.getInt("ESTADO_OPER") == 3) {
	            						conexion.Update("DELETE FROM QO_RECIBOS WHERE NUM_OS='" + orden + "'");
	            						sql = "UPDATE QO_ORDENES SET ESTADO_OPER=1,FECHA_ANOMALIA=NULL, ANOMALIA='-1',TECNICO=?, IMEI='',RESERVA=0,NUM_LOTE=?,NUM_ZONA=?,EST_LOTE=0 WHERE NUM_OS=? AND ESTADO_OPER=3 ";
	            						pst = conexion.getConnection().prepareStatement(sql);
	    	            				pst.setString(1, cedula);
	    	            				pst.setString(2, lote);
	    	            				pst.setString(3, zona);
	    	            				pst.setString(4, orden);
	    	            				try {
				            				if (conexion.Update(pst) > 0 ) {
				            					conexion.Commit();
				            					out.println("<img src='images/alerta.gif'>OS " + orden + " Con ANOMALIA, cambio de estado a GENERADA<br>");
				            				}
				            			}catch (SQLException e) {
				            				out.println("<img src='images/alerta.gif'>" +e.getMessage() + ", Error No. " + e.getErrorCode() + "<br>");
				            			}
	            						
	            					}
	            					if (rs.getInt("ESTADO_OPER") == 99) {
	            						out.println("<img src='images/alerta.gif'>OS " + orden + " RESUELTA<br>");
	            					}
	            					
	            					if (rs.getInt("ESTADO_OPER") == 1 || rs.getInt("ESTADO_OPER") == 0 ) {
	            						conexion.Update("DELETE FROM QO_RECIBOS WHERE NUM_OS='" + orden + "'");
	            						if (!cedula.equals("")) {
		            						sql = "UPDATE QO_ORDENES SET ESTADO_OPER=1, TECNICO=?, NUM_LOTE=?, NUM_ZONA=?,EST_LOTE=0 WHERE NUM_OS=?";
		            						pst = conexion.getConnection().prepareStatement(sql);
		            						pst.setString(1, cedula);
		            						pst.setString(2, lote);
		            						pst.setString(3, zona);
		            						pst.setString(4, orden);
		            						try {
					            				if (conexion.Update(pst) > 0 ) {
					            					conexion.Commit();
					            				}
					            			}catch (SQLException e) {
					            					out.println("<img src='images/alerta.gif'>" +e.getMessage() + ", Error No. " + e.getErrorCode() + "<br>");
					            			}
		            						
		            					}
	            					}
	            					
	            					
	            					
	            				}else {
			            			sql = "INSERT INTO QO_ORDENES (NUM_OS,TIP_OS,TIP_SERV,F_GEN,F_ESTM_REST,HORA_CITA,CO_PRIOR_ORD,NIS_RAD,NIC,SEC_NIS,NUM_LOTE,COD_EMP_ASIG,NUM_CAMP,IND_MAT_UUCC,IND_ACTA,COMENT_OS,COMENT_OS2,DESC_TIPO_ORDEN,DESC_COD_PRIORIDAD,DIRECCION,RUTAITIN,ESTADO,FECHA_CARGA,USUARIO_CARGA,NUM_ZONA) " +
			                                "VALUES (" ;
			            			for (int x=0; x < registro.length ; x++) {
			            				if (x==0) {
			            					sql += "'" + registro [x].substring(1) + "'";
			            				}else {
			            					sql += "'" + registro [x].replace("'", "") + "'";
			            				}
			            				
			            				if ((x+1) != registro.length){
			            					sql += ",";
			            				}
			            			}
			            			sql += ",SYSDATE(),'" + usuario + "','" + zona + "'"; // AGREGANDO FECHA Y USUARIO DE CARGA
			            			sql += ")";
			            			//out.print(sql);
			            			try {
			            				if (conexion.Update(sql) > 0 ) {
			            					conexion.Commit();
			            					reg++;
			            					
			            					if (!cedula.equals("")) {
			            						sql = "UPDATE QO_ORDENES SET ESTADO_OPER=1, TECNICO=?,EST_LOTE=0,NUM_LOTE=? WHERE NUM_OS=?";
			            						java.sql.PreparedStatement ps = conexion.getConnection().prepareStatement(sql);
			            						ps.setString(1, cedula);
			            						ps.setString(2, lote);
			            						ps.setString(3, orden);
			            						try {
						            				if (conexion.Update(ps) > 0 ) {
						            					conexion.Commit();
						            				}
						            			}catch (SQLException e) {
						            					out.println("<img src='images/alerta.gif'>" +e.getMessage() + ", Error No. " + e.getErrorCode() + "<br>");
						            			}
			            						
			            					}
			            				}
			            			}catch (SQLException e) {
			            				if (e.getErrorCode() != 1062) { // registro repetido
			            					out.println("<img src='images/alerta.gif'>" +e.getMessage() + ", Error No. " + e.getErrorCode() + "<br>");
			            				}
			            			}
	            				}
                                                // Actualizar información de censo
                                                
                                                this.updateCenso(orden, conexion);
                                                
                                                
	            				strLinea = buffer.readLine();
	            				if (strLinea != null) {
	            					cArray = strLinea.toCharArray();
	            				}
	            			}
	            			
	            			out.println("<strong>Total registros Leidos:</strong> " + contador + " <strong>Total Insertados:</strong> " + reg + "<br>");
	            			
	            		}
            		}
            		
            		if (opt_ordenes.trim().equals("1")) {
	            		if (strLinea.equals("3SELECT * FROM APARATOS")) {
	            			out.println("<img src='images/upload.png'><strong>Subiendo Tabla QO_APARATOS</strong><br>");
	            			int contador = 0;
	            			int reg = 0;
	            			strLinea = buffer.readLine();
	            			cArray = strLinea.toCharArray();
	            			
	            			while (strLinea != null && cArray[0] == '1') {
	            				contador++;
	            				String[] registro = strLinea.split("	");
		            			String sql = "INSERT INTO QO_APARATOS (NUM_OS,NIS_RAD,NUM_APA,CO_MARCA,TIP_APA,EST_APA,AOL_APA,TIP_INTENSIDAD,TIP_FASE,TIP_TENSION,F_INST,F_FABRIC,CSMO_PROM) " +
		                                "VALUES (" ;
		            			for (int x=0; x < registro.length ; x++) {
		            				if (x==0) {
		            					sql += "'" + registro [x].substring(1) + "'";
		            				}else {
		            					sql += "'" + registro [x] + "'";
		            				}
		            				
		            				if ((x+1) != registro.length){
		            					sql += ",";
		            				}
		            			}
		            			sql += ")";
		            			//out.print(sql);
		            			try {
		            				conexion.Update("DELETE FROM QO_APARATOS WHERE NUM_OS='" + registro[0].substring(1) + "'");
		            				if (conexion.Update(sql) > 0 ) {
		            					conexion.Commit();
		            					reg++;
		            				}
		            			}catch (SQLException e) {
		            				if (e.getErrorCode() != 1062) { // registro repetido
		            					out.println("<img src='images/alerta.gif'>" +e.getMessage() + ", Error No. " + e.getErrorCode() + "<br>");
		            				}
		            			}
	            				strLinea = buffer.readLine();
	            				if (strLinea != null) {
	            					cArray = strLinea.toCharArray();
	            				}
	            			}
	            			
	            			out.println("<strong>Total registros Leidos:</strong> " + contador + " <strong>Total Insertados:</strong> " + reg + "<br>");
	            			
	            		}
            		}
            		
            		if (opt_ordenes.trim().equals("1")) {
	            		if (strLinea.equals("3SELECT * FROM APACONEN")) {
	            			out.println("<img src='images/upload.png'><strong>Subiendo Tabla QO_APACONEN</strong><br>");
	            			int contador = 0;
	            			int reg = 0;
	            			strLinea = buffer.readLine();
	            			cArray = strLinea.toCharArray();
	            			
	            			while (strLinea != null && cArray[0] == '1') {
	            				contador++;
	            				String[] registro = strLinea.split("	");
		            			String sql = "INSERT INTO QO_APACONEN (NUM_OS,CO_MARCA,NUM_APA,TIP_CSMO,NUM_RUE,CTE,LECT,F_LECT,CSMO,NIS_RAD) " +
		                                "VALUES (" ;
		            			for (int x=0; x < registro.length ; x++) {
		            				if (x==0) {
		            					sql += "'" + registro [x].substring(1) + "'";
		            				}else {
		            					sql += "'" + registro [x] + "'";
		            				}
		            				
		            				if ((x+1) != registro.length){
		            					sql += ",";
		            				}
		            			}
		            			sql += ")";
		            			//out.print(sql);
		            			try {
		            				conexion.Update("DELETE FROM QO_APACONEN WHERE NUM_OS='" + registro[0].substring(1) + "'");
		            				if (conexion.Update(sql) > 0 ) {
		            					conexion.Commit();
		            					reg++;
		            				}
		            			}catch (SQLException e) {
		            				if (e.getErrorCode() != 1062) { // registro repetido
		            					out.println("<img src='images/alerta.gif'>" +e.getMessage() + ", Error No. " + e.getErrorCode() + "<br>");
		            				}
		            			}
	            				strLinea = buffer.readLine();
	            				if (strLinea != null) {
	            					cArray = strLinea.toCharArray();
	            				}
	            			}
	            			
	            			out.println("<strong>Total registros Leidos:</strong> " + contador + " <strong>Total Insertados:</strong> " + reg + "<br>");
	            			
	            		}
            		}
            		
            		/*if (strLinea.equals("3SELECT * FROM CONSUAPA")) {
            			
            			out.println("<img src='images/upload.png'><strong>Subiendo Tabla QO_CONSUAPA</strong><br>");
            			int contador = 0;
            			int reg = 0;
            			strLinea = buffer.readLine();
            			cArray = strLinea.toCharArray();
            			conexion.Update("DELETE FROM QO_CONSUAPA");
            			while (strLinea != null && cArray[0] == '1') {
            				contador++;
            				String[] registro = strLinea.split("	");
	            			String sql = "INSERT INTO QO_CONSUAPA (TIP_CSMO,TIP_APA,IND_BAJA_TEN) " +
	                                "VALUES (" ;
	            			for (int x=0; x < registro.length ; x++) {
	            				if (x==0) {
	            					sql += "'" + registro [x].substring(1) + "'";
	            				}else {
	            					sql += "'" + registro [x] + "'";
	            				}
	            				
	            				if ((x+1) != registro.length){
	            					sql += ",";
	            				}
	            			}
	            			sql += ")";
	            			//out.print(sql);
	            			try {
	            				if (conexion.Update(sql) > 0 ) {
	            					conexion.Commit();
	            					reg++;
	            				}
	            			}catch (SQLException e) {
	            				if (e.getErrorCode() != 1062) { // registro repetido
	            					out.println("<img src='images/alerta.gif'>" +e.getMessage() + ", Error No. " + e.getErrorCode() + "<br>");
	            				}
	            			}
            				strLinea = buffer.readLine();
            				if (strLinea != null) {
            					cArray = strLinea.toCharArray();
            				}
            			}
            			
            			out.println("<strong>Total registros Leidos:</strong> " + contador + " <strong>Total Insertados:</strong> " + reg + "<br>");
            			
            		}*/
            		
            		if (opt_tipoos.trim().equals("1")) {
	            		if (strLinea.equals("3SELECT * FROM TIPOS")) {
	            			out.println("<img src='images/upload.png'><strong>Subiendo Tabla QO_TIPOS</strong><br>");
	            			int contador = 0;
	            			int reg = 0;
	            			strLinea = buffer.readLine();
	            			cArray = strLinea.toCharArray();
	            			
	            			while (strLinea != null && cArray[0] == '1') {
	            				contador++;
	            				String[] registro = strLinea.split("	");
		            			String sql = "INSERT INTO QO_TIPOS (TIPO,DESC_TIPO) " +
		                                "VALUES (" ;
		            			for (int x=0; x < registro.length ; x++) {
		            				if (x==0) {
		            					sql += "'" + registro [x].substring(1) + "'";
		            				}else {
		            					sql += "'" + registro [x] + "'";
		            				}
		            				
		            				if ((x+1) != registro.length){
		            					sql += ",";
		            				}
		            			}
		            			sql += ")";
		            			//out.print(sql);
		            			try {
		            				if (conexion.Update(sql) > 0 ) {
		            					conexion.Commit();
		            					reg++;
		            				}
		            			}catch (SQLException e) {
		            				if (e.getErrorCode() != 1062) { // registro repetido
		            					out.println("<img src='images/alerta.gif'>" +e.getMessage() + ", Error No. " + e.getErrorCode() + "<br>");
		            				}
		            			}
	            				strLinea = buffer.readLine();
	            				if (strLinea != null) {
	            					cArray = strLinea.toCharArray();
	            				}
	            			}
	            			
	            			out.println("<strong>Total registros Leidos:</strong> " + contador + " <strong>Total Insertados:</strong> " + reg + "<br>");
	            			
	            		}
            		}
            		
            		if (opt_material.trim().equals("1")) {
	            		if (strLinea.equals("3SELECT * FROM MATER_I")) {
	            			out.println("<img src='images/upload.png'><strong>Subiendo Tabla QO_MATER_I</strong><br>");
	            			int contador = 0;
	            			int reg = 0;
	            			strLinea = buffer.readLine();
	            			cArray = strLinea.toCharArray();
	            			//conexion.Update("DELETE FROM QO_MATER_I");
	            			while (strLinea != null && cArray[0] == '1') {
	            				contador++;
	            				String[] registro = strLinea.split("	");
		            			String sql = "INSERT INTO QO_MATER_I (COD_CLASE,CO_ACCEJE,CO_ELEMENTO,TIP_ELEMENTO,PREF_ELEMENTO,IND_FORMATO,NUEVO_FLUJO) " +
		                                "VALUES (" ;
		            			for (int x=0; x < registro.length ; x++) {
		            				if (x==0) {
		            					sql += "'" + registro [x].substring(1) + "'";
		            				}else {
		            					sql += "'" + registro [x] + "'";
		            				}
		            				
		            				if ((x+1) != registro.length){
		            					sql += ",";
		            				}
		            			}
		            			sql += ")";
		            			//out.print(sql);
		            			try {
		            				if (conexion.Update(sql) > 0 ) {
		            					conexion.Commit();
		            					reg++;
		            				}
		            			}catch (SQLException e) {
		            				if (e.getErrorCode() != 1062) { // registro repetido
		            					out.println("<img src='images/alerta.gif'>" +e.getMessage() + ", Error No. " + e.getErrorCode() + "<br>");
		            				}
		            			}
	            				strLinea = buffer.readLine();
	            				if (strLinea != null) {
	            					cArray = strLinea.toCharArray();
	            				}
	            			}
	            			
	            			out.println("<strong>Total registros Leidos:</strong> " + contador + " <strong>Total Insertados:</strong> " + reg + "<br>");
	            			
	            		}
            		}
            		
            		if (opt_material.trim().equals("1")) {
	            		if (strLinea.equals("3SELECT * FROM MATER_R")) {
	            			out.println("<img src='images/upload.png'><strong>Subiendo Tabla QO_MATER_R</strong><br>");
	            			int contador = 0;
	            			int reg = 0;
	            			strLinea = buffer.readLine();
	            			cArray = strLinea.toCharArray();
	            			//conexion.Update("DELETE FROM QO_MATER_R");
	            			while (strLinea != null && cArray[0] == '1') {
	            				contador++;
	            				String[] registro = strLinea.split("	");
		            			String sql = "INSERT INTO QO_MATER_R (COD_CLASE,CO_ACCEJE,CO_ELEMENTO,TIP_ELEMENTO,PREF_ELEMENTO,IND_FORMATO,NUEVO_FLUJO) " +
		                                "VALUES (" ;
		            			for (int x=0; x < registro.length ; x++) {
		            				if (x==0) {
		            					sql += "'" + registro [x].substring(1) + "'";
		            				}else {
		            					sql += "'" + registro [x] + "'";
		            				}
		            				
		            				if ((x+1) != registro.length){
		            					sql += ",";
		            				}
		            			}
		            			sql += ")";
		            			//out.print(sql);
		            			try {
		            				if (conexion.Update(sql) > 0 ) {
		            					conexion.Commit();
		            					reg++;
		            				}
		            			}catch (SQLException e) {
		            				if (e.getErrorCode() != 1062) { // registro repetido
		            					out.println("<img src='images/alerta.gif'>" +e.getMessage() + ", Error No. " + e.getErrorCode() + "<br>");
		            				}
		            			}
	            				strLinea = buffer.readLine();
	            				if (strLinea != null) {
	            					cArray = strLinea.toCharArray();
	            				}
	            			}
	            			
	            			out.println("<strong>Total registros Leidos:</strong> " + contador + " <strong>Total Insertados:</strong> " + reg + "<br>");
	            			
	            		}
            		}
            		
            		if (opt_material.trim().equals("1")) {
	            		if (strLinea.equals("3SELECT * FROM DESC_MI")) {
	            			out.println("<img src='images/upload.png'><strong>Subiendo Tabla QO_DESC_MI</strong><br>");
	            			int contador = 0;
	            			int reg = 0;
	            			strLinea = buffer.readLine();
	            			cArray = strLinea.toCharArray();
	            			
	            			while (strLinea != null && cArray[0] == '1') {
	            				contador++;
	            				String[] registro = strLinea.split("	");
	            				String sql = "INSERT INTO QO_DESC_MI (CO_ELEMENTO,DESC_ELEMENTO";
	            				if (registro.length == 3) {
	            					sql += ",NUEVO_FLUJO " ;
	            				}
		                         sql += ") VALUES (" ;
		            			for (int x=0; x < registro.length ; x++) {
		            				if (x==0) {
		            					sql += "'" + registro [x].substring(1) + "'";
		            				}else {
		            					sql += "'" + registro [x] + "'";
		            				}
		            				
		            				if ((x+1) != registro.length){
		            					sql += ",";
		            				}
		            			}
		            			sql += ")";
		            			//out.print(sql);
		            			try {
		            				if (conexion.Update(sql) > 0 ) {
		            					conexion.Commit();
		            					reg++;
		            				}
		            			}catch (SQLException e) {
		            				if (e.getErrorCode() != 1062) { // registro repetido
		            					out.println("<img src='images/alerta.gif'>" +e.getMessage() + ", Error No. " + e.getErrorCode() + "<br>");
		            				}
		            			}
	            				strLinea = buffer.readLine();
	            				if (strLinea != null) {
	            					cArray = strLinea.toCharArray();
	            				}
	            			}
	            			
	            			out.println("<strong>Total registros Leidos:</strong> " + contador + " <strong>Total Insertados:</strong> " + reg + "<br>");
	            			
	            		}
            		}
            		
            		if (opt_material.trim().equals("1")) {
	            		if (strLinea.equals("3SELECT * FROM DESC_MR")) {
	            			out.println("<img src='images/upload.png'><strong>Subiendo Tabla QO_DESC_MR</strong><br>");
	            			int contador = 0;
	            			int reg = 0;
	            			strLinea = buffer.readLine();
	            			cArray = strLinea.toCharArray();
	            			
	            			while (strLinea != null && cArray[0] == '1') {
	            				contador++;
	            				String[] registro = strLinea.split("	");
		            			
	            				String sql = "INSERT INTO QO_DESC_MR (CO_ELEMENTO,DESC_ELEMENTO";
	            				if (registro.length == 3) {
	            					sql += ",NUEVO_FLUJO " ;
	            				}
		                         sql += ") VALUES (" ;
		            			
		            			for (int x=0; x < registro.length ; x++) {
		            				if (x==0) {
		            					sql += "'" + registro [x].substring(1) + "'";
		            				}else {
		            					sql += "'" + registro [x] + "'";
		            				}
		            				
		            				if ((x+1) != registro.length){
		            					sql += ",";
		            				}
		            			}
		            			sql += ")";
		            			//out.print(sql);
		            			try {
		            				if (conexion.Update(sql) > 0 ) {
		            					conexion.Commit();
		            					reg++;
		            				}
		            			}catch (SQLException e) {
		            				if (e.getErrorCode() != 1062) { // registro repetido
		            					out.println("<img src='images/alerta.gif'>" +e.getMessage() + ", Error No. " + e.getErrorCode() + "<br>");
		            				}
		            			}
	            				strLinea = buffer.readLine();
	            				if (strLinea != null) {
	            					cArray = strLinea.toCharArray();
	            				}
	            			}
	            			
	            			out.println("<strong>Total registros Leidos:</strong> " + contador + " <strong>Total Insertados:</strong> " + reg + "<br>");
	            			
	            		}
            		}
            		
            		
            		/*if (strLinea.equals("3SELECT * FROM CLASES_I")) {
            			out.println("<img src='images/upload.png'><strong>Subiendo Tabla QO_CLASES_I</strong><br>");
            			int contador = 0;
            			int reg = 0;
            			strLinea = buffer.readLine();
            			cArray = strLinea.toCharArray();
            			conexion.Update("DELETE FROM QO_CLASES_I");
            			while (strLinea != null && cArray[0] == '1') {
            				contador++;
            				String[] registro = strLinea.split("	");
	            			String sql = "INSERT INTO QO_CLASES_I (NUM_PASO,CO_ACCEJE,COD_CLASE,DESC_CLASE,NUEVO_FLUJO) " +
	                                "VALUES (" ;
	            			for (int x=0; x < registro.length ; x++) {
	            				if (x==0) {
	            					sql += "'" + registro [x].substring(1) + "'";
	            				}else {
	            					sql += "'" + registro [x] + "'";
	            				}
	            				
	            				if ((x+1) != registro.length){
	            					sql += ",";
	            				}
	            			}
	            			sql += ")";
	            			//out.print(sql);
	            			try {
	            				if (conexion.Update(sql) > 0 ) {
	            					conexion.Commit();
	            					reg++;
	            				}
	            			}catch (SQLException e) {
	            				if (e.getErrorCode() != 1062) { // registro repetido
	            					out.println("<img src='images/alerta.gif'>" +e.getMessage() + ", Error No. " + e.getErrorCode() + "<br>");
	            				}
	            			}
            				strLinea = buffer.readLine();
            				if (strLinea != null) {
            					cArray = strLinea.toCharArray();
            				}
            			}
            			
            			out.println("<strong>Total registros Leidos:</strong> " + contador + " <strong>Total Insertados:</strong> " + reg + "<br>");
            			
            		}
            		
            		if (strLinea.equals("3SELECT * FROM CLASES_R")) {
            			out.println("<img src='images/upload.png'><strong>Subiendo Tabla QO_CLASES_R</strong><br>");
            			int contador = 0;
            			int reg = 0;
            			strLinea = buffer.readLine();
            			cArray = strLinea.toCharArray();
            			conexion.Update("DELETE FROM QO_CLASES_R");
            			while (strLinea != null && cArray[0] == '1') {
            				contador++;
            				String[] registro = strLinea.split("	");
	            			String sql = "INSERT INTO QO_CLASES_R (NUM_PASO,CO_ACCEJE,COD_CLASE,DESC_CLASE,NUEVO_FLUJO) " +
	                                "VALUES (" ;
	            			for (int x=0; x < registro.length ; x++) {
	            				if (x==0) {
	            					sql += "'" + registro [x].substring(1) + "'";
	            				}else {
	            					sql += "'" + registro [x] + "'";
	            				}
	            				
	            				if ((x+1) != registro.length){
	            					sql += ",";
	            				}
	            			}
	            			sql += ")";
	            			//out.print(sql);
	            			try {
	            				if (conexion.Update(sql) > 0 ) {
	            					conexion.Commit();
	            					reg++;
	            				}
	            			}catch (SQLException e) {
	            				if (e.getErrorCode() != 1062) { // registro repetido
	            					out.println("<img src='images/alerta.gif'>" +e.getMessage() + ", Error No. " + e.getErrorCode() + "<br>");
	            				}
	            			}
            				strLinea = buffer.readLine();
            				if (strLinea != null) {
            					cArray = strLinea.toCharArray();
            				}
            			}
            			
            			out.println("<strong>Total registros Leidos:</strong> " + contador + " <strong>Total Insertados:</strong> " + reg + "<br>");
            			
            		}*/
            		
            		if (opt_codigos.equals("1")) {
	            		if (strLinea.equals("3SELECT * FROM CODIGOS")) {
	            			out.println("<img src='images/upload.png'><strong>Subiendo Tabla QO_CODIGOS</strong><br>");
	            			int contador = 0;
	            			int reg = 0;
	            			strLinea = buffer.readLine();
	            			cArray = strLinea.toCharArray();
	            			
	            			while (strLinea != null && cArray[0] == '1') {
	            				contador++;
	            				String[] registro = strLinea.split("	");
		            			String sql = "INSERT INTO QO_CODIGOS (COD,DESC_COD) " +
		                                "VALUES (" ;
		            			for (int x=0; x < registro.length ; x++) {
		            				if (x==0) {
		            					sql += "'" + registro [x].substring(1) + "'";
		            				}else {
		            					sql += "'" + registro [x] + "'";
		            				}
		            				
		            				if ((x+1) != registro.length){
		            					sql += ",";
		            				}
		            			}
		            			sql += ")";
		            			//out.print(sql);
		            			try {
		            				if (conexion.Update(sql) > 0 ) {
		            					conexion.Commit();
		            					reg++;
		            				}
		            			}catch (SQLException e) {
		            				if (e.getErrorCode() != 1062) { // registro repetido
		            					out.println("<img src='images/alerta.gif'>" +e.getMessage() + ", Error No. " + e.getErrorCode() + "<br>");
		            				}
		            			}
	            				strLinea = buffer.readLine();
	            				if (strLinea != null) {
	            					cArray = strLinea.toCharArray();
	            				}
	            			}
	            			
	            			out.println("<strong>Total registros Leidos:</strong> " + contador + " <strong>Total Insertados:</strong> " + reg + "<br>");
	            			
	            		}
            		}
            		
            		if (opt_anomalias.trim().equals("1")) {
	            		if (strLinea.equals("3SELECT * FROM ANOM")) {
	            			out.println("<img src='images/upload.png'><strong>Subiendo Tabla QO_ANOM</strong><br>");
	            			int contador = 0;
	            			int reg = 0;
	            			strLinea = buffer.readLine();
	            			cArray = strLinea.toCharArray();
	            			
	            			while (strLinea != null && cArray[0] == '1') {
	            				contador++;
	            				String[] registro = strLinea.split("	");
		            			String sql = "INSERT INTO QO_ANOM (COD,DESC_COD) " +
		                                "VALUES (" ;
		            			for (int x=0; x < registro.length ; x++) {
		            				if (x==0) {
		            					sql += "'" + registro [x].substring(1) + "'";
		            				}else {
		            					sql += "'" + registro [x] + "'";
		            				}
		            				
		            				if ((x+1) != registro.length){
		            					sql += ",";
		            				}
		            			}
		            			sql += ")";
		            			//out.print(sql);
		            			try {
		            				if (conexion.Update(sql) > 0 ) {
		            					conexion.Commit();
		            					reg++;
		            				}
		            			}catch (SQLException e) {
		            				if (e.getErrorCode() != 1062) { // registro repetido
		            					out.println("<img src='images/alerta.gif'>" +e.getMessage() + ", Error No. " + e.getErrorCode() + "<br>");
		            				}
		            			}
	            				strLinea = buffer.readLine();
	            				if (strLinea != null) {
	            					cArray = strLinea.toCharArray();
	            				}
	            			}
	            			
	            			out.println("<strong>Total registros Leidos:</strong> " + contador + " <strong>Total Insertados:</strong> " + reg + "<br>");
	            			
	            		}
            		}
            		
//            		if (strLinea.equals("3SELECT * FROM TARIFAS")) {
//            			out.println("<img src='images/upload.png'><strong>Subiendo Tabla QO_TARIFAS</strong><br>");
//            			int contador = 0;
//            			int reg = 0;
//            			strLinea = buffer.readLine();
//            			cArray = strLinea.toCharArray();
//            			
//            			while (strLinea != null && cArray[0] == '1') {
//            				contador++;
//            				String[] registro = strLinea.split("	");
//	            			String sql = "INSERT INTO QO_TARIFAS (COD_TAR,DESC_TAR) " +
//	                                "VALUES (" ;
//	            			for (int x=0; x < registro.length ; x++) {
//	            				if (x==0) {
//	            					sql += "'" + registro [x].substring(1) + "'";
//	            				}else {
//	            					sql += "'" + registro [x] + "'";
//	            				}
//	            				
//	            				if ((x+1) != registro.length){
//	            					sql += ",";
//	            				}
//	            			}
//	            			sql += ")";
//	            			//out.print(sql);
//	            			try {
//	            				if (conexion.Update(sql) > 0 ) {
//	            					conexion.Commit();
//	            					reg++;
//	            				}
//	            			}catch (SQLException e) {
//	            				if (e.getErrorCode() != 1062) { // registro repetido
//	            					out.println("<img src='images/alerta.gif'>" +e.getMessage() + ", Error No. " + e.getErrorCode() + "<br>");
//	            				}
//	            			}
//            				strLinea = buffer.readLine();
//            				if (strLinea != null) {
//            					cArray = strLinea.toCharArray();
//            				}
//            			}
//            			
//            			out.println("<strong>Total registros Leidos:</strong> " + contador + " <strong>Total Insertados:</strong> " + reg + "<br>");
//            			
//            		}
            		
            		if (opt_ordenes.trim().equals("1")) {
	            		if (strLinea.equals("3SELECT * FROM OSACCION")) {
	            			out.println("<img src='images/upload.png'><strong>Subiendo Tabla QO_OSACCION</strong><br>");
	            			int contador = 0;
	            			int reg = 0;
	            			strLinea = buffer.readLine();
	            			cArray = strLinea.toCharArray();
	            			//conexion.Update("DELETE FROM QO_OSACCION");
	            			while (strLinea != null && cArray[0] == '1') {
	            				contador++;
	            				String[] registro = strLinea.split("	");
		            			String sql = "INSERT INTO QO_OSACCION (TIP_OS,NUM_PASO,CO_ACCEJE,IND_LIQUIDABLE,DESC_COD) " +
		                                "VALUES (" ;
		            			for (int x=0; x < registro.length ; x++) {
		            				if (x==0) {
		            					sql += "'" + registro [x].substring(1) + "'";
		            				}else {
		            					sql += "'" + registro [x] + "'";
		            				}
		            				
		            				if ((x+1) != registro.length){
		            					sql += ",";
		            				}
		            			}
		            			sql += ")";
		            			//out.print(sql);
		            			try {
		            				if (conexion.Update(sql) > 0 ) {
		            					conexion.Commit();
		            					reg++;
		            				}
		            			}catch (SQLException e) {
		            				if (e.getErrorCode() != 1062) { // registro repetido
		            					out.println("<img src='images/alerta.gif'>" +e.getMessage() + ", Error No. " + e.getErrorCode() + "<br>");
		            				}
		            			}
	            				strLinea = buffer.readLine();
	            				if (strLinea != null) {
	            					cArray = strLinea.toCharArray();
	            				}
	            			}
	            			
	            			out.println("<strong>Total registros Leidos:</strong> " + contador + " <strong>Total Insertados:</strong> " + reg + "<br>");
	            			
	            		}
            		}
            		
            		if (opt_ordenes.trim().equals("1")) {
	            		if (strLinea.equals("3SELECT * FROM DATOSUM")) {
	            			out.println("<img src='images/upload.png'><strong>Subiendo Tabla QO_DATOSUM</strong><br>");
	            			int contador = 0;
	            			int reg = 0;
	            			strLinea = buffer.readLine();
	            			cArray = strLinea.toCharArray();
	            			
	            			while (strLinea != null && cArray[0] == '1') {
	            				contador++;
	            				String[] registro = strLinea.split("	");
		            			String sql = "INSERT INTO QO_DATOSUM (NIS_RAD,SEC_NIS,NIC,TIP_SERV,TIP_SUMINISTRO,COD_TAR,TIP_CONEXION,TIP_TENSION,POT,NUM_EXP,NUM_RE,RUTA,NUM_ITIN,MUNICIPIO,LOCALIDAD,DEPARTAMENTO,TIP_VIA,CALLE,NUM_PUERTA,DUPLICADOR,CGV_SUM,NOM_FINCA,REF_DIR,ACC_FINCA,APART_POSTAL,APE1_CLI,APE2_CLI,NOM_CLI,TFNO_CLI,TIP_CLI,DIRECCION) " +
		                                "VALUES (" ;
		            			for (int x=0; x < registro.length ; x++) {
		            				if (x==0) {
		            					sql += "'" + registro [x].substring(1) + "'";
		            				}else {
		            					sql += "'" + registro [x].replace("'", "") + "'";
		            				}
		            				
		            				if ((x+1) != registro.length){
		            					sql += ",";
		            				}
		            			}
		            			sql += ")";
		            			//out.print(sql);
		            			try {
		            				if (conexion.Update(sql) > 0 ) {
		            					conexion.Commit();
		            					reg++;
		            				}
		            			}catch (SQLException e) {
		            				if (e.getErrorCode() != 1062) { // registro repetido
		            					out.println("<img src='images/alerta.gif'>" +e.getMessage() + ", Error No. " + e.getErrorCode() + "<br>");
		            				}
		            			}
		            			
	            				strLinea = buffer.readLine();
	            				if (strLinea != null) {
	            					cArray = strLinea.toCharArray();
	            				}
	            			}
	            			
	            			out.println("<strong>Total registros Leidos:</strong> " + contador + " <strong>Total Insertados:</strong> " + reg + "<br>");
	            			
	            		}
            		}
            		
            		if (opt_flujos.equals("1")) {
	            		if (strLinea.equals("3SELECT * FROM OSACCIONFLUJO")) {
	            			out.println("<img src='images/upload.png'><strong>Subiendo Tabla QO_OSACCIONFLUJO</strong><br>");
	            			int contador = 0;
	            			int reg = 0;
	            			strLinea = buffer.readLine();
	            			cArray = strLinea.toCharArray();
	            			
	            			while (strLinea != null && cArray[0] == '1') {
	            				contador++;
	            				String[] registro = strLinea.split("	");
		            			String sql = "INSERT INTO QO_OSACCIONFLUJO (TIP_OS,NUM_PASO,CO_ACCEJE,IND_LIQUIDABLE,DESC_COD,TIP_FLUJO) " +
		                                "VALUES (" ;
		            			for (int x=0; x < registro.length ; x++) {
		            				if (x==0) {
		            					sql += "'" + registro [x].substring(1) + "'";
		            				}else {
		            					sql += "'" + registro [x] + "'";
		            				}
		            				
		            				if ((x+1) != registro.length){
		            					sql += ",";
		            				}
		            			}
		            			sql += ")";
		            			//out.print(sql);
		            			try {
		            				if (conexion.Update(sql) > 0 ) {
		            					conexion.Commit();
		            					reg++;
		            				}
		            			}catch (SQLException e) {
		            				if (e.getErrorCode() != 1062) { // registro repetido
		            					out.println("<img src='images/alerta.gif'>" +e.getMessage() + ", Error No. " + e.getErrorCode() + "<br>");
		            				}
		            			}
	            				strLinea = buffer.readLine();
	            				if (strLinea != null) {
	            					cArray = strLinea.toCharArray();
	            				}
	            			}
	            			
	            			out.println("<strong>Total registros Leidos:</strong> " + contador + " <strong>Total Insertados:</strong> " + reg + "<br>");
	            			
	            		}
            		}
            		
            		if (opt_flujos.equals("1")) {
	            		if (strLinea.equals("3SELECT * FROM PASOSFLUJO")) {
	            			out.println("<img src='images/upload.png'><strong>Subiendo Tabla QO_PASOSFLUJO</strong><br>");
	            			int contador = 0;
	            			int reg = 0;
	            			strLinea = buffer.readLine();
	            			cArray = strLinea.toCharArray();
	            			while (strLinea != null && cArray[0] == '1') {
	            				contador++;
	            				String[] registro = strLinea.split("	");
		            			String sql = "INSERT INTO QO_PASOSFLUJO (TIP_FLUJO,NUM_PASO,OPCOND,DESCRIPCION,CONDICION,ELSEACCION,CO_ACCEJE) " +
		                                "VALUES (" ;
		            			for (int x=0; x < registro.length ; x++) {
		            				if (x==0) {
		            					sql += "'" + registro [x].substring(1) + "'";
		            				}else {
		            					sql += "'" + registro [x] + "'";
		            				}
		            				
		            				if ((x+1) != registro.length){
		            					sql += ",";
		            				}
		            			}
		            			sql += ")";
		            			//out.print(sql);
		            			try {
		            				if (conexion.Update(sql) > 0 ) {
		            					conexion.Commit();
		            					reg++;
		            				}
		            			}catch (SQLException e) {
		            				if (e.getErrorCode() != 1062) { // registro repetido
		            					out.println("<img src='images/alerta.gif'>" +e.getMessage() + ", Error No. " + e.getErrorCode() + "<br>");
		            				}
		            			}
	            				strLinea = buffer.readLine();
	            				if (strLinea != null) {
	            					cArray = strLinea.toCharArray();
	            				}
	            			}
	            			
	            			out.println("<strong>Total registros Leidos:</strong> " + contador + " <strong>Total Insertados:</strong> " + reg + "<br>");
	            			
	            		}
            		}
            		
            		if (opt_flujos.equals("1")) {
	            		if (strLinea.equals("3SELECT * FROM OSFLUJO")) {
	            			out.println("<img src='images/upload.png'><strong>Subiendo Tabla QO_OSFLUJO</strong><br>");
	            			int contador = 0;
	            			int reg = 0;
	            			strLinea = buffer.readLine();
	            			cArray = strLinea.toCharArray();
	            			while (strLinea != null && cArray[0] == '1') {
	            				contador++;
	            				String[] registro = strLinea.split("	");
		            			String sql = "INSERT INTO QO_OSFLUJO (TIP_FLUJO,TIP_OS,CO_ACCEJE) " +
		                                "VALUES (" ;
		            			for (int x=0; x < registro.length ; x++) {
		            				if (x==0) {
		            					sql += "'" + registro [x].substring(1) + "'";
		            				}else {
		            					sql += "'" + registro [x] + "'";
		            				}
		            				
		            				if ((x+1) != registro.length){
		            					sql += ",";
		            				}
		            			}
		            			sql += ")";
		            			//out.print(sql);
		            			try {
		            				if (conexion.Update(sql) > 0 ) {
		            					conexion.Commit();
		            					reg++;
		            				}
		            			}catch (SQLException e) {
		            				if (e.getErrorCode() != 1062) { // registro repetido
		            					out.println("<img src='images/alerta.gif'>" +e.getMessage() + ", Error No. " + e.getErrorCode() + "<br>");
		            				}
		            			}
	            				strLinea = buffer.readLine();
	            				if (strLinea != null) {
	            					cArray = strLinea.toCharArray();
	            				}
	            			}
	            			
	            			out.println("<strong>Total registros Leidos:</strong> " + contador + " <strong>Total Insertados:</strong> " + reg + "<br>");
	            			
	            		}
            		}
            		
            		if (opt_ordenes.trim().equals("1")) {
	            		if (strLinea.equals("3SELECT * FROM RECIBOS")) {
	            			out.println("<img src='images/upload.png'><strong>Subiendo Tabla QO_RECIBOS</strong><br>");
	            			int contador = 0;
	            			int reg = 0;
	            			strLinea = buffer.readLine();
	            			cArray = strLinea.toCharArray();
	            			
	            			while (strLinea != null && cArray[0] == '1') {
	            				contador++;
	            				String[] registro = strLinea.split("	");
	            				String sql = "INSERT INTO QO_RECIBOS (NUM_OS,NIS_RAD,SEC_REC,SEC_NIS,F_FACT,SIMBOLO_VAR,F_VCTO_FACT,IMP_TOT_REC,IMP_CTA) " +
		                                "VALUES (" ;
		            			for (int x=0; x < registro.length ; x++) {
		            				if (x==0) {
		            					sql += "'" + registro [x].substring(1) + "'";
		            				}else {
		            					sql += "'" + registro [x] + "'";
		            				}
		            				
		            				if ((x+1) != registro.length){
		            					sql += ",";
		            				}
		            			}
		            			sql += ")";
		            			//out.print(sql);
		            			try {
		            				
		            				if (conexion.Update(sql) > 0 ) {
		            					conexion.Commit();
		            					reg++;
		            				}
		            			}catch (SQLException e) {
		            				if (e.getErrorCode() != 1062) { // registro repetido
		            					out.println("<img src='images/alerta.gif'>" +e.getMessage() + ", Error No. " + e.getErrorCode() + "<br>");
		            				}
		            			}
	            				strLinea = buffer.readLine();
	            				if (strLinea != null) {
	            					cArray = strLinea.toCharArray();
	            				}
	            			}
	            			
	            			out.println("<strong>Total registros Leidos:</strong> " + contador + " <strong>Total Insertados:</strong> " + reg + "<br>");
	            			
	            		}
            		}
            		
            		if (opt_ordenes.trim().equals("1")) {
	            		if (strLinea.equals("3SELECT * FROM PRECIN")) {
	            			out.println("<img src='images/upload.png'><strong>Subiendo Tabla QO_PRECIN</strong><br>");
	            			int contador = 0;
	            			int reg = 0;
	            			strLinea = buffer.readLine();
	            			cArray = strLinea.toCharArray();
	            			
	            			while (strLinea != null && cArray[0] == '1') {
	            				contador++;
	            				String[] registro = strLinea.split("	");
		            			String sql = "INSERT INTO QO_PRECIN (NUM_OS,NUM_APA,CO_MARCA,NUM_PRECIN) " +
		                                "VALUES (" ;
		            			for (int x=0; x < registro.length ; x++) {
		            				if (x==0) {
		            					sql += "'" + registro [x].substring(1) + "'";
		            				}else {
		            					sql += "'" + registro [x] + "'";
		            				}
		            				
		            				if ((x+1) != registro.length){
		            					sql += ",";
		            				}
		            			}
		            			sql += ")";
		            			//out.print(sql);
		            			try {
		            				conexion.Update("DELETE FROM QO_PRECIN WHERE NUM_OS='" + registro[0].substring(1) + "'");
		            				if (conexion.Update(sql) > 0 ) {
		            					conexion.Commit();
		            					reg++;
		            				}
		            			}catch (SQLException e) {
		            				if (e.getErrorCode() != 1062) { // registro repetido
		            					out.println("<img src='images/alerta.gif'>" +e.getMessage() + ", Error No. " + e.getErrorCode() + "<br>");
		            				}
		            			}
	            				strLinea = buffer.readLine();
	            				if (strLinea != null) {
	            					cArray = strLinea.toCharArray();
	            				}
	            			}
	            			
	            			out.println("<strong>Total registros Leidos:</strong> " + contador + " <strong>Total Insertados:</strong> " + reg + "<br>");
	            			
	            		}
            		}
            		
            	}
            	
            	if (strLinea != null) {
            		
            		strLinea = buffer.readLine();
            	}
            }
            
            // Cerramos el archivo
            entrada.close();
			
		
		} catch (FileNotFoundException e) {
			out.println("<img src='images/alerta.gif'>" + e.getMessage() + "<br>");
		} catch (IOException e) {
			out.println("<img src='images/alerta.gif'>" +e.getMessage() + "<br>");
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			out.println("<img src='images/alerta.gif'>" +e.getMessage() + "<br>");
		} catch (Exception e) {
			out.println("<img src='images/alerta.gif'>" + e.getMessage() + "<br>");
		}finally {
			
			if (conexion != null) {
				try {
					conexion.Close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					out.println("<img src='images/alerta.gif'>" +e.getMessage() + "<br>");
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
        
        private boolean updateCenso(String orden, db conexion) throws SQLException {
            boolean result = false;
            String sql = "SELECT nro_acta,tarifa,uso,fecha_acta,ct,mt,fecha_ejecucion,visitas.brigada "
                                + " FROM visitas,camp_orden "
                                + " WHERE camp_orden.id_visita = visitas.id "
                                + " AND camp_orden.num_os =? AND camp_orden.id_visita != 0 ";
            java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
            pst.setString(1, orden);
            java.sql.ResultSet rs = conexion.Query(pst);
            if (rs.next()) {
                sql = "UPDATE qo_ordenes SET NUM_ACTA=?, NUM_CT=?, NUM_MT=?, TARIFA=?, CIUU=?,FECHA_CENSO=?, NOMBRE_OPERARIO_HDA=? "
                                    + " WHERE NUM_OS=?";
                            java.sql.PreparedStatement pst4 = conexion.getConnection().prepareStatement(sql);
                            pst4.setString(1, rs.getString("nro_acta"));
                            pst4.setString(2, rs.getString("ct"));
                            pst4.setString(3, rs.getString("mt"));
                            pst4.setString(4, rs.getString("tarifa"));
                            pst4.setString(5, rs.getString("uso"));
                            pst4.setString(6, rs.getString("fecha_acta"));
                            pst4.setString(7, rs.getString("brigada"));
                            pst4.setString(8, orden);
                            
                            if (conexion.Update(pst4) > 0) {
                                result = true;
                            }
                
                
            }else {
                result = true;
            }
            
            
            return result;
        }

}
