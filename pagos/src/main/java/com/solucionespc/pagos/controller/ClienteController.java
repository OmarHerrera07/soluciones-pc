package com.solucionespc.pagos.controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.solucionespc.pagos.dto.*;
import com.solucionespc.pagos.entity.Pago;
import com.solucionespc.pagos.service.*;
import com.solucionespc.pagos.utils.PDFReporteClientes;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.itextpdf.text.DocumentException;
import com.solucionespc.pagos.entity.Cliente;
import com.solucionespc.pagos.entity.Colonia;
import com.solucionespc.pagos.entity.Usuario;

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxTrigger;

@Controller
@RequestMapping("/clientes")
public class ClienteController {

	@Autowired
	private IClienteService clienteService;

	@Autowired
	private IPaqueteService paqueteService;

	@Autowired
	private IColoniaService coloniaService;

	@Autowired
	private IUsuarioService usuarioService;

	/**
	 * Registrar un cliente nuevo
	 * 
	 * @param cliente cliente a registrar
	 * @return Resultado sobre el registro del usuario
	 */
	@PostMapping(value = "/registrar", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.TEXT_HTML_VALUE)
	@HxTrigger("refresh")
	@ResponseBody
	@ResponseStatus(code = HttpStatus.CREATED)
	public String registrarCliente(ClienteRegisterDTO cliente) {
		boolean res = clienteService.registrarCliente(cliente);

		if (res) {
			return "<div id=\"result\" data-notify=\"1\" hidden>Se ha registro el cliente</div>";
		}
		return "<div id=\"result\" data-notify=\"2\" hidden>Ha ocurrido un error en el registro del cliente</div>";
	}

	/**
	 * Editar un usuario nuevo
	 * 
	 * @param usuario a editar
	 * @return Resultado sobre el la edición del usuario
	 */
	@PostMapping(value = "/editar", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.TEXT_HTML_VALUE)
	@HxTrigger("refresh")
	@ResponseBody
	@ResponseStatus(code = HttpStatus.CREATED)
	public String editarCliente(ClienteRegisterDTO cliente) {
		boolean res = clienteService.editarCliente(cliente);

		if (res) {
			return "<div id=\"result\" data-notify=\"1\" hidden>Se ha registro el cliente</div>";
		}

		return "<div id=\"result\" data-notify=\"2\" hidden>Ha ocurrido un error al editar al cliente</div>";
	}

	/**
	 * Obtiene el cuerpo del formulario de registrar usuario
	 * 
	 * @param model          Proporcionar datos a la vista para que se muestren
	 *                       dinámicamente
	 * @param authentication Obtiene información sobre la autenticación actual
	 * @return fragmento de thymeleaf con el cuerpo del formulario
	 */
	@GetMapping("/get-form-registrar")
	public String getRegistrarClieneteForm(Model model, Authentication authentication) {
		model.addAttribute("paquetes", paqueteService.findAll());
		model.addAttribute("colonias", coloniaService.findAll());
		System.out.println(authentication.getName());
		return "fragments/clientes/registro-cliente :: registrar-cliente-form";
	}

	/**
	 * Obtiene el cuerpo del formulario de editar usuario
	 * 
	 * @param model Proporcionar datos a la vista para que se muestren dinámicamente
	 * @param id    del Cliente a consultar
	 * @return fragmento de thymeleaf con el cuerpo del formulario
	 */
	@GetMapping("/get-consulta-cliente")
	public String getConsultarUsuarioForm(@RequestParam(value = "id") Integer id, Model model) {
		Cliente cliente = clienteService.finById(id);
		model.addAttribute("cliente", cliente);
		return "fragments/clientes/consultar-cliente :: datos-consultar-cliente";
	}

	/**
	 * 
	 * @param id    id del cliente a editar (Para obtener su información)
	 * @param model Proporcionar datos a la vista para que se muestren dinámicamente
	 * @return fragmento con el formulario para mostrar la información del cliente
	 */
	@GetMapping("/get-editar-cliente")
	public String getEditarUsuarioForm(@RequestParam(value = "id") Integer id, Model model) {
		Cliente cliente = clienteService.finById(id);
		model.addAttribute("cliente", cliente);
		model.addAttribute("paquetes", paqueteService.findAll());
		model.addAttribute("colonias", coloniaService.findAll());
		System.out.println(cliente);
		return "fragments/clientes/editar-cliente :: editar-cliente-form";
	}
	
	/**
	 * Editar un usuario nuevo
	 * 
	 * @param usuario a editar
	 * @return Resultado sobre el la edición del usuario
	 */
	@PostMapping(value = "/eliminar", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.TEXT_HTML_VALUE)
	@HxTrigger("refresh")
	@ResponseBody
	@ResponseStatus(code = HttpStatus.CREATED)
	public String eliminarCliente(@RequestParam(value = "idEliminar") Integer id) {
		
		boolean totalRegistros = clienteService.totalMesesPagados(id);
		
		if(totalRegistros) {
			return "<div id=\"result\" data-notify=\"3\" hidden>No se puede eliminar un cliente con pagos</div>";
		}
		boolean res = clienteService.deteleCliente(id);
		if (res) {
			return "<div id=\"result\" data-notify=\"1\" hidden>Se ha eliminado el cliente</div>";
		}

		return "<div id=\"result\" data-notify=\"2\" hidden>Ha ocurrido un error al eliminar al cliente</div>";
	}

	/**
	 * 
	 * @param pageable clase para controlar la paginación y ordenamiento de los
	 *                 resultados.
	 * @param nombre   nombre del cliente que es usado para el filtro de busqueda
	 * @param size     tamaño de la pagina a mostrar
	 * @return clase con los resultados de la paginación (tamaño, clientes, etc)
	 */
	@ResponseBody
	@GetMapping("/paginacion")
	public Page<ClienteDTO> paginacion(Pageable pageable,
			@RequestParam(name = "nombre", required = false) String nombre,
			@RequestParam(name = "idColonia", required = false) Integer idColonia,
			@RequestParam(name = "size", required = false, defaultValue = "5") Integer size) {
		pageable = PageRequest.of(pageable.getPageNumber(), size, pageable.getSort());
		return clienteService.paginacionCliente(nombre, idColonia, pageable);
	}

	/**
	 * 
	 * @param id             id del cliente a realizar el pago
	 * @param authentication authentication representa la información de
	 *                       autenticación del usuario que realiza la solicitud
	 * @return resultado sobre si se realizo el pago
	 * @throws DocumentException indica que el método puede arrojar una excepción
	 *                           del tipo DocumentException
	 * @throws IOException       indica que el método puede arrojar una excepción
	 *                           del tipo IOException
	 */
	@PostMapping(value = "/realizar-pago", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.TEXT_HTML_VALUE)
	@HxTrigger("refresh")
	@ResponseBody
	@ResponseStatus(code = HttpStatus.CREATED)
	public String realizarPago(@RequestParam(value = "id") Integer id, Authentication authentication)
			throws DocumentException, IOException {

		Cliente cliente = clienteService.finById(id);
		Usuario user = usuarioService.finUserByUsername(authentication.getName());

		SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy/MM/dd");

		List<String> meses = new ArrayList<>();
		// Convertir el objeto Date a un String
		String fechaString = formatoFecha.format(cliente.getFechaPago());
		meses.add(fechaString);

		clienteService.pagoMasivo(meses, cliente, user.getIdUsuario());
		return "<div id=\"result\" data-notify=\"1\" hidden>Se ha registro el pago</div>";
	}

	/**
	 * 
	 * @param id    id del cliente a mostrar sus pagos
	 * @param model modelo que proporcionar datos a la vista para que se muestren
	 *              dinámicamente
	 * @return vista para mostrar los pagos que ha realizado el cliente (html)
	 */

	@GetMapping("/pagos")
	public String pagosClientes(@RequestParam(value = "id") Integer id, Model model) {

		Date fechaPago = clienteService.obtenerFechaPago(id);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(fechaPago);

		// Obtener el día del mes
		Integer diaDelMes = calendar.get(Calendar.DAY_OF_MONTH);
		System.out.println(fechaPago);

		int anioPago = calendar.get(Calendar.YEAR);
		model.addAttribute("anio", anioPago);
		model.addAttribute("cliente", clienteService.finById(id));
		model.addAttribute("idCliente", id);
		model.addAttribute("mesPago", fechaPago);
		model.addAttribute("meses", clienteService.generarMeses2(diaDelMes, anioPago));
		model.addAttribute("mesesPagados", clienteService.obtenerMesesPagados(id));
		return "cliente-pagos";
	}

	/**
	 * 
	 * @param idCliente      id del cliente a realizar sus pagos
	 * @param meses          meses que se van a pagar
	 * @param authentication Obtiene información sobre la autenticación actual
	 * @return resultado sobre si se realizo el pago
	 * @throws DocumentException indica que el método puede arrojar una excepción
	 *                           del tipo DocumentException
	 * @throws IOException       indica que el método puede arrojar una excepción
	 *                           del tipo IOException
	 */
	@PostMapping(value = "/realizar-pago-masivo", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.TEXT_HTML_VALUE)
	@HxTrigger("refresh")
	@ResponseBody
	@ResponseStatus(code = HttpStatus.CREATED)
	public String pagoMasivo(@RequestParam(value = "idCliente") Integer idCliente,
			@RequestParam(value = "meses") List<String> meses, Authentication authentication)
			throws IOException, DocumentException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

		System.out.println(meses);
		List<Date> mesesPagar = new ArrayList<>();
		for (String fechaString : meses) {
			try {
				Date fecha = dateFormat.parse(fechaString);
				// Aquí puedes hacer algo con el objeto Date
				mesesPagar.add(fecha);
			} catch (ParseException e) {
				e.printStackTrace();
				// Manejo de error en caso de que no se pueda parsear la fecha
			}
		}

		Usuario user = usuarioService.finUserByUsername(authentication.getName());
		Cliente cliente = clienteService.finById(idCliente);
		clienteService.pagoMasivo(meses, cliente, user.getIdUsuario());
		System.out.println(clienteService.finById(idCliente));
		return "<div id=\"result\" data-notify=\"1\" hidden>Se ha registro el pago</div>";
	}

	/**
	 * Maneja la solicitud para obtener una lista de fechas representando los
	 * últimos 14 meses. Utiliza la anotación {@code @GetMapping} para mapear las
	 * solicitudes HTTP GET dirigidas a "/meses". Devuelve la lista de fechas como
	 * cuerpo de respuesta en formato JSON.
	 *
	 * @param model El modelo utilizado para la vista (no utilizado en este caso).
	 * @return Una lista de objetos {@code Date} representando los últimos 14 meses.
	 */
	@GetMapping("/meses")
	@ResponseBody
	public List<Date> meses(Model model) {
		return clienteService.generarMeses(14);
	}

	/**
	 * Maneja la solicitud para obtener una lista de informes de clientes. Utiliza
	 * la anotación {@code @GetMapping} para mapear las solicitudes HTTP GET
	 * dirigidas a "/prueba". Devuelve la lista de informes de clientes como cuerpo
	 * de respuesta en formato JSON.
	 *
	 * @param model El modelo utilizado para la vista (no utilizado en este caso).
	 * @return Una lista de objetos {@code ReporteCliente}.
	 */
	@GetMapping("/prueba")
	@ResponseBody
	public List<ReporteCliente> meses2(Model model) {
		return clienteService.getReporteClientes();
	}

	/**
	 * Maneja la solicitud para refrescar la lista de meses pagados de un cliente.
	 * Utiliza la anotación {@code @GetMapping} para mapear las solicitudes HTTP GET
	 * dirigidas a "/refrescar-meses".
	 *
	 * @param id    El ID del cliente.
	 * @param anio  El año para filtrar los meses.
	 * @param model El modelo utilizado para la vista.
	 * @return La vista "cliente-pagos :: lista-meses" actualizada con la
	 *         información de los meses pagados.
	 */
	@GetMapping("/refrescar-meses")
	public String refrescarMeses(@RequestParam(value = "idRefresh") Integer id,
			@RequestParam(value = "anioRefresh") String anio, Model model) {

		Date fechaPago = clienteService.obtenerFechaPago(id);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(fechaPago);

		// Obtener el día del mes
		Integer diaDelMes = calendar.get(Calendar.DAY_OF_MONTH);
		System.out.println(fechaPago);
		int anioPago = calendar.get(Calendar.YEAR);
		model.addAttribute("cliente", clienteService.finById(id));
		model.addAttribute("anio", anioPago);
		model.addAttribute("idCliente", id);
		model.addAttribute("mesPago", fechaPago);
		model.addAttribute("meses", clienteService.generarMesesPorAnio(diaDelMes, Integer.parseInt(anio)));
		model.addAttribute("mesesPagados", clienteService.obtnerMesesPagadosFiltro(anio, id));

		return "cliente-pagos :: lista-meses";
	}

	/**
	 * Maneja la solicitud para obtener la lista de meses pagados de un cliente con
	 * filtro por año. Utiliza la anotación {@code @GetMapping} para mapear las
	 * solicitudes HTTP GET dirigidas a "/pagos-meses-filtro".
	 *
	 * @param id    El ID del cliente.
	 * @param anio  El año para filtrar los meses pagados.
	 * @param model El modelo utilizado para la vista.
	 * @return La vista "cliente-pagos :: lista-meses" con la información de los
	 *         meses pagados filtrada por año.
	 */
	@GetMapping("/pagos-meses-filtro")
	public String mesesPagadosFiltro(@RequestParam(value = "id") Integer id, @RequestParam(value = "anio") String anio,
			Model model) {

		Date fechaPago = clienteService.obtenerFechaPago(id);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(fechaPago);

		// Obtener el día del mes
		Integer diaDelMes = calendar.get(Calendar.DAY_OF_MONTH);
		System.out.println(fechaPago);
		int anioPago = calendar.get(Calendar.YEAR);
		model.addAttribute("cliente", clienteService.finById(id));
		model.addAttribute("anio", anio);
		model.addAttribute("idCliente", id);
		model.addAttribute("mesPago", fechaPago);
		model.addAttribute("meses", clienteService.generarMesesPorAnio(diaDelMes, Integer.parseInt(anio)));
		model.addAttribute("mesesPagados", clienteService.obtnerMesesPagadosFiltro(anio, id));

		return "cliente-pagos :: lista-meses";
	}

	/**
	 * Maneja la solicitud para exportar un informe en formato PDF.
	 *
	 * Este método utiliza la anotación {@code @GetMapping} para mapear las
	 * solicitudes HTTP GET dirigidas a la ruta "/reporte". Devuelve un archivo PDF
	 * como respuesta.
	 *
	 * @param request  La solicitud HTTP recibida.
	 * @param response La respuesta HTTP que se enviará al cliente.
	 * @throws DocumentException Excepción lanzada en caso de problemas al manipular
	 *                           el documento PDF.
	 * @throws IOException       Excepción lanzada en caso de problemas de
	 *                           entrada/salida.
	 */
	@GetMapping("/reporte")
	@ResponseBody
	public void exportPDF(HttpServletRequest request, HttpServletResponse response)
			throws DocumentException, IOException {
		response.setContentType("application/pdf");

		String headerKey = "Content-Disposition";
		String sbHeaderValue = "attachment; filename=Reporte.pdf";

		List<ReporteCliente> reportes = clienteService.getReporteClientes();

		PDFReporteClientes reporte = new PDFReporteClientes(reportes);
		reporte.export(response);

	}

	@GetMapping("/get-meses-pagados")
	public String getMesesPagados(@RequestParam(value = "idCliente") Integer id, Model model) {
		List<Date> meses = clienteService.obtenerMesesPagados(id);
		model.addAttribute("mesesPagados", meses);
		model.addAttribute("clienteId", id);
		return "fragments/clientes/cancelar-pagos :: form-eliminar-pago";
	}

	/**
	 * Editar un usuario nuevo
	 * 
	 * @param usuario a editar
	 * @return Resultado sobre el la edición del usuario
	 * @throws DocumentException 
	 * @throws IOException 
	 */
	@PostMapping(value = "/cancelar-pago", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.TEXT_HTML_VALUE)
	@HxTrigger("refresh")
	@ResponseBody
	@ResponseStatus(code = HttpStatus.CREATED)
	public String cancelarPago(@RequestParam(value = "clienteId") Integer id,
			@RequestParam(value = "mesesCancelar") List<String> meses) throws IOException, DocumentException {

		System.out.println(meses);
		for (String mes : meses) {			
			Integer idPago = clienteService.cancelarPago(id, mes);
			System.out.println("Este el id de pago");
			System.out.println(idPago);
			if(idPago>0) {
				
			}
		}
		return "<div id=\"result\" data-notify=\"1\" hidden>Se ha registro el pago</div>";
	}
	
	/**
	 * Registrar un cliente nuevo
	 * 
	 * @param cliente cliente a registrar
	 * @return Resultado sobre el registro del usuario
	 */
	@PostMapping(value = "/registrar-colonia", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.TEXT_HTML_VALUE)
	@HxTrigger("refreshCol")
	@ResponseBody
	@ResponseStatus(code = HttpStatus.CREATED)
	public String registrarColonia(@RequestParam(value = "colonia") String colonia) {
		
		Colonia col = new Colonia();
		col.setColonia(colonia);
		boolean res = coloniaService.save(col);

		if (res) {
			return "<div id=\"result\" data-notify=\"1\" hidden>Se ha registro el cliente</div>";
		}
		return "<div id=\"result\" data-notify=\"2\" hidden>Ha ocurrido un error en el registro del cliente</div>";
	}
	
	@GetMapping("/refrescar-colonias")
	public String getColonias(Model model) {
		model.addAttribute("colonias", coloniaService.findAll());
		System.out.println("ENTROOOOOOOOOOO");
		return "index :: select-colonias";
	}


}
