package com.are.sofatec;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.DataInputStream;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class SrvGetFile
 */
@WebServlet("/SrvGetFile")
public class SrvGetFile extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SrvGetFile() {
        super();
        // TODO Auto-generated constructor stub
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            //InputStream in = request.getInputStream();
        	DataInputStream in = new DataInputStream(request.getInputStream());
            
        	String orden = in.readUTF();
        	String filename = in.readUTF();
        	String imei = in.readUTF();
        	
        	db conexion = new db();
        	
        	Ordenes os = new Ordenes(conexion);
        	if (!os.Find(orden)) {
        		out.print("Orden de servicio no se encuentra registrada");
        		conexion.Close();
        		return;
        	}
        	
        	departamento dpto = new departamento(conexion);
        	if (!dpto.FindByLoca(os.getMunicipio())) {
        		out.print("Departamento asignado a la orden no encontrado");
        		conexion.Close();
        		return;
        	}
        	
        	
            BufferedReader r = new BufferedReader(new InputStreamReader(in));
    		StringBuffer buf = new StringBuffer();
    		String line;
    		while ((line = r.readLine()) != null) {
    			buf.append(line);
    		}

            ServletContext servletContext = getServletContext();
            String nomFile =  dpto.getCodigo() + "_" + orden.trim().replace(" ", "")+ "_" + Utilidades.AhoraToString() + "_" + filename;
            String Filename = servletContext.getRealPath("/imagenes") + File.separator + nomFile;
            

    		String s = buf.toString();
    		
            
            FileOutputStream fos = new FileOutputStream(Filename);
            byte[] bytes = Base64.decode(s);
            fos.write(bytes);
            
            fos.close();

            
            
            
            Equipos equipo = new Equipos(conexion);
            String recurso = "-1";
            if (equipo.Find(imei)) {
            	recurso = equipo.getRecurso();
            }
            
            imagenes img = new imagenes();
            img.setOrden(orden);
            img.setFilename(nomFile);
            img.setRecurso(recurso);
            
            GestionImagenes GI = new GestionImagenes(img,conexion);
            if (GI.add()) {
            	out.print("Foto Guardada en el Servidor");
            }else {
            	File fichero = new File(Filename);
            	fichero.delete();
            }
            
            conexion.Close();
           
       
        }catch (IOException e) {
          out.print(e.getMessage());
         }catch (Exception e) {
            out.print(e.getMessage());
        } finally {
            out.close();
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

}
