<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<%@ include file="validausuario.jsp"%>
<%@ page import="com.are.sofatec.*" %>
<%@ page import="java.sql.*" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>RUTA RECURSO</title>
<link rel="stylesheet" href="themes/redmond/jquery.ui.all.css">
<LINK REL="stylesheet" TYPE="text/css" HREF="main.css">
<script src="js/jquery.js"></script>
<script src="ui/jquery.ui.core.js"></script>
<script src="ui/jquery.ui.widget.js"></script>
<script src="ui/jquery.ui.button.js"></script>

<script type="text/javascript">
$(function() {
	$( "input:submit, a, button", ".demo" ).button();
	$( "input:button, a, button", ".demo" ).button();
});
</script>
<style type="text/css">
  #map_canvas { width: 100%; height: 600px; margin-left:5px;margin-top: 5px; }
</style>
<script type="text/javascript" src="https://maps.google.com/maps/api/js?sensor=false"></script>
<script type="text/javascript" src="http://google-maps-utility-library-v3.googlecode.com/svn/trunk/markerclusterer/src/markerclusterer_compiled.js"></script>
<script type="text/javascript">
var map;
var geocoder;
function initialize() {
	geocoder = new google.maps.Geocoder();
	var latlng = new google.maps.LatLng(6.708254,-72.861328);
    var myOptions = {
      zoom: 5,
      center: latlng,
      mapTypeId: google.maps.MapTypeId.ROADMAP
    };
    map = new google.maps.Map(document.getElementById("map_canvas"),
        myOptions);
}


</script>

<script type="text/javascript">

	function buscar() {
		url = "BuscarRecurso.jsp";
		window.open(url , "BuscarRecurso" , "width=600,height=500,scrollbars=YES,menubar=No,toolbar=NO,status=YES"); 
	}
	function ruta() {
		initialize();
		var markers = [];
		var info = [];
		var infowindow = new google.maps.InfoWindow({
	        content: ''
	    });
		var puntos = [];
		var url = "XmlRutaRecurso?cedula=" + $("#cedula").val();
		var i=1;
		$.get(url, function (xml) {
		    $(xml).find("coordenada").each(function () {
		    	var latitud = $(this).find("latitud").text();
		       	var longitud = $(this).find("longitud").text();
		       	var fecha = $(this).find("fecha").text();
		       	var icono = "images/marker" + i + ".png"; 
		       	latitud = latitud.replace(',','.');
		       	longitud = longitud.replace(',','.');
		       	var direccion = "";
		       	var posicion = new google.maps.LatLng(parseFloat(latitud),parseFloat(longitud));
		       	puntos.push(posicion);
		       	var marker = new google.maps.Marker({ //opciones
		            position: posicion,
		            //icon: icono,
		            map:map
		            
		        });
		       	var contenido ="<B>Informacion Posición</b><br/>Fecha Posicionamiento: <b>" + fecha + "</b><br/>Coordenadas: <b>" + latitud + "," + longitud + "</b>" ;
		        
		       	markers.push(marker);
		       	info.push(contenido);
				if (i==1) {
					map.setCenter(posicion);
					map.setZoom(13);
					
				}
				i++;
		    });
		    
		    var flightPath = new google.maps.Polyline({
		        path: puntos,
		        strokeColor: "#0000dd",
		        strokeOpacity: 0.3,
		        strokeWeight: 6
		      });

		      flightPath.setMap(map);
		    //var markerclusterer = new MarkerClusterer(map, markers);
		    for (var x = 0, j = info.length; x < j; x++) {
		        var contenido = info[x];
		        var marker = markers[x];
		        (function(marker, contenido){                       
		            google.maps.event.addListener(marker, 'click', function() {
		                infowindow.setContent(contenido);
		                infowindow.open(map, marker);
		            });
		        })(marker,contenido);
		    }
		});
	}
</script>
</head>
<body onload="initialize()">
<%@ include file="header.jsp" %>
<div class="contencenter demo">
<h2>RUTA RECURSO</h2>
<div id="info"></div>
<form action="KmlRutaRecurso" name="form1">
	<table>
		<tr>
			<th colspan="4">RECURSO</th>
		</tr>
		<tr>
			<td>Recurso</td>
			<td><input type="text" name="cedula" id="cedula" readonly></td>
			<td>Nombre</td>
			<td><input type="text" name="nombre" id="nombre" size=30 readonly></td>
		</tr>
			
	</table>
	<input type="hidden" name="operacion" value="download_resource" >
	<input type="button" name="cmd_buscar" id="cmd_buscar" value="Buscar" onclick="javascript:buscar()" > 
	<input type="button" name="cmd_buscar" value="Ver Ruta" onclick="javascript:ruta()" >
	<input type="submit" name="cmd_download" value="KML" >
</form>
<br/>
<div id="map_canvas"></div>
</div>
</body>
</html>
