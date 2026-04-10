package com.ejemplo.demo.domain.service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import org.springframework.stereotype.Service;

import com.ejemplo.demo.api.dto.PrestamoRequest;
import com.ejemplo.demo.api.dto.PrestamoResponse;

@Service
public class PrestamoService {

	public PrestamoResponse simular(PrestamoRequest request) {
		BigDecimal monto     = request.monto();
		   BigDecimal tasaAnual = request.tasaAnual();
	        int        meses     = request.meses();
	        
	        BigDecimal tasaMensual = tasaAnual
	                .divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP)
	                .divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP);
	        
	        BigDecimal unoMasTasa = BigDecimal.ONE.add(tasaMensual);
	        BigDecimal factor     = unoMasTasa.pow(meses, MathContext.DECIMAL128);

	        
	        BigDecimal numerador   = monto.multiply(tasaMensual.multiply(factor));
	        BigDecimal denominador = factor.subtract(BigDecimal.ONE);
	        if (denominador.compareTo(BigDecimal.ZERO) == 0) {
	            throw new IllegalArgumentException("No se puede calcular con esos valores");
	        }

	        BigDecimal cuotaMensual = numerador
	                .divide(denominador, 2, RoundingMode.HALF_UP); BigDecimal totalPagar   = cuotaMensual
	                .multiply(BigDecimal.valueOf(meses))
	                .setScale(2, RoundingMode.HALF_UP);

	        BigDecimal interesTotal = totalPagar
	                .subtract(monto)
	                .setScale(2, RoundingMode.HALF_UP);

	        return new PrestamoResponse(cuotaMensual, interesTotal, totalPagar);

	}
}
