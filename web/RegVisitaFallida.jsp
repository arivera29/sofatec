<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="validausuario.jsp"%>
<%@ page import="com.are.sofatec.*" %>
<%@ page import="java.util.*" %>
<%
	if (request.getParameter("codigo") == null) {
		response.sendRedirect("FindOrder.jsp?operacion=vista");
		return;
	}
	
	String codigo = (String)request.getParameter("codigo");
	db conexion = new db();
	Ordenes orden = new Ordenes(conexion);
	if (!orden.Find(codigo)) {
		conexion.Close();
		response.sendRedirect("FindOrder.jsp?operacion=vista");
		return;
	}
	
	List<visita> lista = new ArrayList<visita>();
	
	if (orden.getVisitas() > 0) { // Hay visitas fallidas
		VisitaFallida VF = new VisitaFallida(conexion);
		lista = VF.List(codigo);
	}
	
	List<imagenes> lista_imagenes = new ArrayList<imagenes>();
	GestionImagenes GI = new GestionImagenes(conexion);
	lista_imagenes = GI.List(codigo);
	
	TipoOrden tipo = new TipoOrden(conexion);
	tipo.Find(orden.getTipo());
	departamento dpto = new departamento(conexion);
	localidad loca = new localidad(conexion);
	dpto.FindByLoca(orden.getMunicipio());
	loca.Find(orden.getMunicipio());
	Estados estado = new Estados(conexion);
	estado.Find(orden.getEstado());
	Causales causales = new Causales(conexion);
	
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>INFORMACION ORDEN DE SERVICIO <%= (String)request.getParameter("codigo") %></title>
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

<script type="text/javascript">
$(function() {
	$( "input:submit, a, button", ".demo" ).button();
	$( "input:button, a, button", ".demo" ).button();
	$( ".fecha" ).datepicker({
		showOn: "button",
		buttonImage: "images/calendar.gif",
		buttonImageOnly: true,
		dateFormat: "yy-mm-dd"
	});
	$( ".fecha" ).datepicker( "option", "showAnim", "slide" );
});

function buscar() {
	url = "BuscarRecurso.jsp";
	window.open(url , "BuscarRecurso" , "width=600,height=500,scrollbars=YES,menubar=No,toolbar=NO,status=YES"); 
	
	
}

function Agregar(orden) {
	var causal = $("#causal").val();
	var cedula = $("#cedula").val();
	var fecha = $("#fecha").val();
	var observacion = $("#observacion").val();
	
	if (causal == "" || cedula == "" || fecha == "" || observacion == "") {
		alert("Faltan datos");
		return;
	}
	
	if (confirm("Esta seguro de agregar la visita?")) {
	var url = "SrvVisita";
		$.post(url,{
			operacion: "add_web",
			causal : causal,
			cedula : cedula,
			fecha : fecha,
			observacion : observacion,
			orden : orden
		},procesar);
	}
}

function procesar(resultado) {
	var cmd = document.getElementById("cmd_agregar");
	cmd.disabled = false;
	if (resultado != 'OK') {
		$("#info").html("<img src=\"warning.jpg\">" + resultado);
	}else {
		alert("Visita Agregada correctamente");
		window.location.reload();
	}
	
}

</script>

    
</head>
<body>
<%@ include file="header.jsp" %>
<div class="contencenter demo">
<h2>INFORMACION ORDEN DE SERVICIO <%= orden.getOrden() %></h2>
		<table>
		<tr>
			<th colspan="4">INFORMACION GENERAL</th>
		</tr>
		<tr>
			<td>No. Orden</td>
			<td><%= orden.getOrden() %></td>
			<td>NIC</td>
			<td><%= orden.getNic() %></td>
		</tr>
		<tr>
			<td>Departamento</td>
			<td><%= dpto.getCodigo() %> - <%= (String)dpto.getDescripcion()  %></td>
			<td>Municipio</td>
			<td><%= orden.getMunicipio() %> - <%= (String)loca.getDescripcion()  %></td>
		</tr>
		<tr>
			<td>Direccion</td>
			<td><%= orden.getDireccion() %></td>
			<td>Direccion Referencia</td>
			<td><%= orden.getRdireccion() %></td>
			
		</tr>
		<tr>	
			<td>Medidor</td>
			<td><%= orden.getNum_apa()  %></td>
			<td>Marca</td>
			<td><%= orden.getDescmarca()  %></td>		
		</tr>
		<tr>
			<td>Tipo Orden</td>
			<td><%= tipo.getDescripcion() %></td>
			<td>NIS</td>
			<td><%= orden.getNis() %></td>
		</tr>
		<tr>
			<td>Fecha Generacion</td>
			<td><%= orden.getFtratamiento() %></td>
			<td>Estado</td>
			<td><%= estado.getDescripcion() %></td>
		</tr>
		<tr>	
			<td>Visitas</td>
			<td><%= orden.getVisitas() %></td>
			<td></td>
			<td></td>
		</tr>
		</table>

	<h2>INFORMACION VISITAS FALLIDAS</h2>
		<table>
					<tr>
						<th>FECHA</th>
						<th>MOTIVO</th>
						<th>OBSERVACION</th>
						<th>TECNICO</th>
						<th>USUARIO</th>
					</tr>
					<% if (lista.size() > 0)  {%>		
					<% for (int i=0; i < lista.size(); i++)  {%>	
					<% visita v = lista.get(i); %>
					<tr>
						<td><%= v.getFecha() %></td>
						<td><%= v.getCausal() %> - <%= v.getDescripcionCausal() %></td>
						<td><%= v.getObservacion() %></td>
						<td><%= v.getNombreRecurso() %></td>
						<td><%= v.getUsuario() %></td>
					</tr>
					<% } %>
					<% } else { %>
						<tr>
						<td colspan="5">La orden no registra visitas fallidas</td>
					</tr>
					
					<% } %>
				</table>		
	
	
	<h2>IMAGENES REPORTADAS</h2>
		<table>
					<tr>
						<th>FECHA</th>
						<th>CEDULA TECNICO</th>
						<th>NOMBRE TECNICO</th>
						<th>NOMBRE ARCHIVO</th>
						<th>ACCION</th>
					</tr>
					<% if (lista_imagenes.size() > 0)  {%>		
					<% for (int i=0; i < lista_imagenes.size(); i++)  {%>	
					<% imagenes img = lista_imagenes.get(i); %>
					<tr>
						<td><%= img.getFecha() %></td>
						<td><%= img.getRecurso() %></td>
						<td><%= img.getNombrerecurso() %></td>
						<td><%= img.getFilename() %></td>
						<td><a href="imagenes/<%= img.getFilename()  %>" target="_blank">Descargar</a></td>
					</tr>
					<% } %>
					<% } else { %>
						<tr>
						<td colspan="5">La orden no registra imagenes</td>
					</tr>
					
					<% } %>
				</table>	
		<h2>Registro de Visita Fallida</h2>
		<div id="info"></div>
		<form name="form1" action="">
		<table>
			<tr>
				<th colspan="2">Informacion</th>
			</tr>
			<tr>
				<td>Causal</td>
				<td>
					<%= causales.CreateSelectHTML("causal") %>
				</td>
			</tr>
			<tr>
				<td>Fecha</td>
				<td><input type="text"  id="fecha" name="fecha" class="fecha" value="<%= Utilidades.strDateServer() %>" readonly size="10" ></td>
			</tr>
			<tr>
				<td>Tecnico</td>
				<td><input type="text" name="cedula" id="cedula" readonly><input type="text" name="nombre" id="nombre" size=40 readonly> <input type="button" name="cmd_buscar" id="cmd_buscar" value="Buscar" onclick="javascript:buscar()" ></td>
			</tr>
			<tr>
				<td>Observacion</td>
				<td><textarea rows="5" cols="40" id="observacion" name="observacion"></textarea></td>
			</tr>
			
		</table>
		<input type="button" value="Agregar Visita Fallida" name="cmd_agregar" id="cmd_agregar" onclick="javascript:Agregar('<%= (String)request.getParameter("codigo") %>')" >
		</form>
	</div>
</body>
</html>

<%
	conexion.Close();
%>