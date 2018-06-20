<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="validausuario.jsp"%>
<%@ page import="com.are.sofatec.*" %>
<%@ page import="java.util.*" %>
<%
	if (request.getParameter("codigo") == null) {
		response.sendRedirect("FindOrder.jsp?operacion=assign");
		return;
	}
	
	String codigo = (String)request.getParameter("codigo");
	db conexion = new db();
	Ordenes orden = new Ordenes(conexion);
	if (!orden.Find(codigo)) {
		conexion.Close();
		response.sendRedirect("FindOrder.jsp?operacion=assign");
		return;
	}
	
	List<visita> lista = new ArrayList<visita>();
	
	if (orden.getVisitas() > 0) { // Hay visitas fallidas
		VisitaFallida VF = new VisitaFallida(conexion);
		lista = VF.List(codigo);
	}
	
	TipoOrden tipo = new TipoOrden(conexion);
	tipo.Find(orden.getTipo());
	departamento dpto = new departamento(conexion);
	localidad loca = new localidad(conexion);
	dpto.FindByLoca(orden.getMunicipio());
	loca.Find(orden.getMunicipio());
	Estados estado = new Estados(conexion);
	estado.Find(orden.getEstado());
	recursohumano recurso = new recursohumano(conexion);
	recurso.Find(orden.getRecurso());
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>ASIGNAR RECURSO A ORDEN DE SERVICIO</title>
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
<script type="text/javascript" language="javascript">
	function asignar(key) {
		var cedula = $("#cedula").val();
		var sms = $("#sms").val();
		if (cedula == "") {
			alert("falta ingresar informacion");
			return;
		}
		var cmd = document.getElementById("cmd_asignar");
		cmd.disabled = true;
		$("#info").html("<img src=\"images/loading.gif\" >Procesando solicitud");
		$.post(
			"SrvOrders",
			{
				operacion: "asignar",
				orden: key,
				cedula: cedula,
				num_show: "0",
				sms: sms
			},
			procesar
		
		);
		
	}
	function procesar(resultado) {
		var cmd = document.getElementById("cmd_asignar");
		cmd.disabled = false;
		if (resultado != 'OK') {
			$("#info").html("<img src=\"warning.jpg\">" + resultado);
		}else {
			$("#info").html("<img src=\"images/ok.png\">Orden asignada correctamente");
		}
	}

	function liberar(key) {
		if (confirm("Desea liberar la Orden " + key)) {
			var cmd = document.getElementById("cmd_liberar");
			cmd.disabled = true;
			$("#info").html("<img src=\"images/loading.gif\" >Procesando solicitud");
			$.post(
				"SrvOrders",
				{
					operacion: "liberar",
					orden: key
				},
				procesarLiberar
			
			);
	}
		
	}
	
	function procesarLiberar(resultado) {
		var cmd = document.getElementById("cmd_liberar");
		cmd.disabled = false;
		if (resultado != 'OK') {
			$("#info").html("<img src=\"warning.jpg\">" + resultado);
		}else {
			$("#info").html("<img src=\"images/ok.png\">Orden liberada correctamente");
		}
		
	}
	
	function cancelar() {
		window.location.href="FindOrder.jsp?operacion=assign";
	}
	
	
	function buscar() {
		url = "BuscarRecurso.jsp";
		window.open(url , "BuscarRecurso" , "width=600,height=500,scrollbars=YES,menubar=No,toolbar=NO,status=YES"); 
	}
	
	function ShowMapa(nic) {
		
		url = "ShowCoordCliente.jsp?nic="+nic;
		window.open(url , "ShowMapaCliente" , "width=600,height=500,scrollbars=YES,menubar=No,toolbar=NO,status=YES"); 
		
	}
	
</script>
</head>
<body>
<%@ include file="header.jsp" %>
<div class="contencenter demo">
<h2>ASIGNAR RECURSO A ORDEN DE SERVICIO</h2>
<div id="info">
	<% if (orden.getEstado().equals("99")) { %>
	<img src="warning.jpg"> Esta orden no se puede asignar por que se encuentre en estado CERRADA.	
	<% } %>
</div>
<form action="" name="form1">
		<table>
		<tr>
			<th colspan="4">Informacion Orden</th>
		</tr>
		<tr>
			<td>ORDEN</td>
			<td><%= orden.getOrden() %></td>
			<td>NIC</td>
			<td><%= orden.getNic() %></td>
		</tr>
		<tr class="odd">
			<td>Departamento</td>
			<td><%= dpto.getCodigo() %> <%= (String)dpto.getDescripcion()  %></td>
			<td>Municipio</td>
			<td><%= orden.getMunicipio() %> <%= (String)loca.getDescripcion()  %></td>
		</tr>
		<tr>
			<td>Direccion</td>
			<td><%= orden.getDireccion() %></td>
			<td>Direccion Referencia</td>
			<td><%= orden.getRdireccion() %></td>
			
		</tr>
		<tr class="odd">	
			<td>Medidor</td>
			<td><%= orden.getNum_apa()  %></td>
			<td>Marca</td>
			<td><%= orden.getDescmarca()  %></td>		
		</tr>
		<tr>
		<tr>
			<td>Tipo Orden</td>
			<td><%= tipo.getDescripcion() %></td>
			<td>NIS</td>
			<td><%= orden.getNis() %></td>
		</tr>
		<tr class="odd">
			<td>Fecha Generacion</td>
			<td><%= orden.getFtratamiento() %></td>
			<td>Estado</td>
			<td><%= estado.getDescripcion() %></td>
		</tr>
		<tr>
			<td>Visitas</td>
			<td colspan="3"><%= orden.getVisitas() %></td>
		</tr>
		<tr>
		<th colspan="4">Asignación</th>
		</tr>
		<% if (!orden.getEstado().equals("1")) {  %>	
		<tr>
			<td>Asignacion Actual</td>
			<td><%= orden.getRecurso() %> <%= recurso.getNombre() %></td>
			<td>Fecha asignacion</td>
			<td><%= orden.getFecha_asignacion() %></td>
		</tr>
		<% } %>
		<tr class="odd">
			<td>Asignar a</td>
			<td colspan="3"><input type="text" name="cedula" id="cedula" readonly><input type="text" name="nombre" id="nombre" size=40 readonly> <input type="button" name="cmd_buscar" id="cmd_buscar" value="Buscar" onclick="javascript:buscar()" ></td>
		</tr>
		<tr>
			<td>Enviar SMS?</td>
			<td colspan="3">
				<select id="sms" name="sms">
					<option value="N">No</option>
					<option value="S">Si</option>
				</select>
			</td>
		</tr>
		</table>
		<% if (!orden.getEstado().equals("99")) { // Si la orden no esta cerrada %>
			<input type="button" onclick="javascript:asignar('<%= (String)request.getParameter("codigo") %>');" value="Asignar" id="cmd_asignar" name="cmd_asignar" > 
			<input type="button" onclick="javascript:liberar('<%= (String)request.getParameter("codigo") %>');" value="Liberar" id="cmd_liberar" name="cmd_liberar" >  
			<% } %>
			<input type="button" name="cmd_mapa" id="cmd_mapa" value ="Ver Mapa" onclick="javascript:ShowMapa('<%= orden.getNic() %>')" >
			<input type="button" name="cmd_cancelar" id="cmd_cancelar" value ="Cancelar" onclick="javascript:cancelar()" >
	</form>
	</div>
	<%@ include file="foot.jsp" %>
</body>
</html>

<%
	conexion.Close();
%>