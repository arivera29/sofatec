<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="validausuario.jsp"%>
<%@ page import="com.are.sofatec.*" %>
<%@ page import="java.sql.*" %>
<% 
	db conexion = new db();
	ResultSet rs = null;
	boolean rsIsEmpty = true;

	if (request.getParameter("cmdLiberar") != null) {
		String recurso = (String)request.getParameter("cedula");
		String sql = "UPDATE QO_ORDENES SET ESTADO_OPER=0 " +
				" WHERE ESTADO_OPER=1 AND TECNICO ='" + recurso + "'" ;
			int filas_update = conexion.Update(sql);
			if (filas_update > 0) {
				conexion.Commit();
			}else {
				conexion.Rollback();
			}
	}

	
	if (request.getParameter("cedula") != null) {
		String cedula = (String)request.getParameter("cedula");
		String sql = "SELECT QO_ORDENES.*, RECURSO.RECUNOMB, QO_DATOSUM.*, "
				+ " (SELECT QO_APARATOS.NUM_APA FROM QO_APARATOS WHERE QO_ORDENES.NUM_OS = QO_APARATOS.NUM_OS) AS NUM_APA ,"
				+ "(SELECT SUM(IMP_TOT_REC) FROM QO_RECIBOS WHERE QO_RECIBOS.NUM_OS = QO_ORDENES.NUM_OS) AS DEUDA "
				+ " FROM QO_ORDENES,RECURSO, QO_DATOSUM " 
				+ " WHERE QO_ORDENES.ESTADO_OPER = 1 "
				+ " AND QO_ORDENES.NIC = QO_DATOSUM.NIC"
				+ " AND QO_ORDENES.NIS_RAD = QO_DATOSUM.NIS_RAD"
				+ " AND QO_ORDENES.TECNICO = RECURSO.RECUCODI "
				+ " AND QO_ORDENES.TECNICO = ? "
				+ " ORDER BY FECHA_CIERRE";
		PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		pst.setString(1, cedula);
		rs = conexion.Query(pst);
		rsIsEmpty = !rs.next();
		
	}
	

	int fila=0;

%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>CONSULTA ASIGNACION POR RECURSO</title>
<link rel="stylesheet" href="themes/redmond/jquery.ui.all.css">
<LINK REL="stylesheet" TYPE="text/css" HREF="main.css">
<script src="js/jquery.js" language="JavaScript"></script>
<script src="ui/jquery.ui.core.js" language="JavaScript"></script>
<script src="ui/jquery.ui.widget.js" language="JavaScript"></script>
<script src="ui/jquery.ui.button.js"></script>

<script type="text/javascript">
var tab;
var filas;

$(function() {
	$( "input:submit, a, button", ".demo" ).button();
	$( "input:button, a, button", ".demo" ).button();
	$("#descargar").css("display", "none");
});
</script>
<script type="text/javascript" language="javascript">
	function buscar() {
		url = "BuscarRecurso.jsp";
		window.open(url , "BuscarRecurso" , "width=600,height=500,scrollbars=YES,menubar=No,toolbar=NO,status=YES"); 
	}
	
	function consultar() {
		var cedula = $("#cedula").val();
		if (cedula == "") {
			alert("Debe seleccionar el recurso");
			return false;
		}
		
		return true;
	}
	
	function confirmarLiberar() {
		if (confirm("Esta seguro de liberar las ordenes de servicio?")) {
			return true;
		}else {
			return false;
		}
	}
	</script>
<script language="javascript"> 
$(document).ready(function() { 
     $(".botonExcel").click(function(event) { 
     $("#datos_a_enviar").val( $("<div>").append( $("#Exportar_a_Excel").eq(0).clone()).html()); 
     $("#FormularioExportacion").submit(); 
	}); 
}); 
</script>
</head>
<body>
<%@ include file="header.jsp" %>
<div class="contencenter demo">
<h2>CONSULTA ASIGNACION POR RECURSO</h2>
<div id="info"></div>
<form action="" name="form1" onsubmit="javascript: return Consultar();">
	<table>
		<tr>
			<th colspan="4">RECURSO</th>
		</tr>
		<tr>
			<td>Recurso</td>
			<td colspan="3"><input type="text" name="cedula" id="cedula" readonly value="<%= request.getParameter("cedula")!=null?(String)request.getParameter("cedula"):""  %>"><input type="text" name="nombre" id="nombre" size=40 readonly value="<%= request.getParameter("nombre")!=null?(String)request.getParameter("nombre"):""  %>"> </td>
		</tr>
	</table>
	<input type="button" name="cmd_buscar" id="cmd_buscar" value="Buscar Recurso" onclick="javascript:buscar()" >  
	<input type="submit" name="cmd_buscar" value="Consultar">
</form>
<%   if (request.getParameter("cedula") != null) { %>
<h2>Listado de Ordenes Asignadas <%=  (String)request.getParameter("cedula") %> <%= (String)request.getParameter("nombre") %></h2>
<% if (!rsIsEmpty) { %>
	<form action="ExportExcel.jsp" method="post" target="_blank" id="FormularioExportacion">
	<p>Exportar a Excel  <img src="images/Download.png" class="botonExcel" /></p> 
	<input type="hidden" id="datos_a_enviar" name="datos_a_enviar" /> 
	</form> 
	<form action="" onSubmit="javascript: return confirmarLiberar();">
		<input type="hidden" name="cedula" value="<%= (String)request.getParameter("cedula") %>">
		<input type="submit" name="cmdLiberar" value="Liberar Ordenes">
	</form>
<table id="Exportar_a_Excel">
	<tr>
		<th>DEPARTAMENTO</th>
		<th>MUNICIPIO</th>
		<th>ORDEN</th>
		<th>NIC</th>
		<th>CLIENTE</th>
		<th>DIRECCION</th>
		<th>DIR. REF</th>
		<th>BARRIO</th>
		<th>TARIFA</th>
		<th>TIPO ORDEN</th>
		<th>MEDIDOR</th>
		<th>DEUDA</th>
		<th>FECHA GEN.</th>
		<th>CEDULA TECNICO</th>
		<th>NOMBRE TECNICO</th>
	</tr>
	<% do { %>
	<tr <%= (fila %2 ==0)?"class='odd'":"" %>>
		<td><%= (String)rs.getString("DEPARTAMENTO") %></td>
		<td><%= (String)rs.getString("MUNICIPIO") %></td>
		<td><%= (String)rs.getString("NUM_OS") %></td>
		<td><%= (String)rs.getString("NIC") %></td>
		<td><%= (String)rs.getString("APE1_CLI") %> <%= (String)rs.getString("APE2_CLI") %> <%= (String)rs.getString("NOM_CLI") %></td>
		<td><%= (String)rs.getString("DIRECCION") %></td>
		<td><%= (String)rs.getString("REF_DIR") %></td>
		<td><%= (String)rs.getString("LOCALIDAD") %></td>
		<td><%= (String)rs.getString("COD_TAR") %></td>
		<td><%= (String)rs.getString("DESC_TIPO_ORDEN") %></td>
		<td><%= rs.getString("NUM_APA")!=null?(String)rs.getString("NUM_APA"):"DIRECTO" %></td>
		<td><%= rs.getString("DEUDA")!=null?(String)rs.getString("DEUDA"):"0" %></td>
		<td><%= (String)rs.getString("F_GEN") %></td>
		<td><%= (String)rs.getString("TECNICO") %></td>
		<td><%= (String)rs.getString("RECUNOMB") %></td>
	</tr>
	<% fila++; %>
	<% } while(rs.next()); %>
</table>
Total registros: <%= fila %>


<% }else { // Si esta vacio el ResultSet %>
No tiene ordenes asignadas.

<% } %>
<% } %>
</div>
<%@ include file="foot.jsp" %>
</body>
</html>
<%
	conexion.Close();
%>
