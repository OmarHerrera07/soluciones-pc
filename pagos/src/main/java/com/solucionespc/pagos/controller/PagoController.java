package com.solucionespc.pagos.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.itextpdf.text.DocumentException;
import com.solucionespc.pagos.dto.ClienteDTO;
import com.solucionespc.pagos.dto.Corte;
import com.solucionespc.pagos.dto.MesesPagoDTO;
import com.solucionespc.pagos.dto.MesesRecibo;
import com.solucionespc.pagos.dto.PagoDTO;
import com.solucionespc.pagos.dto.ReporteCliente;
import com.solucionespc.pagos.entity.Pago;
import com.solucionespc.pagos.entity.Usuario;
import com.solucionespc.pagos.repository.MesesPagoRepositoty;
import com.solucionespc.pagos.repository.PagoRepository;
import com.solucionespc.pagos.service.IPagoService;
import com.solucionespc.pagos.service.IUsuarioService;
import com.solucionespc.pagos.utils.PDFCorte;
import com.solucionespc.pagos.utils.PDFCorteExporter;
import com.solucionespc.pagos.utils.PDFExporter;
import com.solucionespc.pagos.utils.PDFReciboExporter;
import com.solucionespc.pagos.utils.PDFReporteClientes;

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxTrigger;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/pagos")
public class PagoController {

	@Autowired
	private IPagoService pagoService;

	@Autowired
	private PagoRepository pagoRepository;
	
	@Autowired
	private IUsuarioService usuarioService;

	/**
	 * Redirege a la vista de pagos
	 * @return	vista para pagos (archivo html)
	 */
	@GetMapping
	public String pagos() {
		return "pagos";
	}

	@GetMapping("/e")
	@ResponseBody
	public List<Corte> get() {
		return pagoService.getInfoCorte();
	}
	
	/**
	 * Obtiene una lista paginada de todos los pagos realizados
	 * @param nombre		filtro por nombre del cliente
	 * @param size			tamaño del listado de pagos
	 * @param fechaInicio	filtro de rango fecha para la paginación
	 * @param fechaFin		filtro de rango fecha para la paginación
	 * @param pageable		clase para la paginación
	 * @return				lista paginada con los pagos realizados
	 */
	@ResponseBody
	@GetMapping("/paginacion")
	public Page<PagoDTO> paginacion(@RequestParam(name = "nombre", required = false) String nombre,
			@RequestParam(name = "size", required = false, defaultValue = "5") Integer size,
			@RequestParam(name = "fechaInicio", required = false) String fechaInicio,
			@RequestParam(name = "fechaFin", required = false) String fechaFin, Pageable pageable) {

		System.out.println(fechaInicio);
		pageable = PageRequest.of(pageable.getPageNumber(), size, pageable.getSort());
		return pagoRepository.paginacionPagos(nombre, fechaInicio, fechaFin, pageable);
	}
	
	/**
	 * 
	 * @param request				representa la solicitud HTTP que llega al servidor. Proporciona información sobre la solicitud web, como los parámetros de la URL, encabezados, tipo de solicitud, etc.
	 * @param response				representa la respuesta HTTP que el servidor enviará de vuelta al cliente				
	 * @throws DocumentException	indica que el método puede arrojar una excepción del tipo DocumentException
	 * @throws IOException			indica que el método puede arrojar una excepción del tipo IOException
	 */

	@GetMapping("/recibo")
	@ResponseBody
	public void exportPDF(HttpServletRequest request, HttpServletResponse response)
			throws DocumentException, IOException {
		response.setContentType("application/pdf");

		String headerKey = "Content-Disposition";
		String sbHeaderValue = "attachment; filename=Recibo.pdf";
		response.setHeader(headerKey, sbHeaderValue);
		// System.out.println(solicitud.get());
		PDFExporter export = new PDFExporter(null);
		export.export(response);
	}
	
	/**
	 * 
	 * @param request				representa la solicitud HTTP que llega al servidor. Proporciona información sobre la solicitud web, como los parámetros de la URL, encabezados, tipo de solicitud, etc.
	 * @param response				representa la respuesta HTTP que el servidor enviará de vuelta al cliente				
	 * @throws DocumentException	indica que el método puede arrojar una excepción del tipo DocumentException
	 * @throws IOException			indica que el método puede arrojar una excepción del tipo IOException
	 */
	
	@GetMapping("/reciboCliente")
	@ResponseBody
	public void recibo(@RequestParam(value="id") Integer id,HttpServletRequest request, HttpServletResponse response)
			throws DocumentException, IOException {
		
		Pago pago = pagoService.findById(id);
		List<MesesRecibo> meses = pagoService.obtnerMesesPagadosRecibo(pago.getIdCliente().getIdCliente(), id);		
		response.setContentType("application/pdf");
		String headerKey = "Content-Disposition";
		String sbHeaderValue = "inline; filename=Recibo.pdf";
		response.setHeader(headerKey, sbHeaderValue);
		// System.out.println(solicitud.get());
		PDFReciboExporter export = new PDFReciboExporter(pago,meses);
		export.export(response);
	}
	
	/**
	 * Descarga el recibo de pago del cliente en formato pdf
	 * @param id	id del recibo a descargar
	 * @return		descarga el recibo en formato pdf
	 */
    @GetMapping("/generar-recibo")
    public ResponseEntity<byte[]> descargarReporteFinal(@RequestParam(value = "id") Integer id) {
        Pago pago = pagoService.findById(id);
        byte[] bytes = pago.getRecibo();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.builder("inline").filename("Reporte Final.pdf").build());
        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }
	
	
	/**
	 * Genera un pdf con información de corte de caja
	 * @param request				representa la solicitud HTTP que llega al servidor. Proporciona información sobre la solicitud web, como los parámetros de la URL, encabezados, tipo de solicitud, etc.
	 * @param response				representa la respuesta HTTP que el servidor enviará de vuelta al cliente				
	 * @param authentication		representa la información de autenticación del usuario que realiza la solicitud
	 * @throws DocumentException	indica que el método puede arrojar una excepción del tipo DocumentException
	 * @throws IOException			indica que el método puede arrojar una excepción del tipo IOException
	 */
    @GetMapping("/corte")
    @ResponseBody
    public void exportPDFCorte(HttpServletRequest request, HttpServletResponse response,Authentication authentication) throws DocumentException, IOException {
        response.setContentType("application/pdf");
        
//        String headerKey = "Content-Disposition";
//        String sbHeaderValue = "inline; filename=Reporte.pdf";
//        response.setHeader(headerKey, sbHeaderValue);
//        List<Corte> infoCorte = pagoService.getInfoCorte();
//        Usuario user = usuarioService.finUserByUsername(authentication.getName());
//        PDFCorte reporte = new PDFCorte(infoCorte,user);
        String headerKey = "Content-Disposition";
        String sbHeaderValue = "inline; filename=Reporte.pdf";
        response.setHeader(headerKey, sbHeaderValue);
        List<Corte> infoCorteEfectivo = pagoService.getInfoCorteEfectivo();
        List<Corte> infoCorteTransferencia = pagoService.getInfoCorteTransferencia();
        Usuario user = usuarioService.finUserByUsername(authentication.getName());
        PDFCorteExporter reporte = new PDFCorteExporter(infoCorteEfectivo,infoCorteTransferencia,user);
        reporte.export(response);

    }
    
    @GetMapping("/corte-dinamico")
    @ResponseBody
    public void exportPDFCorteDinamico(HttpServletRequest request, HttpServletResponse response,Authentication authentication,
    		@RequestParam(name = "fechaInicio", required = false) String fechaInicio,
			@RequestParam(name = "fechaFin", required = false) String fechaFin
    		) throws DocumentException, IOException {
        response.setContentType("application/pdf");

        String headerKey = "Content-Disposition";
        String sbHeaderValue = "inline; filename=Reporte.pdf";
        response.setHeader(headerKey, sbHeaderValue);
        List<Corte> infoCorteEfectivo = pagoService.getInfoCorteDinamicoEfectivo(fechaInicio, fechaFin);
        List<Corte> infoCorteTransferencia = pagoService.getInfoCorteDinamicoTransferencia(fechaInicio, fechaFin);
        Usuario user = usuarioService.finUserByUsername(authentication.getName());
        PDFCorte reporte = new PDFCorte(infoCorteEfectivo,infoCorteTransferencia,user,fechaInicio,fechaFin);
        reporte.export(response);

    }
    
    @GetMapping("/cortePrueba")
    @ResponseBody
    public void exportPDFCorteNew(HttpServletRequest request, HttpServletResponse response,Authentication authentication) throws DocumentException, IOException {
        response.setContentType("application/pdf");

        String headerKey = "Content-Disposition";
        String sbHeaderValue = "inline; filename=Reporte.pdf";
        response.setHeader(headerKey, sbHeaderValue);
        List<Corte> infoCorteEfectivo = pagoService.getInfoCorteEfectivo();
        List<Corte> infoCorteTransferencia = pagoService.getInfoCorteTransferencia();
        Usuario user = usuarioService.finUserByUsername(authentication.getName());
        PDFCorteExporter reporte = new PDFCorteExporter(infoCorteEfectivo,infoCorteTransferencia,user);
        reporte.export(response);

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
	public String eliminarCliente(@RequestParam(value = "idPagoEliminar") Integer id) {
		
		boolean totalMeses = pagoService.mesesConPago(id);
		
		if(totalMeses) {
			return "<div id=\"result\" data-notify=\"3\" hidden>No se puede eliminar un pago con meses registrados</div>";
		}
		boolean res = pagoService.deletePago(id);
		if (res) {
			return "<div id=\"result\" data-notify=\"1\" hidden>Se ha eliminado el pago</div>";
		}
		return "<div id=\"result\" data-notify=\"2\" hidden>Ha ocurrido un error al eliminar el pago</div>";
	}

}
