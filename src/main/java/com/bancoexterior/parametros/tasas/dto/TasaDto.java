package com.bancoexterior.parametros.tasas.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor @NoArgsConstructor
public class TasaDto implements Serializable{
	
	

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
	
	
	
	
	public TasaDto(TasaRequest tasaRequest) {
		super();
		this.codMonedaOrigen = tasaRequest.getTasasDtoRequest().getCodMonedaOrigen();
		this.codMonedaDestino = tasaRequest.getTasasDtoRequest().getCodMonedaDestino();
		this.montoTasa = tasaRequest.getTasasDtoRequest().getMontoTasa();
		this.codUsuario = tasaRequest.getCodUsuarioMR();
		
		
	}
	
	

	
	
	
	
	public TasaDto(String codMonedaOrigen, String codMonedaDestino, BigDecimal montoTasa, String codUsuario,
			Date fechaModificacion) {
		super();
		this.codMonedaOrigen = codMonedaOrigen;
		this.codMonedaDestino = codMonedaDestino;
		this.montoTasa = montoTasa;
		this.codUsuario = codUsuario;
		this.fechaModificacion = fechaModificacion;
	}







	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}

