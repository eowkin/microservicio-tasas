package com.bancoexterior.parametros.tasas.config;



import lombok.Data;

public class Codigos {

	@Data
	public class Ambientes{
		
		public static final String DESARROLLO = "des";
		public static final String CALIDAD    = "qa";
		public static final String PRODUCCION = "pro";
	}
	
	@Data
	public class CodRespuesta{
		//ok
		public static final String C0000 = "0000";
		public static final String C0001 = "0001";
		
		//entrada tasa
		public static final String CDE1000 = "1000";
		public static final String CDE1001 = "1001";
		public static final String CDE1002 = "1002";
		public static final String CDE1003 = "1003";
		public static final String CDE1004 = "1004";
		public static final String CDE1005 = "1005";
		public static final String CDE1006 = "1006";
		public static final String CDE1007 = "1007";
		public static final String CDE1008 = "1008";
		
		
		//entrada tasa
		public static final String CDE1009 = "1009";
		public static final String CDE1010 = "1010";
		public static final String CDE1011 = "1011";
		
		
		//BD
		public static final String CME2000 = "2000";
		public static final String CME2001 = "2001";
		public static final String CME2002 = "2002";
		public static final String CME2003 = "2003";
		public static final String CME2004 = "2004";
		public static final String CME2005 = "2005";
		public static final String CME2006 = "2006";
		
		////GENERAL
    	public static final String CME6000 = "6000";
    	public static final String CME6001 = "6001";
		public static final String CME6002 = "6002";
		
	}
	
	@Data
	public class Annotation{
		 public static final String OBJECTDEFAULT     = "[Objeto vacio]";
		 public static final String FECHADEFAULT      = "[Fecha invalida]";
	 }
	
	@Data
	public class ParamConfig{
		
		public static final String CANAL           = "^[a-zA-Z\\-0-9]{1,4}$";
		public static final String IDSESIONVALID   = "uuuuMMddHHmmss";
		public static final String CODUSUARIO      = "^.{2,10}$";
		public static final String IDUSUARIO       = "^.{1,15}$";
		public static final String CODMONEDA       = "^[a-zA-Z\\-0-9]{1,3}$";
		public static final String DESCRIPCION     = "^.{1,500}$";
		public static final String CODALTERNO      = "^.{1,10}$";
		//public static final String MONTO           = "^\\d{1,3}(\\.?\\d{3})*(,\\d{1,2})?$";
		
	}
	
	@Data
	public class Constantes{
		
		public static final String MONEDADEFAULT                      = "000";
		public static final String TRUE                               = "true";
		public static final String CODALTERNODEFAULT                  = "1234";
		public static final String BLANK                              = "";
		public static final String RES                                = "res.";
		public static final String SUBSTRING_COD_OK                   = "0";
		public static final String SUBSTRING_COD_UNPROCESSABLE_ENTITY = "1";
		public static final String INTERNAL_SERVER_ERROR              = "6";
		public static final String XCLIENTIP                          = "X-Client-IP";
		public static final String CONTENT_TYPE                       = "Content-Type";
		public static final String ACCEPT_CHARSET                     = "Accept-Charset";
		public static final String UTF8                               = "UTF-8";
		public static final String TERMINAL                           = "CONVENIO1";
		public static final String N_A                                = "N/A";
		public static final String FECHA_HORA                         = "yyyy-MM-dd HH:mm:ss";
		public static final String PLECA                              = "|";
		public static final String APP_JSON                           = "application/json";
		public static final String EXC                                = "Exc:";
		public static final String ERROR                              = "@@Error";
		public static final String RIF                                = "J000000000";
		public static final String CEDULA                             = "V00000000";
		public static final String TELEFONO                           = "00000000000000";
	}
	
	
	@Data
	public class Servicios{
		
		
		//tasas
		public static final String TASASURLV1       = "/v1/parametros/tasas";
		
		
		//Monedas
		public static final String TASAS            = "Convenio1-Tasa";
		public static final String TASASACTUALIZAR  = "Convenio1-Tasa Actualizacion";
		public static final String TASASCONTROLLERI = "[==== INICIO Convenio n° 1 Tasas - Controller ====]";
		public static final String TASASCONTROLLERF = "[==== FIN Convenio n° 1 Tasas - Controller ====]";
		public static final String TASASSERVICEI    = "==== INICIO Convenio 1 - Tasas - Service ====";
		public static final String TASASSERVICEF    = "==== FIN Convenio 1 - Tasas - Service ====";
		public static final String TASASSERVICEICOSULTAS    = "==== INICIO Convenio 1 - Tasas - Service - Consultas ====";
		public static final String TASASSERVICEFCOSULTAS    = "==== FIN Convenio 1 - Tasas - Service - Consultas ====";
		public static final String TASASSERVICEICREAR    = "==== INICIO Convenio 1 - Tasas - Service - Crear ====";
		public static final String TASASSERVICEFCREAR    = "==== FIN Convenio 1 - Tasas - Service - Crear ====";
		public static final String TASASSERVICEIACTUALIZAR    = "==== INICIO Convenio 1 - Tasas - Service - Actualizar ====";
		public static final String TASASSERVICEFACTUALIZAR    = "==== FIN Convenio 1 - Tasas - Service - Actualizar ====";
		
		//Auditoria
		
		public static final String ASERVICEI    = "====== INICIO Registrar Auditoria ======";
		public static final String ASERVICEF    = "====== FIN Registrar Auditoria ======";
		
	}
	
	@Data
	public class ExceptionMessages{
		
		public static final String UNIRESTHTTPE         = "HttpStatusCodeException: %1$s";
		public static final String UNIRESTBODYE         = "ResponseBody: %1$s";
		public static final String UNIRESTE             = "Exception UNI: %1$s";
		
		public static final String AUPRINTERROR         = "ERROR:{}";
		public static final String AUPRINTERRORMENSA    = "Ocurrio un error en registrar Auditoria:{}";
	}
		
	
	@Data
	public class InfoMessages {
	
		// Auditoria Service
    	public static final String AUREQUEST                = "Request = [{}]";
    	public static final String AUPRINTINFO              = "registrar Auditoria respuesta: ";
	}
		
		
		
	
	
}
