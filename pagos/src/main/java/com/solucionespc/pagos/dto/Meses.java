package com.solucionespc.pagos.dto;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public interface Meses {
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	Date getFecha();
	
	String getNombreMes();

}
