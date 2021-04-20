package com.bancoexterior.parametros.tasas.dto;

import java.io.Serializable;

import com.bancoexterior.parametros.tasas.response.Resultado;

import lombok.Data;

@Data
public class TasaDtoResponseActualizar implements Serializable{

	
	private Resultado resultado;
	
	
	
	
	public TasaDtoResponseActualizar() {
		super();
		this.resultado = new Resultado();
	}




	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
