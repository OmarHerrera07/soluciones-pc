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

import jakarta.transaction.Transactional;


public interface ClienteRepository extends JpaRepository<Cliente, Integer>{
	
	/**
	 * Realiza una paginación de clientes con opciones de filtrado por nombre y colonia.
	 *
	 * @param nombre     El nombre del cliente a filtrar (ignora mayúsculas y minúsculas).
	 * @param idColonia  El ID de la colonia para filtrar clientes o null para ignorar este filtro.
	 * @param pageable   Información sobre la paginación.
	 * @return Una página de objetos ClienteDTO que cumplen con los criterios de paginación y filtrado.
	 */
    @Query(value = "select c.id_cliente as idCliente, c.nombre,c.telefono,p.precio as paquete,c.fecha_pago as fechaPago,c.ultimo_pago as ultimoPago, c.estado  from cliente c join paquete_internet p ON c.id_paquete = p.id_paquete WHERE LOWER(c.nombre) LIKE CONCAT('%', LOWER(?1), '%') and (c.id_colonia = ?2 or ?2 is null) order by c.nombre",
            countQuery = "select COUNT(*) from cliente c join paquete_internet p ON c.id_paquete = p.id_paquete WHERE LOWER(c.nombre) LIKE CONCAT('%', LOWER(?1), '%') and (c.id_colonia = ?2 or ?2 is null)",
            nativeQuery = true)
    Page<ClienteDTO> paginacionCliente(String nombre,Integer idColonia, Pageable pageable);
    
    /**
     * Realiza una consulta de prueba para recuperar información de clientes y paquetes de Internet.
     *
     * @return Lista de objetos ClienteDTO que representan la información de clientes y paquetes.
     */
    @Query(value = "select c.nombre,c.telefono, c.fecha_pago as fechaPago,p.precio as paquete from cliente c join paquete_internet p on c.id_paquete = p.id_paquete",
            nativeQuery = true)
    List<ClienteDTO> prueba();
    
    
    /**
     * Obtiene la fecha de pago para un cliente específico.
     *
     * @param idCliente ID del cliente para el cual se obtiene la fecha de pago.
     * @return La fecha de pago del cliente.
     */
    @Query(value = "SELECT c.fecha_pago FROM cliente c WHERE id_cliente = ?1",
            nativeQuery = true)
    Date obtenerFechaPago(Integer idCliente);
    
    
    /**
     * Procedimiento almacenado que actualiza los pagos para un cliente específico.
     *
     * @param clienteId ID del cliente para el cual se actualizan los pagos.
     */
    @Procedure(name = "actualizarPagos")
    void actualizarPagos(@Param("clienteId") Integer clienteId);
    
    /**
     * Procedimiento almacenado que inserta un mes de pago para un cliente específico.
     *
     * @param clienteId ID del cliente para el cual se inserta el mes de pago.
     * @param idPaquete ID del paquete de Internet asociado al cliente.
     * @param idPago    ID del pago asociado al mes de pago.
     * @param fecha     Fecha del mes de pago en formato string.
     */
    @Procedure(name = "InsertarMesPago")
    void InsertarMesPago(@Param("p_id_cliente") Integer clienteId, @Param("p_id_paquete") Integer idPaquete, @Param("p_id_pago") Integer idPago, @Param("p_fecha") String fecha);

    /**
     * Obtiene un informe de clientes con pagos atrasados.
     *
     * @return Lista de objetos ReporteCliente que representan el informe de clientes.
     */
    @Query(value = "SELECT " +
            "c.nombre,c.fecha_pago as fechaPago,c2.colonia," +
            "DATEDIFF(NOW(), fecha_pago) AS diasAtraso " +
            "FROM " +
            "cliente c join colonia c2 on c.id_colonia = c2.id_colonia " +
            "WHERE " +
            "  fecha_pago < CURDATE() and c.estado = 1",
            nativeQuery = true)
    List<ReporteCliente> getReporteClientes();
    
    
    @Procedure(name = "cancelarPago")
    Integer cancelarPago(@Param("p_id_cliente") Integer idCliente, @Param("p_fecha") String fecha);
    
    @Modifying
    @Transactional
    @Query(value = "update pago set total = ?1 where id_pago = ?2", nativeQuery = true)
    int actualizarTotal(Float total, Integer idPago);
    
    @Modifying
    @Transactional
    @Query(value = "update cliente set abono = ?1 where id_cliente = ?2", nativeQuery = true)
    int setAbono(Float abono, Integer idCliente);
    
    
    @Query(value = "select c.abono from cliente c where c.id_cliente = ?1",
            nativeQuery = true)
    Float obtenerAbonoActual(Integer idCliente);
    
}
