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
import com.tsti.clima.entities.Mail;
import com.tsti.clima.exceptions.Excepcion;
import com.tsti.clima.restServices.error.MensajeError;
import com.tsti.clima.services.EnvioMailService;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/enviar")
@Api(tags = { SwaggerConfig.MAIL})
public class MailController {
	
	
	@Autowired
	EnvioMailService mail;
	
	
	
	
	/**
	 *  Envia un mail ingresando la direccion y el cuerpo del mismo.
	 * 			curl --location --request PUT 'http://localhost:8081/enviar/mail' 
	 *			--header 'Accept: application/json' 
	 * 			--header 'Content-Type: application/json' 
	 *			--data-raw '{
	 *			    "email": "lucianab@hotmail.com",
	 *			    "mensaje": "escribo el presente mail..."
	 *			}'
	 * @param s Suscripcion a grabar
	 * @return Suscripcion guardada o error en otro caso
	 * @throws Excepcion 
	 */
	@PutMapping("/mail")
	public ResponseEntity<Object> enviar( @Valid @RequestBody Mail form, BindingResult result) throws Exception
	{
		
		if(result.hasErrors())
		{
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, this.formatearError(result));
		}
		
		else
			mail.enviarMail(form.getEmail(), form.getMensaje());
				
				URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/mail")
						.buildAndExpand(form.getEmail()).toUri(); //Por convención en REST, se devuelve la  url del recurso recién creado

				return ResponseEntity.created(location).build();//201 (Recurso creado correctamente)


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
