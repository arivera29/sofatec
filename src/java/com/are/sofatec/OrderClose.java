package com.are.sofatec;

import java.sql.SQLException;

public class OrderClose extends Ordenes {


	private String accion; // R: Resuelta A: Anulada
	private String actividad;
	private String observacion;	
	private String imei;
	private String lectura;
	private String tiposello;
	private int cntsello;
	private String tipoacometida;
	private double cntacometida;
	
	
	public String getTiposello() {
		return tiposello;
	}

	public void setTiposello(String tiposello) {
		this.tiposello = tiposello;
	}

	public int getCntsello() {
		return cntsello;
	}

	public void setCntsello(int cntsello) {
		this.cntsello = cntsello;
	}

	public String getTipoacometida() {
		return tipoacometida;
	}

	public void setTipoacometida(String tipoacometida) {
		this.tipoacometida = tipoacometida;
	}

	public double getCntacometida() {
		return cntacometida;
	}

	public void setCntacometida(double cntacometida) {
		this.cntacometida = cntacometida;
	}

	public String getImei() {
		return imei;
	}

	public String getLectura() {
		return lectura;
	}

	public void setLectura(String lectura) {
		this.lectura = lectura;
	}

	public void setImei(String imei) throws SQLException {
		this.imei = imei;
		Equipos equipo = new Equipos(this.getConexion());
		if (equipo.Find(imei)) {
			this.setRecurso(equipo.getRecurso());
		}else {
			this.setRecurso("-1");
		}
	}

	public OrderClose(db conexion) {
		super(conexion);
		// TODO Auto-generated constructor stub
	}

	public String getAccion() {
		return accion;
	}

	public void setAccion(String accion) {
		this.accion = accion;
	}

	public String getActividad() {
		return actividad;
	}

	public void setActividad(String actividad) {
		this.actividad = actividad;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public boolean CloseOrder() throws SQLException {
		boolean ret = false;
		String sql = "update orders set accion=?, actividad=?, observacion=?, fecha_cierre=sysdate(), equipo=?, recurso_cierre=?,lectura = ?, estado=99, tipo_sello=?, cantidad_sello=?, tipo_acometida=?, cantidad_acometida=? where orden=? and estado != 99";
		java.sql.PreparedStatement pst =  this.getConexion().getConnection().prepareStatement(sql);
		pst.setString(1, this.accion);
		pst.setString(2, this.actividad);
		pst.setString(3, this.observacion);
		pst.setString(4, this.imei);
		pst.setString(5, this.getRecurso());
		pst.setString(6, this.lectura);
		pst.setString(7, this.tiposello);
		pst.setInt(8, this.cntsello);
		pst.setString(9, this.tipoacometida);
		pst.setDouble(10, this.cntacometida);
		pst.setString(11, this.getOrden());
		
		if (this.getConexion().Update(pst) > 0) {
			HistorialCierre hc = new HistorialCierre(this.getConexion() );
			hc.setOrden(this.getOrden());
			hc.setRecurso(this.getRecurso());
			hc.setTipo("R");
			hc.setImei(this.imei);
			if (hc.Add(0)) {
				this.getConexion().Commit();
			}
			ret = true;
		}
		
		return ret;
	}
	
	
}
