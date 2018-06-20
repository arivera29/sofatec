<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="validausuario.jsp"%>
<%@ page import="com.are.sofatec.*" %>

<%
	db conexion = new db();
	String recurso = (String)request.getParameter("recurso");
	String zona = (String)request.getParameter("zona");
	String fecha = (String)request.getParameter("fecha");
	
	String sql = "select QO_ORDENES.NUM_OS,QO_ORDENES.NIC,DESC_TIPO, " +
			"IF(ISNULL(FECHA_ANOMALIA),FECHA_CIERRE,FECHA_ANOMALIA) FECHA," +
			"QO_ORDENES.DIRECCION,QO_DATOSUM.DEPARTAMENTO, QO_DATOSUM.MUNICIPIO, " +
			"IF(ESTADO_OPER=99,OBSERVACION_OS(NUM_OS),QO_ORDENES.OBSERVACION) OBSERVACION,"+
			" QO_ORDENES.ESTADO_OPER, QO_ORDENES.F_GEN " +
			" from QO_ORDENES,QO_DATOSUM,QO_TIPOS" +
			" where QO_ORDENES.NIC = QO_DATOSUM.NIC " +
			" AND QO_ORDENES.NIS_RAD = QO_DATOSUM.NIS_RAD "+
			" and QO_ORDENES.TIP_OS = QO_TIPOS.TIPO " +
			" and QO_ORDENES.ESTADO_OPER IN(3,99) " +
			" and QO_ORDENES.TECNICO = ? " +
			" and (DATE(FECHA_ANOMALIA) = ?  OR DATE(FECHA_CIERRE) =  ? ) " ;
			
			if (!zona.equals("all")) {
				sql += " and QO_ORDENES.NUM_ZONA = '" + zona + "' ";
				
			}

			sql += " ORDER BY FECHA";

			
	java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
	pst.setString(1, recurso);
	pst.setString(2, fecha);
	pst.setString(3, fecha);
	
		
	java.sql.ResultSet rs = conexion.Query(pst);
	boolean rsIsEmpty = !rs.next();
	int cont=0;
	
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Detalle Rendimiento</title>
<LINK REL="stylesheet" TYPE="text/css" HREF="main.css">
<script src="js/prototype.js" language="JavaScript"> </script>
<link rel="Shortcut Icon" href="icono_tm.ico" type="image/x-icon" />
</head>
<body>
<img src="images/logo_mobile.png">
<h2>Ordenes Ejecutadas</h2>
<table>
	<tr>
		<tr>
		<th>Dpto.</th>
		<th>Municipio</th>
		<th>Orden</th>
		<th>NIC</th>
		<th>F.GEN</th>
		<th>Direccion</th>
		<th>Tipo orden</th>
		<th>Fecha</th>
		<th>Resultado</th>
		<th>Observacion</th>
		<th>Tiempo</th>
	</tr>
	<% if (!rsIsEmpty) { 
		java.sql.Timestamp anterior = rs.getTimestamp("FECHA");
	%>
	
	<% do { 
		java.sql.Timestamp actual = rs.getTimestamp("FECHA");
		
		
	%>
	<tr>
		<td><%= rs.getString("DEPARTAMENTO") %></td>
		<td><%= rs.getString("MUNICIPIO") %></td>
		<td><a href="DetalleOrdenLote.jsp?orden=<%= rs.getString("NUM_OS") %>" target="_blank"><%= rs.getString("NUM_OS") %></a></td>
		<td><%= rs.getString("NIC") %></td>
		<td><%= rs.getString("F_GEN") %></td>
		<td><%= rs.getString("DIRECCION") %> </td>
		<td><%= rs.getString("DESC_TIPO") %></td>
		<td><%= rs.getString("FECHA") %></td>
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
		<td><%= rs.getString("OBSERVACION").toUpperCase() %></td>
		
		<td><%= Utilidades.diferenciaDates(anterior, actual) %></td>
	</tr>
	<% 
		anterior = rs.getTimestamp("FECHA");
		cont++;
	}while(rs.next()); %>
	<tr>
		<td colspan="10">Total de ordenes: <%= cont %></td>
	</tr>
	<% } else { %>
	<tr>
		<td colspan="10">No se encontraron registros</td>
	</tr>	
	
	<% } %>

</table>
</body>
</html>
<%
	conexion.Close();
%>