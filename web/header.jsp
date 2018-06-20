<%@ page import="java.util.*"%>
<%@ page import="com.are.sofatec.*"%>
<%
	Controlador controlador = new Controlador();
	if (!controlador.verify()) {
		response.sendRedirect("suspend.jsp");
		return;
	}

	db conex = new db();
	ManejadorMenu mm = new ManejadorMenu(conex);
	String perfil = (String)session.getAttribute("perfil");
	ArrayList<Menu> menuPadres =mm.ListMenuUserParent(perfil); 
	
%>

<div class="header">
<div class="usuario">
Bienvenid@ <%=(String)session.getAttribute("usuario") %> <a href="logout.jsp"><b>Cerrar Sesion</b></a>
</div>
</div>
<div class="contenmenu">
<div id="menuv">
<ul>
<% for (Menu m : menuPadres) { %>
	<li>
		<div><%= m.getTitulo() %></div>
		<% ArrayList<Menu> menuHijos = mm.ListMenuUserChild(perfil, m.getMenuid()); %>
		<% if (menuHijos.size() > 0 ) { %>
			<ul>
		<%for(Menu mh: menuHijos) {  %>
		
			<li><a href="<%= mh.getUrl()  %>"><%= mh.getTitulo() %></a></li>
		
		<% } %>
		</ul>
		<% }  // fin si %>
	</li>

<% } %>
</ul>
</div>
</div>
<%  conex.Close(); %>