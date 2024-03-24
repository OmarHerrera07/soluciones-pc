package com.solucionespc.pagos.dto;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MesesDTO {

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date fecha;

	private String nombresMes;

	private boolean pagado;

}
