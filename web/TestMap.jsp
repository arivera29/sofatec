<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
<style type="text/css">
  html { height: 100% }
  body { height: 100%; margin: 0px; padding: 0px }
  #map_canvas { height: 100% }
</style>
<script type="text/javascript"
    src="https://maps.google.com/maps/api/js?sensor=false">
</script>
<script type="text/javascript">

  function initialize() {
	    var options = {
	        zoom: 9
	        , center: new google.maps.LatLng(18.2, -66.3)
	        , mapTypeId: google.maps.MapTypeId.ROADMAP
	    };
	  
	    var map = new google.maps.Map(document.getElementById('map'), options);
	 
	    var southWest = new google.maps.LatLng(11.020058,-74.837494);
	    var northEast = new google.maps.LatLng(10.923663,-74.75647);
	    var lngSpan = northEast.lng() - southWest.lng();
	    var latSpan = northEast.lat() - southWest.lat();
	 
	    for(var i=1; i<=1000; i++){
	        var lat = southWest.lat() + latSpan * Math.random();
	        var lng = southWest.lng() + lngSpan * Math.random();
	        document.write( lat + "," + lng + "<br/>");
	        var latlng = new google.maps.LatLng(lat, lng);
	        var marker = new google.maps.Marker({
	            position: latlng,
	            map: map
	        });

	    }
  }

</script>
<title>Insert title here</title>
</head>
<body onload="initialize()">

  
<div id="map" style="width:100%; height:100%"></div>
</body>
</html>