package com.lazarux.ventas.ventas.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class VentaEntityTest {

    @Test
    void calcularTotal_conDetalles_debeCalcularCorrectamente() {
        VentaDetalle d1 = VentaDetalle.builder()
                .productoId("p1").cantidad(2).precioUnitario(new BigDecimal("100.00")).build();
        VentaDetalle d2 = VentaDetalle.builder()
                .productoId("p2").cantidad(3).precioUnitario(new BigDecimal("50.00")).build();

        Venta venta = Venta.builder().clienteId(1L).detalles(List.of(d1, d2)).build();
        venta.calcularTotal(); // 2*100 + 3*50 = 350

        assertThat(venta.getTotal()).isEqualByComparingTo(new BigDecimal("350.00"));
    }

    @Test
    void calcularTotal_sinDetalles_debeDevolverCero() {
        Venta venta = Venta.builder().clienteId(1L).detalles(List.of()).build();
        venta.calcularTotal();

        assertThat(venta.getTotal()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void calcularTotal_conDetallesNull_debeDevolverCero() {
        Venta venta = Venta.builder().clienteId(1L).detalles(null).build();
        venta.calcularTotal();

        assertThat(venta.getTotal()).isEqualByComparingTo(BigDecimal.ZERO);
    }
}
