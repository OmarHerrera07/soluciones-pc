package com.solucionespc.pagos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UsuarioRegisterDTO {
    
    private String username;
    
    private String password;
    
    private String apellidoPaterno;
    
    private String apellidoMaterno;
    
    private Integer idRol;
}
