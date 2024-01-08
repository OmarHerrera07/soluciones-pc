package com.solucionespc.pagos.dto;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public interface ReporteCliente {
    String getNombre();

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    Date getFechaPago();

    String getColonia();

    Integer getDiasAtraso();

}
