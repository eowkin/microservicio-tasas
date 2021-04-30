package com.bancoexterior.parametros.tasas.controller;


import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.bancoexterior.parametros.tasas.util.LibreriaUtils;
import com.bancoexterior.parametros.tasas.validator.ITasaValidator;
import com.bancoexterior.parametros.tasas.config.Codigos.Constantes;
import com.bancoexterior.parametros.tasas.config.Codigos.Servicios;
import com.bancoexterior.parametros.tasas.dto.TasaDtoResponse;
import com.bancoexterior.parametros.tasas.dto.TasaDtoResponseActualizar;
import com.bancoexterior.parametros.tasas.dto.TasaRequestConsulta;
import com.bancoexterior.parametros.tasas.dto.TasaRequestCrear;
import com.bancoexterior.parametros.tasas.service.ITasaService;




@RestController
@RequestMapping("${microservicio.path.pre}" + "${microservicio.ambiente}")
public class TasaController {

	private static final Logger LOGGER = LogManager.getLogger(TasaController.class);
	
	@Autowired
	private ITasaService tasaService;
	
	
	@Autowired
	private ITasaValidator tasaValidator;
	

	
	
	/**
	 * Nombre: listAllTasas 
	 * Descripcion: Invocar metodo para consultar tasas segun 
	 * los parametros enviados
	 *
	 * @param request     Objeto tipo TasaRequestConsulta
	 * @param requestHTTP Objeto tipo HttpServletRequest
	 * @return ResponseEntity<Object>
	 * @version 1.0
	 * @author Eugenio Owkin
	 * @since 12/04/21
	 */
	
	@PostMapping(path =Servicios.TASASURLV1+"/consultas", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> listAllTasas(@RequestBody TasaRequestConsulta tasaRequestConsulta, 
			HttpServletRequest requestHTTP){
		
		
		
		LOGGER.info(Servicios.TASASCONTROLLERI);
		LOGGER.info(tasaRequestConsulta);
		TasaDtoResponse response;
		HttpStatus estatusCM;
		
		response = tasaService.consultaTasas(tasaRequestConsulta);
		estatusCM = LibreriaUtils.getHttpStatus(response.getResultado().getCodigo().trim());
		LOGGER.info(estatusCM);
		LOGGER.info(response);
		LOGGER.info(Servicios.TASASCONTROLLERF);
		if(response.getResultado().getCodigo().trim().substring(0, 1).equalsIgnoreCase(Constantes.SUBSTRING_COD_OK)) {
			return new ResponseEntity<>(response,estatusCM);
		}else {
		
			return new ResponseEntity<>(response.getResultado(),estatusCM);
		}
	}
	
	/**
	 * Nombre: crearTasa 
	 * Descripcion: Invocar metodo para crear una tasa
	 *
	 * @param request     Objeto tipo TasaRequestCrear
	 * @param  BindingResult result
	 * @param requestHTTP Objeto tipo HttpServletRequest
	 * @return ResponseEntity<Object>
	 * @version 1.0
	 * @author Eugenio Owkin
	 * @since 12/04/21
	 */
	@PostMapping(path =Servicios.TASASURLV1, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Object> crearTasa(@Valid @RequestBody TasaRequestCrear tasaRequestCrear, BindingResult result,
			HttpServletRequest requestHTTP){
		
		LOGGER.info(Servicios.TASASCONTROLLERI);
		LOGGER.info(tasaRequestCrear);
		
		tasaValidator.validarCrear(tasaRequestCrear, result);
		
		TasaDtoResponseActualizar response;
		HttpStatus estatusCM;
		
				
		response = tasaService.save(tasaRequestCrear, requestHTTP);
		LOGGER.info(response);
		estatusCM = LibreriaUtils.getHttpStatus(response.getResultado().getCodigo().trim());
		
		LOGGER.info(estatusCM);
		
		LOGGER.info(Servicios.TASASCONTROLLERF);
		if(response.getResultado().getCodigo().trim().substring(0, 1).equalsIgnoreCase(Constantes.SUBSTRING_COD_OK)) {
			return new ResponseEntity<>(response,estatusCM);
		}else {
			return new ResponseEntity<>(response.getResultado(),estatusCM);
		}
	}
	
	/**
	 * Nombre: actualizarTasa 
	 * Descripcion: Invocar metodo para actualizar una tasa
	 *
	 * @param request     Objeto tipo TasaRequestCrear
	 * @param  BindingResult result
	 * @param requestHTTP Objeto tipo HttpServletRequest
	 * @return ResponseEntity<Object>
	 * @version 1.0
	 * @author Eugenio Owkin
	 * @since 12/04/21
	 */
	@PutMapping(path =Servicios.TASASURLV1, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Object> actualizarTasa(@Valid @RequestBody TasaRequestCrear tasaRequestCrear, BindingResult result,
			HttpServletRequest requestHTTP){
		
		LOGGER.info(Servicios.TASASCONTROLLERI);
		LOGGER.info(tasaRequestCrear);
		
		tasaValidator.validarActualizar(tasaRequestCrear, result);

		TasaDtoResponseActualizar response;
		HttpStatus estatusCM;
		
				
		response = tasaService.actualizar(tasaRequestCrear, requestHTTP);
		estatusCM = LibreriaUtils.getHttpStatus(response.getResultado().getCodigo().trim());
		LOGGER.info(estatusCM);
		LOGGER.info(response);
		LOGGER.info(Servicios.TASASCONTROLLERF);
		if(response.getResultado().getCodigo().trim().substring(0, 1).equalsIgnoreCase(Constantes.SUBSTRING_COD_OK)) {
			return new ResponseEntity<>(response,estatusCM);
		}else {
			return new ResponseEntity<>(response.getResultado(),estatusCM);
		}
	}
	
	
}
