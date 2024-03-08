package com.solucionespc.pagos.dto;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public interface InfoRecibo {
    String getNombreCliente();

    String getColonia();

    String getRfc();
    
    Integer getTipoPago();

    String getNombreUsuario();
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date getFecha();

    Float getTotal();
    
    Float getAbono();
    
    Integer getTipoTicket();
}
