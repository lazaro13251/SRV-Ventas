package com.lazarux.ventas.ventas.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lazarux.ventas.ventas.entity.Venta;
import com.lazarux.ventas.ventas.entity.VentaDetalle;
import com.lazarux.ventas.ventas.repository.VentaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VentaController.class)
class VentaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VentaRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void findAll_debeRetornarListaDeVentas() throws Exception {
        Venta v1 = Venta.builder().id(1L).clienteId(10L)
                .fecha(LocalDateTime.now()).total(new BigDecimal("300.00"))
                .detalles(List.of()).build();

        when(repository.findAll()).thenReturn(List.of(v1));

        mockMvc.perform(get("/ventas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].clienteId").value(10));
    }

    @Test
    void findAll_cuandoNoHayVentas_debeRetornarListaVacia() throws Exception {
        when(repository.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/ventas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void save_debeCrearVentaYRetornarla() throws Exception {
        VentaDetalle detalle = VentaDetalle.builder()
                .productoId("prod-abc").cantidad(2)
                .precioUnitario(new BigDecimal("50.00")).build();

        Venta nueva = Venta.builder().clienteId(5L).detalles(List.of(detalle)).build();
        Venta guardada = Venta.builder().id(1L).clienteId(5L)
                .fecha(LocalDateTime.now()).total(new BigDecimal("100.00"))
                .detalles(List.of(detalle)).build();

        when(repository.save(any(Venta.class))).thenReturn(guardada);

        mockMvc.perform(post("/ventas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nueva)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.clienteId").value(5));
    }
}
