package com.lazarux.ventas.productos.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lazarux.ventas.productos.entity.Producto;
import com.lazarux.ventas.productos.repository.ProductoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductoController.class)
class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductoRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void findAll_debeRetornarListaDeProductos() throws Exception {
        Producto p1 = Producto.builder().id("abc1").nombre("Laptop")
                .descripcion("Laptop gaming").precio(new BigDecimal("1200.00")).stock(10).build();
        Producto p2 = Producto.builder().id("abc2").nombre("Mouse")
                .descripcion("Mouse inalámbrico").precio(new BigDecimal("25.00")).stock(50).build();

        when(repository.findAll()).thenReturn(List.of(p1, p2));

        mockMvc.perform(get("/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].nombre").value("Laptop"))
                .andExpect(jsonPath("$[1].nombre").value("Mouse"));
    }

    @Test
    void findAll_cuandoNoHayProductos_debeRetornarListaVacia() throws Exception {
        when(repository.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void findById_cuandoExiste_debeRetornarProducto() throws Exception {
        Producto producto = Producto.builder().id("abc1").nombre("Laptop")
                .descripcion("Laptop gaming").precio(new BigDecimal("1200.00")).stock(10).build();

        when(repository.findById("abc1")).thenReturn(Optional.of(producto));

        mockMvc.perform(get("/productos/abc1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("abc1"))
                .andExpect(jsonPath("$.nombre").value("Laptop"))
                .andExpect(jsonPath("$.precio").value(1200.00));
    }

    @Test
    void findById_cuandoNoExiste_debeRetornar404() throws Exception {
        when(repository.findById("no-existe")).thenReturn(Optional.empty());

        mockMvc.perform(get("/productos/no-existe"))
                .andExpect(status().isNotFound());
    }

    @Test
    void save_debeCrearProductoYRetornarlo() throws Exception {
        Producto nuevo = Producto.builder().nombre("Teclado")
                .descripcion("Teclado mecánico").precio(new BigDecimal("75.00")).stock(30).build();
        Producto guardado = Producto.builder().id("abc3").nombre("Teclado")
                .descripcion("Teclado mecánico").precio(new BigDecimal("75.00")).stock(30).build();

        when(repository.save(any(Producto.class))).thenReturn(guardado);

        mockMvc.perform(post("/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("abc3"))
                .andExpect(jsonPath("$.nombre").value("Teclado"));
    }

    @Test
    void deleteById_debeEliminarYRetornar200() throws Exception {
        doNothing().when(repository).deleteById("abc1");

        mockMvc.perform(delete("/productos/abc1"))
                .andExpect(status().isOk());

        verify(repository, times(1)).deleteById("abc1");
    }
}
