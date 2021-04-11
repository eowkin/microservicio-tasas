package com.bancoexterior.parametros.tasas.dto;

import java.io.Serializable;
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
	private Double montoTasa;
	
	@JsonProperty("codUsuario")
	private String codUsuario;
	
	@JsonProperty("fechaModificacion")
	private Date fechaModificacion;
	
	
	public TasaDto(TasaRequest tasaRequest) {
		super();
		this.codMonedaOrigen = tasaRequest.getTasasDtoRequest().getCodMonedaOrigen();
		this.codMonedaDestino = tasaRequest.getTasasDtoRequest().getCodMonedaDestino();
		this.montoTasa = tasaRequest.getTasasDtoRequest().getMontoTasa();
		this.codUsuario = tasaRequest.getCodUsuarioMR();
		
		
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}

