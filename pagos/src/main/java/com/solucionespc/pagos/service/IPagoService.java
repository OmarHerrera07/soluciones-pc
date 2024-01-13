package com.solucionespc.pagos.service;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.solucionespc.pagos.dto.Corte;
import com.solucionespc.pagos.dto.MesesRecibo;
import com.solucionespc.pagos.entity.Pago;

public interface IPagoService {
	List<Pago> findAll();
	
	Pago findById(Integer id);
	
	List<MesesRecibo> obtnerMesesPagadosRecibo(Integer idCliente,Integer idPago);
	
	List<Corte> getInfoCorte();
	
	List<Corte> getInfoCorteDinamico(String fechaInico, String fechaFIn);
	
    List<Corte> getInfoCorteEfectivo();

    List<Corte> getInfoCorteTransferencia();
    
    List<Corte> getInfoCorteDinamicoEfectivo(String fechaInico, String fechaFIn);
    
    List<Corte> getInfoCorteDinamicoTransferencia(String fechaInico, String fechaFIn);
}
