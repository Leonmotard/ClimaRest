package com.tsti.clima.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Persona {


	@Id
	private Long dni;
	@NotNull
	@Size(min = 1,max = 100, message = "Debe completar el apellido")
	private String apellido;
	
	private String nombre;
	

	@Email(message = "El e-mail ingresado no es valido")
	private String email;
	
	@ManyToOne
	private Ciudad ciudad;
	
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
	
	
	
	@Override
	public String toString() {
		return dni+" - "+ nombre +" "+ apellido;
	}
	
	
	
	public Ciudad getCiudad() {
		return ciudad;
	}
	public void setCiudad(Ciudad ciudad) {
		this.ciudad = ciudad;
	}
	
	
}
