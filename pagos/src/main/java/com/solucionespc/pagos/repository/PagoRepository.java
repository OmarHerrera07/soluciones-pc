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

public interface PagoRepository extends JpaRepository<Pago, Integer> {

	/**
	 * Obtiene el pago realizado por un cliente en la fecha actual.
	 *
	 * @param id ID del cliente para el cual se obtiene el pago.
	 * @return Objeto Pago que representa el pago realizado por el cliente en la
	 *         fecha actual.
	 */
	@Query(value = "select *from pago p where p.id_cliente = ?1 and DATE(p.fecha) = CURDATE()", nativeQuery = true)
	Pago ObtenerPago(Integer id);

	/**
	 * Realiza la paginación de pagos con opciones de filtrado por nombre de cliente
	 * y rango de fechas.
	 *
	 * @param nombre      El nombre del cliente a filtrar (ignora mayúsculas y
	 *                    minúsculas).
	 * @param fechaInicio La fecha de inicio del rango para filtrar pagos o null
	 *                    para ignorar este filtro.
	 * @param fechaFin    La fecha de fin del rango para filtrar pagos o null para
	 *                    ignorar este filtro.
	 * @param pageable    Información sobre la paginación.
	 * @return Una página de objetos PagoDTO que cumplen con los criterios de
	 *         paginación y filtrado.
	 */
	@Query(value = "select p.id_pago as idPago, c.nombre, p.fecha,p.total from pago p join cliente c on p.id_cliente  = c.id_cliente WHERE LOWER(c.nombre) LIKE CONCAT('%', LOWER(?1), '%')"
			+ "and (p.fecha >= ?2 or cast(?2 as date) is null) and (p.fecha <= ?3 or cast(?3 as date) is null) order by p.fecha desc", countQuery = "SELECT COUNT(*) from pago p join cliente c on p.id_cliente  = c.id_cliente WHERE LOWER(c.nombre) LIKE CONCAT('%', LOWER(?1), '%') and (p.fecha >= ?2 or cast(?2 as date) is null) and (p.fecha <= ?3 or cast(?3 as date) is null)", nativeQuery = true)
	Page<PagoDTO> paginacionPagos(String nombre, String fechaInico, String fechaFIn, Pageable pageable);

	/**
	 * Actualiza el recibo asociado a un pago específico.
	 *
	 * @param recibo Byte array que representa el recibo a actualizar.
	 * @param idPago ID del pago para el cual se actualiza el recibo.
	 * @return El número de registros actualizados (debería ser 1 si la
	 *         actualización fue exitosa).
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
	@Query(value = "select c.nombre as nombreCliente, co.colonia,  c.rfc,u.nombre as nombreUsuario,p.fecha,p.total,p.abono,p.tipo_ticket as tipoTicket  from pago p join cliente c on p.id_cliente = c.id_cliente join usuario u on u.id_usuario = p.id_usuario join colonia co on co.id_colonia = c.id_colonia where p.id_pago =?1", nativeQuery = true)
	InfoRecibo getInfoRecibo(Integer idRecibo);

	/**
	 * Obtiene información de corte para los pagos realizados en la fecha actual.
	 *
	 * @return Lista de objetos Corte que representan la información de corte.
	 */
	@Query(value = "select c.nombre as nombreCliente,p.total as totalPago from pago p join cliente c on p.id_cliente = c.id_cliente where p.fecha = curdate()", nativeQuery = true)
	List<Corte> getInfoCorte();

	/**
	 * Obtiene la información de corte para pagos en efectivo realizados hoy.
	 *
	 * @return Lista de objetos Corte que contiene el nombre del cliente y el total
	 *         del pago.
	 */
	@Query(value = "select c.nombre as nombreCliente,p.total as totalPago from pago p join cliente c on p.id_cliente = c.id_cliente where p.fecha = curdate() and p.tipo_pago  = 1", nativeQuery = true)
	List<Corte> getInfoCorteEfectivo();

	/**
	 * Obtiene la información de corte para pagos por transferencia realizados hoy.
	 *
	 * @return Lista de objetos Corte que contiene el nombre del cliente y el total
	 *         del pago.
	 */
	@Query(value = "select c.nombre as nombreCliente,p.total as totalPago from pago p join cliente c on p.id_cliente = c.id_cliente where p.fecha = curdate() and p.tipo_pago  = 2", nativeQuery = true)
	List<Corte> getInfoCorteTransferencia();

	/**
	 * Obtiene la información de corte dinámico para un rango de fechas.
	 *
	 * @param fechaInicio Fecha de inicio del rango.
	 * @param fechaFin    Fecha de fin del rango.
	 * @return Lista de objetos Corte que contiene el nombre del cliente y el total del pago.
	 */
	@Query(value = "select c.nombre as nombreCliente,p.total as totalPago from pago p join cliente c on p.id_cliente = c.id_cliente where (p.fecha >= ?1 or cast(?1 as date) is null) and (p.fecha <= ?2 or cast(?2 as date) is null)", nativeQuery = true)
	List<Corte> getInfoCorteDinamico(String fechaInico, String fechaFIn);

	/**
	 * Obtiene la información de corte dinámico para pagos en efectivo dentro de un rango de fechas.
	 *
	 * @param fechaInicio Fecha de inicio del rango.
	 * @param fechaFin    Fecha de fin del rango.
	 * @return Lista de objetos Corte que contiene el nombre del cliente y el total del pago en efectivo.
	 */
	@Query(value = "select c.nombre as nombreCliente,p.total as totalPago from pago p join cliente c on p.id_cliente = c.id_cliente where (p.fecha >= ?1 or cast(?1 as date) is null) and (p.fecha <= ?2 or cast(?2 as date) is null) and p.tipo_pago  = 1", nativeQuery = true)
	List<Corte> getInfoCorteDinamicoEfectivo(String fechaInico, String fechaFIn);

	
	/**
	 * Obtiene la información de corte dinámico para pagos por transferencia dentro de un rango de fechas.
	 *
	 * @param fechaInicio Fecha de inicio del rango.
	 * @param fechaFin    Fecha de fin del rango.
	 * @return Lista de objetos Corte que contiene el nombre del cliente y el total del pago por transferencia.
	 */
	@Query(value = "select c.nombre as nombreCliente,p.total as totalPago from pago p join cliente c on p.id_cliente = c.id_cliente where (p.fecha >= ?1 or cast(?1 as date) is null) and (p.fecha <= ?2 or cast(?2 as date) is null) and p.tipo_pago  = 2", nativeQuery = true)
	List<Corte> getInfoCorteDinamicoTransferencia(String fechaInico, String fechaFIn);

	/**
	 * Actualiza el monto total de un pago identificado por su ID.
	 *
	 * @param total  Nuevo monto total del pago.
	 * @param idPago Identificador del pago a actualizar.
	 * @return Entero que indica el número de filas afectadas por la actualización.
	 */
	@Modifying
	@Transactional
	@Query(value = "update pago set total = ?1 where id_pago = ?2", nativeQuery = true)
	int actualizarTotal(Float total, Integer idPago);

	/**
	 * Actualiza la información de pago, incluyendo el abono y el tipo de ticket, para un pago identificado por su ID.
	 *
	 * @param idPago     Identificador del pago a actualizar.
	 * @param abono      Nuevo monto de abono para el pago.
	 * @param tipoTicket Nuevo tipo de ticket para el pago.
	 * @return Entero que indica el número de filas afectadas por la actualización.
	 */
	@Modifying
	@Transactional
	@Query(value = "update pago set abono = ?2, tipo_ticket = ?3 where id_pago = ?1", nativeQuery = true)
	int actualizarInfoPago(Integer idPago, Float abono, Integer tipoTicket);

	/**
	 * Obtiene el número de meses con pago asociado a un pago identificado por su ID.
	 *
	 * @param id Identificador del pago.
	 * @return Número de meses con pago asociado.
	 */
	@Query(value = "select count(*) from meses_pago mp  where mp.id_pago = ?1", nativeQuery = true)
	Integer mesesConPago(Integer id);
}
