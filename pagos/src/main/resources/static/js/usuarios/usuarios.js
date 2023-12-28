
var currentPage = 0;

function cargarUsuarios(page) {
	$.get("/usuarios/paginacion?page=" + page, function (data) {
		currentPage = page;
		console.log(data);
		//actualizarTabla(data.content);
		//cargarPaginacion(data.totalPages);
	});
}

function actualizarTabla(ubicaciones) {
	var tabla = $("#tablaUbicaciones");
	tabla.empty();
	if (ubicaciones.length !== 0) {
		ubicaciones.forEach(function (ubicacion) {
			tabla.append(`<tr>
			<td>${ubicacion.nombreUbicacion}</td>
			<td>
				<button
				onclick="mostrarModal('${ubicacion.idUbicacion}','ubicacion')" type="button" class="btn btn-success">
				<i class="bi bi-eye-fill"></i>
				</button>			
			</td>
			<td>
				<button
				onclick="detalles('${ubicacion.idUbicacion}')" type="button" class="btn btn-warning">
				<i class="bi bi-pencil"></i>
				</button>			
			</td>
			<td>
				<button
					onclick="eliminarConfirmar('${ubicacion.idUbicacion}','${ubicacion.nombreUbicacion}')"
					type="button" class="btn btn-danger" data-bs-toggle="modal" data-bs-target="#modal-eliminar-ubicacion">
					<i class="bi bi-x-circle"></i>						
				</button>
			</td>			
			</tr>"`);
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
		paginacion.append('<li class="page-item"><button class="page-link" onclick="cargarUbicaciones(' + (currentPage - 1) + ')">Anterior</button></li>');
	} else {
		paginacion.append('<li class="page-item"><button class="page-link disabled" onclick="cargarUbicaciones(' + (0) + ')" disabled>Anterior</button></li>');
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
		paginacion.append('<li class="page-item"><button class="page-link" onclick="cargarUbicaciones(' + (currentPage + 1) + ')">Siguiente</button></li>');
	} else {
		paginacion.append('<li class="page-item"><button class="page-link disabled" onclick="cargarUbicaciones(' + (0) + ')" disabled>Siguiente</button></li>');
	}

	function agregarPagina(page) {
		let mostrar = page + 1;
		if (page === currentPage) {
			paginacion.append(`<li class="page-item active"><span id="current-page" class="page-link" data-currentPage = "${page}"> ${mostrar}</span></li>`);
		} else {
			paginacion.append('<li class="page-item"><button class="page-link" onclick="cargarUbicaciones(' + page + ')">' + mostrar + '</button></li>');
		}
	}
}


$(document).ready(function () {
	cargarUsuarios(0);
});