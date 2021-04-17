package com.bancoexterior.parametros.tasas.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor @NoArgsConstructor
public class TasaDtoInversa {
	@JsonProperty("codMonedaOrigen")
	private String codMonedaOrigen;
	
	@JsonProperty("codMonedaDestino")
	private String codMonedaDestino;
	
	@JsonProperty("montoTasa")
	private BigDecimal montoTasa;
	
	@JsonProperty("codUsuario")
	private String codUsuario;
	
	@JsonProperty("fechaModificacion")
	private Date fechaModificacion;
	
	@JsonProperty("montoTasaInversa")
	private BigDecimal montoTasaInversa;

}
