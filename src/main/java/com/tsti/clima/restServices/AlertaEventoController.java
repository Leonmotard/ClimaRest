package com.tsti.clima.restServices;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsti.clima.config.SwaggerConfig;
import com.tsti.clima.entities.EventoExtremo;
import com.tsti.clima.entities.Mail;
import com.tsti.clima.exceptions.Excepcion;
import com.tsti.clima.restServices.error.MensajeError;
import com.tsti.clima.services.AlertaEventoExtremo;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/evento")
@Api(tags = { SwaggerConfig.EVENTO})
public class AlertaEventoController {
	
	@Autowired
	AlertaEventoExtremo alerta;
	
	
	/**
	 *  Envia un mail de alerta de evento extremo a cada miembro de la lista de eventos por ciudad ingresada con la fecha actual. 
	 * 			curl --location --request PUT 'http://localhost:8081/evento' 
	 *			--header 'Accept: application/json' 
	 * 			--header 'Content-Type: application/json' 
	 *			--data-raw '{
	 *			    "descripcion": "Se esperan grandes tormentas...",
	 *			    "idCiudad": 1
	 *			}'
	 * @param 
	 * @return Imprime por consola la lista de mails enviados simulando el envío.
	 * @throws Excepcion 
	 */
	
	@PutMapping("")
	public ResponseEntity<Object> enviar( @Valid @RequestBody EventoExtremo form, BindingResult result) throws Exception
	{
		
		if(result.hasErrors())
		{
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, this.formatearError(result));
		}
		
		else
		{
			alerta.avisoEvento(form.getDescripcion(), form.getIdCiudad());
				
				URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("")
						.buildAndExpand().toUri(); //Por convención en REST, se devuelve la  url del recurso recién creado

				return ResponseEntity.created(location).build();//201 (Recurso creado correctamente)
		}

	}
	
	
	
	private String formatearError(BindingResult result) throws JsonProcessingException
	{
//		primero transformamos la lista de errores devuelta por Java Bean Validation
		List<Map<String, String>> errores= result.getFieldErrors().stream().map(err -> {
															Map<String, String> error= new HashMap<>();
															error.put(err.getField(),err.getDefaultMessage() );
															return error;
														}).collect(Collectors.toList());
		MensajeError e1=new MensajeError();
		e1.setCodigo("01");
		e1.setMensajes(errores);
		
		//ahora usamos la librería Jackson para pasar el objeto a json
		ObjectMapper maper = new ObjectMapper();
		String json = maper.writeValueAsString(e1);
		return json;
	}

	
	
	
	
	

}
