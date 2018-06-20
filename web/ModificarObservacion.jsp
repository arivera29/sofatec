<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="validausuario.jsp"%>
<%@ page import="com.are.sofatec.*" %>
<%@ page import="java.sql.*" %>
<%
	db conexion = new db();
	String id = (String)request.getParameter("id");
	String tipo = (String)request.getParameter("tipo");
	String torden = (String)request.getParameter("torden");
	
	String sql = "SELECT NUM_OS,OBSERVACION FROM QO_PASOS WHERE ID =?";
	if (tipo.equals("2")) {
		sql = "SELECT NUM_OS,OBSERVACION FROM QO_NUEVOS_PASOS WHERE ID =?";
	}
	PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
	pst.setString(1, id);
	ResultSet rs = conexion.Query(pst);
	boolean rsIsEmpty = !rs.next();
	

%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Modificar Observacion</title>
<link rel="stylesheet" href="themes/redmond/jquery.ui.all.css">
<LINK REL="stylesheet" TYPE="text/css" HREF="main.css">
<script src="js/jquery.js" language="JavaScript"></script>
<script src="ui/jquery.ui.core.js" language="JavaScript"></script>
<script src="ui/jquery.ui.widget.js" language="JavaScript"></script>
<script src="ui/jquery.ui.button.js"></script>
<script type="text/javascript">
	$(function() {
		$(".boton").button();
	});
	
	function Modificar(id,tipo,torden,orden) {
		var observacion = $("#observacion").val();
				
		if (!confirm("Esta seguro de guardar el registro?")) {
			return;
		}
		
		$("#info").html("<img src='images/loading.gif'> Procesando solicitud");
		
		var url = "SrvResolverOrden";
		$.post(url,{
			operacion : "update_observacion",
			id : id,
			tipo : tipo,
			observacion : observacion
		}, function (result){
			if (result.trim() == "OK") {
				alert("Observacion modificada correctamente");
				if (tipo == "1") {
					window.opener.ListaPasos(orden,torden);
				}else {
					window.opener.ListaNuevosPasos(orden,torden);
				}
				window.close();
			}else {
				$("#info").html("<img src='images/alerta.gif'> " + result);
			}			
		});
		
	}
</script>
</head>
<body>
<% if (!rsIsEmpty)  { %>
<form name="form1" id="form1" action="" method="get">

<h2>Observacion</h2>
<div id="info"></div>
<textarea rows="5" cols="60" name="observacion" id="observacion"><%= (String)rs.getString("OBSERVACION") %></textarea>
<p>
	<input type="button" name="cmd_enviar" id="cmd_enviar" value="Aceptar" onclick="javascipt:Modificar('<%= (String)request.getParameter("id") %>','<%= (String)request.getParameter("tipo") %>','<%= (String)request.getParameter("torden") %>','<%= (String)rs.getString("NUM_OS") %>');" class="boton"> 
	<input type="button" name="cmd_cancelar" id="cmd_cancelar" value="Caneclar" onclick="javascript:window.close();" class="boton">
</p> 
</form>

<% } %>

</body>
</html>

<%
	if (rs != null) rs.close();
	if (conexion != null) conexion.Close();
%>