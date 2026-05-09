package com.ejemplo.demo.domain.repository;

import com.ejemplo.demo.domain.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
}