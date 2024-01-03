package com.solucionespc.pagos.service;

import java.util.List;

import com.solucionespc.pagos.entity.Pago;

public interface IPagoService {
	List<Pago> findAll();
}
