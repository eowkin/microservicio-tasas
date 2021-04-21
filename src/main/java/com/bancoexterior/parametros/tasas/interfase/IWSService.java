package com.bancoexterior.parametros.tasas.interfase;

import com.bancoexterior.parametros.tasas.model.WSRequest;
import com.bancoexterior.parametros.tasas.model.WSResponse;

public interface  IWSService {
	WSResponse post(WSRequest request) ;
}
