package com.solucionespc.pagos.dto;

import java.util.Date;

public interface PagoDTO {
	
	Integer getIdPago();
	
	String getNombre();

	Date getFecha();
	
	Float getTotal();

}
