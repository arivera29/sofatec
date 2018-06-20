<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="validausuario.jsp"%>
<%@ page import="com.are.sofatec.*" %>
<%
	
        if (request.getParameter("codigo") == null) {
                response.sendRedirect("FindOrder.jsp?operacion=modify");
                return;
        }
	
        String codigo = (String)request.getParameter("codigo");
        db conexion = new db();
        Orders orden = new Orders(conexion);
        if (!orden.Find(codigo)) {
                conexion.Close();
                response.sendRedirect("FindOrder.jsp?operacion=modify");
                return;
        }
        TipoOrden tipo = new TipoOrden(conexion);
        departamento dpto = new departamento(conexion);
        localidad loca = new localidad(conexion);
        Clientes cliente = new Clientes(conexion);
        cliente.Find(orden.getNic());
        dpto.Find(orden.getDepartamento());
        loca.Find(orden.getMunicipio());
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>Modificar Orden</title>
        <LINK REL="stylesheet" TYPE="text/css" HREF="main.css">
        <script src="js/jquery-1.3.2.js" language="JavaScript"></script>
        <link rel="Shortcut Icon" href="icono_tm.ico" type="image/x-icon" />
        <script type="text/javascript" language="javascript">
            function modificar(key) {
                var nic = $("#nic").val();
                var orden = $("#orden").val();
                var departamento = $("#departamento").val();
                var municipio = $("#municipio").val();
                var direccion = $("#direccion").val();
                var tipo = $("#tipo").val();
                var nis = $("#nis").val();
                var fecha = $("#fecha").val();

                if (nic == "" || orden == "" || direccion == "" || departamento == "" || municipio == "" || tipo == "") {
                    alert("falta ingresar informacion");
                    return;
                }
                var cmd = document.getElementById("cmd_modificar");
                cmd.disabled = true;
                $("#info").html("<img src=\"images/loading.gif\" >Procesando solicitud");
                $.post(
                        "SrvOrders",
                        {
                            operacion: "modify",
                            nic: nic,
                            nis: nis,
                            orden: orden,
                            direccion: direccion,
                            departamento: departamento,
                            municipio: municipio,
                            fecha: fecha,
                            tipo: tipo,
                            key: key
                        },
                        procesar

                        );

            }
            function procesar(resultado) {
                var cmd = document.getElementById("cmd_modificar");
                cmd.disabled = false;
                if (resultado != 'OK') {
                    $("#info").html("<img src=\"warning.jpg\">" + resultado);
                } else {
                    $("#info").html("<img src=\"images/ok.png\">Orden modificada correctamente");
                }
            }

            function eliminar(key) {
                if (confirm("Desea eliminar la Orden " + key)) {
                    var cmd = document.getElementById("cmd_eliminar");
                    cmd.disabled = true;
                    $("#info").html("<img src=\"images/loading.gif\" >Procesando solicitud");
                    $.post(
                            "SrvOrders",
                            {
                                operacion: "remove",
                                key: key
                            },
                            procesarEliminar

                            );
                }

            }

            function procesarEliminar(resultado) {
                var cmd = document.getElementById("cmd_eliminar");
                cmd.disabled = false;
                if (resultado != 'OK') {
                    $("#info").html("<img src=\"warning.jpg\">" + resultado);
                } else {
                    alert("Orden eliminada correctamente");
                    window.location.href = "FindOrder.jsp?operacion=modify";
                }

            }

            function cancelar() {
                window.location.href = "FindOrder.jsp?operacion=modify";
            }

            function buscar() {
                var nic = $("#nic").val();
                if (nic != "") {
                    $("#info").html("<img src=\"images/loading.gif\" >Procesando solicitud");
                    $.getJSON(
                            "SrvClientes?operacion=buscar&nic=" + nic,
                            busqueda
                            );
                }
            }

            function busqueda(obj) {
                if (obj.estado != "OK") {
                    $("#info").html("<img src=\"warning.jpg\">" + obj.mensaje + "  <a href=\"Clientes.jsp\" target=\"_blank\" >Crear nuevo cliente</a>");
                    var cmd = document.getElementById("cmd_modificar");
                    cmd.disabled = true;
                    $("#nombre").val("");
                    $("#direccion").val("");
                    $("#latitud").val("");
                    $("#longitud").val("");
                    $("#medidor").val("");
                    $("#departamento").val("");
                    $("#nom_depa").val("");
                    $("#municipio").val("");
                    $("#nom_municipio").val("");
                } else {
                    $("#info").html("");
                    var cmd = document.getElementById("cmd_modificar");
                    cmd.disabled = false;
                    $("#nombre").val(obj.nombre);
                    $("#direccion").val(obj.direccion);
                    $("#latitud").val(obj.latitud);
                    $("#longitud").val(obj.longitud);
                    $("#medidor").val(obj.medidor);
                    $("#departamento").val(obj.departamento);
                    $("#nom_depa").val(obj.nom_departamento);
                    $("#municipio").val(obj.municipio);
                    $("#nom_municipio").val(obj.nom_municipio);
                }
            }

        </script>
    </head>
    <body>
        <%@ include file="header.jsp" %>
        <h2>Modificar Orden</h2>
        <div id="info"></div>
        <form action="" name="form1">
            <table>
                <tr>
                    <th colspan="4">Datos Ordenes</th>
                </tr>
                <tr>
                    <td>No. Orden</td>
                    <td><input type="text" name="orden" id="orden" size=40 value ="<%= orden.getOrden() %>"></td>
                    <td>NIC</td>
                    <td><input type="text" name="nic" id="nic" onblur="javascript:buscar()" value ="<%= orden.getNic() %>"></td>
                </tr>
                <tr>
                    <td>Departamento</td>
                    <td><input type="text" name="departamento" id="departamento" readonly size="10" value="<%= orden.getDepartamento() %>" > <input type="text" name="nom_depa" id="nom_depa" readonly size="40" value="<%= (String)dpto.getDescripcion()  %>"  ></td>
                    <td>Municipio</td>
                    <td><input type="text" name="municipio" id="municipio" readonly  size="10" value="<%= orden.getMunicipio() %>"  > <input type="text" name="nom_municipio" id="nom_municipio"  readonly size="40"  value="<%= (String)loca.getDescripcion()  %>"></td>
                </tr>
                <tr>
                    <td>Direccion</td>
                    <td><input type="text" name="direccion" id="direccion" size=40 disabled value ="<%= orden.getDireccion() %>"></td>
                    <td>Medidor</td>
                    <td></td>
                </tr>
                <tr>
                <tr>
                    <td>Tipo Orden</td>
                    <td></td>
                    <td>NIS</td>
                    <td><input type="text" name="nis" id="nis" size=40 value ="<%= orden.getNis() %>" ></td>
                </tr>
                <tr>
                    <td>Fecha Generacion</td>
                    <td colspan="3"><input type="text" name="fecha" id="fecha" value ="<%= orden.getFecha_generacion() %>"></td>
                </tr>
                <tr>
                    <th colspan="4">Georeferenciacion</th>
                </tr>	

                <tr>
                    <td>Latitud</td>
                    <td><input type="text" name="latitud" id="latitud" size=40 disabled value="<%= cliente.getLatitud()  %>"></td>
                    <td>Longitud</td>
                    <td><input type="text" name="longitud" id="longitud" size=40 disabled value="<%= cliente.getLongitud()  %>"></td>
                </tr>		
                <tr>
                    <td colspan="4"><input type="button" onclick="javascript:modificar('<%= (String)request.getParameter("codigo") %>');" value="Modificar" id="cmd_modificar" name="cmd_modificar" > <input type="button" onclick="javascript:eliminar('<%= (String)request.getParameter("codigo") %>');" value="Eliminar" id="cmd_eliminar" name="cmd_eliminar" >  <input type="button" name="cmd_cancelar" id="cmd_cancelar" value ="Cancelar" onclick="javascript:cancelar()" ></td>
                </tr>
            </table>
        </form>
        <%@ include file="foot.jsp" %>
    </body>
</html>

<%
        conexion.Close();
%>