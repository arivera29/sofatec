<%@page import="com.are.sofatec.*"%>
<%@page import="java.sql.*"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="validausuario.jsp"%>

<%
	String id = (String)request.getParameter("sitio");
	db conexion = new db();

	String sql = "select * from sitios where id=?";
	PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
	pst.setString(1, id);
	ResultSet rs = conexion.Query(pst);
	boolean rsIsEmpty = !rs.next();
	


%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Actualizar Sitio</title>
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

$(function() {
	$( ".boton" ).button();
	
});
</script>
<script type="text/javascript">
var map;
var geocoder;
var marker;
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
    
  
    google.maps.event.addListener(map, "click", function(evento) {
		//Obtengo las coordenadas separadas
		var latitud = evento.latLng.lat();
		var longitud = evento.latLng.lng();
		
		$("#longitud").val(longitud);
		$("#latitud").val(latitud);
		
		
		
		//Puedo unirlas en una unica variable si asi lo prefiero
				
		//Creo un marcador utilizando las coordenadas obtenidas y almacenadas por separado en "latitud" y "longitud"
		var coordenadas = new google.maps.LatLng(latitud, longitud); /* Debo crear un punto geografico utilizando google.maps.LatLng */
		if (!marker) {
			marker = new google.maps.Marker({position: coordenadas,map: map, animation: google.maps.Animation.DROP});
		}else {
			marker.setPosition(coordenadas);
		}
	}); //Fin del evento
    
}

function codeAddress() {
	  var address = document.getElementById('address').value;
	  geocoder.geocode( { 'address': address}, function(results, status) {
	    if (status == google.maps.GeocoderStatus.OK) {
	      map.setCenter(results[0].geometry.location);
	      var coordenadas = results[0].geometry.location;
	      
	      $("#longitud").val(coordenadas.lng());
		  $("#latitud").val(coordenadas.lat());
	      
	      if (!marker) {
	      marker = new google.maps.Marker({
	          map: map,
	          position: coordenadas
	      });
	      }else {
	    	  marker.setPosition(coordenadas); 
	      }
	    } else {
	      alert('Geocode was not successful for the following reason: ' + status);
	    }
	  });
	}

function ActualizarSitio(id) {
	
	var nombre = $("#nombre").val();
	var descripcion = $("#descripcion").val();
	var latitud = $("#latitud").val();
	var longitud = $("#longitud").val();
	
	if (nombre == "") {
		alert("Debe ingresar el nombre del sitio");
		return;
	}
	
	if (latitud == "" || longitud == "") {
		alert("Ingresar las coordenadas");
		return;
	}
	
	if (latitud == "0" || longitud == "0") {
		alert("Coordenadas no válidas");
		return;
	}
	
	if (confirm("Esta seguro de actualizar el Sitio ")) {
		var cmd = document.getElementById("cmd_update");
		cmd.disabled = true;
		$("#info").html("Procesando solicitud <img src='images/loading.gif'>");
		
		$.post("ControllerSitio.jsp",{
				operacion : "actualizar",
				nombre: nombre,
				descripcion : descripcion,
				latitud : latitud,
				longitud : longitud,
				id: id
			},procesar);
		
	}
	
}

function procesar(resultado) {
	var cmd = document.getElementById("cmd_update");
	cmd.disabled = false;
	if (resultado.trim() == 'OK') {
		alert("Sitio actualizado correctamente");
		window.opener.location.reload();
		window.close();
	}else {
		$("#info").html("<img src='images/error.png'>" + resultado);
	}
}

</script>
</head>
<body onload="javascript:initialize();">
<h2>Agregar Sitio</h2>
<div id="info"></div>
<% if (!rsIsEmpty)  { %>
<div>
	<form name="form1" action ="">
	<table>
		<tr>
			<th colspan="4">Informacion Sitio</th>
		</tr>
		<tr>
			<td>Nombre</td>
			<td colspan="3"><input type="text" name="nombre" id="nombre" size=40 value = "<%= (String)rs.getString("nombre") %>"></td>
		</tr>
		<tr>
			<td>Descripcion</td>
			<td colspan="3"><textarea rows="3" cols="40" name="descripcion" id="descripcion"><%= (String)rs.getString("descripcion") %></textarea></td>
		</tr>
		<tr>
			<td>Latitud</td>
			<td><input type="text" id="latitud" name="latitud" value = "<%= (String)rs.getString("latitud") %>"></td>
			<td>Longitud</td>
			<td><input type="text" id="longitud" name="longitud" value = "<%= (String)rs.getString("longitud") %>"></td>
		</tr>
	</table>
	<input type="button" class="boton" id="cmd_update" value="Agregar" onclick="javascript:ActualizarSitio('<%= (String)rs.getString("id") %>')" class="boton" >
	</form>
</div>
<h2>Busqueda direccion</h2>
Direccion: <input type="text" id="address"  size="50" name="address" value =""> 
<input type="button" class="boton" value="Buscar" onclick="javascript:codeAddress()" class="boton">

<div id="map_canvas"></div>
<% } %>
</body>
</body>
</html>
