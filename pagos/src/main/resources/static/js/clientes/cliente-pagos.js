document.body.addEventListener("resetForm", function() {
	
	$('#cantidadAbonoinput').val('');
	console.log("Hola que hace");
	document.getElementById('mesesAbonos').innerHTML = ' ';
	$('.modal').modal('hide');
})

function mostrarMesesAbono(abono) {
	
	
	const precioPaquete = $('#precioPaquete').text();
	const fechaInput = $('#fechaPagoCliente').val();
	
	abono = parseFloat(abono) + parseFloat($('#cantidadAbono').text());

	let resultado = abono / precioPaquete;
	let residuo = abono % precioPaquete;
	let numMeses = Math.floor(resultado);
	console.log("FECHAAA");
	console.log(fechaInput);
	console.log(abono);
	console.log(numMeses);
	console.log();

	// Convertir la fecha de "yyyy/MM/dd" a "dd/MM/yyyy"
	const fechaParts = fechaInput.split("-");
	const fecha = fechaParts[2] + "/" + fechaParts[1] + "/" + fechaParts[0];

	// Crear un array para almacenar las fechas generadas
	const fechasGeneradas = [];
	var contenedorMeses = '';

	// Generar las fechas adicionales
	for (let i = 0; i < numMeses; i++) {
		const fechaActual = new Date(fechaInput);
		fechaActual.setMonth(fechaActual.getMonth() + i);
		const dia = fechaActual.getDate() + 1;
		const mes = fechaActual.getMonth() + 1;
		const anio = fechaActual.getFullYear();
		const fechaGenerada = `${anio}/${mes.toString().padStart(2, '0')}/${dia.toString().padStart(2, '0')}`;
		fechasGeneradas.push(fechaGenerada);
	}




	if (fechasGeneradas.length > 0) {
		console.log(fechasGeneradas);
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
	console.log(fechasGeneradas);
}

function resetAbono(){
	$('#cantidadAbonoinput').val('');
	console.log("Hola que hace");
	document.getElementById('mesesAbonos').innerHTML = ' ';
	$('.modal').modal('hide');
}