const notificaError = (msg) => Lobibox.notify("error", { msg });
const notificaExito = (msg) => Lobibox.notify("success", { msg });
const notificaAdvertencia = (msg) => Lobibox.notify("warning", { msg });
const notificaAviso = (msg) => Lobibox.notify("info", { msg });


function prueba() {
	notificaExito("Correcto");
}

const myDiv = document.getElementById('result-msg');
const observer = new MutationObserver((mutationsList, observer) => {
	var texto = $('#result').text();
	var notify = $('#result').data('notify');
	if (notify == '1') {
		notificaExito(texto);
	} else if (notify == '2') {
		notificaError(texto);
	}
});
const config = { childList: true };
observer.observe(myDiv, config);

function pagarMeses() {
	var valoresSeleccionados = $('input[name="meses"]:checked').map(function() {
		return this.value;
	}).get();


	if (valoresSeleccionados.length > 0) {
		console.log(valoresSeleccionados);
		let contenedorMeses = '';
		valoresSeleccionados.forEach(function(mes) {
			var fecha = new Date(mes);
			var opciones = { month: 'long' };
			var nombreMes = fecha.toLocaleString('es-ES', opciones);
			contenedorMeses += `<p class="fw-bold meses-pagar"> Mes: <span class="no-bold" id="clientePago">${nombreMes}</span></p>`;

		});
		var paquete = $('#precioPaquete').text();

		contenedorMeses += `<div class="text-right total-meses">
  <p class="fw-bold meses-pagar"> Total: $<span class="no-bold">${valoresSeleccionados.length * paquete}</span></p>
</div>
`;
		document.getElementById('confirmarMeses').innerHTML = contenedorMeses;
		$('#alerta-pagar-mes').css('visibility', 'hidden');
		$('#exampleModal').modal('show');
	}else{
		$('#alerta-pagar-mes').css('visibility', 'visible');

	}


}