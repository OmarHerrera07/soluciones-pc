package com.solucionespc.pagos.config;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.solucionespc.pagos.entity.Usuario;

@Component
public class UserDetailsMapper {
    public UserDetails toUserDetails(Usuario usuario, String rol) {
    	
    	System.out.println(usuario.getPassword());
        // TODO: Manejar roles, ahora mismo mapea todos los usuarios con el rol de docentes hardcodeado
        return User.withUsername(usuario.getUsername())
                .password(usuario.getPassword())
                .roles(rol.toUpperCase())
                .build();
    }
}
