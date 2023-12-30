package com.solucionespc.pagos.dto;

import java.time.LocalTime;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ClienteRegisterDTO {
	
	private Integer idCliente;

	private String nombre;

	private String telefono;

	private String observaciones;

	private String rfc;

	private Integer paquete;

	private Integer idColonia;

	private String coordenadas;
	
	private Boolean estado;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date fecha;
}
