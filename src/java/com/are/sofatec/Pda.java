package com.are.sofatec;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Pda {
	private String recurso;
	private String IMEI;
	private String error;
	private db conexion = null;
	public String getRecurso() {
		return recurso;
	}
	public void setRecurso(String recurso) {
		this.recurso = recurso;
	}
	public String getIMEI() {
		return IMEI;
	}
	public void setIMEI(String iMEI) {
		IMEI = iMEI;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public db getConexion() {
		return conexion;
	}
	public void setConexion(db conexion) {
		this.conexion = conexion;
	}
	public Pda(db conexion) {
		super();
		this.conexion = conexion;
	}
	
	public boolean FindPersonal(String recurso) throws SQLException {
		String sql = String.format("select recucodi from personal where recucodi='%s'",recurso);
		ResultSet rs = conexion.Query(sql);
		if (rs.next()) {
			return true;
		}else {
			return false;
		}
	}
	
	public boolean FindIMEI(String IMEI) throws SQLException {
		String sql = String.format("select IMEI from pda where IMEI='%s'",IMEI);
		ResultSet rs = conexion.Query(sql);
		if (rs.next()) {
			return true;
		}else {
			return false;
		}
	}
	
	public boolean Add() throws Exception,SQLException {
		if (!this.FindIMEI(this.IMEI)) {
			String sql = String.format("", this.recurso,this.IMEI);
			if (conexion.Update(sql) > 0 ) {
				conexion.Commit();
				return true;
			}else {
				return false;
			}
		}else {
			throw new Exception(
					"Ya se encuentra asignado el IMEI");
		}
	}
	
	public boolean Remove(String id) throws SQLException {
		String sql = String.format("delete from pda where id=%s",id);
		if (conexion.Update(sql) > 0 ) {
			return true;
		}else {
			return false;
		}
	}

}
