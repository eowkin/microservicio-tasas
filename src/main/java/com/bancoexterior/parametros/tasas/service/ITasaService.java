package com.bancoexterior.parametros.tasas.service;

import java.util.List;

import com.bancoexterior.parametros.tasas.dto.TasaDto;
import com.bancoexterior.parametros.tasas.dto.TasaDtoConsulta;
import com.bancoexterior.parametros.tasas.dto.TasaDtoRequestConsulta;
import com.bancoexterior.parametros.tasas.dto.TasaDtoResponse;
import com.bancoexterior.parametros.tasas.dto.TasaRequestConsulta;
import com.bancoexterior.parametros.tasas.entities.Tasa;
import com.bancoexterior.parametros.tasas.entities.TasaPk;







public interface ITasaService {

	public TasaDtoResponse consultaTasas(TasaRequestConsulta tasaRequestConsulta);
	
	public List<Tasa> findAllNative(String codMonedaOrigen, String codMonedaDestino);
	
	public List<Tasa> findAll();
	
	public Tasa findById(TasaPk id);
	
	public List<TasaDto> getTasaByCodMonedaOrigenAndCodMonedaDestino(String codMonedaOrigen, String codMonedaDestino);
	
	public TasaDtoResponse getTasaByParameter(String codMonedaOrigen, String codMonedaDestino);
	
	public TasaDtoResponse getTasaByParameter(String codMonedaOrigen);
	
	public TasaDtoResponse getTasaByParameterCodMonedaDestino(String codMonedaDestino);
	
	public List<TasaDto> findAllDto();
	
	public List<TasaDto> findAllDto(TasaDtoConsulta tasaDtoConsulta);
	
	public TasaDtoResponse findAllDtoResponse();
	
}
