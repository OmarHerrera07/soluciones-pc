package com.solucionespc.pagos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.solucionespc.pagos.dto.MesesPagoDTO;
import com.solucionespc.pagos.entity.MesesPago;

public interface MesesPagoRepositoty extends JpaRepository<MesesPago, Integer>{

	@Query(value = "SELECT SUM(p.precio) AS suma_precios FROM meses_pago mp JOIN paquete_internet p ON p.id_paquete = mp.id_paquete WHERE mp.id_cliente = ?1 AND DATE(mp.fecha_pago) = CURDATE()",
            nativeQuery = true)
    Float ObtenerTotalPago(Integer id);
	
	@Query(value = "select mp.fecha,p.precio  from meses_pago mp join paquete_internet p on p.id_paquete = mp.id_paquete where mp.id_cliente  = ?1 and DATE(mp.fecha_pago) = CURDATE()",
            nativeQuery = true)
    List<MesesPagoDTO> ObtenerPagosRealizados(Integer id);
	
	
}
