<%@ page import="com.are.sofatec.*"%>
<%@ page import="java.sql.*"%>
<%
    response.setContentType("application/vnd.ms-excel");
    response.setHeader("Content-Disposition", "attachment; filename=\"sellos.xls\"");
    request.setCharacterEncoding("UTF-8");
    String zona = (String) request.getParameter("zona");
    String fecha_inicial = (String) request.getParameter("fecha_inicial");
    String fecha_final = (String) request.getParameter("fecha_final");

    db conexion = new db();
    String sql = "SELECT QO_ORDEN_PRECINTOS.*,QO_ORDENES.TIP_OS,"
            + " QO_ORDENES.CEDULA_OPERARIO_HDA, QO_ORDENES.NOMBRE_OPERARIO_HDA,"
            + " QO_ORDENES.FECHA_CIERRE "
            + " FROM QO_ORDEN_PRECINTOS,QO_ORDENES "
            + " WHERE QO_ORDEN_PRECINTOS.NUM_OS = QO_ORDENES.NUM_OS "
            + " AND QO_ORDENES.NUM_ZONA = ?"
            + " AND DATE(QO_ORDENES.FECHA_CIERRE) BETWEEN ? AND ?";

    PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
    pst.setString(1, zona);
    pst.setString(2, fecha_inicial);
    pst.setString(3, fecha_final);
    ResultSet rs = conexion.Query(pst);
    int contador = 0;
%>
<h2>Listado Sellos</h2>

<table id="sellos">
    <tr>
        <th>ORDEN</th>
        <th>TIPO</th>
        <th>FECHA CIERRE</th>
        <th>CEDULA TECNICO</th>
        <th>NOMBRE TECNICO</th>
        <th>SERIAL PRECINTO</th>
        <th>MEDIDOR</th>
    </tr>
    <%
        while (rs.next()) {
    %>
    <tr <%= (++contador % 2 == 0) ? "style='background : gray'":""%>>
        <td><%= rs.getString("NUM_OS")%></td>
        <td><%= rs.getString("TIP_OS")%></td>
        <td><%= rs.getString("FECHA_CIERRE")%></td>
        <td><%= rs.getString("CEDULA_OPERARIO_HDA")%></td>
        <td><%= rs.getString("NOMBRE_OPERARIO_HDA")%></td>
        <td><%= rs.getString("NUM_PRECIN")%></td>
        <td><%= rs.getString("NUM_APA")%></td>
    </tr>
    <% } %>
</table>

<%
    conexion.Close();
%>