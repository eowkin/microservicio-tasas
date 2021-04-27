package com.bancoexterior.parametros.tasas.config;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import com.bancoexterior.parametros.tasas.seguridad.MiCipher;



@Configuration
@PropertySource( value = "file://"+"${${microservicio.ambiente}"+".seed.ruta}"+"application.properties", ignoreResourceNotFound = false)
public class DataSourceConfig {
    
	private static final Logger LOGGER = LogManager.getLogger(DataSourceConfig.class);
	
	@Value("${microservicio.ambiente}")
	private String ambiente;
	
	@Value("${spring.datasource.username}")
	private String usuario;
	
	@Value("${spring.datasource.password}")
	private String password;
	
	@Value("${${microservicio.ambiente}"+".seed.tasa}")
    private String seedTasa;
	
	@Value("${sconfig.deskey}")
    private String sconfigDesKey;
	
	@Value("${sconfig.prokey}")
    private String sconfigProKey;
	
    @Bean
    public DataSource getDataSource() {
    	
    	
    	
    	
    	
    	LOGGER.info("ambiente: "+ambiente);
    	LOGGER.info("usuario: "+usuario);
    	LOGGER.info("password: "+password);
    	LOGGER.info("sconfigDesKey: "+sconfigDesKey);
    	LOGGER.info("sconfigProKey: "+sconfigProKey);
    	LOGGER.info("seedTasa: "+seedTasa);
    	
    	String usuarioEncryptBanco = MiCipher.encrypt("C14405", sconfigDesKey);
    	String passwordEncryptBanco = MiCipher.encrypt("Cumana01*", sconfigDesKey);
    	LOGGER.info("usuarioEncryptBanco: "+usuarioEncryptBanco);
    	LOGGER.info("passwordEncryptBanco: "+passwordEncryptBanco);
    	
    	
    	
    	String usuarioDecrypt = MiCipher.decrypt(usuario, seedTasa);
    	String passwordDecrypt = MiCipher.decrypt(password, seedTasa);
    	
    	LOGGER.info("usuarioDecrypt: "+usuarioDecrypt);
    	LOGGER.info("passwordDecrypt: "+passwordDecrypt);
    	
        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("org.postgresql.Driver");
        //dataSourceBuilder.url("jdbc:postgresql://localhost:5432/Convenio1");
        dataSourceBuilder.url("jdbc:postgresql://172.19.148.50:5432/Convenio1");
        dataSourceBuilder.username(usuarioDecrypt);
        dataSourceBuilder.password(passwordDecrypt);
        return dataSourceBuilder.build();
    }
}
