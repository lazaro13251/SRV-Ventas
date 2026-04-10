package com.lazarux.ventas.ventas.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Table(name = "ventas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Venta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    private Long clienteId;
    
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime fecha;
    
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private BigDecimal total;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "venta_id")
    private List<VentaDetalle> detalles;

    @PrePersist
    public void prePersist() {
        this.fecha = LocalDateTime.now();
        calcularTotal();
    }

    @PreUpdate
    public void preUpdate() {
        calcularTotal();
    }

    public void calcularTotal() {
        if (this.detalles != null && !this.detalles.isEmpty()) {
            this.total = this.detalles.stream()
                    .map(detalle -> {
                        detalle.calcularSubtotal();
                        return detalle.getSubtotal() != null ? detalle.getSubtotal() : BigDecimal.ZERO;
                    })
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        } else {
            this.total = BigDecimal.ZERO;
        }
    }
}
