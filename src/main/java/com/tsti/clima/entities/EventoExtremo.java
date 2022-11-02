package com.tsti.clima.entities;

public class EventoExtremo {

	private Long idCiudad;
	
	private String descripcion;

	public EventoExtremo() {
		
	}
	
	public EventoExtremo(Long idCiudad, String descripcion) {
		super();
		this.idCiudad = idCiudad;
		this.descripcion = descripcion;
	}

	public Long getIdCiudad() {
		return idCiudad;
	}

	public void setIdCiudad(Long idCiudad) {
		this.idCiudad = idCiudad;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	
	
	
}
