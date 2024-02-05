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

    
    /**
     * Constructor de la clase PcUserDetailsService.
     *
     * @param usuarioService   Servicio que proporciona operaciones relacionadas con los usuarios.
     * @param userDetailsMapper Mapper utilizado para convertir los detalles del usuario a UserDetails.
     */
    public PcUserDetailsService(UsuarioServicio usuarioService, UserDetailsMapper userDetailsMapper) {
        this.usuarioService = usuarioService;
        this.userDetailsMapper = userDetailsMapper;
    }
    
    /**
     * Carga los detalles del usuario por nombre de usuario durante el proceso de autenticación.
     *
     * @param username Nombre de usuario para el cual se cargan los detalles.
     * @return UserDetails que representa los detalles del usuario.
     * @throws UsernameNotFoundException Se lanza si no se encuentra el usuario o si el usuario está desactivado.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Usuario oUsuario = usuarioService.finUserByUsername(username);
        
        if(oUsuario == null || !oUsuario.getEstado()) {
        	throw new UsernameNotFoundException("No se encontró el usuario " + username);
        }

        String rolAsignado = oUsuario.getRol().getRol();

        return userDetailsMapper.toUserDetails(oUsuario, rolAsignado);
    }

}
