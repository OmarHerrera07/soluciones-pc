package com.solucionespc.pagos.repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.solucionespc.pagos.dto.PagoDTO;
import com.solucionespc.pagos.entity.Pago;
import com.solucionespc.pagos.entity.Usuario;

public interface PagoRepository extends JpaRepository<Pago, Integer>{
	@Query(value = "select *from pago p where p.id_cliente = ?1 and DATE(p.fecha) = CURDATE()",
            nativeQuery = true)
    Pago ObtenerPago(Integer id);
	
    @Query(value = "select p.id_pago as idPago, c.nombre, p.fecha,p.total from pago p join cliente c on p.id_cliente  = c.id_cliente WHERE LOWER(c.nombre) LIKE CONCAT('%', LOWER(?1), '%')"
    		+ "and (p.fecha >= ?2 or cast(?2 as date) is null) and (p.fecha <= ?3 or cast(?3 as date) is null)",
            countQuery = "SELECT COUNT(*) from pago p join cliente c on p.id_cliente  = c.id_cliente WHERE LOWER(c.nombre) LIKE CONCAT('%', LOWER(?1), '%') and (p.fecha >= ?2 or cast(?2 as date) is null) and (p.fecha <= ?3 or cast(?3 as date) is null)",
            nativeQuery = true)
    Page<PagoDTO> paginacionPagos(String nombre,String fechaInico, String fechaFIn, Pageable pageable);
}
