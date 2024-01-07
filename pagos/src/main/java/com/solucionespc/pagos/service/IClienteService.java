package com.solucionespc.pagos.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.itextpdf.text.DocumentException;
import com.solucionespc.pagos.dto.ClienteDTO;
import com.solucionespc.pagos.dto.ClienteRegisterDTO;
import com.solucionespc.pagos.dto.Meses;
import com.solucionespc.pagos.dto.MesesDTO;
import com.solucionespc.pagos.entity.Cliente;

public interface IClienteService{
	
	List<Cliente> findAll();
	
	Page<ClienteDTO> paginacionCliente(String nombre,Pageable pageable);
	
	List<ClienteDTO> prueba();
	
	boolean registrarCliente(ClienteRegisterDTO c);
	
	boolean editarCliente(ClienteRegisterDTO c);
	
	Cliente finById(Integer id);
	
	boolean realizarPago(Integer idCliente,String username);
	
	List<Date> obtenerMesesPagados(Integer idCliente);
	
	List<Date> generarMeses(Integer dia);
	
	List<MesesDTO> generarMeses2(Integer diaDePago);
	
	List<MesesDTO> generarMesesPorAnio(Integer diaDePago, Integer anio);
	
	Date obtenerFechaPago(Integer idCliente);
	
	void pagoMasivo(List<String> meses,Cliente cliente,Integer idUsuario) throws IOException, DocumentException;
	
	List<Date> obtnerMesesPagadosFiltro(String anio,Integer idCliente);

}
