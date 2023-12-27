package com.solucionespc.pagos.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.solucionespc.pagos.dto.UsuarioRegisterDTO;
import com.solucionespc.pagos.entity.Rol;
import com.solucionespc.pagos.entity.Usuario;
import com.solucionespc.pagos.repository.UsuarioRepository;

@Service
public class UsuarioServicio implements IUsuarioService{
	@Autowired
	private UsuarioRepository usuarioRepository;

	@Override
	public List<Usuario> findAll() {
		return usuarioRepository.findAll();
	}

	@Override
	public Usuario finUserByUsername(String username) {
		// TODO Auto-generated method stub
		return usuarioRepository.finUserByUsername(username);
	}

	@Override
	public boolean registerUser(UsuarioRegisterDTO userDTO) {
		// TODO Auto-generated method stub
		Usuario user = new Usuario();
		user.setUsername(userDTO.getUsername());
		user.setApellidoPaterno(userDTO.getApellidoPaterno());
		user.setApellidoMaterno(userDTO.getApellidoMaterno());
		user.setPassword(userDTO.getPassword());
		user.setRol(Rol.builder().idRol(userDTO.getIdRol()).build());

		try {
			usuarioRepository.save(user);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
