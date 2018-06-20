<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="validausuario.jsp"%>
<%@ page import="com.are.sofatec.*" %>
<%@ page import="java.sql.*" %>
<%
	db conexion = new db();
	String orden = (String)request.getParameter("orden");
	String accion = (String)request.getParameter("accion");
	String tipo = (String)request.getParameter("tipo");
	
	String sql = "SELECT QO_MATER_I.*,QO_DESC_MI.DESC_ELEMENTO FROM QO_MATER_I,QO_DESC_MI WHERE QO_MATER_I.CO_ELEMENTO = QO_DESC_MI.CO_ELEMENTO AND CO_ACCEJE =? ORDER BY DESC_ELEMENTO";
	if (tipo.equals("2")) {  // material retirado
		sql = "SELECT QO_MATER_R.*,QO_DESC_MR.DESC_ELEMENTO FROM QO_MATER_R,QO_DESC_MR WHERE QO_MATER_R.CO_ELEMENTO = QO_DESC_MR.CO_ELEMENTO AND CO_ACCEJE =? ORDER BY DESC_ELEMENTO";
	}
	PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
	pst.setString(1, accion);
	ResultSet rs = conexion.Query(pst);
	boolean rsIsEmpty = !rs.next();
	

%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Seleccionar Materiales</title>
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
	
	function Seleccion(orden,accion,tipo) {
		var material = $("#material").val();
		var cantidad = $("#cantidad").val();
		var cobro = $("#cobro").val();
		
		if( material == "" || cantidad == "" || cobro == "") {  
			alert("Falta informacion");
			return;
		}
		
		if (isNaN($('#cantidad').val() )) {
			alert("cantidad no valida");
			return;
		}
		
		var url = "reportar_material.jsp";
		$.post(url,{
			orden : orden,
			accion : accion,
			tipo : tipo,
			material : material,
			cantidad : cantidad,
			cobro : cobro
		}, function (result){
			if (result.trim() == "OK") {
				alert("Material Agregado");
				<% if (tipo.equals("1")) { %>
					window.opener.ListaMaterialInstalado(orden);
				<% } %>
				<% if (tipo.equals("2")) { %>
					window.opener.ListaMaterialRetirado(orden);
				<% }  %>
				
			}else {
				$("#info").html("<img src='image/alerta.gif'> " + result);
			}			
		});
		
	}
</script>
</head>
<body>
<h2>Materiales Instalados</h2>
<% if (!rsIsEmpty)  { %>
Material: 
<select id="material" name="material">
<% do { %>	
	<option value="<%=rs.getString("CO_ELEMENTO") %>"><%=rs.getString("DESC_ELEMENTO") %></option>
<% }while (rs.next()); %>
</select>
Cantidad: <input type="text" name="cantidad" id="cantidad" value="1" size=5>
Cobro:
<select name="cobro" id="cobro">
<option value="2">No</option>
<option value="1">Si</option>
</select>
<br />
<input type="button" name="cmd_enviar" id="cmd_enviar" value="Aceptar" onclick="javascipt:Seleccion('<%= (String)request.getParameter("orden") %>','<%= (String)request.getParameter("accion") %>','<%= (String)request.getParameter("tipo") %>');" class="boton"> 
<input type="button" name="cmd_cancelar" id="cmd_cancelar" value="Caneclar" onclick="javascript:window.close();" class="boton"> 
</form>

<% }else { %>

Accion <%= (String)request.getParameter("accion") %> no tiene materiales asociados. <input type="button" name="cmd_cancelar" id="cmd_cancelar" value="Caneclar" onclick="javascript:window.close();" class="boton">
<% } %>
</body>
</html>

<%
	if (rs != null) rs.close();
	if (conexion != null) conexion.Close();
%>