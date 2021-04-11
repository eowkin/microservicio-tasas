package com.bancoexterior.parametros.tasas.exception;

public class FieldAlreadyExistException extends BadRequestException{
	private static final String DESCRIPCION = "Ya existe, esta en uso";

	public FieldAlreadyExistException(String detail) {
		super(DESCRIPCION+". "+detail);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
