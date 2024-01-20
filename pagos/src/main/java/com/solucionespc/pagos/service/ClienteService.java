package com.solucionespc.pagos.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.solucionespc.pagos.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.itextpdf.text.DocumentException;
import com.solucionespc.pagos.entity.Cliente;
import com.solucionespc.pagos.entity.Colonia;
import com.solucionespc.pagos.entity.Pago;
import com.solucionespc.pagos.entity.Paquete;
import com.solucionespc.pagos.entity.Rol;
import com.solucionespc.pagos.entity.Usuario;
import com.solucionespc.pagos.repository.ClienteRepository;
import com.solucionespc.pagos.repository.MesesPagoRepositoty;
import com.solucionespc.pagos.repository.PagoRepository;
import com.solucionespc.pagos.repository.UsuarioRepository;
import com.solucionespc.pagos.utils.PDFRecibo;
import com.solucionespc.pagos.utils.PrintTicket;
import com.solucionespc.pagos.utils.PrintTicketNew;
import com.solucionespc.pagos.utils.ReciboAbono;

import jakarta.transaction.Transactional;

@Service
public class ClienteService implements IClienteService {

	@Autowired
	private ClienteRepository clienteRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private PagoRepository pagoRepository;

	@Autowired
	private MesesPagoRepositoty mesesPagoRepositoty;

	/**
	 * Recupera todos los clientes almacenados en el sistema.
	 *
	 * @return Lista de objetos Cliente.
	 */
	@Override
	public List<Cliente> findAll() {
		return clienteRepository.findAll();
	}

	/**
	 * Realiza una paginación de clientes según el nombre y la colonia
	 * proporcionados.
	 *
	 * @param nombre    El nombre del cliente a buscar.
	 * @param idColonia El ID de la colonia a la que pertenece el cliente.
	 * @param pageable  Información sobre la paginación.
	 * @return Una página de objetos ClienteDTO que cumplen con los criterios de
	 *         búsqueda.
	 */
	@Override
	public Page<ClienteDTO> paginacionCliente(String nombre, Integer idColonia, Pageable pageable) {
		return clienteRepository.paginacionCliente(nombre, idColonia, pageable);
	}

	/**
	 * Realiza una operación de prueba recuperando una lista de objetos ClienteDTO.
	 *
	 * @return Lista de objetos ClienteDTO.
	 */

	@Override
	public List<ClienteDTO> prueba() {
		return clienteRepository.prueba();
	}

	/**
	 * Registra un nuevo cliente en el sistema utilizando la información
	 * proporcionada en un objeto ClienteRegisterDTO.
	 *
	 * @param c Objeto ClienteRegisterDTO con la información del nuevo cliente.
	 * @return true si el registro fue exitoso, false en caso contrario.
	 */

	@Override
	public boolean registrarCliente(ClienteRegisterDTO c) {
		Cliente cliente = new Cliente();
		cliente.setNombre(c.getNombre());
		cliente.setTelefono(c.getTelefono());
		cliente.setTelefono2(c.getTelefono2());
		cliente.setCoordenadas(c.getCoordenadas());
		cliente.setFechaPago(c.getFecha());
		cliente.setRfc(c.getRfc());
		cliente.setEstado(c.getEstado());
		cliente.setObservaciones(c.getObservaciones());
		cliente.setPaquete(Paquete.builder().idPaquete(c.getPaquete()).build());
		cliente.setColonia(Colonia.builder().idColonia(c.getIdColonia()).build());
		cliente.setDiasAtraso(0);
		if (c.getAbono() != null) {
			if (c.getAbono() > 0) {
				cliente.setAbono(c.getAbono());
			} else {
				cliente.setAbono(0f);
			}

		} else {
			cliente.setAbono(0f);
		}

		try {
			clienteRepository.save(cliente);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Recupera un cliente por su ID.
	 *
	 * @param id El ID del cliente a recuperar.
	 * @return El objeto Cliente correspondiente al ID proporcionado.
	 */
	@Override
	public Cliente finById(Integer id) {
		// TODO Auto-generated method stub
		return clienteRepository.findById(id).get();
	}

	/**
	 * Edita la información de un cliente existente en el sistema utilizando la
	 * información proporcionada en un objeto ClienteRegisterDTO.
	 *
	 * @param c Objeto ClienteRegisterDTO con la información actualizada del
	 *          cliente.
	 * @return true si la edición fue exitosa, false en caso contrario.
	 */
	@Override
	public boolean editarCliente(ClienteRegisterDTO c) {
		Cliente cliente = clienteRepository.findById(c.getIdCliente()).get();
		cliente.setNombre(c.getNombre());
		cliente.setTelefono(c.getTelefono());
		cliente.setTelefono2(c.getTelefono2());
		cliente.setCoordenadas(c.getCoordenadas());
		cliente.setFechaPago(c.getFecha());
		cliente.setRfc(c.getRfc());
		cliente.setEstado(c.getEstado());
		cliente.setPaquete(Paquete.builder().idPaquete(c.getPaquete()).build());
		cliente.setColonia(Colonia.builder().idColonia(c.getIdColonia()).build());
		cliente.setObservaciones(c.getObservaciones());
		cliente.setAbono(c.getAbono());
		try {
			clienteRepository.save(cliente);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Realiza un pago para un cliente y actualiza la información en la base de
	 * datos.
	 *
	 * @param idCliente ID del cliente para el cual se realiza el pago.
	 * @param username  Nombre de usuario asociado al pago.
	 * @return true si el pago se realiza con éxito, false en caso contrario.
	 */

	@Override
	public boolean realizarPago(Integer idCliente, String username) {
		try {
			clienteRepository.actualizarPagos(idCliente);

			System.out.println();
			Pago pago = pagoRepository.ObtenerPago(idCliente);
			if (pago != null) {
				System.out.println("SI EXISTE");
			} else {
				System.out.println("NO EXISTE");
				pago = new Pago();
			}

			LocalDate fechaActual = LocalDate.now();
			pago.setFecha(Date.from(fechaActual.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
			pago.setIdCliente(Cliente.builder().idCliente(idCliente).build());

			Usuario user = usuarioRepository.finUserByUsername(username);
			pago.setIdUsuario(user);

			List<MesesPagoDTO> mesespagados = mesesPagoRepositoty.ObtenerPagosRealizados(idCliente);

			float sumaPrecios = (float) mesespagados.stream().map(MesesPagoDTO::getPrecio)
					.filter(precio -> precio != null) // Filtra los precios que no son nulos
					.mapToDouble(Float::doubleValue) // Convierte los Float a double para sumar
					.sum();

			pago.setTotal(sumaPrecios);

			pagoRepository.save(pago);

			return true;
		} catch (Exception e) {
			return false;
		}

	}

	/**
	 * Obtiene la lista de meses pagados para un cliente.
	 *
	 * @param idCliente ID del cliente.
	 * @return Lista de fechas representando los meses pagados.
	 */
	@Override
	public List<Date> obtenerMesesPagados(Integer idCliente) {
		// TODO Auto-generated method stub
		return mesesPagoRepositoty.obtnerMesesPagados(idCliente);
	}

	/**
	 * Genera una lista de fechas representando los meses para un año específico y
	 * un día de pago.
	 *
	 * @param diaDePago Día de pago para los clientes.
	 * @return Lista de fechas representando los meses generados.
	 */
	@Override
	public List<Date> generarMeses(Integer diaDePago) {
		List<Date> todosLosMeses = new ArrayList();
		int anioActual = LocalDate.now().getYear();

		for (int i = 0; i < 12; i++) {
			// Crear la fecha y agregarla a la lista
			LocalDate fechaLocal = LocalDate.of(anioActual, i + 1, diaDePago);
			Date fecha = java.sql.Date.valueOf(fechaLocal);
			todosLosMeses.add(fecha);
		}
		return todosLosMeses;
	}

	/**
	 * Genera una lista de objetos MesesDTO representando los meses para un año
	 * específico y un día de pago.
	 *
	 * @param diaDePago Día de pago para los clientes.
	 * @param anio      Año para el cual se generan los meses.
	 * @return Lista de objetos MesesDTO representando los meses generados.
	 */
	@Override
	public List<MesesDTO> generarMeses2(Integer diaDePago, Integer anio) {
		List<MesesDTO> todosLosMeses = new ArrayList<>();
		SimpleDateFormat formato = new SimpleDateFormat("MMMM", new Locale("es", "ES"));

		for (int i = 0; i < 12; i++) {
			// Crear la fecha y agregarla a la lista
			LocalDate fechaLocal = LocalDate.of(anio, i + 1, diaDePago);
			Date fecha = java.sql.Date.valueOf(fechaLocal);

			MesesDTO mes = new MesesDTO(fecha, formato.format(fecha));
			todosLosMeses.add(mes);
		}
		return todosLosMeses;
	}

	/**
	 * Genera una lista de objetos MesesDTO representando los meses para un año
	 * específico y un día de pago.
	 *
	 * @param diaDePago Día de pago para los clientes.
	 * @param anio      Año para el cual se generan los meses.
	 * @return Lista de objetos MesesDTO representando los meses generados.
	 */
	@Override
	public List<MesesDTO> generarMesesPorAnio(Integer diaDePago, Integer anio) {
		List<MesesDTO> todosLosMeses = new ArrayList<>();
		SimpleDateFormat formato = new SimpleDateFormat("MMMM", new Locale("es", "ES"));

		for (int i = 0; i < 12; i++) {
			// Crear la fecha y agregarla a la lista
			LocalDate fechaLocal = LocalDate.of(anio, i + 1, diaDePago);
			Date fecha = java.sql.Date.valueOf(fechaLocal);

			MesesDTO mes = new MesesDTO(fecha, formato.format(fecha));
			todosLosMeses.add(mes);
		}
		return todosLosMeses;
	}

	/**
	 * Obtiene la fecha de pago para un cliente.
	 *
	 * @param idCliente ID del cliente.
	 * @return La fecha de pago del cliente.
	 */

	@Override
	public Date obtenerFechaPago(Integer idCliente) {
		// TODO Auto-generated method stub
		return clienteRepository.obtenerFechaPago(idCliente);
	}

	/**
	 * Realiza un pago masivo para un cliente y actualiza la información en la base
	 * de datos.
	 *
	 * @param meses     Lista de meses para los cuales se realiza el pago masivo.
	 * @param cliente   Cliente para el cual se realiza el pago masivo.
	 * @param idUsuario ID del usuario asociado al pago.
	 * @throws DocumentException Excepción lanzada en caso de problemas al manipular
	 *                           el documento PDF.
	 * @throws IOException       Excepción lanzada en caso de problemas de
	 *                           entrada/salida.
	 */

	@Override
	public void pagoMasivo(List<String> meses, Cliente cliente, Integer idUsuario, Integer tipoPago)
			throws DocumentException, IOException {

		Integer tipoRecibo = 1;
		Float residuo = 0f;
		PrintTicket printTicket = new PrintTicket();
		Pago pago = new Pago();
		pago.setFecha(new Date());
		pago.setIdCliente(Cliente.builder().idCliente(cliente.getIdCliente()).build());
		pago.setIdUsuario(Usuario.builder().idUsuario(idUsuario).build());
		pago.setTipoPago(tipoPago);

		Pago res = pagoRepository.save(pago);

		for (String mes : meses) {
			System.out.println(mes);
			clienteRepository.InsertarMesPago(cliente.getIdCliente(), cliente.getPaquete().getIdPaquete(),
					res.getIdPago(), mes);
		}

		// Se actualiza el tipo de pago y si hay un abono
		pagoRepository.actualizarInfoPago(res.getIdPago(), residuo, tipoRecibo);
		InfoRecibo i = pagoRepository.getInfoRecibo(res.getIdPago());
		if (cliente.getAbono() > 0) {
			tipoRecibo = 4;
			residuo = cliente.getAbono();
			// Se actualiza el tipo de pago y si hay un abono
			pagoRepository.actualizarInfoPago(res.getIdPago(), residuo, tipoRecibo);
			pagoRepository.actualizarTotal(i.getTotal() - cliente.getAbono(), res.getIdPago());
			i = pagoRepository.getInfoRecibo(res.getIdPago());
			clienteRepository.setAbono(0f, cliente.getIdCliente());
		}

		List<MesesRecibo> mesesR = mesesPagoRepositoty.obtnerMesesPagadosRecibo(cliente.getIdCliente(),
				res.getIdPago());

		if (cliente.getAbono() > 0) {
			ReciboAbono reciboAbono = new ReciboAbono(i, mesesR, cliente.getAbono(), 4);

			System.out.println(i.getTotal());
			System.out.println(mesesR);
			System.out.println(tipoPago);
			byte[] pdfBytes = reciboAbono.getPdfBytes();
			pagoRepository.actualizarRecibo(pdfBytes, res.getIdPago());
		} else {
			PDFRecibo recibo = new PDFRecibo(i, mesesR);
			//printTicket.printTicket(i, mesesR);
			byte[] pdfBytes = recibo.getPdfBytes();
			pagoRepository.actualizarRecibo(pdfBytes, res.getIdPago());
		}
		PrintTicketNew printTicketNew = new PrintTicketNew();

		printTicketNew.printTicket(i, mesesR, residuo, tipoRecibo);

	}

	/**
	 * Obtiene la lista de meses pagados para un cliente filtrada por año.
	 *
	 * @param anio      Año para el cual se filtran los meses pagados.
	 * @param idCliente ID del cliente.
	 * @return Lista de fechas representando los meses pagados filtrados por año.
	 */

	@Override
	public List<Date> obtnerMesesPagadosFiltro(String anio, Integer idCliente) {
		return mesesPagoRepositoty.obtnerMesesPagadosFiltro(anio, idCliente);
	}

	/**
	 * Obtiene un informe de clientes que tienen adeudo.
	 *
	 * @return Lista de objetos ReporteCliente que representan el informe de los
	 *         clientes que tienen adeudo.
	 */

	@Override
	public List<ReporteCliente> getReporteClientes() {
		return clienteRepository.getReporteClientes();
	}
	
	/**
	 * Cancacela meses de pagos para un cliente por medio de su id 
	 */
	@Override
	@Transactional
	public Integer cancelarPago(Integer idCliente, String fecha) throws IOException, DocumentException {
		Integer idPago = clienteRepository.cancelarPago(idCliente, fecha);
		if (idPago > 0) {
			InfoRecibo i = pagoRepository.getInfoRecibo(idPago);

			List<MesesRecibo> mesesR = mesesPagoRepositoty.obtnerMesesPagadosRecibo(idCliente, idPago);
			// PDFRecibo recibo = new PDFRecibo(i, mesesR);
			// byte[] pdfBytes = recibo.getPdfBytes();
			// pagoRepository.actualizarRecibo(pdfBytes, idPago);

			ReciboAbono reciboAbono = new ReciboAbono(i, mesesR, i.getAbono(), i.getTipoTicket());

			if (i.getTipoTicket() == 3) {
				pagoRepository.actualizarTotal(i.getTotal() + i.getAbono(), idPago);
				i = pagoRepository.getInfoRecibo(idPago);
			} else if (i.getTipoTicket() == 4) {
				pagoRepository.actualizarTotal(i.getTotal() - i.getAbono(), idPago);
				i = pagoRepository.getInfoRecibo(idPago);
			}

			byte[] pdfBytes = reciboAbono.getPdfBytes();
			pagoRepository.actualizarRecibo(pdfBytes, idPago);
		}
		return idPago;
	}

	/**
	 * Elimina un cliente del sistema, simpre y cuando este no tenga ningun pago registrado
	 */
	@Override
	public boolean deteleCliente(Integer idCliente) {
		try {
			clienteRepository.deleteById(idCliente);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	
	/**
	 * Verifica si un cliente tienen pagos hechos en el sistema
	 */
	@Override
	public boolean totalMesesPagados(Integer idCliente) {
		Integer res = mesesPagoRepositoty.totalMesesPagados(idCliente);

		if (res > 0) {
			return true;
		}
		return false;
	}

	/**
	 * Realiza un pago de un cliente por medio de un abono
	 */
	@Override
	public boolean abonoCliente(Integer idCliente, Float abono, Integer tipoPago)
			throws IOException, DocumentException {
		
		/**
		 * Tipo de tickets
		 * 1.- Ticket normal (solo contiene meses de pago)
		 * 2.- Ticket de abono (solo contiene el abono, sin ningun mes pagado)
		 * 3.- Ticket con mes de pago más abono a favor
		 * 4.- Ticket usando el abono que el cliente tenia guardado
		 * 
		 */

		try {

			Cliente cliente = clienteRepository.findById(idCliente).get();
			/**
			 * Estado 1: ticket normal Estado 2: Solo abono x Estado 3: Meses mas abono
			 * Estado 4: Usando el abono x
			 */
			Integer tipoTicket;
			Float residuo = 0f;
			Pago pago = new Pago();
			List<String> fechasGeneradas = new ArrayList<>();
			Integer tipoRecibo = 1;
			Pago res = new Pago();

			float resultado = (abono + cliente.getAbono()) / cliente.getPaquete().getPrecio();
			int numMeses = (int) Math.floor(resultado);
			residuo = (abono + cliente.getAbono()) % cliente.getPaquete().getPrecio();

			System.out.println("Num de meses: ");
			System.out.println(numMeses);
			System.out.println("Abono proximo mes: ");
			System.out.println(residuo);

			clienteRepository.setAbono(residuo, idCliente);

			// Número de meses a generar
			int numeroMeses = numMeses;

			// Formato deseado para la impresión
			DateTimeFormatter formatoDeseado = DateTimeFormatter.ofPattern("yyyy/MM/dd");
			LocalDate fechaInicio = cliente.getFechaPago().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			// Lista para almacenar las fechas generadas

			// Generar fechas y agregarlas a la lista
			for (int i = 0; i < numeroMeses; i++) {
				LocalDate fechaGenerada = fechaInicio.plusMonths(i);
				fechasGeneradas.add(fechaGenerada.format(formatoDeseado));
			}

			// Imprimir la lista de fechas generadas
			System.out.println("Fechas generadas:");
			fechasGeneradas.forEach(System.out::println);

			pago.setFecha(new Date());
			pago.setIdCliente(Cliente.builder().idCliente(cliente.getIdCliente()).build());
			pago.setIdUsuario(Usuario.builder().idUsuario(1).build());
			pago.setTipoPago(tipoPago);
			res = pagoRepository.save(pago);

			for (String mes : fechasGeneradas) {
				System.out.println(mes);
				clienteRepository.InsertarMesPago(cliente.getIdCliente(), cliente.getPaquete().getIdPaquete(),
						res.getIdPago(), mes);
			}
			pagoRepository.actualizarTotal(abono, res.getIdPago());

			if ((abono + cliente.getAbono()) >= cliente.getPaquete().getPrecio()) {
				if (cliente.getAbono() == 0) {

					if (residuo == 0) {
						// Estado 1: ticket normal
						tipoRecibo = 1;
					} else {
						// Estado 3: Meses mas abono
						tipoRecibo = 3;
					}
					/**
					 * Estado 1: ticket normal Estado 2: Solo abono x Estado 3: Meses mas abono
					 * Estado 4: Usando el abono x
					 */

				} else {
					if ((abono % cliente.getPaquete().getPrecio() == 0)) {
						// Estado 1: ticket normal
						tipoRecibo = 1;
					} else {
						// Estado 4: Usando el abono

						if ((residuo != cliente.getAbono()) && residuo == 0) {
							residuo = cliente.getAbono();
						}

						tipoRecibo = 4;
						Float abonoCurrent = clienteRepository.obtenerAbonoActual(idCliente);
						if ((residuo - abonoCurrent) == 0) {
							residuo = residuo - cliente.getAbono();
							tipoRecibo = 3;
							System.out.println("ENTRO EN LA CONDICIÓN");
						}
						System.out.println("Este es el abono last");
						System.out.println(cliente.getAbono());
						System.out.println("Abono actual");
						System.out.println(clienteRepository.obtenerAbonoActual(idCliente));
						System.out.println("Este es el residuo");
						System.out.println(residuo);
					}

				}

			} else {
				// Estado 2: Solo abono
				tipoRecibo = 2;
				residuo = abono;

			}

			// Se actualiza el tipo de pago y si hay un abono
			pagoRepository.actualizarInfoPago(res.getIdPago(), residuo, tipoRecibo);

			InfoRecibo i = pagoRepository.getInfoRecibo(res.getIdPago());

			List<MesesRecibo> mesesR = mesesPagoRepositoty.obtnerMesesPagadosRecibo(cliente.getIdCliente(),
					res.getIdPago());
			ReciboAbono reciboAbono = new ReciboAbono(i, mesesR, residuo, tipoRecibo);

			System.out.println(i.getTotal());
			System.out.println(mesesR);
			System.out.println(tipoPago);
			byte[] pdfBytes = reciboAbono.getPdfBytes();
			pagoRepository.actualizarRecibo(pdfBytes, res.getIdPago());

			PrintTicketNew printTicket = new PrintTicketNew();

			printTicket.printTicket(i, mesesR, residuo, tipoRecibo);

			return true;
		} catch (Exception e) {
			return false;
		}

	}
	
	/**
	 * Obtinene los meses que un cliente a pagado usando el año como filtro
	 */
	@Override
	public List<Date> obtenerMesesPagadosPorAnio(Integer idCliente, Integer anio) {
		return mesesPagoRepositoty.obtenerMesesPagadosPorAnio(idCliente, anio);
	}

	/**
	 * Obtiene el abono actualziado de un cliente por medio de su id
	 */
	@Override
	public Float obtenerAbonoActual(Integer idCliente) {
		// TODO Auto-generated method stub
		return clienteRepository.obtenerAbonoActual(idCliente);
	}
}
