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
		$('.modal').modal('hide');
	} else if (notify == '2') {
		notificaError(texto);
	}else if (notify == '3') {
		notificaAdvertencia(texto);
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
			fecha.setDate(fecha.getDate() + 1);
			var opciones = { month: 'long' };
			var nombreMes = fecha.toLocaleString('es-ES', opciones);
			contenedorMeses += `<p class="fw-bold meses-pagar"> Mes: <span class="no-bold" id="clientePago">${nombreMes}</span></p>`;

		});
		var paquete = $('#precioPaquete').text();
		var abono = $('#cantidadAbono').text();
		
		if(abono > 0){
			contenedorMeses += `<div class="text-right total-meses d-flex flex-column justify-content-end meses-abonos">
							  <p class="fw-bold meses-pagar"> Abono: - $<span class="no-bold">${abono}</span></p>
							  <p class="fw-bold meses-pagar"> Total: $<span class="no-bold">${valoresSeleccionados.length * paquete - abono}</span></p>
							</div>`;
		}else{
			
		contenedorMeses += `<div class="text-right total-meses meses-abono">
							  <p class="fw-bold meses-pagar"> Total: $<span class="no-bold">${valoresSeleccionados.length * paquete}</span></p>
							</div>`;
		}

		document.getElementById('confirmarMeses').innerHTML = contenedorMeses;
		$('#alerta-pagar-mes').css('visibility', 'hidden');
		$('#exampleModal').modal('show');
	}else{
		$('#alerta-pagar-mes').css('visibility', 'visible');

	}


}

$(document).ready(function() {
  $("input[type='text']").on("keypress", function() {
    $input = $(this);
    setTimeout(function() {
      $input.val($input.val().toUpperCase());
    }, 50);
  });
});
