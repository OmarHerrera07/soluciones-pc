package com.solucionespc.pagos.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.solucionespc.pagos.dto.ClienteDTO;
import com.solucionespc.pagos.entity.Cliente;


public interface ClienteRepository extends JpaRepository<Cliente, Integer>{
	
    @Query(value = "select c.id_cliente as idCliente, c.nombre,c.telefono,p.precio as paquete,c.fecha_pago as fechaPago,c.ultimo_pago as ultimoPago  from cliente c join paquete_internet p ON c.id_paquete = p.id_paquete WHERE LOWER(c.nombre) LIKE CONCAT('%', LOWER(?1), '%')",
            countQuery = "select COUNT(*) from cliente c join paquete_internet p ON c.id_paquete = p.id_paquete WHERE LOWER(c.nombre) LIKE CONCAT('%', LOWER(?1), '%')",
            nativeQuery = true)
    Page<ClienteDTO> paginacionCliente(String nombre, Pageable pageable);
    
    @Query(value = "select c.nombre,c.telefono, c.fecha_pago as fechaPago,p.precio as paquete from cliente c join paquete_internet p on c.id_paquete = p.id_paquete",
            nativeQuery = true)
    List<ClienteDTO> prueba();

}
