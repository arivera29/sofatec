package com.are.sofatec;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ManejadorBarrios{
	private Barrio barrio;
	private db conexion = null;

	public ManejadorBarrios(db conexion) {
		this.conexion = conexion;
		// TODO Auto-generated constructor stub
	}


	public Barrio getBarrio() {
		return barrio;
	}

	public void setBarrio(Barrio barrio) {
		this.barrio = barrio;
	}

	public boolean add() throws SQLException {
		boolean result = false;
		String sql = "insert into barrios (localidad,nombre,peso) values (?,?,?)";
		java.sql.PreparedStatement pst = conexion.getConnection()
				.prepareStatement(sql);
		pst.setString(1, barrio.getLocalidad());
		pst.setString(2, barrio.getNombre());
		pst.setInt(3, barrio.getPeso());
		
		if (conexion.Update(pst) > 0) {
			conexion.Commit();
			result = true;
		}
		return result;
	}

	public boolean remove(String id) throws SQLException {
		boolean result = false;
		String sql = "delete from barrios where id=?";
		java.sql.PreparedStatement pst = conexion.getConnection()
				.prepareStatement(sql);
		pst.setString(1, id);
		if (conexion.Update(pst) > 0) {
			conexion.Commit();
			result = true;
			
		}
		return result;
	}

	public boolean update(String id) throws SQLException {
		boolean result = false;
		String sql = "update barrios set localidad=?,nombre=?,peso=? where id=?";
		java.sql.PreparedStatement pst = conexion.getConnection()
				.prepareStatement(sql);
		pst.setString(1, barrio.getLocalidad());
		pst.setString(2, barrio.getNombre());
		pst.setInt(3, barrio.getPeso());
		pst.setString(4, id);
		
		if (conexion.Update(pst) > 0) {
			conexion.Commit();
			result = true;
		}
		return result;
	}
	
	public ArrayList<Barrio> list(String localidad) throws SQLException {
		ArrayList<Barrio> lista = new ArrayList<Barrio>();
		
		String sql = "select * from barrios where localidad = ? order by peso";
		java.sql.PreparedStatement pst = conexion.getConnection()
				.prepareStatement(sql);
		pst.setString(1, localidad);
		ResultSet rs = conexion.Query(pst);		
		while (rs.next()) {
				Barrio barrio = new Barrio();
				barrio.setId(rs.getInt("id"));
				barrio.setLocalidad(rs.getString("localidad"));
				barrio.setNombre(rs.getString("nombre"));
				barrio.setPeso(rs.getInt("peso"));
				lista.add(barrio);
		
		}
		return lista;
	}

}
