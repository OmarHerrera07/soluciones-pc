document.body.addEventListener("resetForm", function() {

	$('#cantidadAbonoinput').val('');
	document.getElementById('mesesAbonos').innerHTML = ' ';
	document.getElementById('tipoPagoAbono').value = '';
	$('.modal').modal('hide');
})
document.body.addEventListener("refresh", function() {

	document.getElementById('tipoPagoMasivo').value = '';
	$('.modal').modal('hide');
})

function resetFormPagoMasivo(){
	document.getElementById('tipoPagoMasivo').value = '';
} 

function validarTipoPagoAbono() {
	let tipoPagoSelect = $("#tipoPagoAbono").val();
	let abono = $("#cantidadAbonoinput").val();
	if (abono > 0 && tipoPagoSelect != null) {
		$("#confirmarAbonoModal").prop("disabled", false);
	} else {
		$("#confirmarAbonoModal").prop("disabled", true);
	}
}

function mostrarMesesAbono(abono) {

	let tipoPagoSelect = $("#tipoPagoAbono").val();

	if (abono > 0 && tipoPagoSelect != null) {
		$("#confirmarAbonoModal").prop("disabled", false);
	} else {
		$("#confirmarAbonoModal").prop("disabled", true);
	}

	let valor = abono;

	// Eliminar caracteres no numéricos
	valor = valor.replace(/\D/g, '');

	// Limitar a 4 dígitos
	valor = valor.slice(0, 5);


	$('#confirmarAbono').text(abono);

	abono = valor;
	$('#cantidadAbonoinput').val(valor);

	const precioPaquete = $('#precioPaquete').text();
	const fechaInput = $('#fechaPagoCliente').val();

	abono = parseFloat(abono) + parseFloat($('#cantidadAbono').text());

	let resultado = abono / precioPaquete;
	let residuo = abono % precioPaquete;
	let numMeses = Math.floor(resultado);

	// Convertir la fecha de "yyyy/MM/dd" a "dd/MM/yyyy"
	const fechaParts = fechaInput.split("-");
	const fecha = fechaParts[2] + "/" + fechaParts[1] + "/" + fechaParts[0];

	// Crear un array para almacenar las fechas generadas
	const fechasGeneradas = [];
	var contenedorMeses = '';

	// Generar las fechas adicionales
	for (let i = 0; i < numMeses; i++) {
		let fechaActual = new Date(fechaInput);
		fechaActual.setDate(fechaActual.getDate() + 1);
		fechaActual.setMonth(fechaActual.getMonth() + i);
		//const dia = fechaActual.getDate() + 1;
		const dia = fechaActual.getDate();

		const mes = fechaActual.getMonth() + 1;
		const anio = fechaActual.getFullYear();
		const fechaGenerada = `${anio}/${mes.toString().padStart(2, '0')}/${dia.toString().padStart(2, '0')}`;
		fechasGeneradas.push(fechaGenerada);
	}



	if (fechasGeneradas.length > 0) {
		contenedorMeses = '';
		fechasGeneradas.forEach(function(mes) {
			var fecha = new Date(mes);
			var opciones = { month: 'long' };
			var nombreMes = fecha.toLocaleString('es-ES', opciones);
			contenedorMeses += `<p class="fw-bold meses-pagar"> Mes: <span class="no-bold" id="clientePago">${nombreMes}</span></p>`;

		});
		var paquete = $('#precioPaquete').text();

		contenedorMeses += `<div class="text-right total-meses d-flex flex-column justify-content-end meses-abonos">
							    <p class="fw-bold meses-pagar"> Total: $<span class="no-bold">${fechasGeneradas.length * paquete}</span></p>
							    <p class="fw-bold meses-pagar"> Resto: $<span class="no-bold">${residuo}</span></p>
							</div>`;

	}
	document.getElementById('mesesAbonos').innerHTML = contenedorMeses;
	// Mostrar las fechas generadas en la consola (puedes ajustar esto según tus necesidades)

}

function resetAbono() {
	$('#cantidadAbonoinput').val('');
	document.getElementById('mesesAbonos').innerHTML = ' ';
	document.getElementById('tipoPagoAbono').value = '';
	$("#confirmarAbonoModal").prop("disabled", true);
	$('.modal').modal('hide');
}


function cerrarConfirmarAbono() {
	$('#confirmarPagoAbono').modal('hide');
}
function addCalendario(){
	let anio = $('#anio-actual-pago').val();
	let idCliente = $('#id-cliente-calendario').val();
	$.get(`/clientes/get-new-calendar?anio=${anio}&idCliente=${idCliente}`, function (data, status) {
		document.querySelector('#contenedor-calendario').innerHTML = data;
		$("#quitar-calendario").show("slow");
		$("#mostrar-calendario").hide("slow");
	});
}
function deteleCalendario(){
	$("#quitar-calendario").hide("slow");
	$("#mostrar-calendario").show("slow");
	document.querySelector('#contenedor-calendario').innerHTML = "";
}