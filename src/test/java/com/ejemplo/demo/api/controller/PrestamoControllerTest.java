package com.ejemplo.demo.api.controller;

import com.ejemplo.demo.api.dto.PrestamoRequest;
import com.ejemplo.demo.api.dto.PrestamoResponse;
import com.ejemplo.demo.api.exception.GlobalExceptionHandler;  
import com.ejemplo.demo.domain.service.PrestamoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;           
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PrestamoController.class)
@Import(GlobalExceptionHandler.class)   
class PrestamoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PrestamoService prestamoService;

    @Test
    @DisplayName("POST /prestamo con datos válidos debe retornar 200 y resultados")
    void debeRetornarSimulacionExitosa() throws Exception {

        PrestamoRequest request = new PrestamoRequest(
                new BigDecimal("10000"),
                new BigDecimal("12"),
                24
        );

        PrestamoResponse response = new PrestamoResponse(
                new BigDecimal("470.73"),
                new BigDecimal("1297.52"),
                new BigDecimal("11297.52")
        );

        when(prestamoService.simular(any(PrestamoRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/v1/simulaciones/prestamo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cuotaMensual").value(470.73))
                .andExpect(jsonPath("$.interesTotal").value(1297.52))
                .andExpect(jsonPath("$.totalPagar").value(11297.52));
    }

    @Test
    @DisplayName("POST /prestamo con datos inválidos debe retornar 400 y VALIDATION_ERROR")
    void debeRetornar400ConDatosInvalidos() throws Exception {

        String requestInvalida = """
                {
                    "monto": -500,
                    "tasaAnual": 12,
                    "meses": 999
                }
                """;

        mockMvc.perform(post("/api/v1/simulaciones/prestamo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestInvalida))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.codigo").value("VALIDATION_ERROR"));
    }
}