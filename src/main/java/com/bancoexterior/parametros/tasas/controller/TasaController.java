package com.bancoexterior.parametros.tasas.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.bancoexterior.parametros.tasas.util.Utils;
import com.bancoexterior.parametros.tasas.response.ResponseBad;
import com.bancoexterior.parametros.tasas.config.Codigos.CodRespuesta;
import com.bancoexterior.parametros.tasas.config.Codigos.Constantes;
import com.bancoexterior.parametros.tasas.config.Codigos.Servicios;
import com.bancoexterior.parametros.tasas.dto.TasaDto;
import com.bancoexterior.parametros.tasas.dto.TasaDtoRequestActualizar;
import com.bancoexterior.parametros.tasas.dto.TasaDtoRequestCrear;
import com.bancoexterior.parametros.tasas.dto.TasaDtoResponse;
import com.bancoexterior.parametros.tasas.dto.TasaDtoResponseActualizar;
import com.bancoexterior.parametros.tasas.dto.TasaRequestActualizar;
import com.bancoexterior.parametros.tasas.dto.TasaRequestConsulta;
import com.bancoexterior.parametros.tasas.dto.TasaRequestCrear;
import com.bancoexterior.parametros.tasas.entities.TasaPk;
import com.bancoexterior.parametros.tasas.service.IMonedaService;
import com.bancoexterior.parametros.tasas.service.ITasaService;




@RestController
@RequestMapping("${microservicio.path.pre}" + "${microservicio.ambiente}")
public class TasaController {

	private static final Logger LOGGER = LogManager.getLogger(TasaController.class);
	
	@Autowired
	private ITasaService tasaService;
	
	@Autowired
	private IMonedaService monedaService;
	
	
	@Autowired
	private Environment env;
	
	
	/**
	 * Nombre: listAllTasas 
	 * Descripcion: Invocar metodo para consultar tasas parametros
	 *
	 * @param request     Objeto tipo TasaRequestConsulta
	 * @param requestHTTP Objeto tipo HttpServletRequest
	 * @return ResponseEntity<Object>
	 * @version 1.0
	 * @author Eugenio Owkin
	 * @since 16/03/21
	 */
	
	@PostMapping(path =Servicios.TASASURLV1+"/consultas", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> listAllTasas(@RequestBody TasaRequestConsulta tasaRequestConsulta, 
			HttpServletRequest requestHTTP){
		
		
		
		LOGGER.info(Servicios.TASASCONTROLLERI);
		LOGGER.info(tasaRequestConsulta);
		TasaDtoResponse response;
		HttpStatus estatusCM;
		
		response = tasaService.consultaTasas(tasaRequestConsulta);
		estatusCM = Utils.getHttpStatus(response.getResultado().getCodigo().trim());
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
	 * @since 16/03/21
	 */
	@PostMapping(path =Servicios.TASASURLV1, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Object> crearTasa(@Valid @RequestBody TasaRequestCrear tasaRequestCrear, BindingResult result,
			HttpServletRequest requestHTTP){
		
		LOGGER.info(Servicios.TASASCONTROLLERI);
		LOGGER.info(tasaRequestCrear);
		//Validando datos de entrada
	    if (result.hasErrors()) {
			ResponseBad responseBad = new ResponseBad();	
			List<String> errors = result
	                .getFieldErrors()
	                .stream()
	                .map(FieldError::getDefaultMessage)
	                .collect(Collectors.toList());
			LOGGER.error(errors);
			
			HttpStatus httpStatusError = Utils.getHttpStatus(errors.get(0));
			responseBad.getResultadoBAD().setCodigo(errors.get(0));
	    	responseBad.getResultadoBAD().setDescripcion(env.getProperty(Constantes.RES+errors.get(0),errors.get(0)));
			return new ResponseEntity<>(responseBad, httpStatusError);
			
		}
		
	    TasaDtoRequestCrear tasaDtoRequestCrear = tasaRequestCrear.getTasaDtoRequestCrear(); 
	    
	    String codMonedaOrigen = tasaDtoRequestCrear.getCodMonedaOrigen();
		
	    String codMonedaDestino = tasaDtoRequestCrear.getCodMonedaDestino();
	  //Validando que las monedas sean iguales
	    if (codMonedaOrigen.equals(codMonedaDestino)) {
	    	ResponseBad responseBad = new ResponseBad();
	    	HttpStatus httpStatusError = Utils.getHttpStatus(CodRespuesta.CME2004);
			responseBad.getResultadoBAD().setCodigo(CodRespuesta.CME2004);
	    	responseBad.getResultadoBAD().setDescripcion(env.getProperty(Constantes.RES+CodRespuesta.CME2004,CodRespuesta.CME2004));
			return new ResponseEntity<>(responseBad, httpStatusError);
		}
	    
	    TasaPk id = new TasaPk(codMonedaOrigen, codMonedaDestino);
	    //Validando que ya exista la tasa
	    if (tasaService.existsById(id)) {
	    	ResponseBad responseBad = new ResponseBad();
	    	HttpStatus httpStatusError = Utils.getHttpStatus(CodRespuesta.CME2001);
			responseBad.getResultadoBAD().setCodigo(CodRespuesta.CME2001);
	    	responseBad.getResultadoBAD().setDescripcion(env.getProperty(Constantes.RES+CodRespuesta.CME2001,CodRespuesta.CME2001));
			return new ResponseEntity<>(responseBad, httpStatusError);
		}
	    
	    id.setCodMonedaDestino(codMonedaOrigen);
	    id.setCodMonedaOrigen(codMonedaDestino);
	    //Validando si ya exista la tasa inversa entre monedas
	    if (tasaService.existsById(id)) {
	    	ResponseBad responseBad = new ResponseBad();
	    	HttpStatus httpStatusError = Utils.getHttpStatus(CodRespuesta.CME2006);
			responseBad.getResultadoBAD().setCodigo(CodRespuesta.CME2006);
	    	responseBad.getResultadoBAD().setDescripcion(env.getProperty(Constantes.RES+CodRespuesta.CME2006,CodRespuesta.CME2006));
			return new ResponseEntity<>(responseBad, httpStatusError);
		}
	   //Validando si  exista el codMonedaOrigen
	    if (!monedaService.findById(codMonedaOrigen)) {
	    	ResponseBad responseBad = new ResponseBad();
	    	HttpStatus httpStatusError = Utils.getHttpStatus(CodRespuesta.CME2003);
			responseBad.getResultadoBAD().setCodigo(CodRespuesta.CME2003);
	    	responseBad.getResultadoBAD().setDescripcion(env.getProperty(Constantes.RES+CodRespuesta.CME2003,CodRespuesta.CME2003));
			return new ResponseEntity<>(responseBad, httpStatusError);
	    }
	  //Validando si  exista el codMonedaDestino
	    if (!monedaService.findById(codMonedaDestino)) {
	    	ResponseBad responseBad = new ResponseBad();
	    	HttpStatus httpStatusError = Utils.getHttpStatus(CodRespuesta.CME2005);
			responseBad.getResultadoBAD().setCodigo(CodRespuesta.CME2005);
	    	responseBad.getResultadoBAD().setDescripcion(env.getProperty(Constantes.RES+CodRespuesta.CME2005,CodRespuesta.CME2005));
			return new ResponseEntity<>(responseBad, httpStatusError);
	    }
		
		
		TasaDtoResponseActualizar response;
		HttpStatus estatusCM;
		
				
		response = tasaService.save(tasaRequestCrear, requestHTTP);
		LOGGER.info(response);
		estatusCM = Utils.getHttpStatus(response.getResultado().getCodigo().trim());
		
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
	 * @since 16/03/21
	 */
	@PutMapping(path =Servicios.TASASURLV1, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Object> actualizarTasa(@Valid @RequestBody TasaRequestCrear tasaRequestCrear, BindingResult result,
			HttpServletRequest requestHTTP){
		
		
		
		
		
		LOGGER.info(Servicios.TASASCONTROLLERI);
		LOGGER.info(tasaRequestCrear);
		
		
		
		
		//Validando datos de entrada
	    if (result.hasErrors()) {
			ResponseBad responseBad = new ResponseBad();	
			List<String> errors = result
	                .getFieldErrors()
	                .stream()
	                .map(FieldError::getDefaultMessage)
	                .collect(Collectors.toList());
			LOGGER.error(errors);
			
			HttpStatus httpStatusError = Utils.getHttpStatus(errors.get(0));
			responseBad.getResultadoBAD().setCodigo(errors.get(0));
	    	responseBad.getResultadoBAD().setDescripcion(env.getProperty(Constantes.RES+errors.get(0),errors.get(0)));
			return new ResponseEntity<>(responseBad, httpStatusError);
			
		}
		
	    TasaDtoRequestCrear tasaDtoRequestCrear = tasaRequestCrear.getTasaDtoRequestCrear(); 
	    
	    String codMonedaOrigen = tasaDtoRequestCrear.getCodMonedaOrigen();
		
	    String codMonedaDestino = tasaDtoRequestCrear.getCodMonedaDestino();
	  
	    
	    TasaPk id = new TasaPk(codMonedaOrigen, codMonedaDestino);
	    //Validando que exista la tasa
	    if (!tasaService.existsById(id)) {
	    	ResponseBad responseBad = new ResponseBad();
	    	HttpStatus httpStatusError = Utils.getHttpStatus(CodRespuesta.CME2000);
			responseBad.getResultadoBAD().setCodigo(CodRespuesta.CME2000);
	    	responseBad.getResultadoBAD().setDescripcion(env.getProperty(Constantes.RES+CodRespuesta.CME2000,CodRespuesta.CME2000));
			return new ResponseEntity<>(responseBad, httpStatusError);
		}		
		
	    TasaRequestActualizar tasaRequestActualizar = new TasaRequestActualizar();
	    tasaRequestActualizar.setIdSesionMR(tasaRequestCrear.getIdSesionMR());
	    tasaRequestActualizar.setIdUsuarioMR(tasaRequestCrear.getIdUsuarioMR());
	    tasaRequestActualizar.setCodUsuarioMR(tasaRequestCrear.getCodUsuarioMR());
	    tasaRequestActualizar.setCanalCM(tasaRequestCrear.getCanalCM());
	    
	    TasaDto tasaDto = tasaService.findByIdDto(id);
	    TasaDtoRequestActualizar tasaDtoRequestActualizar = new TasaDtoRequestActualizar();
	    tasaDtoRequestActualizar.setCodMonedaOrigen(tasaDto.getCodMonedaOrigen());
	    tasaDtoRequestActualizar.setCodMonedaDestino(tasaDto.getCodMonedaDestino());
	    tasaDtoRequestActualizar.setCodUsuario(tasaDto.getCodUsuario());
	    tasaDtoRequestActualizar.setMontoTasa(tasaDtoRequestCrear.getMontoTasa());
	    tasaDtoRequestActualizar.setFechaModificacion(tasaDto.getFechaModificacion());
	    tasaRequestActualizar.setTasaDtoRequestActualizar(tasaDtoRequestActualizar);
	    
	    
		TasaDtoResponseActualizar response;
		HttpStatus estatusCM;
		
				
		response = tasaService.actualizar(tasaRequestActualizar, requestHTTP);
		estatusCM = Utils.getHttpStatus(response.getResultado().getCodigo().trim());
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
