package com.solucionespc.pagos.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.solucionespc.pagos.entity.Usuario;


public interface UsuarioRepository extends JpaRepository<Usuario, Integer>{
	
    @Query(value = "select *from usuario u where u.username = ?1", nativeQuery = true)
    Usuario finUserByUsername(String username);
    
    @Query(value = "SELECT * FROM usuario u WHERE LOWER(u.username) LIKE CONCAT('%', LOWER(?1), '%')",
            countQuery = "SELECT COUNT(*) FROM usuario u WHERE LOWER(u.username) LIKE CONCAT('%', LOWER(?1), '%')",
            nativeQuery = true)
    Page<Usuario> paginacionUsuarioFiltro(String nombre, Pageable pageable);

    
	
}
