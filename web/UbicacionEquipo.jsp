<%@page import="com.are.sofatec.db"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="validausuario.jsp"%>
<%@ page import="java.sql.*"%>
<%
	String imei = (String)request.getParameter("imei");
	String latitud = "0";
	String longitud = "0";
	String fecha = "";
	db conexion = new db();
	
	String sql = "select latitud,longitud,fecha_sistema from gps where date(fecha_sistema) = current_date() and imei=? order by fecha_sistema desc limit 1";
	PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
	pst.setString(1, imei);
	ResultSet rs = conexion.Query(pst);
	if (rs.next()) {
		latitud = rs.getString("latitud");
		longitud = rs.getString("longitud");
		fecha = rs.getString("fecha_sistema");
	}
	
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Ubicacion Equipo <%= (String)request.getParameter("imei") %></title>
<link rel="stylesheet" href="themes/redmond/jquery.ui.all.css">
<link rel="stylesheet" TYPE="text/css" HREF="main.css">
<script src="js/jquery.js" type="text/javascript"></script>
<script src="ui/jquery.ui.core.js" language="JavaScript"></script>
<script src="ui/jquery.ui.widget.js" language="JavaScript"></script>
<script src="ui/jquery.ui.button.js"></script>
<script type="text/javascript" src="https://maps.google.com/maps/api/js?sensor=false"></script>
<script type="text/javascript" src="http://google-maps-utility-library-v3.googlecode.com/svn/trunk/markerclusterer/src/markerclusterer_compiled.js"></script>
<style type="text/css">
  #map_canvas { width: 780px; height: 400px; margin-top: 10px }
</style>

<script type="text/javascript">
var map;
var geocoder;
function initialize() {
	geocoder = new google.maps.Geocoder();
	var latlng = new google.maps.LatLng(<%= latitud.replace(",",".") %>,<%= longitud.replace(",",".") %>);
    var myOptions = {
      zoom: 13,
      center: latlng,
      mapTypeId: google.maps.MapTypeId.ROADMAP
    };
    map = new google.maps.Map(document.getElementById("map_canvas"),
        myOptions);
    
    <%  if (!latitud.equals("0")  && !longitud.equals("0")) {  %>
	var marker = new google.maps.Marker({ //opciones
	       position: latlng,
	       map:map,
	       icon: "images/ubicacion.png"
	});
	
	var contenido ="<B>Informacion Posición</b><br/>Fecha Posicionamiento: <b><%=  fecha %></b><br/>Coordenadas: <b><%= latitud %>,<%= longitud %></b>" ;
	
	var infowindow = new google.maps.InfoWindow({
        content: ''
    });
	
	(function(marker, contenido){                       
        google.maps.event.addListener(marker, 'click', function() {
            infowindow.setContent(contenido);
            infowindow.open(map, marker);
        });
    })(marker,contenido);
	<% } %>

}
</script>
</head>
<body onload="javascript:initialize();">
<h2>Ubicacion</h2>
<a href = "javascript:window.close()" id ="btn_close">Cerrar</a>
<div id="map_canvas"></div>
</body>
</html>
<%
	conexion.Close();
%>
