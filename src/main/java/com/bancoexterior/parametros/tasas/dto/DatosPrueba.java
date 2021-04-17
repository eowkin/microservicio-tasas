package com.bancoexterior.parametros.tasas.dto;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class DatosPrueba implements Serializable{
	
	//@NotEmpty
	@NotNull
	private String nombre;
	
	//@NotEmpty
	@NotNull
	private double monto;

	
}
