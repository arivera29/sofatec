<%@page import="com.are.sofatec.db"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<%@ include file="validausuario.jsp"%>
<%@ page import="java.sql.*"%>
<%    db conexion = new db();
    String usuario = (String) session.getAttribute("usuario");
    //String perfil = (String) session.getAttribute("perfil");

    String sql = "SELECT ID,NIC, NUM_OS, COMENTARIO, CONTRATISTA, CONTNOMB, "
            + " BRIGADA,R1.RECUNOMB, R2.RECUNOMB AS INGENIERO, ID_CAMP,CAMPDESC, "
            + " ZONA, ZONANOMB, FECHA_CARGA, USUARIO_CARGA, CONFIRMADA "
            + " FROM camp_orden "
            + " INNER JOIN contratistas ON CONTRATISTA = CONTCODI "
            + " INNER JOIN recurso R1 ON BRIGADA = R1.RECUCODI "
            + " INNER JOIN recurso R2 ON INGENIERO = R2.RECUCODI "
            + " INNER JOIN camp ON ID_CAMP = campcodi "
            + " INNER JOIN zonas ON ZONA = zonacodi "
            + " INNER JOIN contratista_usuario cu ON cu.couscont = CONTCODI AND cu.coususua = ? "
            + " WHERE (NUM_OS != '') "
            + " AND ((CONFIRMADA = 0)  OR (CONFIRMADA = 1 AND DATEDIFF(CURRENT_DATE(),DATE(FECHA_CONFIRM)) <= 1)) "
            + " AND (DATEDIFF(CURRENT_DATE(),DATE(FECHA_GEN_OS)) <= 2) "
            + " ORDER BY FECHA_GEN_OS DESC";

    java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
    pst.setString(1, usuario);
    java.sql.ResultSet rs = conexion.Query(pst);

    int count = 0;


%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>Ordenes generadas</title>
        <link rel="stylesheet" href="themes/redmond/jquery.ui.all.css">
        <link rel="stylesheet" TYPE="text/css" HREF="main.css">
        <script src="js/jquery.js" type="text/javascript"></script>
        <script src="ui/jquery.ui.core.js" language="JavaScript"></script>
        <script src="ui/jquery.ui.widget.js" language="JavaScript"></script>
        <script src="ui/jquery.ui.button.js"></script>
        <script>
            $(function () {
                $(".boton", ".demo").button();
            });
            
            function confirmar(orden) {
                
                if (confirm("Esta seguro de confirmar la OS")) {
                    var url = "SrvConfirmSuministro";
                    $.get(url,{
                        orden : orden
                    }, function (data) {
                        if (data.trim() == "OK") {
                            var idTR = "#tr_" + orden;
                            var idTD = "#td_" + orden;
                            
                            $(idTD).html("");
                            $(idTR).attr('class', 'tr_alerta');
                            
                        }else {
                            alert("Error: " + data);
                        }
                        
                        
                    });
                    
                    
                }
                
                
            }

        </script>
        
        <style>
            tr.tr_alerta td {
                background-color: yellowgreen;
                color : black
            }
        </style>
        
    </head>
    <body>

        <%@ include file="header.jsp"%>
        <div class="contencenter demo">
            <h2>Ordenes generadas</h2>

            <table>
                <tr>
                    <th></th>
                    <th>FECHA</th>
                    <th>CREADOR</th>
                    <th>NIC</th>
                    <th>ORDEN</th>
                    <th>COMENTARIO</th>
                    <th>CONTRATISTA</th>
                    <th>BRIGADA</th>
                    <th>NOMBRE</th>
                    <th>INGENIERO</th>
                    <th>CAMPAÑA</th>
                    <th>ZONA</th>
                    <th>ACCION</th>
                </tr>
                <% while (rs.next()) {%>
                <% if (rs.getInt("CONFIRMADA")== 0) {  %>
                    <tr <%= ((count % 2 == 0) ? "class='odd'" : "")%> id="tr_<%= rs.getString("NUM_OS")   %>">
                <% } else { %>
                    <tr class="tr_alerta" id="tr_<%= rs.getString("NUM_OS")   %>">
                <%  }  %>
                    <td id="td_<%= rs.getString("NUM_OS") %>">
                        <% if (rs.getInt("CONFIRMADA")== 0) {  %>
                        <a href="javascript:confirmar('<%= rs.getString("NUM_OS") %>');" title="Confirmar"><img src="images/confirm.png"></a>
                        <% } %>
                    </td>
                    <td>
                        
                        <img src='images/calendario.png'><%= rs.getString("FECHA_CARGA")%>
                        
                    </td>
                    <td><img src='images/recurso.png'><%= rs.getString("USUARIO_CARGA")%></td>
                    <td><%= rs.getString("NIC")%></td>
                    <td><%= rs.getString("NUM_OS")%></td>
                    <td><%= rs.getString("COMENTARIO")%></td>
                    <td><%= rs.getString("CONTNOMB")%></td>
                    <td><%= rs.getString("BRIGADA")%></td>
                    <td><img src='images/recurso.png'><%= rs.getString("RECUNOMB")%></td>
                    <td><%= rs.getString("INGENIERO")%></td>
                    <td><strong><%= rs.getString("ID_CAMP")%></strong> <%= rs.getString("CAMPDESC")%></td>
                    <td><%= rs.getString("ZONANOMB")%></td>
                    <td><a class="boton" target="_blank" href = "consultar_orden.jsp?id=<%= rs.getInt("ID")  %>">Consultar</a></td>
                </tr>
                <% count++; %>
                <% } %>
                <tr>
                    <td colspan="13">Registros: <%= count%></td>
                </tr>
            </table>
            <% if (count == 0) { %>
            <p>No hay registros pendientes de orden de servicio</p>
            <%  }  %>
        </div>

    </body>
</html>
<%
    conexion.Close();
%>
