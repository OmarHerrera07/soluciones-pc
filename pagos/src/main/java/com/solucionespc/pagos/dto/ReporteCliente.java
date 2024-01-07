package com.solucionespc.pagos.dto;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public interface ReporteCliente {
    String getNombre();

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date getFechaPago();

    String getColonia();

    Integer getDiasRetraso();

}
