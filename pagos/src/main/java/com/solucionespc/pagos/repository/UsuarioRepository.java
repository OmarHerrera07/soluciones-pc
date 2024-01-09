package com.solucionespc.pagos.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.solucionespc.pagos.entity.Usuario;


public interface UsuarioRepository extends JpaRepository<Usuario, Integer>{
	
	/**
	 * Encuentra un usuario por su nombre de usuario.
	 *
	 * @param username Nombre de usuario para buscar.
	 * @return Objeto Usuario que representa al usuario encontrado.
	 */
    @Query(value = "select *from usuario u where u.username = ?1", nativeQuery = true)
    Usuario finUserByUsername(String username);
    
    
    /**
     * Realiza la paginación de usuarios con opciones de filtrado por nombre.
     *
     * @param nombre   El nombre de usuario a filtrar (ignora mayúsculas y minúsculas).
     * @param pageable Información sobre la paginación.
     * @return Una página de objetos Usuario que cumplen con los criterios de paginación y filtrado.
     */
    @Query(value = "SELECT * FROM usuario u WHERE LOWER(u.nombre) LIKE CONCAT('%', LOWER(?1), '%') order by u.nombre",
            countQuery = "SELECT COUNT(*) FROM usuario u WHERE LOWER(u.nombre) LIKE CONCAT('%', LOWER(?1), '%')",
            nativeQuery = true)
    Page<Usuario> paginacionUsuarioFiltro(String nombre, Pageable pageable);

    
	
}
