<%@page import="com.are.entidades.Zona"%>
<%@page import="com.are.manejadores.ManejadorZonas"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<%@ include file="validausuario.jsp"%>
<%@ page import="com.are.sofatec.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%
	db conexion = new db();
	ManejadorZonas manejador = new ManejadorZonas(conexion);
	ArrayList<Zona> lista = manejador.List((String)session.getAttribute("usuario"));
	
	ManejadorUsuarios mu = new ManejadorUsuarios(conexion);
	mu.find((String)session.getAttribute("usuario"));
	Usuarios usuario = mu.getUsuario();
	
	if (usuario.getReportes() != 1) {
		response.sendRedirect("EstadoLotes.jsp");
		conexion.Close();
		return;
	}
%>


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Reportes</title>
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
	
	
        function descargar(link,tipo) {
            $('#form1').attr('action', link);
            $("#tipo").val(tipo);
            $('#form1' ).submit();
            
        }
        
	
</script>

</head>
<body>
<%@ include file="header.jsp" %>
<div class="contencenter demo">
<h2>REPORTES</h2>
<div id="info"></div>
<form action="" name="form1" id="form1">
    <input type="hidden" value="1" name="tipo" id="tipo">
<table>
	<tr >
		<th colspan="2">Configuracion Consulta</th>
	</tr>	
	<tr>
		<td>Zona</td>
		<td>
			<select name="zona" id="zona">
				<% for (Zona zona : lista) { %>
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
	<button type="button" name="cmd_acciones" id="cmd_acciones" onclick="javascript:descargar('DownloadAcciones.jsp',0);" class="boton">Acciones</button>
	<button type="button" name="cmd_acciones" id="cmd_acciones" onclick="javascript:descargar('DownloadMaterial.jsp',1);" class="boton">Materiales Instalados</button>
	<button type="button" name="cmd_acciones" id="cmd_acciones" onclick="javascript:descargar('DownloadMaterial.jsp',2);" class="boton">Materiales Retirados</button>
	<button type="button" name="cmd_acciones" id="cmd_acciones" onclick="javascript:descargar('DownloadSellos.jsp',0);" class="boton">Sellos</button>
	<button type="button" name="cmd_acciones" id="cmd_acciones" onclick="javascript:descargar('DownloadCenso.jsp',0);" class="boton">Censo</button>
	
</form>
<div id="lista"></div>


</div>
<%@ include file="foot.jsp" %>
</body>
</html>

<%
	if (conexion != null) {
		conexion.Close();
	}

%>
