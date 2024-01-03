package com.solucionespc.pagos.entity;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "meses_pago")
public class MesesPago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_meses_pago")
    private Integer idMesesPago;
    
    @Column(name = "fecha")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date fecha;
    
    @ManyToOne
    @JoinColumn(name = "id_cliente")
    private Cliente idCliente;
    
    @ManyToOne
    @JoinColumn(name = "id_paquete")
    private Paquete idPaquete;
    
    @Column(name = "fecha_pago")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date fechaPago;
}
