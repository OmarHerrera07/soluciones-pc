package com.solucionespc.pagos.dto;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public interface MesesRecibo {
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	Date getFecha();
	
	Float getPrecio();
}
