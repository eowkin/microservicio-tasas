package com.bancoexterior.parametros.tasas.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.bancoexterior.parametros.tasas.dto.TasaDto;
import com.bancoexterior.parametros.tasas.dto.TasaDtoConsulta;
import com.bancoexterior.parametros.tasas.dto.TasaDtoResponse;
import com.bancoexterior.parametros.tasas.dto.TasaDtoResponseActualizar;
import com.bancoexterior.parametros.tasas.dto.TasaRequestActualizar;
import com.bancoexterior.parametros.tasas.dto.TasaRequestConsulta;
import com.bancoexterior.parametros.tasas.dto.TasaRequestCrear;
import com.bancoexterior.parametros.tasas.entities.TasaPk;







public interface ITasaService {

	
	
	
	public List<TasaDto> findAllDto(TasaDtoConsulta tasaDtoConsulta);
	
	public TasaDtoResponse consultaTasas(TasaRequestConsulta tasaRequestConsulta);
	
	public boolean existsById(TasaPk id);
	
	public TasaDto findByIdDto(TasaPk id);
	
	public TasaDtoResponseActualizar save(TasaRequestCrear tasaRequestCrear, HttpServletRequest requestHTTP);
	
	public TasaDtoResponseActualizar actualizar(TasaRequestActualizar tasaRequestActualizar);
}
