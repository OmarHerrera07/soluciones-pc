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

	@Query(value = "SELECT SUM(p.precio) AS suma_precios FROM meses_pago mp JOIN paquete_internet p ON p.id_paquete = mp.id_paquete WHERE mp.id_cliente = ?1 AND DATE(mp.fecha_pago) = CURDATE()",
            nativeQuery = true)
    Float ObtenerTotalPago(Integer id);
	
	@Query(value = "select mp.fecha,p.precio  from meses_pago mp join paquete_internet p on p.id_paquete = mp.id_paquete where mp.id_cliente  = ?1 and DATE(mp.fecha_pago) = CURDATE()",
            nativeQuery = true)
    List<MesesPagoDTO> ObtenerPagosRealizados(Integer id);
	
	@Query(value = "SELECT mp.fecha FROM meses_pago mp WHERE id_cliente = ?1 AND YEAR(fecha) = YEAR(CURDATE())",
            nativeQuery = true)
    List<Date> obtnerMesesPagados(Integer idCliente);
	
	@Query(value = "SELECT mp.fecha FROM meses_pago mp WHERE YEAR(mp.fecha) = ?1 and mp.id_cliente =?2",
            nativeQuery = true)
	List<Date> obtnerMesesPagadosFiltro(String anio,Integer idCliente);
	
	@Query(value = "SELECT mp.fecha,p.precio FROM meses_pago mp join paquete_internet p on mp.id_paquete = p.id_paquete  where mp.id_cliente =?1 and mp.id_pago =?2",
            nativeQuery = true)
	List<MesesRecibo> obtnerMesesPagadosRecibo(Integer idCliente,Integer idPago);
}
