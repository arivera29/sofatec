<%@page import="com.are.censo.controlador.CtlVisitas"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="validausuario.jsp"%>
<%@ page import="com.are.sofatec.*"%>
<%@ page import="java.sql.*"%>
<%
	db conexion = new db();
	String orden = (String)request.getParameter("orden");

	
	boolean rsIsEmpty = true; 
	String sql = "SELECT QO_ORDENES.*, QO_DATOSUM.*, QO_APARATOS.NUM_APA,ESTADOS.ESTADESC,QO_ANOM.DESC_COD,RECURSO.RECUNOMB "
				+ " FROM QO_ORDENES, QO_DATOSUM, QO_APARATOS,ESTADOS,QO_ANOM,RECURSO " 
				+ " WHERE QO_ORDENES.NIC = QO_DATOSUM.NIC"
				+ " AND QO_ORDENES.NIS_RAD = QO_DATOSUM.NIS_RAD"
				+ " AND QO_ORDENES.ESTADO_OPER = ESTADOS.ESTACODI"
				+ " AND QO_ORDENES.ANOMALIA = QO_ANOM.COD"
				+ " AND QO_ORDENES.TECNICO = RECURSO.RECUCODI"
				+ " AND QO_ORDENES.NUM_OS = ?";
			
	PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
	pst.setString(1, orden);
	ResultSet rs = conexion.Query(pst);
	rsIsEmpty = !rs.next();
	sql ="SELECT QO_APARATOS.*, QO_CODIGOS.DESC_COD FROM QO_APARATOS,QO_CODIGOS WHERE QO_APARATOS.CO_MARCA = QO_CODIGOS.COD AND  QO_APARATOS.NUM_OS=? ";
	pst = conexion.getConnection().prepareStatement(sql);
	pst.setString(1, orden);
	ResultSet rsAparatos= conexion.Query(pst);
	boolean rsAparatosIsEmpty = !rsAparatos.next();
	
	
	sql ="SELECT QO_RECIBOS.* FROM QO_RECIBOS WHERE NUM_OS =?";
	pst = conexion.getConnection().prepareStatement(sql);
	pst.setString(1, orden);
	ResultSet rsRecibos= conexion.Query(pst);
	boolean rsRecibosIsEmpty = !rsRecibos.next();
	
	sql ="SELECT QO_PRECIN.*,QO_CODIGOS.DESC_COD FROM QO_PRECIN,QO_CODIGOS WHERE NUM_OS =? AND CO_MARCA=COD";
	pst = conexion.getConnection().prepareStatement(sql);
	pst.setString(1, orden);
	ResultSet rsPrecintos= conexion.Query(pst);
	boolean rsPrecintosIsEmpty = !rsPrecintos.next();
	
	sql ="SELECT QO_PASOS.*, QO_OSACCION.DESC_COD FROM QO_PASOS, QO_OSACCION " 
			+	" WHERE QO_PASOS.NUM_OS =? "
			+ 	" AND QO_OSACCION.TIP_OS =? "
			+	" AND QO_PASOS.NUM_PASO=QO_OSACCION.NUM_PASO " 
			+	" AND QO_PASOS.CO_ACCEJE = QO_OSACCION.CO_ACCEJE";
	pst = conexion.getConnection().prepareStatement(sql);
	pst.setString(1, orden);
	pst.setString(2, rs.getString("TIP_OS"));
	ResultSet rsPasos= conexion.Query(pst);
	boolean rsPasosIsEmpty = !rsPasos.next();
	
	
	sql ="SELECT QO_ORDEN_PRECINTOS.* FROM QO_ORDEN_PRECINTOS WHERE NUM_OS =?";
	pst = conexion.getConnection().prepareStatement(sql);
	pst.setString(1, orden);
	ResultSet rsPrecintosReporte= conexion.Query(pst);
	boolean rsPrecintosReporteIsEmpty = !rsPrecintosReporte.next();
	
	sql ="SELECT QO_ORDEN_MATERIAL.*, QO_DESC_MI.DESC_ELEMENTO FROM QO_ORDEN_MATERIAL,QO_DESC_MI WHERE NUM_OS =? AND QO_ORDEN_MATERIAL.CO_ELEMENTO = QO_DESC_MI.CO_ELEMENTO";
	pst = conexion.getConnection().prepareStatement(sql);
	pst.setString(1, orden);
	ResultSet rsMaterial= conexion.Query(pst);
	boolean rsMaterialIsEmpty = !rsMaterial.next();
	
	List<imagenes> lista_imagenes = new ArrayList<imagenes>();
	GestionImagenes GI = new GestionImagenes(conexion);
	lista_imagenes = GI.List(orden);
	
	sql = "SELECT COD,DESC_COD FROM QO_ANOM WHERE COD != '-1' ORDER BY COD";
	pst = conexion.getConnection().prepareStatement(sql);
	ResultSet rsAnomalia = conexion.Query(pst);
        
        CtlVisitas ctlVisita = new CtlVisitas(conexion);
	
	int fila = 0;
	
	Gps gps = new Gps(conexion);
	String latitud="";
	String longitud="";
	String fecha = "";
	boolean centrar = false;
	

%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>DETALLE ORDEN DE SERVICIO</title>
<link rel="stylesheet" href="themes/redmond/jquery.ui.all.css">
<LINK REL="stylesheet" TYPE="text/css" HREF="main.css">
<script src="js/jquery.js" language="JavaScript"></script>
<script src="ui/jquery.ui.core.js" language="JavaScript"></script>
<script src="ui/jquery.ui.widget.js" language="JavaScript"></script>
<script src="ui/jquery.ui.button.js"></script>
<script src="ui/jquery.ui.tabs.js"></script>
<script src="ui/jquery.ui.dialog.js"></script>
<script src="ui/jquery.ui.position.js"></script>
<script src="ui/jquery.effects.core.js"></script>
<script src="ui/jquery.effects.slide.js"></script>
<script src="ui/jquery.ui.datepicker.js"></script>
<script src="ui/jquery.effects.explode.js"></script>
<script src="ui/jquery.effects.fold.js"></script>
<script src="ui/jquery.effects.slide.js"></script>
<style type="text/css">
  #map_canvas { width: 100%; height: 500px; float: left; margin: 10px 10px }
</style>
<script type="text/javascript"
    src="https://maps.google.com/maps/api/js?sensor=false">
</script>
<script type="text/javascript">
  var map;
  var LatR, LonR, fechaR;
  function initialize() {
	
	var coordenadas = new google.maps.LatLng(6.708254,-72.861328);
    var myOptions = {
      zoom: 5,
      center: coordenadas,
      mapTypeId: google.maps.MapTypeId.ROADMAP
    };
    
    var map = new google.maps.Map(document.getElementById("map_canvas"),
        myOptions);
    
    
    <%  if (gps.CoordenasCliente(rs.getString("NIC"))) { 
    	centrar = true;
    %>
    	var coordenadas = new google.maps.LatLng(<%= gps.getLatitud().replace(',', '.') %>,<%= gps.getLongitud().replace(',', '.') %>);    
    	new google.maps.Marker({ //opciones
		        position: coordenadas,
		        map: map,
		        title:"Orden " + <%= rs.getString("NUM_OS") %>,
		        icon: "images/home.png"
		    });
		    map.setCenter(coordenadas);
		    map.setZoom(15);
    
    <%  } %>
    
    <% if (rs.getString("ESTADO_OPER").equals("3")) {  %>
    <%    
    if (gps.CoordenaRecursoFecha(rs.getString("TECNICO"),rs.getString("FECHA_ANOMALIA")) ) {
    	latitud = gps.getLatitud().replace(',','.');
    	longitud = gps.getLongitud().replace(',','.');
    	fecha = gps.getFecha();
    %>
    	var coordenadas = new google.maps.LatLng(<%= latitud %>,<%= longitud %>);
    	new google.maps.Marker({ //opciones
            position: coordenadas,
            map: map,
            icon: "images/ubicacion.png"
        });
    	
    	<% if (!centrar)  { %>
	    	map.setCenter(coordenadas);
		    map.setZoom(15);
    	<% } %>
    	
    
    <% }  
    }
    %>
    
    <% if (rs.getString("ESTADO_OPER").equals("99")) {  %>
    <%    
    	if (gps.CoordenaRecursoFecha(rs.getString("TECNICO"),rs.getString("FECHA_CIERRE")) ) {
    		latitud = gps.getLatitud().replace(',','.');
        	longitud = gps.getLongitud().replace(',','.');
        	fecha = gps.getFecha();
    %>
    	var coordenadas = new google.maps.LatLng(<%= latitud %>,<%= longitud %>);
    	new google.maps.Marker({ //opciones
            position: coordenadas,
            map: map,
            icon: "images/ubicacion.png"
        });
    	
    	<% if (!centrar)  { %>
    	map.setCenter(coordenadas);
	    map.setZoom(15);
	<% } %>
    
    <% }  
    }
    %>
  }

</script>
<script type="text/javascript">
	$(function() {
		$(".boton").button();
	});
	
	function buscar() {
		url = "BuscarRecurso.jsp";
		window.open(url, "BuscarRecurso","width=600,height=500,scrollbars=YES,menubar=No,toolbar=NO,status=YES");
	}
</script>
<script type="text/javascript">
$(function() {
	$( "#ventana" ).dialog({
		autoOpen : false,
		show: "blind",
		hide: "blind",
		modal : true,
		width: 500,
		position : {my: "center", at: "center", of: window }
	});
});

function ReporteAnomalia() {
	$( "#ventana" ).dialog("open");
}

function Anomalia(orden) {
	var observacion = $("#observacion").val();
	var cedula = $("#cedula").val();
	var anomalia = $("#anomalia").val();
	
	if (observacion == "" || cedula == "" || anomalia == "") {
		alert("Falta informacion.");
		return;
	}
	
	var url = "SrvAnomaliaMasiva?ordenes=" + orden;
	//alert(recurso);
	if (confirm("Esta seguro de reportar la orden como Anomala?")) {
		$("#info").html("<img src='images/loading.gif' > Procesando Solicitud");
		
		$.post(url,{
			anomalia : anomalia,
			observacion : observacion,
			recurso : cedula
		},function(result) {
			$( "#ventana" ).dialog("close");
			if (result == "OK") {
				alert("Anomalia Reportada correctamente");
				document.location.reload();
			}else {
				$("#info").html("<img src='images/alerta.gif' >" + result + "<BR>");
			}
		});		
	}
}
</script>
</head>
<body onload="initialize()">
	<%@ include file="header.jsp"%>
	<div class="contencenter demo">
		<h2>INFORMACION DE ORDEN DE SERVICIO <%= (String)request.getParameter("orden") %></h2>
                <% if (ctlVisita.isOrdenCenso((String)request.getParameter("orden"))) {  %>
                <a class="boton" href ="SrvGenerateActaCenso?orden=<%= (String)request.getParameter("orden")  %>">Acta Censo</a>
                <% }  %>
		<div id="info"></div>
			<table>
				<tr>
					<th colspan="4">INFORMACION ORDEN DE SERVICIO</th>
				</tr>
				<tr>
					<td>NUMERO ORDEN</td>
					<td><%= (String)rs.getString("NUM_OS") %></td>
					<td>LOTE</td>
					<td><%= (String)rs.getString("NUM_LOTE") %></td>
				</tr>
				<tr>
					<td>NIC</td>
					<td><%= (String)rs.getString("NIC") %></td>
					<td>NIS RAD</td>
					<td><%= (String)rs.getString("NIS_RAD") %></td>
				</tr>
				<tr>
					<td>TIPO ORDEN</td>
					<td><%= (String)rs.getString("TIP_OS") %> <%= (String)rs.getString("DESC_TIPO_ORDEN") %></td>
					<td>PRIORIDAD</td>
					<td><%= (String)rs.getString("DESC_COD_PRIORIDAD") %></td>
				</tr>
				<tr>
					<td>COMENTARIO OS</td>
					<td><%= (String)rs.getString("COMENT_OS") %></td>
					<td>COMENTATIO OS 2</td>
					<td><%= (String)rs.getString("COMENT_OS2") %></td>
				</tr>
				<tr>
					<td>DEPARTAMENTO</td>
					<td><%= (String)rs.getString("DEPARTAMENTO") %></td>
					<td>MUNICIPIO</td>
					<td><%= (String)rs.getString("MUNICIPIO") %></td>
				</tr>
				<tr>
					<td>LOCALIDAD</td>
					<td><%= (String)rs.getString("LOCALIDAD") %></td>
					<td>FINCA</td>
					<td><%= (String)rs.getString("ACC_FINCA") %></td>
				</tr>
				<tr>
					<td>DIRECCION</td>
					<td><%= (String)rs.getString("DIRECCION") %></td>
					<td>DIRECCION REF</td>
					<td><%= (String)rs.getString("REF_DIR") %></td>
				</tr>
				<tr>
					<td>ESTADO DE LA ORDEN</td>
					<td style="font-size : 14px"><strong><%= (String)rs.getString("ESTADESC") %></strong></td>
					<td></td>
					<td></td>
				</tr>
				
				
			</table>
			<h2>Informacion Medidor</h2>
			<% if (!rsAparatosIsEmpty) { %>
			<table>
				<tr>
					<th colspan="4">Medidor</th>
				</tr>
				<tr>
					<td>MEDIDOR</td>
					<td><%= (String)rsAparatos.getString("NUM_APA") %></td>
					<td>MARCA</td>
					<td><%= (String)rsAparatos.getString("DESC_COD") %></td>
				</tr>
				<tr>
					<td>FECHA INSTALACION</td>
					<td><%= (String)rsAparatos.getString("F_INST") %></td>
					<td>FECHA FABRICACION</td>
					<td><%= (String)rsAparatos.getString("F_FABRIC") %></td>
				</tr>
			</table>
			<% } else { %>
				No Hay informacion de medidor.
			<% } %>
			
			<h2>Recibos</h2>
			<% if (!rsRecibosIsEmpty) { %>
			<table>
				<tr>
					<th>SIMBOLO VARIABLE</th>
					<th>FECHA FACT.</th>
					<th>FECHA VENC.</th>
					<th>IMPORTE</th>
				</tr>
				<% do { %>
				<tr>
					<td><%= (String)rsRecibos.getString("SIMBOLO_VAR") %></td>
					<td><%= (String)rsRecibos.getString("F_FACT") %></td>
					<td><%= (String)rsRecibos.getString("F_VCTO_FACT") %></td>
					<td><%= rsRecibos.getDouble("IMP_TOT_REC") %></td>
				</tr>
				<% }while(rsRecibos.next()); %>
			</table>
			<% } else { %>
				No Hay informacion de recibos.
			<% } %>
			
			<h2>Precintos</h2>
			<% if (!rsPrecintosIsEmpty) { %>
			<table>
				<tr>
					<th>MEDIDOR</th>
					<th>MARCA</th>
					<th>PRECINTO</th>
				</tr>
				<% do { %>
				<tr>
					<td><%= (String)rsPrecintos.getString("NUM_APA") %></td>
					<td><%= (String)rsPrecintos.getString("DESC_COD") %></td>
					<td><%= (String)rsPrecintos.getString("NUM_PRECIN") %></td>
				</tr>
				<% }while(rsPrecintos.next()); %>
			</table>
			<% } else { %>
				No Hay informacion de precintos.
			<% } %>
			
			<% if (rs.getInt("ESTADO_OPER") == 3 || rs.getInt("ESTADO_OPER") == 99 ) { %>
			<H2>Informacion de Cierre</H2>
			<% if (rs.getInt("ESTADO_OPER") == 3) {  // ANOMALIA %>
			<H2>Reporte Anomalia</H2>
			<table>
				<tr>
					<th colspan="2">Informacion Anomailia</th>
				</tr>
				<tr>
					<td>ANOMALIA</td>
					<td><%= (String)rs.getString("DESC_COD") %></td>
					
				</tr>
				<tr>
					<% String obs = new String(rs.getBytes("OBSERVACION"),"UTF-8"); %>
					<td>OBSERVACION</td>
					<td><%= obs %></td>
					
				</tr>
				<tr>
					<td>SMARTPHONE REPORTE</td>
					<td><%= (String)rs.getString("IMEI") %></td>
				</tr>
				<tr>
					<td>FECHA ANOMALIA</td>
					<td><%= (String)rs.getString("FECHA_ANOMALIA") %></td>
				</tr>
				<tr>
					<td>TECNICO</td>
					<td><%= (String)rs.getString("RECUNOMB") %></td>
				</tr>
			</table>
			
			<% } else {  // EFECTIVA%>
			<h2>Pasos</h2>
			<% if (!rsPasosIsEmpty) { %>
			
			<table>
				<tr>
					<th>PASO</th>
					<th>CONFIRMADO</th>
					<th>ACCION</th>
					<th>DESCRIPCION</th>
					<th>OBSERVACION</th>
				</tr>
				<% do { %>
				<tr>
					<% String obs = new String(rsPasos.getBytes("OBSERVACION"),"utf-8"); %>
					<td><%= (String)rsPasos.getString("NUM_PASO") %> (<%= (String)rsPasos.getString("DESCRIPCION") %>)</td>
					<td><%= rsPasos.getInt("CUMPLIDO")==1?"Si":"No" %></td>
					<td><%= (String)rsPasos.getString("CO_ACCEJE") %></td>
					<td><%= (String)rsPasos.getString("DESC_COD") %></td>
					<td><%= obs %></td>
				</tr>
				<% }while(rsPasos.next()); %>
			</table>
			<% } else { %>
				No Hay informacion de precintos.
			<% } %>
			
			
			<h2>Precintos Reportados</h2>
			<% if (!rsPrecintosReporteIsEmpty) { %>
			<table>
				<tr>
					<th>MEDIDOR</th>
					<th>PRECINTO</th>
					<th>AGREGADO</th>
				</tr>
				<% do { %>
				<tr>
					<td><%= (String)rsPrecintosReporte.getString("NUM_APA") %></td>
					<td><%= (String)rsPrecintosReporte.getString("NUM_PRECIN") %></td>
					<td><%= (String)rsPrecintosReporte.getString("AGREGADO") %></td>
				</tr>
				<% }while(rsPrecintosReporte.next()); %>
			</table>
			<% } else { %>
				No Hay informacion de precintos.
			<% } %>
			
			<h2>Materiales Reportados</h2>
			<% if (!rsMaterialIsEmpty) { %>
			<table>
				<tr>
					<th>ACCION</th>
					<th>COD ELEMENTO</th>
					<th>DESCRIPCION</th>
					<th>CANTIDAD</th>
					<th>TIPO</th>
					<th>COBRO</th>
				</tr>
				<% do { %>
				<tr>
					<td><%= (String)rsMaterial.getString("CO_ACCEJE") %></td>
					<td><%= (String)rsMaterial.getString("CO_ELEMENTO") %></td>
					<td><%= (String)rsMaterial.getString("DESC_ELEMENTO") %></td>
					<td><%= rsMaterial.getDouble("CANTIDAD") %></td>
					<td><%= rsMaterial.getInt("TIPO")==1?"Instalado":"Retirado" %></td>
					<td><%= rsMaterial.getInt("COBRO")==1?"Si":"No" %></td>
				</tr>
				<% }while(rsMaterial.next()); %>
			</table>
			<% } else { %>
				No Hay informacion de Materiales.
			<% } %>
			
			<% } // FIN EFECTIVA  %>
			
			
			<% } else { // FIN SI ES REPORTE %>
			<a href="javascript:ReporteAnomalia()" class="boton">Reportar Anomalia</a>
			<a href="ReporteOrdenEfectiva.jsp?orden=<%= (String)request.getParameter("orden") %>" class="boton">Reportar Efectiva</a>
			<% } %>
	
	<h2>Imagenes Enviadas</h2>
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
						<td><a href="imagenes/<%= img.getFilename()  %>" target="_blank" class="boton">Descargar</a></td>
					</tr>
					<% } %>
					<% } else { %>
						<tr>
						<td colspan="5">La orden no registra imagenes</td>
					</tr>
					
					<% } %>
				</table>	
	
	<H2>UBICACION CIERRE ORDEN DE SERVICIO</H2>		
		<div id="map_canvas"></div>
	
	</div>
	
	<div id="ventana">
		<form action="" name="form1">
			<h2>Anomalias</h2>

			Seleccionar Anomalia: <br> <select name="anomalia" id="anomalia">
				<%
					while (rsAnomalia.next()) {
				%>
				<option value="<%=rsAnomalia.getString("COD")%>">
					(<%=rsAnomalia.getString("COD")%>)
					<%=rsAnomalia.getString("DESC_COD")%></option>
				<%
					}
				%>
			</select> <br> Ingresar Observacion: <br>
			<textarea rows="8" cols="40" id="observacion"></textarea> <br>
			
			Recurso: 
			<input type="text" name="cedula" id="cedula" readonly> <input type="text" name="nombre" id="nombre" size=40 readonly> 
			<br>	
			<input type="button" name="cmd_buscar" id="cmd_buscar" value="Buscar" onclick="javascript:buscar()" class="boton">
			<input type="button" name="cmd_anomalia" value="Resolver como Anomalia" onclick="javascript:Anomalia('<%=  (String)request.getParameter("orden") %>')" class="boton">
		</form>
		</div>

</body>
</html>

<%
	conexion.Close();

%>