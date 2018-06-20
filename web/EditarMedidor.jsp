<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<%@ include file="validausuario.jsp"%>
<%@ page import="com.are.sofatec.*" %>
<%@ page import="java.sql.*" %>
<%
	db conexion = new db();
	String orden = (String)request.getParameter("orden");
	String medidor = (String)request.getParameter("medidor");
	
	String sql = "SELECT QO_MEDIDOR.* FROM QO_MEDIDOR WHERE NUM_OS=? AND NUM_APA=?";
	PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
	pst.setString(1, orden);
	pst.setString(2, medidor);
	ResultSet rs = conexion.Query(pst);
	boolean rsIsEmpty = !rs.next();

	sql = "SELECT * FROM qo_codigos WHERE COD LIKE 'MC%' ORDER BY DESC_COD";
	ResultSet rsMarcas = conexion.Query(sql);
	
	sql = "SELECT TIPO,DESC_TIPO FROM QO_TIPOS WHERE TIPO LIKE 'TA%' ORDER BY DESC_TIPO";
	ResultSet rsTipo = conexion.Query(sql);

	sql = "SELECT COD,DESC_COD FROM QO_CODIGOS WHERE COD LIKE 'PA%' ORDER BY DESC_COD";
	ResultSet rsPropiedad = conexion.Query(sql);
	
	sql = "SELECT TIPO,DESC_TIPO FROM QO_TIPOS WHERE TIPO LIKE 'FA%' ORDER BY DESC_TIPO";
	ResultSet rsFases = conexion.Query(sql);
	
	sql = "SELECT TIPO,DESC_TIPO FROM QO_TIPOS WHERE TIPO LIKE 'TT%' ORDER BY DESC_TIPO";
	ResultSet rsTension = conexion.Query(sql);
	
	sql = "SELECT TIPO,DESC_TIPO FROM QO_TIPOS WHERE TIPO LIKE 'AP%' ORDER BY DESC_TIPO";
	ResultSet rsIntensidad = conexion.Query(sql);
	
	sql = "SELECT TIPO,DESC_TIPO FROM QO_TIPOS WHERE TIPO LIKE 'RE%' ORDER BY DESC_TIPO";
	ResultSet rsRegulador = conexion.Query(sql);
	
	sql = "SELECT TIPO,DESC_TIPO FROM QO_TIPOS WHERE TIPO LIKE 'MA%' ORDER BY DESC_TIPO";
	ResultSet rsMaterial = conexion.Query(sql);
	
	sql = "SELECT TIPO,DESC_TIPO FROM QO_TIPOS WHERE TIPO LIKE 'RF%' ORDER BY DESC_TIPO";
	ResultSet rsNaturaleza = conexion.Query(sql);
	
	sql = "SELECT TIPO,DESC_TIPO FROM QO_TIPOS WHERE TIPO LIKE 'CO%' ORDER BY DESC_TIPO";
	ResultSet rsConsumo = conexion.Query(sql);
	
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Editar Medidor</title>
<link rel="stylesheet" href="themes/redmond/jquery.ui.all.css">
<LINK REL="stylesheet" TYPE="text/css" HREF="main.css">
<script src="js/jquery.js"></script>
<script src="ui/jquery.ui.core.js"></script>
<script src="ui/jquery.ui.widget.js"></script>
<script src="ui/jquery.ui.button.js"></script>
<script type="text/javascript">
	$(function() {
		$(".boton").button();
	});
	
	function guardar(orden,medidor) {
		var num_apa = $("#medidor").val();
		var marca = $("#marca").val();
		var propiedad = $("#propiedad").val();
		var tipo = $("#tipo").val();
		var aol = $("#aol").val();
		var fases = $("#fases").val();
		var tension = $("#tension").val();
		var fecha_inst = $("#fecha_inst").val();
		var intensidad = $("#intensidad").val();
		var constante = $("#constante").val();
		var fecha_fab = $("#fecha_fab").val();
		var fecha_rev = $("#fecha_rev").val();
		var regulador = $("#regulador").val();
		var dimension = $("#dimension").val();
		var fecha_cal = $("#fecha_cal").val();
		var fecha_ver = $("#fecha_ver").val();
		var material = $("#material").val();
		var naturaleza = $("#naturaleza").val();
		var diametro = $("#diametro").val();
		var alta = $("#alta").val();
		var lectura = $("#lectura").val();
		var consumo = $("#consumo").val();
		var ruedas = $("#ruedas").val();
		var coeficiente = $("#coeficiente").val();
		
		
		if( num_apa == "" || 
				marca == "" || 
				propiedad == "" ||
				tipo == "" ||
				aol == "" ||
				fases == "" ||
				tension == "" ||
				fecha_inst == "" ||
				intensidad == "" ||
				constante == "" ||
				fecha_fab == "" ||
				fecha_rev == "" ||
				regulador == "" ||
				dimension == "" ||
				fecha_cal == "" ||
				fecha_ver == "" ||
				material == "" ||
				naturaleza == "" ||
				diametro == "" ||
				alta == "" ||
				lectura == "" ||
				consumo == "" ||
				ruedas == "" ||
				coeficiente == "" 
				) {  
			alert("Falta informacion");
			return;
		}
		
		if (isNaN($('#constante').val() )) {
			alert("Valor no valido");
			return;
		}
		if (isNaN($('#dimension').val() )) {
			alert("Valor no valido");
			return;
		}
		if (isNaN($('#diametro').val() )) {
			alert("Valor no valido");
			return;
		}
		if (isNaN($('#alta').val() )) {
			alert("Valor no valido");
			return;
		}
		if (isNaN($('#lectura').val() )) {
			alert("Valor no valido");
			return;
		}
		if (isNaN($('#ruedas').val() )) {
			alert("Valor no valido");
			return;
		}
		if (isNaN($('#coeficiente').val() )) {
			alert("Valor no valido");
			return;
		}
		
		var url = "SrvResolverOrden";
		$.post(url,{
			operacion : "update_medidor",
			medidor : num_apa, 
			marca : marca, 
			propiedad : propiedad,
			tipo : tipo,
			aol : aol,
			fases : fases,
			tension : tension,
			fecha_inst : fecha_inst,
			intensidad : intensidad,
			constante : constante,
			fecha_fab : fecha_fab,
			fecha_rev : fecha_rev,
			regulador : regulador,
			dimension : dimension,
			fecha_cal : fecha_cal,
			fecha_ver : fecha_ver,
			material : material,
			naturaleza : naturaleza,
			diametro : diametro,
			alta : alta,
			lectura : lectura,
			consumo : consumo,
			ruedas : ruedas,
			coeficiente : coeficiente,
			orden : orden,
			key : medidor
			
		}, function (result){
			if (result.trim() == "OK") {
				alert("Medidor actualizado correctamente");
				window.opener.ListaMedidorInstalado(orden);
				window.close();
			}else {
				$("#info").html("<img src='images/alerta.gif'> " + result);
			}			
		});
		
	}
</script>
</head>
<body>
<h2>EDITAR MEDIDOR <%= (String)request.getParameter("medidor") %></h2>
<div id="info"></div>
<% if (!rsIsEmpty)  { %>
<form action="" name="form1">
<table>
	<tr>
		<td>Numero Medidor</td>
		<td><input type="text" name="medidor" id="medidor" value="<%= rs.getString("NUM_APA") %>" ></td>
	</tr>
	<tr>
		<td>Marca</td>
		<td>
			<select name="marca" id="marca">
				<option value="">Seleccionar</option>
				<% while(rsMarcas.next()) { %>
				<option value="<%= rsMarcas.getString("COD") %>" <%= (rsMarcas.getString("COD").equals(rs.getString("CO_MARCA")))?"selected":"" %>><%= rsMarcas.getString("DESC_COD") %></option>
				<% } %>
			</select>
		</td>
	</tr>
	<tr>
		<td>Tipo</td>
		<td>
			<select name="tipo" id="tipo">
				<option value="">Seleccionar</option>
				<% while(rsTipo.next()) { %>
				<option value="<%= rsTipo.getString("TIPO") %>" <%= (rsTipo.getString("TIPO").equals(rs.getString("TIP_APA")))?"selected":"" %>><%= rsTipo.getString("DESC_TIPO") %></option>
				<% } %>
			</select>
		</td>
	</tr>
	<tr>
		<td>Propiedad</td>
		<td>
			<select name="propiedad" id="propiedad">
				<option value="">Seleccionar</option>
				<% while(rsPropiedad.next()) { %>
				<option value="<%= rsPropiedad.getString("COD") %>" <%= (rsPropiedad.getString("COD").equals(rs.getString("CO_PROP_APA")))?"selected":"" %> ><%= rsPropiedad.getString("DESC_COD") %></option>
				<% } %>
			</select>
		</td>
	</tr>
	<tr>
		<td>AOL</td>
		<td><input type="text" name="aol" id="aol" value="<%= rs.getString("AOL_APA") %>"></td>
	</tr>
	<tr>
		<td>Fases</td>
		<td>
			<select name="fases" id="fases">
				<option value="">Seleccionar</option>
				<% while(rsFases.next()) { %>
				<option value="<%= rsFases.getString("TIPO") %>" <%= (rsFases.getString("TIPO").equals(rs.getString("TIP_FASE")))?"selected":"" %>><%= rsFases.getString("DESC_TIPO") %></option>
				<% } %>
			</select>
		</td>
	</tr>
	<tr>
		<td>Tension</td>
		<td>
			<select name="tension" id="tension">
				<option value="">Seleccionar</option>
				<% while(rsTension.next()) { %>
				<option value="<%= rsTension.getString("TIPO") %>" <%= (rsTension.getString("TIPO").equals(rs.getString("TIP_TENSION")))?"selected":"" %>><%= rsTension.getString("DESC_TIPO") %></option>
				<% } %>
			</select>
		</td>
	</tr>
	<tr>
		<td>Fecha Instalacion</td>
		<td><input type="text" name="fecha_inst" id="fecha_inst" value="<%= rs.getString("F_INST") %>"></td>
	</tr>
	<tr>
		<td>Tipo Intensidad</td>
		<td>
			<select name="intensidad" id="intensidad">
				<option value="">Seleccionar</option>
				<% while(rsIntensidad.next()) { %>
				<option value="<%= rsIntensidad.getString("TIPO") %>" <%= (rsIntensidad.getString("TIPO").equals(rs.getString("TIP_INTENSIDAD")))?"selected":"" %>><%= rsIntensidad.getString("DESC_TIPO") %></option>
				<% } %>
			</select>
		</td>
	</tr>
	<tr>
		<td>Constante</td>
		<td><input type="text" name="constante" id="constante" value="<%= rs.getString("CTE_APA") %>"></td>
	</tr>
	<tr>
		<td>Fecha Fabricacion</td>
		<td><input type="text" name="fecha_fab" id="fecha_fab" value="<%= rs.getString("F_FABRIC") %>"></td>
	</tr>
	<tr>
		<td>Fecha Ult. Revision</td>
		<td><input type="text" name="fecha_rev" id="fecha_rev" value="<%= rs.getString("F_UREVIS") %>"></td>
	</tr>
	<tr>
		<td>Tipo Regulador</td>
		<td>
			<select name="regulador" id="regulador">
				<option value="">Seleccionar</option>
				<% while(rsRegulador.next()) { %>
				<option value="<%= rsRegulador.getString("TIPO") %>" <%= (rsRegulador.getString("TIPO").equals(rs.getString("REGULADOR")))?"selected":"" %>><%= rsRegulador.getString("DESC_TIPO") %></option>
				<% } %>
			</select>
		</td>
	</tr>
	<tr>
		<td>Dimension</td>
		<td><input type="text" name="dimension" id="dimension" value="<%= rs.getString("DIMEN_CONEX") %>"></td>
	</tr>
	<tr>
		<td>Fecha Prox. Calibracion</td>
		<td><input type="text" name="fecha_cal" id="fecha_cal" value="<%= rs.getString("F_PROX_CALIBRACION") %>"></td>
	</tr>
	<tr>
		<td>Fecha Prox. Verificacion</td>
		<td><input type="text" name="fecha_ver" id="fecha_ver" value="<%= rs.getString("F_PROX_VERIFICACION") %>"></td>
	</tr>
	<tr>
		<td>Tipo Material</td>
		<td>
			<select name="material" id="material">
				<option value="">Seleccionar</option>
				<% while(rsMaterial.next()) { %>
				<option value="<%= rsMaterial.getString("TIPO") %>" <%= (rsMaterial.getString("TIPO").equals(rs.getString("TIP_MATERIAL")))?"selected":"" %>><%= rsMaterial.getString("DESC_TIPO") %></option>
				<% } %>
			</select>
		</td>
	</tr>
	<tr>
		<td>Tipo Naturaleza</td>
		<td>
			<select name="naturaleza" id="naturaleza">
				<option value="">Seleccionar</option>
				<% while(rsNaturaleza.next()) { %>
				<option value="<%= rsNaturaleza.getString("TIPO") %>" <%= (rsNaturaleza.getString("TIPO").equals(rs.getString("TIP_NATUR")))?"selected":"" %>><%= rsNaturaleza.getString("DESC_TIPO") %></option>
				<% } %>
			</select>
		</td>
	</tr>
	<tr>
		<td>Diametro</td>
		<td><input type="text" name="diametro" id="diametro" value="<%= rs.getString("DIAMETRO") %>"></td>
	</tr>
	<tr>
		<td>Alta</td>
		<td><input type="text" name="alta" id="alta" value="<%= rs.getString("ALTA") %>"></td>
	</tr>
	<tr>
		<td>Lectura</td>
		<td><input type="text" name="lectura" id="lectura" value="<%= rs.getString("LECTURA") %>"></td>
	</tr>
	<tr>
		<td>Tipo de Consumos</td>
		<td>
			<select name="consumo" id="consumo">
				<option value="">Seleccionar</option>
				<% while(rsConsumo.next()) { %>
				<option value="<%= rsConsumo.getString("TIPO") %>" <%= (rsConsumo.getString("TIPO").equals(rs.getString("TIP_CSMO")))?"selected":"" %>><%= rsConsumo.getString("DESC_TIPO") %></option>
				<% } %>
			</select>
		</td>
	</tr>
	<tr>
		<td>Num. Ruedas</td>
		<td><input type="text" name="ruedas" id="ruedas" value="<%= rs.getString("NUM_RUE") %>"></td>
	</tr>
	<tr>
		<td>Coeficiente Perdida</td>
		<td><input type="text" name="coeficiente" id="coeficiente" value="<%= rs.getString("COEF_PER") %>"></td>
	</tr>
</table>

<button type="button" name="cmd_guardar" id="cmd_guardar" onclick="javascript:guardar('<%= (String)request.getParameter("orden") %>','<%= (String)request.getParameter("medidor") %>')" class="boton" >Guardar</button>
<button type="button" name="cmd_cancelar" id="cmd_cancelar" onclick="javascript:window.close();" class="boton">Cancelar</button>
</form>


<% }else { %>
	Medidor <%= (String)request.getParameter("medidor") %>no encontrado. <input type="button" name="cmd_cancelar" id="cmd_cancelar" value="Caneclar" onclick="javascript:window.close();" class="boton">
<% } %>
</body>
</html>

<%
	if (rs != null) rs.close();
	if (conexion != null) conexion.Close();
%>