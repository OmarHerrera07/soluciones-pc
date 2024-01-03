package com.solucionespc.pagos.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.solucionespc.pagos.dto.ClienteDTO;
import com.solucionespc.pagos.dto.MesesPagoDTO;
import com.solucionespc.pagos.dto.PagoDTO;
import com.solucionespc.pagos.entity.Pago;
import com.solucionespc.pagos.repository.MesesPagoRepositoty;
import com.solucionespc.pagos.repository.PagoRepository;
import com.solucionespc.pagos.service.IPagoService;

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
    public String pagos(){
        return "pagos";
    }
    
    @GetMapping("/e")
    @ResponseBody
    public List<MesesPagoDTO> get(){
    	System.out.println(mesesPagoRepositoty.ObtenerPagosRealizados(1));
        return mesesPagoRepositoty.ObtenerPagosRealizados(1);
    }
    
    @ResponseBody
    @GetMapping("/paginacion")
    public Page<PagoDTO> paginacion(
    		@RequestParam(name = "nombre", required = false) String nombre,
    		@RequestParam(name = "size", required = false, defaultValue = "5") Integer size,
    		Pageable pageable) {
    	pageable = PageRequest.of(pageable.getPageNumber(), size, pageable.getSort());
        return pagoRepository.paginacionPagos(nombre,pageable);
    }
}
