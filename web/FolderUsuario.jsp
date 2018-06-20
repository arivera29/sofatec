<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
	<%@ page import="java.io.*" %>
	<%@ page import="java.text.*" %>
<%@ include file="validausuario.jsp"%>

<%
	ServletContext servletContext = getServletContext();
	
	String usuario = (String)session.getAttribute("usuario");
	
	
	String path = servletContext.getRealPath("/generated") + File.separator + usuario.trim() ;
	
	File f = new File(path);
	if (!f.exists()) {
		f.mkdirs();		
	}
	File[] ficheros = f.listFiles();
	

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>MIS ARCHIVOS</title>
<link rel="stylesheet" href="themes/redmond/jquery.ui.all.css">
<LINK REL="stylesheet" TYPE="text/css" HREF="main.css">
<script src="js/jquery.js" language="JavaScript"></script>
<script src="ui/jquery.ui.core.js" language="JavaScript"></script>
<script src="ui/jquery.ui.widget.js" language="JavaScript"></script>
<script src="ui/jquery.ui.button.js"></script>

</head>
<body>
	<%@ include file="header.jsp"%>
	<div class="contencenter demo">
	<h2>MI DIRECTORIO</h2>
	
	<table>
	<tr>
		<th>File</th>
		<th>Size</th>
		<th>Date</th>
		<th></th>
	</tr>
	<%
		if (ficheros.length > 0) {
			
			for (int x=0;x<ficheros.length;x++){
				long ms = ficheros[x].lastModified();
				Date d = new Date(ms);
	%>		
			<tr>
				<td><%= ficheros[x].getName() %></td>
				<td>
				
					<% 
						if (ficheros[x].length() > 0) {
							if (ficheros[x].length() < 1024) {
								out.print(ficheros[x].length() + " Bytes");
							}else {
								NumberFormat formatter = new DecimalFormat("#0.00");    
								out.print(formatter.format(ficheros[x].length()/1024) + " KB");
							}
						}else {
							out.print("-");
						}
					
					%></td>
				<td><%= d.toLocaleString() %></td>
				<td><a href="<%= "generated/" + usuario.trim() + File.separator + ficheros[x].getName()  %>" title="Descargar archivo"><img src='images/descarga.png'></a></td>	
			</tr>	
	
	<%
				}
			
		}
	
	
	%>	
	</table>
	</div>
	<%@ include file="foot.jsp"%>
</body>
</html>
