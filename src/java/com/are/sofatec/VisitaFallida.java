package com.are.sofatec;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class VisitaFallida {
	
	private db conexion = null;
	private visita v=null;
	public db getConexion() {
		return conexion;
	}
	public void setConexion(db conexion) {
		this.conexion = conexion;
	}
	public VisitaFallida(db conexion,visita v) {
		super();
		this.conexion = conexion;
		this.v = v;
	}
	public VisitaFallida(db conexion) {
		super();
		this.conexion = conexion;
	}
	

	public visita getV() {
		return v;
	}
	public void setV(visita v) {
		this.v = v;
	}
	
	public boolean Add() throws SQLException {
		if (v == null) return false;
		
		boolean result = false;
		String sql = "insert into visitafallida (orden,causal,observacion,usuario,recurso,fecha) values (?,?,?,?,?,sysdate())";
		java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		pst.setString(1, v.getOrden());
		pst.setString(2, v.getCausal());
		pst.setString(3, v.getObservacion());
		pst.setString(4, v.getUsuario());
		pst.setString(5, v.getRecurso());
		if (conexion.Update(pst) > 0) {
			if (this.IncCountVisita(v.getOrden())) { // incrementar consecutivo visita y liberar
				
				HistorialCierre hc = new HistorialCierre(this.conexion);
				hc.setOrden(v.getOrden());
				hc.setRecurso(v.getRecurso());
				hc.setImei("-1");
				hc.setTipo("F");
				if (hc.Add(0)) {
					conexion.Commit();
					result = true;
				}
			}
		}
	return result;
	}
	
	public boolean Add(boolean fecha) throws SQLException {
		if (v == null) return false;
		
		boolean result = false;
		String sql = "insert into visitafallida (orden,causal,observacion,usuario,recurso,fecha) values (?,?,?,?,?,sysdate())";
		if (fecha) {
			sql = "insert into visitafallida (orden,causal,observacion,usuario,recurso,fecha) values (?,?,?,?,?,?)";
		}
		java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		pst.setString(1, v.getOrden());
		pst.setString(2, v.getCausal());
		pst.setString(3, v.getObservacion());
		pst.setString(4, v.getUsuario());
		pst.setString(5, v.getRecurso());
		if (fecha) {
			pst.setString(6, v.getFecha());
		}
		
		if (conexion.Update(pst) > 0) {
			if (this.IncCountVisita(v.getOrden())) { // incrementar consecutivo visita y liberar
				
				HistorialCierre hc = new HistorialCierre(this.conexion);
				hc.setOrden(v.getOrden());
				hc.setRecurso(v.getRecurso());
				hc.setImei("-1");
				hc.setTipo("F");
				if (hc.Add(0)) {
					conexion.Commit();
					result = true;
				}
			}
		}
		return result;
	}
	
	private boolean IncCountVisita(String orden) throws SQLException {
		boolean result = false;
		String sql = "update orders set visita=visita+1,recurso='-1',confirm=0,fecha_asignacion=null,date_confirm=null,num_show=0,estado=1 where orden=?";
		java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		pst.setString(1, orden);
		if (conexion.Update(pst) > 0) {
			result = true;
		}
	return result;
	}
	
	public ArrayList<visita> List(String orden) throws SQLException {
		ArrayList<visita> lista = new ArrayList<visita>();
		String sql = "select orden,causal,causdesc,observacion,usuario,fecha,recurso,recunomb from visitafallida,causales,recurso where causal=causcodi and recurso=recucodi and orden=? order by fecha";
		java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		pst.setString(1, orden);
		ResultSet rs = conexion.Query(pst);
		while (rs.next()) {
			visita v = new visita();
			v.setOrden(rs.getString("orden"));
			v.setCausal(rs.getString("causal"));
			v.setDescripcionCausal(rs.getString("causdesc"));
			v.setUsuario(rs.getString("usuario"));
			v.setRecurso(rs.getString("recurso"));
			v.setNombreRecurso(rs.getString("recunomb"));
			v.setFecha(rs.getString("fecha"));
			v.setObservacion(rs.getString("observacion"));
			lista.add(v);
		}
		rs.close();
		return lista;
	}
	public int CountVisitas(String orden) throws SQLException {
		int count =0;
		String sql = "select count(*) contador from visitafallida where orden = ?";
		java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		pst.setString(1, orden);
		ResultSet rs = conexion.Query(pst);
		if (rs.next()) {
			count = rs.getInt("contador");
		}
		rs.close();
		return count;
	}
 }
