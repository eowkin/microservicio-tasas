package com.bancoexterior.parametros.tasas.service;

import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.bancoexterior.parametros.tasas.util.Mapper;
import com.bancoexterior.parametros.tasas.config.Codigos.CodRespuesta;
import com.bancoexterior.parametros.tasas.config.Codigos.Constantes;
import com.bancoexterior.parametros.tasas.dto.TasaDto;
import com.bancoexterior.parametros.tasas.dto.TasaDtoConsulta;
import com.bancoexterior.parametros.tasas.dto.TasaDtoInversa;
import com.bancoexterior.parametros.tasas.dto.TasaDtoRequestConsulta;
import com.bancoexterior.parametros.tasas.dto.TasaDtoRequestCrear;
import com.bancoexterior.parametros.tasas.dto.TasaDtoResponse;
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
	private Mapper mapper;
	
	@Override
	public List<Tasa> findAll() {
		return repo.findAll();
	}

	@Override
	public Tasa findById(TasaPk id) {
		return repo.findById(id).orElse(null);
	}

	@Override
	public List<TasaDto> getTasaByCodMonedaOrigenAndCodMonedaDestino(String codMonedaOrigen, String codMonedaDestino) {
		return repo.getTasaByCodMonedaOrigenAndCodMonedaDestino(codMonedaOrigen, codMonedaDestino);
	}

	@Override
	public List<TasaDto> findAllDto() {
		//return repo.getAll();
		return repo.getAll(null);
	}

	@Override
	public List<Tasa> findAllNative(String codMonedaOrigen, String codMonedaDestino) {
		
		return null;
		//return repo.getQueryNativo(null, "926");
		//return repo.getQueryNativo1();
		//return repo.getQueryNativo21("111");
	}
	
	@Override
	public TasaDtoResponse getTasaByParameter(String codMonedaOrigen, String codMonedaDestino) {
		TasaDtoResponse response = new TasaDtoResponse();
		Resultado resultado = new Resultado();
		resultado.setCodigo(CodRespuesta.C0000);
		resultado.setDescripcion(Constantes.BLANK);
		List<TasaDto> listTasasDto = repo.getTasaByCodMonedaOrigenAndCodMonedaDestino(codMonedaOrigen, codMonedaDestino);
		if (listTasasDto.isEmpty()) {
			resultado.setCodigo(CodRespuesta.C0001);
			resultado.setDescripcion(env.getProperty(Constantes.RES + resultado.getCodigo(), resultado.getCodigo()));
		}else {
			resultado.setDescripcion(env.getProperty(Constantes.RES + resultado.getCodigo(), resultado.getCodigo()));
		}
		
		response.setResultado(resultado);
		response.setListTasasDto(listTasasDto);
		return response;
	}
	
	@Override
	public TasaDtoResponse getTasaByParameter(String codMonedaOrigen) {
		TasaDtoResponse response = new TasaDtoResponse();
		Resultado resultado = new Resultado();
		resultado.setCodigo(CodRespuesta.C0000);
		resultado.setDescripcion(Constantes.BLANK);
		List<TasaDto> listTasasDto = repo.getTasaByCodMonedaOrigen(codMonedaOrigen);
		if (listTasasDto.isEmpty()) {
			resultado.setCodigo(CodRespuesta.C0001);
			resultado.setDescripcion(env.getProperty(Constantes.RES + resultado.getCodigo(), resultado.getCodigo()));
		}else {
			resultado.setDescripcion(env.getProperty(Constantes.RES + resultado.getCodigo(), resultado.getCodigo()));
		}
		
		response.setResultado(resultado);
		response.setListTasasDto(listTasasDto);
		return response;
	}

	@Override
	public TasaDtoResponse getTasaByParameterCodMonedaDestino(String codMonedaDestino) {
		TasaDtoResponse response = new TasaDtoResponse();
		Resultado resultado = new Resultado();
		resultado.setCodigo(CodRespuesta.C0000);
		resultado.setDescripcion(Constantes.BLANK);
		List<TasaDto> listTasasDto = repo.getTasaByCodMonedaDestino(codMonedaDestino);
		if (listTasasDto.isEmpty()) {
			resultado.setCodigo(CodRespuesta.C0001);
			resultado.setDescripcion(env.getProperty(Constantes.RES + resultado.getCodigo(), resultado.getCodigo()));
		}else {
			resultado.setDescripcion(env.getProperty(Constantes.RES + resultado.getCodigo(), resultado.getCodigo()));
		}
		
		response.setResultado(resultado);
		response.setListTasasDto(listTasasDto);
		return response;
	}
	
	@Override
	public TasaDtoResponse findAllDtoResponse() {
		TasaDtoResponse response = new TasaDtoResponse();
		Resultado resultado = new Resultado();
		resultado.setCodigo(CodRespuesta.C0000);
		resultado.setDescripcion(Constantes.BLANK);
		List<TasaDto> listTasasDto = repo.getAll();
		
		if (listTasasDto.isEmpty()) {
			resultado.setCodigo(CodRespuesta.C0001);
			resultado.setDescripcion(env.getProperty(Constantes.RES + resultado.getCodigo(), resultado.getCodigo()));
		}else {
			resultado.setDescripcion(env.getProperty(Constantes.RES + resultado.getCodigo(), resultado.getCodigo()));
		}
		
		response.setResultado(resultado);
		response.setListTasasDto(listTasasDto);
		return response;
	}

	@Override
	public List<TasaDto> findAllDto(TasaDtoConsulta tasaDtoConsulta) {
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
	
	@Override
	public TasaDtoResponse consultaTasas(TasaRequestConsulta request) {
		log.info("\"==== INICIO Convenio 1 - Tasas Consultas ====\"");
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
		log.info("==== FIN Convenio 1 - Tasa Consultas ====");
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

	@Override
	public boolean existsById(TasaPk id) {
		return repo.existsById(id);
	}

	@Override
	public TasaDtoResponse save(TasaRequestCrear tasaRequestCrear) {
		log.info("Inicio del guardar nueva tasa");
		Tasa obj = new Tasa();
		TasaDtoResponse response = new TasaDtoResponse();
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
			response.setListTasasDto(repo.getTasaByCodMonedaOrigenAndCodMonedaDestino(obj.getId().getCodMonedaOrigen(), obj.getId().getCodMonedaOrigen()));
			return response;
		} catch (Exception e) {
			log.error("no se pudo crear el usuario");
			response.getResultado().setCodigo(CodRespuesta.CME6001);
			log.info("error: "+env.getProperty(Constantes.RES+CodRespuesta.CME6001,CodRespuesta.CME6001));
			response.getResultado().setDescripcion(env.getProperty(Constantes.RES+CodRespuesta.CME6001,CodRespuesta.CME6001));
			return response;
		}
		
	}

	@Override
	public List<TasaDtoInversa> findAllInversa() {
		//return null;
		return repo.getAll2();
	}

	

	

	
	
	

}
