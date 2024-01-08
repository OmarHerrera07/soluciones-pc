package com.solucionespc.pagos.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.solucionespc.pagos.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.itextpdf.text.DocumentException;
import com.solucionespc.pagos.entity.Cliente;
import com.solucionespc.pagos.entity.Colonia;
import com.solucionespc.pagos.entity.Pago;
import com.solucionespc.pagos.entity.Paquete;
import com.solucionespc.pagos.entity.Rol;
import com.solucionespc.pagos.entity.Usuario;
import com.solucionespc.pagos.repository.ClienteRepository;
import com.solucionespc.pagos.repository.MesesPagoRepositoty;
import com.solucionespc.pagos.repository.PagoRepository;
import com.solucionespc.pagos.repository.UsuarioRepository;
import com.solucionespc.pagos.utils.PDFRecibo;


@Service
public class ClienteService implements IClienteService{
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private PagoRepository pagoRepository;
	
	@Autowired
	private MesesPagoRepositoty mesesPagoRepositoty;
	
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
		cliente.setEstado(true);
		
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

	@Override
	public boolean editarCliente(ClienteRegisterDTO c) {
		Cliente cliente = clienteRepository.findById(c.getIdCliente()).get();
		cliente.setNombre(c.getNombre());
		cliente.setTelefono(c.getTelefono());
		cliente.setCoordenadas(c.getCoordenadas());
		cliente.setFechaPago(c.getFecha());
		cliente.setRfc(c.getRfc());
		cliente.setEstado(c.getEstado());		
		cliente.setPaquete(Paquete.builder().idPaquete(c.getPaquete()).build());
		cliente.setColonia(Colonia.builder().idColonia(c.getIdColonia()).build());
		cliente.setObservaciones(c.getObservaciones());
		try {
			clienteRepository.save(cliente);
			return true;
		} catch (Exception e) {
			return false;
		}		
	}
	
	@Override
	public boolean realizarPago(Integer idCliente,String username) {
		try {
		    clienteRepository.actualizarPagos(idCliente);
		    	
		    System.out.println();
		    Pago pago = pagoRepository.ObtenerPago(idCliente);
		    if(pago != null) {
		    	System.out.println("SI EXISTE");
		    }else {
		    	System.out.println("NO EXISTE");
		    	pago = new Pago();
		    }
		    
		    LocalDate fechaActual = LocalDate.now();
		    pago.setFecha(Date.from(fechaActual.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
		    pago.setIdCliente(Cliente.builder().idCliente(idCliente).build());
		    
		    Usuario user = usuarioRepository.finUserByUsername(username);		    
		    pago.setIdUsuario(user);
		    
		    List<MesesPagoDTO> mesespagados = mesesPagoRepositoty.ObtenerPagosRealizados(idCliente);
		    
	        float sumaPrecios = (float) mesespagados.stream()
	                .map(MesesPagoDTO::getPrecio)
	                .filter(precio -> precio != null) // Filtra los precios que no son nulos
	                .mapToDouble(Float::doubleValue) // Convierte los Float a double para sumar
	                .sum();
	        
	        pago.setTotal(sumaPrecios);
	        
	        pagoRepository.save(pago);
		   		    
		    return true;
		} catch (Exception e) {
			return false;
		}
		
	}

	@Override
	public List<Date> obtenerMesesPagados(Integer idCliente) {
		// TODO Auto-generated method stub
		return mesesPagoRepositoty.obtnerMesesPagados(idCliente);
	}

	@Override
	public List<Date> generarMeses(Integer diaDePago) {
        List<Date> todosLosMeses = new ArrayList();
        int anioActual = LocalDate.now().getYear();

        for (int i = 0; i < 12; i++) {
            // Crear la fecha y agregarla a la lista
            LocalDate fechaLocal = LocalDate.of(anioActual, i + 1, diaDePago);
            Date fecha = java.sql.Date.valueOf(fechaLocal);
            todosLosMeses.add(fecha);
        }
        return todosLosMeses;
	}
	
	@Override
	public List<MesesDTO> generarMeses2(Integer diaDePago, Integer anio) {
	    List<MesesDTO> todosLosMeses = new ArrayList<>();
	    SimpleDateFormat formato = new SimpleDateFormat("MMMM", new Locale("es", "ES"));

	    for (int i = 0; i < 12; i++) {
	        // Crear la fecha y agregarla a la lista
	        LocalDate fechaLocal = LocalDate.of(anio, i + 1, diaDePago);
	        Date fecha = java.sql.Date.valueOf(fechaLocal);

	        MesesDTO mes = new MesesDTO(fecha, formato.format(fecha));
	        todosLosMeses.add(mes);
	    }
	    return todosLosMeses;
	}

    
    @Override
    public List<MesesDTO> generarMesesPorAnio(Integer diaDePago, Integer anio) {
        List<MesesDTO> todosLosMeses = new ArrayList<>();
        SimpleDateFormat formato = new SimpleDateFormat("MMMM", new Locale("es", "ES"));

        for (int i = 0; i < 12; i++) {
            // Crear la fecha y agregarla a la lista
            LocalDate fechaLocal = LocalDate.of(anio, i + 1, diaDePago);
            Date fecha = java.sql.Date.valueOf(fechaLocal);

            MesesDTO mes = new MesesDTO(fecha, formato.format(fecha));
            todosLosMeses.add(mes);
        }
        return todosLosMeses;
    }


	@Override
	public Date obtenerFechaPago(Integer idCliente) {
		// TODO Auto-generated method stub
		return clienteRepository.obtenerFechaPago(idCliente);
	}

	@Override
	public void pagoMasivo(List<String> meses,Cliente cliente, Integer idUsuario) throws DocumentException, IOException {
		Pago pago = new Pago();
		pago.setFecha(new Date());
		pago.setIdCliente(Cliente.builder().idCliente(cliente.getIdCliente()).build());
		pago.setIdUsuario(Usuario.builder().idUsuario(idUsuario).build());
		Pago res = pagoRepository.save(pago);
		
		for(String mes : meses ) {
			System.out.println(mes);
			clienteRepository.InsertarMesPago(cliente.getIdCliente(),cliente.getPaquete().getIdPaquete(),res.getIdPago(), mes);
		}
		InfoRecibo i = pagoRepository.getInfoRecibo(res.getIdPago());
		List<MesesRecibo> mesesR = mesesPagoRepositoty.obtnerMesesPagadosRecibo(cliente.getIdCliente(), res.getIdPago());
		PDFRecibo recibo = new PDFRecibo(i,mesesR);
		byte[] pdfBytes = recibo.getPdfBytes();
		pagoRepository.actualizarRecibo(pdfBytes,res.getIdPago());
	}

	@Override
	public List<Date> obtnerMesesPagadosFiltro(String anio, Integer idCliente) {
		return mesesPagoRepositoty.obtnerMesesPagadosFiltro(anio, idCliente);
	}

	@Override
	public List<ReporteCliente> getReporteClientes() {
		return clienteRepository.getReporteClientes();
	}

}
