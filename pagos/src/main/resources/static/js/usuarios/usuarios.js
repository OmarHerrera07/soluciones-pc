
function editarUsuario(id) {
	console.log("HOL");
	$('#idUser').val(id);
	console.log($('#idUser').val());
	var boton = document.getElementById('botonid');

// Simular un clic en el botón
boton.click();
}

var currentPage = 0;

function cargarUsuarios(page) {
	$.get("/usuarios/paginacion?page=" + page+"&nombre="+$("#filtroNombre").val()+ "&size=" + $("#num-registros").val(), function(data) {
		currentPage = page;
		console.log(data);
		actualizarTabla(data.content);
		cargarPaginacion(data.totalPages);
	});
}

function actualizarTabla(usuarios) {
	var tabla = $("#tablaUsuarios");
	tabla.empty();
	var button = $("#pruebaB");
	console.log(button);
	button.attr('hx-get', '/usuarios/get-form-editar?id=1');
	button.attr('hx-target', '#form-editar-usuario');
	button.attr('hx-swap', 'innerHTML');
	if (usuarios.length !== 0) {
		usuarios.forEach(function(user) {
			tabla.append(`
								<tr>
						<td>${user.nombre}</td>
						<td>${user.username}</td>
						<td>Admin</td>
						<td>
							<button type="button" 
								onclick="editarUsuario('${user.idUsuario}');"
								class="btn btn-warning">
										<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor"
									class="bi bi-pencil" viewBox="0 0 16 16">
									<path
										d="M12.146.146a.5.5 0 0 1 .708 0l3 3a.5.5 0 0 1 0 .708l-10 10a.5.5 0 0 1-.168.11l-5 2a.5.5 0 0 1-.65-.65l2-5a.5.5 0 0 1 .11-.168l10-10zM11.207 2.5 13.5 4.793 14.793 3.5 12.5 1.207zm1.586 3L10.5 3.207 4 9.707V10h.5a.5.5 0 0 1 .5.5v.5h.5a.5.5 0 0 1 .5.5v.5h.293zm-9.761 5.175-.106.106-1.528 3.821 3.821-1.528.106-.106A.5.5 0 0 1 5 12.5V12h-.5a.5.5 0 0 1-.5-.5V11h-.5a.5.5 0 0 1-.468-.325z" />
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
		paginacion.append('<li class="page-item"><button class="page-link" onclick="cargarUsuarios(' + (currentPage - 1) + ')">Anterior</button></li>');
	} else {
		paginacion.append('<li class="page-item"><button class="page-link disabled" onclick="cargarUsuarios(' + (0) + ')" disabled>Anterior</button></li>');
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
		paginacion.append('<li class="page-item"><button class="page-link" onclick="cargarUsuarios(' + (currentPage + 1) + ')">Siguiente</button></li>');
	} else {
		paginacion.append('<li class="page-item"><button class="page-link disabled" onclick="cargarUsuarios(' + (0) + ')" disabled>Siguiente</button></li>');
	}

	function agregarPagina(page) {
		let mostrar = page + 1;
		if (page === currentPage) {
			paginacion.append(`<li class="page-item active"><span id="current-page" class="page-link" data-currentPage = "${page}"> ${mostrar}</span></li>`);
		} else {
			paginacion.append('<li class="page-item"><button class="page-link" onclick="cargarUsuarios(' + page + ')">' + mostrar + '</button></li>');
		}
	}
}


$(document).ready(function() {
	cargarUsuarios(0);
		$("#filtroNombre").on("input", function () {
		cargarUsuarios(0);
	});
});

function numRegistros() {
	cargarUsuarios(0);
}