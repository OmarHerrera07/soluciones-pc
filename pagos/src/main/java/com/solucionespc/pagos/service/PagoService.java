package com.solucionespc.pagos.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.solucionespc.pagos.entity.Pago;
import com.solucionespc.pagos.repository.PagoRepository;

@Service
public class PagoService implements IPagoService{
	
	@Autowired
	private PagoRepository pagoRepository;
	
	@Override
	public List<Pago> findAll(){
		return pagoRepository.findAll();
	}
}
