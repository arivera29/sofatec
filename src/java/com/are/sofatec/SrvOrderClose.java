package com.are.sofatec;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.are.manejadores.ManejadorTipoOrden;

/**
 * Servlet implementation class SrvOrderClose
 */
@WebServlet("/SrvOrderClose")
public class SrvOrderClose extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SrvOrderClose() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    protected void ProcesarPeticion(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
    	
    	response.setContentType("text/html;charset=ISO-8859-1");
        PrintWriter out = response.getWriter();
        
        db conexion = null;
        String orden = (String)request.getParameter("orden");
        String imei = (String)request.getParameter("imei");
        String accion = (String)request.getParameter("accion");
        String actividad = (String)request.getParameter("actividad");
        String observacion = (String)request.getParameter("observacion");
        String lectura = (String)request.getParameter("lectura");
        String tiposello = (String)request.getParameter("sello");
        String cntsello = (String)request.getParameter("cantidad_sellos");
        String acometida = (String)request.getParameter("acometida");
        String metraje = (String)request.getParameter("metraje");
        
        if (cntsello.equals("")){
        	cntsello = "0";
        }
        if (metraje.equals("")) {
        	metraje="0";
        }
        
        try {
        	
        		conexion = new db();
        		
        		Equipos eq = new Equipos(conexion);
        		if (!eq.Find(imei)) {
        			throw  new Exception("IMEI no se encuentra registrado");
        		}
        		
        		if (eq.getActivo() != 1) {
        			throw  new Exception("IMEI no se encuentra activo");
        		}
        		
        		
        		Actividades act = new Actividades(conexion);
        		if (!act.Find(actividad)) {
        			throw  new Exception("El codigo de la actividad ingresado no existe, verifique por favor");
        		}else {
        			if (act.getActivo() == 0) {
        				throw  new Exception("El codigo de la actividad ingresado no está activo, verifique por favor");
        			}
        		}
        		accion = act.getAccion(); // Accion parametrizada en la actividad seleccionada.
        		
        		OrderClose oc = new OrderClose(conexion);
        		
        		if (oc.Find(orden)) {
        		
        			ManejadorTipoOrden to = new ManejadorTipoOrden(conexion);
        			
        			//if (!to.FindActividad(oc.getTipo(), actividad)) {
            		//	throw  new Exception("Actividad ingresada no corresponde al tipo de orden. Actividad " + actividad);
            		//}
        			if (eq.getFoto() == 1) {
	        			if (oc.CountImages(orden) == 0) {
	        				throw  new Exception("No hay imagenes registradas para la orden " + orden);
	        			}
        			}
        			
        			if (!oc.getEstado().equals("99") ) {
        				oc.setOrden(orden);
                		oc.setImei(imei);
                		oc.setAccion(accion);
                		oc.setActividad(actividad);
                		oc.setObservacion(observacion);
                		oc.setLectura(lectura);
                		oc.setTiposello(tiposello);
                		oc.setCntsello(Integer.parseInt(cntsello));
                		oc.setTipoacometida(acometida);
                		oc.setCntacometida(Double.parseDouble(metraje));
                		if (oc.CloseOrder()) {
                			out.print("OK");
                			
                		}else {
                			throw  new Exception("Error al finalizar la Orden de servicio, favor contactar al Administrador");
                		}
        				
        			}else {
        				throw  new Exception("Orden se encuentra en estado FINALIZADA");
        			}
        		
        		
        		
        		}else {
        			throw  new Exception("Orden no se encuentra registrada " + orden);
        			
        		}
        		
        		
        	
        } catch (SQLException e) {
			// TODO Auto-generated catch block
			out.print("Error de conexion con el servidor: "
					+ e.getMessage());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			out.print("Error:  "
					+ e.getMessage());
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

}
