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
	ArrayList<zonas> lista = manejador.List((String)session.getAttribute("usuario"));
	
	ResultSet rs = null;
	boolean rsIsEmpty = true;
	
	if (request.getParameter("cmd_consultar") != null) {
		String zona = (String)request.getParameter("zona");
		String fecha_inicial = (String)request.getParameter("fecha_inicial");
		String fecha_final = (String)request.getParameter("fecha_final");
		
		String sql = "SELECT TECNICO,RECUNOMB, SUM(IF(ESTADO_OPER=3,1,0)) CA, SUM(IF(ESTADO_OPER=99,1,0)) CR, COUNT(*) TOTAL  " 
				+ " FROM QO_ORDENES,RECURSO WHERE ESTADO_OPER IN(3,99) AND TECNICO=RECUCODI AND (DATE(FECHA_CIERRE) BETWEEN ? AND ? OR DATE(FECHA_ANOMALIA) BETWEEN ? AND ?) ";
				
				if (!zona.equals("all")) {
					sql += " AND QO_ORDENES.NUM_ZONA = '" + zona + "'";
				}
				sql += " GROUP BY TECNICO " 
				+ " ORDER BY RECUNOMB";
		PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		pst.setString(1, fecha_inicial);
		pst.setString(2, fecha_final);
		pst.setString(3, fecha_inicial);
		pst.setString(4, fecha_final);
		rs = conexion.Query(pst);
		rsIsEmpty = !rs.next();
	}
%>


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Consulta Estado Reportes Ordenes de Servicio</title>
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
</head>
<body>
<%@ include file="header.jsp" %>
<div class="contencenter demo">
<h2>ESTADO LOTES</h2>

<form action="" name="form1">
<table>
	<tr >
		<th colspan="2">Configuracion Consulta</th>
	</tr>	
	<tr>
		<td>Zona</td>
		<td>
			<select name="zona" id="zona">
				<% for (zonas zona : lista) { %>
				<option value="<%= zona.getId() %>"><%= zona.getNombre() %></option>
				<% } %>
			</select> 
		</td>
	</tr>
	<tr>
			<td>Fecha Inicial</td>
			<td><input type="text" name="fecha_inicial" id="fecha_inicial" readonly value="<%= Utilidades.strDateServer()  %>" class="fecha"></td>
		</tr>
		<tr>
			<td>Fecha Final</td>
			<td><input type="text" name="fecha_final" id="fecha_final" readonly value="<%= Utilidades.strDateServer()  %>" class="fecha"></td>
		</tr>
</table>
<input type="submit" name="cmd_consultar" value="Consultar" class="boton">
</form>

<div id="info"></div>
<% if (request.getParameter("cmd_consultar") != null) { %>
<table>
	<tr>
		<th>CEDULA</th>
		<th>NOMBRE</th>
		<th>ANOMALIA</th>
		<th>EFECTIVA</th>
		<th>TOTAL</th>
	</tr>
	<% if (!rsIsEmpty) { %>
	<% do { %>
	<tr id="<%= rs.getString("TECNICO") %>">
		<td><%= rs.getString("TECNICO") %></td>
		<td><%= rs.getString("RECUNOMB") %></td>
		<td><%= rs.getLong("CA") %></td>
		<td><%= rs.getLong("CR") %></td>
		<td><%= rs.getLong("TOTAL") %></td>
	</tr>
	<% }while(rs.next()); %>
	<% } %>

</table>



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
