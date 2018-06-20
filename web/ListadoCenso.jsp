<%@ page import="com.are.sofatec.*"%>
<%@ page import="java.sql.*"%>
<%
        request.setCharacterEncoding("UTF-8");
        String zona = (String)request.getParameter("zona");
        String fecha_inicial = (String)request.getParameter("fecha_inicial");
        String fecha_final = (String)request.getParameter("fecha_final");
	
        db conexion = new db();
        String sql ="SELECT QO_ORDENES.NIC,QO_ORDENES.NUM_OS,QO_ORDENES.TIP_OS,QO_ORDENES.DESC_TIPO_ORDEN, " 
                        + " QO_ORDENES.CEDULA_OPERARIO_HDA, QO_ORDENES.NOMBRE_OPERARIO_HDA,"
                        + " QO_ORDENES.FECHA_CIERRE, QO_ORDENES.NUM_CT, QO_ORDENES.NUM_MT, "
                        + " QO_ORDENES.NUM_ACTA, QO_ORDENES.FECHA_CENSO, "
                        + " QO_ORDENES.TARIFA, QO_TARIFAS.DESC_TAR, QO_ORDENES.CIUU, QO_CIUU.DESC_CIUU, "
                        + " (SELECT SUM(QO_CENSO.CANTIDAD*QO_EQUIPOS.CARGA) "
                        + "    FROM QO_CENSO,QO_EQUIPOS "
                        + "    WHERE QO_CENSO.NUM_OS=QO_ORDENES.NUM_OS "
                        + "    AND QO_CENSO.ID_EQUIPO = QO_EQUIPOS.ID) AS CENSO "
                        + " FROM QO_CIUU, QO_TARIFAS,QO_ORDENES " 
                        + " WHERE QO_CIUU.COD_CIUU = QO_ORDENES.CIUU "
                        + " AND QO_TARIFAS.COD_TAR = QO_ORDENES.TARIFA"
                        + " AND QO_ORDENES.NUM_ZONA = ?"
                        + " AND DATE(QO_ORDENES.FECHA_CIERRE) BETWEEN ? AND ?"		
                                        ;
	
        PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        pst.setString(1, zona);
        pst.setString(2, fecha_inicial);
        pst.setString(3, fecha_final);
        ResultSet rs= conexion.Query(pst);
        boolean rsIsEmpty = !rs.next();
        int contador=0;
%>
<h2>Listado Sellos</h2>
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
        <th>NIC</th>
        <th>TIPO</th>
        <th>FECHA CENSO</th>
        <th>ACTA</th>
        <th>NOMBRE TECNICO</th>
        <th>CT</th>
        <th>MT</th>
        <th>TARIFA</th>
        <th>CIUU</th>
        <th>CENSO</th>
    </tr>
    <%
            do {
    %>
    <tr <%= (++contador%2==0)?"class='odd'":"" %>>
        <td><%= rs.getString("NUM_OS") %></td>
        <td><%= rs.getString("NIC") %></td>
        <td><%= rs.getString("DESC_TIPO_ORDEN") %></td>
        <td><%= rs.getString("FECHA_CENSO") %></td>
        <td><%= rs.getString("NUM_ACTA") %></td>
        <td><%= rs.getString("NOMBRE_OPERARIO_HDA") %></td>
        <td><%= rs.getString("NUM_CT") %></td>
        <td><%= rs.getString("NUM_MT") %></td>
        <td><%= rs.getString("DESC_TAR") %></td>
        <td><%= rs.getString("DESC_CIUU") %></td>
        <td><%= rs.getString("CENSO") %></td>
    </tr>
    <% } while (rs.next());%>
</table>

<script>
    $(document).ready(function () {
        $(".botonExcel").click(function (event) {
            $("#datos_a_enviar").val($("<div>").append($("#sellos").eq(0).clone()).html());
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