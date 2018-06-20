<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="validausuario.jsp"%>
<%@ page import="com.are.sofatec.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%
	db conexion = new db();
	ManejadorZonas manejador = new ManejadorZonas(conexion);
	ArrayList<zonas> lista = manejador.List();
	
	ResultSet rs = null;
	boolean rsIsEmpty = true;
	int idZona=0;
	
	if (request.getParameter("cmd_consultar") != null) {
		String zona = (String)request.getParameter("zona");
		String fecha_inicial = (String)request.getParameter("fecha_inicial");
		String fecha_final = (String)request.getParameter("fecha_final");
		
		
		
		String sql = "SELECT QO_ORDENES.*, QO_ANOM.DESC_COD, RECURSO.RECUNOMB "
				+ " FROM QO_ORDENES,QO_ANOM, RECURSO " 
				+ " WHERE QO_ORDENES.ESTADO_OPER=3 " 
				+ " AND QO_ORDENES.TECNICO = RECURSO.RECUCODI "
				+ " AND QO_ORDENES.ANOMALIA = QO_ANOM.COD "
				+ " AND DATE(FECHA_ANOMALIA) BETWEEN ? AND ? ";
				
				if (!zona.equals("all")) {
					idZona = Integer.parseInt(zona);
					sql += " AND QO_ORDENES.NUM_ZONA = '" + zona + "'";
				}
		PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		pst.setString(1, fecha_inicial);
		pst.setString(2, fecha_final);
		rs = conexion.Query(pst);
		rsIsEmpty = !rs.next();
	}
	
	int fila=0;
%>


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Reporte Anomalias</title>
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
	$( ".fecha" ).datepicker({
		showOn: "button",
		buttonImage: "images/calendar.gif",
		buttonImageOnly: true,
		dateFormat: "yy-mm-dd"
	});
	$( ".fecha" ).datepicker( "option", "showAnim", "slide" );
	});
	
	function Generar(lote) {
		var url = "SrvInterfaz?lote=" + lote;
		$("#info").html("<img src='images/loading.gif'>Procesando solicitud");
		$("#info").load(url);
	}
	
	function Finalizar(lote) {
		if (confirm("Esta seguro de finalizar el lote?")) {
			var url ="SrvFinalizarLote";
			$.post(url,{
				lote : lote
			},function(result) {
				if (result.trim()== "OK") {
					RemoveRow(lote);
					$("#info").html("<img src='images/alerta.gif'> LOTE " + lote + " finalizado correctamente");
				}else {
					$("#info").html("<img src='images/alerta.gif'> " + resultado);
				}			
				
			});
		}
	}
	
	function RemoveRow(idElemento){
	    $("#" + idElemento).delay(400);
	    $("#" + idElemento).fadeOut(800, function () {
	        $("#" + idElemento).remove().fadeOut(800);
	    });
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
<h2>REPORTE ANOMALIAS</h2>

<form action="" name="form1">
<table>
	<tr >
		<th colspan="2">Filtro</th>
	</tr>	
	<tr>
		<td>Zona</td>
		<td>
			<select name="zona" id="zona">
				<option value="all">Todos</option>
				<% for (zonas zona : lista) { %>
				<option value="<%= zona.getId() %>" <%= zona.getId() == idZona?"selected":"" %>><%= zona.getNombre() %></option>
				<% } %>
			</select> 
		</td>
	</tr>
	<tr>
			<td>Fecha Inicial</td>
			<td><input type="text" name="fecha_inicial" id="fecha_inicial" readonly value="<%= request.getParameter("fecha_inicial")!= null?(String)request.getParameter("fecha_inicial"):Utilidades.strDateServer()   %>" class="fecha"></td>
		</tr>
		<tr>
			<td>Fecha Final</td>
			<td><input type="text" name="fecha_final" id="fecha_final" readonly value="<%= request.getParameter("fecha_final")!= null?(String)request.getParameter("fecha_final"):Utilidades.strDateServer()  %>" class="fecha"></td>
		</tr>
</table>
<input type="submit" name="cmd_consultar" value="Consultar" class="boton">
</form>

<div id="info"></div>
<% if (request.getParameter("zona") != null) { %>
<% if (!rsIsEmpty) { %>
<form action="ExportExcel.jsp" method="post" target="_blank" id="FormularioExportacion">
	<p>Exportar a Excel  <img src="images/Download.png" class="botonExcel" /></p> 
	<input type="hidden" id="datos_a_enviar" name="datos_a_enviar" /> 
	</form> 
	<table id="Exportar_a_Excel">
	<tr>
		<th>LOTE</th>
		<th>NUM_OS</th>
		<th>NIC</th>
		<th>TIPO</th>
		<th>DESC.TIPO</th>
		<th>DIRECCION</th>
		<th>ANOMALIA</th>
		<th>DESCRIPCION</th>
		<th>FECHA</th>
		<th>TECNICO</th>
	</tr>

	<% do { %>
		<tr id="<%= (String)rs.getString("NUM_OS") %>" <%=fila % 2 == 0 ? "class='odd'" : ""%>>
			<td><%= rs.getString("NUM_LOTE") %></td>
			<td><a href="DetalleOrdenLote.jsp?orden=<%= rs.getString("NUM_OS") %>"><%= rs.getString("NUM_OS") %></a></td>
			<td><%= rs.getString("NIC") %></td>
			<td><%= rs.getString("TIP_OS") %></td>
			<td><%= rs.getString("DESC_TIPO_ORDEN") %></td>
			<td><%= rs.getString("DIRECCION") %></td>
			<td><%= rs.getString("ANOMALIA") %></td>
			<td><%= rs.getString("DESC_COD") %></td>
			<td><%= rs.getString("FECHA_ANOMALIA") %></td>
			<td><%= rs.getString("TECNICO") %> <%= rs.getString("RECUNOMB") %></td>
		</tr>
		<% fila++; %>
		<% }while(rs.next()); %>
</table>
<% }else { %>
<h3>No se encontraron registros</h3>
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
