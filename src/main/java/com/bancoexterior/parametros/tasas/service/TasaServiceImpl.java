package com.bancoexterior.parametros.tasas.service;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.bancoexterior.parametros.tasas.interfase.IRegistrarAuditoriaService;
import com.bancoexterior.parametros.tasas.model.RegistrarAuditoriaRequest;
import com.bancoexterior.parametros.tasas.config.Codigos.CodRespuesta;
import com.bancoexterior.parametros.tasas.config.Codigos.Constantes;
import com.bancoexterior.parametros.tasas.config.Codigos.Servicios;
import com.bancoexterior.parametros.tasas.dto.TasaDto;
import com.bancoexterior.parametros.tasas.dto.TasaDtoConsulta;
import com.bancoexterior.parametros.tasas.dto.TasaDtoRequestActualizar;
import com.bancoexterior.parametros.tasas.dto.TasaDtoRequestConsulta;
import com.bancoexterior.parametros.tasas.dto.TasaDtoRequestCrear;
import com.bancoexterior.parametros.tasas.dto.TasaDtoResponse;
import com.bancoexterior.parametros.tasas.dto.TasaDtoResponseActualizar;
import com.bancoexterior.parametros.tasas.dto.TasaRequestActualizar;
import com.bancoexterior.parametros.tasas.dto.TasaRequestConsulta;
import com.bancoexterior.parametros.tasas.dto.TasaRequestCrear;
import com.bancoexterior.parametros.tasas.entities.Tasa;
import com.bancoexterior.parametros.tasas.entities.TasaPk;
import com.bancoexterior.parametros.tasas.repository.ITasaRepository;
import com.bancoexterior.parametros.tasas.response.Resultado;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class TasaServiceImpl implements ITasaService{

	@Autowired
	private ITasaRepository repo;
	
	@Autowired
	private Environment env;

	
	@Autowired
	private IRegistrarAuditoriaService registrarA ;


	
	@Override
	public TasaDtoResponse consultaTasas(TasaRequestConsulta request) {
		log.info(Servicios.TASASSERVICEI);
		TasaDtoResponse tasaDtoResponse = new TasaDtoResponse();
		Resultado resultado = new Resultado();
		String codigo = CodRespuesta.C0000;
		String errorCM = Constantes.BLANK;
		List<TasaDto> listTasaDto;
		TasaDtoConsulta tasaDtoConsulta = new TasaDtoConsulta(request);
		TasaDtoRequestConsulta tasaDtoRequestConsulta = request.getTasaDtoRequestConsulta();
		log.info("codMonedaOrigen: "+tasaDtoRequestConsulta.getCodMonedaOrigen());
		log.info("codMonedaDestino: "+tasaDtoRequestConsulta.getCodMonedaDestino());
		try {
			log.info("antes de llamara validarDatosConsulta");
			codigo = validaDatosConsulta(request);
			log.info("codigo: "+codigo);
			if(codigo.equalsIgnoreCase(CodRespuesta.C0000)) {
				log.info("monedaDto: "+tasaDtoConsulta);	
				log.info("codMonedaOrigen: "+tasaDtoConsulta.getCodMonedaOrigen());
				log.info("codMonedaDestino: "+tasaDtoConsulta.getCodMonedaDestino());
				//consulta BD
				listTasaDto = this.findAllDto(tasaDtoConsulta);
				tasaDtoResponse.setListTasasDto(listTasaDto);
				
				//Validar Respuesta
				resultado = validaConsulta(listTasaDto);
				codigo = resultado.getCodigo();
				errorCM = resultado.getDescripcion();
			}
		} catch (Exception e) {
			log.error(""+e);
			codigo = CodRespuesta.CME6000;
			errorCM = Constantes.EXC+e;
		}
		
		tasaDtoResponse.getResultado().setCodigo(codigo);
		tasaDtoResponse.getResultado().setDescripcion(env.getProperty(Constantes.RES+codigo,codigo).replace(Constantes.ERROR, errorCM));
		
		log.info("tasaDtoResponse: "+tasaDtoResponse);
		log.info(Servicios.TASASSERVICEF);
		return tasaDtoResponse;
	}
	
	private String validaDatosConsulta(TasaRequestConsulta request) {
		log.info("dentro de validarDatosConsulta");
		log.info(""+request);
		String codigo = CodRespuesta.C0000;
		String codMonedaOrigen;
		String codMonedaDestino;
		
		codMonedaOrigen = request.getTasaDtoRequestConsulta().getCodMonedaOrigen() == null ? "000":request.getTasaDtoRequestConsulta().getCodMonedaOrigen();
		codMonedaDestino = request.getTasaDtoRequestConsulta().getCodMonedaDestino() == null ? "000":request.getTasaDtoRequestConsulta().getCodMonedaDestino();
		
		request.getTasaDtoRequestConsulta().setCodMonedaOrigen(codMonedaOrigen);
		request.getTasaDtoRequestConsulta().setCodMonedaDestino(codMonedaDestino);
		
		
		log.info("antes de llamar factory");
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<TasaRequestConsulta>> errores = validator.validate(request);
		
	
			for (ConstraintViolation<TasaRequestConsulta> cv : errores) {
				
				if ( !cv.getMessage().equalsIgnoreCase(Constantes.BLANK)) {
					codigo = cv.getMessage();
					 break;
				}

			}

		
		return codigo;
	}

	/**
     * Nombre:                  validaConsulta
     * Descripcion:             Metodo para evaluar el resultado de la consulta de las monedas
     *
     * @param  Objeto List<MonedasBD>
     * @return Resultado  Objeto con la información de la evaluacion.
     * @version 1.0
     * @author Wilmer Vieira
	 * @since 16/03/21
    */ 
	
	private Resultado validaConsulta(List<TasaDto> listTasaDto) {
		Resultado resultado = new Resultado();
		resultado.setCodigo(CodRespuesta.C0000);
		resultado.setDescripcion(Constantes.BLANK);
		
		if(listTasaDto.isEmpty()) {
			resultado.setCodigo(CodRespuesta.C0001);
			return resultado;
		}

		/*
	    if(monedasBD.get(0).getCodMonedaBD().equalsIgnoreCase(Constantes.SERROR)) {
	    	resultado.setCodigo(CodRespuesta.CME6002);
	    	resultado.setDescripcion(monedasBD.get(0).getDescripcionBD());
	    	 LOGGER.error(resultado);
	    	return resultado;
	    }*/

	    
	    log.info(""+resultado);
		return resultado;
		
	}
	
	/**
     * Nombre:                 registrarAuditoriaBD
     * Descripcion:            Registrar Auditoria en Web Service
     *
     * @param  req  Objeto RegistrarAuditoriaRequest
     * @param  codigo   Codigo de respuesta
     * @param descripcion Descripcion del resultado
     * @version 1.0
     * @author Wilmer Vieira
	 * @since 02/03/21
     */
	private void registrarAuditoriaBD(RegistrarAuditoriaRequest registrarAu,Resultado response, String errorAdicional) {
			
		        registrarA.registrarAuditoria(registrarAu, response.getCodigo(),response.getDescripcion(),errorAdicional);	
	}
	
	@Override
	public boolean existsById(TasaPk id) {
		return repo.existsById(id);
	}

	@Override
	public TasaDtoResponseActualizar save(TasaRequestCrear tasaRequestCrear, HttpServletRequest requestHTTP) {
		
		log.info(Servicios.TASASSERVICEI);
		log.info("tasaRequestCrear: "+tasaRequestCrear);
		String microservicio = Servicios.TASAS;
		
		RegistrarAuditoriaRequest reAU = null;
		
		
		reAU = new RegistrarAuditoriaRequest(tasaRequestCrear, microservicio,requestHTTP);
		String errorM ;
		Tasa obj = new Tasa();
		TasaDtoResponseActualizar response = new TasaDtoResponseActualizar();
		Resultado resultado = new Resultado();
		
		resultado.setCodigo(CodRespuesta.C0000);
		resultado.setDescripcion(env.getProperty(Constantes.RES+CodRespuesta.C0000,CodRespuesta.C0000).replace(Constantes.ERROR, Constantes.BLANK));
		
		try {
			log.info("tasaRequestCrear: "+tasaRequestCrear);
			TasaDtoRequestCrear tasaDtoRequestCrear = tasaRequestCrear.getTasaDtoRequestCrear();
			log.info("tasaDtoRequestCrear: "+tasaDtoRequestCrear);
			//obj = mapper.map(tasaDtoRequestCrear, Tasa.class);
			TasaPk id = new TasaPk(tasaDtoRequestCrear.getCodMonedaOrigen(), tasaDtoRequestCrear.getCodMonedaDestino());
			
			obj.setId(id);
			obj.setCodUsuario(tasaRequestCrear.getCodUsuarioMR());
			obj.setMontoTasa(tasaDtoRequestCrear.getMontoTasa());
			
			
			log.info("obj: "+obj);
			obj = repo.save(obj);
			response.setResultado(resultado);
			//response.setListTasasDto(repo.getTasaByCodMonedaOrigenAndCodMonedaDestino(obj.getId().getCodMonedaOrigen(), obj.getId().getCodMonedaOrigen()));
			//return response;
		} catch (Exception e) {
			log.error("no se pudo crear el usuario");
			response.getResultado().setCodigo(CodRespuesta.CME6001);
			log.info("error: "+env.getProperty(Constantes.RES+CodRespuesta.CME6001,CodRespuesta.CME6001));
			response.getResultado().setDescripcion(env.getProperty(Constantes.RES+CodRespuesta.CME6001,CodRespuesta.CME6001));
			//return response;
		}
		
		errorM = resultado.getDescripcion();
		
		if(reAU != null) {
			reAU.setIdCliente(Constantes.RIF);
			reAU.setCedula(Constantes.CEDULA);
			reAU.setTelefono(Constantes.TELEFONO);
			reAU.setIdCanal("8");
			registrarAuditoriaBD(reAU, resultado, errorM);
		}
		return response;
	}



	@Override
	public TasaDto findByIdDto(TasaPk id) {
		Tasa tasa = repo.findById(id).orElse(null);
		TasaDto tasaDto = new TasaDto();
		tasaDto.setCodMonedaOrigen(tasa.getId().getCodMonedaOrigen());
		tasaDto.setCodMonedaDestino(tasa.getId().getCodMonedaDestino());
		tasaDto.setMontoTasa(tasa.getMontoTasa());
		tasaDto.setCodUsuario(tasa.getCodUsuario());
		tasaDto.setFechaModificacion(tasa.getFechaModificacion());
		return tasaDto;
	}

	@Override
	public TasaDtoResponseActualizar actualizar(TasaRequestActualizar tasaRequestActualizar) {
		log.info("Inicio del actualizar tasa");
		Tasa obj = new Tasa();
		//TasaDtoResponse response = new TasaDtoResponse();
		TasaDtoResponseActualizar response = new TasaDtoResponseActualizar();
		Resultado resultado = new Resultado();
		
		resultado.setCodigo(CodRespuesta.C0000);
		resultado.setDescripcion(env.getProperty(Constantes.RES+CodRespuesta.C0000,CodRespuesta.C0000).replace(Constantes.ERROR, Constantes.BLANK));
		
		try {
			log.info("tasaRequestCrear: "+tasaRequestActualizar);
			TasaDtoRequestActualizar tasaDtoRequestActualizar = tasaRequestActualizar.getTasaDtoRequestActualizar();
			
			log.info("tasaDtoRequestActualizar: "+tasaDtoRequestActualizar);
			//obj = mapper.map(tasaDtoRequestCrear, Tasa.class);
			TasaPk id = new TasaPk(tasaDtoRequestActualizar.getCodMonedaOrigen(), tasaDtoRequestActualizar.getCodMonedaDestino());
			
			obj.setId(id);
			obj.setCodUsuario(tasaRequestActualizar.getCodUsuarioMR());
			obj.setMontoTasa(tasaDtoRequestActualizar.getMontoTasa());
			obj.setFechaModificacion(tasaDtoRequestActualizar.getFechaModificacion());
			//obj.setFechaModificacion(tasaDtoRequestCrear.getTasaDtoRequestCrear());
			
			log.info("obj: "+obj);
			obj = repo.save(obj);
			response.setResultado(resultado);
			return response;
		} catch (Exception e) {
			log.error("no se pudo actualizar el usuario");
			response.getResultado().setCodigo(CodRespuesta.CME6001);
			log.info("error: "+env.getProperty(Constantes.RES+CodRespuesta.CME6001,CodRespuesta.CME6001));
			response.getResultado().setDescripcion(env.getProperty(Constantes.RES+CodRespuesta.CME6001,CodRespuesta.CME6001));
			return response;
		}
	}
	
	@Override
	public List<TasaDto> findAllDto(TasaDtoConsulta tasaDtoConsulta){
		List<TasaDto> listTasaDto = null;
		
		if(tasaDtoConsulta.getCodMonedaOrigen() == null && tasaDtoConsulta.getCodMonedaDestino() == null) {
			listTasaDto = repo.getAll();
		}
		
		if(tasaDtoConsulta.getCodMonedaOrigen() != null && tasaDtoConsulta.getCodMonedaDestino() == null) {
			listTasaDto = repo.getTasaByCodMonedaOrigen(tasaDtoConsulta.getCodMonedaOrigen());
		}
		
		if(tasaDtoConsulta.getCodMonedaOrigen() == null && tasaDtoConsulta.getCodMonedaDestino() != null) {
			listTasaDto = repo.getTasaByCodMonedaDestino(tasaDtoConsulta.getCodMonedaDestino());
		}
		
		if(tasaDtoConsulta.getCodMonedaOrigen() != null && tasaDtoConsulta.getCodMonedaDestino() != null) {
			listTasaDto = repo.getTasaByCodMonedaOrigenAndCodMonedaDestino(tasaDtoConsulta.getCodMonedaOrigen(), tasaDtoConsulta.getCodMonedaDestino());
		}
		return listTasaDto;
	}

	

	

	
	
	

}
