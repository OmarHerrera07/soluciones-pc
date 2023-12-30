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
	
	public List<Colonia> findAll(){
		return coloniaRepository.findAll();
	}

}
