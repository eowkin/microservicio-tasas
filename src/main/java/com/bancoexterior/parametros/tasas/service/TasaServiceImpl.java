package com.bancoexterior.parametros.tasas.service;



import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import com.bancoexterior.parametros.tasas.dto.TasaDtoRequestCrear;
import com.bancoexterior.parametros.tasas.dto.TasaDtoResponse;
import com.bancoexterior.parametros.tasas.dto.TasaDtoResponseActualizar;
import com.bancoexterior.parametros.tasas.dto.TasaRequestConsulta;
import com.bancoexterior.parametros.tasas.dto.TasaRequestCrear;
import com.bancoexterior.parametros.tasas.entities.Tasa;
import com.bancoexterior.parametros.tasas.entities.TasaPk;
import com.bancoexterior.parametros.tasas.repository.ITasaRepository;
import com.bancoexterior.parametros.tasas.response.Resultado;




@Service
public class TasaServiceImpl implements ITasaService{

	private static final Logger LOGGER = LogManager.getLogger(TasaServiceImpl.class);
	
	@Autowired
	private ITasaRepository repo;
	
	@Autowired
	private Environment env;

	@Autowired
	private IRegistrarAuditoriaService registrarA ;

	
	/**
	 * Nombre: consultaTasas 
	 * Descripcion: Invocar metodo para la gestion de consulta a realizar
	 * para la busqueda de tasas con los parametros enviados.
	 *
	 * @param request     Objeto tipo TasaRequestConsulta
	 * @return TasaDtoResponse
	 * @version 1.0
	 * @author Eugenio Owkin
	 * @since 12/04/21
	 */
	@Override
	public TasaDtoResponse consultaTasas(TasaRequestConsulta request) {
		LOGGER.info(Servicios.TASASSERVICEICOSULTAS);
		TasaDtoResponse tasaDtoResponse = new TasaDtoResponse();
		Resultado resultado = new Resultado();
		String codigo = CodRespuesta.C0000;
		String errorCM = Constantes.BLANK;
		List<TasaDto> listTasaDto;
		TasaDtoConsulta tasaDtoConsulta = new TasaDtoConsulta(request);
		//TasaDtoRequestConsulta tasaDtoRequestConsulta = request.getTasaDtoRequestConsulta();
		try {
			
			codigo = validaDatosConsulta(request);
			LOGGER.info("codigo: "+codigo);
			if(codigo.equalsIgnoreCase(CodRespuesta.C0000)) {
				//consulta BD
				listTasaDto = this.findAllDto(tasaDtoConsulta);
				//listTasaDto = this.findAllDtoNuevo(tasaDtoConsulta);
				tasaDtoResponse.setListTasasDto(listTasaDto);
				
				//Validar Respuesta
				resultado = validaConsulta(listTasaDto);
				codigo = resultado.getCodigo();
				errorCM = resultado.getDescripcion();
			}
		} catch (Exception e) {
			LOGGER.error(e);
			codigo = CodRespuesta.CME6000;
			errorCM = Constantes.EXC+e;
		}
		
		tasaDtoResponse.getResultado().setCodigo(codigo);
		tasaDtoResponse.getResultado().setDescripcion(env.getProperty(Constantes.RES+codigo,codigo).replace(Constantes.ERROR, errorCM));
		
		LOGGER.info(tasaDtoResponse);
		LOGGER.info(Servicios.TASASSERVICEFCOSULTAS);
		return tasaDtoResponse;
	}
	
	/**
     * Nombre:                  validaDatosConsulta
     * Descripcion:             Valida datos de entrada del metodo de consulta.
     *
     * @param request Objeto TasaRequestConsulta
     * @return String  Codigo resultado de la evaluacion.
     * @version 1.0
     * @author Eugenio Owkin
	 * @since 12/04/21
	 */
	private String validaDatosConsulta(TasaRequestConsulta request) {
		LOGGER.info("dentro de validarDatosConsulta");
		LOGGER.info(""+request);
		String codigo = CodRespuesta.C0000;
		String codMonedaOrigen;
		String codMonedaDestino;
		
		codMonedaOrigen = request.getTasaDtoRequestConsulta().getCodMonedaOrigen() == null ? "000":request.getTasaDtoRequestConsulta().getCodMonedaOrigen();
		codMonedaDestino = request.getTasaDtoRequestConsulta().getCodMonedaDestino() == null ? "000":request.getTasaDtoRequestConsulta().getCodMonedaDestino();
		
		request.getTasaDtoRequestConsulta().setCodMonedaOrigen(codMonedaOrigen);
		request.getTasaDtoRequestConsulta().setCodMonedaDestino(codMonedaDestino);
		
		
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
     * @param listTasaDto Objeto List<TasaDto>
     * @return Resultado  Objeto con la información de la evaluacion.
     * @version 1.0
     * @author Eugenio Owkin
	 * @since 12/04/21
	 */ 
	
	private Resultado validaConsulta(List<TasaDto> listTasaDto) {
		Resultado resultado = new Resultado();
		resultado.setCodigo(CodRespuesta.C0000);
		resultado.setDescripcion(Constantes.BLANK);
		
		if(listTasaDto.isEmpty()) {
			resultado.setCodigo(CodRespuesta.C0001);
			return resultado;
		}
	    
		LOGGER.info(resultado);
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
     * @author Eugenio Owkin
	 * @since 12/04/21
	 */
	private void registrarAuditoriaBD(RegistrarAuditoriaRequest registrarAu,Resultado response, String errorAdicional) {
			
		        registrarA.registrarAuditoria(registrarAu, response.getCodigo(),response.getDescripcion(),errorAdicional);	
	}
	
	
	
	/**
	 * Nombre: existsById 
	 * Descripcion: Invocar metodo para buscar si existe o no 
	 * una tasa por TasaPk.
	 * @param id String  
	 * @return boolean
	 * @version 1.0
	 * @author Eugenio Owkin
	 * @since 12/04/21
	 */
	@Override
	public boolean existsById(TasaPk id) {
		return repo.existsById(id);
	}

	/**
	 * Nombre: save 
	 * Descripcion: Invocar metodo para crear la tasa con
	 * los parametros enviados.
	 *
	 * @param request     Objeto tipo TasaRequestCrear
	 * @param requestHTTP Objeto tipo HttpServletRequest
	 * @return MonedaDtoResponseActualizar
	 * @version 1.0
	 * @author Eugenio Owkin
	 * @since 12/04/21
	 */
	
	@Override
	public TasaDtoResponseActualizar save(TasaRequestCrear tasaRequestCrear, HttpServletRequest requestHTTP) {
		
		LOGGER.info(Servicios.TASASSERVICEICREAR);
		LOGGER.info(tasaRequestCrear);
		String microservicio = Servicios.TASAS;
		
		RegistrarAuditoriaRequest reAU = null;
		
		
		reAU = new RegistrarAuditoriaRequest(tasaRequestCrear, microservicio,requestHTTP);
		String errorM = Constantes.BLANK;
		String codigo =  CodRespuesta.C0000;
		Tasa obj = new Tasa();
		TasaDtoResponseActualizar response = new TasaDtoResponseActualizar();
		Resultado resultado = new Resultado();
		
		resultado.setCodigo(CodRespuesta.C0000);
		resultado.setDescripcion(env.getProperty(Constantes.RES+CodRespuesta.C0000,CodRespuesta.C0000).replace(Constantes.ERROR, Constantes.BLANK));
		
		try {
			
			TasaDtoRequestCrear tasaDtoRequestCrear = tasaRequestCrear.getTasaDtoRequestCrear();
			TasaPk id = new TasaPk(tasaDtoRequestCrear.getCodMonedaOrigen(), tasaDtoRequestCrear.getCodMonedaDestino());
			
			obj.setId(id);
			obj.setCodUsuario(tasaRequestCrear.getCodUsuarioMR());
			obj.setMontoTasa(tasaDtoRequestCrear.getMontoTasa());
			//E66666
			//obj.setCodUsuario("E555555555555555");
			
			
			obj = repo.save(obj);
			response.setResultado(resultado);
			
		} catch (Exception e) {
			LOGGER.error(e);
			codigo = CodRespuesta.CME6001;
			errorM = Constantes.EXC+e;
			response.getResultado().setCodigo(CodRespuesta.CME6001);
			response.getResultado().setDescripcion(env.getProperty(Constantes.RES+CodRespuesta.CME6001,CodRespuesta.CME6001));
			
		}
		
		resultado.setCodigo(codigo);
		resultado.setDescripcion(env.getProperty(Constantes.RES+codigo,codigo).replace(Constantes.ERROR, errorM));
		
		if(reAU != null) {
			reAU.setIdCliente(Constantes.RIF);
			reAU.setCedula(Constantes.CEDULA);
			reAU.setTelefono(Constantes.TELEFONO);
			reAU.setIdCanal(tasaRequestCrear.getCanalCM());
			registrarAuditoriaBD(reAU, resultado, errorM);
		}
		
		LOGGER.info(Servicios.TASASSERVICEFCREAR);
		return response;
	}


	/**
	 * Nombre: findByIdDto 
	 * Descripcion: Invocar metodo para una busqueda de una tasa
	 * por id.
	 *
	 * @param id TasaPk  
	 * @return TasaDto
	 * @version 1.0
	 * @author Eugenio Owkin
	 * @since 12/04/21
	 */
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
	
	/**
	 * Nombre: actualizar 
	 * Descripcion: Invocar metodo para actualizar la tasa con
	 * los parametros enviados.
	 *
	 * @param request     Objeto tipo TasaRequestCrear
	 * @param requestHTTP Objeto tipo HttpServletRequest
	 * @return TasaDtoResponseActualizar
	 * @version 1.0
	 * @author Eugenio Owkin
	 * @since 12/04/21
	 */
	@Override
	public TasaDtoResponseActualizar actualizar(TasaRequestCrear tasaRequestCrear, HttpServletRequest requestHTTP) {
		LOGGER.info(Servicios.TASASSERVICEIACTUALIZAR);
		LOGGER.info(tasaRequestCrear);
		String microservicio = Servicios.TASASACTUALIZAR;
		
		RegistrarAuditoriaRequest reAU = null;
		
		
		reAU = new RegistrarAuditoriaRequest(tasaRequestCrear, microservicio,requestHTTP);
		String errorM = Constantes.BLANK;
		String codigo =  CodRespuesta.C0000;
		
		Tasa obj = new Tasa();
		//TasaDtoResponse response = new TasaDtoResponse();
		TasaDtoResponseActualizar response = new TasaDtoResponseActualizar();
		Resultado resultado = new Resultado();
		
		resultado.setCodigo(CodRespuesta.C0000);
		resultado.setDescripcion(env.getProperty(Constantes.RES+CodRespuesta.C0000,CodRespuesta.C0000).replace(Constantes.ERROR, Constantes.BLANK));
		
		try {
			
			TasaDtoRequestCrear tasaDtoRequestCrear = tasaRequestCrear.getTasaDtoRequestCrear();
			
			TasaPk id = new TasaPk(tasaDtoRequestCrear.getCodMonedaOrigen(), tasaDtoRequestCrear.getCodMonedaDestino());
			TasaDto tasaDto = this.findByIdDto(id);
		    tasaDto.setMontoTasa(tasaDtoRequestCrear.getMontoTasa());
		    tasaDto.setCodUsuario(tasaRequestCrear.getCodUsuarioMR());
		  
			obj.setId(id);
			obj.setCodUsuario(tasaDto.getCodUsuario());
			obj.setMontoTasa(tasaDto.getMontoTasa());
			obj.setFechaModificacion(tasaDto.getFechaModificacion());

			//obj.setCodUsuario("E555555555555555");
			
			LOGGER.info("obj: "+obj);
			obj = repo.save(obj);
			response.setResultado(resultado);
			//return response;
		} catch (Exception e) {
			LOGGER.error(e);
			codigo = CodRespuesta.CME6001;
			errorM = Constantes.EXC+e;
			response.getResultado().setCodigo(CodRespuesta.CME6001);
			response.getResultado().setDescripcion(env.getProperty(Constantes.RES+CodRespuesta.CME6001,CodRespuesta.CME6001));
		}
		
		resultado.setCodigo(codigo);
		resultado.setDescripcion(env.getProperty(Constantes.RES+codigo,codigo).replace(Constantes.ERROR, errorM));
		
		if(reAU != null) {
			reAU.setIdCliente(Constantes.RIF);
			reAU.setCedula(Constantes.CEDULA);
			reAU.setTelefono(Constantes.TELEFONO);
			reAU.setIdCanal(tasaRequestCrear.getCanalCM());
			registrarAuditoriaBD(reAU, resultado, errorM);
		}
		LOGGER.info(Servicios.TASASSERVICEFACTUALIZAR);
		return response;
	}
	
	/**
	 * Nombre: findAllDto 
	 * Descripcion: Invocar metodo para una busqueda de las tasas con
	 * los parametros enviados.
	 *
	 * @param request     Objeto tipo TasaDtoConsulta
	 * @return List<TasaDto>
	 * @version 1.0
	 * @author Eugenio Owkin
	 * @since 12/04/21
	 */
		
	@Override
	public List<TasaDto> findAllDto(TasaDtoConsulta tasaDtoConsulta){
		LOGGER.info(Servicios.TASASSERVICEICOSULTAS+"BD");
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
		LOGGER.info(Servicios.TASASSERVICEFCOSULTAS+"BD");
		return listTasaDto;
	}

	
}
