<%@page import="com.are.entidades.CampBandeja"%>
<%@page import="com.are.manejadores.ManejadorCampBandeja"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<%@ include file="validausuario.jsp"%>
<%    db conexion = new db();
    String id = (String) request.getParameter("id");

    ManejadorCampBandeja manejador = new ManejadorCampBandeja(conexion);

    CampBandeja bandeja = null;
    if (!manejador.Find(Integer.parseInt(id))) {
        response.sendRedirect("bandejas.jsp");
        conexion.Close();
        return;
    }
    bandeja = manejador.getBandeja();

    ArrayList<CampBandeja> lista = manejador.List();


%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>Transferir bandeja</title>
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
            function transferir(source) {
                var destination = $("#bandeja").val();
                if (destination == "") {
                    alert("Debe seleccionar la bandeja destino");
                    return;
                }

                if (confirm("Esta seguro de transferir los registros a la bandeja seleccionada?")) {
                    $("#cmd_transferir").prop('disabled', true);
                    $("#info").html("<img src='images/loading.gif'> Procesando solicitud...");
                    var url = "SrvCampBandeja";
                    $.post(url, {
                        operacion: "transferir",
                        source: source,
                        destination: destination
                    }, function (data) {
                        if (data.trim() == "OK") {
                            alert("Registros transferidos correctamente");
                            cancelar();
                        } else {
                            $("#info").html("<img src='images/error.png'> " + data);
                            $("#cmd_transferir").prop('disabled', false);
                        }

                    });
                    

                }
            }

            function cancelar() {
                window.location.href = "bandejas.jsp";
            }

        </script>
    </head>
    <body onload="list()">
        <%@ include file="header.jsp" %>
        <div class="contencenter demo">
            <H2>Registro de Bandejas</H2>
            <div id="info"></div>
            <table>
                <thead>
                    <tr>
                        <th colspan="2">Información Bandeja Origen</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>Identificador</td>
                        <td><%= bandeja.getId()%></td>
                    </tr>
                    <tr>
                        <td>Usuario</td>
                        <td><%=  bandeja.getUsuario()%></td>
                    </tr>
                    <tr>
                        <td>Total pendientes</td>
                        <td><%= manejador.Count(bandeja.getId())%></td>
                    </tr>

                </tbody>
            </table>

            <form action="" name="form1">
                <table id="lista" border=0>
                    <tr>
                        <th colspan="2">Información Bandeja Destino</th>
                    </tr>
                    <tr>
                        <td>Usuario</td>
                        <td>
                            <select name="usuario" id="usuario">
                                <option value="">Seleccionar</option>
                                <% for (CampBandeja temp : lista) {%>
                                <option value="<%= temp.getId()%>"><%= temp.getUsuario()%></option>
                                <% }%>

                            </select>


                        </td>
                    </tr>
                    <tr>
                        <td colspan="2">
                            <input type="button" onclick="javascript:transferir(<%= (String) request.getParameter("id")%>);" value="Transferir" id="cmd_transferir" name="cmd_transferir"  >
                        </td>
                    </tr>
                </table>
            </form>
            <button type="button" onclick="javascript:cancelar()">Cancelar</button>

        </div>
        <%@ include file="foot.jsp" %>
    </body>
</html>
<%
    conexion.Close();
%>
