package com.solucionespc.pagos.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.solucionespc.pagos.dto.Meses;
import com.solucionespc.pagos.dto.MesesPagoDTO;
import com.solucionespc.pagos.dto.MesesRecibo;
import com.solucionespc.pagos.entity.MesesPago;

public interface MesesPagoRepositoty extends JpaRepository<MesesPago, Integer>{

	/**
	 * Obtiene el total de pagos realizados en el mes actual para un cliente específico.
	 *
	 * @param id ID del cliente para el cual se calcula el total de pagos.
	 * @return Total de pagos realizados en el mes actual para el cliente.
	 */
	@Query(value = "SELECT SUM(p.precio) AS suma_precios FROM meses_pago mp JOIN paquete_internet p ON p.id_paquete = mp.id_paquete WHERE mp.id_cliente = ?1 AND DATE(mp.fecha_pago) = CURDATE()",
            nativeQuery = true)
    Float ObtenerTotalPago(Integer id);
	
	
	/**
	 * Obtiene los pagos realizados en el mes actual para un cliente específico.
	 *
	 * @param id ID del cliente para el cual se obtienen los pagos realizados.
	 * @return Lista de objetos MesesPagoDTO que representan los pagos realizados en el mes actual.
	 */
	@Query(value = "select mp.fecha,p.precio  from meses_pago mp join paquete_internet p on p.id_paquete = mp.id_paquete where mp.id_cliente  = ?1 and DATE(mp.fecha_pago) = CURDATE()",
            nativeQuery = true)
    List<MesesPagoDTO> ObtenerPagosRealizados(Integer id);
	
	/*
	 * SELECT mp.fecha FROM meses_pago mp WHERE id_cliente = ?1 AND YEAR(fecha) = YEAR(CURDATE())*
	 */
	
	/**
	 * Obtiene los meses en los que se realizaron pagos para un cliente específico.
	 *
	 * @param idCliente ID del cliente para el cual se obtienen los meses de pagos realizados.
	 * @return Lista de objetos Date que representan los meses en los que se realizaron pagos.
	 */
	@Query(value = "SELECT mp.fecha FROM meses_pago mp join cliente c on mp.id_cliente = c.id_cliente  WHERE mp.id_cliente = ?1 AND YEAR(fecha) = YEAR(c.fecha_pago)",
            nativeQuery = true)
    List<Date> obtnerMesesPagados(Integer idCliente);
	
	
	/**
	 * Obtiene los meses en los que se realizaron pagos para un cliente específico en un año determinado.
	 *
	 * @param anio      Año para el cual se filtran los meses de pagos.
	 * @param idCliente ID del cliente para el cual se obtienen los meses de pagos realizados.
	 * @return Lista de objetos Date que representan los meses en los que se realizaron pagos.
	 */
	@Query(value = "SELECT mp.fecha FROM meses_pago mp WHERE YEAR(mp.fecha) = ?1 and mp.id_cliente =?2",
            nativeQuery = true)
	List<Date> obtnerMesesPagadosFiltro(String anio,Integer idCliente);
	
	
	/**
	 * Obtiene los meses en los que se realizaron pagos para un recibo específico de un cliente.
	 *
	 * @param idCliente ID del cliente para el cual se obtienen los meses de pagos realizados.
	 * @param idPago    ID del pago asociado al recibo.
	 * @return Lista de objetos MesesRecibo que representan los meses en los que se realizaron pagos.
	 */
	@Query(value = "SELECT mp.fecha,p.precio FROM meses_pago mp join paquete_internet p on mp.id_paquete = p.id_paquete  where mp.id_cliente =?1 and mp.id_pago =?2",
            nativeQuery = true)
	List<MesesRecibo> obtnerMesesPagadosRecibo(Integer idCliente,Integer idPago);
}
