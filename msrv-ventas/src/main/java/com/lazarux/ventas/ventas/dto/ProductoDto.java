package com.lazarux.ventas.ventas.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductoDto {
    private String id;
    private String nombre;
    private BigDecimal precio;
    private Integer stock;
}
