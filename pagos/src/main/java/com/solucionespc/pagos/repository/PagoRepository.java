package com.solucionespc.pagos.repository;
import com.solucionespc.pagos.dto.Corte;
import com.solucionespc.pagos.dto.InfoRecibo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.solucionespc.pagos.dto.PagoDTO;
import com.solucionespc.pagos.entity.Pago;

import jakarta.transaction.Transactional;

public interface PagoRepository extends JpaRepository<Pago, Integer>{
	
	/**
	 * Obtiene el pago realizado por un cliente en la fecha actual.
	 *
	 * @param id ID del cliente para el cual se obtiene el pago.
	 * @return Objeto Pago que representa el pago realizado por el cliente en la fecha actual.
	 */
	@Query(value = "select *from pago p where p.id_cliente = ?1 and DATE(p.fecha) = CURDATE()",
            nativeQuery = true)
    Pago ObtenerPago(Integer id);
	
	
	/**
	 * Realiza la paginación de pagos con opciones de filtrado por nombre de cliente y rango de fechas.
	 *
	 * @param nombre      El nombre del cliente a filtrar (ignora mayúsculas y minúsculas).
	 * @param fechaInicio La fecha de inicio del rango para filtrar pagos o null para ignorar este filtro.
	 * @param fechaFin    La fecha de fin del rango para filtrar pagos o null para ignorar este filtro.
	 * @param pageable    Información sobre la paginación.
	 * @return Una página de objetos PagoDTO que cumplen con los criterios de paginación y filtrado.
	 */
    @Query(value = "select p.id_pago as idPago, c.nombre, p.fecha,p.total from pago p join cliente c on p.id_cliente  = c.id_cliente WHERE LOWER(c.nombre) LIKE CONCAT('%', LOWER(?1), '%')"
    		+ "and (p.fecha >= ?2 or cast(?2 as date) is null) and (p.fecha <= ?3 or cast(?3 as date) is null) order by p.fecha desc",
            countQuery = "SELECT COUNT(*) from pago p join cliente c on p.id_cliente  = c.id_cliente WHERE LOWER(c.nombre) LIKE CONCAT('%', LOWER(?1), '%') and (p.fecha >= ?2 or cast(?2 as date) is null) and (p.fecha <= ?3 or cast(?3 as date) is null)",
            nativeQuery = true)
    Page<PagoDTO> paginacionPagos(String nombre,String fechaInico, String fechaFIn, Pageable pageable);
    
    
    /**
     * Actualiza el recibo asociado a un pago específico.
     *
     * @param recibo Byte array que representa el recibo a actualizar.
     * @param idPago ID del pago para el cual se actualiza el recibo.
     * @return El número de registros actualizados (debería ser 1 si la actualización fue exitosa).
     */
    @Modifying
    @Transactional
    @Query(value = "update pago set recibo = ?1 where id_pago = ?2", nativeQuery = true)
    int actualizarRecibo(byte[] recibo, Integer idPago);

    
    /**
     * Obtiene información detallada de un recibo específico.
     *
     * @param idRecibo ID del recibo para el cual se obtiene la información.
     * @return Objeto InfoRecibo que representa la información detallada del recibo.
     */
    @Query(value = "select c.rfc,u.nombre as nombreUsuario,p.fecha,p.total  from pago p join cliente c on p.id_cliente = c.id_cliente join usuario u on u.id_usuario = p.id_usuario where p.id_pago =?1",
            nativeQuery = true)
    InfoRecibo getInfoRecibo(Integer idRecibo);
    
    
    /**
     * Obtiene información de corte para los pagos realizados en la fecha actual.
     *
     * @return Lista de objetos Corte que representan la información de corte.
     */
    @Query(value = "select c.nombre as nombreCliente,p.total as totalPago from pago p join cliente c on p.id_cliente = c.id_cliente where p.fecha = curdate()",
            nativeQuery = true)
    List<Corte> getInfoCorte();
    
    @Query(value = "select c.nombre as nombreCliente,p.total as totalPago from pago p join cliente c on p.id_cliente = c.id_cliente where p.fecha = curdate() and p.tipo_pago  = 1",
            nativeQuery = true)
    List<Corte> getInfoCorteEfectivo();
    
    @Query(value = "select c.nombre as nombreCliente,p.total as totalPago from pago p join cliente c on p.id_cliente = c.id_cliente where p.fecha = curdate() and p.tipo_pago  = 2",
            nativeQuery = true)
    List<Corte> getInfoCorteTransferencia();
    
    @Query(value = "select c.nombre as nombreCliente,p.total as totalPago from pago p join cliente c on p.id_cliente = c.id_cliente where (p.fecha >= ?1 or cast(?1 as date) is null) and (p.fecha <= ?2 or cast(?2 as date) is null)",
            nativeQuery = true)
    List<Corte> getInfoCorteDinamico(String fechaInico, String fechaFIn);
}
