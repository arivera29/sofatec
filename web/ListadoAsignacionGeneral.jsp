<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="validausuario.jsp"%>
<%@ page import="com.are.sofatec.*" %>
<%@ page import="java.sql.*" %>
<%
	db conexion = new db();
	
	ManejadorZonas manejador = new ManejadorZonas(conexion);
	ArrayList<zonas> lista = manejador.List();
	
	ResultSet rs = null;
	boolean rsIsEmpty = true;
	
	if (request.getParameter("cmd_consultar") != null) {
		String zona = (String)request.getParameter("zona");
			String sql = "SELECT QO_ORDENES.*, RECURSO.RECUNOMB, QO_DATOSUM.*, "
					+ " (SELECT QO_APARATOS.NUM_APA FROM QO_APARATOS WHERE QO_ORDENES.NUM_OS = QO_APARATOS.NUM_OS) AS NUM_APA ,"
					+ "(SELECT SUM(IMP_TOT_REC) FROM QO_RECIBOS WHERE QO_RECIBOS.NUM_OS = QO_ORDENES.NUM_OS) AS DEUDA "
					+ " FROM QO_ORDENES,RECURSO, QO_DATOSUM  " 
					+ " WHERE QO_ORDENES.ESTADO_OPER = 1 "
					+ " AND QO_ORDENES.NIC = QO_DATOSUM.NIC"
					+ " AND QO_ORDENES.NIS_RAD = QO_DATOSUM.NIS_RAD"
					+ " AND QO_ORDENES.TECNICO = RECURSO.RECUCODI ";
				
				if (!zona.equals("all")) {
					sql += " AND QO_ORDENES.NUM_ZONA = '" + zona + "'";
				}
				sql += " ORDER BY RECUNOMB";
		PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		rs = conexion.Query(pst);
		rsIsEmpty = !rs.next();
	}
	int fila=0;
%>


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Consulta Ejecucion Ordenes</title>
<link rel="stylesheet" href="themes/redmond/jquery.ui.all.css">
<LINK REL="stylesheet" TYPE="text/css" HREF="main.css">
<script src="js/jquery.js" language="JavaScript"></script>
<script src="ui/jquery.ui.core.js" language="JavaScript"></script>
<script src="ui/jquery.ui.widget.js" language="JavaScript"></script>
<script src="ui/jquery.ui.button.js"></script>
<script src="ui/jquery.effects.core.js"></script>
<script src="ui/jquery.effects.slide.js"></script>
<script src="ui/jquery.ui.datepicker.js"></script>
<script src="ui/jquery.effects.explode.js"></script>
<script src="ui/jquery.effects.fold.js"></script>
<script src="ui/jquery.effects.slide.js"></script>

<script type="text/javascript" language="javascript">
	$(function() {
		$( ".boton" ).button();
	});
	
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
<h2>LISTADO DE ASIGNACION GENERAL</h2>
<form action="" name="form1">
	Seleccionar Zona: 
	<select name="zona" id="zona">
					<option value="all">Todos</option>
					<% for (zonas zona : lista) { %>
					<option value="<%= zona.getId() %>"><%= zona.getNombre() %></option>
					<% } %>
				</select> 
	<input type="submit" name="cmd_consultar" value="Consultar" class="boton">
</form>
<div id="info"></div>
<%   if (request.getParameter("zona") != null) { %>
<% if (!rsIsEmpty) { %>
	<form action="ExportExcel.jsp" method="post" target="_blank" id="FormularioExportacion">
	<p>Exportar a Excel  <img src="images/Download.png" class="botonExcel" /></p> 
	<input type="hidden" id="datos_a_enviar" name="datos_a_enviar" /> 
	</form> 
<table id="Exportar_a_Excel">
	<tr>
		<th>DEPARTAMENTO</th>
		<th>MUNICIPIO</th>
		<th>ORDEN</th>
		<th>NIC</th>
		<th>CLIENTE</th>
		<th>DIRECCION</th>
		<th>DIR. REF</th>
		<th>BARRIO</th>
		<th>TARIFA</th>
		<th>TIPO ORDEN</th>
		<th>MEDIDOR</th>
		<th>DEUDA</th>
		<th>FECHA GEN.</th>
		<th>CEDULA TECNICO</th>
		<th>NOMBRE TECNICO</th>
	</tr>
	<% do { %>
	<tr <%= (fila %2 ==0)?"class='odd'":"" %>>
		<td><%= (String)rs.getString("DEPARTAMENTO") %></td>
		<td><%= (String)rs.getString("MUNICIPIO") %></td>
		<td><%= (String)rs.getString("NUM_OS") %></td>
		<td><%= (String)rs.getString("NIC") %></td>
		<td><%= (String)rs.getString("APE1_CLI") %> <%= (String)rs.getString("APE2_CLI") %> <%= (String)rs.getString("NOM_CLI") %></td>
		<td><%= (String)rs.getString("DIRECCION") %></td>
		<td><%= (String)rs.getString("REF_DIR") %></td>
		<td><%= (String)rs.getString("LOCALIDAD") %></td>
		<td><%= (String)rs.getString("COD_TAR") %></td>
		<td><%= (String)rs.getString("DESC_TIPO_ORDEN") %></td>
		<td><%= rs.getString("NUM_APA")!=null?(String)rs.getString("NUM_APA"):"DIRECTO" %></td>
		<td><%= rs.getString("DEUDA")!=null?(String)rs.getString("DEUDA"):"0" %></td>
		<td><%= (String)rs.getString("F_GEN") %></td>
		<td><%= (String)rs.getString("TECNICO") %></td>
		<td><%= (String)rs.getString("RECUNOMB") %></td>
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
<%@ include file="foot.jsp" %>
</body>
</html>

<%
	if (conexion != null) {
		conexion.Close();
	}

%>
