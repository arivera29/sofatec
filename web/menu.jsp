<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ include file="validausuario.jsp"%>
<%@ page import="com.are.sofatec.*"%>
<%@ page import="java.sql.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	db conexion = new db();
	String menu = (String) request.getParameter("menu");
	String sql = "select * from menu where menu='" + menu
			+ "' order by orden";
	ResultSet rs = conexion.Query(sql);
	boolean rsIsEmpty = !rs.next();
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Menu</title>
<LINK REL="stylesheet" TYPE="text/css" HREF="main.css">
<link rel="Shortcut Icon" href="icono_tm.ico" type="image/x-icon" />
<!-- <link rel="stylesheet" type="text/css" href="css/accordion.css" />
<script type="text/javascript" src="js/prototype.js"></script>
<script type="text/javascript" src="js/scriptaculous.js"></script>
<script type="text/javascript" src="js/accordion.js"></script> -->
<script type="text/javascript">
	function ir_url() {
		var url = document.getElementById("menu").value;
	    window.location.href = url;
	}
</script>
</head>
<body>
	<%@ include file="header.jsp"%>
	<h2><img src="images/opciones.png"> OPCIONES</h2>
	<table>
		<tr>
			<th></th>
		</tr>
		<%
			if (!rsIsEmpty) { // hay opciones para el menu
		%>
		<tr>
			<td>Items: <select id="menu">
					<%
						do {
					%>
					<option value="<%=rs.getString("url")%>"><%=rs.getString("descripcion")%></option>
					<%
						} while (rs.next());
					%>
			</select> <a href="javascript:ir_url();" class="small button blue">Ir</a></td>
		</tr>
		<%
			}
		%>

	</table>
<%-- 		<%
			if (!rsIsEmpty) { // hay opciones para el menu
		%>
	<div id="test-accordion" class="accordion">
		
		<div class="accordion-toggle">Main</div>
		
			<div class="accordion-content">
					<%
					rs.first();	
					do {
					%>
					 <a href="<%=rs.getString("url")%>"><%=rs.getString("descripcion")%></a><br/>
					<%
						} while (rs.next());
					%>	
			</div>
			<div class="accordion-toggle">Why Use Us</div>
			<div class="accordion-content">
				logout
			</div>
	</div>
	<% } %> --%>
	<%@ include file="foot.jsp"%>
</body>
</html>

<%
	conexion.Close();
%>