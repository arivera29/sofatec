<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<%@ include file="validausuario.jsp"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>CAMPAÑAS</title>
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
            function agregar() {
                var codigo = $("#codigo").val();
                var descripcion = $("#descripcion").val();
                var plan = $("#plan").val();
                var estado = $("#estado").val();

                if (codigo == "" || descripcion == "" || plan == "") {
                    alert("falta ingresar informacion");
                    return;
                }

                var cmd = document.getElementById("cmd_agregar");
                cmd.disabled = true;
                $("#info").html("<img src=\"images/loading.gif\" >Procesando solicitud");
                $.post(
                        "SrvCamp",
                        {
                            operacion: "add",
                            codigo: codigo,
                            descripcion: descripcion,
                            plan: plan,
                            estado: estado
                        },
                        procesar

                        );

            }

            function procesar(resultado) {
                var cmd = document.getElementById("cmd_agregar");
                cmd.disabled = false;
                if (resultado != 'OK') {
                    $("#info").html("<img src=\"warning.jpg\">" + resultado);
                } else {
                    $("#info").html("<img src=\"images/ok.png\">Campaña agregada correctamente");
                    $("#codigo").val("");
                    $("#descripcion").val("");
                    list();
                }

            }

            function list() {
                $("#list").load("SrvCamp?operacion=list", function () {
                    $("input:submit, a, button", ".demo").button();
                });
            }

            function buscar() {
                var criterio = $("#criterio").val();
                if (criterio == "") {
                    alert("Debe ingresar el criterio de busqueda");
                    return;
                }

                var url = "SrvCamp";
                $("#list").html("<img src='images/loading.gif'> Procesando solicitud");
                $("#list").load(url, {
                    operacion: "busqueda",
                    criterio: criterio
                }, function (data) {
                    $("input:submit, a, button", "#list").button();
                });

            }
            
            function ActivarTodas() {
                if (confirm("Esta seguro de activar todas las campañas?")) {
                    var url = "SrvCamp";
                    $.post(url,{
                       operacion: "estado",
                       estado : "1"
                    }, function (data) {
                        if (data.trim() == "OK") {
                            alert("Todas las campañas han sido activadas");
                        }else {
                            alert(data);
                        }
                    });
                }
            }
            
            function InactivarTodas() {
                if (confirm("Esta seguro de inactivar todas las campañas?")) {
                    var url = "SrvCamp";
                    $.post(url,{
                       operacion: "estado",
                       estado : "0"
                    }, function (data) {
                        if (data.trim() == "OK") {
                            alert("Todas las campañas han sido inactivadas");
                        }else {
                            alert(data);
                        }
                    });
                }
            }



        </script>
    </head>
    <body>
        <%@ include file="header.jsp" %>
        <div class="contencenter demo">
            <h2>CAMPAÑAS</h2>
            <a href="SrvCamp?operacion=download">Descargar campañas</a>
            <a href="javascript:ActivarTodas();">Activar todas las campañas</a>
            <a href="javascript:InactivarTodas();d">Inactivar todas las campañas</a>
            <div id="info"></div>
            <form action="" name="form1">
                <table id="lista" border=0>
                    <tr>
                        <th colspan="2">NUEVA CAMPAÑA</th>
                    </tr>
                    <tr>
                        <td>Codigo</td>
                        <td><input type="text" name="codigo" id="codigo"></td>
                    </tr>
                    <tr>
                        <td>Descripcion</td>
                        <td><input type="text" name="descripcion" id="descripcion" size=40></td>
                    </tr>
                    <tr>
                        <td>Plan</td>
                        <td><input type="text" name="plan" id="plan" size=40></td>
                    </tr>
                    <tr>
                        <td>Activo</td>
                        <td>
                            <select name="estado" id="estado">
                                <option value="1">Si</option>
                                <option value="0">No</option>
                            </select>
                        </td>
                    </tr>
                </table>
                <input type="button" name="cmd_agregar" id="cmd_agregar" value="Agregar" onclick="javascript:agregar()" >
            </form>
            <h2>Buscar Campaña</h2>
            <form name="form2" action="">
                Criterio de busqueda: <input type="text" name="criterio" id="criterio" value="" size="40"> <input type="button" onclick="javascript:buscar();" value="Buscar">
            </form>

            <h2>Procesar archivo de campañas</h2>
            <p><strong>NOTA:</strong> El archivo a subir debe tener formato texto separado por tabuladores. Las columnas del archivo son: CODIGO,DESCRIPCION,PLAN,ESTADO.  la columna ESTADO debe ser 1 si es ACTIVA o 0 si es INACTIVA</p>
            <form name="form3" action="">
                Seleccionar archivo:  <input type="file" name="archivo" id="archivo"> 
                <input type="button" onclick="javascript:ProcesarArchivo();" value="Procesar">
            </form>

            <div id="list"></div>
        </div>
        <%@ include file="foot.jsp" %>

        <script>
            function ProcesarArchivo() {//Funcion encargada de enviar el archivo via AJAX
                

                $("#list").html("<img src='images/loading.gif'> Cargando...");
                var inputFile = document.getElementById("archivo");
                var file = inputFile.files[0];
                var data = new FormData();
                data.append('archivo', file);
                $.ajax({
                    url: "SrvUploadCamp", // Url to which the request is send
                    type: "POST", // Type of request to be send, called as method
                    data: data, // Data sent to server, a set of key/value pairs (i.e. form fields and values)
                    contentType: false, // The content type used when sending data to the server.
                    cache: false, // To unable request pages to be cached
                    processData: false, // To send DOMDocument or non processed data file it is set to false
                    success: function (data)   // A function to be called if request succeeds
                    {
                        $("#list").html(data);
                    }
                });

            }
        </script>
    </body>
</html>