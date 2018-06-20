package com.are.sofatec;


import java.io.IOException;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class SrvGps
 */
@WebServlet("/SrvConsultaWS")
public class SrvConsultaWS extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SrvConsultaWS() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    protected void ProcesarPeticion(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		//response.setContentType("text/html;charset=UTF-8");
    	response.setContentType("text/html;charset=ISO-8859-1");
		PrintWriter out = response.getWriter();
		
		String metodo =(String)request.getParameter("metodo");
		String nic =(String)request.getParameter("nic");
		
		NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());
		
        long lnic = Long.parseLong(nic);
        try {
        	
        	if (metodo.equals("1")) {
        		WebServiceEca ws = new WebServiceEca();
        		String res = ws.ConsultaIdCobro(nic);
        		if (res != null) {
        			String[] resultado = res.split(";");
        			if (resultado.length == 3) {
        				String cadena = "Deuda Total: " + nf.format(Double.parseDouble(resultado[1]));
        				cadena += "\n Valor Ultima factura: " + nf.format(Double.parseDouble(resultado[2]));
        				out.print(cadena);
        			}else {
        				out.print(res);
        			}
        		
        		}else {
        			out.print("Error al consultar Web Services");
        		}
        	}
        	
        	if (metodo.equals("2")) {
        		WebServiceEca ws = new WebServiceEca();
        		String res =ws.EsClienteMoroso(nic);
        		if (res != null) {
        			if (res.equals("true")) {
        				out.print("Cliente Moroso");
        			}else if (res.equals("false")) {
        				out.print("Cliente al dia");
        			}else {
        				out.print(res);
        			}
        		}else {
        			out.print("Error al consultar Web Services");
        		}
        		
        	}

        	if (metodo.equals("3")) {
        		WebServiceEca ws = new WebServiceEca();
        		String res = ws.ValorFacturaActual(nic);
        		if (res != null) {
        			String[] resultado = res.split(";");
        			if (resultado.length == 3) {
        				String cadena = "Simbolo Variable: " + resultado[1];
        				cadena += "\n Valor Ultima factura: " + nf.format(Double.parseDouble(resultado[2]));
        				out.print(cadena);
        			}else {
        				out.print(res);
        			}
        		
        		}else {
        			out.print("Error al consultar Web Services");
        		}
        	}
        	
        	
        	
        }catch (Exception e) {
        	out.print("ERROR:  " + e.getMessage());
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
	
	 private static String consultaIdCobro(long nic) {
	        wstest.ServicioX0020WebX0020SistemaX0020Comercial service = new wstest.ServicioX0020WebX0020SistemaX0020Comercial();
	        wstest.ServicioX0020WebX0020SistemaX0020ComercialSoap port = service.getServicioX0020WebX0020SistemaX0020ComercialSoap();
	        return port.consultaIdCobro(nic);
	 }
	 
	 private static String esClienteMoroso(long nic) {
	        wstest.ServicioX0020WebX0020SistemaX0020Comercial service = new wstest.ServicioX0020WebX0020SistemaX0020Comercial();
	        wstest.ServicioX0020WebX0020SistemaX0020ComercialSoap port = service.getServicioX0020WebX0020SistemaX0020ComercialSoap();
	        return port.esClienteMoroso(nic);
	    }
	 
	 private static String valorFacturaActual(long nic) {
	        wstest.ServicioX0020WebX0020SistemaX0020Comercial service = new wstest.ServicioX0020WebX0020SistemaX0020Comercial();
	        wstest.ServicioX0020WebX0020SistemaX0020ComercialSoap port = service.getServicioX0020WebX0020SistemaX0020ComercialSoap();
	        return port.valorFacturaActual(nic);
	    }

}
