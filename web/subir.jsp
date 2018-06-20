<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ include file="validausuario.jsp"%>
<%@ page import="java.util.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="org.apache.commons.fileupload.*" %>
<%@ page import="org.apache.commons.fileupload.disk.*" %>
<%@ page import="org.apache.commons.fileupload.servlet.*" %>
<%@ page import="org.apache.commons.io.*" %>
<%@ page import="java.io.*" %>

<%
	   	String usuario = (String)session.getAttribute("usuario");

		/*FileItemFactory es una interfaz para crear FileItem*/
        FileItemFactory file_factory = new DiskFileItemFactory();
		String cadena = "";
		/*ServletFileUpload esta clase convierte los input file a FileItem*/
        ServletFileUpload servlet_up = new ServletFileUpload(file_factory);
		/*sacando los FileItem del ServletFileUpload en una lista */
		try {
		List items = servlet_up.parseRequest(request);
		
		Iterator iter = items.iterator();
		
		while (iter.hasNext()) {
			/*FileItem representa un archivo en memoria que puede ser pasado al disco duro*/
            FileItem item = (FileItem) iter.next();
			/*item.isFormField() false=input file; true=text field*/
            if (! item.isFormField()){
				/*cual sera la ruta al archivo en el servidor*/
				ServletContext servletContext = getServletContext();
				String n[] =item.getName().replace("\\","-").split("-");
				String nombreReal=n[n.length-1]; //nombre real del archivo para guardar
				nombreReal = usuario + "_" + Utilidades.AhoraToString() + "_" + nombreReal;
				String filename = servletContext.getRealPath("/upload") + File.separator + nombreReal;
				
				File archivo_server = new File(filename);
				/*y lo escribimos en el servido*/
				item.write(archivo_server);
				
				db conexion = new db();
				Archivo archivo = new Archivo();
				archivo.setUsuario(usuario);
				archivo.setFilename(nombreReal);
				archivo.setRuta(filename);
				
				ManejadorArchivo ma = new ManejadorArchivo(archivo,conexion);
				ma.add();
				
				conexion.Close();
				
				cadena = "Nombre Local--> " + item.getName();
			    cadena += "<br>Nombre Remoto --> " + filename ;
			    cadena += "<br> Tipo --> " + item.getContentType();
			    cadena += "<br> tamaño --> " + (item.getSize()/1240)+ "KB";
				cadena += "<br>"; 
            }
        }
		}catch(FileUploadException ex) {
			cadena = "Error encountered while parsing the request" + ex.getMessage();
		} catch (SQLException ex) {
			cadena = "Error encountered while save record bd " + ex.getMessage();
		} catch(Exception ex) {
			cadena = "Error encountered while uploading file" + ex.getMessage();
		}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>RESPUESTA SERVIDOR</title>
<LINK REL="stylesheet" TYPE="text/css" HREF="main.css">
</head>
<body>
	<%@ include file="header.jsp"%>
	<div class="contencenter demo">
	<h2>RESPUESTA DEL SERVIDOR</h2>
	<%
		out.print(cadena);
	%>
	</div>
	<%@ include file="foot.jsp"%>
</body>
</html>