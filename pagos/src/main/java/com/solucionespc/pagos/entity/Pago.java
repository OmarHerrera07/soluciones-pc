package com.solucionespc.pagos.entity;

import java.util.Date;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pago")
public class Pago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pago")
    private Integer idPago;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente")
    private Cliente idCliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario")
    private Usuario idUsuario;

    @Column(name = "tipo_pago")
    private Integer tipoPago;
    
    @Column(name = "fecha")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date fecha;

    private Float total;
    
    @Lob
    @Column(name = "recibo")
    private byte[] recibo;
    
    @Column(name = "tipo_ticket")
    private Integer tipoTicket;
    
    private Float abono;
    
}
