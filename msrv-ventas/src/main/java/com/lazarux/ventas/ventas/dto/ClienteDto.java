package com.lazarux.ventas.ventas.dto;

import lombok.Data;

@Data
public class ClienteDto {
    private Long id;
    private String nombre;
    private String apellido;
    private String email;
}
