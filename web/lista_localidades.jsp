<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="validausuario.jsp"%>
<%@ page import="com.are.sofatec.*" %>
<%@ page import="java.sql.*" %>
<%
        db conexion = new db();
        ResultSet rs = null;
        boolean rsIsEmpty = true;
	
        departamento dpto = new departamento(conexion);

        String opcion = "";
        if (request.getParameter("opcion") != null) {
                opcion = (String)request.getParameter("opcion");
        }
        String departamento = "";
        if (request.getParameter("departamento") != null) {
                departamento = (String)request.getParameter("departamento");
        }
        if (opcion.equals("1")) {
		
                String sql = "select depadesc,locacodi,locadesc from localidad,departamentos where locadepa=depacodi ";
                if (!departamento.equals("")) {
        sql += " and locadepa='" + departamento + "' ";
                }
		
                sql += " order by depadesc,locadesc";
                rs = conexion.Query(sql);
                rsIsEmpty = !rs.next();
	
        }
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Listado localidades</title>
        <LINK REL="stylesheet" TYPE="text/css" HREF="main.css">
        <link rel="Shortcut Icon" href="icono_tm.ico" type="image/x-icon" />
        <script src="js/jquery-1.3.2.js" language="JavaScript"></script>
        <script type="text/javascript">
            function validar() {
                var departamento = $("#departamento").val();
                if (departamento == "" || departamento == null) {
                    alert("Debe seleccionar el departamento");
                    return false;
                }
                return true;
            }
            function agregar_recurso(localidad) {
                window.location.href = "recursolocalidad.jsp?localidad=" + localidad;
            }
        </script>
    </head>
    <body>
        <%@ include file="header.jsp" %>
        <h2>Listado localidades disponibles</h2>
        <form action="lista_localidades.jsp?opcion=1" method="post" name="form1" onsubmit="return validar();">
            Filtro por departamento: <%= dpto.CreateSelectHTML("departamento") %>  <input type="submit" value="Consultar" >
        </form>
        <% if (opcion.equals("1")) { %>
        <table>
            <tr>
                <th>Departamento</th>
                <th>Codigo</th>
                <th>Descripcion</th>
                <th>Acción</th>
            </tr>
            <% if (!rsIsEmpty)  { %>
            <% do { %>
            <tr>
                <td><%=rs.getString("depadesc") %></td>
                <td><%=rs.getString("locacodi") %></td>
                <td><%=rs.getString("locadesc") %></td>
                <td>
                    <% if (opcion.equals("1")) { %>
                    <input type="button" onclick="javascript:agregar_recurso('<%=rs.getString("locacodi") %>')" name="cmd_agregar" value="Gestion Recurso" >
                    <% } %>
                </td>
            </tr>


            <% }while (rs.next()); %>
            <% }else { %>
            <tr>
                <td colspan="4">No se encontraron registros</td>
            </tr>

            <% } %>
        </table>
        <% } %>
        <br/>
        <br/>
        <%@ include file="foot.jsp" %>
    </body>
</html>

<%
        if (rs != null) rs.close();
        if (conexion != null) conexion.Close();
%>