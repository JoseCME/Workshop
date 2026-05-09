package com.ejemplo.demo.api.controller;

import com.ejemplo.demo.api.dto.CategoriaRequest;
import com.ejemplo.demo.api.dto.CategoriaResponse;
import com.ejemplo.demo.domain.service.CategoriaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoriaController.class)
class CategoriaControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    CategoriaService service;

    @Test
    void crear_valido_retorna201() throws Exception {
        CategoriaRequest req = new CategoriaRequest("Bebidas", "Gaseosas y jugos");
        CategoriaResponse res = new CategoriaResponse(1L, "Bebidas", "Gaseosas y jugos");
        when(service.crear(any())).thenReturn(res);

        mockMvc.perform(post("/api/v1/categorias")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.nombre").value("Bebidas"));
    }

    @Test
    void crear_invalido_retorna400() throws Exception {
     
        String bodyInvalido = """
            {"nombre": "", "descripcion": "algo"}
            """;

        mockMvc.perform(post("/api/v1/categorias")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bodyInvalido))
            .andExpect(status().isBadRequest());
    }

    @Test
    void obtener_noExiste_retorna404() throws Exception {
        when(service.obtener(99L))
            .thenThrow(new EntityNotFoundException("Categoria no encontrada: 99"));

        mockMvc.perform(get("/api/v1/categorias/99"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.codigo").value("NOT_FOUND"));
    }
}