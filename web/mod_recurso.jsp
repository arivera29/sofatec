<%@page import="com.are.entidades.Zona"%>
<%@page import="com.are.manejadores.ManejadorZonas"%>
<%@page import="com.are.manejadores.ManejadorRecurso"%>
<%@page import="com.are.entidades.Recurso"%>
<%@page import="com.are.entidades.Contratista"%>
<%@page import="com.are.manejadores.ManejadorContratista"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="validausuario.jsp"%>
<%@ page import='com.are.sofatec.*' %>
<%    db conexion = new db();
    cargos cargo = new cargos(conexion);

    String key = (String) request.getParameter("codigo");

    ManejadorRecurso m1 = new ManejadorRecurso(conexion);
    if (!m1.Find(key)) {
        conexion.Close();
        response.sendRedirect("recurso.jsp");
        return;
    }
    Recurso recurso = m1.getRecurso();
    ManejadorContratista manejador = new ManejadorContratista(conexion);
    ArrayList<Contratista> lista = manejador.List();
    ManejadorZonas manejador2 = new ManejadorZonas(conexion);
    ArrayList<Zona> lista2 = manejador2.List();
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>MODIFICAR RECURSO HUMANO</title>
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
            function modificar(key) {
                var codigo = $("#codigo").val();
                var nombre = $("#nombre").val();
                var direccion = $("#direccion").val();
                var telefono = $("#telefono").val();
                var correo = $("#correo").val();
                var cargo = $("#cargo").val();
                var contratista = $("#contratista").val();
                var estado = $("#estado").val();
                var rol = $("#rol").val();
                var zona = $("#zona").val();
                var auth = $("#auth").val();
                if (codigo == "" || nombre == "" || contratista == "" || zona == "") {
                    alert("falta ingresar informacion");
                    return;
                }

                var url = "SrvRecurso";
                $.post(url, {
                    operacion: "update",
                    codigo: codigo,
                    nombre: nombre,
                    direccion: direccion,
                    telefono: telefono,
                    correo: correo,
                    cargo: cargo,
                    contratista: contratista,
                    estado: estado,
                    rol: rol,
                    zona: zona,
                    auth: auth,
                    key: key
                }, procesar);


            }
            function procesar(resultado) {

                if (resultado != 'OK') {
                    $("#info").html("<img src=\"warning.jpg\">" + resultado);
                } else {
                    $("#info").html("Recurso Modificado");
                }
            }

            function eliminar(key) {
                if (confirm("Desea eliminar recurso " + key)) {
                    var url = "SrvRecurso?operacion=remove&key=" + key;
                    $.post(url, {
                        operacion: "remove",
                        key: key
                    }, procesarEliminar);

                }

            }

            function procesarEliminar(resultado) {
                if (resultado != 'OK') {
                    $("#info").html("<img src=\"warning.jpg\">" + resultado);
                } else {
                    alert("Recurso eliminado");
                    window.location.href = "recurso.jsp";
                }

            }

            function cancelar() {
                window.location.href = "recurso.jsp";
            }
        </script>
    </head>
    <body>
        <%@ include file="header.jsp" %>
        <div class="contencenter demo">
            <h2>Modificar recurso</h2>
            <div id="info"></div>
            <form action="" name="form1">
                <table id="lista" border=0>
                    <tr>
                        <th colspan="2">INFORMACION DEL RECURSO</th>
                    </tr>
                    <tr>
                        <td>Identificacion</td>
                        <td><input type="text" name="codigo" id="codigo" value ="<%= recurso.getCodigo()%>">
                            <input type="hidden" name="key" id="key" value ="<%= (String) request.getParameter("codigo")%>">

                        </td>
                    </tr>
                    <tr>
                        <td>Nombre</td>
                        <td><input type="text" name="nombre" id="nombre" size=40 value ="<%= recurso.getNombre()%>"></td>
                    </tr>
                    <tr>
                        <td>Direccion</td>
                        <td><input type="text" name="direccion" id="direccion" size=40 value ="<%= recurso.getDireccion()%>"></td>
                    </tr>
                    <tr>
                        <td>Telefono</td>
                        <td><input type="text" name="telefono" id="telefono" value ="<%= recurso.getTelefono()%>"></td>
                    </tr>
                    <tr>
                        <td>Correo</td>
                        <td><input type="text" name="correo" id="correo" size=40 value ="<%= recurso.getCorreo()%>"></td>
                    </tr>
                    <tr>
                        <td>Cargo</td>
                        <td><%= cargo.CreateSelectHTML("cargo", recurso.getCargo())%></td>
                    </tr>
                    <tr>
                        <td>Contratista</td>
                        <td>
                            <select name="contratista" id="contratista">
                                <option value="">Seleccionar</option>
                                <% for (Contratista contratista : lista) {%>
                                <option value ="<%= contratista.getCodigo()%>" <%= contratista.getCodigo() == recurso.getContratista() ? "selected" : ""%> ><%= contratista.getNombre()%></option>
                                <% }  %>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td>Zona</td>
                        <td>
                            <select name="zona" id="zona">
                                <option value="">Seleccionar</option>
                                <% for (Zona zona : lista2) {%>
                                <option value ="<%= zona.getId()%>" <%= zona.getId() == recurso.getZona() ? "selected" : ""%>><%= zona.getNombre()%></option>
                                <% }%>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td>Permitir acceso a la APP</td>
                        <td>
                            <select id="auth" name="auth">
                                <option value="0" <%= recurso.getAuthAPP() == 0 ? "selected" : ""%>>No</option>
                                <option value="1" <%= recurso.getAuthAPP() == 1 ? "selected" : ""%>>Si</option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td>Rol</td>
                        <td> <select id="rol">
                                <option value="1" <%= recurso.getRol() == 1 ? "selected" : ""%>>Tecnico</option>
                                <option value="2" <%= recurso.getRol() == 2 ? "selected" : ""%>>Coordinador</option>
                                <option value="3" <%= recurso.getRol() == 3 ? "selected" : ""%>>Inspector</option>
                                <option value="4" <%= recurso.getRol() == 4 ? "selected" : ""%>>Ingeniero</option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td>Activo?</td>
                        <td>
                            <select id="estado">
                                <option value="1" <%= recurso.getEstado() == 1 ? "selected" : ""%>>Si</option>
                                <option value="0" <%= recurso.getEstado() == 2 ? "selected" : ""%>>No</option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2"></td>
                    </tr>
                </table>
                <input type="button" onclick="javascript:modificar('<%= (String) request.getParameter("codigo")%>');" value="Guardar" id="cmd_modificar" name="cmd_modificar" > <input type="button" onclick="javascript:eliminar('<%= (String) request.getParameter("codigo")%>');" value="Eliminar" id="cmd_eliminar" name="cmd_eliminar" >  <input type="button" name="cmd_cancelar" id="cmd_cancelar" value ="Cancelar" onclick="javascript:cancelar()" >
            </form>

        </div>
        <%@ include file="foot.jsp" %>
    </body>
</html>
<%
    conexion.Close();
%>
