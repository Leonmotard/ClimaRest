package com.tsti.clima.restServices;

import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
import com.tsti.clima.entities.Pronostico;
import com.tsti.clima.restServices.error.MensajeError;
import com.tsti.clima.services.AlertaEventoExtremo;
import com.tsti.clima.services.CiudadService;
import com.tsti.clima.services.PronosticoService;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/clima/pronostico")
@Api(tags = { SwaggerConfig.PRONOSTICO})
public class PronosticoController {
	
	@Autowired 
	private PronosticoService service;
	
	@Autowired
	private CiudadService ciudadSer;
	
	@Autowired
	AlertaEventoExtremo alerta;
	
	@GetMapping(path = {"/{id}"}, produces = { MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<PronosticoDTO> getPronosticoClima( @PathVariable Long id) throws Exception {
		
		LocalDate fecha = LocalDate.now();
		Pronostico rta = service.getPronosticoPorCiudadYFecha(id, fecha);
		if(rta != null)
		{
			return new ResponseEntity<PronosticoDTO>(buildResponse(rta), HttpStatus.OK);
		}
		else
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();



	}

	/**
	 * Obtiene la lista de pronósticos de los próximos diez días.
	 *  curl --location --request GET 'http://localhost:8081/clima/pronostico/extendido/{idCiudad}'
	 * @param id de la ciudad a buscar
	 * @return Lista de pronóstico extendido
	 * @throws Exception 
	 */
	@GetMapping(value = "/extendido/{id}", produces = { MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<List<Pronostico>> getPronosticoExtendido(@PathVariable Long id) throws Exception
	{
		List<Pronostico> rta=service.getPronosticoExtendido(id);
		return new ResponseEntity<List<Pronostico>>(rta,HttpStatus.OK);
	}
	
	/**4
	 * Inserta nuevos datos del clima en la base de datos, si existe un pronóstico para la ciudad y fecha ingresados, el mismo se actualizará en la base de datos. 
	 * 			curl --location --request POST 'http://localhost:8081/clima/pronostico' 
	 *			--header 'Accept: application/json' 
	 * 			--header 'Content-Type: application/json' 
	 *			--data-raw '{
	 *			    "idCiudad": 2,
	 *				"fechaPronosticada": 2022-03-26,
	 *			    "porcentajeProbLluvia": 78,
	 *				"descripcion": "Se anuncian posibles tormentas, caída de granizo..."
	 *			    "eventoExtremo": True
	 *			    
	 *			}'
	 * @param c Pronostico  a insertar
	 * @return Pronostico insertada o error en otro caso
	 * @throws Exception 
	 */

	@PostMapping
//	@RequestMapping("/Pronostico")
	public ResponseEntity<Object> guardarOActualizar( @Valid @RequestBody PronosticoDTO form, BindingResult result) throws Exception
	{
		
		if(result.hasErrors())
		{
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, this.formatearError(result));
		}
		
		

			Pronostico c = form.toPojo();
			Optional<Ciudad> ciu = ciudadSer.getById(form.getIdCiudad());
			if(ciu.isPresent())
				c.setCiudad(ciu.get());
			else
			{
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getError("02", "Ciudad Requerida", "La ciudad indicada no se encuentra en la base de datos."));
//				return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La ciudad indicada no se encuentra en la base de datos.");
			}
						
				if(service.getPronosticoPorCiudadYFecha(c.getCiudad().getId(), c.getFechaPronosticada()).equals(c))
				service.update(c);
				else
					service.insert(c);
				alerta.comprobarEvento(c);	
				URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
						.buildAndExpand(c.getId()).toUri(); //Por convención en REST, se devuelve la  url del recurso recién creado

				return ResponseEntity.created(location).build();//201 (Recurso creado correctamente)
				
			
		
				


	}
	
	
	
	private PronosticoDTO buildResponse(Pronostico pojo)
	{
		try {
			
			
			PronosticoDTO dto = new PronosticoDTO(pojo);
			//Self link
			Link selfLink = WebMvcLinkBuilder.linkTo(ClimaController.class).withSelfRel();
			dto.add(selfLink);
			
			//Method link
			Link ciudadLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CiudadController.class)
			        .getById(pojo.getCiudad().getId()))
			        .withRel("ciudad");
			dto.add(ciudadLink);
			
			//Method link ClimaActual
		//	Link climaActualLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ClimaController.class)
			//		.getEstadoActualClima(null, pojo.getCiudad().getId()));
	//		dto.add(climaActualLink);
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