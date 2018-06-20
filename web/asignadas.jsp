<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="validausuario.jsp"%>
<%@ page import="com.are.sofatec.*" %>
<%@ page import="java.sql.*" %>
<%
        db conexion = new db();

        String lote = (String)request.getParameter("num_lote");
	
        ManejadorUsuarios mu = new ManejadorUsuarios(conexion);
        mu.find((String)session.getAttribute("usuario"));
        Usuarios usuario = mu.getUsuario();
	
        String sql = "SELECT QO_ORDENES.*, RECURSO.RECUNOMB "
                        + " FROM QO_ORDENES,RECURSO " 
                        + " WHERE QO_ORDENES.ESTADO_OPER=1 " 
                        + " AND QO_ORDENES.NUM_LOTE =? " 
                        + " AND QO_ORDENES.TECNICO = RECURSO.RECUCODI "
                        + " ORDER BY FECHA_CARGA";
        PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
        pst.setString(1, lote);
        ResultSet rs = conexion.Query(pst);
        boolean rsIsEmpty = !rs.next();

        int fila =0;
	
        sql = "SELECT COD,DESC_COD FROM QO_ANOM WHERE COD != '-1' ORDER BY COD";
        pst = conexion.getConnection().prepareStatement(sql);
        ResultSet rsAnomalia = conexion.Query(pst);

%>


<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>Resueltas LOTE <%= (String)request.getParameter("num_lote") %></title>
        <link rel="stylesheet" href="themes/redmond/jquery.ui.all.css">
        <LINK REL="stylesheet" TYPE="text/css" HREF="main.css">
        <script src="js/jquery.js" language="JavaScript"></script>
        <script src="ui/jquery.ui.core.js" language="JavaScript"></script>
        <script src="ui/jquery.ui.widget.js" language="JavaScript"></script>
        <script src="ui/jquery.ui.button.js"></script>
        <script src="ui/jquery.ui.tabs.js"></script>
        <script src="ui/jquery.ui.dialog.js"></script>
        <script src="ui/jquery.ui.position.js"></script>
        <script src="ui/jquery.effects.core.js"></script>
        <script src="ui/jquery.effects.slide.js"></script>
        <script src="ui/jquery.ui.datepicker.js"></script>
        <script src="ui/jquery.effects.explode.js"></script>
        <script src="ui/jquery.effects.fold.js"></script>
        <script src="ui/jquery.effects.slide.js"></script>
        <script type="text/javascript" language="javascript">
            $(function () {
                $(".boton").button();
                $(".fecha").datepicker({
                    showOn: "button",
                    buttonImage: "images/calendar.gif",
                    buttonImageOnly: true,
                    dateFormat: "yy-mm-dd"
                });
                $(".fecha").datepicker("option", "showAnim", "slide");
            });

            function Generar(lote) {
                var url = "SrvInterfaz?lote=" + lote;
                $("#info").html("<img src='images/loading.gif'>Procesando solicitud");
                $("#info").load(url);
            }

            function selectAll() {
                $("input:checkbox").prop('checked', true);
            }
            function clearAll() {
                $("input:checkbox").prop('checked', false);
            }

            function SolicitarNuevoLote() {
                var url = "SrvSolicitarNuevoLote?";

                var ordenes = "";
                //recorremos todos los checkbox seleccionados con .each
                $('input[name="orden"]:checked').each(function () {
                    //$(this).val() es el valor del checkbox correspondiente
                    ordenes += "&ordenes=" + this.value;
                });

                if (ordenes == "") {
                    alert("Debe seleccionar al menos una orden");
                    return;
                }

                url += ordenes;

                if (confirm("Esta seguro Solicitar nuevo lote para las OS seleccionadas?")) {
                    $("#info").html("<img src='images/loading.gif' > Procesando Solicitud")
                    $.get(url, procesar);
                }

            }
            function procesar(result) {
                if (result.indexOf("OK") != -1) {
                    alert(result);
                    $('input[name="orden"]:checked').each(function () {
                        var orden = this.value;
                        RemoveRow(orden);
                    });

                } else {
                    $("#info").html("<img src='images/alerta.gif' >" + result + "<BR>");
                }
            }

            function RemoveRow(idElemento) {
                $("#" + idElemento).delay(400);
                $("#" + idElemento).fadeOut(800, function () {
                    $("#" + idElemento).remove().fadeOut(800);
                });
            }

            function buscar() {
                url = "BuscarRecurso.jsp";
                window.open(url, "BuscarRecurso", "width=600,height=500,scrollbars=YES,menubar=No,toolbar=NO,status=YES");
            }

            function Anomalia() {
                var ordenes = "";
                //recorremos todos los checkbox seleccionados con .each
                $('input[name="orden"]:checked').each(function () {
                    //$(this).val() es el valor del checkbox correspondiente
                    ordenes += "&ordenes=" + this.value;
                });

                if (ordenes == "") {
                    alert("Debe seleccionar al menos una orden");
                    return;
                }

                var anomalia = $("#anomalia").val();
                var observacion = $("#observacion").val();
                var cedula = $("#cedula").val();

                if (anomalia == "" || observacion == "") {
                    alert("Faltan datos verifique por favor");
                    return;
                }

                var url = "SrvAnomaliaMasiva";
                url += ordenes;

                if (confirm("Esta seguro de reportar las ordenes como Anomalas?")) {
                    $("#info").html("<img src='images/loading.gif' > Procesando Solicitud");

                    $.post(url, {
                        anomalia: anomalia,
                        observacion: observacion,
                        recurso: cedula
                    }, function (result) {
                        $("#ventana").dialog("close");
                        if (result == "OK") {
                            $("#info").html("<img src='images/alerta.gif' > Ordenes Reportadas Anomalas correctamente<BR>");
                            $('input[name="orden"]:checked').each(function () {
                                var orden = this.value;
                                RemoveRow(orden);
                            });
                        } else {
                            $("#info").html("<img src='images/alerta.gif' >" + result + "<BR>");
                        }
                    });
                }
            }

            function NuevoLote(actual) {

                var nuevo_lote = $("#lote").val();

                if (nuevo_lote == "") {
                    alert("Debe ingresar el numero del nuevo lote");
                    return;
                }


                var ordenes = "";
                //recorremos todos los checkbox seleccionados con .each
                $('input[name="orden"]:checked').each(function () {
                    //$(this).val() es el valor del checkbox correspondiente
                    ordenes += "&ordenes=" + this.value;
                });

                if (ordenes == "") {
                    alert("Debe seleccionar al menos una orden");
                    return;
                }

                var url = "SrvCambioLote?lote=" + actual + "&nuevo_lote=" + nuevo_lote;
                url += ordenes;

                if (confirm("Esta seguro de realizar el cambio de lote de las ordenes seleccionadas?")) {

                    $.post(url, {}, function (result) {
                        $("#ventana_cambio_lote").dialog("close");
                        if (result == "OK") {
                            $("#info").html("<img src='images/alerta.gif' > Lote actualizado correctamente correctamente<BR>");
                            $('input[name="orden"]:checked').each(function () {
                                var orden = this.value;
                                RemoveRow(orden);
                            });
                        } else {
                            $("#info").html("<img src='images/alerta.gif' >" + result + "<BR>");
                        }
                    });

                }
            }

            function Cancelar() {
                document.location.href = "EstadoLotes.jsp";
            }


        </script>

        <script type="text/javascript">
            $(function () {
                $("#ventana").dialog({
                    autoOpen: false,
                    show: "blind",
                    hide: "blind",
                    modal: true,
                    width: 500,
                    position: {my: "center", at: "center", of: window}
                });
                $("#ventana_cambio_lote").dialog({
                    autoOpen: false,
                    show: "blind",
                    hide: "blind",
                    modal: true,
                    width: 500,
                    position: {my: "center", at: "center", of: window}
                });
            });

            function ReporteAnomalia() {
                var cont = 0;
                //recorremos todos los checkbox seleccionados con .each
                $('input[name="orden"]:checked').each(function () {
                    //$(this).val() es el valor del checkbox correspondiente
                    cont++;
                });

                if (cont == 0) {
                    alert("Debe seleccionar al menos una orden");
                    return;
                }

                $("#ventana").dialog("open");
            }

            function VentanaNuevoLote() {
                var cont = 0;
                //recorremos todos los checkbox seleccionados con .each
                $('input[name="orden"]:checked').each(function () {
                    //$(this).val() es el valor del checkbox correspondiente
                    cont++;
                });

                if (cont == 0) {
                    alert("Debe seleccionar al menos una orden");
                    return;
                }
                $("lote").val("");
                $("#ventana_cambio_lote").dialog("open");
            }

        </script>

    </head>
    <body>
        <%@ include file="header.jsp" %>
        <div class="contencenter demo">
            <h2>ASIGNADAS LOTE <%= (String)request.getParameter("num_lote") %></h2>

            <div id="info"></div>

            <form action="" name="form1">

                <% if (request.getParameter("num_lote") != null) { %>
                <% if (!rsIsEmpty) { %>
                <a href="javascript:selectAll()" class="boton">Seleccionar Todo</a> 
                <a href="javascript:clearAll()" class="boton">Quitar Seleccion</a> 
                <a href="javascript:SolicitarNuevoLote()" class="boton">Solicitar Nuevo Lote</a> 
                <% if (usuario.getAnomalias() == 1) { %>
                <a href="javascript:ReporteAnomalia()" class="boton">Reportar Anomalia</a>
                <% } %>
                <a href="javascript:VentanaNuevoLote()" class="boton">Cambiar Lote</a>
                <a href="javascript:Cancelar()" class="boton">Cancelar</a>

                <table>
                    <tr>
                        <th></th>
                        <th>LOTE</th>
                        <th>NUM_OS</th>
                        <th>NIC</th>
                        <th>TIPO</th>
                        <th>DIRECCION</th>
                        <th>FECHA</th>
                        <th>TECNICO</th>
                        <th>ACCION</th>
                    </tr>

                    <% do { %>
                    <tr id="<%= (String)rs.getString("NUM_OS") %>" <%=fila % 2 == 0 ? "class='odd'" : ""%>>
                        <td><input type="checkbox" name="orden" value ="<%= rs.getString("NUM_OS") %>" ></td>
                        <td><%= rs.getString("NUM_LOTE") %></td>
                        <td><%= rs.getString("NUM_OS") %></td>
                        <td><%= rs.getString("NIC") %></td>
                        <td><%= rs.getString("TIP_OS") %> <%= rs.getString("DESC_TIPO_ORDEN") %></td>
                        <td><%= rs.getString("DIRECCION") %></td>
                        <td><%= rs.getString("FECHA_CARGA") %></td>
                        <td><%= rs.getString("TECNICO") %> <%= rs.getString("RECUNOMB") %></td>
                        <td>
                            <% if (usuario.getHda()== 1) { %>
                            <a href="procesar_orden_hda.jsp?orden=<%= rs.getString("NUM_OS") %>&lote=<%= rs.getString("NUM_LOTE") %>" class="boton">HDA</a>
                            <% } %>
                            <% if (usuario.getResolver()== 1) { %>
                            <a href="ReporteOrdenEfectiva.jsp?orden=<%= rs.getString("NUM_OS") %>&lote=<%= rs.getString("NUM_LOTE") %>" class="boton">Resolver</a>
                            <% } %>
                            <a href="ReporteOrdenEfectiva.jsp?orden=<%= rs.getString("NUM_OS") %>&lote=<%= rs.getString("NUM_LOTE") %>" class="boton">Ver</a>
                        </td>
                    </tr>
                    <% fila++; %>
                    <% }while(rs.next()); %>
                    <% } %>


                </table>

            </form>


            <div id="ventana">
                <form action="" name="form1">
                    <h2>Anomalias</h2>

                    Seleccionar Anomalia: <br> <select name="anomalia" id="anomalia">
                        <%
                                while (rsAnomalia.next()) {
                        %>
                        <option value="<%=rsAnomalia.getString("COD")%>">
                            (<%=rsAnomalia.getString("COD")%>)
                            <%=rsAnomalia.getString("DESC_COD")%></option>
                            <%
                                    }
                            %>
                    </select> <br> Ingresar Observacion: <br>
                    <textarea rows="8" cols="40" id="observacion"></textarea> <br>

                    Recurso: 
                    <input type="text" name="cedula" id="cedula" readonly> <input type="text" name="nombre" id="nombre" size=40 readonly> 
                    <br>	
                    <input type="button" name="cmd_buscar" id="cmd_buscar" value="Buscar" onclick="javascript:buscar()" class="boton">
                    <input type="button" name="cmd_anomalia" value="Resolver como Anomalia" onclick="javascript:Anomalia()" class="boton">
                </form>
            </div>

            <div id="ventana_cambio_lote">
                <form action="" name="form3">
                    <h2>Cambio Lote</h2>
                    Nuevo Lote: <input type="text" name="lote" id="lote" value="" size="40">
                    <input type="button" name="cmd_nuevo_lote" value="Actualizar Lote" onclick="javascript:NuevoLote('<%= (String)request.getParameter("num_lote") %>')" class="boton">
                </form>
            </div>


            <% } %>



        </div>
        <%@ include file="foot.jsp" %>
    </body>
</html>

<%
        if (conexion != null) {
                conexion.Close();
        }

%>
