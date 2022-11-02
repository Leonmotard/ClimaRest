package com.tsti.clima.config;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Podremos ver la documentacion de la api e interactuar con ella en: http://localhost:8081/swagger-ui/
 * @author dardo
 *
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
	public static final String PERSONAS	 = "Personas";
	public static final String CIUDADES	 = "Ciudades";
	public static final String CLIMA = "ClimaActual";
	public static final String PRONOSTICO = "Pronostico";
	public static final String SUSCRIPCION ="Suscripcion";
	public static final String MAIL = "Mail";
	public static final String EVENTO = "evento";

	@Bean
	public Docket apiDocket() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.tsti.clima.restServices"))
				.paths(PathSelectors.any())
				.build()
				.tags(new Tag(PERSONAS, "Recurso Personas."))
				.tags(new Tag(CIUDADES, "Recurso Ciudaes."))
				.tags(new Tag(CLIMA, "Recurso ClimaActual"))
				.tags(new Tag(PRONOSTICO, "Recurso Pronostico"))
				.tags(new Tag(SUSCRIPCION, "Recurso Suscripcion"))
				.tags(new Tag(MAIL, "Envío mail"))
				.tags(new Tag(EVENTO, "Envío alerta"))
				.apiInfo(getApiInfo())
				;
	}
	
	private ApiInfo getApiInfo() {
		return new ApiInfo(
				"Demo API",
				"Api demo utilizada en clases",
				"1.1",
				"http://tuti.com/terminosycondiciones",
				new Contact("TUTI", "https://tuti.com", "contactenos@tuti.com"),
				"LICENSE",
				"LICENSE URL",
				Collections.emptyList()
				);
	}
}