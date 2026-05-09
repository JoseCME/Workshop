package com.ejemplo.demo.api.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record ProductoRequest(
    @NotBlank String nombre,
    @NotNull @DecimalMin("0.01") BigDecimal precio,
    @NotNull Long categoriaId
) {}