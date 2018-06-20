<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>SOFATEC</title>
<LINK REL="stylesheet" TYPE="text/css" HREF="main.css">
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
<style type="text/css">
  #map_canvas { width: 100%; height: 600px; margin-left:5px;margin-top: 5px; }
</style>
</head>
<body onload="javascript:initialize();">
<%@ include file="header.jsp"%>
<div class="contencenter demo">
<div id="map_canvas"></div>
</div>
</body>
</html>