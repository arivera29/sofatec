<%@page import="com.are.entidades.Recurso"%>
<%@page import="com.are.manejadores.ManejadorRecurso"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="validausuario.jsp"%>
<%    db conexion = new db();
    String id = (String) request.getParameter("id");
    ManejadorRecurso manejador = new ManejadorRecurso(conexion);
    if (!manejador.Find(id)) {
        conexion.Close();
        response.sendRedirect("recurso.jsp");
        return;
    }

    Recurso recurso = manejador.getRecurso();


%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>Cambiar clave recurso</title>
        <link rel="stylesheet" href="themes/redmond/jquery.ui.all.css">
        <LINK REL="stylesheet" TYPE="text/css" HREF="main.css">
        <script src="js/jquery.js" language="JavaScript"></script>
        <script src="ui/jquery.ui.core.js" language="JavaScript"></script>
        <script src="ui/jquery.ui.widget.js" language="JavaScript"></script>
        <script src="ui/jquery.ui.button.js"></script>

        <script type="text/javascript">
            $(function () {
                $("input:submit, a, button", ".demo").button();
                $("input:button, a, button", ".demo").button();
            });
        </script>
        <script type="text/javascript" language="javascript">
            function asignar(id) {
                var clave1 = $("#clave1").val();
                var clave2 = $("#clave2").val();

                if (clave1 == "" || clave2 == "") {
                    $("#info").html("<img src='images/alerta.gif'> Falta ingresar informacion");
                    return;
                }
                
                if (clave1.length <= 5) {
                    $("#info").html("<img src='images/alerta.gif'> La contraseña debe tener al menos 6 caracteres");
                    return;
                }

                if (clave1 != clave2) {
                    $("#info").html("<img src='images/alerta.gif'> Las contraseñas deben ser iguales");
                    return;
                }


                var url = "SrvRecurso";
                $.post(url, {
                    operacion: "password",
                    key : id,
                    clave : clave1
                },
                        procesar);
            }


            function procesar(resultado) {
                if (resultado != 'OK') {
                    $("#info").html("<img src='images/alerta.gif'> " +resultado);
                } else {
                    alert("Contraseña asignada correctamente");
                    document.location.href="recurso.jsp";
                }
            }

        </script>
    </head>
    <body>
        <%@ include file="header.jsp" %>
        <div class="contencenter demo">
            <h1>Cambiar/Asignar contraseña recurso</h1>
            <h2>Recurso: <%= recurso.getCodigo()%> <%= recurso.getNombre()%></h2>
            <div id="info"></div>
            <form action="" name="form1">
                <table id="lista" border=0>
                    <tr>
                        <th colspan="2">Nueva contraseña</th>
                    </tr>
                    <tr>
                        <td>Ingrese contraseña</td>
                        <td><input type="password" name="clave1" id="clave1"></td>
                    </tr>
                    <tr>
                        <td>Repita contraseña</td>
                        <td><input type="password" name="clave2" id="clave2"></td>
                    </tr>
                </table>
                <a href="javascript:asignar('<%= (String) request.getParameter("id")%>');" id="cmd_asignar">Asignar</a>
                <a href="recurso.jsp">Cancelar</a>
            </form>

        </div>
        <%@ include file="foot.jsp" %>
    </body>
</html>
<%
    conexion.Close();

%>