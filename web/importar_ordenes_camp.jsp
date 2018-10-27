<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="validausuario.jsp"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>IMPORTAR ARCHIVO ORDENES</title>
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
        <script>
            

        </script>
    </head>
    <body>
        <%@ include file="header.jsp" %>
        <div class="contencenter demo">
            <h2>Importar ordenes de campaña</h2>
             <p><strong>NOTA:</strong> El archivo a subir debe tener formato texto separado por tabuladores.</p>
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
                    url: "SrvUploadOrdenesCamp", // Url to which the request is send
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