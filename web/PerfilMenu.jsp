<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="com.are.sofatec.*"%>
<%@include file="validausuario.jsp"%>
<%

if (request.getParameter("id") == null) {
	response.sendRedirect("Perfiles_v2.jsp");
	return;
}

String id = (String)request.getParameter("id");
db conexion = new db();
ManejadorPerfiles mp = new ManejadorPerfiles(conexion);
String usuario = (String)session.getAttribute("usuario");

Perfiles p = new Perfiles();
if (mp.Find(id)) {
	p = mp.getPerfil();
}else {
	response.sendRedirect("Perfiles.jsp");
	return;
}

ArrayList<Menu> menu = new ArrayList<Menu>();
ManejadorMenu manager_menu = new ManejadorMenu(conexion);
menu = manager_menu.list("INI");

ManejadorPerfilesMenu mpm = new ManejadorPerfilesMenu(conexion);

%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Menu del perfil</title>
<link rel="stylesheet" href="themes/redmond/jquery.ui.all.css">
<LINK REL="stylesheet" TYPE="text/css" HREF="main.css">
<script src="js/jquery.js" language="JavaScript"></script>
<script src="ui/jquery.ui.core.js" language="JavaScript"></script>
<script src="ui/jquery.ui.widget.js" language="JavaScript"></script>
<script src="ui/jquery.ui.button.js"></script>

<script type="text/javascript">
$(function() {
	$( "input:submit, a, button", ".demo" ).button();
	$( "input:button, a, button", ".demo" ).button();
});
</script>

<script type="text/javascript">

function guardar(perfil) {
	var contador = 0;
	$(':checkbox').each(function () {
		if (this.checked == true) {
			contador++;
		} 
	});

	if(contador == 0){
	    alert("Debe seleccionar al menos un item de menu");
	    return false;
	}
	
	var url = "SrvPerfilesMenu?operacion=add&perfil="+ perfil;
	 var cad = "";
	 var datos = new Array();
	 $(':checkbox').each(function () {
			if (this.checked == true) {
			    var valor = $(this).val();
			    cad = "menu=" + valor;
			    datos.push(cad);
			}
		}); 
	cad = datos.join("&");
	url += "&" + cad;
	$.get(url,procesar); 
	}

	function procesar(result) {
		if (result == "OK") {
			$("#info").html("Menu actualizado correctamente");
		}else {
			$("#info").html(result);
		}
	}
	function NoSelectAllCheckbox() {
		$(':checkbox').each(function () {
			this.checked = false;
		});
	}
	function SelectAllCheckbox() {
		$(':checkbox').each(function () {
			this.checked = true;
		});
	}
</script>
</head>
<body>
<%@include file="header.jsp"%>
<div class="contencenter demo">
<h2><img alt="Perfiles" src="images/perfiles.png">Perfiles</h2>
<div id="info"></div>
	<table>
		<tr>
			<th colspan="4">Perfil</th>
		</tr>
		<tr>
			<td>Id</td>
			<td><%= p.getId() %></td>
		</tr>
		<tr>
			<td>Nombre</td>
			<td><%= p.getPerfil() %></td>
		</tr>
	</table>
<a  href="javascript:guardar('<%=p.getId() %>')">Guardar</a>
<a  href="Perfiles.jsp">Cancelar</a>

<div id="tb_menu">
<h2>Menu registrado</h2>
<% if (menu.size() > 0) { %>
<form name="form1" action="">
<a href="javascript:SelectAllCheckbox()">Seleccionar todos</a><a href="javascript:NoSelectAllCheckbox()">Quitar todos</a>
<table>
	<% 
	int fila = 1;
	for (Menu m : menu) { 
		if (m.getPadreid().equals(m.getMenuid())) {  // un menu padre
	%>
		<tr >	
			<th><input type="checkbox" id="menu[]" value="<%=m.getMenuid() %>" <%= (mpm.isCreated(p.getId(), m.getMenuid())?"checked":"") %> ></th>
			<th><%= m.getTitulo() %></th>
		</tr>
	<% } else { %>
		<tr <%= (fila%2==0?"class='odd'":"") %>>	
			<td><input type="checkbox" id="menu[]" value="<%=m.getMenuid() %>" <%= (mpm.isCreated(p.getId(), m.getMenuid())?"checked":"") %>></td>
			<td><%= m.getTitulo() %></td>
		</tr>
	
	
	<% } %>
	<% 
	fila++;
	} 
	%>
</table>

</form>
<% } else { %>
<strong>No se encontraron item de menu, comunicarse con el administrador del sistema</strong>

<% } %>

</div>
</div>
<%@include file="foot.jsp"%>
</body>
</html>
<% 
	conexion.Close();
%>