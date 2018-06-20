<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ include file="validausuario.jsp"%>
<%@ page import="com.are.sofatec.*" %>
<%@ page import="java.sql.*" %>
<html>
<head>
<title>HISTORICO RUTA RECURSO</title>
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
<link rel="Shortcut Icon" href="icono_tm.ico" type="image/x-icon" />
<style type="text/css">
  #map_canvas { width: 100%; height: 600px; left: 10px; top: 10px }
</style>
<script type="text/javascript" src="https://maps.google.com/maps/api/js?sensor=false"></script>
<script type="text/javascript" src="http://google-maps-utility-library-v3.googlecode.com/svn/trunk/markerclusterer/src/markerclusterer_compiled.js"></script>
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
</script>
<script type="text/javascript">
var map;
function initialize() {
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
		var puntos = [];
		var infowindow = new google.maps.InfoWindow({
	        content: ''
	    });
		var url = "XmlRutaRecurso?cedula=" + $("#cedula").val() + "&fecha=" + $("#fecha").val();
		var i=1;
		$.get(url, function (xml) {
		    $(xml).find("coordenada").each(function () {
		    	var latitud = $(this).find("latitud").text();
		       	var longitud = $(this).find("longitud").text();
		       	var fecha =  $(this).find("fecha").text();
		       	var icono = "images/car.png"; 
		       	latitud = latitud.replace(',','.');
		       	longitud = longitud.replace(',','.');
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
					map.setCenter(new google.maps.LatLng(parseFloat(latitud),parseFloat(longitud)));
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
<h2>HISTORICO RUTA RECURSO</h2>
<div id="info"></div>
<form action="KmlRutaRecurso" name="form1">
	<table>
		<tr>
			<th colspan="6">Seleccion Recurso</th>
		</tr>
		<tr>
			<td>Recurso</td>
			<td><input type="text" name="cedula" id="cedula" readonly> </td>
			<td>Nombre</td>
			<td><input type="text" name="nombre" id="nombre" size=30 readonly></td>
			<td>Fecha</td>
			<td><input type="text" class="fecha" name="fecha" id="fecha" readonly value="<%= Utilidades.strDateServer()  %>"></td>
		</tr>
	</table>
	<input type="button" name="cmd_buscar" id="cmd_buscar" value="Buscar Recurso" onclick="javascript:buscar()" > <input type="button" name="cmd_buscar" value="Ver ruta" onclick="javascript:ruta()" > <input type="submit" name="cmd_download" value="KML" > <input type="hidden" name="operacion" value="download_resource" >
</form>
<div id="map_canvas"></div>
</div>
</body>
</html>
