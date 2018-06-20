package com.are.sofatec;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class XmlOrders
 */
@WebServlet("/SrvVisita")
public class SrvVisita extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SrvVisita() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    protected void ProcesarPeticion(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
    	
    	response.setContentType("text/html;charset=ISO-8859-1");
        PrintWriter out = response.getWriter();
        db conexion = null;
        
        HttpSession sesion = request.getSession();
        	
        	
        	String operacion = (String)request.getParameter("operacion");
        	
        	try {
        		if (operacion.equals("add")) {  // Agregar Visita
        		
        		
        			conexion = new db();
        			
        			String usuario = (String)request.getParameter("usuario");
        			String causal = (String)request.getParameter("causal");
        			String observacion = (String)request.getParameter("observacion");
        			String orden = (String)request.getParameter("orden");
        			String imei = (String)request.getParameter("imei");
        			
        			Causales c = new Causales(conexion);
        			if (!c.isActiva(causal)) {
        				throw new Exception("Causal no encontrada o no se encuentra activa, verifique porfavor");
        			}
        			
        			Ordenes oc = new Ordenes(conexion);
        			if (!oc.Find(orden)) {
        				throw new Exception("Orden de servicio no encontrada");
        			}
        			
        			if (oc.CountImages(orden) == 0) {
        				throw  new Exception("No hay imagenes registradas para la orden " + orden);
        			}
        			
        			Equipos equipo = new Equipos(conexion);
        			if (equipo.Find(imei)) {
        				String recurso = equipo.getRecurso();
        				if (!recurso.equals("-1")) {
        					
        					visita v = new visita();
        					v.setCausal(causal);
        					v.setOrden(orden);
        					v.setRecurso(recurso);
        					v.setUsuario(usuario);
        					v.setObservacion(observacion);
        					v.setImei(imei);
        					
        					VisitaFallida VF = new VisitaFallida(conexion,v);
        					if (VF.Add()) {
        						out.print("OK");
        					}else {
        						throw new Exception("Error al procesar la solicitud");
        					}
        					
        					
        				}else {
        					throw new Exception("Equipo no asignado a ningún recurso");
        				}
        				
        			}else {
        				throw new Exception("Equipo no registrado en la BD");
        			}
        		}
        		
        		if (operacion.equals("add_web")) {  // Agregar Visita
            		
        			if (sesion.getAttribute("usuario") == null) {
        				out.print("La sesion ha caducado... intente de nuevo por favor");
        				return;
        			}
        			
        			conexion = new db();
        			
        			String usuario = (String)sesion.getAttribute("usuario");
        			String causal = (String)request.getParameter("causal");
        			String observacion = (String)request.getParameter("observacion");
        			String orden = (String)request.getParameter("orden");
        			String recurso = (String)request.getParameter("cedula");
        			String fecha = (String)request.getParameter("fecha");
        			
        			
      			
        			       					
        					visita v = new visita();
        					v.setCausal(causal);
        					v.setOrden(orden);
        					v.setRecurso(recurso);
        					v.setUsuario(usuario);
        					v.setObservacion(observacion);
        					v.setImei("");
        					v.setFecha(fecha);
        					
        					VisitaFallida VF = new VisitaFallida(conexion,v);
        					if (VF.Add(true)) {
        						out.print("OK");
        					}else {
        						throw new Exception("Error al procesar la solicitud");
        					}
        					
        					
        				
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
