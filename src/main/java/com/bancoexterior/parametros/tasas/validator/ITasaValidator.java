package com.bancoexterior.parametros.tasas.validator;

import org.springframework.validation.BindingResult;

import com.bancoexterior.parametros.tasas.dto.TasaRequestCrear;

public interface ITasaValidator {

	public void validarCrear(TasaRequestCrear tasaRequestCrear, BindingResult result);
	
	public void validarActualizar(TasaRequestCrear tasaRequestCrear, BindingResult result);
}
