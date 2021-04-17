package com.bancoexterior.parametros.tasas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bancoexterior.parametros.tasas.dto.TasaDto;
import com.bancoexterior.parametros.tasas.dto.TasaDtoInversa;
import com.bancoexterior.parametros.tasas.entities.Tasa;
import com.bancoexterior.parametros.tasas.entities.TasaPk;


@Repository
public interface ITasaRepository extends JpaRepository<Tasa, TasaPk>{
	//(CASE WHEN t.montoTasa <> 0 THEN 1/t.montoTasa ELSE 0 END)
	//case when monto_tasa <> 0 then 1/monto_tasa else 0 end
	String queryAll2 = "select new com.bancoexterior.parametros.tasas.dto.TasaDtoInversa(t.id.codMonedaOrigen, "
			+ "t.id.codMonedaDestino, t.montoTasa, t.codUsuario, t.fechaModificacion, "
			+ "round((case when t.montoTasa <> 0 then (1/t.montoTasa) else t.montoTasa end),2)) "
			+ " from Tasa t"
			+ " where 1=1";
	
	
	
	
	String queryAll = "select new com.bancoexterior.parametros.tasas.dto.TasaDto(t.id.codMonedaOrigen, t.id.codMonedaDestino, t.montoTasa, t.codUsuario, t.fechaModificacion) "
			+ " from Tasa t"
			+ " where 1=1";
	
	String queryAllParameter = "select new com.bancoexterior.parametros.tasas.dto.TasaDto(t.id.codMonedaOrigen, t.id.codMonedaDestino, t.montoTasa, t.codUsuario, t.fechaModificacion) "
			+ " from Tasa t"
			+ " where t.id.codMonedaOrigen= (case when ?1 is null then t.id.codMonedaOrigen else ?1 end)";

		
		@Query(value = queryAll2)
		public List<TasaDtoInversa> getAll2();
		
		
		@Query(value = queryAll)
		public List<TasaDto> getAll();
		
		//@Modifying 
		@Query(value = queryAllParameter)
		public List<TasaDto> getAll(String codMonedaOrigen);
		
		@Query(value = queryAll + " and t.id.codMonedaOrigen = ?1")
		public List<TasaDto> getTasaByCodMonedaOrigen(String codMonedaOrigen);
		
		@Query(value = queryAll + " and t.id.codMonedaDestino = ?1")
		public List<TasaDto> getTasaByCodMonedaDestino(String codMonedaDestino);
	
		@Query(value = queryAll + " and t.id.codMonedaOrigen = ?1 and t.id.codMonedaDestino = ?2")
		public List<TasaDto> getTasaByCodMonedaOrigenAndCodMonedaDestino(String codMonedaOrigen, String codMonedaDestino);
	
}
