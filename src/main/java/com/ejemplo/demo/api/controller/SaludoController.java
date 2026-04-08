package com.ejemplo.demo.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ejemplo.demo.api.dto.SaludoResponse;
import com.ejemplo.demo.domain.service.SaludoService;
import org.springframework.web.bind.annotation.RequestParam;
import com.ejemplo.demo.api.dto.SaludoRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class SaludoController {

    @GetMapping
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of(
                "estado", "ok",
                "mensaje", "Workshop Spring Boot activo"
                
        ));
        
    }
    private final SaludoService saludoService;

    public SaludoController(SaludoService saludoService) {
        this.saludoService = saludoService;
    }
    
    @GetMapping("/saludos")
    public ResponseEntity<SaludoResponse> saludar(
            @RequestParam(defaultValue = "Desconocido") String nombre
    ) {
        return ResponseEntity.ok(saludoService.crearSaludo(nombre));
    }
    @PostMapping("/saludos")
    public ResponseEntity<SaludoResponse> saludarPost(@Valid @RequestBody SaludoRequest request) {
        return ResponseEntity.ok(saludoService.crearSaludo(request.nombre()));
    }
    
    
}
