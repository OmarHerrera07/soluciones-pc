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
	
	@Override
	public List<Paquete> findAll(){
		return paqueteRepository.findAll();
	}

}
