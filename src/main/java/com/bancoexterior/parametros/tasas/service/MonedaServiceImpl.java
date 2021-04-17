package com.bancoexterior.parametros.tasas.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bancoexterior.parametros.tasas.repository.IMonedaRepository;

@Service
public class MonedaServiceImpl implements IMonedaService{

	@Autowired
	private IMonedaRepository repo;
	
	@Override
	public boolean findById(String codMoneda) {
		return repo.existsById(codMoneda);
	}

}
