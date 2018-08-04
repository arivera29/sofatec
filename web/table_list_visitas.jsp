<%@ page import="com.are.sofatec.*" %>

<%
	db conexion = new db();
	String fecha_inicial = (String)request.getParameter("fecha_inicial");
	String fecha_final = (String)request.getParameter("fecha_final");
        String tipo = (String)request.getParameter("tipo");
        String reporte = (String)request.getParameter("reporte");
        
	
	String sql = "SELECT id,tipo,nic,observacion,departamento,municipio,"
                + "direccion,barrio,cliente,brigada,recunomb,fecha_ingreso, "
                + "fecha_asignacion, fecha_carga, usuario_carga, estado,"
                + "fecha_ejecucion, estado_ejecucion, anomalia, "
                + "QO_ANOM.DESC_COD, obs_anomalia  "
                + " FROM VISITAS "
                + " INNER JOIN RECURSO ON RECURSO.recucodi = VISITAS.brigada "
                + " INNER JOIN QO_ANOM ON VISITAS.anomalia = QO_ANOM.COD "
                + " WHERE CONVERT(fecha_carga,DATE) BETWEEN ? AND ? ";
        
        
        
        sql += " ORDER BY FECHA_CARGA";
			
	java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
	pst.setString(1, fecha_inicial);
	pst.setString(2, fecha_final);
	
	java.sql.ResultSet rs = conexion.Query(pst);
	boolean rsIsEmpty = !rs.next();
	int cont=0;
	
%>
<table id="Exportar_a_Excel">
	<tr>
		<th>ID</th>
		<th>TIPO</th>
		<th>NIC</th>
		<th>OBSERVACION</th>
		<th>DPTO</th>
                <th>MUNICIPIO</th>
		<th>DIRECCION</th>
		<th>BARRIO</th>
		<th>F.CARGA</th>
		<th>US.CARGA</th>
                <th>ESTADO</th>
		<th>REPORTE</th>
                <th>ANOMALIA</th>
                <th>ASIG</th>

	</tr>
	<% if (!rsIsEmpty) { 
	%>
	
	<% do { 
	%>
	<tr <%= (cont%2==0?"class=\"odd\"":"") %>>
		<td><%= rs.getString("ID") %></td>
                <td><%= rs.getString("TIPO").equals("1")?"INSPECCION":"CENSO" %></td>
		<td><%= rs.getString("NIC") %></td>
                <td><%= rs.getString("OBSERVACION") %></td>
		<td><%= rs.getString("DEPARTAMENTO") %></td>
                <td><%= rs.getString("MUNICIPIO") %></td>
		<td><%= rs.getString("DIRECCION") %></td>
                <td><%= rs.getString("BARRIO") %></td>
		<td><%= rs.getString("FECHA_CARGA") %></td>
		<td><%= rs.getString("USUARIO_CARGA") %></td>
		<td>
                    <% 
                        switch(rs.getInt("ESTADO")) {
                            case 0:
                                out.print("PEND.ASIGNAR");
                                break;
                            case 1:
                                out.print("ASIGNADA");
                                break;
                            case 2:
                                out.print("EJECUTADA");
                                break;
                            default:
                        }
                            
                    
                    
                    
                    %>
                
                </td>
                <td>
                    <% 
                        
                        
                        switch (rs.getInt("ESTADO_EJECUCION")) {
                            case 0: 
                                out.print("-");
                                break;
                            case 1:
                                out.print("EFECTIVA");
                                break;
                            case 3:
                                out.print("ANOMALIA");
                                break;
                            default:
                                out.print("");
                        } 
                    
                    
                    %>
                
                </td>
		<td><%= rs.getString("DESC_COD") %></td>
                <td><%= rs.getString("RECUNOMB") %></td>
	</tr>
	<% 
		cont++;
	}while(rs.next()); %>
	<tr>
		<td colspan="14">Total de ordenes: <%= cont %></td>
	</tr>
	<% } else { %>
	<tr>
		<td colspan="14">No se encontraron registros</td>
	</tr>	
	
	<% } %>
	</table>
<%
	conexion.Close();
%>