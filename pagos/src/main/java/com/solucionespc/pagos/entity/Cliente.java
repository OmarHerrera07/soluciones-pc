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
@Table(name = "cliente")
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente")
    private Integer idCliente;
    
    private String nombre;
    
    private String telefono;
    
    private String telefono2;
    
    private String observaciones;
    
    @Column(name = "fecha_pago")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date fechaPago;
    
    private String rfc;
    
    private int estado;
    
    @ManyToOne
    @JoinColumn(name = "id_paquete")
    private Paquete paquete;
    
    @ManyToOne
    @JoinColumn(name = "id_colonia")
    private Colonia colonia;
        
    private String coordenadas;
    
    @Column(name = "dias_atraso")
    private Integer diasAtraso;
    
    @Column(name = "ultimo_pago")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date ultimoPago;
    
    
}