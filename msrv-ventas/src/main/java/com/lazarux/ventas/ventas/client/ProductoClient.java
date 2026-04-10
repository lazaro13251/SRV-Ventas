package com.lazarux.ventas.ventas.client;

import com.lazarux.ventas.ventas.dto.ProductoDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "productos-svc", url = "http://localhost:8083/productos")
public interface ProductoClient {
    @GetMapping("/{id}")
    ProductoDto getProductoById(@PathVariable("id") String id);
}
