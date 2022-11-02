package com.tsti.clima.restServices;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;

import com.tsti.clima.entities.Persona;
import com.tsti.clima.repo.CiudadRepo;

/**
 * Objeto necesario para insertar o eliminar una persona. 
 * Nótese que en lugar de referenciar al objeto Ciudad, reemplaza ese atributo por el idCiudad, lo cual resulta mas sencillo de representar en JSON
 *
 */
public class PersonaDTO {

	@Autowired
	CiudadRepo repo;

	@NotNull(message = "el dni no puede ser nulo")
	@Min(7000000)
	private Long dni;
	@NotNull
	@Size(min=2, max=30)
	private String apellido;
	@NotNull
	@Size(min=2, max=30)
	private String nombre;
	@NotNull
	private Long idCiudad;
	
	public Long getDni() {
		return dni;
	}
	public void setDni(Long dni) {
		this.dni = dni;
	}
	public String getApellido() {
		return apellido;
	}
	public void setApellido(String apellido) {
		this.apellido = apellido;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public Long getIdCiudad() {
		return idCiudad;
	}
	public void setIdCiudad(Long idCiudad) {
		this.idCiudad = idCiudad;
	}
	
	public Persona toPojo()
	{
		Persona p = new Persona();
		p.setDni(this.getDni());
		p.setApellido(this.getApellido());
		p.setNombre(this.getNombre());
		return p;
	}
	
}
