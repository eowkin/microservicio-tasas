package com.bancoexterior.parametros.tasas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bancoexterior.parametros.tasas.dto.TasaDto;
import com.bancoexterior.parametros.tasas.entities.Tasa;
import com.bancoexterior.parametros.tasas.entities.TasaPk;


@Repository
public interface ITasaRepository extends JpaRepository<Tasa, TasaPk>{

	String queryNativo1 = "SELECT cod_moneda_origen, cod_moneda_destino, monto_tasa, cod_usuario, fecha_modificacion\r\n"
			+ "FROM \"Convenio1\".\"Tasas\"\r\n";
	
	String queryNativo2 = "SELECT cod_moneda_origen, cod_moneda_destino, monto_tasa, cod_usuario, fecha_modificacion\r\n"
			+ "FROM \"Convenio1\".\"Tasas\"\r\n"
			+"where cod_moneda_origen= (case when :codMonedaOrigen is null then cod_moneda_origen else :codMonedaOrigen end)\r\n";
	
	String queryNativo21 = "SELECT cod_moneda_origen, cod_moneda_destino, monto_tasa, cod_usuario, fecha_modificacion\r\n"
			+ "FROM \"Convenio1\".\"Tasas\"\r\n"
			+"where cod_moneda_origen= (case when ?1 is null then cod_moneda_origen else ?1 end)\r\n";
	
	String queryNativo = "SELECT cod_moneda_origen, cod_moneda_destino, monto_tasa, cod_usuario, fecha_modificacion\r\n"
			+ "FROM \"Convenio1\".\"Tasas\"\r\n"
			+ "where cod_moneda_origen= (case when :codMonedaOrigen is null then cod_moneda_origen else :codMonedaOrigen end)\r\n"
			+ "and cod_moneda_destino = (case when :codMonedaDestino is null then cod_moneda_destino else :codMonedaDestino end);";
	
	String queryAll = "select new com.bancoexterior.parametros.tasas.dto.TasaDto(t.id.codMonedaOrigen, t.id.codMonedaDestino, t.montoTasa, t.codUsuario, t.fechaModificacion) "
			+ " from Tasa t"
			+ " where 1=1";
	
	String queryAllParameter = "select new com.bancoexterior.parametros.tasas.dto.TasaDto(t.id.codMonedaOrigen, t.id.codMonedaDestino, t.montoTasa, t.codUsuario, t.fechaModificacion) "
			+ " from Tasa t"
			+ " where t.id.codMonedaOrigen= (case when ?1 is null then t.id.codMonedaOrigen else ?1 end)";

		@Query(value = queryNativo,
				nativeQuery = true)
		List<Tasa> getQueryNativo(@Param("codMonedaOrigen") String codMonedaOrigen, @Param("codMonedaDestino") String codMonedaDestino);
	
		@Query(value = queryNativo1,
				nativeQuery = true)
		List<Tasa> getQueryNativo1();
	
		@Query(value = queryNativo2,
				nativeQuery = true)
		List<Tasa> getQueryNativo2(@Param("codMonedaOrigen") String codMonedaOrigen);
		
		@Query(value = queryNativo21,
				nativeQuery = true)
		List<Tasa> getQueryNativo21(String codMonedaOrigen);
	
		
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
