<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="validausuario.jsp"%>
<%@ page import="com.are.sofatec.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%
	db conexion = new db();
	Gps gps = new Gps(conexion);
	String cedula = (String)request.getParameter("cedula");
	
	ArrayList<GeoOrden> lista = gps.UbicacionOrdenesAsignadas(cedula);
	int contador =0;
	GeoPosicion geo = gps.UbicationNow(cedula);
	
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
<script src="js/jquery.js" language="JavaScript"></script>
<script src="ui/jquery.ui.core.js" language="JavaScript"></script>
<script src="ui/jquery.ui.widget.js" language="JavaScript"></script>
<script src="ui/jquery.ui.button.js"></script>
<link rel="stylesheet" href="themes/redmond/jquery.ui.all.css">
<LINK REL="stylesheet" TYPE="text/css" HREF="main.css">
<style type="text/css">
  html { height: 100% }
  body { height: 100%; margin: 0px; padding: 0px }
  #map_canvas { height: 400px; width: 800px; float: left; padding: 10px; margin-top: 10px }
</style>
<script type="text/javascript" src="https://maps.google.com/maps/api/js?sensor=false"></script>
<script type="text/javascript" src="http://google-maps-utility-library-v3.googlecode.com/svn/trunk/markerclusterer/src/markerclusterer_compiled.js"></script>
<script type="text/javascript">
	var map;
	var ruta = [];
	var ordenes = [];
	var direcciones = [];
	var markers = [];
    
	function inicializar() {
    	var marker;
		var latlng = new google.maps.LatLng(6.708254,-72.861328);
	    var myOptions = {
	      zoom: 5,
	      center: latlng,
	      mapTypeId: google.maps.MapTypeId.ROADMAP
	    };
	     
	    map = new google.maps.Map(document.getElementById("map_canvas"),
	            myOptions);
	    
	    var posicion;
	    
	    <% if (geo.isValid())  {  %>
			   	//var direccion = "";
	   	posicion = new google.maps.LatLng(<%= geo.getLatitud() %>,<%= geo.getLongitud()  %>);
	   	var marker = new google.maps.Marker({ //opciones
	        position: posicion ,
	        map:map,
	        icon: "images/ubicacion.png"
	   	});
	   	
	   	map.setCenter(posicion);
		map.setZoom(13);
		
	   	<% } %>
	   	
	    <% if (lista.size() > 0) {  %>
	   

	    <% for (GeoOrden posicion: lista)  {  %>
	    <% if (posicion.isValid()) { %>
		   	posicion = new google.maps.LatLng(<%= posicion.getLatitud() %>,<%= posicion.getLongitud()  %>);
		   	marker = new google.maps.Marker({
		        position: posicion ,
		        map:map
		   	});
	       ordenes.push("<%= posicion.getOrden() %>");
	       direcciones.push("<%= posicion.getDireccion()  %>");
	       markers.push(marker);
	       <% contador++; %>
	       
	   	<%  }   %>
	   	
	   	<% } %>
	    for (var x = 0, j = ordenes.length; x < j; x++) {
	        orden = ordenes[x];
	        direccion = direcciones[x];
	        marker = markers[x];
	        (function(marker, orden){                       
	            google.maps.event.addListener(marker, 'click', function() {
	                AgregarOrden(orden,marker);
	                //MostrarAsignacion();
	                
	            });
	        })(marker,orden);
	    }
	   	<%  } %> 
    }
    
    function MostrarAsignacion() {
    	var cadena="<h2>Ruta de Ejecucion</h2>";
    	for (var x = 0; x < ruta.length; x++) {
    		cadena = cadena + "[" + x + "] " + ruta[x] + " " + getDireccion(ruta[x])+ "<br/>"; 
    		
    	}
    	
    	if (ruta.length == ordenes.length ) {
    		cadena = cadena + "<br><a href=\"javascript:AsignarRuta('<%= (String)request.getParameter("cedula")  %>')\">Guardar Ruta</a>"
    	}
    	$("#ordenes").html(cadena);
    }
    
    function getDireccion(num_orden) {
    	for (var x = 0; x < ordenes.length; x++) {
    		if (ordenes[x] == num_orden) {
    			return direcciones[x];
    		}
    	}
    	return "";
    }
    
    function AgregarOrden(num_orden, marker) {
    	for (var x = 0; x < ruta.length; x++) {
    		if (ruta[x] == num_orden) {
    			if (confirm("Orden ya ha sido seleccionada, desea elimianr")) {
    				ruta.splice(x,1);
    				PintarNuevaRuta();
    			}
    			return ;
    		}
    		
    	}
    	ruta.push(num_orden);
    	var icon = "images/marker" + ruta.length + ".png";
    	marker.setIcon(icon);
    	
    }
    
    function PintarNuevaRuta() {
    	for (var x = 0; x < ordenes.length; x++) {
    		markers[x].setIcon("");
        	for (var y = 0; y < ruta.length; y++) {
        		if (ruta[y] == ordenes[x]) {
        			var icon = "images/marker" + (y+1) + ".png";
        			markers[x].setIcon(icon);
        			break;
        		}
    		}
    	}
    }
    
    function AsignarRuta(cedula) {
    	if (ruta.length == 0) {
    		alert("No se ha asignado ruta, verifique por favor");
    		return;
    	}
    	
    	if (confirm("Esta seguro de confirmar ruta de ejecución")) {
	    	 var url = "SrvAsignar?operacion=asignar&cedula="+ cedula;
			 var cad = "";
			 var datos = new Array();
			 for (var x = 0; x < ruta.length; x++) {
				    var valor = ruta[x];
				    cad = "orden=" + valor;
				    datos.push(cad);
				} 
			cad = datos.join("&");
				
			url += "&" + cad;
			$("#error").html("<img src=\"images/loading.gif\" >Procesando solicitud ");
			$.get(url,procesar);
    	}
    }
    
    function procesar(resultado) {
		if (resultado != 'OK') {
			$("#error").html("<img src=\"warning.jpg\">" + resultado);
		}else {
			$("#error").html("<img src=\"images/ok.png\">Ordenes asignadas");
		}
	}
    function automatico(cedula) {
    	if (confirm("Esta seguro de confirmar ruta de ejecución")) {
    		var url = "SrvRutaOrdenesAsignadas?recuros="+ cedula;
			$.get(url,procesarAutomatico);
    	}
    }
    function procesarAutomatico(resultado) {
		if (resultado != 'OK') {
			$("#error").html("<img src=\"warning.jpg\">" + resultado);
		}else {
			alert("Asignacion Completada");
			document.location.reload(true);
		}
	}

</script>
<script type="text/javascript">
$(function() {
	$( "input:submit, a, button").button();
	$( "input:button, a, button").button();
});
</script>
<title>GEOREFERENCIA ORDENES PENDIENTES</title>
</head>
<body onload="javascript:inicializar()">
  <h2>ORDENES ASIGNADAS TECNICO</h2>
  
  <div id="error"></div>
  <div>
  <a href="javascript:AsignarRuta('<%= (String)request.getParameter("cedula") %>')">Confirmar Ruta</a> <a href="javascript:automatico('<%= (String)request.getParameter("cedula") %>')">Automatico</a> <a href="javascript:window.close();" >Cancelar</a>
  </div>
  <pre><strong>Total Ordenes asignadas:</strong> <%= lista.size() %> | <strong>Ordenes Georeferenciadas:</strong> <%= contador %></pre>
  <div id="map_canvas"></div>
 
</body>
</html>

<%
	conexion.Close();
%>