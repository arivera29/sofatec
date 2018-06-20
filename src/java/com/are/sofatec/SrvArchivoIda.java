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
 * Servlet implementation class SrvGetFileOrders
 */
@WebServlet("/SrvArchivoIda")
public class SrvArchivoIda extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private static String SEPARADOR = "|";
    private static String FIN_LINEA = "\r\n";
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SrvArchivoIda() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    protected void ProcesarPeticion(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
    	response.setContentType("text/plain;charset=ISO-8859-1");
    	response.setHeader("Content-Disposition", "attachment; filename=\"ARCHIVO_IDA.TXT\"");
    	
		PrintWriter out = response.getWriter();
		String fecha_inicial = (String)request.getParameter("fecha1");
		String fecha_final = (String)request.getParameter("fecha2");
		String departamento = (String)request.getParameter("departamento");
		String municipio = (String)request.getParameter("localidad");

		
		db conexion;
		try {
			conexion = new db();
					
		String sql = "select orders.*,localidad.locadesc,departamentos.depadesc " +
				"from orders,localidad,departamentos " +
				"where date(fecha_cierre) between ? and ? " +
				" and localidad=locacodi " +
				" and locadepa = depacodi ";
			
		
				if (!departamento.equals("-1")) {
					sql += " and locadepa = '" + departamento + "'" ;
				}
				
				if (!municipio.equals("-1")) {
					sql += " and localidad = '" + municipio + "'" ;
				}
				
				
		
		java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		pst.setString(1, fecha_inicial);
		pst.setString(2, fecha_final);
		ResultSet rs = conexion.Query(pst);
		String header = "num_lote|est_lote|nro_tpo|nic|nis|tip_serv|cod_tar|num_orden|tip_os|est_os|f_estm_res|f_uce|f_puest_trat|ruta|num_itin|aol|nom_depto|nom_munic|Cab.Mun-Correg|Urb/Barrio|nom_via|nom_calle|crucero|placa|cgv|ref_dir|" + FIN_LINEA;
		out.println(header);
		while(rs.next()) {
			String record = "";
			record += "201212015901" + SEPARADOR; 	//1
			record += "EL003" + SEPARADOR;	//2
			record += "112001" + SEPARADOR;	//3
			record += rs.getString("nic") + SEPARADOR;	//4
			record += rs.getString("nis") + SEPARADOR;	//5
			record += "1111111" + SEPARADOR;		//6
			record += rs.getString("cod_tar") + SEPARADOR;		//7
			record += rs.getString("orden") + SEPARADOR;		//8
			record += rs.getString("tipo") + SEPARADOR;		//9
			record += "EO004" + SEPARADOR;		//10
			record += rs.getString("f_pues_trat") + SEPARADOR;			//11
			record += rs.getString("f_pues_trat") + SEPARADOR;
			record += rs.getString("f_pues_trat") + SEPARADOR;
			record += rs.getString("ruta") + SEPARADOR;		//14
			record += rs.getString("num_itin") + SEPARADOR;		//15
			record += rs.getString("aol") + SEPARADOR;
			record += rs.getString("depadesc") + SEPARADOR;			//19
			record += rs.getString("locadesc") + SEPARADOR;		//20
			record += rs.getString("nom_local") + SEPARADOR;		//20
			record += rs.getString("nom_local") + SEPARADOR;		//20
			record += rs.getString("nom_via") + SEPARADOR;		//8
			record += rs.getString("nom_calle") + SEPARADOR;		//9
			record += rs.getString("crucero") + SEPARADOR;		//10
			record += rs.getString("placa") + SEPARADOR;			//11
			record += this.Espacios(rs.getString("interior")) + SEPARADOR;
			record += this.Espacios(rs.getString("ref_dir")) + SEPARADOR + FIN_LINEA;
			
			out.print(record);
		}
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			out.println(e.getMessage());
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
		cadena = cadena.replace("|","");
		return cadena;
	}
	
	public String Espacios(String cadena) {
		if (cadena.equals("")) {
			return "AA";
		}else {
			return cadena;
		}
	}

}
