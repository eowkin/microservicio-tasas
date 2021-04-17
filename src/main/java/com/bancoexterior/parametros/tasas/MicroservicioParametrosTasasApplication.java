package com.bancoexterior.parametros.tasas;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.bancoexterior.parametros.tasas.dto.TasaDtoInversa;
import com.bancoexterior.parametros.tasas.entities.Tasa;
import com.bancoexterior.parametros.tasas.repository.ITasaRepository;
import com.bancoexterior.parametros.tasas.service.ITasaService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class MicroservicioParametrosTasasApplication implements CommandLineRunner{

	
	@Autowired
	ITasaService service;
	
	public static void main(String[] args) {
		SpringApplication.run(MicroservicioParametrosTasasApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		log.info("epale");
		List<TasaDtoInversa> list =(List<TasaDtoInversa>) service.findAllInversa();
		for (Object object : list) {
			log.info("object: "+object.toString());
			
		}
		
	}

}
