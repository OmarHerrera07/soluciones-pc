package com.solucionespc.pagos.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import com.solucionespc.pagos.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import com.itextpdf.text.DocumentException;
import com.solucionespc.pagos.entity.Cliente;

public interface IClienteService{
	
	List<Cliente> findAll();
	
	Page<ClienteDTO> paginacionCliente(String nombre,Integer idColonia,Pageable pageable);
	
	List<ClienteDTO> prueba();
	
	boolean registrarCliente(ClienteRegisterDTO c);
	
	boolean editarCliente(ClienteRegisterDTO c);
	
	Cliente finById(Integer id);
	
	boolean realizarPago(Integer idCliente,String username);
	
	List<Date> obtenerMesesPagados(Integer idCliente);
	
	List<Date> generarMeses(Integer dia);
	
	List<MesesDTO> generarMeses2(Integer diaDePago, Integer anio);
	
	List<MesesDTO> generarMesesPorAnio(Integer diaDePago, Integer anio);
	
	Date obtenerFechaPago(Integer idCliente);
	
	void pagoMasivo(List<String> meses,Cliente cliente,Integer idUsuario,Integer tipoPago) throws DocumentException, IOException;
	
	List<Date> obtnerMesesPagadosFiltro(String anio,Integer idCliente);

	List<ReporteCliente> getReporteClientes();
	
	Integer cancelarPago(Integer idCliente, String fecha) throws IOException, DocumentException;
	
	boolean deteleCliente(Integer idCliente);
	
	boolean totalMesesPagados(Integer idCliente);
	
	boolean abonoCliente(Integer idCliente, Float abono,Integer tipoPago) throws IOException, DocumentException;
	
	List<Date> obtenerMesesPagadosPorAnio(Integer idCliente, Integer anio);
	
	Float obtenerAbonoActual(Integer idCliente);
	
	boolean findClienteByNombre(String nombre);

}
