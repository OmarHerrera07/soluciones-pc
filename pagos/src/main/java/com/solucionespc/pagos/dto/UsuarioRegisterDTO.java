package com.solucionespc.pagos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UsuarioRegisterDTO {
	
	private Integer idUsuario;
    
    private String username;
    
    private String password;
    
    private String nombre;
    
    private Integer idRol;
}
