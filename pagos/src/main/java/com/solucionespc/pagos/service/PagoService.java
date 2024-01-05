package com.solucionespc.pagos.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.solucionespc.pagos.dto.MesesRecibo;
import com.solucionespc.pagos.entity.Pago;
import com.solucionespc.pagos.repository.MesesPagoRepositoty;
import com.solucionespc.pagos.repository.PagoRepository;

@Service
public class PagoService implements IPagoService{
	
	@Autowired
	private PagoRepository pagoRepository;
	
	@Autowired
	private MesesPagoRepositoty mesesPagoRepositoty;
	
	@Override
	public List<Pago> findAll(){
		return pagoRepository.findAll();
	}

	@Override
	public Pago findById(Integer id) {
		// TODO Auto-generated method stub
		return pagoRepository.findById(id).get();
	}

	@Override
	public List<MesesRecibo> obtnerMesesPagadosRecibo(Integer idCliente, Integer idPago) {
		// TODO Auto-generated method stub
		return mesesPagoRepositoty.obtnerMesesPagadosRecibo(idCliente, idPago);
	}
}
