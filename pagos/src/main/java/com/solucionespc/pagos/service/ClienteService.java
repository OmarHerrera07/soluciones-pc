package com.solucionespc.pagos.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.solucionespc.pagos.dto.ClienteDTO;
import com.solucionespc.pagos.dto.ClienteRegisterDTO;
import com.solucionespc.pagos.entity.Cliente;
import com.solucionespc.pagos.entity.Colonia;
import com.solucionespc.pagos.entity.Paquete;
import com.solucionespc.pagos.entity.Rol;
import com.solucionespc.pagos.repository.ClienteRepository;


@Service
public class ClienteService implements IClienteService{
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Override
	public List<Cliente> findAll(){
		return clienteRepository.findAll();
	}
	
	@Override
	public Page<ClienteDTO> paginacionCliente(String nombre,Pageable pageable){
		return clienteRepository.paginacionCliente(nombre,pageable);
	}
	
	@Override
	public List<ClienteDTO> prueba(){
		return clienteRepository.prueba();
	}

	@Override
	public boolean registrarCliente(ClienteRegisterDTO c) {
		Cliente cliente = new Cliente();
		cliente.setNombre(c.getNombre());
		cliente.setTelefono(c.getTelefono());
		cliente.setCoordenadas(c.getCoordenadas());
		cliente.setFechaPago(c.getFecha());
		cliente.setRfc(c.getRfc());
		cliente.setEstado(1);
		
		cliente.setPaquete(Paquete.builder().idPaquete(c.getPaquete()).build());
		cliente.setColonia(Colonia.builder().idColonia(c.getIdColonia()).build());
		
		cliente.setFechaAlerta(c.getFecha());
		cliente.setDiasAtraso(0);
		cliente.setUltimoPago(c.getFecha());	
		try {
			clienteRepository.save(cliente);
			return true;
		} catch (Exception e) {
			return false;
		}		
	}

	@Override
	public Cliente finById(Integer id) {
		// TODO Auto-generated method stub
		return clienteRepository.findById(id).get();
	}

}
