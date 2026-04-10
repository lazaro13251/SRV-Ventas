package com.lazarux.ventas.productos.repository;

import com.lazarux.ventas.productos.entity.Producto;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository extends MongoRepository<Producto, String> {}
