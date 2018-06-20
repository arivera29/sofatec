<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="validausuario.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Acceso Denegado</title>
<LINK REL="stylesheet" TYPE="text/css" HREF="main.css">
<link rel="Shortcut Icon" href="icono_tm.ico" type="image/x-icon" />
</head>
<body>
<%@ include file="header.jsp"%>
<h2>Acceso denegado</h2>
No tiene permiso para acceder al recurso solicitado, debe iniciar una nueva sesión con los privilegios autorizados
<br/>
<a href="logout" class="small button red">Iniciar Sesión</a>
<br/>
<br/>
<%@ include file="foot.jsp"%>
</body>
</html>