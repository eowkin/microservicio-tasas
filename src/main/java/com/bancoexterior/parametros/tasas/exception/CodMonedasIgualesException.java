package com.bancoexterior.parametros.tasas.exception;

public class CodMonedasIgualesException extends BadRequestException{
	
	public CodMonedasIgualesException(String codigo) {
		super(codigo);
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
