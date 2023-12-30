package com.solucionespc.pagos.dto;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;




public interface ClienteDTO {
	
	Integer getIdCliente();
	
    String getNombre();

    String getTelefono();

    Float getPaquete();
    
    Date getFechaPago();
    
    Date getUltimoPago();
    
}