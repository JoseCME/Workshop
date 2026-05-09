package com.ejemplo.demo.domain.service;

import com.ejemplo.demo.api.dto.CategoriaRequest;
import com.ejemplo.demo.api.dto.CategoriaResponse;
import com.ejemplo.demo.domain.model.Categoria;
import com.ejemplo.demo.domain.repository.CategoriaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class CategoriaService {

    private final CategoriaRepository repo;

    public CategoriaService(CategoriaRepository repo) {
        this.repo = repo;
    }

    @Transactional(readOnly = true)
    public List<CategoriaResponse> listar() {
        return repo.findAll().stream()
            .map(c -> new CategoriaResponse(c.getId(), c.getNombre(), c.getDescripcion()))
            .toList();
    }

    @Transactional(readOnly = true)
    public CategoriaResponse obtener(Long id) {
        Categoria c = repo.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Categoria no encontrada: " + id));
        return new CategoriaResponse(c.getId(), c.getNombre(), c.getDescripcion());
    }

    @Transactional
    public CategoriaResponse crear(CategoriaRequest req) {
        Categoria c = new Categoria();
        c.setNombre(req.nombre());
        c.setDescripcion(req.descripcion());
        repo.save(c);
        return new CategoriaResponse(c.getId(), c.getNombre(), c.getDescripcion());
    }

    @Transactional
    public CategoriaResponse actualizar(Long id, CategoriaRequest req) {
        Categoria c = repo.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Categoria no encontrada: " + id));
        c.setNombre(req.nombre());
        c.setDescripcion(req.descripcion());
        return new CategoriaResponse(c.getId(), c.getNombre(), c.getDescripcion());
    }

    @Transactional
    public void eliminar(Long id) {
        if (!repo.existsById(id))
            throw new EntityNotFoundException("Categoria no encontrada: " + id);
        repo.deleteById(id);
    }
}