package com.ejemplo.demo.domain.service;

import com.ejemplo.demo.api.dto.SaludoResponse;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class SaludoService {

    public SaludoResponse crearSaludo(String nombre) {
        String nombreNormalizado = normalizarNombre(nombre);
        String mensaje = "Hola, %s. Bienvenido a Spring Boot 3!".formatted(nombreNormalizado);
        return new SaludoResponse(mensaje, Instant.now());
    }

    /*
    PASO 4 (EJERCICIO):
    - Modifica esta logica para personalizar el formato del nombre.
    - Ideas:
      1) Primera letra mayuscula y resto minuscula.
      2) Rechazar nombres con numeros.
      3) Agregar prefijo "Estudiante".
    */
    String normalizarNombre(String nombre) {
        String NombreBien = nombre;

        if (nombre == null) {
            nombre = "desconocido";
        }

        nombre = nombre.trim();

        boolean tieneNumeros = false;
        for (int i = 0; i < nombre.length(); i++) {
            if (Character.isDigit(nombre.charAt(i))) {
                tieneNumeros = true;
                break;
            }
        }

        if (tieneNumeros) {
        	 throw new IllegalArgumentException("El nombre no debe contener numeros");
        }

        NombreBien = "Estudiante " + nombre.substring(0, 1).toUpperCase() + nombre.substring(1).toLowerCase();

        return NombreBien;
    }
    
}
