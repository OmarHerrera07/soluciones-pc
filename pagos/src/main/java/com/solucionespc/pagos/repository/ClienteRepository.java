package com.solucionespc.pagos.repository;

import java.util.Date;
import java.util.List;

import com.solucionespc.pagos.dto.ReporteCliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import com.solucionespc.pagos.dto.ClienteDTO;
import com.solucionespc.pagos.entity.Cliente;


public interface ClienteRepository extends JpaRepository<Cliente, Integer>{
	
    @Query(value = "select c.id_cliente as idCliente, c.nombre,c.telefono,p.precio as paquete,c.fecha_pago as fechaPago,c.ultimo_pago as ultimoPago, c.estado  from cliente c join paquete_internet p ON c.id_paquete = p.id_paquete WHERE LOWER(c.nombre) LIKE CONCAT('%', LOWER(?1), '%') and (c.id_colonia = ?2 or ?2 is null)",
            countQuery = "select COUNT(*) from cliente c join paquete_internet p ON c.id_paquete = p.id_paquete WHERE LOWER(c.nombre) LIKE CONCAT('%', LOWER(?1), '%') and (c.id_colonia = ?2 or ?2 is null)",
            nativeQuery = true)
    Page<ClienteDTO> paginacionCliente(String nombre,Integer idColonia, Pageable pageable);
    
    @Query(value = "select c.nombre,c.telefono, c.fecha_pago as fechaPago,p.precio as paquete from cliente c join paquete_internet p on c.id_paquete = p.id_paquete",
            nativeQuery = true)
    List<ClienteDTO> prueba();
    
    @Query(value = "SELECT c.fecha_pago FROM cliente c WHERE id_cliente = ?1",
            nativeQuery = true)
    Date obtenerFechaPago(Integer idCliente);
    
    @Procedure(name = "actualizarPagos")
    void actualizarPagos(@Param("clienteId") Integer clienteId);
    
    @Procedure(name = "InsertarMesPago")
    void InsertarMesPago(@Param("p_id_cliente") Integer clienteId, @Param("p_id_paquete") Integer idPaquete, @Param("p_id_pago") Integer idPago, @Param("p_fecha") String fecha);

    @Query(value = "SELECT " +
            "c.nombre,c.fecha_pago as fechaPago,c2.colonia," +
            "DATEDIFF(NOW(), fecha_pago) AS diasAtraso " +
            "FROM " +
            "cliente c join colonia c2 on c.id_colonia = c2.id_colonia " +
            "WHERE " +
            "  fecha_pago < CURDATE() and c.estado = 1",
            nativeQuery = true)
    List<ReporteCliente> getReporteClientes();
}
