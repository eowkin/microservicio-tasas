package com.bancoexterior.parametros.tasas.interfase;

import com.bancoexterior.parametros.tasas.model.RegistrarAuditoriaRequest;

public interface IRegistrarAuditoriaService {
	
	void registrarAuditoria(RegistrarAuditoriaRequest auditoria,  String codigo, String mensaje, String errorAdicional);

}
