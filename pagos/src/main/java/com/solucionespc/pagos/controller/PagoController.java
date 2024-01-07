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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itextpdf.text.DocumentException;
import com.solucionespc.pagos.dto.ClienteDTO;
import com.solucionespc.pagos.dto.MesesPagoDTO;
import com.solucionespc.pagos.dto.MesesRecibo;
import com.solucionespc.pagos.dto.PagoDTO;
import com.solucionespc.pagos.entity.Pago;
import com.solucionespc.pagos.repository.MesesPagoRepositoty;
import com.solucionespc.pagos.repository.PagoRepository;
import com.solucionespc.pagos.service.IPagoService;
import com.solucionespc.pagos.utils.PDFExporter;
import com.solucionespc.pagos.utils.PDFReciboExporter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/pagos")
public class PagoController {

	@Autowired
	private IPagoService pagoService;

	@Autowired
	private MesesPagoRepositoty mesesPagoRepositoty;

	@Autowired
	private PagoRepository pagoRepository;

	@GetMapping
	public String pagos() {
		return "pagos";
	}

	@GetMapping("/e")
	@ResponseBody
	public List<MesesRecibo> get() {
		System.out.println(mesesPagoRepositoty.ObtenerPagosRealizados(1));
		return mesesPagoRepositoty.obtnerMesesPagadosRecibo(1, 59);
	}

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
	
	@GetMapping("/reciboCliente")
	@ResponseBody
	public void recibo(@RequestParam(value="id") Integer id,HttpServletRequest request, HttpServletResponse response)
			throws DocumentException, IOException {
		
		Pago pago = pagoService.findById(id);
		List<MesesRecibo> meses = pagoService.obtnerMesesPagadosRecibo(pago.getIdCliente().getIdCliente(), id);
		
		response.setContentType("application/pdf");

		String headerKey = "Content-Disposition";
		String sbHeaderValue = "attachment; filename=Recibo.pdf";
		response.setHeader(headerKey, sbHeaderValue);
		// System.out.println(solicitud.get());
		PDFReciboExporter export = new PDFReciboExporter(pago,meses);
		export.export(response);
	}
	
	@GetMapping("/descargarRecibo")
	public ResponseEntity<byte[]> descargarReporteFinal(@RequestParam(value="id") Integer id) {
		Pago pago = pagoService.findById(id);
	    byte[] bytes = pago.getRecibo();
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_PDF);
	    headers.setContentDisposition(ContentDisposition.builder("attachment").filename("Reporte Final.pdf").build());
	    return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
	}
}
