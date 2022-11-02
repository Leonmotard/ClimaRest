package com.tsti.clima.restServices;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsti.clima.config.SwaggerConfig;
import com.tsti.clima.entities.Ciudad;
import com.tsti.clima.entities.Persona;
import com.tsti.clima.exceptions.Excepcion;
import com.tsti.clima.restServices.error.MensajeError;
import com.tsti.clima.services.CiudadService;
import com.tsti.clima.services.PersonaService;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
/**
 * Recurso Personas
 * @author dardo
 *
 */
@RestController
@RequestMapping("/personas")
@Api(tags = { SwaggerConfig.PERSONAS })
@Validated
public class PersonaController {
	
	@Autowired
	private PersonaService service; 
	@Autowired
	private CiudadService ciudadService;
	
	
	/**
	 * Permite filtrar personas. 
	 * Ej1 curl --location --request GET 'http://localhost:8081/personas?apellido=Perez&&nombre=Juan' Lista las personas llamadas Perez, Juan
	 * Ej2 curl --location --request GET 'http://localhost:8081/personas?apellido=Perez' Lista aquellas personas de apellido PErez
	 * Ej3 curl --location --request GET 'http://localhost:8081/personas'   Lista todas las personas
	 * @param apellido
	 * @param nombre
	 * @return
	 * @throws Excepcion 
	 */
	@Operation(summary = "Permite filtrar personas. ")
	@GetMapping( produces = { MediaType.APPLICATION_JSON_VALUE})
	public List<PersonaResponseDTO> filtrarPersonas(@RequestParam(name = "apellido",required = false) String apellido 
			, @RequestParam(name = "nombre",required = false)  @javax.validation.constraints.Size(min = 1, max = 20) String nombre) throws Excepcion {
		
		List<Persona> personas = service.filtrar(apellido,nombre);
		List<PersonaResponseDTO> dtos=new ArrayList<PersonaResponseDTO>();
		for (Persona pojo : personas) {
			
	        dtos.add(buildResponse(pojo));
		}
		return dtos;

	}
	
	


	/**
	 * Busca una persona a partir de su dni
	 * 	curl --location --request GET 'http://localhost:8081/personas/27837171'
	 * @param id DNI de la persona buscada
	 * @return Persona encontrada o Not found en otro caso
	 * @throws Excepcion 
	 */
	@GetMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<PersonaResponseDTO> getById(@PathVariable Long id) throws Excepcion
	{
		Optional<Persona> rta = service.getById(id);
		if(rta.isPresent())
		{
			Persona pojo=rta.get();
			return new ResponseEntity<PersonaResponseDTO>(buildResponse(pojo), HttpStatus.OK);
		}
		else
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		
	}
	
	
	/**
	 * Inserta una nueva persona en la base de datos
	 * 			curl --location --request POST 'http://localhost:8081/personas' 
	 *			--header 'Accept: application/json' 
	 * 			--header 'Content-Type: application/json' 
	 *			--data-raw '{
	 *			    "dni": 27837171,
	 *			    "apellido": "perez",
	 *			    "nombre": "juan",
	 *			    "idCiudad": 2
	 *			}'
	 * @param p Persona  a insertar
	 * @return Persona insertada o error en otro caso
	 * @throws Exception 
	 */
	@PostMapping
	public ResponseEntity<Object> guardar( @Valid @RequestBody PersonaDTO form, BindingResult result) throws Exception
	{
		
		if(result.hasErrors())
		{
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, this.formatearError(result));
		}
		
		 

			Persona p = form.toPojo();
			Optional<Ciudad> c = ciudadService.getById(form.getIdCiudad());
			if(c.isPresent())
				p.setCiudad(c.get());
			else
			{
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getError("02", "Ciudad Requerida", "La ciudad indicada no se encuentra en la base de datos."));
//				return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La ciudad indicada no se encuentra en la base de datos.");
			}
			
			
			
				service.insert(p);
				URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{dni}")
						.buildAndExpand(p.getDni()).toUri(); //Por convención en REST, se devuelve la  url del recurso recién creado

				return ResponseEntity.created(location).build();//201 (Recurso creado correctamente)
		
//		}

	}
	
	/**
	 * Modifica una persona existente en la base de datos:
	 * 			curl --location --request PUT 'http://localhost:8081/personas/27837176' 
	 *			--header 'Accept: application/json' 
	 * 			--header 'Content-Type: application/json' 
	 *			--data-raw '{
	 *			    "apellido": "Perez",
	 *			    "nombre": "Juan Martin",
	 *			    "idCiudad": 1
	 *			}'
	 * @param p Persona a modificar
	 * @return Persona Editada o error en otro caso
	 * @throws Excepcion 
	 */
	@PutMapping("/{dni}")
	public ResponseEntity<Object>  actualizar(@RequestBody PersonaDTO form, @PathVariable long dni) throws Excepcion
	{
		
		
			Persona p = form.toPojo();
			Optional<Ciudad> c = ciudadService.getById(form.getIdCiudad());
			if(c.isPresent())
				p.setCiudad(c.get());
			else
				return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La ciudad indicada no se encuentra en la base de datos.");
			
			p.setDni(dni);  //El dni es el identificador, con lo cual es el único dato que no permito modificar
			service.update(p);
			return ResponseEntity.ok(buildResponse(p));
		

	}
	/**
	 * Borra la persona con el dni indicado
	 * 	  curl --location --request DELETE 'http://localhost:8081/personas/27837176'
	 * @param dni Dni de la persona a borrar
	 * @return ok en caso de borrar exitosamente la persona, error en otro caso
	 * @throws Excepcion 
	 */
	@DeleteMapping("/{dni}")
	public ResponseEntity<String> eliminar(@PathVariable  @Min(7000000) Long dni) throws Excepcion
	{
		
		service.delete(dni);
		
		return ResponseEntity.ok().build();
		
		
	}
	
	
	
	
	/**
	 * Métdo auxiliar que toma los datos del pojo y construye el objeto a devolver en la response, con su hipervinculos
	 * @param pojo
	 * @return
	 * @throws Excepcion 
	 */
	private PersonaResponseDTO buildResponse(Persona pojo) throws Excepcion {
		try {
			PersonaResponseDTO dto = new PersonaResponseDTO(pojo);
			 //Self link
			Link selfLink = WebMvcLinkBuilder.linkTo(PersonaController.class)
										.slash(pojo.getDni())                
										.withSelfRel();
			//Method link: Link al servicio que permitirá navegar hacia la ciudad relacionada a la persona
			Link ciudadLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CiudadController.class)
			       													.getById(pojo.getCiudad().getId()))
			       													.withRel("ciudad");
			dto.add(selfLink);
			dto.add(ciudadLink);
			return dto;
		} catch (Exception e) {
			throw new Excepcion(e.getMessage(), 500);
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
