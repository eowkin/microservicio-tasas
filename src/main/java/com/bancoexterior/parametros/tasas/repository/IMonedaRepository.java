package com.bancoexterior.parametros.tasas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bancoexterior.parametros.tasas.entities.Moneda;

@Repository
public interface IMonedaRepository extends JpaRepository<Moneda, String>{

	
	
}
