package com.are.sofatec;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Orders {
	private String orden;
	private String nic;
	private String tipo;
	private String direccion;
	private String nis;
	private String ref_direccion;
	private String departamento;
	private String municipio;
	private String localidad;  // Barrio
	private String fecha_generacion;
	private String estado;
	private String usuario="none";
	private String sql_last = "";
	private String recurso;
	private String fecha_asignacion;
	
	private db conexion = null;
	
	public Orders(db conexion) {
		super();
		this.conexion = conexion;
	}
	public String getOrden() {
		return orden;
	}
	public void setOrden(String orden) {
		this.orden = orden;
	}
	public String getNic() {
		return nic;
	}
	public void setNic(String nic) {
		this.nic = nic;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public String getNis() {
		return nis;
	}
	public void setNis(String nis) {
		this.nis = nis;
	}
	public String getRef_direccion() {
		return ref_direccion;
	}
	public void setRef_direccion(String ref_direccion) {
		this.ref_direccion = ref_direccion;
	}
	public String getDepartamento() {
		return departamento;
	}
	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}
	public String getMunicipio() {
		return municipio;
	}
	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}
	public String getLocalidad() {
		return localidad;
	}
	public void setLocalidad(String localidad) {
		this.localidad = localidad;
	}
	public String getFecha_generacion() {
		return fecha_generacion;
	}
	public void setFecha_generacion(String fecha_generacion) {
		this.fecha_generacion = fecha_generacion;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	public db getConexion() {
		return conexion;
	}
	public void setConexion(db conexion) {
		this.conexion = conexion;
	}
	
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public String getSql_last() {
		return sql_last;
	}
	public void setSql_last(String sql_last) {
		this.sql_last = sql_last;
	}
	public String getRecurso() {
		return recurso;
	}
	public void setRecurso(String recurso) {
		this.recurso = recurso;
	}
	public String getFecha_asignacion() {
		return fecha_asignacion;
	}
	public void setFecha_asignacion(String fecha_asignacion) {
		this.fecha_asignacion = fecha_asignacion;
	}
	public boolean AgregarOrden() throws SQLException {
		boolean result = false;
		
		if (!this.ExistTipo()) {
			throw new SQLException("Tipo de orden no válida");
		}
		if (!this.ExistClient(this.nic)) {
			throw new SQLException("El cliente no se encuentra registrado (" + this.nic + ")");
		}
		
		if (this.ExistOrder(this.orden)) {
			throw new SQLException("La Orden se encuentra registrada");
		}
		if (this.isCorrectUbication()) {
			if (this.add()) {
				result = true;
			}
		}
		return result;
	}
	
	public boolean add() throws SQLException{
		boolean result = false;
			String sql = String.format("insert into orders (orden,nic,tipo,direccion,nis,ref_direccion,departamento,municipio,localidad,fecha_generacion,estado,fecha_carga,usuario_carga) values ('%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','1',sysdate(),'%s')",
					this.orden,this.nic,this.tipo,this.direccion,this.nis,this.ref_direccion,this.departamento,this.municipio,this.localidad,this.fecha_generacion,this.usuario);
			if (conexion.Update(sql) > 0) {
				conexion.Commit();
				result = true;
			}
		return result;
	}
	
	public boolean modify(String key) throws SQLException{
		boolean result = false;
			String sql = String.format("update orders set orden='%s',nic='%s',tipo='%s',direccion='%s',nis='%s',ref_direccion='%s',departamento='%s',municipio='%s',localidad='%s',fecha_generacion='%s' where orden='%s'",
					this.orden,this.nic,this.tipo,this.direccion,this.nis,this.ref_direccion,this.departamento,this.municipio,this.localidad,this.fecha_generacion,key);
			if (conexion.Update(sql) > 0) {
				conexion.Commit();
				result = true;
			}
		return result;
	}
	public boolean remove(String key) throws SQLException{
		boolean result = false;
			String sql = String.format("delete from orders where orden='%s'",key);
			if (conexion.Update(sql) > 0) {
				conexion.Commit();
				result = true;
			}
		return result;
	}
	
	public boolean isCorrectUbication() throws SQLException {
		boolean result = false;
		String sql = String.format("select depacodi,depadesc from departamentos where depadesc='%s'", this.departamento);
		ResultSet rs = conexion.Query(sql);
		if (rs.next()) {
			
			this.departamento = (String)rs.getString("depacodi");  // obtiene el codigo del departamento
			rs.close();
			sql = String.format("select locacodi,locadesc from localidad where locadesc='%s' and locadepa='%s'", this.municipio,this.departamento);
			rs = conexion.Query(sql);
			if (rs.next()) {
				this.municipio = (String)rs.getString("locacodi");
				result = true;
				
			}else {
				throw new SQLException("No se encuentra el municipio");
			}
			
			
		} else {
			throw new SQLException("No se encuentra el departamento");
		}
		if (!rs.isClosed()) {
			rs.close();
		}
		return result;
	}
	
	public boolean ExistOrder(String key) throws SQLException {
		boolean result = false;
		String sql = String.format("select orden from orders where orden='%s'", key);
		ResultSet rs = conexion.Query(sql);
		result = rs.next();
		rs.close();		
		return result;
	}
	
	public boolean ExistTipo() throws SQLException {
		boolean result = false;
		String sql = String.format("select tiorcodi from tipo_orden where tiorcodi='%s'", this.tipo);
		ResultSet rs = conexion.Query(sql);
		result = rs.next();
		rs.close();		
		return result;
	}
	public boolean Find(String key) throws SQLException {
		boolean result = false;
		String sql = String.format("select * from orders where orden='%s'", key);
		ResultSet rs = conexion.Query(sql);
		result = rs.next();
		if (result) { // orden encontrada
			this.orden = rs.getString("orden");
			this.nic = rs.getString("nic");
			this.nis = rs.getString("nis");
			this.departamento = rs.getString("departamento");
			this.municipio = rs.getString("municipio");
			this.localidad = rs.getString("localidad");
			this.estado =rs.getString("estado");
			this.direccion = rs.getString("direccion");
			this.fecha_generacion = rs.getString("fecha_generacion");
			this.tipo = rs.getString("tipo");
			this.recurso = rs.getString("recurso");
			this.fecha_asignacion = rs.getString("fecha_asignacion");
		}
		rs.close();		
		return result;
	}
	public boolean ExistClient(String nic) throws SQLException {
		boolean result = false;
		Clientes cliente = new Clientes(conexion);
		result = cliente.Find(nic);
		return result;
	}
	
	public boolean AsignarRecurso(String orden,String cedula, int num_show) throws SQLException {
		boolean result = false;
		if (num_show == 0) {
			num_show = this.OrdenVisualizacion(cedula);
		}
		String sql = String.format("update orders set recurso='%s', fecha_asignacion = sysdate(),num_show=%s,estado='2' where orden='%s' and estado != '99'", cedula,num_show,orden);
		this.sql_last = sql;
		if (conexion.Update(sql) > 0 && this.AddHistory(orden, cedula)) {
			conexion.Commit();
			result = true;
		}else {
			conexion.Rollback();  // deshacer cambios
		}
		return result;	
	}
	public int OrdenVisualizacion(String recurso) throws SQLException {
		int consecutivo = 1;
		String sql = "select max(num_show) num_show from orders where recurso='" + recurso + "' and estado != '99'";
		ResultSet rs = conexion.Query(sql);
		if (rs.next()) {
			consecutivo = rs.getInt("num_show");
			consecutivo++;
		}
		return consecutivo;
	}
	public String AsignacionActual(String orden) throws SQLException {
		String result = "-1";
		String sql = String.format("select recurso from orders where orden='%s'", orden);
		ResultSet rs = conexion.Query(sql);
		if (rs.next()) {
			result = rs.getString("recurso");
		}
		rs.close();		
		return result;
	}
	public boolean  Liberar(String orden) throws SQLException {
		boolean result = false;
		String sql = String.format("update orders set recurso='-1', fecha_asignacion = null,num_show=-1,estado=1 where orden='%s' and estado != '99'", orden);
		if (conexion.Update(sql) > 0) {
			conexion.Commit();
			result = true;
		}	
		return result;	
	}
	
	public boolean  LiberarTecnico(String cedula) throws SQLException {
		boolean result = false;
		String sql = String.format("update orders set recurso='-1', fecha_asignacion = null,num_show=-1,estado=1 where recurso='%s' and estado != '99'", cedula);
		if (conexion.Update(sql) > 0) {
			conexion.Commit();
			result = true;
		}	
		return result;	
	}
	
	public boolean AddHistory(String orden, String cedula) throws SQLException {
		boolean result= false;
		String sql = String.format("insert into history_assign (orden,recurso,fecha_sistema,usuario) values('%s','%s',sysdate(),'%s')",orden,cedula,this.usuario);
		if (conexion.Update(sql) > 0) {
			result = true;
		}
		return result;
	}
}
