<%@ page import="com.are.sofatec.*"%>
<%@ page import="java.sql.*"%>
<%

        request.setCharacterEncoding("UTF-8");
	String zona = (String)request.getParameter("zona");
	String fecha_inicial = (String)request.getParameter("fecha_inicial");
	String fecha_final = (String)request.getParameter("fecha_final");
	
	db conexion = new db();
	String sql= "SELECT * FROM (SELECT QO_PASOS.NUM_OS,QO_PASOS.CO_ACCEJE,QO_PASOS.COBRO,QO_ORDENES.TIP_OS," 
			+ " QO_ORDENES.CEDULA_OPERARIO_HDA, QO_ORDENES.NOMBRE_OPERARIO_HDA,"
			+ " QO_ORDENES.FECHA_CIERRE, QO_CODIGOS.DESC_COD, QO_PASOS.OBSERVACION, QO_ORDENES.USUARIO_CARGA "
			+ " FROM QO_PASOS,QO_ORDENES, QO_CODIGOS " 
			+ " WHERE QO_PASOS.NUM_OS = QO_ORDENES.NUM_OS "
			+ " AND QO_ORDENES.NUM_ZONA = ?"
			+ " AND QO_PASOS.CO_ACCEJE = QO_CODIGOS.COD "
			+ " AND DATE(QO_ORDENES.FECHA_CIERRE) BETWEEN ? AND ? "
			+ " UNION ALL SELECT QO_NUEVOS_PASOS.NUM_OS,QO_NUEVOS_PASOS.CO_ACCEJE,"
			+ " QO_NUEVOS_PASOS.COBRO ,QO_ORDENES.TIP_OS," 
			+ " QO_ORDENES.CEDULA_OPERARIO_HDA, QO_ORDENES.NOMBRE_OPERARIO_HDA,"
			+ " QO_ORDENES.FECHA_CIERRE, QO_CODIGOS.DESC_COD, QO_NUEVOS_PASOS.OBSERVACION, QO_ORDENES.USUARIO_CARGA "
			+ " FROM QO_NUEVOS_PASOS,QO_ORDENES, QO_CODIGOS " 
			+ " WHERE QO_NUEVOS_PASOS.NUM_OS = QO_ORDENES.NUM_OS "
			+ " AND QO_ORDENES.NUM_ZONA = ?"
			+ " AND QO_NUEVOS_PASOS.CO_ACCEJE = QO_CODIGOS.COD "
			+ " AND DATE(QO_ORDENES.FECHA_CIERRE) BETWEEN ? AND ? )" 
			+ " A ORDER BY FECHA_CIERRE, NUM_OS";
	PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
	pst.setString(1, zona);
	pst.setString(2, fecha_inicial);
	pst.setString(3, fecha_final);
	pst.setString(4, zona);
	pst.setString(5, fecha_inicial);
	pst.setString(6, fecha_final);
	ResultSet rs= conexion.Query(pst);
	boolean rsIsEmpty = !rs.next();
	int contador=0;
%>
<h2>Listado Acciones</h2>
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
				<th>OBSERVACION</th>
				<th>COBRO</th>
				<th>CEDULA OPERARIO</th>
				<th>NOMBRE OPERARIO</th>
                                <th>USUARIO</th>
			</tr>
			<%
				do {
			%>
			<tr <%= (++contador%2==0)?"class='odd'":"" %>>
				<td><%= rs.getString("NUM_OS") %></td>
				<td><%= rs.getString("TIP_OS") %></td>
				<td><%= rs.getString("FECHA_CIERRE") %></td>
				<td><%= rs.getString("CO_ACCEJE") %></td>
				<td><%= rs.getString("DESC_COD") %></td>
				<td><%= rs.getString("OBSERVACION") %></td>
				<td><%= rs.getString("COBRO") %></td>
				<td><%= rs.getString("CEDULA_OPERARIO_HDA") %></td>
				<td><%= rs.getString("NOMBRE_OPERARIO_HDA") %></td>
                                <td><%= rs.getString("USUARIO_CARGA") %></td>
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