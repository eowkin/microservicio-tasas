package com.bancoexterior.parametros.tasas.exception;

public class NotFoundException extends RuntimeException {

	public NotFoundException(String codigo) {
        super(codigo);
    }
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
