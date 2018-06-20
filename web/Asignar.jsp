<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="validausuario.jsp"%>
<%@ page import="com.are.sofatec.*" %>
<%@ page import="java.sql.*" %>
<%
	db conexion = new db();
	departamento dpto = new departamento(conexion);
	ResultSet rs = dpto.list();
	boolean rsIsEmpty = !rs.next();
	TipoOrden tipo = new TipoOrden(conexion);
	ResultSet rsTipo = tipo.List();
	
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>ASIGNAR ORDENES POR BARRIO</title>
<link rel="stylesheet" href="themes/redmond/jquery.ui.all.css">
<LINK REL="stylesheet" TYPE="text/css" HREF="main.css">
<script src="js/jquery.js" language="JavaScript"></script>
<script src="ui/jquery.ui.core.js" language="JavaScript"></script>
<script src="ui/jquery.ui.widget.js" language="JavaScript"></script>
<script src="ui/jquery.ui.button.js"></script>

<script type="text/javascript">
$(function() {
	$( "input:submit, button, a", ".demo" ).button();
	$( "input:button, button, a", ".demo" ).button();
});
</script>
<script type="text/javascript" language="javascript">
	function consultar() {
		var url = "SrvAsignar?operacion=list&localidad=" + $("#localidad").val() + "&tipo=" + $("#tipo").val();
		$("#list").html("<img src='images/loading.gif'> Procesando solicitud");
		$("#list").load(url, function() {
			$( "input:button, a", ".demo" ).button();
		});
	}
	
	function cargar_localidad(combo) {
		var url = "srvLocalidad?operacion=combo&departamento=" + combo.value;
		$("#municipio").load(url);
	}
	
	function validar() {
		
		var cedula = $("#cedula").val();
		if (cedula == "") {
			$("#error").html("<img src=\"warning.jpg\"  />Debe seleccionar el personal");
			return;
		}
		
		//var check = $("input[@name='orden[]']:checked").length;
		var check = $("input:checkbox:checked").length;
		 if(check == ""){
			    $('#error').html("<img src=\"warning.jpg\"  />Seleccione al menos una orden de servicio");
			    return false;
		 }else {
			 $("#error").html("");
			 var url = "SrvAsignarSeleccion?recurso="+ cedula;
			 var cad = "";
			 var datos = new Array();
			 $("input:checkbox:checked").each(function() {
				    var valor = $(this).val();
				    cad = "orden=" + valor;
				    datos.push(cad);
				}); 
			cad = datos.join("&");
				
			url += "&" + cad;
			$("#error").html("<img src=\"images/loading.gif\" >Procesando solicitud ");
			var cmd = document.getElementById("cmd_asignar");
			cmd.disabled = true;
			
			$.get(url,procesar);

		 }
			 
	}
	
	function procesar(resultado) {
		var cmd = document.getElementById("cmd_asignar");
		cmd.disabled = false;
		if (resultado != 'OK') {
			$("#error").html("<img src=\"warning.jpg\">" + resultado);
		}else {
			$("#error").html("<img src=\"images/ok.png\">Ordenes asignadas");
			eliminar_filas();
		}
		
	}
	
	function eliminar_filas() {
		$("input:checkbox:checked").each(function() {
		    var valor = $(this).val();
		    var fila = "#f" + valor;
		    jqRow = $(fila);
		    jqRow.fadeOut("slow", function(){
 				jqRow.remove();
                                                
			});
		}); 
		
	}
	
	function BuscarPersonal() {
		
		url = "BuscarRecurso.jsp";
		window.open(url , "BuscarRecurso" , "width=600,height=500,scrollbars=YES,menubar=No,toolbar=NO,status=YES"); 
		
	}
	
	function ShowMapa(nic) {
		
		url = "ShowCoordCliente.jsp?nic="+nic;
		window.open(url , "ShowMapaCliente" , "width=800,height=600,scrollbars=YES,menubar=No,toolbar=NO,status=YES"); 
		
	}
	function filtrar(ciudad,tipo) {
		var barrio = $("#cbo_barrio").val();
		url ="SrvAsignar?operacion=filtro&localidad=" + ciudad + "&tipo=" + tipo + "&barrio=" + encodeURIComponent(barrio);
		$("#lista").load(url, function() {
			$( "input:button, a", ".demo" ).button();
		});
	}
</script>
</head>
<body>
<%@ include file="header.jsp" %>
<div class="contencenter demo">
<h2>ORDENES PENDIENTE DE ASIGNAR</h2>
<div id="info"></div>
<form action="SrvAsignar" name="form2">
	<table>
		<tr>
			<th colspan="6">FILTRO</th>
		</tr>
		<tr>
			<td>Departamento <input type="hidden" value="list" name="operacion" /></td>
			<td>
				<select id="departamento" onchange="javascript:cargar_localidad(this);" name="departamento">
					<option value="all">Todos</option>
					<%  if (!rsIsEmpty) { %>
					<% do { %>
					<option value="<%= rs.getString("depacodi") %>"><%= rs.getString("depadesc") %></option>
					<% }while(rs.next()); %>
					<% } %>
				</select>
			</td>
			<td>Municipio</td>
			<td><div id="municipio">
				<select id="localidad">
					<option value="all">Todos</option>
				</select>
			</div></td>
			<td>Tipo de Orden</td>
			<td>
				<select id="tipo" name="tipo">
					<option value="all">Todos</option>
					
					<% while(rsTipo.next()) { %>
					<option value="<%= rsTipo.getString("tiorcodi") %>"><%= rsTipo.getString("tiordesc") %></option>
					<% } %>

				</select>
			</td>
		</tr>
	</table>
	<input type="button" name="cmd_buscar" value="Consultar" onclick="javascript:consultar()" >
</form>
<div id="list"></div>
</div>
<%@ include file="foot.jsp" %>
</body>
</html>
<%
	if (conexion != null) {
		conexion.Close();
	}
%>