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
@WebServlet("/SrvFileOrdenes")
public class SrvFileOrdenes extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SrvFileOrdenes() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    protected void ProcesarPeticion(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
    	
    	response.setContentType("text/html;charset=ISO-8859-1");
        PrintWriter out = response.getWriter();
        
        
        String imei = (String)request.getParameter("imei");
        db conexion = null;
        
        try {
        	
        	conexion = new db();
        	
        	Equipos eq = new Equipos(conexion);
			if (!eq.Find(imei)) {
				throw new Exception("IMEI no se encuentra registrado");
			}

			if (eq.getActivo() != 1) {
				throw new Exception("IMEI no se encuentra activo");
			}
			
			if (eq.getRecurso().equals("-1")) {
				throw new Exception("Equipo no asignado a un tecnico");
			}
        	
        	String sql = "SELECT * FROM QO_ORDENES WHERE ESTADO_OPER=1 AND TECNICO=?";
        	java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        	pst.setString(1, eq.getRecurso());
        	ResultSet rs = conexion.Query(pst);
        	out.print("3ORDENES\r\n");
        	while(rs.next()) {
        		String cadena = "1" + LimpiarCadena(rs.getString("NUM_OS")) + ",";
        		cadena += LimpiarCadena(rs.getString("TIP_OS")) + ",";
        		cadena += LimpiarCadena(rs.getString("TIP_SERV")) + ",";
        		cadena += LimpiarCadena(rs.getString("F_GEN")) + ",";
        		cadena += LimpiarCadena(rs.getString("F_ESTM_REST")) + ",";
        		cadena += LimpiarCadena(rs.getString("HORA_CITA")) + ",";
        		cadena += LimpiarCadena(rs.getString("CO_PRIOR_ORD")) + ",";
        		cadena += LimpiarCadena(rs.getString("NIS_RAD")) + ",";
        		cadena += LimpiarCadena(rs.getString("NIC")) + ",";
        		cadena += LimpiarCadena(rs.getString("SEC_NIS")) + ",";
        		cadena += LimpiarCadena(rs.getString("NUM_LOTE")) + ",";
        		cadena += LimpiarCadena(rs.getString("COD_EMP_ASIG")) + ",";
        		cadena += LimpiarCadena(rs.getString("NUM_CAMP")) + ",";
        		cadena += LimpiarCadena(rs.getString("IND_MAT_UUCC")) + ",";
        		cadena += LimpiarCadena(rs.getString("IND_ACTA")) + ",";
        		cadena += LimpiarCadena(rs.getString("COMENT_OS")) + ",";
        		cadena += LimpiarCadena(rs.getString("COMENT_OS2")) + ",";
        		cadena += LimpiarCadena(rs.getString("DESC_TIPO_ORDEN")) + ",";
        		cadena += LimpiarCadena(rs.getString("DESC_COD_PRIORIDAD")) + ",";
        		cadena += LimpiarCadena(rs.getString("DIRECCION")) + ",";
        		cadena += LimpiarCadena(rs.getString("RUTAITIN")) + ",";
        		cadena += LimpiarCadena(rs.getString("ESTADO")) + ",";
        		cadena += LimpiarCadena(rs.getString("RESERVA"));
        		out.print(cadena+"\r\n");	
        	}
        	rs.close();
        	
        	sql = "SELECT QO_DATOSUM.* " +
        			" FROM QO_DATOSUM,QO_ORDENES " +
        			" WHERE QO_DATOSUM.NIC = QO_ORDENES.NIC " +
        			" AND QO_DATOSUM.NIS_RAD = QO_ORDENES.NIS_RAD  " +
        			" AND ESTADO_OPER=1 " +
        			" AND TECNICO=?";
        	pst = conexion.getConnection().prepareStatement(sql);
        	pst.setString(1, eq.getRecurso());
        	rs = conexion.Query(pst);
        	
        	out.print("3DATOSUM\r\n");
        	while(rs.next()) {
        		String cadena = "1" + (rs.getString("NIS_RAD").equals("")?" ":rs.getString("NIS_RAD")) + ",";  // 1
        		cadena += (rs.getString("SEC_NIS").equals("")?" ":LimpiarCadena(rs.getString("SEC_NIS"))) + ","; // 2
        		cadena += (rs.getString("NIC").equals("")?" ":LimpiarCadena(rs.getString("NIC")))+ ","; // 3
        		cadena += (rs.getString("TIP_SERV").equals("")?" ":LimpiarCadena(rs.getString("TIP_SERV")))+ ","; // 4
        		cadena += (rs.getString("TIP_SUMINISTRO").equals("")?" ":LimpiarCadena(rs.getString("TIP_SUMINISTRO")))+ ","; // 5
        		cadena += (rs.getString("COD_TAR").equals("")?" ":LimpiarCadena(rs.getString("COD_TAR")))+ ","; // 6
        		cadena += (rs.getString("TIP_CONEXION").equals("")?" ":LimpiarCadena(rs.getString("TIP_CONEXION"))) + ","; // 7
        		cadena += (rs.getString("TIP_TENSION").equals("")?" ":LimpiarCadena(rs.getString("TIP_TENSION"))) + ","; //8
        		cadena += (rs.getString("POT").equals("")?" ":LimpiarCadena(rs.getString("POT")))+ ","; // 9
        		cadena += (rs.getString("NUM_EXP").equals("")?" ":LimpiarCadena(rs.getString("NUM_EXP"))) + ","; // 10
        		cadena += (rs.getString("NUM_RE").equals("")?" ":LimpiarCadena(rs.getString("NUM_RE")))+ ","; // 11
        		cadena += (rs.getString("RUTA").equals("")?" ":LimpiarCadena(rs.getString("RUTA")))+ ","; // 12
        		cadena += (rs.getString("NUM_ITIN").equals("")?" ":LimpiarCadena(rs.getString("NUM_ITIN"))) + ","; // 13
        		cadena += (rs.getString("MUNICIPIO").equals("")?" ":LimpiarCadena(rs.getString("MUNICIPIO"))) + ","; // 14
        		cadena += (rs.getString("LOCALIDAD").equals("")?" ":LimpiarCadena(rs.getString("LOCALIDAD"))) + ","; // 15
        		cadena += (rs.getString("DEPARTAMENTO").equals("")?" ":LimpiarCadena(rs.getString("DEPARTAMENTO"))) + ","; // 16
        		cadena += (rs.getString("TIP_VIA").equals("")?" ":LimpiarCadena(rs.getString("TIP_VIA"))) + ","; // 17
        		cadena += (rs.getString("CALLE").equals("")?" ":LimpiarCadena(rs.getString("CALLE"))) + ","; //18
        		cadena += (rs.getString("NUM_PUERTA").equals("")?" ":LimpiarCadena(rs.getString("NUM_PUERTA"))) + ",";//19
        		cadena += (rs.getString("DUPLICADOR").equals("")?" ":LimpiarCadena(rs.getString("DUPLICADOR"))) + ","; //20
        		cadena += (rs.getString("CGV_SUM").equals("")?" ":LimpiarCadena(rs.getString("CGV_SUM"))) + ","; // 21
        		cadena += (rs.getString("NOM_FINCA").equals("")?" ":LimpiarCadena(rs.getString("NOM_FINCA"))) + ","; // 22
        		cadena += (rs.getString("REF_DIR").equals("")?" ":LimpiarCadena(rs.getString("REF_DIR")))+ ","; // 23
        		cadena += (rs.getString("ACC_FINCA").equals("")?" ":LimpiarCadena(rs.getString("ACC_FINCA"))) + ","; //24
        		cadena += (rs.getString("APART_POSTAL").equals("")?" ":LimpiarCadena(rs.getString("APART_POSTAL"))) + ","; // 25
        		cadena += (rs.getString("APE1_CLI").equals("")?" ":LimpiarCadena(rs.getString("APE1_CLI"))) + ","; // 26
        		cadena += (rs.getString("APE2_CLI").equals("")?" ":LimpiarCadena(rs.getString("APE2_CLI"))) + ","; // 27
        		cadena += (rs.getString("NOM_CLI").equals("")?" ":LimpiarCadena(rs.getString("NOM_CLI"))) + ","; // 28
        		cadena += (rs.getString("TFNO_CLI").equals("")?" ":LimpiarCadena(rs.getString("TFNO_CLI"))) + ","; //29
        		cadena += (rs.getString("TIP_CLI").equals("")?" ":LimpiarCadena(rs.getString("TIP_CLI"))) + ","; //30
        		cadena += (rs.getString("DIRECCION").equals("")?" ":LimpiarCadena(rs.getString("DIRECCION"))) + ","; //31
        		cadena += (rs.getString("TIP_CONEXION_SGD").equals("")?" ":LimpiarCadena(rs.getString("TIP_CONEXION_SGD"))) + ","; //32
        		cadena += (rs.getString("MATRICULA_SGD").equals("")?" ":LimpiarCadena(rs.getString("MATRICULA_SGD"))) + ","; //33
        		cadena += (rs.getString("MATRICULA_CT").equals("")?" ":LimpiarCadena(rs.getString("MATRICULA_CT"))) + ","; //34
        		cadena += (rs.getString("MATRICULA_CT").equals("")?" ":LimpiarCadena(rs.getString("MATRICULA_CT"))) + ","; //35
        		cadena += (rs.getString("DIRECCION_BDI").equals("")?" ":LimpiarCadena(rs.getString("DIRECCION_BDI")))+ ","; //36
        		cadena += (rs.getString("LOCALIDAD_BDI").equals("")?" ":LimpiarCadena(rs.getString("LOCALIDAD_BDI"))) ; //37
        		out.print(cadena+"\r\n");	
        	}
        	
        	rs.close();

        	sql = "SELECT QO_PRECIN.* FROM QO_PRECIN, QO_ORDENES WHERE QO_PRECIN.NUM_OS = QO_ORDENES.NUM_OS AND ESTADO_OPER=1 AND TECNICO=? ";
        	pst = conexion.getConnection().prepareStatement(sql);
        	pst.setString(1, eq.getRecurso());
        	rs = conexion.Query(pst);
        	
        	out.print("3PRECINTOS\r\n");
        	while(rs.next()) {
        		String cadena = "1" + rs.getString("NUM_OS") + ",";
        		cadena += rs.getString("NUM_APA") + ",";
        		cadena += rs.getString("CO_MARCA") + ",";
        		cadena += rs.getString("NUM_PRECIN");
        		out.print(cadena+"\r\n");	
        	}
        	
        	rs.close();
        	
        	sql = "SELECT QO_RECIBOS.* FROM QO_RECIBOS,QO_ORDENES WHERE QO_RECIBOS.NUM_OS = QO_ORDENES.NUM_OS AND ESTADO_OPER=1 AND TECNICO = ?";
        	pst = conexion.getConnection().prepareStatement(sql);
        	pst.setString(1, eq.getRecurso());
        	rs = conexion.Query(pst);
        	
        	out.print("3RECIBOS\r\n");
        	while(rs.next()) {
        		String cadena = "1" + rs.getString("NUM_OS") + ",";
        		cadena += rs.getString("NIS_RAD") + ",";
        		cadena += rs.getString("SEC_REC") + ",";
        		cadena += rs.getString("SEC_NIS")+ ",";
        		cadena += rs.getString("F_FACT")+ ",";
        		cadena += rs.getString("SIMBOLO_VAR")+ ",";
        		cadena += rs.getString("F_VCTO_FACT")+ ",";
        		cadena += rs.getString("IMP_TOT_REC")+ ",";
        		cadena += rs.getString("IMP_CTA");
        		out.print(cadena+"\r\n");	
        	}
        	
        	sql = "SELECT QO_PASOS.* FROM QO_PASOS, QO_ORDENES WHERE QO_PASOS.NUM_OS = QO_ORDENES.NUM_OS AND ESTADO_OPER=1 AND TECNICO=?";
        	pst = conexion.getConnection().prepareStatement(sql);
        	pst.setString(1, eq.getRecurso());
        	rs = conexion.Query(pst);
        	
        	out.print("3PASOS\r\n");
        	while(rs.next()) {
        		String cadena = "1" + rs.getString("NUM_OS") + ",";
        		cadena += rs.getString("NUM_PASO") + ",";
        		cadena += rs.getString("OPCOND") + ",";
        		cadena += rs.getString("DESCRIPCION")+ ",";
        		cadena += rs.getString("CONDICION")+ ",";
        		cadena += rs.getString("ELSEACCION")+ ",";
        		cadena += rs.getString("CUMPLIDO")+ ",";
        		cadena += rs.getString("CO_ACCEJE")+ ",";
        		cadena += rs.getString("IND_DECISOR");
        		
        		out.print(cadena+"\r\n");	
        	}
        	
        	
        	sql = "SELECT QO_APARATOS.* FROM QO_APARATOS, QO_ORDENES WHERE QO_APARATOS.NUM_OS = QO_ORDENES.NUM_OS AND ESTADO_OPER=1 AND TECNICO=?";
        	pst = conexion.getConnection().prepareStatement(sql);
        	pst.setString(1, eq.getRecurso());
        	rs = conexion.Query(pst);
        	
        	out.print("3APARATOS\r\n");
        	while(rs.next()) {
        		String cadena = "1" + rs.getString("NUM_OS") + ",";
        		cadena += rs.getString("NIS_RAD") + ",";
        		cadena += rs.getString("NUM_APA") + ",";
        		cadena += rs.getString("CO_MARCA")+ ",";
        		cadena += rs.getString("TIP_APA")+ ",";
        		cadena += rs.getString("EST_APA")+ ",";
        		cadena += rs.getString("AOL_APA")+ ",";
        		cadena += rs.getString("TIP_INTENSIDAD")+ ",";
        		cadena += rs.getString("TIP_FASE")+ ",";
        		cadena += rs.getString("TIP_TENSION")+ ",";
        		cadena += rs.getString("F_INST")+ ",";
        		cadena += rs.getString("F_FABRIC")+ ",";
        		cadena += rs.getString("CSMO_PROM");
        		
        		out.print(cadena+"\r\n");	
        	}
        	
        	
        	
        } catch (SQLException e) {
			// TODO Auto-generated catch block
			out.print("9"
					+ e.getMessage());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			out.print("9"
					+ e.getMessage());
		} finally {
			if (conexion != null) {
				try {
					conexion.Close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					out.print("9" + e.getMessage());
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
		cadena = cadena.replace("'", "");
		cadena = cadena.replace(",", "");
		cadena = cadena.replace("\"","");
		return cadena;
	}

}
