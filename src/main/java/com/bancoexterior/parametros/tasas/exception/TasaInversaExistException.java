package com.bancoexterior.parametros.tasas.exception;

public class TasaInversaExistException extends BadRequestException{
	
	public TasaInversaExistException(String codigo) {
		super(codigo);
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
