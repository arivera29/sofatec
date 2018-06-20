<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="validausuario.jsp"%>
<%@ page import="com.are.sofatec.*" %>
<%@ page import="java.sql.*" %>
<%
	db conexion = new db();
	ResultSet rs = null;
	boolean rsIsEmpty = true; 
	if (request.getParameter("cmd_buscar") != null) {
		String orden = (String)request.getParameter("orden");
		String nic = (String)request.getParameter("nic");
		
		
		String sql = "SELECT QO_ORDENES.*, QO_DATOSUM.*,ESTADOS.ESTADESC "
				+ " FROM QO_ORDENES, QO_DATOSUM, ESTADOS " 
				+ " WHERE QO_ORDENES.NIC = QO_DATOSUM.NIC"
				+ " AND QO_ORDENES.NIS_RAD = QO_DATOSUM.NIS_RAD"
				+ " AND QO_ORDENES.ESTADO_OPER = ESTADOS.ESTACODI";
			
			if (!orden.equals("")) {
				sql += " AND QO_ORDENES.NUM_OS = '" + orden + "'";
			}
			
			if (!nic.equals("")) {
				sql += " AND QO_ORDENES.NIC = '" + nic + "'";
			}
			sql += " ORDER BY FECHA_CARGA";
		
			rs = conexion.Query(sql);
			rsIsEmpty = !rs.next();
		
	}

	int fila = 0;

%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>BUSQUEDA ORDEN DE SERVICIO</title>
<link rel="stylesheet" href="themes/redmond/jquery.ui.all.css">
<LINK REL="stylesheet" TYPE="text/css" HREF="main.css">
<script src="js/jquery.js" language="JavaScript"></script>
<script src="ui/jquery.ui.core.js" language="JavaScript"></script>
<script src="ui/jquery.ui.widget.js" language="JavaScript"></script>
<script src="ui/jquery.ui.button.js"></script>

<script type="text/javascript">
$(function() {
	$( "input:submit, a, button", ".demo" ).button();
	$( "input:button, a, button", ".demo" ).button();
});
</script>
<script type="text/javascript">

function validar() {
	var orden = $("#orden").val();
	var nic = $("#nic").val();
	
	if (orden == "" && nic == "") {
		alert("Debe ingresar al menos un criterio de busqueda")
		return false;
	}
	
	return true;
}
</script>

<script language="javascript"> 
$(document).ready(function() { 
     $(".botonExcel").click(function(event) { 
     $("#datos_a_enviar").val( $("<div>").append( $("#Exportar_a_Excel").eq(0).clone()).html()); 
     $("#FormularioExportacion").submit(); 
	}); 
}); 
</script>

</head>
<body>
<%@ include file="header.jsp" %>
<div class="contencenter demo">
<h2>BUSQUEDA DE ORDEN DE SERVICIO</h2>
<form action="" name="form1" onsubmit="javascript: return validar();">
	<table>
		<tr>	
			<th colspan="2">Criterio de Busqueda</th>
		</tr>
		<tr>
			<td>NUMERO ORDEN</td>
			<td><input type="text" name="orden" id="orden" size=40 value="<%= request.getParameter("orden")!=null?(String)request.getParameter("orden"):""  %>"></td>
		</tr>
		<tr>
			<td>NIC</td>
			<td><input type="text" name="nic" id="nic" size=40 <%= request.getParameter("nic")!=null?(String)request.getParameter("nic"):""  %>></td>
		</tr>
	</table>
	<input type="submit" name="cmd_buscar" id="cmd_buscar"  value="Buscar">
</form>
<%   if (request.getParameter("cmd_buscar") != null) { %>
<% if (!rsIsEmpty) { %>
	<form action="ExportExcel.jsp" method="post" target="_blank" id="FormularioExportacion">
	<p>Exportar a Excel  <img src="images/Download.png" class="botonExcel" /></p> 
	<input type="hidden" id="datos_a_enviar" name="datos_a_enviar" /> 
	</form> 
<table>
	<tr>
		<th>DEPARTAMENTO</th>
		<th>MUNICIPIO</th>
		<th>ORDEN</th>
		<th>NIC</th>
		<th>LOTE</th>
		<th>CLIENTE</th>
		<th>DIRECCION</th>
		<th>DIR. REF</th>
		<th>BARRIO</th>
		<th>TIPO ORDEN</th>
		<th>FECHA CARGA</th>
		<th>USUARIO</th>
		<th>USUARIO</th>
		<th></th>
	</tr>
	<% do { %>
	<tr <%= (fila %2 ==0)?"class='odd'":"" %>>
		<td><%= (String)rs.getString("DEPARTAMENTO") %></td>
		<td><%= (String)rs.getString("MUNICIPIO") %></td>
		<td><%= (String)rs.getString("NUM_OS") %></td>
		<td><%= (String)rs.getString("NIC") %></td>
		<td><%= (String)rs.getString("NUM_LOTE") %></td>
		<td><%= (String)rs.getString("APE1_CLI") %> <%= (String)rs.getString("APE2_CLI") %> <%= (String)rs.getString("NOM_CLI") %></td>
		<td><strong><%= (String)rs.getString("DIRECCION") %></strong></td>
		<td><%= (String)rs.getString("REF_DIR") %></td>
		<td><%= (String)rs.getString("LOCALIDAD") %></td>
		<td><strong><%= (String)rs.getString("DESC_TIPO_ORDEN") %></strong></td>
		<td><%= (String)rs.getString("FECHA_CARGA") %></td>
		<td><%= (String)rs.getString("USUARIO_CARGA") %></td>
		<td><%= (String)rs.getString("ESTADESC") %></td>
		<td><a href="DetalleOrdenLote.jsp?orden=<%= (String)rs.getString("NUM_OS") %>">Detalle</a></td>
		
	</tr>
	<% fila++; %>
	<% } while(rs.next()); %>
</table>
Total registros: <%= fila %>


<% }else { // Si esta vacio el ResultSet %>
No tiene ordenes asignadas.

<% } %>
<% } %>
</div>

</body>
</html>

<%
	conexion.Close();

%>