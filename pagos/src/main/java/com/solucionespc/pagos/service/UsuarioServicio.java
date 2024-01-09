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

	/**
	 * Recupera todos los usuarios almacenados en el sistema.
	 *
	 * @return Lista de objetos Usuario.
	 */
	@Override
	public List<Usuario> findAll() {
		return usuarioRepository.findAll();
	}

	/**
	 * Recupera un usuario por su nombre de usuario.
	 *
	 * @param username Nombre de usuario del usuario a recuperar.
	 * @return El objeto Usuario correspondiente al nombre de usuario proporcionado.
	 */
	@Override
	public Usuario finUserByUsername(String username) {
		// TODO Auto-generated method stub
		return usuarioRepository.finUserByUsername(username);
	}

	/**
	 * Registra un nuevo usuario en el sistema utilizando la información proporcionada en un objeto UsuarioRegisterDTO.
	 *
	 * @param userDTO Objeto UsuarioRegisterDTO con la información del nuevo usuario.
	 * @return true si el registro fue exitoso, false en caso contrario.
	 */
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
	
	/**
	 * Actualiza la información de un usuario existente en el sistema utilizando la información proporcionada en un objeto UsuarioRegisterDTO.
	 *
	 * @param userDTO Objeto UsuarioRegisterDTO con la información actualizada del usuario.
	 * @return true si la actualización fue exitosa, false en caso contrario.
	 */
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

	/**
	 * Realiza una paginación de usuarios.
	 *
	 * @param pageable Información sobre la paginación.
	 * @return Una página de objetos Usuario que cumplen con los criterios de paginación.
	 */
	@Override
	public Page<Usuario> paginacionUsuarios(Pageable pageable) {
		// TODO Auto-generated method stub
		return usuarioRepository.findAll(pageable);
	}
	
	/**
	 * Recupera un usuario por su ID.
	 *
	 * @param id El ID del usuario a recuperar.
	 * @return El objeto Usuario correspondiente al ID proporcionado.
	 */
	@Override
	public Usuario findUsuarioById(Integer id) {
		// TODO Auto-generated method stub
		return usuarioRepository.findById(id).get();
	}
	
	/**
	 * Realiza una paginación de usuarios filtrando por nombre.
	 *
	 * @param nombre   El nombre del usuario a filtrar.
	 * @param pageable Información sobre la paginación.
	 * @return Una página de objetos Usuario que cumplen con los criterios de paginación y filtro por nombre.
	 */
	@Override
	public Page<Usuario> paginacionUsuariosFiltro(String nombre,Pageable pageable) {
		// TODO Auto-generated method stub
		return usuarioRepository.paginacionUsuarioFiltro(nombre, pageable);
	}
	
	/**
	 * Recupera un usuario por su ID.
	 *
	 * @param id El ID del usuario a recuperar.
	 * @return El objeto Usuario correspondiente al ID proporcionado.
	 */
	@Override
	public Usuario findById(Integer id) {
		// TODO Auto-generated method stub
		return usuarioRepository.findById(id).get();
	}
	
	/**
	 * Recupera todos los roles almacenados en el sistema.
	 *
	 * @return Lista de objetos Rol.
	 */
	@Override
	public List<Rol> findRoles(){
		return rolRepository.findAll();
	}
}
