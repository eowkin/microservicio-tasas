package com.bancoexterior.parametros.tasas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bancoexterior.parametros.tasas.config.Codigos.SQLUtils;
import com.bancoexterior.parametros.tasas.dto.TasaDto;
import com.bancoexterior.parametros.tasas.entities.Tasa;
import com.bancoexterior.parametros.tasas.entities.TasaPk;


@Repository
public interface ITasaRepository extends JpaRepository<Tasa, TasaPk>{
	
		
	@Query(value = SQLUtils.SELECTTASA)
	public List<TasaDto> getAll();
	
	@Query(value = SQLUtils.SELECTTASA + " and t.id.codMonedaOrigen = ?1")
	public List<TasaDto> getTasaByCodMonedaOrigen(String codMonedaOrigen);
	
	@Query(value = SQLUtils.SELECTTASA + " and t.id.codMonedaDestino = ?1")
	public List<TasaDto> getTasaByCodMonedaDestino(String codMonedaDestino);
	
	@Query(value = SQLUtils.SELECTTASA + " and t.id.codMonedaOrigen = ?1 and t.id.codMonedaDestino = ?2")
	public List<TasaDto> getTasaByCodMonedaOrigenAndCodMonedaDestino(String codMonedaOrigen, String codMonedaDestino);
			
}
