package com.are.sofatec;

import java.io.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.sql.*;

import org.apache.commons.fileupload.*;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import java.util.*;

@WebServlet("/UploadDocument")
@MultipartConfig
public class UploadDocument extends HttpServlet {
	private String orden="";  // Orden de servicio
	private String imei="";  // IMEI del equipo
	private String filename = "";  // nombre del archivo guardado en el Servidor
	private String usuario = "nologin";
    /**
	 * 
	 */
	private static final long serialVersionUID = 4242892843995231109L;

	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        try {
			if (procesaFicheros(request,out)) {
				try {
					if (SaveRecord()) {
						out.print("Archivo cargado correctamente en el Servidor");
						
					}else {
						out.print("No se registro el documento en la base de datos");
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					out.println("Error al guardar en la base de datos. " + e.getMessage());
				}
			}else {
				out.println("Error al Cargar el Archivo");
			}
		} catch (FileUploadException e) {
			// TODO Auto-generated catch block
			out.println("Error al Cargar el Archivo. " + e.getMessage());
		}catch (Exception e) {
			// TODO Auto-generated catch block
			out.println("Error al Cargar el Archivo. " + e.getMessage());
		}
        out.close();
    }


    void depura(String cadena)
    {
        System.out.println("El error es " + cadena);
    }

    public boolean procesaFicheros(HttpServletRequest req, PrintWriter out ) throws Exception {
    	FileItemFactory factory = new DiskFileItemFactory();
    	ServletFileUpload upload = new ServletFileUpload(factory);


    	// req es la HttpServletRequest que recibimos del formulario.
    	// Los items obtenidos serán cada uno de los campos del formulario,
    	// tanto campos normales como ficheros subidos.
    	
			
			@SuppressWarnings("unchecked")
			List<FileItem> items = (List<FileItem>)upload.parseRequest(req);

    	// Se recorren todos los items, que son de tipo FileItem
    	for (Object item : items) {
    	   FileItem uploaded = (FileItem) item;

    	   // Hay que comprobar si es un campo de formulario. Si no lo es, se guarda el fichero
    	   // subido donde nos interese
    	   if (!uploaded.isFormField()) {
    		  filename = Utilidades.AhoraToString() + "_" + uploaded.getName();
    	      File fichero = new File(this.getServletContext().getRealPath(
						"/imagenes"), filename);

				uploaded.write(fichero);
			
    	   } else {
    	      // es un campo de formulario, podemos obtener clave y valor
    	      String key = uploaded.getFieldName();
    	      if (key.equals("orden")) {
    	    	  orden = uploaded.getString();
    	      }
    	      if (key.equals("imei")) {
    	    	  imei = uploaded.getString();
    	      }
    	      if (key.equals("filename")) {
    	    	  filename = uploaded.getString();
    	      }
    	      
    	   }
    	}

        return true;
    }
    
    public boolean SaveRecord() throws SQLException {
    	boolean ret = false;
    	db conexion = new db();
    	Equipos equipo = new Equipos(conexion);
        String recurso = "-1";
        if (equipo.Find(imei)) {
        	recurso = equipo.getRecurso();
        }
        
        imagenes img = new imagenes();
        img.setOrden(orden);
        img.setFilename(filename);
        img.setRecurso(recurso);
        
        GestionImagenes GI = new GestionImagenes(img,conexion);
        if (GI.add()) {
        	ret = true;
        }
    	
    	conexion.Close();
    	return ret;
    }

    /** Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }
}
