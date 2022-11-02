package com.tsti.clima.restServices;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.Email;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsti.clima.config.SwaggerConfig;
import com.tsti.clima.entities.Ciudad;
import com.tsti.clima.entities.Suscripcion;
import com.tsti.clima.exceptions.Excepcion;
import com.tsti.clima.restServices.error.MensajeError;
import com.tsti.clima.services.CiudadService;
import com.tsti.clima.services.SuscripcionService;

import io.swagger.annotations.Api;


@RestController
@RequestMapping("/suscripcion")
@Api(tags = { SwaggerConfig.SUSCRIPCION})
public class SuscripcionController {
	
	@Autowired
	SuscripcionService sus;
	
	@Autowired
	CiudadService ciudad;
	
	
	/**
	 *  Graba un mail en suscripcion y la ciudad para la cual desea recibir notificaciones de alerta
	 * 			curl --location --request PUT 'http://localhost:8081/suscripcion/{email}' 
	 *			--header 'Accept: application/json' 
	 * 			--header 'Content-Type: application/json' 
	 *			--data-raw '{
	 *			    "email": "lucianab@hotmail.com",
	 *			    "idCiudad": 1
	 *			}'
	 * @param s Suscripcion a grabar
	 * @return Suscripcion guardada o error en otro caso
	 * @throws Excepcion 
	 */
	@PutMapping("/email")
	public ResponseEntity<Object> guardar( @Valid @RequestBody SuscripcionDTO form, BindingResult result) throws Exception
	{
		
		if(result.hasErrors())
		{
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, this.formatearError(result));
		}
		
		

			Suscripcion s = form.toPojo();
			Optional<Ciudad> c = ciudad.getById(form.getIdCiudad());
			if(c.isPresent())
				s.setCiudad(c.get());
			else
			{
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getError("02", "Ciudad Requerida", "La ciudad indicada no se encuentra en la base de datos."));
			
			}
			
			if(sus.getSuscripcionByEmail(s.getEmail()) != null)
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getError("03", "Mail existente", "El mail ingresado ya se encuentra suscripto."));
			else
				sus.insert(s);
				
				URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{email}")
						.buildAndExpand(s.getEmail()).toUri(); //Por convención en REST, se devuelve la  url del recurso recién creado

				return ResponseEntity.created(location).build();//201 (Recurso creado correctamente)


	}
	
	@DeleteMapping("/{email}")
	public ResponseEntity<String> eliminar(@PathVariable  @Email String email) throws Excepcion
	{
		
		sus.delete(email);
		
		return ResponseEntity.ok().build();
		
		
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

	/**
	 * Crea un error a partir del codigo, titulo 
	 * @param code
	 * @param err
	 * @param descr
	 * @return
	 * @throws JsonProcessingException
	 */
	private String getError(String code, String err, String descr) throws JsonProcessingException
	{
		//NOTA, esta es una implementación simplificada simplemente a los fines de uso en la materia... 
		//La forma correcta de implementar la creación de excepciones es invocando a este método 
		//con un código de excepcion y luego , con ese codigo, buscando la información de la excepcion desde un archivo que nos permita la internacionalización 
		MensajeError e1=new MensajeError();
		e1.setCodigo(code);
		ArrayList<Map<String,String>> errores=new ArrayList<>();
		Map<String, String> error=new HashMap<String, String>();
		error.put(err, descr);
		errores.add(error);
		e1.setMensajes(errores);
		
		//ahora usamos la librería Jackson para pasar el objeto a json
				ObjectMapper maper = new ObjectMapper();
				String json = maper.writeValueAsString(err);
				return json;
	}
	
	

}
