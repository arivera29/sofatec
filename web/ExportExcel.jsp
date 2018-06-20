<%@page contentType="application/vnd.ms-excell; name='excel'"%> 
<% 

	String nombre = "listado.xls"; 
	response.setHeader("Content-Disposition","attachment; filename=\""+ nombre + "\""); 
	String datos = (String)request.getParameter("datos_a_enviar");
	out.write(datos);
%>  