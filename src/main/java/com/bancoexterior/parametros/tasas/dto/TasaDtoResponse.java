package com.bancoexterior.parametros.tasas.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.bancoexterior.parametros.tasas.response.Resultado;

import lombok.Data;


@Data
public class TasaDtoResponse implements Serializable{

	private Resultado resultado;
	
	private List<TasaDto> listTasasDto;
	
	public TasaDtoResponse() {
		super();
		this.resultado = new Resultado();
		this.listTasasDto = new ArrayList<TasaDto>();
	}
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
