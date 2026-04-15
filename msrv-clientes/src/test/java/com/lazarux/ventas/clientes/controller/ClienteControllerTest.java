package com.lazarux.ventas.clientes.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lazarux.ventas.clientes.entity.Cliente;
import com.lazarux.ventas.clientes.repository.ClienteRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClienteController.class)
class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClienteRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void findAll_debeRetornarListaDeClientes() throws Exception {
        Cliente c1 = Cliente.builder().id(1L).nombre("Juan").apellido("Pérez")
                .email("juan@email.com").telefono("555-1111").build();
        Cliente c2 = Cliente.builder().id(2L).nombre("Ana").apellido("López")
                .email("ana@email.com").telefono("555-2222").build();

        when(repository.findAll()).thenReturn(List.of(c1, c2));

        mockMvc.perform(get("/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].nombre").value("Juan"))
                .andExpect(jsonPath("$[1].nombre").value("Ana"));
    }

    @Test
    void findAll_cuandoNoHayClientes_debeRetornarListaVacia() throws Exception {
        when(repository.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void findById_cuandoExiste_debeRetornarCliente() throws Exception {
        Cliente cliente = Cliente.builder().id(1L).nombre("Juan").apellido("Pérez")
                .email("juan@email.com").telefono("555-1111").build();

        when(repository.findById(1L)).thenReturn(Optional.of(cliente));

        mockMvc.perform(get("/clientes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Juan"))
                .andExpect(jsonPath("$.email").value("juan@email.com"));
    }

    @Test
    void findById_cuandoNoExiste_debeRetornar404() throws Exception {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/clientes/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void save_debeCrearClienteYRetornarlo() throws Exception {
        Cliente nuevo = Cliente.builder().nombre("Carlos").apellido("Ruiz")
                .email("carlos@email.com").telefono("555-3333").build();
        Cliente guardado = Cliente.builder().id(3L).nombre("Carlos").apellido("Ruiz")
                .email("carlos@email.com").telefono("555-3333").build();

        when(repository.save(any(Cliente.class))).thenReturn(guardado);

        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.nombre").value("Carlos"));
    }

    @Test
    void deleteById_debeEliminarYRetornar200() throws Exception {
        doNothing().when(repository).deleteById(1L);

        mockMvc.perform(delete("/clientes/1"))
                .andExpect(status().isOk());

        verify(repository, times(1)).deleteById(1L);
    }
}
