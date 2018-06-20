<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ include file="validausuario.jsp"%>
<%@ page import="com.are.sofatec.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.sql.*" %>
<%
	
	db conexion = new db();
	Gps gps = new Gps(conexion);
	
	ArrayList<GeoPosicion> lista = gps.UbicacionActualRecurso();
	
%>
<html>
<head>
<title>UBICACION RECURSO</title>
<LINK REL="stylesheet" TYPE="text/css" HREF="main.css">
<script src="js/jquery.js"></script>
<style type="text/css">
  #map_canvas { width: 100%; height: 500px; float: left; margin-top: 10px; margin-left:10px }
</style>
<script type="text/javascript" src="https://maps.google.com/maps/api/js?sensor=false"></script>
<script type="text/javascript" src="http://google-maps-utility-library-v3.googlecode.com/svn/trunk/markerclusterer/src/markerclusterer_compiled.js"></script>
<script type="text/javascript">
var map;
var geocoder;
function initialize() {
    var latlng = new google.maps.LatLng(6.708254,-72.861328);
    var myOptions = {
      zoom: 5,
      center: latlng,
      mapTypeId: google.maps.MapTypeId.ROADMAP
    };
    map = new google.maps.Map(document.getElementById("map_canvas"),
        myOptions);
    var i=1;
    <% if (lista.size() > 0) {  %>
	    var markers = [];
	    var info = [];
	    var infowindow = new google.maps.InfoWindow({
	        content: ''
	    });
	    var icono = "images/ubicacion.png"; 
	    <% for (GeoPosicion posicion: lista)  {  %>
			   	//var direccion = "";
	   	posicion = new google.maps.LatLng(<%= posicion.getLatitud() %>,<%= posicion.getLongitud()  %>);
	   	var marker = new google.maps.Marker({ //opciones
	        position: posicion ,
	        map:map,
	        icon: icono
	   	});
    

       	var contenido ="Tecnico: <STRONG> <%= posicion.getNombre()  %> </STRONG><br/>Ultimo reporte: <%= posicion.getFecha()  %><br/>Posicion (X,Y): <%= posicion.getLatitud() %>,<%= posicion.getLongitud() %><br/>IMEI: <%= posicion.getIMEI() %><br/><a href=\"javascript:VerInfo('<%= posicion.getCedula() %>')\">Ver informacion recurso</a>";
        markers.push(marker);
        info.push(contenido);
        
        if (i==1) {
			map.setCenter(posicion);
			map.setZoom(13);
			
		}
		i++;
		   	
	   	<%  }   %>

	   	var markerclusterer = new MarkerClusterer(map, markers);
	   	
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

	   	
	   	<%  } %> 
    
}

function VerInfo(recurso) {
	var url= "InfoRecurso.jsp?recurso=" + recurso;
	window.open(url , "ShowInfoRecurso" , "width=400,height=300,scrollbars=YES,menubar=No,toolbar=NO,status=YES"); 
}



</script>
<% if (request.getParameter("autorefresh") != null) { %>
<script type="text/javascript">
function ReloadPage() { 
	   location.reload();
	};

	$(document).ready(function() {
	  setTimeout("ReloadPage()", <%= (String)request.getParameter("tiempo") %>*1000*60); 
	});
</script>
<% } %>
</head>
<body onload="initialize()">
<%@ include file="header.jsp" %>
<div class="contencenter demo">
<h2>UBICACION RECURSO</h2>
<p><strong>TECNICOS ACTIVOS:</strong> <%= lista.size() %> | <strong>HORA SERVER:</strong> <%= Utilidades.strTimeServer() %></p>
<form name="form1" action="" method="get">
	<input type="checkbox" value="1" name="autorefresh" <%= (request.getParameter("autorefresh")==null?"":"checked") %> >Auto-Refrescar Tiempo(Minutos): <input type="text" name="tiempo" value="<%= (request.getParameter("tiempo")==null?"10":(String)request.getParameter("tiempo")) %>" size=10> <input type="submit" name="cmd_auto_refresh" value="Configurar">
</form>
<div id="map_canvas"></div>
</div>
</body>
</html>
<%  conexion.Close(); %>
