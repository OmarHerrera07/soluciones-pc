var currentPage = 0;

function cargarPagos(page) {
	$.get("/pagos/paginacion?page=" + page + "&nombre=" + $("#filtroNombre").val() + "&size=" + $("#num-registros").val() + "&fechaInicio=" + $("#fecha-inicio").val() + "&fechaFin=" + $("#fecha-fin").val(), function(data) {
		currentPage = page;
		console.log(data);
		actualizarTabla(data.content);
		cargarPaginacion(data.totalPages);
	});
}

function actualizarTabla(pagos) {
	var tabla = $("#tablaPagos");
	tabla.empty();
	if (pagos.length !== 0) {
		pagos.forEach(function(pago) {
			tabla.append(`
					<tr>
					
					    <th>${pago.nombre}</th>
                        <td>${pago.fecha}</td>
                        <td>$${pago.total}</td>
                        <td>
                            <a href="/pagos/recibo" class="btn btn-warning">
                                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-file-earmark-text-fill" viewBox="0 0 16 16">
                                    <path d="M9.293 0H4a2 2 0 0 0-2 2v12a2 2 0 0 0 2 2h8a2 2 0 0 0 2-2V4.707A1 1 0 0 0 13.707 4L10 .293A1 1 0 0 0 9.293 0M9.5 3.5v-2l3 3h-2a1 1 0 0 1-1-1M4.5 9a.5.5 0 0 1 0-1h7a.5.5 0 0 1 0 1zM4 10.5a.5.5 0 0 1 .5-.5h7a.5.5 0 0 1 0 1h-7a.5.5 0 0 1-.5-.5m.5 2.5a.5.5 0 0 1 0-1h4a.5.5 0 0 1 0 1z"/>
                                  </svg>
                            </a>
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
		paginacion.append('<li class="page-item"><button class="page-link" onclick="cargarPagos(' + (currentPage - 1) + ')">Anterior</button></li>');
	} else {
		paginacion.append('<li class="page-item"><button class="page-link disabled" onclick="cargarPagos(' + (0) + ')" disabled>Anterior</button></li>');
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
		paginacion.append('<li class="page-item"><button class="page-link" onclick="cargarPagos(' + (currentPage + 1) + ')">Siguiente</button></li>');
	} else {
		paginacion.append('<li class="page-item"><button class="page-link disabled" onclick="cargarPagos(' + (0) + ')" disabled>Siguiente</button></li>');
	}

	function agregarPagina(page) {
		let mostrar = page + 1;
		if (page === currentPage) {
			paginacion.append(`<li class="page-item active"><span id="current-page" class="page-link" data-currentPage = "${page}"> ${mostrar}</span></li>`);
		} else {
			paginacion.append('<li class="page-item"><button class="page-link" onclick="cargarPagos(' + page + ')">' + mostrar + '</button></li>');
		}
	}
}


$(document).ready(function() {
	cargarPagos(0);
	$("#filtroNombre").on("input", function() {
		cargarPagos(0);
	});

	$("#fecha-inicio").on("change", function() {
		cargarPagos(0);
	});

	$("#fecha-fin").on("change", function() {
		cargarPagos(0);
	});



});

function numRegistros() {
	cargarPagos(0);
}