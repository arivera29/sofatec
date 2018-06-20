<%@page import="com.are.entidades.Zona"%>
<%@page import="com.are.manejadores.ManejadorZonas"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<%@ include file="validausuario.jsp"%>
<%@ page import="com.are.sofatec.*" %>
<%@ page import="java.sql.*" %>
<%
	db conexion = new db();

	ManejadorZonas manejador = new ManejadorZonas(conexion);
	ArrayList<Zona> lista = manejador.List((String)session.getAttribute("usuario"));
	ManejadorUsuarios mu = new ManejadorUsuarios(conexion);
	mu.find((String)session.getAttribute("usuario"));
	Usuarios usuario = mu.getUsuario();
	
	ResultSet rs = null;
	boolean rsIsEmpty = true;
	if (request.getParameter("cmd_consultar") != null) {
		String zona = (String)request.getParameter("zona");
		String sql = "SELECT NUM_LOTE, SUM(IF(ESTADO_OPER=0,1,0)) PENDIENTE, " 
				+ "SUM(IF(ESTADO_OPER=1,1,0)) ASIGNADO," 
				+ "SUM(IF(ESTADO_OPER=3,1,0)) ANOMALIA,"
				+ "SUM(IF(ESTADO_OPER=99,1,0)) RESUELTA, "
				+ "USUARIO_CARGA,"
				+ "COUNT(*) TOTAL "
				+ " FROM QO_ORDENES, QO_DATOSUM "
				+ " WHERE  QO_ORDENES.NIC = QO_DATOSUM.NIC "
				+ " AND QO_ORDENES.EST_LOTE = 0 "
				+ " AND QO_ORDENES.NIS_RAD = QO_DATOSUM.NIS_RAD ";
		
				if (!session.getAttribute("usuario").toString().toUpperCase().equals("ADMIN") && !session.getAttribute("perfil").toString().equals("6") && !session.getAttribute("perfil").toString().equals("7")) {
				 	sql += " AND USUARIO_CARGA='" + (String)session.getAttribute("usuario") + "' ";
				}
				
				if (!zona.equals("all")) {
					sql += " AND QO_ORDENES.NUM_ZONA = '" + zona + "'";
				}
				sql += " GROUP BY NUM_LOTE " 
				+ " ORDER BY NUM_LOTE";
		PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		rs = conexion.Query(pst);
		rsIsEmpty = !rs.next();
	}
	
	int contador=0;
%>


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Consulta Ejecucion Ordenes</title>
<link rel="stylesheet" href="themes/redmond/jquery.ui.all.css">
<LINK REL="stylesheet" TYPE="text/css" HREF="main.css">
<script src="js/jquery.js"></script>
<script src="ui/jquery.ui.core.js"></script>
<script src="ui/jquery.ui.widget.js"></script>
<script src="ui/jquery.ui.button.js"></script>
<script src="ui/jquery.effects.core.js"></script>
<script src="ui/jquery.effects.slide.js"></script>
<script src="ui/jquery.ui.datepicker.js"></script>
<script src="ui/jquery.effects.explode.js"></script>
<script src="ui/jquery.effects.fold.js"></script>
<script src="ui/jquery.effects.slide.js"></script>

<script type="text/javascript">
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
	
	function selectAll() {
		$("input:checkbox").prop('checked', true);
	}
	function clearAll() {
		$("input:checkbox").prop('checked', false);	
	}
	
	function GenerarArchivos() {
	var url = "SrvInterfazLotes?";
		
		var lotes = "";
		//recorremos todos los checkbox seleccionados con .each
		$('input[name="lote"]:checked').each(function() {
			//$(this).val() es el valor del checkbox correspondiente
			lotes += "&lotes=" + this.value;
		});
		
		if (lotes == "") {
			alert("Debe seleccionar al menos un lote");
			return;
		}
		
		url += lotes;
	
		$("#info").html("<img src='images/loading.gif'>Procesando solicitud");
		$("#info").load(url);
	}
	
	
	
</script>
</head>
<body>
<%@ include file="header.jsp" %>
<div class="contencenter demo">
<h2>ESTADO LOTES</h2>

<form action="" name="form1">
	Seleccionar Zona: 
	<select name="zona" id="zona" required>
		<% for (Zona z : lista) { %>
		<option value="<%= z.getId() %>" 
			<% 
					if (request.getParameter("zona")!= null) { 
						String c = (String)request.getParameter("zona");
						if (!c.equals("all") && !c.equals("")) {
							int id = Integer.parseInt(c);
							if (z.getId()== id) {
								out.print("selected");
							}
						}
					} 
					
			%> 
		><%= z.getNombre() %></option>
		<% } %>
	</select> 
	<input type="submit" name="cmd_consultar" value="Consultar" class="boton">
	<% if (usuario.getReportes()==1) { %>
	<a href="Reportes2.jsp" class="boton">Reportes</a>
	<% } %>
</form>
<div id="info"></div>
<% if (request.getParameter("cmd_consultar") != null) { %>
<h2>Lotes encontrados</h2>
<a href="javascript:selectAll()" class="boton">Seleccionar Todo</a> 
<a href="javascript:clearAll()" class="boton">Quitar Seleccion</a> 

<table>
	<tr>
		<th></th>
		<th>LOTE</th>
		<th>USUARIO</th>
		<th>PTE. ASIGNAR</th>
		<th>ASIGNADA</th>
		<th>ANOMALIA</th>
		<th>RESUELTA</th>
		<th>EJECUTADO</th>
		<th>TOTAL</th>
		<th>GENERAR</th>
	</tr>
	<% if (!rsIsEmpty) { %>
	<% do { %>
	<tr id="<%= rs.getString("NUM_LOTE") %>" <%= ++contador%2==0?"class='odd'":"" %>>
		<td><input type="checkbox" name="lote" id="lote" value="<%= rs.getString("NUM_LOTE") %>"></td>
		<td><%= rs.getString("NUM_LOTE") %></td>
		<td><%= rs.getString("USUARIO_CARGA") %></td>
		<td><a href="pendientes.jsp?num_lote=<%= rs.getString("NUM_LOTE") %>"><%= rs.getLong("PENDIENTE") %></a></td>
		<td><a href="asignadas.jsp?num_lote=<%= rs.getString("NUM_LOTE") %>"><%= rs.getLong("ASIGNADO") %></a></td>
		<td><a href="anomalias.jsp?num_lote=<%= rs.getString("NUM_LOTE") %>"><%= rs.getLong("ANOMALIA") %></a></td>
		<td><a href="resueltas.jsp?num_lote=<%= rs.getString("NUM_LOTE") %>"><%= rs.getLong("RESUELTA") %></a></td>
		<td><a href="ejecutadas.jsp?num_lote=<%= rs.getString("NUM_LOTE") %>"><%= rs.getLong("RESUELTA") + rs.getLong("ANOMALIA") %></a></td>
		<td><%= rs.getLong("TOTAL") %></td>
		<td>
			<% if (rs.getLong("ANOMALIA") + rs.getLong("RESUELTA") == rs.getLong("TOTAL") ) { %>
				<a href="javascript:Generar('<%= rs.getString("NUM_LOTE") %>')" class="boton">Generar Cierre</a>
				
			<% }else { %>
				<% if (usuario.getHda()== 1) { %>
				<a href="procesar_lote_hda.jsp?lote=<%= rs.getString("NUM_LOTE") %>" class="boton">HDA</a>
				<% } %>
			<% } %>
			<a href="javascript:Finalizar('<%= rs.getString("NUM_LOTE") %>')" class="boton">Finalizar Lote</a>
		</td>	
	</tr>
	<% }while(rs.next()); %>
	<% } %>

</table>
<input type="button" name="cmd_generar" id="cmd_generar" value="Generar Cierre" onclick="javascript:GenerarArchivos();" class="boton"> Genera el archivo de cierres de las ordenes ejecutadas (anomalias+efectivas) de los LOTES seleccionados.


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
