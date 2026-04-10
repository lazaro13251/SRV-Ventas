package com.lazarux.ventas.ventas.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "ventas_detalles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VentaDetalle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productoId; // Referencia a MongoDB
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;
}
