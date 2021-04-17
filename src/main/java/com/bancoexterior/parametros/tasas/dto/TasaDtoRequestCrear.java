package com.bancoexterior.parametros.tasas.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.bancoexterior.parametros.tasas.config.Codigos.CodRespuesta;
import com.bancoexterior.parametros.tasas.config.Codigos.ParamConfig;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class TasaDtoRequestCrear implements Serializable{
	

	@JsonProperty("codMonedaOrigen")
	@NotEmpty(message=CodRespuesta.CDE1009)
	@Pattern(regexp=ParamConfig.CODMONEDA, message=CodRespuesta.CDE1009)
	private String codMonedaOrigen;
	
	@JsonProperty("codMonedaDestino")
	@NotEmpty(message=CodRespuesta.CDE1010)
	@Pattern(regexp=ParamConfig.CODMONEDA, message=CodRespuesta.CDE1010)
	private String codMonedaDestino;
	
	@JsonProperty("montoTasa")
	@NotNull(message = CodRespuesta.CDE1011)
	@Digits(integer=13, fraction=2, message = CodRespuesta.CDE1011)
	private BigDecimal montoTasa;

	
		
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
