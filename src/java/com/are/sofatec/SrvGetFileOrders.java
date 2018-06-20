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

import com.are.manejadores.ManejadorTipoOrden;

/**
 * Servlet implementation class SrvGetFileOrders
 */
@WebServlet("/SrvGetFileOrders")
public class SrvGetFileOrders extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private static String SEPARADOR = "|";
    private static String FIN_LINEA = "\r\n";
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SrvGetFileOrders() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    protected void ProcesarPeticion(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
    	response.setContentType("text/plain;charset=ISO-8859-1");
    	response.setHeader("Content-Disposition", "attachment; filename=\"cierres.txt\"");
    	
		PrintWriter out = response.getWriter();
		String fecha_inicial = (String)request.getParameter("fecha1");
		String fecha_final = (String)request.getParameter("fecha2");
		String hora1 = (String)request.getParameter("hora1");
		String hora2 = (String)request.getParameter("hora2");
		String departamento = (String)request.getParameter("departamento");
		String municipio = (String)request.getParameter("localidad");
		String tipo = (String)request.getParameter("tipo");
		
		fecha_inicial += " " + hora1;
		fecha_final += " " + hora2;
		
		db conexion;
		try {
			conexion = new db();
			ManejadorConfig MC = new ManejadorConfig(conexion);
			MC.Find();
			Config cfg = MC.getConfig();
			ManejadorTipoOrden manejador = new ManejadorTipoOrden(conexion);
		
		String sql = "select orders.*,date_format(fecha_cierre,'%Y%m%d') f_actual, date_format(fecha_cierre,'%H.%i') hora,localidad.locadesc,actividades.actitiac " +
				"from orders,localidad,actividades " +
				"where fecha_cierre between ? and ? " +
				"and localidad=locacodi " +
				"and actividad = acticodi ";
				
		
				if (!departamento.equals("")) {
					sql += " and locadepa = '" + departamento + "'" ;
				}
				
				if (!municipio.equals("-1")) {
					sql += " and localidad = '" + municipio + "'" ;
				}
				
				if (!tipo.equals("all")) {
					sql += " and tipo = '" + tipo + "'" ;
				}
				
		
				sql += " order by fecha_cierre";
		
		java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		pst.setString(1, fecha_inicial);
		pst.setString(2, fecha_final);
		ResultSet rs = conexion.Query(pst);
		String header = "num_os" + SEPARADOR + 
				"tip_os" + SEPARADOR + 
				"nic" + SEPARADOR + 
				"nis_rad" + SEPARADOR + 
				"f_pues_trat" + SEPARADOR + 
				"nom_munic" + SEPARADOR + 
				"nom_local" + SEPARADOR +
				"nom_via" + SEPARADOR + 
				"nom_calle" + SEPARADOR + 
				"crucero" + SEPARADOR + 
				"placa" + SEPARADOR +
				"interior(cgv)" + SEPARADOR + 
				"ref_dir" + SEPARADOR + 
				"cod_cli" + SEPARADOR + 
				"sec_cta" +	SEPARADOR + 
				"nom_cli" + SEPARADOR + 
				"ape1_cli" + SEPARADOR + 
				"ape2_cli" + SEPARADOR +
				"deuda" + SEPARADOR + 
				"cant_fact" + SEPARADOR + 
				"cod_tar" + SEPARADOR + 
				"num_apa" + SEPARADOR +
				"co_marca" + SEPARADOR + 
				"desc_co_marca" + SEPARADOR + 
				"tip_apa" + SEPARADOR + 
				"desc_tip_apa" + SEPARADOR + 
				"num_rue" + SEPARADOR + 
				"aol_apa" + SEPARADOR +
				"amperios" + SEPARADOR + 
				"f_inst" + 	SEPARADOR + 
				"f_fabric" + SEPARADOR + 
				"c_cte" + SEPARADOR + 
				"desc_tip_tension_apa" + SEPARADOR +
				"tip_csmo" + SEPARADOR + 
				"desc_tip_csmo" + SEPARADOR + 
				"ruta" + SEPARADOR + 
				"num_itin" + SEPARADOR + 
				"aol" + SEPARADOR +
				"LECTURA" + SEPARADOR + 
				"USUARIO" + SEPARADOR + 
				"F_ACTUAL" + SEPARADOR + 
				"FECHA_VISITA" + SEPARADOR + 
				"HORA_INI_VISITA" + SEPARADOR + 
				"HORA_FIN_VISITA" + SEPARADOR + 
				"COD_EMPLEADO" + SEPARADOR + 
				"OBSERVACIONES" + SEPARADOR + 
				"RESOLVER/ANULAR" + SEPARADOR + 
				"EFECTO/MOTIVO" + SEPARADOR +
				"ELEMENTO_1" + SEPARADOR + 
				"CANTIDAD_1" + SEPARADOR + 
				"PAGO_1" + SEPARADOR + 
				"ELEMENTO_2" + SEPARADOR +
				"CANTIDAD_2" + SEPARADOR + 
				"PAGO_2" + SEPARADOR + 
				"ELEMENTO_3" + SEPARADOR + 
				"CANTIDAD_3" + 	SEPARADOR + 
				"PAGO_3" + SEPARADOR + 
				"ELEMENTO_4" + SEPARADOR + 
				"CANTIDAD_4" +SEPARADOR + 
				"PAGO_4" + SEPARADOR + 
				"ELEMENTO_5" + SEPARADOR + 
				"CANTIDAD_5" + SEPARADOR + 
				"PAGO_5" + SEPARADOR + 
				"COB_CLIENTE" + SEPARADOR + 
				"FINANCIACION" + SEPARADOR + 
				"COBRO_MANO_OBRA" + SEPARADOR + 
				"PRECIO_NO_TABULADO" + SEPARADOR;
		out.println(header);
		while(rs.next()) {
			String record = "";
			record += rs.getString("orden") + SEPARADOR; 	//1
			record += rs.getString("tipo") + SEPARADOR;	//2
			record += rs.getString("nic") + SEPARADOR;	//3
			record += rs.getString("nis") + SEPARADOR;	//4
			record += rs.getString("f_pues_trat") + SEPARADOR;	//5
			record += rs.getString("locadesc") + SEPARADOR;		//6
			record += rs.getString("nom_local") + SEPARADOR;		//7
			record += rs.getString("nom_via") + SEPARADOR;		//8
			record += rs.getString("nom_calle") + SEPARADOR;		//9
			record += rs.getString("crucero") + SEPARADOR;		//10
			record += rs.getString("placa") + SEPARADOR;			//11
			record += this.Espacios(rs.getString("interior")) + SEPARADOR;
			record += this.Espacios(rs.getString("ref_dir")) + SEPARADOR;
			record += rs.getString("cod_cli") + SEPARADOR;		//14
			record += rs.getString("sec_cta") + SEPARADOR;		//15
			record += this.Espacios(rs.getString("nom_cli")) + SEPARADOR;
			record += this.Espacios(rs.getString("ape1_cli")) + SEPARADOR;
			record += this.Espacios(rs.getString("ape2_cli")) + SEPARADOR;
			record += rs.getString("deuda") + SEPARADOR;			//19
			record += rs.getString("cant_fact") + SEPARADOR;		//20
			record += rs.getString("cod_tar") + SEPARADOR;		//21
			record += rs.getString("num_apa") + SEPARADOR;		//22
			record += rs.getString("co_marca") + SEPARADOR;		//23
			record += rs.getString("desc_co_marca") + SEPARADOR;	//24
			
			record += rs.getString("tip_apa") + SEPARADOR;		//25
			record += rs.getString("desc_tip_apa") + SEPARADOR;	//26
			record += rs.getString("num_rue") + SEPARADOR;		//27
			record += rs.getString("aol_apa") + SEPARADOR;		//28
			record += rs.getString("amperios") + SEPARADOR;		//29
			record += rs.getString("f_inst") + SEPARADOR;			//30
				
			record += rs.getString("f_fabric") + SEPARADOR;		//31
			record += rs.getString("c_cte") + SEPARADOR;			//32
			record += rs.getString("desc_tip_tension_apa") + SEPARADOR;	//33
			record += rs.getString("tip_csmo") + SEPARADOR;		//34
			record += rs.getString("desc_tip_csmo") + SEPARADOR;	//35
			record += rs.getString("ruta") + SEPARADOR;			//36
			record += rs.getString("num_itin") + SEPARADOR;		//37
			record += rs.getString("aol") + SEPARADOR;			//38
			
			// Campos para el cierre
			
			record += rs.getString("lectura") + SEPARADOR; //LECTURA
			record += cfg.getUser() + SEPARADOR;//USUARIO
			record += rs.getString("f_actual") + SEPARADOR;//F_ACTUAL
			record += rs.getString("f_actual") + SEPARADOR;//FECHA_VISITA 
			record += rs.getString("hora") + SEPARADOR;//HORA_INI_VISITA
			record += rs.getString("hora") + SEPARADOR;//HORA_FIN_VISITA
			record += cfg.getNumEmpleado() + SEPARADOR;//COD_EMPLEADO
			record += LimpiarCadena(rs.getString("observacion")) + SEPARADOR; //OBSERVACIONES
			//String accion = to.getAccionActividad(rs.getString("tipo"), rs.getString("actividad"));
			String accion = "";
			record += accion + SEPARADOR;//RESOLVER/ANULAR
			record += rs.getString("actividad") + SEPARADOR;
			
			if (rs.getInt("cantidad_sello") > 0) {
				if (rs.getString("tipo_sello").equals("1")) {
					record += cfg.getCodigoSello1() + SEPARADOR;//ELEMENTO_1 Sello suspension
				}else {
					record += cfg.getCodigoSello2() + SEPARADOR;//ELEMENTO_1  Sello doble ancla
				}
				record += rs.getInt("cantidad_sello") + SEPARADOR;//CANTIDAD_1
				record += "N" + "|"; //PAGO_1
			}else { // no se registraron sellos
				record += "<COD_EC1>" + SEPARADOR;//ELEMENTO_1
				record += "<CANT_EC1>" + SEPARADOR;//CANTIDAD_1
				record += "<PAGO_EC1>" + SEPARADOR;//PAGO_1
			}
			if (rs.getDouble("cantidad_acometida") > 0) {
				
				if (rs.getString("tipo_sello").equals("1")) {
					record += cfg.getCodigoAcometida1() + SEPARADOR;//ELEMENTO_2  Acometida 2x6
				}else {
					record += cfg.getCodigoAcometida2() + SEPARADOR;//ELEMENTO_2	Acometida 2x6+8
				}
				record += rs.getDouble("cantidad_acometida") + SEPARADOR;//CANTIDAD_2
				record += "<PAGO_EC2>" + SEPARADOR;//PAGO_2
			}else { // no se registró acometida
				record += "<COD_EC2>" + SEPARADOR;//ELEMENTO_2
				record += "<CANT_EC2>" + SEPARADOR;//CANTIDAD_2
				record += "<PAGO_EC2>" + SEPARADOR;//PAGO_2
			}
			
			record += "<COD_EC3>" + SEPARADOR;//ELEMENTO_3
			record += "<CANT_EC3>" + SEPARADOR;//CANTIDAD_3
			record += "<PAGO_EC3>" + SEPARADOR;//PAGO_3
			record += "<COD_EC4>" + SEPARADOR;//ELEMENTO_4
			record += "<CANT_EC4>" + SEPARADOR;//CANTIDAD_4
			record += "<PAGO_EC4>" + SEPARADOR;//PAGO_4
			record += "<COD_EC5>" + SEPARADOR;//ELEMENTO_5
			record += "<CANT_EC5>" + SEPARADOR;//CANTIDAD_5
			record += "<PAGO_EC5>" + SEPARADOR;//PAGO_5
			if (accion.equals("R")) { // Resolver
				record += "1" + SEPARADOR; //COB_CLIENTE
			} else { // Anular
				record += "2" + SEPARADOR; //COB_CLIENTE
			}
			record += "<NUM_CUOTAS>" + SEPARADOR;//FINANCIACION
			if (accion.equals("R")) {
				if (rs.getString("tipo").equals("TO502") || rs.getString("tipo").equals("TO503") || rs.getString("tipo").equals("TO600") ) { 
					record += "S" + SEPARADOR;//COBRO_MANO_OBRA
				}else {
					record += "N" + SEPARADOR;//COBRO_MANO_OBRA
				}
			}else {
				record += "N" + SEPARADOR;//COBRO_MANO_OBRA
			}
			record += "<PRECIO_NO_TAB>" + SEPARADOR + FIN_LINEA;;//PRECIO_NO_TABULADO

			
			
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
