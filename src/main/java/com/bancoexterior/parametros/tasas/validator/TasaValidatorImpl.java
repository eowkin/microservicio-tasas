package com.bancoexterior.parametros.tasas.validator;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.bancoexterior.parametros.tasas.exception.CodMonedaNoExistException;
import com.bancoexterior.parametros.tasas.exception.CodMonedasIgualesException;
import com.bancoexterior.parametros.tasas.exception.FieldErrorValidationException;
import com.bancoexterior.parametros.tasas.exception.TasaExistException;
import com.bancoexterior.parametros.tasas.exception.TasaInversaExistException;
import com.bancoexterior.parametros.tasas.exception.TasaNoExistException;
import com.bancoexterior.parametros.tasas.service.IMonedaService;
import com.bancoexterior.parametros.tasas.service.ITasaService;
import com.bancoexterior.parametros.tasas.config.Codigos.CodRespuesta;
import com.bancoexterior.parametros.tasas.dto.TasaDtoRequestCrear;
import com.bancoexterior.parametros.tasas.dto.TasaRequestCrear;
import com.bancoexterior.parametros.tasas.entities.TasaPk;

@Component
public class TasaValidatorImpl implements ITasaValidator{

	@Autowired
	private ITasaService tasaService;
	
	@Autowired
	private IMonedaService monedaService;
	
	
	/**
	 * Nombre: validarCrear 
	 * Descripcion: Invocar metodo para realizar validacion
	 * de los parametros de entrada y demas validaciones 
	 * antes de procesar al endPoint crearTasa.
	 *
	 * @param request     Objeto tipo TasaRequestCrear
	 * @param result     Objeto tipo BindingResult
	 * @return void
	 * @version 1.0
	 * @author Eugenio Owkin
	 * @since 12/04/21
	 */
	@Override
	public void validarCrear(TasaRequestCrear tasaRequestCrear, BindingResult result) {
		
		//Validando los valores de entrada
		if (result.hasErrors()) {
			List<String> errors = result.getFieldErrors().stream().map(FieldError::getDefaultMessage)
					.collect(Collectors.toList());
			throw new FieldErrorValidationException(errors.get(0));			
		}
		
		TasaDtoRequestCrear tasaDtoRequestCrear = tasaRequestCrear.getTasaDtoRequestCrear(); 
	    String codMonedaOrigen = tasaDtoRequestCrear.getCodMonedaOrigen();
	    String codMonedaDestino = tasaDtoRequestCrear.getCodMonedaDestino();
	    TasaPk id = new TasaPk(codMonedaOrigen, codMonedaDestino);
	    
	    //Validando que las monedas sean iguales
	    if (codMonedaOrigen.equals(codMonedaDestino)) {
	    	throw new CodMonedasIgualesException(CodRespuesta.CME2004);
		}
	    
	    //Validando que ya exista la tasa
	    if (tasaService.existsById(id)) {
	    	throw new TasaExistException(CodRespuesta.CME2001);
	    }
		
	    
	    id.setCodMonedaDestino(codMonedaOrigen);
	    id.setCodMonedaOrigen(codMonedaDestino);
	    //Validando si ya exista la tasa inversa entre monedas
	    if (tasaService.existsById(id)) {
	    	throw new TasaInversaExistException(CodRespuesta.CME2006);
	    }
	    
	    //Validando si  exista el codMonedaOrigen
	    if (!monedaService.findById(codMonedaOrigen)) {
	    	throw new CodMonedaNoExistException(CodRespuesta.CME2003);
	    }
	    
	    //Validando si  exista el codMonedaDestino
	    if (!monedaService.findById(codMonedaDestino)) {
	    	throw new CodMonedaNoExistException(CodRespuesta.CME2005);
	    }
	}

	
	/**
	 * Nombre: validarActualizar 
	 * Descripcion: Invocar metodo para realizar validacion
	 * de los parametros de entrada y demas validaciones 
	 * antes de procesar el endPoint actualizarMoneda.
	 *
	 * @param request     Objeto tipo TasaRequestCrear
	 * @param result     Objeto tipo BindingResult
	 * @return void
	 * @version 1.0
	 * @author Eugenio Owkin
	 * @since 12/04/21
	 */
	@Override
	public void validarActualizar(TasaRequestCrear tasaRequestCrear, BindingResult result) {
		//Validando los valores de entrada
		if (result.hasErrors()) {
			List<String> errors = result.getFieldErrors().stream().map(FieldError::getDefaultMessage)
					.collect(Collectors.toList());
			throw new FieldErrorValidationException(errors.get(0));			
		}
		
		TasaDtoRequestCrear tasaDtoRequestCrear = tasaRequestCrear.getTasaDtoRequestCrear(); 
	    
	    String codMonedaOrigen = tasaDtoRequestCrear.getCodMonedaOrigen();
		
	    String codMonedaDestino = tasaDtoRequestCrear.getCodMonedaDestino();
	  
	    
	    TasaPk id = new TasaPk(codMonedaOrigen, codMonedaDestino);
	    //Validando que exista la tasa
	    if (!tasaService.existsById(id)) {
	    	throw new TasaNoExistException(CodRespuesta.CME2000);
	    }
	}

}
