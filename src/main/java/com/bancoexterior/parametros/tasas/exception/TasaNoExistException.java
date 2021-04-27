package com.bancoexterior.parametros.tasas.exception;

public class TasaNoExistException extends BadRequestException{
	
	public TasaNoExistException(String codigo) {
		super(codigo);
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
