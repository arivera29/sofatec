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
<title>Procesar Archivo de Recurso</title>
</head>
<body>
<%@ include file="header.jsp"%>
<h2>Archivos .cvs uploaded en el servidor</h2>
<form name="form1" action="ProcessResource" method = "POST">
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
<%@ include file="foot.jsp" %>
</body>
</html>