package com.are.sofatec;


import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class SrvGps
 */
@WebServlet("/SrvSolicitarNuevoLote")
public class SrvSolicitarNuevoLote extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SrvSolicitarNuevoLote() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    protected void ProcesarPeticion(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		//response.setContentType("text/html;charset=UTF-8");
    	request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		
		if (request.getParameterValues("ordenes") == null) {
			out.print("No se recibe ordenes");
			return;
		}
		
	
		String[] ordenes = request.getParameterValues("ordenes");
		

        String cadena = "";
        if (ordenes.length > 0 ) {
        	for (int x=0; x < ordenes.length; x++) {
        		cadena += ordenes[x];
        		if ((x+1) < ordenes.length) {
        			cadena += ";";
        		}
        		
        	}
        }else {
        	out.print("No se recibe ordenes");
        	return;
        }
        
        //out.print(cadena);
 
		db conexion =  null;
		
		try {
			
			cadena = ordenes.length + ";" + cadena;
			WebServiceEca ws = new WebServiceEca(); 
			String result = ws.AsociarOrdenesServicio(cadena);
			long lote = -1;
			if (result != null) {
				try {
					lote = Long.parseLong(result);
				}catch (Exception e) {
					
				}
			}
			
			if (lote != -1) {
				conexion = new db();
				int cont = 0;
				for (int x=0; x < ordenes.length; x++) {
					String sql = "UPDATE QO_ORDENES SET NUM_LOTE=? WHERE NUM_OS=?";
					java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
					pst.setString(1, Long.toString(lote));
					pst.setString(2, ordenes[x]);
					if (conexion.Update(pst) > 0) {
						cont++;
					}
	        	}
				
				if (cont == ordenes.length) {
					conexion.Commit();
					out.print("OK, LOTE GENERADO: " + lote);
				}else {
					out.print("ERROR AL ACTUALIZAR ORDENES, LOTE GENERADO: " + lote);
				}
				
			}else {
				out.print(result);
			}
			
			
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			out.print("Error de conexion con el servidor: "
					+ e.getMessage());
		} catch (NumberFormatException e) {
			out.println("Error.  Formato de numero no válido. " + e.getMessage());
			
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
	
	private static String asociarOrdenesServicio(java.lang.String sarta) {
        wstest.ServicioX0020WebX0020SistemaX0020Comercial service = new wstest.ServicioX0020WebX0020SistemaX0020Comercial();
        wstest.ServicioX0020WebX0020SistemaX0020ComercialSoap port = service.getServicioX0020WebX0020SistemaX0020ComercialSoap();
        return port.asociarOrdenesServicio(sarta);
    }

}
