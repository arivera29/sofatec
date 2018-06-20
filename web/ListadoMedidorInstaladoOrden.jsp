<%@ page import="com.are.sofatec.*"%>
<%@ page import="java.sql.*"%>
<%
        request.setCharacterEncoding("UTF-8");
        String orden = (String) request.getParameter("orden");
        db conexion = new db();
        String sql = "SELECT QO_MEDIDOR.* "
                        + " FROM QO_MEDIDOR "
                        + " WHERE NUM_OS =? ";
        PreparedStatement pst = conexion.getConnection().prepareStatement(
                        sql);
        pst.setString(1, orden);
        ResultSet rsMedidor = conexion.Query(pst);
        boolean rsrsMedidorIsEmpty = !rsMedidor.next();

        int contador = 0;
	
        ManejadorUsuarios mu = new ManejadorUsuarios(conexion);
        mu.find((String)session.getAttribute("usuario"));
        Usuarios usuario = mu.getUsuario();
	
%>
<h2>Medidor Instalado</h2>
<%
        if (!rsrsMedidorIsEmpty) {
%>
<table>
    <tr>
        <th>MEDIDOR</th>
        <th>MARCA</th>
        <th>TIPO</th>
        <th>FASE</th>
        <th>TENSION</th>
        <th>F.INST.</th>
        <th>DIMENSION</th>
        <th>DIAMETRO</th>
        <th>LECTURA</th>
        <th>ACCION</th>
    </tr>
    <tr <%= (++contador%2==0)?"class='odd'":"" %>>
        <td><%=(String) rsMedidor.getString("NUM_APA")%></td>
        <td><%=(String) rsMedidor.getString("CO_MARCA")%></td>
        <td><%=(String) rsMedidor.getString("TIP_APA")%></td>
        <td><%=(String) rsMedidor.getString("TIP_FASE")%></td>
        <td><%=(String) rsMedidor.getString("TIP_TENSION")%></td>
        <td><%=(String) rsMedidor.getString("F_INST")%></td>
        <td><%=(String) rsMedidor.getString("DIMEN_CONEX")%></td>
        <td><%=(String) rsMedidor.getString("DIAMETRO")%></td>
        <td><%=(String) rsMedidor.getString("LECTURA")%></td>
        <td>
            <% if (usuario.getResolver()== 1) { %>
            <a href="javascript:EditarMedidor('<%= (String)request.getParameter("orden") %>','<%= rsMedidor.getString("NUM_APA") %>');" class="boton">Editar</a>
            <% } %>
        </td>
    </tr>
</table>
<%
        } else {
%>
No Hay informacion de Medidor Instalado 
<% if (usuario.getResolver()== 1) { %>
<button type="button" name="cmd_medidor" id="cmd_medidor" onclick="javascript:AgregarMedidor('<%= (String)request.getParameter("orden") %>')" class="boton" >Agregar Medidor</button>
<% } %>
<%
        }
        conexion.Close();
%>