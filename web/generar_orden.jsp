<%@page import="com.are.entidades.Irregularidad"%>
<%@page import="com.are.manejadores.ManejadorIrregularidad"%>
<%@page import="com.are.entidades.Zona"%>
<%@page import="com.are.manejadores.ManejadorZonas"%>
<%@page import="com.are.entidades.Camp"%>
<%@page import="com.are.manejadores.ManejadorCamp"%>
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
    ArrayList<Contratista> lista = manejador.ListByEstado(1);
    ManejadorCamp m1 = new ManejadorCamp(conexion);
    ArrayList<Camp> l1 = m1.ListByEstado(1);
    ManejadorZonas m2 = new ManejadorZonas(conexion);
    ArrayList<Zona> l2 = m2.List();
    ManejadorIrregularidad m3 = new ManejadorIrregularidad(conexion);
    ArrayList<Irregularidad> l3 = m3.List();

%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>GENERAR ORDEN DE SERVICIO</title>
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
                var nic = $("#nic").val();
                var nis = $("#nis").val();
                var orden = $("#orden").val();
                var comentario = $("#comentario").val();
                var contratista = $("#contratista").val();
                var inspector = $("#inspector").val();
                var ingeniero = $("#ingeniero").val();
                var brigada = $("#brigada").val();
                var camp = $("#camp").val();
                var tipo_brigada = $("#tipo_brigada").val();
                var zona = $("#zona").val();
                var corriente1 = $("#corriente1").val();
                var voltaje1 = $("#voltaje1").val();
                var corriente2 = $("#corriente2").val();
                var voltaje2 = $("#voltaje2").val();
                var irreg = $("#irreg").val();

                if (nic == "" || contratista == "" || comentario == "" || zona == ""
                        || inspector == "" || ingeniero == "" || brigada == "" || camp == "" || tipo_brigada == "") {
                    $("#info").html("<img src=\"warning.jpg\">Falta ingresar informacion para guardar el registro");
                    return;
                }
                
                var patron = /^\d*$/;
                var OK = nic.search(patron);
                if (OK) {
                    alert("El NIC debe contener solo numeros, favor verificar");
                    return;
                }else {
                    if (nic.length != 7) {
                        alert("El NIC debe contener solo 7 digitos, favor verificar");
                    return;
                    }
                }
                
                if (orden != "") {
                    var patron = /^\d*$/;
                    var OK = orden.search(patron);
                    if (OK) {
                        alert("La orden debe contener solo numeros, favor verificar");
                        return;
                    }else {
                       if (orden.length != 8) {
                            alert("La orden debe contener solo 8 digitos, favor verificar");
                            return;
                        } 
                    }  
                }
                
                var url = "SrvGenerarOrden";
                $.post(url, {
                    nic : nic,
                    nis : nis,
                    orden : orden,
                    comentario: comentario,
                    contratista: contratista,
                    inspector: inspector,
                    ingeniero:  ingeniero,
                    brigada:  brigada,
                    tipo_brigada: tipo_brigada,
                    camp: camp,
                    zona : zona,
                    irreg : irreg,
                    corriente1 : corriente1,
                    voltaje1 : voltaje1,
                    corriente2 : corriente2,
                    voltaje2 : voltaje2
                }, procesar);
            }

            function procesar(resultado) {
                if (resultado != 'OK') {
                    $("#info").html("<img src=\"warning.jpg\">" + resultado);
                } else {
                    alert("Registro agregado correctamente");
                    document.getElementById("form1").reset();
                    
                }
            }

            function list() {

                //$("#list").load("SrvRecurso?operacion=list", function () {
                //    $("input:button, a, button", ".demo").button();
                //});
            }
            function getPersonal() {
                var contratista = $("#contratista").val();

                $("#dv_inspecto").html("<img src='images/loading.gif'>");
                $("#dv_inspector").load("SrvRecursoRol", {
                    contratista: contratista,
                    rol: 3,
                    id: 'inspector'
                });
                $("#dv_ingeniero").html("<img src='images/loading.gif'>");
                $("#dv_ingeniero").load("SrvRecursoRol", {
                    contratista: contratista,
                    rol: 4,
                    id: 'ingeniero'
                });
                $("#dv_brigada").html("<img src='images/loading.gif'>");
                $("#dv_brigada").load("SrvRecursoRol", {
                    contratista: contratista,
                    rol: 1,
                    id: 'brigada'
                });

            }
            function buscarInformacion() {
                var nic = $("#nic").val();
                $("#alerta").html("");
                $("#info").html("");
                if (nic == "") {
                    $("#cliente").val("");
                    $("#direccion").val();
                    $("#municipio").val("");
                    $("#localidad").val("");
                    $("#departamento").val("");
                } else {
                    var url = "SrvInfoSuministro";
                    $.get(url, {
                        nic: nic
                    }, function (data) {
                        var obj = $.parseJSON(data);
                        if (obj != null) {
                            
                            $("#cliente").val(obj.cliente);
                            $("#direccion").val(obj.direccion);
                            $("#municipio").val(obj.municipio);
                            $("#localidad").val(obj.localidad);
                            $("#departamento").val(obj.departamento);
                            $("#nis").val(obj.nis);
                            
                        } else {
                            $("#info").html("Error." + data);
                        }

                    });
                    
                    var url = "SrvAlertaSuministro";
                    $.get(url, {
                        nic: nic
                    }, function (data) {
                        if (data.trim() != "") {
                            $("#alerta").html("<img src='images/alerta.gif'>ALERTA: " + data);
                        }

                    });
                    

                }


            }

        </script>
    </head>
    <body onload="javascript:list();">
        <%@ include file="header.jsp" %>
        <div class="contencenter demo">
            <h2>GENERAR ORDEN DE SERVICIO</h2>
            <div id="info"></div>
            <form action="" name="form1" id="form1">
                <table>
                    <tr>
                        <th colspan="4">INFORMACION ORDEN DE SERVICIO</th>
                    </tr>

                    <tr>
                        <td>NIC</td>
                        <td><input type="text" name="nic" id="nic" onblur="javascript:buscarInformacion();"> <div id="info"></div>
                            <div id="alerta"></div></td>
                        <td>NIS</td>
                        <td><input type="text" name="nis" id="nis" ></td>
                    </tr>
                    <tr>
                        <td>Nombre Cliente</td>
                        <td><input type="text" name="cliente" id="cliente" size=40 readonly></td>
                        <td>Direccion</td>
                        <td><input type="text" name="direccion" id="direccion" size=40 readonly></td>
                    </tr>
                    <tr>
                        <td>Municipio</td>
                        <td><input type="text" name="municipio" id="municipio" size=40 readonly></td>
                        <td>Localidad</td>
                        <td><input type="text" name="localidad" id="localidad" size=40 readonly></td>
                    </tr>
                    <tr>
                        <td>Departamento</td>
                        <td><input type="text" name="departamento" id="departamento" size=40 readonly></td>
                        <td>Orden de servicio</td>
                        <td><input type="text" name="orden" id="orden"></td>
                    </tr>
                    <tr>
                        <td>Comentario</td>
                        <td colspan="3"><textarea name="comentario" id="comentario" rows="10" cols="40"></textarea></td>
                    </tr>
                    <tr>
                        <td>Corriente 1</td>
                        <td><input type="text" value="0" name="corriente1" id="corriente1" ></td>
                        <td>Voltaje 1</td>
                        <td><input type="text" value="0" name="voltaje1" id="voltaje1" ></td>
                    </tr>
                    <tr>
                        <td>Corriente 2</td>
                        <td><input type="text" value="0" name="corriente2" id="corriente2" ></td>
                        <td>Voltaje 2</td>
                        <td><input type="text" value="0" name="voltaje2" id="voltaje2" ></td>
                    </tr>
                    <tr>
                        <td>Irregularidad</td>
                        <td colspan="3">
                            <select name="irreg" id="irreg">
                                <option value="-1">Seleccionar</option>
                                <% for (Irregularidad item : l3) {%>
                                <option value ="<%= item.getCodigo()%>"><%= item.getDescripcion()%></option>
                                <% }  %>
                            </select>
                        </td>
                        
                    </tr>
                    <tr>
                        <td>Contratista</td>
                        <td>
                            <select name="contratista" id="contratista" onchange="javascript:getPersonal();">
                                <option value="">Seleccionar</option>
                                <% for (Contratista contratista : lista) {%>
                                <option value ="<%= contratista.getCodigo()%>"><%= contratista.getNombre()%></option>
                                <% }  %>
                            </select>
                        </td>
                        <td>Inspector</td>
                        <td>
                            <div id="dv_inspector">
                                <select name="inspector" id="inspector">
                                    <option value="">Seleccionar</option>

                                </select>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td>Ingeniero</td>
                        <td>
                            <div id="dv_ingeniero">
                                <select name="ingeniero" id="ingeniero">
                                    <option value="">Seleccionar</option>

                                </select>
                            </div>
                        </td>
                        <td>Brigada</td>
                        <td>
                            <div id="dv_brigada">
                                <select name="brigada" id="brigada">
                                    <option value="">Seleccionar</option>

                                </select>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td>Tipo Brigada</td>
                        <td>
                            <select name="tipo_brigada" id="tipo_brigada">
                                <option value="">Seleccionar</option>
                                <option value="1">Normal</option>
                                <option value="2">Auto-gestión</option>
                            </select>

                        </td>
                        <td>Campaña</td>
                        <td>
                            <select name="camp" id="camp">
                                <option value="">Seleccionar</option>
                                <% for (Camp camp : l1) {%>
                                <option value ="<%= camp.getCodigo()%>"><%= camp.getDescripcion()%> (<%= camp.getCodigo()%>)</option>
                                <% }  %>
                            </select>
                        </td>

                    </tr>
                    <tr>
                        
                        <td>Zona</td>
                        <td>
                            <select name="zona" id="zona">
                                <option value="">Seleccionar</option>
                                <% for (Zona zona : l2) {%>
                                <option value ="<%= zona.getId() %>"><%= zona.getNombre()%></option>
                                <% }  %>
                            </select>
                        </td>
                        <td></td>
                        <td></td>

                    </tr>
                </table>
            </form>
            <input type="button" onclick="javascript:agregar();" value="Agregar" id="cmd_agregar" name="cmd_agregar" >

            <div id="list"></div>

        </div>
        <%@ include file="foot.jsp" %>
    </body>
</html>
<%
    conexion.Close();
%>
