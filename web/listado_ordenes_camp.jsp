<%@page import="com.are.censo.controlador.CtlOrden"%>
<%@ page import="com.are.sofatec.*" %>

<%
    db conexion = new db();
    String fecha_inicial = (String) request.getParameter("fecha_inicial");
    String fecha_final = (String) request.getParameter("fecha_final");
    String contratista = (String) request.getParameter("contratista");
    String estado = (String) request.getParameter("estado");
    String camp = (String) request.getParameter("camp");

    String sql = "SELECT * FROM VIEW_ORDENES_CAMP WHERE CONVERT(FECHA_CARGA,DATE) BETWEEN ? AND ? ";
    if (!contratista.equals("all")) {
        sql += " AND ID_CONTRATA = " + contratista;
    }
    if (!estado.equals("all")) {
        if (estado.equals("1")) {
            sql += " AND ORDEN != 'PENDIENTE'";
        }
        if (estado.equals("2")) {
            sql += " AND ORDEN = 'PENDIENTE'";
        }

    }
    if (!camp.equals("all")) {
        sql += " AND ID_CAMP = '" + camp + "'";
    }

    sql += " ORDER BY FECHA_CARGA";

    java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
    pst.setString(1, fecha_inicial);
    pst.setString(2, fecha_final);

    java.sql.ResultSet rs = conexion.Query(pst);
    boolean rsIsEmpty = !rs.next();
    int cont = 0;

    java.text.SimpleDateFormat ft = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm");


%>
<table id="Exportar_a_Excel">
    <tr>
        <th>F</th>
        <th>FECHA</th>
        <th>USUARIO</th>
        <th>NIC</th>
        <th>NUM_ORDEN</th>
        <th>COMENTARIO</th>
        <th>COD. BRIGADA</th>
        <th>BRIGADA</th>
        <th>INSPECTOR</th>
        <th>INGENIERO</th>
        <th>TIPO_BRIGADA</th>
        <th>CONTRATA</th>
        <th>CAMPA�A</th>
        <th>TIPO_INGRESO</th>
        <th>F.GEN.OS</th>


    </tr>
    <% if (!rsIsEmpty) {
    %>

    <% do {
    %>
    <tr <%= (cont % 2 == 0 ? "class=\"odd\"" : "")%>>
        <td>
            <% if (CtlOrden.ContadorPhoto(rs.getInt("ID"), conexion) > 0) {%>
            <a href="javascript:verFotos(<%= rs.getInt("ID")%>)"><img alt="Ver fotos" src="images/buscar.png"></a>
                <% }  %>
        </td>
        <td><%= ft.format(rs.getTimestamp("FECHA_CARGA"))%></td>
        <td><%= rs.getString("USUARIO_CARGA")%></td>
        <td><%= rs.getString("NIC")%></td>
        <td><strong><%= rs.getString("ORDEN")%></strong></td>
        <td><%= rs.getString("COMENTARIO")%></td>
        <td><%= rs.getString("ID_BRIGADA")%></td>
        <td><%= rs.getString("NOMBRE_BRIGADA")%></td>
        <td><%= rs.getString("NOMBRE_INSPECTOR")%></td>
        <td><%= rs.getString("NOMBRE_INGENIERO")%></td>
        <td><%= rs.getString("TIPO_BRIGADA")%></td>
        <td><%= rs.getString("NOMBRE_CONTRATA")%></td>
        <td><%= rs.getString("NOMBRE_CAMP")%></td>
        <td><%= rs.getString("TIPO_INGRESO")%></td>
        <%
            String fecha = "";
            java.sql.Date f = rs.getDate("FECHA_GEN_OS");
            if (!rs.wasNull()) {
                fecha = ft.format(rs.getTimestamp("FECHA_GEN_OS"));
            }

        %>
        <td><%= fecha%></td>
        
    </tr>
    <%
                cont++;
            } while (rs.next());%>
    <tr>
        <td colspan="15">Total de ordenes: <%= cont%></td>
    </tr>
    <% } else { %>
    <tr>
        <td colspan="15">No se encontraron registros</td>
    </tr>	

    <% } %>
</table>
<%
    conexion.Close();
%>