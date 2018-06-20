/**
 * 
 */

function ListaSellos(orden) {
		var url = "ListadoSellosOrden.jsp";
		$("#lista_sellos").html("<img src='images/loading.gif'>Consultando Precintos ")
		$("#lista_sellos").load(url, {
			orden : orden
		}, function(data) {
			$(".boton").button();
		});
}

function ListaLectura(orden) {
	var url = "ListadoLecturaOrden.jsp";
	$("#lista_lectura").html("<img src='images/loading.gif'>Consultando Lecturas ")
	$("#lista_lectura").load(url, {
		orden : orden
	}, function(data) {
		$(".boton").button();
	});
}


function ListaMaterialInstalado(orden) {
	var url = "ListadoMaterialInstaladoOrden.jsp";
	$("#lista_material_instalado").html("<img src='images/loading.gif'>Consultando Material Instalado ")
	$("#lista_material_instalado").load(url, {
		orden : orden
	}, function(data) {
		$(".boton").button();
	});
}

function ListaMedidorInstalado(orden) {
	var url = "ListadoMedidorInstaladoOrden.jsp";
	$("#lista_medidor_instalado").html("<img src='images/loading.gif'>Consultando Material Instalado ")
	$("#lista_medidor_instalado").load(url, {
		orden : orden
	}, function(data) {
		$(".boton").button();
	});
}

function ListaMaterialRetirado(orden) {
	var url = "ListadoMaterialRetiradoOrden.jsp";
	$("#lista_material_retirado").html("<img src='images/loading.gif'>Consultando Material Instalado ")
	$("#lista_material_retirado").load(url, {
		orden : orden
	}, function(data) {
		$(".boton").button();
	});
}

function EliminarMaterialInstalado(orden,id) {
	var url = "SrvResolverOrden";
	$.get(url,{
		operacion : "remove_material",
		id: id
	}, function (data) {
		if (data.trim() == "OK") {
			$("#info_material_instalado").html("<img src='images/alerta.gif'> Material Eliminado correctamente");
			ListaMaterialInstalado(orden);
		} else {
			$("#info_material_instalado").html("<img src='images/alerta.gif'> Error eliminar el material. "	+ data);
		}
	});
	
}

function EliminarMaterialRetirado(orden,id) {
	var url = "SrvResolverOrden";
	$.get(url,{
		operacion : "remove_material",
		id: id
	}, function (data) {
		if (data.trim() == "OK") {
			$("#info_material_instalado").html("<img src='images/alerta.gif'> Material eliminado correctamente");
			ListaMaterialRetirado(orden);
		} else {
			$("#info_material_instalado").html("<img src='images/alerta.gif'> Error al eliminar material. "	+ data);
		}
	});
	
}

function ListaPasos(orden,tipo) {
	var url = "ListadoPasosOrden.jsp";
	$("#lista_pasos").html("<img src='images/loading.gif'>Consultando Pasos ")
	$("#lista_pasos").load(url, {
		orden : orden,
		tipo : tipo
	}, function(data) {
		$(".boton").button();
	});
}

function ListaNuevosPasos(orden,tipo) {
	var url = "ListadoNuevosPasosOrden.jsp";
	$("#lista_nuevos_pasos").html("<img src='images/loading.gif'>Consultando Pasos ")
	$("#lista_nuevos_pasos").load(url, {
		orden : orden,
		tipo : tipo
	}, function(data) {
		$(".boton").button();
	});
}

function AgregarSello(orden, medidor) {
	var precinto = $("#precinto").val();
	if (precinto == "") {
		$("#info_sellos").html("<img src='images/alerta.gif'>Debe ingresar el serial del precinto");
		return;
	}
	var url = "SrvResolverOrden";
	$.get(url,{
		operacion : "add_sello",
		orden : orden,
		medidor : medidor,
		precinto : precinto
		},function(data) {
			if (data.trim() == "OK") {
				$("#info_sellos").html("<img src='images/alerta.gif'> Sello agregado correctamente");
				ListaSellos(orden);
			} else {
				$("#info_sellos").html("<img src='images/alerta.gif'> Error al agregar sello. "	+ data);
			}
		});
}

function AgregarLectura(orden, medidor) {
	var lectura = $("#lectura").val();
	if (lectura == "" || lectura== "0") {
		$("#info_lectura").html("<img src='images/alerta.gif'>Debe ingresar valor de lectura");
		return;
	}
	var url = "SrvResolverOrden";
	$.get(url,{
		operacion : "add_lectura",
		orden : orden,
		medidor : medidor,
		lectura : lectura
		},function(data) {
			if (data.trim() == "OK") {
				$("#info_lectura").html("<img src='images/alerta.gif'> Lectura agregada correctamente");
				$("#lectura").val("0");
				ListaLectura(orden);
			} else {
				$("#info_lectura").html("<img src='images/alerta.gif'> Error al agregar lectura. "	+ data);
			}
		});
}

function EliminarLectura(orden,medidor) {
	var url ="SrvResolverOrden";
	$.get(url,{
		operacion : "remove_lectura",
		orden: orden,
		medidor : medidor
	},function (data){
		if (data.trim() == "OK") {
			$("#info_lectura").html("<img src='images/alerta.gif'> Lectura Eliminada correctamente");
			ListaLectura(orden);
		} else {
			$("#info_lectura").html("<img src='images/alerta.gif'> Error al eliminar lectura. "	+ data);
		}
	});
}

function EliminarSello(orden,id) {
	var url ="SrvResolverOrden";
	$.get(url,{
		operacion : "remove_sello",
		id: id
	},function (data){
		if (data.trim() == "OK") {
			$("#info_sellos").html("<img src='images/alerta.gif'> Sello agregado correctamente");
			ListaSellos(orden);
		} else {
			$("#info_sellos").html("<img src='images/alerta.gif'> Error al agregar sello. "	+ data);
		}
	});
}

function Efectiva(orden) {
	var url ="SrvResolverOrden";
	$.get(url,{
		operacion : "update_orden",
		orden: orden
	},function (data){
		if (data.trim() == "OK") {
			alert("Orden actualizada correctamente");
			document.location.href="EstadoLotes.jps";
		} else {
			$("#info_orden").html("<img src='images/alerta.gif'> Error al agregar sello. "	+ data);
		}
	});
}

function EditarMedidor(orden,medidor) {
	url = "EditarMedidor.jsp?orden=" + orden + "&medidor=" + medidor;
	window.open(url, "EditarMedidor","width=600,height=500,scrollbars=YES,menubar=No,toolbar=NO,status=YES");
	
}