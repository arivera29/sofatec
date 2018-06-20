package com.are.sofatec;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Ordenes {
	private db conexion = null;
	private String orden;
	private String tipo;
	private String nic;
	private String nis;
	private String ftratamiento;  // fecha puesta tratamiento
	private String municipio;
	private String localidad;
	private String via;
	private String calle;
	private String crucero;
	private String placa;
	private String interior;
	private String rdireccion;  // referencia de direccion
	private String codigocliente;  
	private String cuenta;
	private String nombre;
	private String apellido1;
	private String apellido2;
	private String deuda;
	private String cantfactura;
	private String tarifa;
	private String num_apa;
	private String codmarca; // codigo marca
	private String descmarca;  // descripcion marca
	private String tipo_apa;
	private String desc_tipo_apa;
	private String num_rue;
	private String aol_apa;
	private String amperios;
	private String finstalacion;  // fecha instalacion
	private String ffabricacion;  // fecha fabricacion
	private String tipo_tension;
	private String desc_tipo_tension;
	private String tipo_csmo;
	private String desc_tipo_csmo;
	private String ruta;
	private String itin;
	private String aol;
	
	private String estado;
	private String usuario="none";
	private String recurso="-1";
	private String fecha_asignacion;
	
	private String departamento;
	private int confirm;
	private int visitas;
	
	private String fecha_cierre;
	private String lectura;
	private String observaciones;
	
	
	
	public String getFecha_cierre() {
		return fecha_cierre;
	}
	public String getLectura() {
		return lectura;
	}
	public String getObservaciones() {
		return observaciones;
	}
	public int getVisitas() {
		return visitas;
	}
	public void setVisitas(int visitas) {
		this.visitas = visitas;
	}
	public int getConfirm() {
		return confirm;
	}
	public String getDepartamento() {
		return departamento;
	}
	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}
	public db getConexion() {
		return conexion;
	}
	public void setConexion(db conexion) {
		this.conexion = conexion;
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
	public String getNic() {
		return nic;
	}
	public void setNic(String nic) {
		this.nic = nic;
	}
	public String getNis() {
		return nis;
	}
	public void setNis(String nis) {
		this.nis = nis;
	}
	public String getFtratamiento() {
		return ftratamiento;
	}
	public void setFtratamiento(String ftratamiento) {
		this.ftratamiento = ftratamiento;
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
	public String getVia() {
		return via;
	}
	public void setVia(String via) {
		this.via = via;
	}
	public String getCalle() {
		return calle;
	}
	public void setCalle(String calle) {
		this.calle = calle;
	}
	public String getCrucero() {
		return crucero;
	}
	public void setCrucero(String crucero) {
		this.crucero = crucero;
	}
	public String getPlaca() {
		return placa;
	}
	public void setPlaca(String placa) {
		this.placa = placa;
	}
	public String getInterior() {
		return interior;
	}
	public void setInterior(String interior) {
		this.interior = interior;
	}
	public String getRdireccion() {
		return rdireccion;
	}
	public void setRdireccion(String rdireccion) {
		this.rdireccion = rdireccion;
	}
	public String getCodigocliente() {
		return codigocliente;
	}
	public void setCodigocliente(String codigocliente) {
		this.codigocliente = codigocliente;
	}
	public String getCuenta() {
		return cuenta;
	}
	public void setCuenta(String cuenta) {
		this.cuenta = cuenta;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getApellido1() {
		return apellido1;
	}
	public void setApellido1(String apellido1) {
		this.apellido1 = apellido1;
	}
	public String getApellido2() {
		return apellido2;
	}
	public void setApellido2(String apellido2) {
		this.apellido2 = apellido2;
	}
	public String getDeuda() {
		return deuda;
	}
	public void setDeuda(String deuda) {
		this.deuda = deuda;
	}
	public String getCantfactura() {
		return cantfactura;
	}
	public void setCantfactura(String cantfactura) {
		this.cantfactura = cantfactura;
	}
	public String getTarifa() {
		return tarifa;
	}
	public void setTarifa(String tarifa) {
		this.tarifa = tarifa;
	}
	public String getNum_apa() {
		return num_apa;
	}
	public void setNum_apa(String num_apa) {
		this.num_apa = num_apa;
	}
	public String getCodmarca() {
		return codmarca;
	}
	public void setCodmarca(String codmarca) {
		this.codmarca = codmarca;
	}
	public String getDescmarca() {
		return descmarca;
	}
	public void setDescmarca(String descmarca) {
		this.descmarca = descmarca;
	}
	public String getTipo_apa() {
		return tipo_apa;
	}
	public void setTipo_apa(String tipo_apa) {
		this.tipo_apa = tipo_apa;
	}
	public String getDesc_tipo_apa() {
		return desc_tipo_apa;
	}
	public void setDesc_tipo_apa(String desc_tipo_apa) {
		this.desc_tipo_apa = desc_tipo_apa;
	}
	public String getNum_rue() {
		return num_rue;
	}
	public void setNum_rue(String num_rue) {
		this.num_rue = num_rue;
	}
	public String getAol_apa() {
		return aol_apa;
	}
	public void setAol_apa(String aol_apa) {
		this.aol_apa = aol_apa;
	}
	public String getAmperios() {
		return amperios;
	}
	public void setAmperios(String amperios) {
		this.amperios = amperios;
	}
	public String getFinstalacion() {
		return finstalacion;
	}
	public void setFinstalacion(String finstalacion) {
		this.finstalacion = finstalacion;
	}
	public String getFfabricacion() {
		return ffabricacion;
	}
	public void setFfabricacion(String ffabricacion) {
		this.ffabricacion = ffabricacion;
	}
	public String getTipo_tension() {
		return tipo_tension;
	}
	public void setTipo_tension(String tipo_tension) {
		this.tipo_tension = tipo_tension;
	}
	public String getDesc_tipo_tension() {
		return desc_tipo_tension;
	}
	public void setDesc_tipo_tension(String desc_tipo_tension) {
		this.desc_tipo_tension = desc_tipo_tension;
	}
	public String getTipo_csmo() {
		return tipo_csmo;
	}
	public void setTipo_csmo(String tipo_csmo) {
		this.tipo_csmo = tipo_csmo;
	}
	public String getDesc_tipo_csmo() {
		return desc_tipo_csmo;
	}
	public void setDesc_tipo_csmo(String desc_tipo_csmo) {
		this.desc_tipo_csmo = desc_tipo_csmo;
	}
	public String getRuta() {
		return ruta;
	}
	public void setRuta(String ruta) {
		this.ruta = ruta;
	}
	public String getItin() {
		return itin;
	}
	public void setItin(String itin) {
		this.itin = itin;
	}
	public String getAol() {
		return aol;
	}
	public void setAol(String aol) {
		this.aol = aol;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
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
	public Ordenes(db conexion) {
		super();
		this.conexion = conexion;
	}
	
	public boolean AgregarOrden() throws SQLException {
		boolean result = false;
		
		if (!this.FoundTipo()) {
			throw new SQLException("Tipo de orden no válida");
		}
		
		
		if (this.ExistOrder(this.orden)) {
			throw new SQLException("La Orden se encuentra registrada");
		}
		if (!this.FindLocalidad(this.departamento).equals("-1") ) {
			if (this.add()) {
				result = true;
			}
		}
		return result;
	}
	
	public String getDireccion() {
		return this.via + " " + this.calle + " " + this.crucero + " " + this.placa + " " + this.interior ;
	}
	
	public String getCliente() {
		return this.nombre + " " + this.apellido1 + " " + this.apellido2;
	}
	
	public boolean add() throws SQLException{
		boolean result = false;
		String sql = "insert into orders (orden,tipo,nic,nis,f_pues_trat,localidad," +
				"nom_local,nom_via,nom_calle,crucero,placa,interior,ref_dir,cod_cli," +
				"sec_cta,nom_cli,ape1_cli,ape2_cli,deuda,cant_fact,cod_tar,num_apa," +
				"co_marca,desc_co_marca,tip_apa,desc_tip_apa,num_rue,aol_apa,amperios," +
				"f_inst,f_fabric,c_cte,desc_tip_tension_apa,tip_csmo,desc_tip_csmo," +
				"ruta,num_itin,aol,usuario_carga,fecha_carga,estado) values (" +
				"?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
						"?,?,?,?,?,?,?,?,?,?,sysdate(),1)";
		java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		pst.setString(1, this.orden);
		pst.setString(2, this.tipo);
		pst.setString(3, this.nic);
		pst.setString(4, this.nis);
		pst.setString(5, this.ftratamiento);
		pst.setString(6, this.FindLocalidad(this.departamento));
		pst.setString(7, this.localidad);
		pst.setString(8, this.via);
		pst.setString(9, this.calle);
		pst.setString(10, this.crucero);
		pst.setString(11, this.placa);
		pst.setString(12, this.interior);
		pst.setString(13, this.rdireccion);
		pst.setString(14, this.codigocliente);
		pst.setString(15, this.cuenta);
		pst.setString(16, this.nombre);
		pst.setString(17, this.apellido1);
		pst.setString(18, this.apellido2);
		pst.setString(19, this.deuda);
		pst.setString(20, this.cantfactura);
		pst.setString(21, this.tarifa);
		pst.setString(22, this.num_apa);
		pst.setString(23, this.codmarca);
		pst.setString(24, this.descmarca);
		pst.setString(25, this.tipo_apa);
		pst.setString(26, this.desc_tipo_apa);
		pst.setString(27, this.num_rue);
		pst.setString(28, this.aol_apa);
		pst.setString(29, this.amperios);
		pst.setString(30, this.finstalacion);
		pst.setString(31, this.ffabricacion);
		pst.setString(32, this.tipo_tension);
		pst.setString(33, this.desc_tipo_tension);
		pst.setString(34, this.tipo_csmo);
		pst.setString(35, this.desc_tipo_csmo);
		pst.setString(36, this.ruta);
		pst.setString(37, this.itin);
		pst.setString(38, this.aol);
		pst.setString(39, this.usuario);
		
		if ( conexion.Update(pst) > 0) {
			conexion.Commit();
			result = true;
		}
		
		return result;
	}
	
	public String FindLocalidad(String departamento) throws SQLException {
		String result = "-1";
		String sql = String.format("select locacodi from localidad where locadesc='%s' and locadepa='%s'", this.municipio,departamento);
		ResultSet rs = conexion.Query(sql);
		if (rs.next()) {
				result = (String)rs.getString("locacodi");
			}else {
				throw new SQLException("No se encuentra el municipio");
			}
		if (!rs.isClosed()) {
			rs.close();
		}
		return result;
	}
	
	public boolean FoundTipo() throws SQLException {
		boolean result = false;
		String sql = String.format("select tiorcodi from tipo_orden where tiorcodi='%s'", this.tipo);
		ResultSet rs = conexion.Query(sql);
		result = rs.next();
		rs.close();		
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
	
	public boolean Find(String key) throws SQLException {
		boolean result = false;
		String sql = String.format("select * from orders where orden='%s'", key);
		ResultSet rs = conexion.Query(sql);
		result = rs.next();
		if (result) { // orden encontrada
			this.orden = rs.getString("orden");
			this.tipo = rs.getString("tipo");
			this.nic = rs.getString("nic");
			this.nis = rs.getString("nis");
			this.ftratamiento = rs.getString("f_pues_trat");  // fecha puesta tratamiento
			this.municipio = rs.getString("localidad");;
			this.localidad = rs.getString("nom_local");;
			this.via = rs.getString("nom_via");
			this.calle = rs.getString("nom_calle");
			this.crucero = rs.getString("crucero");
			this.placa = rs.getString("placa");
			this.interior = rs.getString("interior");
			this.rdireccion = rs.getString("ref_dir");  // referencia de direccion
			this.codigocliente = rs.getString("cod_cli");  
			this.cuenta = rs.getString("sec_cta");
			this.nombre = rs.getString("nom_cli");
			this.apellido1 = rs.getString("ape1_cli");
			this.apellido2 = rs.getString("ape2_cli");
			this.deuda = rs.getString("deuda");
			this.cantfactura = rs.getString("cant_fact");
			this.tarifa = rs.getString("cod_tar");
			this.num_apa = rs.getString("num_apa");
			this.codmarca = rs.getString("co_marca"); // codigo marca
			this.descmarca = rs.getString("desc_co_marca");  // descripcion marca
			this.tipo_apa = rs.getString("tip_apa");
			this.desc_tipo_apa = rs.getString("desc_tip_apa");
			this.num_rue = rs.getString("num_rue");
			this.aol_apa = rs.getString("aol_apa");
			this.amperios = rs.getString("amperios");
			this.finstalacion = rs.getString("f_inst");  // fecha instalacion
			this.ffabricacion = rs.getString("f_fabric");  // fecha fabricacion
			this.tipo_tension = rs.getString("c_cte");
			this.desc_tipo_tension = rs.getString("desc_tip_tension_apa");
			this.tipo_csmo = rs.getString("tip_csmo");
			this.desc_tipo_csmo = rs.getString("desc_tip_csmo");
			this.ruta = rs.getString("ruta");
			this.itin = rs.getString("num_itin");
			this.aol = rs.getString("aol");		
			this.estado = rs.getString("estado");
			this.usuario = rs.getString("usuario_carga");
			this.recurso = rs.getString("recurso");
			this.fecha_asignacion = rs.getString("fecha_asignacion");
			this.confirm = rs.getInt("confirm");
			this.visitas = rs.getInt("visita");
			this.fecha_cierre = rs.getString("fecha_cierre");
			this.lectura = rs.getString("lectura");
			this.observaciones = rs.getString("observacion");
			result = true;
			
		}
		rs.close();		
		return result;
	}
	
	public boolean AsignarRecurso(String orden,String cedula, int num_show) throws SQLException {
		boolean result = false;
		if (num_show == 0) {
			num_show = this.OrdenVisualizacion(cedula);
		}
		String sql = "update orders set recurso=?, fecha_asignacion = sysdate(),num_show=?,estado='2' where orden=? and estado != '99'";
		java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		pst.setString(1, cedula);
		pst.setInt(2, num_show);
		pst.setString(3, orden);
		if (conexion.Update(pst) > 0 && this.AddHistory(orden, cedula)) {
			conexion.Commit();
			result = true;
		}else {
			conexion.Rollback();  // deshacer cambios
		}
		return result;	
	}
	
	public boolean AsignarRecurso(String orden,String cedula, int num_show,boolean commit) throws SQLException {
		boolean result = false;
		if (num_show == 0) {
			num_show = this.OrdenVisualizacion(cedula);
		}
		String sql = "update orders set recurso=?, fecha_asignacion = sysdate(),num_show=?,estado='2' where orden=? and estado != '99'";
		java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		pst.setString(1, cedula);
		pst.setInt(2, num_show);
		pst.setString(3, orden);
		if (conexion.Update(pst) > 0 && this.AddHistory(orden, cedula)) {
			if (commit) {
				conexion.Commit();
			}
			result = true;
		}else {
			conexion.Rollback();  // deshacer cambios
		}
		return result;	
	}
	
	public boolean AsignarOrdenVisualizacion(String orden,String cedula, int num_show,boolean commit) throws SQLException {
		boolean result = false;
		String sql = "update orders set num_show=? where orden=? and estado = '2' and recurso = ? ";
		java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		pst.setInt(1, num_show);
		pst.setString(2, orden);
		pst.setString(3, cedula);
		if (conexion.Update(pst) > 0 ) {
			if (commit) {
				conexion.Commit();
			}
			result = true;
		}else {
			conexion.Rollback();  // deshacer cambios
		}
		return result;	
	}
	
	public boolean CleanOrdenVisualizacion(String cedula, boolean commit) throws SQLException {
		boolean result = false;
		String sql = "update orders set num_show=0 where estado = '2' and recurso = ? ";
		java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		pst.setString(1, cedula);
		if (conexion.Update(pst) > 0 ) {
			if (commit) {
				conexion.Commit();
			}
			result = true;
		}else {
			conexion.Rollback();  // deshacer cambios
		}
		return result;	
	}
	
	public int OrdenVisualizacion(String recurso) throws SQLException {
		int consecutivo = 1;
		String sql = "select max(num_show) num_show from orders where recurso=? and estado != '99'";
		java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		pst.setString(1, recurso);
		ResultSet rs = conexion.Query(pst);
		if (rs.next()) {
			consecutivo = rs.getInt("num_show");
			consecutivo++;
		}
		return consecutivo;
	}
	
	public boolean AddHistory(String orden, String cedula) throws SQLException {
		boolean result= false;
		String sql = "insert into history_assign (orden,recurso,fecha_sistema,usuario) values(?,?,sysdate(),?)";
		java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		pst.setString(1, orden);
		pst.setString(2,cedula);
		pst.setString(3,this.usuario);
		if (conexion.Update(pst) > 0) {
			result = true;
		}
		return result;
	}
	public String AsignacionActual(String orden) throws SQLException {
		String result = "-1";
		String sql = "select recurso from orders where orden=?";
		java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		pst.setString(1, orden);
		ResultSet rs = conexion.Query(pst);
		if (rs.next()) {
			result = rs.getString("recurso");
		}
		rs.close();		
		return result;
	}
	
	public boolean Confirm(String orden) throws SQLException {
		boolean result= false;
		
		String sql = "update orders set confirm=1, date_confirm=sysdate() where orden=? and confirm=0 ";
		java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		pst.setString(1, orden);
		if (conexion.Update(pst) > 0) {
			conexion.Commit();
			result = true;
		}
		
		return result;
	}
	
	public int CountImages(String orden) throws SQLException {
		int count = 0;
		String sql = "select orden,count(*) total from imagenes where orden = ? group by orden";
		java.sql.PreparedStatement pst =  this.getConexion().getConnection().prepareStatement(sql);
		pst.setString(1, orden);
		ResultSet rs = this.getConexion().Query(pst);
		if (rs.next()) {
			count = rs.getInt("total");
		}
		return count;
	}

}
