package com.are.sofatec;

import java.sql.SQLException;

public class HistorialCierre {
	private String recurso;
	private String orden;
	private String tipo; // R-Resuelta F-Visita fallida
	private String fecha;
	private String imei;
	
	private db conexion = null;

	public String getRecurso() {
		return recurso;
	}

	public void setRecurso(String recurso) {
		this.recurso = recurso;
	}

	public String getOrden() {
		return orden;
	}

	public void setOrden(String orden) {
		this.orden = orden;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public db getConexion() {
		return conexion;
	}

	public void setConexion(db conexion) {
		this.conexion = conexion;
	}

	public HistorialCierre(db conexion) {
		super();
		this.conexion = conexion;
	}
	
	
	public boolean Add(int mode) throws SQLException {
		boolean ret = false;
		String sql = "insert into reportes (recurso,orden,tipo,fecha,imei) values (?,?,?,sysdate(),?)";
		java.sql.PreparedStatement pst =  this.getConexion().getConnection().prepareStatement(sql);
		pst.setString(1, this.recurso);
		pst.setString(2, this.orden);
		pst.setString(3, this.tipo);
		pst.setString(4, this.imei);
		
		if (this.getConexion().Update(pst) > 0) {
			if (mode == 1) {
				this.getConexion().Commit();
			}
			ret = true;
		}
		
		return ret;
	}

}
