package com.bancoexterior.parametros.tasas.exception;

public class BadRequestUnprocessableException extends RuntimeException {
	
	public BadRequestUnprocessableException(String codigo) {
		super(codigo);
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
