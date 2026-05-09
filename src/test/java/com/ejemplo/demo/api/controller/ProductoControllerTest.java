package com.ejemplo.demo.api.controller;

import com.ejemplo.demo.api.dto.ProductoRequest;
import com.ejemplo.demo.api.dto.ProductoResponse;
import com.ejemplo.demo.domain.service.ProductoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductoController.class)
class ProductoControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    ProductoService service;

    @Test
    void crear_valido_retorna201() throws Exception {
        ProductoRequest req = new ProductoRequest("Coca Cola", new BigDecimal("15.00"), 1L);
        ProductoResponse res = new ProductoResponse(1L, "Coca Cola", new BigDecimal("15.00"), 1L, "Bebidas");
        when(service.crear(any())).thenReturn(res);

        mockMvc.perform(post("/api/v1/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.nombre").value("Coca Cola"));
    }

    @Test
    void crear_invalido_retorna400() throws Exception {
       
        String bodyInvalido = """
            {"nombre": "", "precio": null, "categoriaId": 1}
            """;

        mockMvc.perform(post("/api/v1/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bodyInvalido))
            .andExpect(status().isBadRequest());
    }

    @Test
    void obtener_noExiste_retorna404() throws Exception {
        when(service.obtener(99L))
            .thenThrow(new EntityNotFoundException("Producto no encontrado: 99"));

        mockMvc.perform(get("/api/v1/productos/99"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.codigo").value("NOT_FOUND"));
    }
}