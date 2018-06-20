<%@ page import="com.are.sofatec.*" %>

<%
        db conexion = new db();
        String departamento = (String)request.getParameter("departamento");
        String tipo = (String)request.getParameter("tipo");
	
        String fecha_inicial = (String)request.getParameter("fecha_inicial");
        String hora1 = (String)request.getParameter("hora1");
	
        String fecha_final = (String)request.getParameter("fecha_final");
        String hora2 = (String)request.getParameter("hora2");
	
        fecha_inicial += " " + hora1;
        fecha_final += " " + hora2;
	
        String sql = "select reportes.orden,orders.nic,tiordesc,locadesc, " +
                "reportes.tipo,date(reportes.fecha) fecha, time(reportes.fecha) hora," +
                "orders.nom_via,orders.nom_calle,orders.crucero,orders.placa,orders.interior, " +
                "recurso.recunomb,orders.observacion,imagenes.filename " +
                " from reportes,orders,localidad,tipo_orden,recurso,imagenes " +
                " where orders.orden = reportes.orden " +
                " and orders.tipo = tiorcodi " +
                " and orders.orden = imagenes.orden " +
                " and orders.localidad = locacodi " +
                " and reportes.recurso = recurso.recucodi " +
                " and reportes.fecha between   ? and ? " ;
		
                if (!departamento.equals("all")) {
                        sql += " and locadepa = '" + departamento + "' ";
			
                }
                if (!tipo.equals("all") ){
                        sql += " and orders.tipo = '" + tipo + "' ";
			
                }
	
	
                sql += " order by fecha,locadesc,recunomb";
			
        java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        pst.setString(1, fecha_inicial);
        pst.setString(2, fecha_final);
	
        java.sql.ResultSet rs = conexion.Query(pst);
        boolean rsIsEmpty = !rs.next();
        int cont=0;
	
%>
<table id="Exportar_a_Excel">
    <tr>
        <th>Municipio</th>
        <th>Orden</th>
        <th>NIC</th>
        <th>Direccion</th>
        <th>Recurso</th>
        <th>Tipo orden</th>
        <th>Tipo Reporte</th>
        <th>Foto</th>
        <th>Fecha</th>
        <th>Hora</th>
    </tr>
    <% if (!rsIsEmpty) { 
    %>

    <% do { 
    %>
    <tr <%= (cont%2==0?"class=\"odd\"":"") %>>
        <td><%= rs.getString("locadesc") %></td>
        <td><%= rs.getString("orden") %></td>
        <td><%= rs.getString("nic") %></td>
        <td><%= rs.getString("nom_via") + " "+ rs.getString("nom_calle") + " " + rs.getString("crucero") + " " + rs.getString("placa") + " " + rs.getString("interior") %> </td>
        <td><%= rs.getString("recunomb") %></td>
        <td><%= rs.getString("tiordesc") %></td>
        <td><%= rs.getString("tipo") %></td>
        <td><a href="imagenes/<%= rs.getString("filename") %>" target="_blank">Descargar</a></td>
        <td><%= rs.getString("fecha") %></td>
        <td><%= rs.getString("hora") %></td>
    </tr>
    <% 
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
<%
        conexion.Close();
%>