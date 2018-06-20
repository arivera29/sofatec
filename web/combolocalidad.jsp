<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="com.are.sofatec.*" %>
<%@ page import="java.sql.*" %>
<%
        String departamento = (String)request.getParameter("departamento");
        db conexion = new db();
        String sql = "select locacodi,locadesc from localidad where locadepa='" + departamento + "' order by locadesc";
        ResultSet rs = conexion.Query(sql);
	
        out.println("<select id=\"localidad\" name=\"localidad\">");
        out.println("<option value=\"\">Seleccionar</option>");
        while (rs.next()) {
                out.println("<option value=\"" + rs.getString("locacodi") + "\">" + rs.getString("locadesc") + "</option>");
        }
	
        out.println("</select");
        rs.close();
        conexion.Close();
%>
