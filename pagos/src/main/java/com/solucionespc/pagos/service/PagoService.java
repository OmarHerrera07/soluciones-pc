package com.solucionespc.pagos.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.solucionespc.pagos.dto.Corte;
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
	
	/**
	 * Recupera todos los pagos almacenados en el sistema.
	 *
	 * @return Lista de objetos Pago.
	 */
	@Override
	public List<Pago> findAll(){
		return pagoRepository.findAll();
	}
	
	/**
	 * Recupera un objeto Pago por su ID.
	 *
	 * @param id El ID del pago a recuperar.
	 * @return El objeto Pago correspondiente al ID proporcionado.
	 */

	@Override
	public Pago findById(Integer id) {
		// TODO Auto-generated method stub
		return pagoRepository.findById(id).get();
	}
	
	/**
	 * Obtiene la lista de meses pagados para un recibo específico.
	 *
	 * @param idCliente ID del cliente asociado al recibo.
	 * @param idPago    ID del recibo para el cual se obtienen los meses pagados.
	 * @return Lista de objetos MesesRecibo representando los meses pagados en el recibo.
	 */
	@Override
	public List<MesesRecibo> obtnerMesesPagadosRecibo(Integer idCliente, Integer idPago) {
		// TODO Auto-generated method stub
		return mesesPagoRepositoty.obtnerMesesPagadosRecibo(idCliente, idPago);
	}

	/**
	 * Obtiene información de corte relacionada con los pagos.
	 *
	 * @return Lista de objetos Corte que representan la información de corte.
	 */
	@Override
	public List<Corte> getInfoCorte() {
		// TODO Auto-generated method stub
		return pagoRepository.getInfoCorte();
	}

	@Override
	public List<Corte> getInfoCorteDinamico(String fechaInico, String fechaFIn) {
		// TODO Auto-generated method stub
		return pagoRepository.getInfoCorteDinamico(fechaInico, fechaFIn);
	}

	@Override
	public List<Corte> getInfoCorteEfectivo() {
		// TODO Auto-generated method stub
		return pagoRepository.getInfoCorteEfectivo();
	}

	@Override
	public List<Corte> getInfoCorteTransferencia() {
		// TODO Auto-generated method stub
		return pagoRepository.getInfoCorteTransferencia();
	}

	@Override
	public List<Corte> getInfoCorteDinamicoEfectivo(String fechaInico, String fechaFIn) {
		// TODO Auto-generated method stub
		return pagoRepository.getInfoCorteDinamicoEfectivo(fechaInico, fechaFIn);
	}

	@Override
	public List<Corte> getInfoCorteDinamicoTransferencia(String fechaInico, String fechaFIn) {
		// TODO Auto-generated method stub
		return pagoRepository.getInfoCorteDinamicoTransferencia(fechaInico, fechaFIn);
	}
}
