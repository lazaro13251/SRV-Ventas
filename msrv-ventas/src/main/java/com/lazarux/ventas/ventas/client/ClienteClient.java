package com.lazarux.ventas.ventas.client;

import com.lazarux.ventas.ventas.dto.ClienteDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "clientes-svc", url = "http://localhost:8082/clientes")
public interface ClienteClient {
    @GetMapping("/{id}")
    ClienteDto getClienteById(@PathVariable("id") Long id);
}
