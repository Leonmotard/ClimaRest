package com.tsti.clima.restServices;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsti.clima.config.SwaggerConfig;
import com.tsti.clima.entities.Ciudad;
import com.tsti.clima.entities.ClimaActual;
import com.tsti.clima.restServices.error.MensajeError;
import com.tsti.clima.services.CiudadService;
import com.tsti.clima.services.ClimaService;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/clima")
@Api(tags = { SwaggerConfig.CLIMA })
public class ClimaController {
	
	@Autowired 
	private ClimaService service;
	
	@Autowired
	private CiudadService ciudadSer;
	
	@GetMapping(path = {"/estadoActual/{id}"}, produces = { MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ClimaActualDTO> getEstadoActualClima(Model modelo, @PathVariable Long id) throws Exception {
		
		ClimaActual pojo = service.getEstadoActual(id);
        return new ResponseEntity<ClimaActualDTO>(buildResponse(pojo), HttpStatus.OK);



	}
	
	/**
	 * Inserta nuevos datos del clima en la base de datos
	 * 			curl --location --request POST 'http://localhost:8081/clima/estadoActual' 
	 *			--header 'Accept: application/json' 
	 * 			--header 'Content-Type: application/json' 
	 *			--data-raw '{
	 *			    "temperatura": 37.5,
	 *			    "estadoClima": "nevado",
	 *			    "humedad": "87",
	 *			    "idCiudad": 2
	 *			}'
	 * @param c ClimaActual  a insertar
	 * @return ClimaActual insertada o error en otro caso
	 * @throws Exception 
	 */

	@PostMapping
	@RequestMapping("/estadoActual")
	public ResponseEntity<Object> guardar( @Valid @RequestBody ClimaActualDTO form, BindingResult result) throws Exception
	{
		
		if(result.hasErrors())
		{
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, this.formatearError(result));
		}
		
		

			ClimaActual c = form.toPojo();
			Optional<Ciudad> ciu = ciudadSer.getById(form.getIdCiudad());
			if(ciu.isPresent())
				c.setCiudad(ciu.get());
			else
			{
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getError("02", "Ciudad Requerida", "La ciudad indicada no se encuentra en la base de datos."));
//				return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La ciudad indicada no se encuentra en la base de datos.");
			}
			
			
			
				service.insert(c);
				URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
						.buildAndExpand(c.getId()).toUri(); //Por convención en REST, se devuelve la  url del recurso recién creado

				return ResponseEntity.created(location).build();//201 (Recurso creado correctamente)
		
//		}

	}
	
	
	
	private ClimaActualDTO buildResponse(ClimaActual pojo)
	{
		try {
			
			
			ClimaActualDTO dto = new ClimaActualDTO(pojo);
			//Self link
			Link selfLink = WebMvcLinkBuilder.linkTo(ClimaController.class).withSelfRel();
			dto.add(selfLink);
			
			//Method link
			Link ciudadLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CiudadController.class)
			        .getById(pojo.getCiudad().getId()))
			        .withRel("ciudad");
			dto.add(ciudadLink);
			
			//Method link pronostico extendido
			
			Link pronosticoLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PronosticoController.class).getPronosticoExtendido(pojo.getCiudad().getId())).withRel("pronostico Extendido");
			dto.add(pronosticoLink);
			
			return dto;
			
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
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