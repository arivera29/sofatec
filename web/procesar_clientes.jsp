<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ include file="validausuario.jsp"%>
<%
	String perfilUser = (String)session.getAttribute("perfil");
	if (!perfilUser.equals("Admin")) {
		response.sendRedirect("accesodenegado.jsp");
		return;
	}
%>
<%@ page import="java.io.*" %>
<%@ page import="com.are.sofatec.*" %>
<%@ page import="java.sql.*" %>
<%@ page import ="com.csvreader.CsvReader" %>
<%
	String operacion = "";
	if (request.getParameter("operacion") != null) {
		operacion = (String)request.getParameter("operacion");
	}
	
	String usuario = (String)session.getAttribute("usuario");
%>

<%
ServletContext servletContext = getServletContext();
String folder = servletContext.getRealPath("/upload");
File dir = new File(folder);
FilenameFilter filter = new FilenameFilter() {
    public boolean accept(File dir, String name) {
    	return name.endsWith(".csv") || name.endsWith(".txt") ;
    }
};
String[] ficheros = dir.list(filter);

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<LINK REL="stylesheet" TYPE="text/css" HREF="main.css">
<script src="prototype.js" language="JavaScript"></script>
<link rel="Shortcut Icon" href="icono_tm.ico" type="image/x-icon" />
<script type="text/javascript">
function submitform()
{
  document.form1.submit();
}

</script>
<title>Procesar Archivo de Clientes</title>
</head>
<body>
<%@ include file="header.jsp"%>
<h2>Archivos .cvs uploaded en el servidor</h2>
<form name="form1" action="" method = "POST">
<table>
<tr>
	<th colspan="2">Archivo a procesar</th>
</tr>
<tr>
	<td>Archivo</td>
	<td> <select name="file" id="file">
<%
if (ficheros != null) {
	  for (int x=0;x<ficheros.length;x++)
		  out.println("<option value=\""+ ficheros[x] + "\">" + ficheros[x] + "</option>");
	}
%>
</select> 
</td>
</tr>
<tr>
	<td>Caracter separador</td>
	<td>
		<select name="separador">
			<option value="1">Tabulador</option>
			<option value="2">Coma (,)</option>
			<option value="3">Punto y Coma (;)</option>
		</select>
	</td>
<tr>
<tr>
	<td colspan="2"><input type="submit" name="procesar" value="Procesar Archivo"> </td>
<tr>
</table>
</form>
<%
	if (request.getParameter("separador") != null) {
		
		String filename = (String)request.getParameter("file");
		String separador = (String)request.getParameter("separador");
		String path = servletContext.getRealPath("/upload") +   File.separator +filename;
		CsvReader reader = null;
		db conexion = null;
		try {
		 reader = new CsvReader(path);
		 
		 
		 if (separador.equals("1")) {
			 reader.setDelimiter('	');  // tabulador
		 }else if (separador.equals("2")) {
			 reader.setDelimiter(','); // coma
		 }else if (separador.equals("3")){
			 reader.setDelimiter(';');  // punto y coma
		 }else {
			 reader.setDelimiter(','); 
		 }
		 
		 	 
	
		 reader.readHeaders();
		 
		 String[] headers = reader.getHeaders();
		 
		 if (headers.length != 3) { // estan las columnas OK.
			  throw  new IOException("Archivo no cargado.  Numero de columnas no coinciden con la estructura del archivo de clientes");
		 }
		 
		 out.println("<h2>Resultado carga</h2>");  // fin de encabezados
		 
		 conexion = new db();
		 int add = 0;
		 int update=0;
		 int failed =0;	 
		 while(reader.readRecord()){
			
			 	
					
				if (reader.get(1).equals("0") || reader.get(2).equals("0")) {
					failed++;
					continue;
				}
				Clientes cliente = new  Clientes(conexion);
				try {
					if (!cliente.Find(reader.get(0))) {
						
						cliente.setNic(reader.get(0));
						cliente.setLatitud(reader.get(1));
						cliente.setLongitud(reader.get(2));
						
						if (cliente.add()) {  // agregar cliente
							add++;
						}else {
							failed++;
						}
					}else {
						cliente.setNic(reader.get(0));
						cliente.setLatitud(reader.get(1));
						cliente.setLongitud(reader.get(2));
						
						if (cliente.modify(reader.get(0))) {  // modificar cliente
							update++;
						}else {
							failed++;
						}
					}
				}catch (SQLException e) {
					failed++; 
				}
				
			 
		 }

		 out.println("<table>");
		 out.println("<tr>");
		 out.println("<th>Item</th>");
		 out.println("<th>Resultado</th>");
		 out.println("</tr>");
		 out.println("<tr>");
		 out.println("<td>Agregados</td>");
		 out.println("<td>" + add +"</td>");
		 out.println("<tr>");
		 out.println("<tr>");
		 out.println("<td>Modificados</td>");
		 out.println("<td>" + update +"</td>");
		 out.println("<tr>");
		 out.println("<tr>");
		 out.println("<td>Descartados</td>");
		 out.println("<td>" + failed +"</td>");
		 out.println("<tr>");
		 out.println("</table>");
		 
		} catch (FileNotFoundException e) {
			out.println(e.getMessage());
		} catch (IOException e) {
			out.println(e.getMessage());
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			reader.close();
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
%>
<%@ include file="foot.jsp" %>
</body>
</html>