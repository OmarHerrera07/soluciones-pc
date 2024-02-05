package com.solucionespc.pagos.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.solucionespc.pagos.entity.Paquete;
import com.solucionespc.pagos.repository.PaqueteRepository;



@Service
public class PaqueteService implements IPaqueteService{
	@Autowired
	private PaqueteRepository paqueteRepository;
	
	/**
	 * Recupera todos los paquetes (Paquetes de internet) almacenados en el sistema.
	 *
	 * @return Lista de objetos Paquete.
	 */
	@Override
	public List<Paquete> findAll(){
		return paqueteRepository.findAll();
	}

	/**
	 * Busca y recupera un paquete (Paquete de internet) por su identificador.
	 *
	 * @param idPaquete Identificador del paquete a buscar.
	 * @return El objeto Paquete correspondiente al identificador proporcionado.
	 * @throws NoSuchElementException Si no se encuentra ningún paquete con el identificador dado.
	 */
	@Override
	public Paquete findbyId(Integer idPaquete) {
		// TODO Auto-generated method stub
		return paqueteRepository.findById(idPaquete).get();
	}

}
