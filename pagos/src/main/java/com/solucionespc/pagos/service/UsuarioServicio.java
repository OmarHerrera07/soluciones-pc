package com.solucionespc.pagos.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.solucionespc.pagos.dto.UsuarioRegisterDTO;
import com.solucionespc.pagos.entity.Rol;
import com.solucionespc.pagos.entity.Usuario;
import com.solucionespc.pagos.repository.RolRepository;
import com.solucionespc.pagos.repository.UsuarioRepository;

@Service
public class UsuarioServicio implements IUsuarioService{
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private RolRepository rolRepository;

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
		user.setNombre(userDTO.getNombre());
		user.setPassword(userDTO.getPassword());
		user.setRol(Rol.builder().idRol(userDTO.getIdRol()).build());
		user.setEstado(true);

		try {
			usuarioRepository.save(user);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	@Override
	public boolean updateUser(UsuarioRegisterDTO userDTO) {
		// TODO Auto-generated method stub
		Usuario user = new Usuario();
		user.setPassword(userDTO.getPassword());
		user.setIdUsuario(userDTO.getIdUsuario());
		user.setUsername(userDTO.getUsername());
		user.setNombre(userDTO.getNombre());
		user.setRol(Rol.builder().idRol(userDTO.getIdRol()).build());
		user.setEstado(userDTO.getEstado());
		try {
			usuarioRepository.save(user);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public Page<Usuario> paginacionUsuarios(Pageable pageable) {
		// TODO Auto-generated method stub
		return usuarioRepository.findAll(pageable);
	}
	
	@Override
	public Usuario findUsuarioById(Integer id) {
		// TODO Auto-generated method stub
		return usuarioRepository.findById(id).get();
	}
	
	@Override
	public Page<Usuario> paginacionUsuariosFiltro(String nombre,Pageable pageable) {
		// TODO Auto-generated method stub
		return usuarioRepository.paginacionUsuarioFiltro(nombre, pageable);
	}
	
	@Override
	public Usuario findById(Integer id) {
		// TODO Auto-generated method stub
		return usuarioRepository.findById(id).get();
	}
	
	@Override
	public List<Rol> findRoles(){
		return rolRepository.findAll();
	}
}
