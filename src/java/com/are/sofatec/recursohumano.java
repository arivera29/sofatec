package com.are.sofatec;

import java.sql.ResultSet;
import java.sql.SQLException;

public class recursohumano {

	private db conexion = null;
	private String codigo;
	private String nombre;
	private String direccion;
	private String telefono;
	private String correo;
	private String cargo;
	private String estado;
	private String error;
	private String key;
	private String rol;
	
	
	public String getRol() {
		return rol;
	}
	public void setRol(String rol) {
		this.rol = rol;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getKey() {
		return key;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion.trim().toUpperCase();
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public String getCorreo() {
		return correo;
	}
	public void setCorreo(String correo) {
		this.correo = correo;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public recursohumano() {
		this.codigo = "";
		this.nombre = "";
		this.error = "";
		this.direccion = "";
		this.correo = "";
		this.key = "";
		this.estado = "";
	}
	public recursohumano(db cnt) {
		this.conexion = cnt;
		this.codigo = "";
		this.nombre = "";
	}
	
	public db getConexion() {
		return conexion;
	}
	public void setConexion(db conexion) {
		this.conexion = conexion;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo.trim();
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre.trim().toUpperCase();
	}
	
	public String getCargo() {
		return cargo;
	}
	public void setCargo(String cargo) {
		this.cargo = cargo;
	}
	public boolean add(){
		boolean result = false;
		try {
			String sql = "insert into recurso (recucodi,recunomb,recudire,recutele,recumail,recucarg,recuesta,recurol) values " +
					"('" + this.codigo + "'," +
							"'" + this.nombre + "'," +
							"'" + this.direccion + "'," +
							"'" + this.telefono + "'," +
							"'" + this.correo + "'," +
							"'" + this.cargo + "'," +
							"'" + this.estado + "'," +
							"'" + this.rol + "'" +
									")";
			if (conexion.Update(sql) > 0) {
				// Habilitando el proyecto al recurso humano
					conexion.Commit();
				result = true;
			}
			
		}catch (SQLException ex) {
			this.error = ex.getMessage();
		}
		return result;
	}
	
	public boolean modify(String key) {
		boolean result = false;
		try {
			String sql = "update recurso set " +
					"recucodi='" + this.codigo + "'," +
					"recunomb='" + this.nombre + "'," +
					"recudire='" + this.direccion + "', " +
					"recutele='" + this.telefono + "'," +
					"recumail='" + this.correo + "'," +
					"recucarg='" + this.cargo + "'," +
					"recuesta='" + this.estado + "', " +
					"recurol='" + this.rol + "' " +
					"where recucodi='" + key + "'";
			
			if (conexion.Update(sql) > 0) {
				conexion.Commit();
				result = true;
			}
			
		}catch (SQLException ex) {
			this.error = ex.getMessage();
		}
		return result;	
	}
	
	public boolean remove() {
		boolean result = false;
		try {
			String sql = "delete from recurso where recucodi='" + this.key + "'";
			if (conexion.Update(sql) > 0) {
				conexion.Commit();
				result = true;
			}
			
		}catch (SQLException ex) {
			this.error = ex.getMessage();
		}
		return result;	
	}
	
	public boolean exist() {
		boolean result = false;
		this.error="";
		try {
			String sql = "select recucodi,recunomb,recudire,recutele,recumail,recucarg,recuesta,recurol from recurso where recucodi='" + this.key + "'";
			ResultSet rs = conexion.Query(sql);
			if (rs.next()) {
				this.codigo = (String)rs.getString("recucodi");
				this.nombre = (String)rs.getString("recunomb");
				this.direccion = (String)rs.getString("recudire");
				this.telefono = (String)rs.getString("recutele");
				this.correo = (String)rs.getString("recumail");
				this.cargo = (String)rs.getString("recucarg");
				this.estado = (String)rs.getString("recuesta");
				this.rol = (String)rs.getString("recurol");
				result = true;
			}
			rs.close();
			
		}catch (SQLException ex) {
			this.error = ex.getMessage();
		}
		return result;	
	}
	
	public boolean exist(String id) {
		boolean result = false;
		this.error = "";
		try {
			String sql = "select recucodi from recurso where recucodi='" + id + "'";
			ResultSet rs = conexion.Query(sql);
			if (rs.next()) {
				result = true;
			}
			rs.close();
			
		}catch (SQLException ex) {
			this.error = ex.getMessage();
		}
		return result;	
	}
	
	public String CreateSelectHTML(String id,String key,String filtro) {
		String strHtml = "<select id='" + id + "' name='" + id + "'>" ;
		String sql = "select recucodi,recunomb from recurso ";
		if (!filtro.equals("")) {
			sql += " where " + filtro;
		}
		sql += " order by recunomb";
		ResultSet rs;
		try {
			rs = conexion.Query(sql);
			if (rs.next()) {
				do {
					String c = "";
					if (key.equals(rs.getString("recucodi"))) c="selected";
					
				strHtml += "<option value='" + rs.getString("recucodi") + "' " + c + ">" + rs.getString("recunomb") + "</option>";
				}while(rs.next());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			this.error = e.getMessage();
		}
		
		strHtml += "</select>";
		return strHtml;
	}
	
	/*
	 * Metodo procesar()
	 * Este metodo registra el recurso humano
	 * realizando las validaciones necesarias
	 * para que no se infrinja la Integridad Referencial
	 */
	
	public boolean procesar() {
		boolean ret = false;
		
		if (this.codigo.equals("")) {
			this.error = "Campo Id recurso vacío";
			return false;
		}
		
		if (this.nombre.equals("")) {
			this.error = "Campo Nombre recurso vacío";
			return false;
		}
		
		if (this.cargo.equals("")) {
			this.error = "Campo Id Cargo vacío";
			return false;
		}
		if (this.rol.equals("")) {
			this.error = "Campo Id Rol vacío";
			return false;
		}
		
		if (!this.rol.equals("1") && !this.rol.equals("2") ){
			this.error = "Valor del campo Rol debe ser (1) Tecnico o (2) Coordinador";
			return false;
		}
		
		String sql ="select recucodi from recurso where recucodi='" + this.codigo + "'";
		
		try {
			ResultSet rs = conexion.Query(sql);
			if (rs.next()) {
				this.error = "Ya existe el Id del recurso";
				return false;
			}
			rs.close();
			sql = "select cargcodi from cargos where cargcodi='" + this.cargo + "'";
			rs = conexion.Query(sql);
			if (!rs.next()) {
				this.error = "No existe el Id del cargo";
				return false;
			}
			
			rs.close();
			
			ret = this.add();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			this.error = e.getMessage();
		}
		
		
		return ret;
	}
	
	public boolean AddRecursoLocalidad(String recurso,String localidad) throws SQLException {
		boolean ret = false;
		String sql = "insert into recurso_localidad (recurso,localidad) values (?,?)";
		java.sql.PreparedStatement pst = conexion.getConnection()
				.prepareStatement(sql);
		pst.setString(1, recurso);
		pst.setString(2, localidad);
		
			if (conexion.Update(pst) > 0) {
				conexion.Commit();
				ret = true;
			}
		return ret;
	}
	
	public boolean RemoveRecursoLocalidad(String id) throws SQLException {
		boolean ret = false;
		String sql = "delete from recurso_localidad where id=?";
		java.sql.PreparedStatement pst = conexion.getConnection()
				.prepareStatement(sql);
		pst.setString(1, id);
		
			if (conexion.Update(pst) > 0) {
				conexion.Commit();
				ret = true;
			}
		return ret;
	}
	

	public boolean isProyecto(String codigo, String proyecto) {
		boolean ret = false;
		String sql = "select proyecto from recurso_proyecto where proyecto='" + proyecto + "' and recurso='" + codigo + "'";
		try {
			ResultSet rs = conexion.Query(sql);
			if (rs.next()) {
				ret = true;
			}
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			this.error = e.getMessage();
		}
		
		return ret;
	}
	
	public boolean Find(String key) throws SQLException {
		boolean result = false;
		this.error="";

			String sql = "select recucodi,recunomb,recudire,recutele,recumail,recucarg,recuesta,recurol from recurso where recucodi='" + key + "'";
			ResultSet rs = conexion.Query(sql);
			if (rs.next()) {
				this.codigo = (String)rs.getString("recucodi");
				this.nombre = (String)rs.getString("recunomb");
				this.direccion = (String)rs.getString("recudire");
				this.telefono = (String)rs.getString("recutele");
				this.correo = (String)rs.getString("recumail");
				this.cargo = (String)rs.getString("recucarg");
				this.estado = (String)rs.getString("recuesta");
				this.rol = (String)rs.getString("recurol");
				result = true;
			}
			rs.close();
			

		return result;	
	}
	
	public boolean AddSupervisor(String cedula) throws SQLException {
		boolean ret = false;
		String sql = "update recurso set recusupe=1 where recucodi='" + cedula + "'";
		if (conexion.Update(sql) > 0) {
			conexion.Commit();
			ret = true;
		}
		
		
		return ret;
	}
	
	public boolean RemoveSupervisor(String cedula) throws SQLException {
		boolean ret = false;
		String sql = "update recurso set recusupe=0 where recucodi='" + cedula + "' and recusupe=1";
		if (conexion.Update(sql) > 0) {
			sql = "update recurso set recucesu=null where recucesu='" + cedula + "'";
			if (conexion.Update(sql) >= 0) {
				conexion.Commit();
				ret = true;
			}
		}
		
		
		return ret;
	}
	public boolean isSupervisor(String cedula) throws SQLException {
		boolean ret = false;
		String sql = "select recucodi from recurso where recusupe=1 and recucodi='" + cedula + "'";
		ResultSet rs = conexion.Query(sql);
		ret = rs.next();	
		return ret;
	}
	public boolean AddRecursoSupervisor(String supervisor, String recurso) throws SQLException {
		boolean ret = false;
		String sql = String.format("update recurso set recucesu='%s' where recucodi='%s' and recusupe=0", supervisor,recurso);
		if (conexion.Update(sql) > 0) {
			conexion.Commit();
			ret = true;
		}		
		return ret;
	}
	
	public boolean RemoveRecursoSupervisor(String recurso) throws SQLException {
		boolean ret = false;
		String sql = String.format("update recurso set recucesu=null where recucodi='%s' and recusupe=0",recurso);
		if (conexion.Update(sql) > 0) {
			conexion.Commit();
			ret = true;
		}		
		return ret;
	}
}
