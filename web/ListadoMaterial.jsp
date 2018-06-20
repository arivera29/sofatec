<%@ page import="com.are.sofatec.*"%>
<%@ page import="java.sql.*"%>
<%
	request.setCharacterEncoding("UTF-8");
	String zona = (String)request.getParameter("zona");
	String fecha_inicial = (String)request.getParameter("fecha_inicial");
	String fecha_final = (String)request.getParameter("fecha_final");
	String tipo = (String)request.getParameter("tipo");
	
	db conexion = new db();
	String sql= "";
	if (tipo.equals("1")) {
		sql ="SELECT QO_ORDEN_MATERIAL.*,QO_ORDENES.TIP_OS," 
			+ " QO_ORDENES.CEDULA_OPERARIO_HDA, QO_ORDENES.NOMBRE_OPERARIO_HDA,"
			+ " QO_ORDENES.FECHA_CIERRE, QO_DESC_MI.DESC_ELEMENTO "
			+ " FROM QO_ORDEN_MATERIAL,QO_ORDENES, QO_DESC_MI " 
			+ " WHERE QO_ORDEN_MATERIAL.NUM_OS = QO_ORDENES.NUM_OS "
			+ " AND QO_ORDENES.NUM_ZONA = ?"
			+ " AND QO_ORDEN_MATERIAL.CO_ELEMENTO = QO_DESC_MI.CO_ELEMENTO "
			+ " AND DATE(QO_ORDENES.FECHA_CIERRE) BETWEEN ? AND ?"		
			+ " AND QO_ORDEN_MATERIAL.TIPO = 1"
			+ " ORDER BY QO_ORDENES.FECHA_CIERRE, QO_ORDENES.NUM_OS";
	
	}else {
		sql ="SELECT QO_ORDEN_MATERIAL.*,QO_ORDENES.TIP_OS," 
				+ " QO_ORDENES.CEDULA_OPERARIO_HDA, QO_ORDENES.NOMBRE_OPERARIO_HDA,"
				+ " QO_ORDENES.FECHA_CIERRE, QO_DESC_MR.DESC_ELEMENTO "
				+ " FROM QO_ORDEN_MATERIAL,QO_ORDENES, QO_DESC_MR " 
				+ " WHERE QO_ORDEN_MATERIAL.NUM_OS = QO_ORDENES.NUM_OS "
				+ " AND QO_ORDENES.NUM_ZONA = ?"
				+ " AND QO_ORDEN_MATERIAL.CO_ELEMENTO = QO_DESC_MR.CO_ELEMENTO "
				+ " AND DATE(QO_ORDENES.FECHA_CIERRE) BETWEEN ? AND ?"		
				+ " AND QO_ORDEN_MATERIAL.TIPO = 2"
				+ " ORDER BY QO_ORDENES.FECHA_CIERRE, QO_ORDENES.NUM_OS";
	}
	
	PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
	pst.setString(1, zona);
	pst.setString(2, fecha_inicial);
	pst.setString(3, fecha_final);
	ResultSet rs= conexion.Query(pst);
	boolean rsIsEmpty = !rs.next();
	int contador=0;
%>
<h2>Listado Material <%= (tipo.equals("1")?"Instalado":"Retirado") %></h2>
<% if (!rsIsEmpty) { %>
<div id="descargar">
<form action="ExportExcel.jsp" method="post" target="_blank" id="FormularioExportacion">
	<p>Exportar a Excel  <img src="images/Download.png" class="botonExcel" /></p> 
	<input type="hidden" id="datos_a_enviar" name="datos_a_enviar" /> 
	</form> 
</div>
		<table id="sellos">
			<tr>
				<th>ORDEN</th>
				<th>TIPO</th>
				<th>FECHA CIERRE</th>
				<th>CODIGO</th>
				<th>DESCRIPCION</th>
				<th>CANTIDAD</th>
				<th>COBRO</th>
				<th>ACCION</th>
				<th>CEDULA OPERARIO</th>
				<th>NOMBRE OPERARIO</th>
			</tr>
			<%
				do {
			%>
			<tr <%= (++contador%2==0)?"class='odd'":"" %>>
				<td><%= rs.getString("NUM_OS") %></td>
				<td><%= rs.getString("TIP_OS") %></td>
				<td><%= rs.getString("FECHA_CIERRE") %></td>
				<td><%= rs.getString("CO_ELEMENTO") %></td>
				<td><%= rs.getString("DESC_ELEMENTO") %></td>
				<td><%= rs.getString("CANTIDAD") %></td>
				<td><%= rs.getString("COBRO") %></td>
				<td><%= rs.getString("CO_ACCEJE") %></td>
				<td><%= rs.getString("CEDULA_OPERARIO_HDA") %></td>
				<td><%= rs.getString("NOMBRE_OPERARIO_HDA") %></td>
			</tr>
			<% } while (rs.next());%>
		</table>
		
		<script> 
	$(document).ready(function() { 
	     $(".botonExcel").click(function(event) { 
	     $("#datos_a_enviar").val( $("<div>").append( $("#sellos").eq(0).clone()).html()); 
	     $("#FormularioExportacion").submit(); 
		}); 
	}); 
</script>
		
		<% 	} else {%>
			<p>No se encontraron registros</p>
		<% }%>
		
<%
	conexion.Close();
%>