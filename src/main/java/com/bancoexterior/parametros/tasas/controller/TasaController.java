package com.bancoexterior.parametros.tasas.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;

import com.bancoexterior.parametros.tasas.util.Utils;
import com.bancoexterior.parametros.tasas.response.ResponseBad;
import com.bancoexterior.parametros.tasas.config.Codigos.Constantes;
import com.bancoexterior.parametros.tasas.config.Codigos.Servicios;
import com.bancoexterior.parametros.tasas.dto.DatosPrueba;
import com.bancoexterior.parametros.tasas.dto.DatosRequestConsulta;
import com.bancoexterior.parametros.tasas.dto.TasaDto;
import com.bancoexterior.parametros.tasas.dto.TasaDtoRequestActualizar;
import com.bancoexterior.parametros.tasas.dto.TasaDtoRequestCrear;
import com.bancoexterior.parametros.tasas.dto.TasaDtoResponse;
import com.bancoexterior.parametros.tasas.dto.TasaDtoResponseActualizar;
import com.bancoexterior.parametros.tasas.dto.TasaRequestActualizar;
import com.bancoexterior.parametros.tasas.dto.TasaRequestConsulta;
import com.bancoexterior.parametros.tasas.dto.TasaRequestCrear;
import com.bancoexterior.parametros.tasas.entities.Tasa;
import com.bancoexterior.parametros.tasas.entities.TasaPk;
import com.bancoexterior.parametros.tasas.service.IMonedaService;
import com.bancoexterior.parametros.tasas.service.ITasaService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
//@RequestMapping("/api/des/v1/parametros/tasas")
@RequestMapping("${microservicio.path.pre}" + "${microservicio.ambiente}")
public class TasaController {

	@Autowired
	private ITasaService tasaService;
	
	@Autowired
	private IMonedaService monedaService;
	
	
	@Autowired
	private Environment env;
	
	@GetMapping(path =Servicios.TASASURLV1+"/prueba1")
	public List<Tasa> findAll(){
		return tasaService.findAll();
	}
	
	
	@GetMapping(path =Servicios.TASASURLV1+"/prueba2")
	public List<TasaDto> findAllTasas(){
		return tasaService.findAllDto();
	}
	
	
	@GetMapping(path =Servicios.TASASURLV1, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> listAllTasas(@RequestBody TasaRequestConsulta tasaRequestConsulta, 
			HttpServletRequest requestHTTP){
		
		log.info("[==== INICIO Convenio n° 1 Tasa - Controller ====]");
		log.info("tasasRequest: " + tasaRequestConsulta);
		TasaDtoResponse response;
		HttpStatus estatusCM;
		
		response = tasaService.consultaTasas(tasaRequestConsulta);
		estatusCM = Utils.getHttpStatus(response.getResultado().getCodigo().trim());
		//TasaDtoResponse response = tasaService.findAllDtoResponse();
		//log.info("response: "+response);
		log.info("[==== FIN Convenio n° 1 Monedas - Controller ====]");
		log.info("estatusCM: "+estatusCM);
		log.info("response: "+response);
		log.info("[==== FIN Convenio n° 1 Monedas - Controller ====]");
		if(response.getResultado().getCodigo().trim().substring(0, 1).equalsIgnoreCase(Constantes.SUBSTRING_COD_OK)) {
			return new ResponseEntity<>(response,estatusCM);
		}else {
		
			return new ResponseEntity<>(response.getResultado(),estatusCM);
		}
	}
	
	@PostMapping(path =Servicios.TASASURLV1, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Object> crearTasa(@Valid @RequestBody TasaRequestCrear tasaRequestCrear, BindingResult result, WebRequest webRequest,
			HttpServletRequest requestHTTP){
		
		log.info("[==== INICIO Convenio n° 1 Crear Tasa - Controller ====]");
		log.info("tasaRequestCrear: " + tasaRequestCrear);
		//Validando datos de entrada
	    if (result.hasErrors()) {
			ResponseBad responseBad = new ResponseBad();	
			List<String> errors = result
	                .getFieldErrors()
	                .stream()
	                .map(FieldError::getDefaultMessage)
	                .collect(Collectors.toList());
	    	log.info("errors: "+errors);
			
			HttpStatus httpStatusError = Utils.getHttpStatus(errors.get(0));
			log.info("httpStatusError: "+httpStatusError);
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
	    	HttpStatus httpStatusError = Utils.getHttpStatus("2004");
			log.info("httpStatusError: "+httpStatusError);
			responseBad.getResultadoBAD().setCodigo("2004");
	    	responseBad.getResultadoBAD().setDescripcion(env.getProperty(Constantes.RES+"2004","2004"));
			return new ResponseEntity<>(responseBad, httpStatusError);
		}
	    
	    TasaPk id = new TasaPk(codMonedaOrigen, codMonedaDestino);
	    //Validando que ya exista la tasa
	    if (tasaService.existsById(id)) {
	    	ResponseBad responseBad = new ResponseBad();
	    	HttpStatus httpStatusError = Utils.getHttpStatus("2006");
			log.info("httpStatusError: "+httpStatusError);
			responseBad.getResultadoBAD().setCodigo("2006");
	    	responseBad.getResultadoBAD().setDescripcion(env.getProperty(Constantes.RES+"2006","2006"));
			return new ResponseEntity<>(responseBad, httpStatusError);
		}
	    
	    id.setCodMonedaDestino(codMonedaOrigen);
	    id.setCodMonedaOrigen(codMonedaDestino);
	    //Validando si ya exista la tasa inversa entre monedas
	    if (tasaService.existsById(id)) {
	    	ResponseBad responseBad = new ResponseBad();
	    	HttpStatus httpStatusError = Utils.getHttpStatus("2007");
			log.info("httpStatusError: "+httpStatusError);
			responseBad.getResultadoBAD().setCodigo("2007");
	    	responseBad.getResultadoBAD().setDescripcion(env.getProperty(Constantes.RES+"2007","2007"));
			return new ResponseEntity<>(responseBad, httpStatusError);
		}
	    
	    if (!monedaService.findById(codMonedaOrigen)) {
	    	ResponseBad responseBad = new ResponseBad();
	    	HttpStatus httpStatusError = Utils.getHttpStatus("2003");
			log.info("httpStatusError: "+httpStatusError);
			responseBad.getResultadoBAD().setCodigo("2003");
	    	responseBad.getResultadoBAD().setDescripcion(env.getProperty(Constantes.RES+"2003","2003"));
			return new ResponseEntity<>(responseBad, httpStatusError);
	    }
	    
	    if (!monedaService.findById(codMonedaDestino)) {
	    	ResponseBad responseBad = new ResponseBad();
	    	HttpStatus httpStatusError = Utils.getHttpStatus("2005");
			log.info("httpStatusError: "+httpStatusError);
			responseBad.getResultadoBAD().setCodigo("2005");
	    	responseBad.getResultadoBAD().setDescripcion(env.getProperty(Constantes.RES+"2005","2005"));
			return new ResponseEntity<>(responseBad, httpStatusError);
	    }
		
		
		TasaDtoResponseActualizar response;
		HttpStatus estatusCM;
		
				
		response = tasaService.save(tasaRequestCrear);
		log.info("response de regrso: "+response);
		estatusCM = Utils.getHttpStatus(response.getResultado().getCodigo().trim());
		log.info("[==== FIN Convenio n° 1 Monedas - Controller ====]");
		log.info("estatusCM: "+estatusCM);
		log.info("response: "+response);
		log.info("[==== FIN Convenio n° 1 Monedas - Controller ====]");
		if(response.getResultado().getCodigo().trim().substring(0, 1).equalsIgnoreCase(Constantes.SUBSTRING_COD_OK)) {
			log.info("se fue por aqui, buena respuesta");
			return new ResponseEntity<>(response,estatusCM);
		}else {
			log.info("se fue por aqui");
			return new ResponseEntity<>(response.getResultado(),estatusCM);
		}
	}
	
	@PutMapping(path =Servicios.TASASURLV1, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Object> actualizarTasa(@Valid @RequestBody TasaRequestCrear tasaRequestCrear, BindingResult result,
			HttpServletRequest requestHTTP){
		
		log.info("[==== INICIO Convenio n° 1 Actualizar Tasa - Controller ====]");
		log.info("tasaRequestCrear: " + tasaRequestCrear);
		//Validando datos de entrada
	    if (result.hasErrors()) {
			ResponseBad responseBad = new ResponseBad();	
			List<String> errors = result
	                .getFieldErrors()
	                .stream()
	                .map(FieldError::getDefaultMessage)
	                .collect(Collectors.toList());
	    	log.info("errors: "+errors);
			
			HttpStatus httpStatusError = Utils.getHttpStatus(errors.get(0));
			log.info("httpStatusError: "+httpStatusError);
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
	    	HttpStatus httpStatusError = Utils.getHttpStatus("2008");
			log.info("httpStatusError: "+httpStatusError);
			responseBad.getResultadoBAD().setCodigo("2008");
	    	responseBad.getResultadoBAD().setDescripcion(env.getProperty(Constantes.RES+"2008","2008"));
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
		
				
		response = tasaService.actualizar(tasaRequestActualizar);
		log.info("response de regrso: "+response);
		estatusCM = Utils.getHttpStatus(response.getResultado().getCodigo().trim());
		log.info("[==== FIN Convenio n° 1 Monedas - Controller ====]");
		log.info("estatusCM: "+estatusCM);
		log.info("response: "+response);
		log.info("[==== FIN Convenio n° 1 Monedas - Controller ====]");
		if(response.getResultado().getCodigo().trim().substring(0, 1).equalsIgnoreCase(Constantes.SUBSTRING_COD_OK)) {
			log.info("se fue por aqui, buena respuesta");
			return new ResponseEntity<>(response,estatusCM);
		}else {
			log.info("se fue por aqui");
			return new ResponseEntity<>(response.getResultado(),estatusCM);
		}
	}
	
	@PostMapping(path =Servicios.TASASURLV1+"create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Object> crearTasa2(@RequestBody @Valid TasaRequestCrear tasaRequestCrear,
			HttpServletRequest requestHTTP){
		
		log.info("[==== INICIO Convenio n° 1 Crear Tasa - Controller ====]");
		log.info("tasaRequestCrear: " + tasaRequestCrear);
		
		
	    TasaDtoRequestCrear tasaDtoRequestCrear = tasaRequestCrear.getTasaDtoRequestCrear(); 
	    
	    String codMonedaOrigen = tasaDtoRequestCrear.getCodMonedaOrigen();
		
	    String codMonedaDestino = tasaDtoRequestCrear.getCodMonedaDestino();
		
	    if (codMonedaOrigen.equals(codMonedaDestino)) {
	    	ResponseBad responseBad = new ResponseBad();
	    	HttpStatus httpStatusError = Utils.getHttpStatus("2004");
			log.info("httpStatusError: "+httpStatusError);
			responseBad.getResultadoBAD().setCodigo("2004");
	    	responseBad.getResultadoBAD().setDescripcion(env.getProperty(Constantes.RES+"2004","2004"));
			return new ResponseEntity<>(responseBad, httpStatusError);
		}
	    
	    
	    if (monedaService.findById(codMonedaOrigen)) {
			
	    	
	    	
		}
		
		
		TasaDtoResponse response;
		HttpStatus estatusCM;
		
		return null;
		/*
		response = tasaService.consultaTasas(tasaRequestConsulta);
		estatusCM = Utils.getHttpStatus(response.getResultado().getCodigo().trim());
		log.info("[==== FIN Convenio n° 1 Monedas - Controller ====]");
		log.info("estatusCM: "+estatusCM);
		log.info("response: "+response);
		log.info("[==== FIN Convenio n° 1 Monedas - Controller ====]");
		if(response.getResultado().getCodigo().trim().substring(0, 1).equalsIgnoreCase(Constantes.SUBSTRING_COD_OK)) {
			return new ResponseEntity<>(response,estatusCM);
		}else {
		
			return new ResponseEntity<>(response.getResultado(),estatusCM);
		}*/
	}
	
	
	
	//@GetMapping("/prueba3")
	@GetMapping(path =Servicios.TASASURLV1+"/todas", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getAllTasasResponse(@Valid @RequestBody DatosRequestConsulta datosRequestConsulta, BindingResult result, 
			HttpServletRequest requestHTTP){
		
		log.info("[==== INICIO Convenio n° 1 Tasa - Controller ====]");
		log.info("datosRequestConsulta: " + datosRequestConsulta);
		if (result.hasErrors()) {
			
			ResponseBad responseBad = new ResponseBad();	
			List<String> errors = result
	                .getFieldErrors()
	                .stream()
	                .map(FieldError::getDefaultMessage)
	                .collect(Collectors.toList());
	    	log.info("errors: "+errors);
			
			HttpStatus httpStatusError = Utils.getHttpStatus(errors.get(0));
			log.info("httpStatusError: "+httpStatusError);
			responseBad.getResultadoBAD().setCodigo(errors.get(0));
	    	responseBad.getResultadoBAD().setDescripcion(env.getProperty(Constantes.RES+errors.get(0),errors.get(0)));
			return new ResponseEntity(responseBad, httpStatusError);

		}
		TasaDtoResponse response = tasaService.findAllDtoResponse();
		//log.info("response: "+response);
		log.info("[==== FIN Convenio n° 1 Monedas - Controller ====]");
		return ResponseEntity.ok(response);
	}
	
	//@GetMapping("/codMonedaOrigen/{codMonedaOrigen}/codMonedaDestino/{codMonedaDestino}")
	@GetMapping(path =Servicios.TASASPARAMETERURLV1, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getTasasResponse(@Valid @RequestBody DatosRequestConsulta datosRequestConsulta, BindingResult result, @PathVariable String codMonedaOrigen, @PathVariable String codMonedaDestino,
			HttpServletRequest requestHTTP){
		
		log.info("[==== INICIO Convenio n° 1 Tasa - Controller ====]");
		log.info("datosRequestConsulta: " + datosRequestConsulta);
		log.info("codMonedaOrigen: " + codMonedaOrigen);
		log.info("codMonedaDestino: " + codMonedaDestino);
		if (result.hasErrors()) {
			
			ResponseBad responseBad = new ResponseBad();	
			List<String> errors = result
	                .getFieldErrors()
	                .stream()
	                .map(FieldError::getDefaultMessage)
	                .collect(Collectors.toList());
	    	log.info("errors: "+errors);
			
			HttpStatus httpStatusError = Utils.getHttpStatus(errors.get(0));
			log.info("httpStatusError: "+httpStatusError);
			responseBad.getResultadoBAD().setCodigo(errors.get(0));
	    	responseBad.getResultadoBAD().setDescripcion(env.getProperty(Constantes.RES+errors.get(0),errors.get(0)));
			return new ResponseEntity(responseBad, httpStatusError);

		}
		TasaDtoResponse response = tasaService.getTasaByParameter(codMonedaOrigen, codMonedaDestino);
		log.info("response: "+response);
		log.info("[==== FIN Convenio n° 1 Monedas - Controller ====]");
		return ResponseEntity.ok(response);
	}
	
	@GetMapping(path =Servicios.TASASPARAMETERCODMONEDAORIGENURLV1, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getTasasByCodMonedaOrigenResponse(@Valid @RequestBody DatosRequestConsulta datosRequestConsulta, BindingResult result, @PathVariable String codMonedaOrigen, 
			HttpServletRequest requestHTTP){
		
		log.info("[==== INICIO Convenio n° 1 Tasa - Controller ====]");
		log.info("datosRequestConsulta: " + datosRequestConsulta);
		log.info("codMonedaOrigen: " + codMonedaOrigen);
		if (result.hasErrors()) {
			
			ResponseBad responseBad = new ResponseBad();	
			List<String> errors = result
	                .getFieldErrors()
	                .stream()
	                .map(FieldError::getDefaultMessage)
	                .collect(Collectors.toList());
	    	log.info("errors: "+errors);
			
			HttpStatus httpStatusError = Utils.getHttpStatus(errors.get(0));
			log.info("httpStatusError: "+httpStatusError);
			responseBad.getResultadoBAD().setCodigo(errors.get(0));
	    	responseBad.getResultadoBAD().setDescripcion(env.getProperty(Constantes.RES+errors.get(0),errors.get(0)));
			return new ResponseEntity(responseBad, httpStatusError);

		}
		
		TasaDtoResponse response = tasaService.getTasaByParameter(codMonedaOrigen);
		log.info("response: "+response);
		log.info("[==== FIN Convenio n° 1 Monedas - Controller ====]");
		return ResponseEntity.ok(response);
	}
	
	@GetMapping(path =Servicios.TASASPARAMETERCODMONEDADESTINOURLV1, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getTasasByCodMonedaDestinoResponse(@Valid @RequestBody DatosRequestConsulta datosRequestConsulta, BindingResult result, @PathVariable String codMonedaDestino, 
			HttpServletRequest requestHTTP){
		
		log.info("[==== INICIO Convenio n° 1 Tasa - Controller ====]");
		log.info("datosRequestConsulta: " + datosRequestConsulta);
		log.info("codMonedaOrigen: " + codMonedaDestino);
		if (result.hasErrors()) {
			
			ResponseBad responseBad = new ResponseBad();	
			List<String> errors = result
	                .getFieldErrors()
	                .stream()
	                .map(FieldError::getDefaultMessage)
	                .collect(Collectors.toList());
	    	log.info("errors: "+errors);
			
			HttpStatus httpStatusError = Utils.getHttpStatus(errors.get(0));
			log.info("httpStatusError: "+httpStatusError);
			responseBad.getResultadoBAD().setCodigo(errors.get(0));
	    	responseBad.getResultadoBAD().setDescripcion(env.getProperty(Constantes.RES+errors.get(0),errors.get(0)));
			return new ResponseEntity(responseBad, httpStatusError);

		}

		
		TasaDtoResponse response = tasaService.getTasaByParameterCodMonedaDestino(codMonedaDestino);
		log.info("response: "+response);
		log.info("[==== FIN Convenio n° 1 Monedas - Controller ====]");
		return ResponseEntity.ok(response);
	}
	
	@GetMapping(path =Servicios.TASASURLV1+"/nativa", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Tasa> getTasasNativa(@RequestBody TasaRequestConsulta tasaRequestConsulta, 
			HttpServletRequest requestHTTP){
		
		log.info("[==== INICIO Convenio n° 1 Monedas - Controller ====]");
		log.info("tasaRequestConsulta: " + tasaRequestConsulta);
		String codMonedaOrigen = null;
		String codMonedaDestino = null;
		log.info("codMonedaOrigen: " + codMonedaOrigen);
		log.info("codMonedaDestino: " + codMonedaDestino);
		
		return tasaService.findAllNative(codMonedaOrigen, codMonedaDestino);
	}
}
