package com.ejemplo.demo.domain.service;

import com.ejemplo.demo.api.dto.ProductoRequest;
import com.ejemplo.demo.api.dto.ProductoResponse;
import com.ejemplo.demo.domain.model.Categoria;
import com.ejemplo.demo.domain.model.Producto;
import com.ejemplo.demo.domain.repository.CategoriaRepository;
import com.ejemplo.demo.domain.repository.ProductoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ProductoService {

    private final ProductoRepository repo;
    private final CategoriaRepository categoriaRepo;

    public ProductoService(ProductoRepository repo, CategoriaRepository categoriaRepo) {
        this.repo = repo;
        this.categoriaRepo = categoriaRepo;
    }

    @Transactional(readOnly = true)
    public List<ProductoResponse> listar() {
        return repo.findAll().stream()
            .map(p -> new ProductoResponse(
                p.getId(), p.getNombre(), p.getPrecio(),
                p.getCategoria().getId(), p.getCategoria().getNombre()))
            .toList();
    }

    @Transactional(readOnly = true)
    public ProductoResponse obtener(Long id) {
        Producto p = repo.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado: " + id));
        return new ProductoResponse(
            p.getId(), p.getNombre(), p.getPrecio(),
            p.getCategoria().getId(), p.getCategoria().getNombre());
    }

    @Transactional
    public ProductoResponse crear(ProductoRequest req) {
        Categoria cat = categoriaRepo.findById(req.categoriaId())
            .orElseThrow(() -> new EntityNotFoundException("Categoria no encontrada: " + req.categoriaId()));
        Producto p = new Producto();
        p.setNombre(req.nombre());
        p.setPrecio(req.precio());
        p.setCategoria(cat);
        repo.save(p);
        return new ProductoResponse(p.getId(), p.getNombre(), p.getPrecio(),
            cat.getId(), cat.getNombre());
    }

    @Transactional
    public ProductoResponse actualizar(Long id, ProductoRequest req) {
        Producto p = repo.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado: " + id));
        Categoria cat = categoriaRepo.findById(req.categoriaId())
            .orElseThrow(() -> new EntityNotFoundException("Categoria no encontrada: " + req.categoriaId()));
        p.setNombre(req.nombre());
        p.setPrecio(req.precio());
        p.setCategoria(cat);
        return new ProductoResponse(p.getId(), p.getNombre(), p.getPrecio(),
            cat.getId(), cat.getNombre());
    }

    @Transactional
    public void eliminar(Long id) {
        if (!repo.existsById(id))
            throw new EntityNotFoundException("Producto no encontrado: " + id);
        repo.deleteById(id);
    }
}