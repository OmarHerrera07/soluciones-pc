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
public class PagoService implements IPagoService {

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
	public List<Pago> findAll() {
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
	 * @return Lista de objetos MesesRecibo representando los meses pagados en el
	 *         recibo.
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

	/**
	 * Obtiene la información de corte dinámico para un rango de fechas.
	 *
	 * @param fechaInicio Fecha de inicio del rango.
	 * @param fechaFin    Fecha de fin del rango.
	 * @return Lista de objetos Corte que contiene el nombre del cliente y el total
	 *         del pago.
	 */
	@Override
	public List<Corte> getInfoCorteDinamico(String fechaInico, String fechaFIn) {
		// TODO Auto-generated method stub
		return pagoRepository.getInfoCorteDinamico(fechaInico, fechaFIn);
	}

	/**
	 * Obtiene la información de corte para pagos en efectivo realizados hoy.
	 *
	 * @return Lista de objetos Corte que contiene el nombre del cliente y el total
	 *         del pago.
	 */
	@Override
	public List<Corte> getInfoCorteEfectivo() {
		// TODO Auto-generated method stub
		return pagoRepository.getInfoCorteEfectivo();
	}

	/**
	 * Obtiene la información de corte para pagos por transferencia realizados hoy.
	 *
	 * @return Lista de objetos Corte que contiene el nombre del cliente y el total
	 *         del pago.
	 */
	@Override
	public List<Corte> getInfoCorteTransferencia() {
		// TODO Auto-generated method stub
		return pagoRepository.getInfoCorteTransferencia();
	}

	/**
	 * Obtiene la información de corte dinámico para pagos en efectivo dentro de un
	 * rango de fechas.
	 *
	 * @param fechaInicio Fecha de inicio del rango.
	 * @param fechaFin    Fecha de fin del rango.
	 * @return Lista de objetos Corte que contiene el nombre del cliente y el total
	 *         del pago en efectivo.
	 */
	@Override
	public List<Corte> getInfoCorteDinamicoEfectivo(String fechaInico, String fechaFIn) {
		// TODO Auto-generated method stub
		return pagoRepository.getInfoCorteDinamicoEfectivo(fechaInico, fechaFIn);
	}

	/**
	 * Obtiene la información de corte dinámico para pagos por transferencia dentro
	 * de un rango de fechas.
	 *
	 * @param fechaInicio Fecha de inicio del rango.
	 * @param fechaFin    Fecha de fin del rango.
	 * @return Lista de objetos Corte que contiene el nombre del cliente y el total
	 *         del pago por transferencia.
	 */
	@Override
	public List<Corte> getInfoCorteDinamicoTransferencia(String fechaInico, String fechaFIn) {
		// TODO Auto-generated method stub
		return pagoRepository.getInfoCorteDinamicoTransferencia(fechaInico, fechaFIn);
	}

	/**
	 * Elimina un pago según su ID.
	 *
	 * @param id Identificador del pago a eliminar.
	 * @return {@code true} si la operación de eliminación fue exitosa,
	 *         {@code false} si se produjo una excepción.
	 */
	@Override
	public boolean deletePago(Integer id) {
		try {
			pagoRepository.deleteById(id);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Verifica si hay meses con pago asociado a un pago identificado por su ID.
	 *
	 * @param id Identificador del pago.
	 * @return {@code true} si hay meses con pago asociado, {@code false} si no hay
	 *         meses con pago asociado.
	 */
	@Override
	public boolean mesesConPago(Integer id) {
		Integer numMeses = pagoRepository.mesesConPago(id);
		if (numMeses > 0) {
			return true;
		}
		return false;
	}
}
