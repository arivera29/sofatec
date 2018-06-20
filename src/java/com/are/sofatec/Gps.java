package com.are.sofatec;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Gps {
	private String imei;
	private String latitud;
	private String longitud;
	private String fecha;
	
	

	private db conexion = null;
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	public String getLatitud() {
		return latitud;
	}
	public void setLatitud(String latitud) {
		this.latitud = latitud;
	}
	public String getLongitud() {
		return longitud;
	}
	public void setLongitud(String longitud) {
		this.longitud = longitud;
	}
	public db getConexion() {
		return conexion;
	}
	public void setConexion(db conexion) {
		this.conexion = conexion;
	}
	
	public String getFecha() {
		return fecha;
	}
	public Gps(db conexion) {
		super();
		this.conexion = conexion;
	}
	
	public boolean Add() throws SQLException {
		boolean ret = false;
		Equipos equipo = new Equipos(conexion);
		String cedula = "-1";
		if (equipo.Find(this.imei)) {
			cedula = equipo.getRecurso();
		}
		String sql = "insert into gps (imei,latitud,longitud,recurso,fecha_sistema) values (?,?,?,?,sysdate())";
		java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		pst.setString(1, this.imei);
		pst.setString(2,this.latitud);
		pst.setString(3, this.longitud);
		pst.setString(4, cedula);
		
		if (conexion.Update(pst) > 0) {
			conexion.Commit();
			ret = true;
		}
		
		return ret;
	}
	
	public boolean UltimaCoordRecursoHoy (String recurso) throws SQLException {
		boolean ret = false;
		String sql = "select latitud,longitud,fecha_sistema from gps where fecha_sistema = (select max(g.fecha_sistema) from gps g where g.recurso=? and date(fecha_sistema) = current_date() ) and recurso = ?";
		java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		pst.setString(1, recurso);
		pst.setString(2, recurso);
		ResultSet rs = conexion.Query(pst);
		if (rs.next()) {
			this.latitud = rs.getString("latitud");
			this.longitud = rs.getString("longitud");
			this.fecha = rs.getString("fecha_sistema");
			ret = true;
		}
		
		return ret;
	}
	
	public GeoPosicion UbicationNow (String recurso) throws SQLException {
		GeoPosicion geo = new GeoPosicion();
		String sql = "select latitud,longitud,fecha_sistema from gps where fecha_sistema = (select max(g.fecha_sistema) from gps g where g.recurso=? and date(fecha_sistema) = current_date() ) and recurso = ?";
		java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		pst.setString(1, recurso);
		pst.setString(2, recurso);
		ResultSet rs = conexion.Query(pst);
		if (rs.next()) {
			geo.setCedula(recurso);
			geo.setLatitud(rs.getString("latitud"));
			geo.setLongitud(rs.getString("longitud"));
			geo.setFecha(rs.getString("fecha_sistema"));
		}
		
		return geo;
	}
	
	public boolean CoordenaRecursoFecha(String recurso, String fecha_max) throws SQLException {
		boolean ret = false;
		String sql = "select latitud,longitud,fecha_sistema " +
				"from gps " +
				"where fecha_sistema = " +
					"(select max(g.fecha_sistema) " +
					"from gps g " +
					"where g.recurso=? " +
					"and fecha_sistema <= ? ) " +
					"and recurso = ? ";
		java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		pst.setString(1, recurso);
		pst.setString(2, fecha_max);
		pst.setString(3, recurso);
		ResultSet rs = conexion.Query(pst);
		if (rs.next()) {
			this.latitud = rs.getString("latitud");
			this.longitud = rs.getString("longitud");
			this.fecha = rs.getString("fecha_sistema");
			ret = true;
		}
		
		return ret;
	}
	
	public boolean CoordenasCliente(String nic) throws SQLException {
		boolean ret = false;
		String sql = "select longitud,latitud from clientes where nic=?";
		java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		pst.setString(1, nic);
		ResultSet rs = conexion.Query(pst);
		if (rs.next()) {
			this.latitud = rs.getString("latitud");
			this.longitud = rs.getString("longitud");
			ret = true;
		}
		
		return ret;
		
		
	}
	
	public GeoPosicion CoordenasNic(String nic) throws SQLException {
		GeoPosicion geo = new GeoPosicion();
		String sql = "select longitud,latitud from clientes where nic=?";
		java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		pst.setString(1, nic);
		ResultSet rs = conexion.Query(pst);
		if (rs.next()) {
			geo.setLatitud(rs.getString("latitud"));
			geo.setLongitud(rs.getString("longitud"));
			
		}else {
			geo.setLatitud("0");
			geo.setLongitud("0");
		}
		
		return geo;
	}
	
	public ResultSet RutaHoy(String recurso) throws SQLException {
		String sql = "select fecha_sistema,latitud,longitud from gps where recurso=? and date(fecha_sistema) = current_date() order by fecha_sistema";
		java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		pst.setString(1, recurso);
		ResultSet rs = conexion.Query(pst);
		return rs;
		
	}
	public ResultSet RutaDate(String recurso, String fecha) throws SQLException {
	
		String sql = "select fecha_sistema,latitud,longitud from gps where recurso=? and date(fecha_sistema) = ? order by fecha_sistema";
		java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		pst.setString(1, recurso);
		pst.setString(2, fecha);
		ResultSet rs = conexion.Query(pst);
		return rs;
	}
	/*public ResultSet UbicacionActualRecurso () throws SQLException {
		String sql = "select g.recurso, g.latitud,g.longitud,g.fecha_sistema,r.recunomb " +
				"from gps g,recurso r " +
				"where fecha_sistema = (select max(h.fecha_sistema) " +
				"from gps h where h.recurso=g.recurso " +
				"and date(h.fecha_sistema) = current_date() ) " +
				"and g.recurso = r.recucodi";
		java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		ResultSet rs = conexion.Query(pst);
		return rs;
	}*/

	public ArrayList<GeoPosicion> UbicacionActualRecurso () throws SQLException {
		ArrayList<GeoPosicion> lista = new ArrayList<GeoPosicion>();
		
		String sql = "select recurso,imei,max(id) id from gps where date(fecha_sistema) = current_date() and (TIMESTAMPDIFF(MINUTE,fecha_sistema,sysdate())) <= 60 group by recurso,imei";
		java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		ResultSet rs = conexion.Query(pst);
		while (rs.next()) {
			sql = "select gps.*,recurso.recunomb,recurso.recutele from gps,recurso where recurso=recucodi and id=?";
			pst = conexion.getConnection().prepareStatement(sql);
			pst.setString(1, rs.getString("id"));
			ResultSet rs2 = conexion.Query(pst);
			if (rs2.next()) {
				GeoPosicion posicion = new GeoPosicion();
				posicion.setCedula(rs2.getString("recurso"));
				posicion.setFecha(rs2.getString("fecha_sistema"));
				posicion.setLatitud(rs2.getString("latitud"));
				posicion.setLongitud(rs2.getString("longitud"));
				posicion.setTelefono(rs2.getString("recutele"));
				posicion.setNombre(rs2.getString("recunomb"));
				posicion.setDireccion("");
				posicion.setIMEI(rs.getString("imei"));
				lista.add(posicion);
			}
		}
		return lista;
	}
	
	public ArrayList<GeoOrden> UbicacionOrdenesPendientes() throws SQLException {
		ArrayList<GeoOrden> lista = new ArrayList<GeoOrden>();
		String sql = "select orden,nic,tipo,num_apa,nom_via,nom_calle,crucero,interior,placa,visita from orders where estado = 1";
		java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		ResultSet rs = conexion.Query(pst);
		while (rs.next()) {
			
				GeoOrden posicion = new GeoOrden();
				posicion.setOrden(rs.getString("orden"));
				posicion.setTipo(rs.getString("tipo"));
				posicion.setMedidor(rs.getString("num_apa"));
				posicion.setNic(rs.getString("nic"));
				posicion.setVisitas(rs.getString("visita"));
				posicion.setDireccion("");
				Clientes cliente = new Clientes(this.conexion);
				
				if (cliente.Find(posicion.getNic())) {
					posicion.setLatitud(cliente.getLatitud());
					posicion.setLongitud(cliente.getLongitud());
					posicion.setEnrejado(cliente.getPredioEnrejado());
					posicion.setAgresivo(cliente.getClienteAgresivo());
				}else {
					posicion.setLatitud("0");
					posicion.setLongitud("0");
					posicion.setEnrejado("0");
					posicion.setAgresivo("0");
					
				}
				
				lista.add(posicion);
		}
		
		
		return lista;
	}
	
	public ArrayList<GeoOrden> UbicacionOrdenesAsignadas(String cedula) throws SQLException {
		ArrayList<GeoOrden> lista = new ArrayList<GeoOrden>();
		String sql = "select orden,nic,tipo,num_apa,nom_via,nom_calle,crucero,interior,placa,visita from orders where estado = 2 and recurso=? order by num_show";
		java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		pst.setString(1, cedula);
		ResultSet rs = conexion.Query(pst);
		while (rs.next()) {
			
				GeoOrden posicion = new GeoOrden();
				posicion.setOrden(rs.getString("orden"));
				posicion.setTipo(rs.getString("tipo"));
				posicion.setMedidor(rs.getString("num_apa"));
				posicion.setNic(rs.getString("nic"));
				posicion.setVisitas(rs.getString("visita"));
				String direccion = rs.getString("nom_via") + " "+ rs.getString("nom_calle") + " " + rs.getString("crucero") + " " + rs.getString("placa") + " " + rs.getString("interior");
				posicion.setDireccion(direccion);
				Clientes cliente = new Clientes(this.conexion);
				
				if (cliente.Find(posicion.getNic())) {
					posicion.setLatitud(cliente.getLatitud());
					posicion.setLongitud(cliente.getLongitud());
					posicion.setEnrejado(cliente.getPredioEnrejado());
					posicion.setAgresivo(cliente.getClienteAgresivo());
				}else {
					posicion.setLatitud("0");
					posicion.setLongitud("0");
					posicion.setEnrejado("0");
					posicion.setAgresivo("0");
					
				}
				
				lista.add(posicion);
		}
		
		
		return lista;
	}

}
