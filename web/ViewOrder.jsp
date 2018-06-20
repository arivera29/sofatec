<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="validausuario.jsp"%>
<%@ page import="com.are.sofatec.*" %>
<%@ page import="java.util.*" %>
<%
        if (request.getParameter("codigo") == null) {
                response.sendRedirect("FindOrder.jsp?operacion=consulta");
                return;
        }
	
        String codigo = (String)request.getParameter("codigo");
        db conexion = new db();
        Ordenes orden = new Ordenes(conexion);
        if (!orden.Find(codigo)) {
                conexion.Close();
                response.sendRedirect("FindOrder.jsp?operacion=consulta");
                return;
        }
	
        List<visita> lista = new ArrayList<visita>();
	
        if (orden.getVisitas() > 0) { // Hay visitas fallidas
                VisitaFallida VF = new VisitaFallida(conexion);
                lista = VF.List(codigo);
        }
	
        List<imagenes> lista_imagenes = new ArrayList<imagenes>();
        GestionImagenes GI = new GestionImagenes(conexion);
        lista_imagenes = GI.List(codigo);
	
        TipoOrden tipo = new TipoOrden(conexion);
        tipo.Find(orden.getTipo());
        departamento dpto = new departamento(conexion);
        localidad loca = new localidad(conexion);
        dpto.FindByLoca(orden.getMunicipio());
        loca.Find(orden.getMunicipio());
        Estados estado = new Estados(conexion);
        estado.Find(orden.getEstado());
        recursohumano recurso = new recursohumano(conexion);
        recurso.Find(orden.getRecurso());
	
        Gps gps = new Gps(conexion);
        String latitud="";
        String longitud="";
        String fecha = "";
        boolean centrar = false;
	
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>INFORMACION ORDEN DE SERVICIO <%= (String)request.getParameter("codigo") %></title>
        <link rel="stylesheet" href="themes/redmond/jquery.ui.all.css">
        <LINK REL="stylesheet" TYPE="text/css" HREF="main.css">
        <script src="js/jquery.js" language="JavaScript"></script>
        <script src="ui/jquery.ui.core.js" language="JavaScript"></script>
        <script src="ui/jquery.ui.widget.js" language="JavaScript"></script>
        <script src="ui/jquery.ui.button.js"></script>

        <script type="text/javascript">
            $(function () {
                $("input:submit, a, button", ".demo").button();
                $("input:button, a, button", ".demo").button();
            });
        </script>
        <style type="text/css">
            #map_canvas { width: 100%; height: 500px; float: left; margin: 10px 10px }
        </style>
        <script type="text/javascript"
                src="https://maps.google.com/maps/api/js?sensor=false">
        </script>
        <script type="text/javascript">
            var map;
            var LatR, LonR, fechaR;
            function initialize() {

                var coordenadas = new google.maps.LatLng(6.708254, -72.861328);
                var myOptions = {
                    zoom: 5,
                    center: coordenadas,
                    mapTypeId: google.maps.MapTypeId.ROADMAP
                };

                var map = new google.maps.Map(document.getElementById("map_canvas"),
                        myOptions);


            <%  if (gps.CoordenasCliente(orden.getNic())) { 
                centrar = true;
            %>
                var coordenadas = new google.maps.LatLng(<%= gps.getLatitud().replace(',', '.') %>,<%= gps.getLongitud().replace(',', '.') %>);
                new google.maps.Marker({//opciones
                    position: coordenadas,
                    map: map,
                    title: "Orden " + <%= orden.getOrden() %>,
                    icon: "images/home.png"
                });
                map.setCenter(coordenadas);
                map.setZoom(15);

            <%  } %>

            <% if (orden.getEstado().equals("2")) {  %>
            <%    
                if (gps.UltimaCoordRecursoHoy(orden.getRecurso()) ) {
                latitud = gps.getLatitud().replace(',','.');
                longitud = gps.getLongitud().replace(',','.');
                fecha = gps.getFecha();
            %>
                var coordenadas = new google.maps.LatLng(<%= latitud %>,<%= longitud %>);
                new google.maps.Marker({//opciones
                    position: coordenadas,
                    map: map,
                    icon: "images/ubicacion.png"
                });

            <% if (!centrar)  { %>
                map.setCenter(coordenadas);
                map.setZoom(15);
            <% } %>


            <% }  
            }
            %>

            <% if (orden.getEstado().equals("99")) {  %>
            <%    
                if (gps.CoordenaRecursoFecha(orden.getRecurso(),orden.getFecha_cierre()) ) {
                        latitud = gps.getLatitud().replace(',','.');
                        longitud = gps.getLongitud().replace(',','.');
                        fecha = gps.getFecha();
            %>
                var coordenadas = new google.maps.LatLng(<%= latitud %>,<%= longitud %>);
                new google.maps.Marker({//opciones
                    position: coordenadas,
                    map: map,
                    icon: "images/ubicacion.png"
                });

            <% if (!centrar)  { %>
                map.setCenter(coordenadas);
                map.setZoom(15);
            <% } %>

            <% }  
            }
            %>
            }

        </script>
    </head>
    <body onload="initialize()">
        <%@ include file="header.jsp" %>
        <div class="contencenter demo">
            <h2>INFORMACION ORDEN DE SERVICIO <%= orden.getOrden() %></h2>
            <table>
                <tr>
                    <th colspan="2">INFORMACION GENERAL</th>
                </tr>
                <tr>
                    <td>No. Orden</td>
                    <td><%= orden.getOrden() %></td>
                </tr>
                <tr>
                    <td>NIC</td>
                    <td><%= orden.getNic() %></td>
                </tr>
                <tr>
                    <td>Departamento</td>
                    <td><%= dpto.getCodigo() %> - <%= (String)dpto.getDescripcion()  %></td>
                </tr>
                <tr>
                    <td>Municipio</td>
                    <td><%= orden.getMunicipio() %> - <%= (String)loca.getDescripcion()  %></td>
                </tr>
                <tr>
                    <td>Direccion</td>
                    <td><%= orden.getDireccion() %></td>
                </tr>
                <tr>
                    <td>Direccion Referencia</td>
                    <td><%= orden.getRdireccion() %></td>

                </tr>
                <tr>	
                    <td>Medidor</td>
                    <td><%= orden.getNum_apa()  %></td>
                </tr>
                <tr>	
                    <td>Marca</td>
                    <td><%= orden.getDescmarca()  %></td>		
                </tr>
                <tr>
                    <td>Tipo Orden</td>
                    <td><%= tipo.getDescripcion() %></td>
                </tr>
                <tr>
                    <td>NIS</td>
                    <td><%= orden.getNis() %></td>
                </tr>
                <tr>
                    <td>Fecha Generacion</td>
                    <td><%= orden.getFtratamiento() %></td>
                </tr>
                <tr>	
                    <td>Estado</td>
                    <td><%= estado.getDescripcion() %></td>
                </tr>
                <tr>	
                    <td>Visitas</td>
                    <td><%= orden.getVisitas() %></td>
                </tr>
                <% if (orden.getEstado().equals("2")) { // No mostrar si la orden esta GENERADA 
                %>			
                <tr>
                    <th colspan="2">INFORMACION ASIGNACION</th>
                </tr>

                <tr>
                    <td>Asignacion Actual</td>
                    <td><%= orden.getRecurso() %></td>
                </tr>
                <tr>
                    <td>Nombre</td>
                    <td><%= recurso.getNombre() %></td>
                </tr>
                <tr>
                    <td>Fecha asignacion</td>
                    <td><%= orden.getFecha_asignacion() %></td>
                </tr>
                <%    if (gps.UltimaCoordRecursoHoy(orden.getRecurso()) ) { %>
                <tr>
                    <th colspan=2>INFORMACION ULTIMA COORDENADAS GPS REPORTADA</th>
                </tr>
                <tr>
                    <td>Latitud</td>
                    <td><%= latitud %></td>
                <tr>
                <tr>
                    <td>Longitud</td>
                    <td><%= longitud %></td>
                <tr>
                <tr>
                    <td>Fecha</td>
                    <td><%= fecha %></td>
                <tr>
                    <% } %>
                    <% } %>

                    <% if (orden.getEstado().equals("99")) { %>	
                <tr>
                    <th colspan="2">INFORMACION DE EJECUCION</th>
                </tr>

                <tr>
                    <td>Fecha</td>
                    <td><%= orden.getFecha_cierre()  %></td>

                <tr>
                <tr>
                    <td>Recurso</td>
                    <td><%= recurso.getNombre() %></td>
                </tr>
                <tr>
                    <td>Observaciones</td>
                    <td><%= orden.getObservaciones() %></td>
                </tr>
                <%    if (gps.UltimaCoordRecursoHoy(orden.getRecurso()) ) { %>
                <tr>
                    <th colspan=2>Ultima posicion GPS</th>
                </tr>
                <tr>
                    <td>Latitud</td>
                    <td><%= latitud %></td>
                <tr>
                <tr>
                    <td>Longitud</td>
                    <td><%= longitud %></td>
                <tr>
                <tr>
                    <td>Fecha</td>
                    <td><%= fecha %></td>
                <tr>
                    <% } %>

                    <% } %>
            </table>

            <h2>INFORMACION VISITAS FALLIDAS</h2>
            <table>
                <tr>
                    <th>FECHA</th>
                    <th>MOTIVO</th>
                    <th>OBSERVACION</th>
                    <th>TECNICO</th>
                    <th>USUARIO</th>
                </tr>
                <% if (lista.size() > 0)  {%>		
                <% for (int i=0; i < lista.size(); i++)  {%>	
                <% visita v = lista.get(i); %>
                <tr>
                    <td><%= v.getFecha() %></td>
                    <td><%= v.getCausal() %> - <%= v.getDescripcionCausal() %></td>
                    <td><%= v.getObservacion() %></td>
                    <td><%= v.getNombreRecurso() %></td>
                    <td><%= v.getUsuario() %></td>
                </tr>
                <% } %>
                <% } else { %>
                <tr>
                    <td colspan="5">La orden no registra visitas fallidas</td>
                </tr>

                <% } %>
            </table>		


            <h2>IMAGENES REPORTADAS</h2>
            <table>
                <tr>
                    <th>FECHA</th>
                    <th>CEDULA TECNICO</th>
                    <th>NOMBRE TECNICO</th>
                    <th>NOMBRE ARCHIVO</th>
                    <th>ACCION</th>
                </tr>
                <% if (lista_imagenes.size() > 0)  {%>		
                <% for (int i=0; i < lista_imagenes.size(); i++)  {%>	
                <% imagenes img = lista_imagenes.get(i); %>
                <tr>
                    <td><%= img.getFecha() %></td>
                    <td><%= img.getRecurso() %></td>
                    <td><%= img.getNombrerecurso() %></td>
                    <td><%= img.getFilename() %></td>
                    <td><a href="imagenes/<%= img.getFilename()  %>" target="_blank">Descargar</a></td>
                </tr>
                <% } %>
                <% } else { %>
                <tr>
                    <td colspan="5">La orden no registra imagenes</td>
                </tr>

                <% } %>
            </table>	
            <H2>GEOREFERENCIA ORDEN DE SERVICIO</H2>		
            <div id="map_canvas"></div>	
        </div>
    </body>
</html>

<%
        conexion.Close();
%>