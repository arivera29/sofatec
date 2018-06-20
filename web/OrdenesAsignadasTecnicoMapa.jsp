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
<link rel="stylesheet" href="themes/redmond/jquery.ui.all.css">
<LINK REL="stylesheet" TYPE="text/css" HREF="main.css">
<style type="text/css">
  html { height: 100% }
  body { height: 100%; margin: 0px; padding: 0px }
  #map_canvas { height: 100% }
</style>
<script type="text/javascript" src="https://maps.google.com/maps/api/js?sensor=false"></script>
<script type="text/javascript" src="http://google-maps-utility-library-v3.googlecode.com/svn/trunk/markerclusterer/src/markerclusterer_compiled.js"></script>
<script type="text/javascript">
	var map;
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
	    var markers = [];
	    var info = [];
	    
	    var contenido;
	    var infowindow = new google.maps.InfoWindow({
	        content: ''
	    });
	    var icono; 
	    <% for (GeoOrden posicion: lista)  {  %>
	    <% if (posicion.isValid()) { %>
	    	icono = "images/marker<%= contador %>.png";
		   	posicion = new google.maps.LatLng(<%= posicion.getLatitud() %>,<%= posicion.getLongitud()  %>);
		   	marker = new google.maps.Marker({
		        position: posicion ,
		        map:map,
		        icon: icono
		   	});
	       contenido ="Orden: <STRONG> <%= posicion.getOrden()  %> </STRONG><br/>Tipo: <%= posicion.getTipo()  %>";
	       markers.push(marker);
	       info.push(contenido);
	       <% contador++; %>
	       
	   	<%  }   %>
	   	<% } %>
	    for (var x = 0, j = info.length; x < j; x++) {
	        contenido = info[x];
	        marker = markers[x];
	        (function(marker, contenido){                       
	            google.maps.event.addListener(marker, 'click', function() {
	                infowindow.setContent(contenido);
	                infowindow.open(map, marker);
	            });
	        })(marker,contenido);
	    }
	   	<%  } %> 
    }
</script>
<title>GEOREFERENCIA ORDENES PENDIENTES</title>
</head>
<body onload="javascript:inicializar()">
  <h2>ORDENES ASIGNADAS TECNICO</h2>
  Total Ordenes asignadas: <%= lista.size() %><br/>
  Ordenes Georeferenciadas: <%= contador %><br/>
  <div id="map_canvas" style="width:100%; height:100%"></div>
</body>
</html>

<%
	conexion.Close();
%>