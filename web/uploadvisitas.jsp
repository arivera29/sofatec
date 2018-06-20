<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<%@ include file="validausuario.jsp"%>
<%
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>Upload visitas</title>
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
        
    </head>
    <body onload="list()">
        <%@ include file="header.jsp" %>
        <div class="contencenter demo">
            <h1>Cargar visitas</h1>
            <div style="background-color: #D5F7D5; color: black; padding: 10px; font-size: 12px">
            <p><strong>NOTA:</strong>
                <br> El archivo a subir debe tener formato texto separado por tabuladores
                <br> Las columnas del archivo son:
                <br> TIPO,NIC,OBSERVACION,DEPARTAMENTO,MUNICIPIO,DIRECCION,BARRIO,CLIENTE,BRIGADA

            </p>
            </div>
            <h2>Procesar archivo de visitas</h2>
            <form name="form1" action="">
                Seleccionar archivo:  <input type="file" name="archivo" id="archivo"> 
                <br/>
                <br/>
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
                    url: "SrvUploadVisitas", // Url to which the request is send
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