package com.solucionespc.pagos.dto;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class MesesDTO {
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date fecha;
	
	private String nombresMes;

}
