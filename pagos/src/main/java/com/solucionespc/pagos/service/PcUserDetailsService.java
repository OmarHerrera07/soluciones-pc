package com.solucionespc.pagos.service;

import org.springframework.stereotype.Service;

import com.solucionespc.pagos.config.UserDetailsMapper;
import com.solucionespc.pagos.entity.Usuario;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Service
public class PcUserDetailsService implements UserDetailsService{
	
    private final UsuarioServicio usuarioService;

    private final UserDetailsMapper userDetailsMapper;

    public PcUserDetailsService(UsuarioServicio usuarioService, UserDetailsMapper userDetailsMapper) {
        this.usuarioService = usuarioService;
        this.userDetailsMapper = userDetailsMapper;
    }
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {


        Usuario oUsuario = usuarioService.finUserByUsername(username);
        
        if(oUsuario == null) {
        	throw new UsernameNotFoundException("No se encontró el usuario " + username);
        }
        System.out.println("JPLALALIHFIUSgdyuf");
        String rolAsignado = oUsuario.getRol().getRol();

        return userDetailsMapper.toUserDetails(oUsuario, rolAsignado);
    }

}
