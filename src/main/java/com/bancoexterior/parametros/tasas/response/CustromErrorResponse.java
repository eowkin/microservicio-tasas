package com.bancoexterior.parametros.tasas.response;

import lombok.Data;

@Data
public class CustromErrorResponse {

	private String timestamp;
	private int status;
	private String error;
	private String message;
	private String path;
}
