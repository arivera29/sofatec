<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="validausuario.jsp"%>
<%@ page import="com.are.sofatec.*" %>
<%@ page import="java.sql.*" %>
<%
	db conexion = new db();
	String orden = (String)request.getParameter("orden");
	String paso = (String)request.getParameter("paso");
	String tipo = (String)request.getParameter("tipo");
	String sql = "SELECT QO_OSACCION.* FROM QO_OSACCION WHERE NUM_PASO =? AND TIP_OS=?";
	PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
	pst.setString(1, paso);
	pst.setString(2, tipo);
	ResultSet rs = conexion.Query(pst);
	boolean rsIsEmpty = !rs.next();
	

%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Buscar Recurso</title>
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
	
	function Seleccion(orden,paso,tipo,parent) {
		var observacion = $("#observacion").val();
		if( !$("#form1 input[name='accion']:radio").is(':checked')) {  
			alert("Debe seleccionar una accion");
			return;
		}
		
		if (!confirm("Esta seguro de guardar el registro?")) {
			return;
		}
		
		$("#info").html("<img src='images/loading.gif'> Procesando solicitud");
		var accion = $('input:radio[name=accion]:checked').val()
		var url = "reportar_paso.jsp";
		$.post(url,{
			orden : orden,
			paso : paso,
			accion : accion,
			observacion : observacion,
			tipo : tipo,
			parent : parent
		}, function (result){
			if (result.trim() == "OK") {
				alert("Accion confirmada");
				window.opener.ListaPasos(orden,'<%= (String)request.getParameter("tipo") %>');
				window.opener.ListaNuevosPasos(orden,'<%= (String)request.getParameter("tipo") %>');
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
<textarea rows="5" cols="60" name="observacion" id="observacion"></textarea>
<h2>Acciones</h2>
<div id="info"></div>
<table>
<tr>
	<th>Acción</th>
	<th>Codigo</th>
	<th>Nombre</th>
</tr>
<% do { %>
<tr>
	<td><input type="radio" name="accion" id="accion" value="<%= rs.getString("CO_ACCEJE")  %>"></td>
	<td><%=rs.getString("CO_ACCEJE") %></td>
	<td><%=rs.getString("DESC_COD") %></td>
</tr>

<% }while (rs.next()); %>
</table>
<input type="button" name="cmd_enviar" id="cmd_enviar" value="Aceptar" onclick="javascipt:Seleccion('<%= (String)request.getParameter("orden") %>','<%= (String)request.getParameter("paso") %>','<%= (String)request.getParameter("tipo") %>','<%= (String)request.getParameter("parent") %>');" class="boton"> 
<input type="button" name="cmd_cancelar" id="cmd_cancelar" value="Caneclar" onclick="javascript:window.close();" class="boton"> 
</form>

<% } %>

</body>
</html>

<%
	if (rs != null) rs.close();
	if (conexion != null) conexion.Close();
%>