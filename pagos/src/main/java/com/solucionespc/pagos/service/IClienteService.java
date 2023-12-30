package com.solucionespc.pagos.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.solucionespc.pagos.dto.ClienteDTO;
import com.solucionespc.pagos.dto.ClienteRegisterDTO;
import com.solucionespc.pagos.entity.Cliente;

public interface IClienteService{
	
	List<Cliente> findAll();
	
	Page<ClienteDTO> paginacionCliente(String nombre,Pageable pageable);
	
	List<ClienteDTO> prueba();
	
	boolean registrarCliente(ClienteRegisterDTO c);
	
	boolean editarCliente(ClienteRegisterDTO c);
	
	Cliente finById(Integer id);

}
