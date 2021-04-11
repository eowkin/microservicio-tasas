package com.bancoexterior.parametros.tasas.response;



import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;


@Data
public class ResponseBad {
	@JsonProperty("resultado")
	private Resultado resultadoBAD;
	
	public ResponseBad() {
		super();
		this.resultadoBAD = new Resultado();

	}
}
