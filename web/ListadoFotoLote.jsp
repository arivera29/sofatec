<%@ page import="com.are.sofatec.*" %>

<%
	db conexion = new db();
	String zona = (String)request.getParameter("zona");
	String tipo = (String)request.getParameter("tipo");
	
	String fecha_inicial = (String)request.getParameter("fecha_inicial");
	String hora1 = (String)request.getParameter("hora1");
	
	String fecha_final = (String)request.getParameter("fecha_final");
	String hora2 = (String)request.getParameter("hora2");
	
	fecha_inicial += " " + hora1;
	fecha_final += " " + hora2;
	
	String sql = "select QO_ORDENES.NUM_OS,QO_ORDENES.NIC,DESC_TIPO, " +
		"DATE(IF(ISNULL(FECHA_ANOMALIA),FECHA_CIERRE,FECHA_ANOMALIA)) fecha_cierre," +
		"TIME(IF(ISNULL(FECHA_ANOMALIA),FECHA_CIERRE,FECHA_ANOMALIA)) hora," +
		"QO_ORDENES.DIRECCION,QO_DATOSUM.DEPARTAMENTO, QO_DATOSUM.MUNICIPIO, " +
		"recurso.recunomb,QO_ORDENES.OBSERVACION,imagenes.filename, QO_ORDENES.ESTADO_OPER " +
		" from QO_ORDENES,QO_DATOSUM,QO_TIPOS,recurso,imagenes " +
		" where QO_ORDENES.NIC = QO_DATOSUM.NIC " +
		" AND QO_ORDENES.NIS_RAD = QO_DATOSUM.NIS_RAD "+
		" and QO_ORDENES.TIP_OS = QO_TIPOS.TIPO " +
		" and QO_ORDENES.NUM_OS = imagenes.orden " +
		" and QO_ORDENES.TECNICO = recurso.recucodi " +
		" and (FECHA_ANOMALIA BETWEEN ? AND ?  OR FECHA_CIERRE BETWEEN ? AND ? ) " ;
		
		if (!zona.equals("all")) {
			sql += " and QO_ORDENES.NUM_ZONA = '" + zona + "' ";
			
		}
		if (!tipo.equals("all") ){
			sql += " and QO_ORDENES.TIP_OS = '" + tipo + "' ";
			
		}
	
	
		sql += " ORDER BY DEPARTAMENTO, MUNICIPIO";
			
	java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
	pst.setString(1, fecha_inicial);
	pst.setString(2, fecha_final);
	pst.setString(3, fecha_inicial);
	pst.setString(4, fecha_final);
	
	java.sql.ResultSet rs = conexion.Query(pst);
	boolean rsIsEmpty = !rs.next();
	int cont=0;
	
%>
<table id="Exportar_a_Excel">
	<tr>
		<th>Dpto.</th>
		<th>Municipio</th>
		<th>Orden</th>
		<th>NIC</th>
		<th>Direccion</th>
		<th>Recurso</th>
		<th>Tipo orden</th>
		<th>Fecha</th>
		<th>Hora</th>
		<th>Resultado</th>
		<th>Foto</th>
	</tr>
	<% if (!rsIsEmpty) { 
	%>
	
	<% do { 
	%>
	<tr <%= (cont%2==0?"class=\"odd\"":"") %>>
		<td><%= rs.getString("DEPARTAMENTO") %></td>
		<td><%= rs.getString("MUNICIPIO") %></td>
		<td><a href="DetalleOrdenLote.jsp?orden=<%= rs.getString("NUM_OS") %>" target="_blank"><%= rs.getString("NUM_OS") %></a></td>
		<td><%= rs.getString("NIC") %></td>
		<td><%= rs.getString("DIRECCION") %> </td>
		<td><%= rs.getString("recunomb") %></td>
		<td><%= rs.getString("DESC_TIPO") %></td>
		<td><%= rs.getDate("FECHA_CIERRE") %></td>
		<td><%= rs.getString("HORA") %></td>
		<td>
			<% 
				switch(rs.getInt("ESTADO_OPER")) {
				case 2:
					out.print("ASIGANDA");
					break;
				case 3:
					out.print("<label class=\"red\">ANOMALIA</label>");
					break;
				case 99:
					out.print("EFECTIVA");
					break;
				}
			 %>
			
		</td>
		<td><a href="imagenes/<%= rs.getString("filename") %>" target="_blank"><img src="imagenes/<%= rs.getString("filename") %>" width="100" height="100" ></a></td>
	</tr>
	<% 
		cont++;
	}while(rs.next()); %>
	<tr>
		<td colspan="11">Total de ordenes: <%= cont %></td>
	</tr>
	<% } else { %>
	<tr>
		<td colspan="11">No se encontraron registros</td>
	</tr>	
	
	<% } %>
	</table>
<%
	conexion.Close();
%>