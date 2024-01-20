package com.solucionespc.pagos.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.solucionespc.pagos.entity.Colonia;
import com.solucionespc.pagos.repository.ColoniaRepository;

@Service
public class ColoniaService implements IColoniaService{
	
	@Autowired
	private ColoniaRepository coloniaRepository;
	
	/**
	 * Recupera todas las colonias almacenadas en el sistema.
	 *
	 * @return Lista de objetos Colonia.
	 */
	public List<Colonia> findAll(){
		return coloniaRepository.findAll();
	}

	/**
	 * Guarda una instancia de la entidad Colonia en el repositorio.
	 *
	 * @param colonia La instancia de la entidad Colonia que se va a guardar.
	 * @return {@code true} si la operación de guardado fue exitosa, {@code false} si se produjo una excepción.
	 */
	@Override
	public boolean save(Colonia colonia) {
		try {		
			coloniaRepository.save(colonia);
			return true;
		}catch (Exception e) {
			return false;
		}
		
	}

}
