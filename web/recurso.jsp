<%@page import="com.are.manejadores.ManejadorZonas"%>
<%@page import="com.are.entidades.Zona"%>
<%@page import="com.are.manejadores.ManejadorContratista"%>
<%@page import="com.are.entidades.Contratista"%>
<%@ page import="com.are.sofatec.*" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<%@ include file="validausuario.jsp"%>

<%    db conexion = new db();
    cargos cargo = new cargos(conexion);
    ManejadorContratista manejador = new ManejadorContratista(conexion);
    ArrayList<Contratista> lista = manejador.List();
    ManejadorZonas manejador2 = new ManejadorZonas(conexion);
    ArrayList<Zona> lista2 = manejador2.List();

%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>RECURSO HUMANO</title>
        <link rel="stylesheet" href="themes/redmond/jquery.ui.all.css">
        <LINK REL="stylesheet" TYPE="text/css" HREF="main.css">
        <script src="js/jquery.js" language="JavaScript"></script>
        <script src="ui/jquery.ui.core.js" language="JavaScript"></script>
        <script src="ui/jquery.ui.widget.js" language="JavaScript"></script>
        <script src="ui/jquery.ui.button.js"></script>

        <script>
            $(function () {
                $("input:submit, a, button", ".demo").button();
                $("input:button, a, button", ".demo").button();
            });
        </script>
        <script>
            function agregar() {
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
                    operacion: "add",
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
                    auth: auth
                }, procesar);
            }

            function procesar(resultado) {
                if (resultado != 'OK') {
                    $("#info").html("<img src=\"warning.jpg\">" + resultado);
                } else {
                    $("#info").html("Recurso humano agregado");
                    $("#codigo").val("");
                    $("#nombre").val("");
                    $("#direccion").val("");
                    $("#telefono").val("");
                    $("#correo").val("");
                }
            }

            function list() {

                $("#list").load("SrvRecurso?operacion=list", function () {
                    $("input:button, a, button", ".demo").button();
                });
            }

            function buscar() {
                var criterio = $("#criterio").val();
                if (criterio == "") {
                    alert("Debe ingresar el criterio de busqueda");
                    return;
                }

                $("#list").html("<img src='images/loading.gif'> Procesando solicitud");
                var url = "SrvRecurso";
                $("#list").load(url, {
                    operacion: "consulta",
                    criterio: criterio
                }, function (data) {
                    $("input:button, a, button", "#list").button();
                });
            }
            
            function filtro1() {
                var contratista = $("#f_contratista").val();
                if (contratista == "") {
                    alert("Debe seleccionar el contratista");
                    return;
                }

                $("#list").html("<img src='images/loading.gif'> Procesando solicitud");
                var url = "SrvRecurso";
                $("#list").load(url, {
                    operacion: "filtro1",
                    contrata: contratista
                }, function (data) {
                    $("input:button, a, button", "#list").button();
                });
            
            }
            
            function filtro2() {
                var zona = $("#f_zona").val();
                if (zona == "") {
                    alert("Debe seleccionar la zona");
                    return;
                }

                $("#list").html("<img src='images/loading.gif'> Procesando solicitud");
                var url = "SrvRecurso";
                $("#list").load(url, {
                    operacion: "filtro2",
                    zona: zona
                }, function (data) {
                    $("input:button, a, button", "#list").button();
                });
            
            }
        </script>
    </head>
    <body>
        <%@ include file="header.jsp" %>
        <div class="contencenter demo">
            <h2>Recurso Humano</h2>
            <div id="info"></div>
            <form action="" name="form1">
                <table>
                    <tr>
                        <th colspan="2">NUEVO RECURSO HUMANO</th>
                    </tr>

                    <tr>
                        <td>Identificacion</td>
                        <td><input type="text" name="codigo" id="codigo"> <div id="info"></div></td>
                    </tr>
                    <tr>
                        <td>Nombre</td>
                        <td><input type="text" name="nombre" id="nombre" size=40></td>
                    </tr>
                    <tr>
                        <td>Direccion</td>
                        <td><input type="text" name="direccion" id="direccion" size=40></td>
                    </tr>
                    <tr>
                        <td>Telefono</td>
                        <td><input type="text" name="telefono" id="telefono"></td>
                    </tr>
                    <tr>
                        <td>Correo</td>
                        <td><input type="text" name="correo" id="correo" size=40></td>
                    </tr>
                    <tr>
                        <td>Cargo</td>
                        <td><%= cargo.CreateSelectHTML("cargo")%></td>
                    </tr>
                    <tr>
                        <td>Contratista</td>
                        <td>
                            <select name="contratista" id="contratista">
                                <option value="">Seleccionar</option>
                                <% for (Contratista contratista : lista) {%>
                                <option value ="<%= contratista.getCodigo()%>"><%= contratista.getNombre()%></option>
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
                                <option value ="<%= zona.getId()%>"><%= zona.getNombre()%></option>
                                <% }  %>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td>Permitir acceso a la APP</td>
                        <td>
                            <select id="auth" name="auth">
                                <option value="0">No</option>
                                <option value="1">Si</option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td>Rol</td>
                        <td>
                            <select id="rol">
                                <option value="1">Tecnico</option>
                                <option value="2">Coordinador</option>
                                <option value="3">Inspector</option>
                                <option value="4">Ingeniero</option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td>Activo?</td>
                        <td>
                            <select id="estado">
                                <option value="1">Si</option>
                                <option value="0">No</option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2"></td>
                    </tr>
                </table>
            </form>
            <input type="button" onclick="javascript:agregar();" value="Agregar" id="cmd_agregar" name="cmd_agregar" >
            <h2>Buscar Recurso</h2>
            <form name="form2" action="">
                Criterio de busqueda: <input type="text" name="criterio" id="criterio" value="" size="40"> <input type="button" onclick="javascript:buscar();" value="Buscar">
            </form>
            <h2>Filtrar recurso por Contrata</h2>
            <form name="form3" action="">
                Seleccione Contrata:  
                <select name="f_contratista" id="f_contratista">
                    <option value="">Seleccionar</option>
                    <% for (Contratista contratista : lista) {%>
                    <option value ="<%= contratista.getCodigo()%>"><%= contratista.getNombre()%></option>
                    <% }  %>
                </select>
                <input type="button" onclick="javascript:filtro1();" value="Filtrar">
            </form>
            <h2>Filtrar recurso por Zona</h2>
            <form name="form4" action="">
                Seleccione Zona:  
                <select name="f_zona" id="f_zona">
                    <option value="">Seleccionar</option>
                    <% for (Zona zona : lista2) {%>
                    <option value ="<%= zona.getId()%>"><%= zona.getNombre()%></option>
                    <% }  %>
                </select>
                <input type="button" onclick="javascript:filtro2();" value="Filtrar">
            </form>

            <div id="list"></div>

        </div>
        <%@ include file="foot.jsp" %>
    </body>
</html>
<%
    conexion.Close();
%>
