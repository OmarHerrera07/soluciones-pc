const notificaError = (msg) => Lobibox.notify("error", { msg });
const notificaExito = (msg) => Lobibox.notify("success", { msg });
const notificaAdvertencia = (msg) => Lobibox.notify("warning", { msg });
const notificaAviso = (msg) => Lobibox.notify("info", { msg });


function prueba(){
	notificaExito("Correcto");
}

const myDiv = document.getElementById('result-msg');
const observer = new MutationObserver((mutationsList, observer) => {
	var texto = $('#result').text();
	var notify = $('#result').data('notify');
	if(notify == '1'){
		notificaExito(texto);
	}else if(notify == '2'){
		notificaError(texto);
	}
});
const config = { childList: true };
observer.observe(myDiv, config);