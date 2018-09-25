<%@page import="com.are.sofatec.db"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<%@ include file="validausuario.jsp"%>
<%@ page import="java.sql.*"%>
<%    db conexion = new db();
    String id = (String) request.getParameter("id");

    String sql = "SELECT ID,NIC, NUM_OS, COMENTARIO, CONTRATISTA, CONTNOMB, "
            + " BRIGADA,R1.RECUNOMB AS NOMB_BRIGADA, ID_CAMP,CAMPDESC, ZONA,"
            + " ZONANOMB, R2.RECUNOMB AS NOMB_INSPECTOR, R3.RECUNOMB AS NOMB_INGENIERO, "
            + " FECHA_CARGA, USUARIO_CARGA, ID_VISITA"
            + " FROM camp_orden "
            + " INNER JOIN contratistas ON CONTRATISTA = CONTCODI "
            + " INNER JOIN recurso R1 ON BRIGADA = R1.RECUCODI "
            + " INNER JOIN recurso R2 ON INSPECTOR = R2.RECUCODI "
            + " INNER JOIN recurso R3 ON INGENIERO = R3.RECUCODI "
            + " INNER JOIN camp ON ID_CAMP = campcodi "
            + " INNER JOIN zonas ON ZONA = zonacodi "
            + " WHERE ID = ?";

    java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
    pst.setString(1, id);
    java.sql.ResultSet rs = conexion.Query(pst);
    if (!rs.next()) {
        conexion.Close();
        response.sendRedirect("pendiente_generar_orden.jsp");
        return;
    }

    java.sql.ResultSet rsSuministro = null;

    sql = "SELECT NIS_RAD, COD_TAR,MUNICIPIO,LOCALIDAD,"
            + "DEPARTAMENTO,DIRECCION,APE1_CLI,APE2_CLI,NOM_CLI,REF_DIR,ACC_FINCA "
            + " FROM qo_datosum "
            + " WHERE NIC=? LIMIT 1 ";

    java.sql.PreparedStatement pst1 = conexion.getConnection().prepareStatement(sql);
    pst1.setString(1, rs.getString("NIC"));
    rsSuministro = conexion.Query(pst1);
    

    int count = 0;
    
    sql = "SELECT filename FROM camp_orden_fotos WHERE id_orden=? AND visita=0";

    java.sql.PreparedStatement pst2 = conexion.getConnection().prepareStatement(sql);
    pst2.setString(1, rs.getString("ID"));
    java.sql.ResultSet rsFotos = conexion.Query(pst2);


%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>Detalle Orden de Servicio</title>
        <link rel="stylesheet" href="themes/redmond/jquery.ui.all.css">
        <link rel="stylesheet" TYPE="text/css" HREF="main.css">
        <script src="js/jquery.js" type="text/javascript"></script>
        <script src="ui/jquery.ui.core.js" language="JavaScript"></script>
        <script src="ui/jquery.ui.widget.js" language="JavaScript"></script>
        <script src="ui/jquery.ui.button.js"></script>
        <script>
            $(function () {
                $("input:submit, a, button", ".demo").button();
                $("input:button, a, button", ".demo").button();
            });



        </script>

    </head>
    <body>

        <%@ include file="header.jsp"%>
        <div class="contencenter demo">
            <h2>Detalle orden de servicio</h2>

            <table>
                <tr>
                    <th colspan="2">Información Registro</th>
                </tr>
                <tr class="odd">
                    <td>NIC</td>
                    <td><%= rs.getString("NIC")%></td>
                </tr>
                <tr>
                    <td>Comentario</td>
                    <td><%= rs.getString("COMENTARIO")%></td>
                </tr>
                <tr class="odd">
                    <td>Contratista</td>
                    <td><%= rs.getString("CONTNOMB")%></td>
                </tr>
                <tr>
                    <td>Inspector</td>
                    <td><img src='images/recurso.png'><%= rs.getString("NOMB_INSPECTOR")%></td>
                </tr>
                <tr class="odd">
                    <td>Ingeniero</td>
                    <td><img src='images/recurso.png'><%= rs.getString("NOMB_INGENIERO")%></td>
                </tr>
                <tr>
                    <td>Brigada</td>
                    <td><img src='images/recurso.png'><%= rs.getString("NOMB_BRIGADA")%></td>
                </tr>
                <tr class="odd">
                    <td>Campaña</td>
                    <td><%= rs.getString("CAMPDESC")%></td>
                </tr>
                <tr>
                    <td>Zona</td>
                    <td><%= rs.getString("ZONANOMB")%></td>
                </tr>
                <tr class="odd">
                    <td>Fecha Creación</td>
                    <td><img src='images/calendario.png'><%= rs.getString("FECHA_CARGA")%></td>
                </tr>
                <tr>
                    <td>Usuario creador</td>
                    <td><img src='images/recurso.png'><%= rs.getString("USUARIO_CARGA")%></td>
                </tr>
                <tr class="odd">
                    <td>Orden de Servicio</td>
                    <td><%= rs.getString("NUM_OS")%></td>
                </tr>
            </table>
            <div id="info"></div>
            <%  if (rsSuministro.next()) { %>
            <h2>Informacion Suministro</h2>
            <table>
                    <tr>
                        <th colspan="4">SUMINISTRO</th>
                    </tr>
                    <tr>
                        <td>Nombre Cliente</td>
                        <td><%= rsSuministro.getString("NOM_CLI") + " " + rsSuministro.getString("APE1_CLI") + " " + rsSuministro.getString("APE2_CLI")  %></td>
                        <td>Direccion</td>
                        <td><%= rsSuministro.getString("DIRECCION")  %></td>
                    </tr>
                    <tr class="odd">
                        <td>Municipio</td>
                        <td><%= rsSuministro.getString("MUNICIPIO")  %></td>
                        <td>Localidad</td>
                        <td><%= rsSuministro.getString("LOCALIDAD")  %></td>
                    </tr>
                    <tr>
                        <td>Departamento</td>
                        <td><%= rsSuministro.getString("DEPARTAMENTO")  %></td>
                        <td>NIS RAD</td>
                        <td><%= rsSuministro.getString("NIS_RAD")  %></td>
                    </tr>
                    
            </table>
            
            
            <% }  %>
            
            <h2>Fotos</h2>
            <% int contador = 0; %>
            <%  while (rsFotos.next()) {  %>
                <img src='imagenes/<%= rsFotos.getString("filename") %>'>
            <% } %>
            
        </div>

    </body>
</html>
<%
    conexion.Close();
%>
