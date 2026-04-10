package com.lazarux.ventas.ventas.controller;

import com.lazarux.ventas.ventas.entity.Venta;
import com.lazarux.ventas.ventas.repository.VentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ventas")
@RequiredArgsConstructor
public class VentaController {
    
    private final VentaRepository repository;

    @GetMapping
    public ResponseEntity<List<Venta>> findAll() {
        return ResponseEntity.ok(repository.findAll());
    }

    @PostMapping
    public ResponseEntity<Venta> save(@RequestBody Venta venta) {
        // En un entorno real aqui iria el VentaService que usa OpenFeign 
        // para validar clientes y reducir stock usando los clientes construidos.
        return ResponseEntity.ok(repository.save(venta));
    }
}
