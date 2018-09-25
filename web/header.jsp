<%@page import="com.are.manejadores.ManejadorCamp"%>
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
<style>
    .badge1 {
       position:relative;
    }
    
    .badge1[data-badge]:after {
       content:attr(data-badge);
       position:absolute;
       top:0;
       left: 100px;
       right:0;
       font-size:12px;
       background:green;
       color:white;
       width:24px;
       height:24px;
       text-align:center;
       line-height:18px;
       border-radius:50%;
       box-shadow:0 0 1px #333;
    }    
    
</style>
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
		
			<li>
                            
                            
                            <% if (mh.getMenuid().equals("10030")) {  %>
                                <% 
                                    int cntx=0;
                                    ManejadorCamp cCamp = new ManejadorCamp(conex);
                                    cntx = cCamp.contadorPendienteUsuario((String)session.getAttribute("usuario"),perfil);
                                %>
                                <a href="<%= mh.getUrl()  %>" class="badge1" data-badge="<%= cntx  %>"><%= mh.getTitulo() %> </a>
                            <%  } else {  %>
                                <a href="<%= mh.getUrl()  %>"><%= mh.getTitulo() %></a>
                            <%  }  %>
                        </li>
		
		<% } %>
		</ul>
		<% }  // fin si %>
	</li>

<% } %>
</ul>
</div>
</div>
<%  conex.Close(); %>