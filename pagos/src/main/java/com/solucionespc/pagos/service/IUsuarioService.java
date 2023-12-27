package com.solucionespc.pagos.service;

import java.util.List;
import java.util.Optional;

import com.solucionespc.pagos.dto.UsuarioRegisterDTO;
import com.solucionespc.pagos.entity.Usuario;

public interface IUsuarioService {
	
	List<Usuario> findAll();
	
	Usuario finUserByUsername(String username);
	
	boolean registerUser(UsuarioRegisterDTO userDTO );

}
