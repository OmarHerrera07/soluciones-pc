package com.solucionespc.pagos.service;

import java.util.List;

import com.solucionespc.pagos.dto.MesesRecibo;
import com.solucionespc.pagos.entity.Pago;

public interface IPagoService {
	List<Pago> findAll();
	
	Pago findById(Integer id);
	
	List<MesesRecibo> obtnerMesesPagadosRecibo(Integer idCliente,Integer idPago);
}
