package com.solucionespc.pagos.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.solucionespc.pagos.dto.UsuarioRegisterDTO;
import com.solucionespc.pagos.entity.Usuario;

public interface IUsuarioService {
	
	List<Usuario> findAll();
	
	Usuario finUserByUsername(String username);
	
	boolean registerUser(UsuarioRegisterDTO userDTO );
	
	Page<Usuario> paginacionUsuarios(Pageable pageable);
	
	Usuario findUsuarioById(Integer id);
	
	Page<Usuario> paginacionUsuariosFiltro(String nombre,Pageable pageable);

}
