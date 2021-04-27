package com.bancoexterior.parametros.tasas.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bancoexterior.parametros.tasas.repository.IMonedaRepository;

@Service
public class MonedaServiceImpl implements IMonedaService{

	@Autowired
	private IMonedaRepository repo;
	
	
	/**
	 * Nombre: findById 
	 * Descripcion: Invocar metodo para buscar si existe el codMoneda
	 * con la que se quiere crear la tasa.
	 *
	 * @param String     codMoneda
	 * @return boolean
	 * @version 1.0
	 * @author Eugenio Owkin
	 * @since 12/04/21
	 */
	@Override
	public boolean findById(String codMoneda) {
		return repo.existsById(codMoneda);
	}

}
