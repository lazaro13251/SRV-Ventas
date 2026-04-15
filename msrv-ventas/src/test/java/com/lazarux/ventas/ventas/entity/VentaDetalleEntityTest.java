package com.lazarux.ventas.ventas.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class VentaDetalleEntityTest {

    @Test
    void calcularSubtotal_conPrecioYCantidad_debeCalcularCorrectamente() {
        VentaDetalle detalle = VentaDetalle.builder()
                .productoId("prod-1").cantidad(4).precioUnitario(new BigDecimal("25.00")).build();
        detalle.calcularSubtotal(); // 4 * 25 = 100

        assertThat(detalle.getSubtotal()).isEqualByComparingTo(new BigDecimal("100.00"));
    }

    @Test
    void calcularSubtotal_conPrecioNull_debeDevolverCero() {
        VentaDetalle detalle = VentaDetalle.builder()
                .productoId("prod-1").cantidad(3).precioUnitario(null).build();
        detalle.calcularSubtotal();

        assertThat(detalle.getSubtotal()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void calcularSubtotal_conCantidadNull_debeDevolverCero() {
        VentaDetalle detalle = VentaDetalle.builder()
                .productoId("prod-1").cantidad(null).precioUnitario(new BigDecimal("10.00")).build();
        detalle.calcularSubtotal();

        assertThat(detalle.getSubtotal()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void calcularSubtotal_conCantidadUno_debeIgualArPrecio() {
        VentaDetalle detalle = VentaDetalle.builder()
                .productoId("prod-1").cantidad(1).precioUnitario(new BigDecimal("99.99")).build();
        detalle.calcularSubtotal();

        assertThat(detalle.getSubtotal()).isEqualByComparingTo(new BigDecimal("99.99"));
    }
}
