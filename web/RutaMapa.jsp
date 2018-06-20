<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="validausuario.jsp"%>
<%@ page import="com.are.sofatec.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.io.*" %>
<%@ page import="com.csvreader.CsvReader" %>
<%
	ServletContext servletContext = getServletContext();

	String filename = (String)request.getParameter("file");
	String separador = (String)request.getParameter("separador");
	String segmentar = (String)request.getParameter("segmentar");
	String radio = (String)request.getParameter("radio");
	
	String path = servletContext.getRealPath("/upload") +   File.separator +filename;
	CsvReader reader = null;
	db conexion = new db();
	
	reader = new CsvReader(path);
	
	 
	 
	 if (separador.equals("1")) {
		 reader.setDelimiter('	');  // tabulador
	 }else if (separador.equals("2")) {
		 reader.setDelimiter(','); // coma
	 }else if (separador.equals("3")){
		 reader.setDelimiter(';');  // punto y coma
	 }else {
		 reader.setDelimiter(','); 
	 }
	 
	 reader.readHeaders();
	 
	 String[] headers = reader.getHeaders();
	 if (headers.length != 1) { // estan las columnas OK.
		  throw  new IOException("El archivo solo debe contener una (1) columna con la orden de servicio");
	 }
	 
	 conexion = new db();
	 ArrayList<datos> ordenes = new ArrayList<datos>();
	 
	 while(reader.readRecord()){
		 	
			datos os = new datos();
		 
		 	os.setOrden(reader.get(0));  // Numero de la orden de servicio
			
			Ordenes orden = new Ordenes(conexion);
			try {
				if (orden.Find(reader.get(0))) {
					
					os.setNic(orden.getNic());
					os.setTipo(orden.getTipo());
					os.setDireccion(orden.getDireccion()); 
					os.setBarrio(orden.getLocalidad());
					os.setRuta(Integer.parseInt(orden.getRuta()));
					os.setAol(Integer.parseInt(orden.getAol()));
					os.setItinerario(Integer.parseInt(orden.getItin()));
					
					// Consultando las coordenadas del cliente
					String sql = "SELECT CLIENTE.NIF,COORDENADAS.LATITUD,COORDENADAS.LONGITUD FROM CLIENTE,COORDENADAS WHERE NIC=? AND CLIENTE.NIF = COORDENADAS.NIF AND LONGITUD !=0 AND LATITUD != 0";
					java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
					pst.setString(1, orden.getNic());
					ResultSet rs = conexion.Query(pst);
					if (rs.next()) {
						os.setNif((String)rs.getString("NIF"));
						os.setLatitud(rs.getDouble("LONGITUD"));
						os.setLongitud(rs.getDouble("LATITUD"));
						os.setEstado(0);
					}else {
						os.setObservacion("INFORMACION GEO-REFERENCIA NO ENCONTRADA");
						os.setEstado(1);
					}
					
				}else {
					os.setObservacion("ORDEN DE SERVICIO NO ENCONTRADA");
					os.setEstado(1);
				}
			}catch (SQLException e) {
				os.setObservacion(e.getMessage());
				os.setEstado(1);
			}catch (NumberFormatException e) {
				os.setObservacion(e.getMessage());
				os.setEstado(1);
			}
			
			ordenes.add(os);
			
			//out.println(String.format("%s|%s|%s|%s|%s|%s|%s|%s", num_os,tipo_os,direccion,nic,nif,latitud,longitud,observacion));
			
		 
	 }
	 
	 ArrayList<datos> ruta = new ArrayList<datos>();
	 double lat_inicial = 11.00791332927005;
	 double lon_inicial = -74.78941372958225;
	 int Ruta =0;
	 String Barrio = "";
	 boolean seguir = true; 
	 
	 datos menor = new datos();
	 // Procesando ruta
	 while (seguir) {

		 
		 // Calculando Distancias
		ordenes = Utilidades.CalcularDistancia(lat_inicial, lon_inicial, ordenes);
		// Buscar la mas cercana
		int index = Utilidades.MasCercana(ordenes,Barrio);
		
				
		if (index != -1) {
			ordenes.get(index).setEstado(2);
			ruta.add(ordenes.get(index));
			
			// Nueva posicion de referencia
			lat_inicial = ordenes.get(index).getLatitud();
			lon_inicial = ordenes.get(index).getLongitud();
	
			Barrio = ordenes.get(index).getBarrio();
			Ruta = ordenes.get(index).getRuta();
			
			if (segmentar.equals("1")) {
			Utilidades.QuitarSegmentos(ordenes);
			// Calculando Distancias con la nueva poscion de referencia
			double R = Double.parseDouble(radio);
			ordenes = Utilidades.CalcularDistancia(lat_inicial, lon_inicial, ordenes);
			// Segmentar
			int cnt_segmento = 0;
			for (int x=0;x < ordenes.size(); x++ ) {
			 if (ordenes.get(x).getEstado() == 0 ) {
					if (ordenes.get(x).getDistancia() <= R) { // ordenes cercanas en un radio de 2 Km.
						ordenes.get(x).setSegmento(1);
						cnt_segmento++;
					}
			 }
			}
			 
			 //Calcular distancia segmento
			 	if (cnt_segmento > 0) {
				 boolean pasa = true;
				 while (pasa) {
					
						// Calculando Distancias
						ordenes = Utilidades.CalcularDistanciaSegmento(lat_inicial, lon_inicial, ordenes);
						
						// Buscar la mas cercana
						
						int i = Utilidades.MasCercanaSegmento(ordenes);
						
						if (i != -1) {
							ordenes.get(i).setEstado(2);
							ordenes.get(i).setSegmento(0);
							ruta.add(ordenes.get(i));
							
							lat_inicial = ordenes.get(i).getLatitud();
							lon_inicial = ordenes.get(i).getLongitud();
							
						}
					 
					 
					 
					 int contador = 0;
					 for (datos os : ordenes) {
						 if (os.getEstado() == 0 && os.getSegmento()== 1) {
							 contador ++;
						 }
					 }
					 if (contador == 0) {
						 pasa = false;
					 }
				 }
			 
			 	}
		  
			
			
			}
		}
		 
		 

		 int contador = 0;
		 for (datos os : ordenes) {
			 if (os.getEstado() == 0) {
				 contador ++;
			 }
		 }
		 if (contador == 0) {
			 seguir = false;
		 }
		 
	 }
	 
	 double total_distancia = 0;
	 for (datos os : ruta) {
		 total_distancia += os.getDistancia();
	 }
	
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
		var latlng = new google.maps.LatLng(11.00791332927005,-74.78941372958225);
	    var myOptions = {
	      zoom: 13,
	      center: latlng,
	      mapTypeId: google.maps.MapTypeId.ROADMAP
	    };
	    
	    map = new google.maps.Map(document.getElementById("map_canvas"),
	            myOptions);
	    
	    var posicion;
	    
	   	posicion = new google.maps.LatLng(11.00791332927005,-74.78941372958225);
	   	marker = new google.maps.Marker({
	        position: posicion ,
	        map:map,
	        icon: "images/home.png"
	   	});
	    
	    <% if (ruta.size() > 0)  {  %>
			   	
	   	
	    var markers = [];
	    var info = [];
	    
	    var contenido;
	    var infowindow = new google.maps.InfoWindow({
	        content: ''
	    });
	    var icono; 
		<% int contador = 1; %>
	    <% for (datos posicion : ruta)  {  %>
	    	icono = "images/marker<%= contador %>.png";
		   	posicion = new google.maps.LatLng(<%= posicion.getLatitud() %>,<%= posicion.getLongitud()  %>);
		   	marker = new google.maps.Marker({
		        position: posicion ,
		        map:map,
		        icon: icono
		   	});
	       contenido ="Orden: <STRONG> <%= posicion.getOrden()  %> </STRONG><br/>Tipo: <%= posicion.getTipo()  %><br/>Direccion: <%= posicion.getDireccion()  %><br/>Barrio: <%= posicion.getBarrio()  %>";
	       markers.push(marker);
	       info.push(contenido);
	       <% contador++; %>
	       
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
<title>RUTA SUGERIDA</title>
</head>
<body onload="javascript:inicializar()">
  <h2>RUTA SUGERIDA</h2>
  <h3>Todal distancia a recorrer: <%= Utilidades.Redondear(total_distancia, 2) %></h3>
  <div id="map_canvas" style="width:100%; height:100%"></div>
</body>
</html>

<%
	conexion.Close();
%>