var currentPage = 0;

document.body.addEventListener("refresh", function(){
    cargarClientes(currentPage);
})





function concultarCliente(id) {
	$('#idUser').val(id);
	console.log($('#idUser').val());
	var boton = document.getElementById('consultarCliente');
	boton.click();
}

function editarCliente(id) {
	$('#idUser').val(id);
	console.log($('#idUser').val());
	var boton = document.getElementById('editarCliente');
	boton.click();
}

function confirmarPago(id) {
	$('#idUser').val(id);
	console.log($('#idUser').val());
}


function cargarClientes(page) {
	$.get("/clientes/paginacion?page=" + page+ page+"&nombre="+$("#filtroNombre").val()+ "&size=" + $("#num-registros").val(), function(data) {
		currentPage = page;
		console.log(data);
		actualizarTabla(data.content);
		cargarPaginacion(data.totalPages);
	});
}

function actualizarTabla(clientes) {
	var tabla = $("#tablaClientes");
	tabla.empty();
	if (clientes.length !== 0) {
		clientes.forEach(function(cliente) {
			tabla.append(`
			
			                    <tr>
			                    
			            <td>
			            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-circle-fill" viewBox="0 0 16 16">
						  <circle cx="8" cy="8" r="8" fill="${cliente.estado ? 'green' : 'red'}"/>
						</svg>
						</td>        
                        <td>${cliente.nombre}</td>
                        <td>${cliente.telefono}</td>
                        <td>$${cliente.paquete}</td>
                        <td>
                        <input class="fechas" type="date" value="${cliente.fechaPago}" disabled>
                        </td>
                        <td>
                        <input class="fechas" type="date" value="${cliente.ultimoPago}" disabled>
                        </td>
                        <td>
							<button class="btn btn-success"	onclick="concultarCliente('${cliente.idCliente}');"							
								>
								<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-eye-fill"
									viewBox="0 0 16 16">
									<path d="M10.5 8a2.5 2.5 0 1 1-5 0 2.5 2.5 0 0 1 5 0" />
									<path d="M0 8s3-5.5 8-5.5S16 8 16 8s-3 5.5-8 5.5S0 8 0 8m8 3.5a3.5 3.5 0 1 0 0-7 3.5 3.5 0 0 0 0 7" />
								</svg>
							</button>
                            <button class="btn btn-warning" onclick="editarCliente('${cliente.idCliente}');">
                                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-pencil" viewBox="0 0 16 16">
                                    <path d="M12.146.146a.5.5 0 0 1 .708 0l3 3a.5.5 0 0 1 0 .708l-10 10a.5.5 0 0 1-.168.11l-5 2a.5.5 0 0 1-.65-.65l2-5a.5.5 0 0 1 .11-.168l10-10zM11.207 2.5 13.5 4.793 14.793 3.5 12.5 1.207zm1.586 3L10.5 3.207 4 9.707V10h.5a.5.5 0 0 1 .5.5v.5h.5a.5.5 0 0 1 .5.5v.5h.293zm-9.761 5.175-.106.106-1.528 3.821 3.821-1.528.106-.106A.5.5 0 0 1 5 12.5V12h-.5a.5.5 0 0 1-.5-.5V11h-.5a.5.5 0 0 1-.468-.325z"/>
                                </svg>
                            </button>
                            <button data-bs-toggle="modal" data-bs-target="#modal-confirmar-pago" onclick="confirmarPago('${cliente.idCliente}');" class="btn btn-success">
                                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-cash-coin" viewBox="0 0 16 16">
                                    <path fill-rule="evenodd" d="M11 15a4 4 0 1 0 0-8 4 4 0 0 0 0 8m5-4a5 5 0 1 1-10 0 5 5 0 0 1 10 0"/>
                                    <path d="M9.438 11.944c.047.596.518 1.06 1.363 1.116v.44h.375v-.443c.875-.061 1.386-.529 1.386-1.207 0-.618-.39-.936-1.09-1.1l-.296-.07v-1.2c.376.043.614.248.671.532h.658c-.047-.575-.54-1.024-1.329-1.073V8.5h-.375v.45c-.747.073-1.255.522-1.255 1.158 0 .562.378.92 1.007 1.066l.248.061v1.272c-.384-.058-.639-.27-.696-.563h-.668zm1.36-1.354c-.369-.085-.569-.26-.569-.522 0-.294.216-.514.572-.578v1.1h-.003zm.432.746c.449.104.655.272.655.569 0 .339-.257.571-.709.614v-1.195l.054.012z"/>
                                    <path d="M1 0a1 1 0 0 0-1 1v8a1 1 0 0 0 1 1h4.083c.058-.344.145-.678.258-1H3a2 2 0 0 0-2-2V3a2 2 0 0 0 2-2h10a2 2 0 0 0 2 2v3.528c.38.34.717.728 1 1.154V1a1 1 0 0 0-1-1z"/>
                                    <path d="M9.998 5.083 10 5a2 2 0 1 0-3.132 1.65 5.982 5.982 0 0 1 3.13-1.567z"/>
                                  </svg>
                            </button>
                        </td>
                    </tr>
			
			`);
			$("#result-registros").hide();
		});
	} else {
		$("#result-registros").show();
	}
}

function cargarPaginacion(totalPages) {
	var paginacion = $("#paginacion");
	paginacion.empty();

	var maxPaginasMostrar = 5; // Número máximo de páginas a mostrar

	if (currentPage > 0) {
		paginacion.append('<li class="page-item"><button class="page-link" onclick="cargarClientes(' + (currentPage - 1) + ')">Anterior</button></li>');
	} else {
		paginacion.append('<li class="page-item"><button class="page-link disabled" onclick="cargarClientes(' + (0) + ')" disabled>Anterior</button></li>');
	}

	// Lógica para mostrar rangos de páginas
	if (totalPages <= maxPaginasMostrar) {
		for (var i = 0; i < totalPages; i++) {
			agregarPagina(i);
		}
	} else {
		if (currentPage < maxPaginasMostrar - 1) {
			for (var i = 0; i < maxPaginasMostrar; i++) {
				agregarPagina(i);
			}
			paginacion.append('<li class="page-item"><span class="page-link">...</span></li>');
			agregarPagina(totalPages - 1);
		} else if (currentPage > totalPages - maxPaginasMostrar) {
			agregarPagina(0);
			paginacion.append('<li class="page-item"><span class="page-link">...</span></li>');
			for (var i = totalPages - maxPaginasMostrar; i < totalPages; i++) {
				agregarPagina(i);
			}
		} else {
			agregarPagina(0);
			paginacion.append('<li class="page-item"><span class="page-link">...</span></li>');
			for (var i = currentPage - Math.floor(maxPaginasMostrar / 2); i <= currentPage + Math.floor(maxPaginasMostrar / 2); i++) {
				agregarPagina(i);
			}
			paginacion.append('<li class="page-item"><span class="page-link">...</span></li>');
			agregarPagina(totalPages - 1);
		}
	}

	if (currentPage < totalPages - 1) {
		paginacion.append('<li class="page-item"><button class="page-link" onclick="cargarClientes(' + (currentPage + 1) + ')">Siguiente</button></li>');
	} else {
		paginacion.append('<li class="page-item"><button class="page-link disabled" onclick="cargarClientes(' + (0) + ')" disabled>Siguiente</button></li>');
	}

	function agregarPagina(page) {
		let mostrar = page + 1;
		if (page === currentPage) {
			paginacion.append(`<li class="page-item active"><span id="current-page" class="page-link" data-currentPage = "${page}"> ${mostrar}</span></li>`);
		} else {
			paginacion.append('<li class="page-item"><button class="page-link" onclick="cargarClientes(' + page + ')">' + mostrar + '</button></li>');
		}
	}
}


$(document).ready(function() {
	cargarClientes(0);
		$("#filtroNombre").on("input", function () {
		cargarClientes(0);
	});
});

function numRegistros() {
	cargarClientes(0);
}