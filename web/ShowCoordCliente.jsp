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
	
	String nic = (String)request.getParameter("nic");
	GeoPosicion geo = new GeoPosicion();
	geo = gps.CoordenasNic(nic);
	ArrayList<GeoPosicion> lista = gps.UbicacionActualRecurso();
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
	    <%  if (!geo.getLatitud().equals("0") && !geo.getLongitud().equals("0"))  { %>
		    var coordenadas = new google.maps.LatLng(<%= geo.getLatitud() %>,<%= geo.getLongitud() %>);
		    //Establecemos las coordenadas del punto
		    marker = new google.maps.Marker({ //opciones
		        position: coordenadas,
		        //Decimos que la posición es la de la variable 'coordenadas'
		        map: map,
		        //Nombre del mapa
		        icon:"images/home.png"
		        //Titulo (visible cuando colocamos el ratón sobre el punto)
		    });
		    map.setCenter(coordenadas);
			map.setZoom(13);
	   <% } %>
	   
	   <% if (lista.size() > 0) {  %>
	    var markers = [];
	    var info = [];
	    var posicion;
	    var contenido;
	    var infowindow = new google.maps.InfoWindow({
	        content: ''
	    });
	    var icono = "images/ubicacion.png"; 
	    <% for (GeoPosicion posicion: lista)  {  %>
			   	//var direccion = "";
	   	posicion = new google.maps.LatLng(<%= posicion.getLatitud() %>,<%= posicion.getLongitud()  %>);
	   	marker = new google.maps.Marker({
	        position: posicion ,
	        map:map,
	        icon: icono
	   	});
   

       contenido ="Tecnico: <STRONG> <%= posicion.getNombre()  %> </STRONG><br/>Ultimo reporte: <%= posicion.getFecha()  %><br/>Posicion (X,Y): <%= posicion.getLatitud() %>,<%= posicion.getLongitud() %><br/><a href=\"javascript:VerInfo('<%= posicion.getCedula() %>')\">Ver informacion recurso</a>";
       markers.push(marker);
       info.push(contenido);
       
       		   	
	   	<%  }   %>

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
<title>GEOREFERENCIA NIC <%= (String)request.getParameter("nic") %></title>
</head>
<body onload="javascript:inicializar()">
  <h2>UBICACION GEOREFERENCIADA DEL CLIENTE</h2>
  NIC: <%= (String)request.getParameter("nic") %><br/>
  Latitud: <%= geo.getLatitud() %> <br/>
  Longitud <%= geo.getLongitud() %>
  <div id="map_canvas" style="width:100%; height:100%"></div>
</body>
</html>

<%
	conexion.Close();
%>