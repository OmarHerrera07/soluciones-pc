const notificaError = (msg) => Lobibox.notify("error", { msg });
const notificaExito = (msg) => Lobibox.notify("success", { msg });
const notificaAdvertencia = (msg) => Lobibox.notify("warning", { msg });
const notificaAviso = (msg) => Lobibox.notify("info", { msg });

//************************************************************/////////
function validarYConvertirMayusculas(inputElement) {
	// Obtener el valor actual del campo de entrada
	var valor = inputElement.value;

	if (valor !== ""){
	// Expresión regular que permite letras, espacios y algunos caracteres especiales comunes
	var expresionRegular = /^[a-zA-Z\sáéíóúñÑÁÉÍÓÚüÜ]+$/;

	// Verificar si el valor cumple con la expresión regular
	if (!expresionRegular.test(valor)) {
		// Si contiene caracteres especiales, mostrar un mensaje de error y limpiar el valor
		alert("No debe contener caracteres especiales");
		inputElement.value = '';
	} else {
		// Convertir el valor a mayúsculas si no contiene caracteres especiales
		inputElement.value = valor.toUpperCase();
	}

	}
}
///////////////////////////////////////////////////////////////////////////////////////////////7
function validarTelefono(inputElement) {
	var valor= inputElement.value.trim();  // Se utiliza trim para eliminar espacios en blanco al inicio y al final

	// Expresión regular que permite solo dígitos numéricos
	var expresionRegular = /^[0-9]+$/;

	// Verificar si el valor no está vacío y cumple con la expresión regular
	if (valor !== "" && !expresionRegular.test(valor)) {
		// Si contiene caracteres no permitidos, mostrar un mensaje de error y limpiar el valor
		alert("El teléfono solo debe contener dígitos numéricos.");
		inputElement.value = '';
	}
}

function validarCaracteres(inputElement){
	var valor= inputElement.value.trim();


	if (valor !== ""){
		// Expresión regular que permite letras, espacios y algunos caracteres especiales comunes
		var expresionRegular = /^[a-zA-Z0-9]+$/;

		// Verificar si el valor cumple con la expresión regular
		if (!expresionRegular.test(valor)) {
			// Si contiene caracteres especiales, mostrar un mensaje de error y limpiar el valor
			alert("No debe contener caracteres especiales");
			inputElement.value = '';
		} else {
			// Convertir el valor a mayúsculas si no contiene caracteres especiales
			inputElement.value = valor.toUpperCase();
		}

	}
}

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
