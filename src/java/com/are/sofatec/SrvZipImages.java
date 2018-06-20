package com.are.sofatec;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class SrvZipImages
 */
@WebServlet("/SrvZipImages")
public class SrvZipImages extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final int BUFFER_SIZE = 1024;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SrvZipImages() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    protected void ProcesarPeticion(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

    	
    	byte [] buffer = new byte[BUFFER_SIZE];
    	
    	db conexion = null;
    	String departamento = (String)request.getParameter("departamento");
    	String tipo = (String)request.getParameter("tipo");
    	String fecha_inicial = (String)request.getParameter("fecha_inicial");
    	String hora1 = (String)request.getParameter("hora1");
    	
    	String fecha_final = (String)request.getParameter("fecha_final");
    	String hora2 = (String)request.getParameter("hora2");
    	
    	fecha_inicial += " " + hora1;
    	fecha_final += " " + hora2;
    	
    	HttpSession sesion=request.getSession();
    	String usuario = (String)sesion.getAttribute("usuario");
    			
    	
    	try {
    		conexion = new db();
    		
    		String sql = "select imagenes.filename " +
    	    		" from reportes,orders,tipo_orden,localidad,imagenes " +
    	    		" where orders.orden = reportes.orden " +
    	    		" and orders.tipo = tiorcodi " +
    	    		" and orders.orden = imagenes.orden " +
    	    		" and orders.localidad = locacodi " +
    	    		" and reportes.fecha between   ? and ? " ;
    	    		
    	    		if (!departamento.equals("all")) {
    	    			sql += " and locadepa = '" + departamento + "' ";
    	    			
    	    		}
    	    		if (!tipo.equals("all") ){
    	    			sql += " and orders.tipo = '" + tipo + "' ";
    	    			
    	    		}
    	    	
    	    	
    	    		sql += " order by reportes.fecha";
    	    		
    	    java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
    		pst.setString(1, fecha_inicial);
			pst.setString(2, fecha_final);
			
			java.sql.ResultSet rs = conexion.Query(pst);
			
			ServletContext servletContext = getServletContext();
            String path = servletContext.getRealPath("/imagenes");
			String FilenameZip = path + File.separator + usuario + "_" + Utilidades.AhoraToString() + ".zip";
			
	    	ZipOutputStream os = new ZipOutputStream(new FileOutputStream(FilenameZip));
	    	
	    	
	    	
	    	while (rs.next()) {
	    		
	    		
	    		String filename = path + File.separator + rs.getString("filename");
	    		ZipEntry entrada = new ZipEntry(rs.getString("filename"));
	    		os.putNextEntry(entrada);
	    		
	    		FileInputStream fis = new FileInputStream(filename);
	    		
	    		int leido=0;
	    		while (0 < (leido=fis.read(buffer))){
	    		   os.write(buffer,0,leido);
	    		}
	    		fis.close();
	    		os.closeEntry();
	    	}
			os.close();
			//out.println("Proceso Finalizado");
			
			response.setContentType("application/octet-strem");
	        response.setHeader("Content-Disposition", "attachment;filename='fichero.zip'"); //preparando el 'download' al navegador
	        
	        FileInputStream in = new FileInputStream(FilenameZip);
	        
	        
	        OutputStream out = new DataOutputStream(response.getOutputStream());
	        int sizeRead = 0;
	        while ((sizeRead = in.read(buffer)) >= 0) { //leyendo del host
	            out.write(buffer, 0, sizeRead); //escribiendo para el navegador
	        }
	        in.close(); // y cerrando
	        out.close(); // todo
			
	        File fichero = new File(FilenameZip);
	        fichero.delete();
	        
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			
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
