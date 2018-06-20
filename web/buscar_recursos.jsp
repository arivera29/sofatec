<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="validausuario.jsp"%>
<%@ page import="com.are.sofatec.*" %>
<%@ page import="java.sql.*" %>
<%
        db conexion = null;
        ResultSet rs = null;
        boolean rsIsEmpty = true;
        if (request.getParameter("criterio") != null ) {
                conexion = new db();
                String criterio = (String)request.getParameter("criterio");
                String sql = "select recucodi,recunomb from recurso where recunomb like '%" + criterio +"%' order by recunomb";
                rs = conexion.Query(sql);
                rsIsEmpty = !rs.next();
        }

%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>Buscar Recurso</title>
        <LINK REL="stylesheet" TYPE="text/css" HREF="main.css">
        <script src="js/prototype.js" language="JavaScript"></script>
        <link rel="Shortcut Icon" href="icono_tm.ico" type="image/x-icon" />
        <script type="text/javascript">
            function agregar(operacion, recurso, key) {
                var url = "";
                if (operacion == "proyecto") {
                    url = "Srv_rproyecto?operacion=agregarrecurso&recurso=" + recurso + "&proyecto=" + key;
                } else {
                    url = "Srv_rlocalidad?operacion=agregarrecurso&recurso=" + recurso + "&localidad=" + key;
                }
                new Ajax.Request(url, {
                    method: 'POST',
                    onComplete: function (transport) {
                        var resultado = transport.responseText;
                        if (resultado != 'OK') {
                            document.getElementById("info").innerHTML = resultado;
                        } else {
                            alert("Recurso agregado");
                            window.document.opener.listar(key);
                        }
                    }
                });
            }


        </script>
    </head>
    <body>
        <img src="logo-tm.jpg" />
        <form name="form1" action="buscar_recursos.jsp?key=<%=(String)request.getParameter("key") %>&operacion=<%=(String)request.getParameter("operacion") %>" method="post">
            <table>
                <tr>
                    <th colspan=2>Busqueda Recurso</th>
                </tr>
                <tr>
                    <td>Criterio</td>
                    <td><input type="text" id="criterio" name="criterio" size=40> <input type="submit" value="Buscar" /></td>
                </tr>
            </table>
            <div id="info"></div>
        </form>
        <% if (!rsIsEmpty)  { %>
        <table>
            <tr>
                <th>Codigo</th>
                <th>Nombre</th>
                <th>Acción</th>
            </tr>
            <% do { %>
            <tr>
                <td><%=rs.getString("recucodi") %></td>
                <td><%=rs.getString("recunomb") %></td>
                <td>
                    <input type="button" onclick="javascript:agregar('<%= (String)request.getParameter("operacion") %>', '<%=rs.getString("recucodi") %>', '<%=(String)request.getParameter("key") %>')" value="Agregar" >
                </td>
            </tr>

            <% }while (rs.next()); %>
        </table>
        <% } %>

        <%@ include file="foot.jsp" %>
    </body>
</html>

<%
        if (rs != null) rs.close();
        if (conexion != null) conexion.Close();
%>