package com.ejemplo.demo.api.controller;


import com.ejemplo.demo.api.dto.SaludoRequest;
import com.ejemplo.demo.api.dto.SaludoResponse;
import com.ejemplo.demo.domain.service.SaludoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@WebMvcTest(SaludoController.class)
class SaludoControllerTest {

    @Autowired
    private MockMvc mockMvc;
    

    @MockBean
    private SaludoService saludoService;


    @Test
    @DisplayName("Debe responder health del workshop")
    void debeResponderHealthDelWorkshop() throws Exception {
        mockMvc.perform(get("/api/v1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("ok"));
    }

    @Test 
    @DisplayName("GET /api/v1/saludos?nombre=Ana -> 200 y mensaje correcto")
    void debeRetornarSaludoConNombreValido() throws Exception {
        // Arrange
        String mensajeEsperado = "Hola, Estudiante Ana. Bienvenido a Spring Boot 3!";
        when(saludoService.crearSaludo("Ana"))
                .thenReturn(new SaludoResponse(mensajeEsperado, Instant.now()));

        // Act & Assert
        mockMvc.perform(get("/api/v1/saludos")
                        .param("nombre", "Ana"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").value(mensajeEsperado));
    }
    
    @Test
    @DisplayName("POST /api/v1/saludos con nombre vacío debe retornar 400 y codigo VALIDATION_ERROR")
    void debeRetornar400ConNombreVacio() throws Exception {
        // Arrange
        String requestBody = "{\"nombre\":\"\"}";

        // Act & Assert
        mockMvc.perform(post("/api/v1/saludos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.codigo").value("VALIDATION_ERROR"));
    }

    
    
    /*
    PASO 6 (EJERCICIO):
    Cuando habilites los endpoints de /api/v1/saludos, crea estas pruebas:

    1) GET /api/v1/saludos?nombre=Ana -> 200 y mensaje correcto
    2) POST /api/v1/saludos con {"nombre":""} -> 400 y codigo VALIDATION_ERROR
    */
}
