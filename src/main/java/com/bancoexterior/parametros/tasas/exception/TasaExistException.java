package com.bancoexterior.parametros.tasas.exception;

public class TasaExistException extends BadRequestException{
	
	public TasaExistException(String codigo) {
		super(codigo);
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
