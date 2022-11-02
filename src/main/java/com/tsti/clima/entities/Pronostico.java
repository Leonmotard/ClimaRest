package com.tsti.clima.entities;

import java.time.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Pronostico {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;	
	
	@ManyToOne
	private Ciudad ciudad;
	
	@NotNull(message = "Debe ingresar la fecha a pronosticar.")
	private LocalDate fechaPronosticada;
	
	@NotNull(message = "Debe ingresar el porcentaje de probabilidad de precipitaciones.")
	private int porcentajeProbLluvia;
	
	@Size(min = 1,max = 300, message = "Ingrese una descripción del pronóstico")
	private String descripcion;
	
	
	private boolean eventoExtremo;
	
	public Pronostico()
	{
		super();
	}
	
	public Pronostico(Ciudad ciudad, LocalDate fecha, int porcentaje, String descripcion, boolean evento) {
		
		this.ciudad = ciudad;
		this.fechaPronosticada = fecha;
		this.porcentajeProbLluvia = porcentaje;
		this.descripcion = descripcion;
		this.eventoExtremo = evento;
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Ciudad getCiudad() {
		return ciudad;
	}

	public void setCiudad(Ciudad ciudad) {
		this.ciudad = ciudad;
	}

	public LocalDate getFechaPronosticada() {
		return fechaPronosticada;
	}

	public void setFechaPronosticada(LocalDate fechaPronosticada) {
		this.fechaPronosticada = fechaPronosticada;
	}

	public int getPorcentajeProbLluvia() {
		return porcentajeProbLluvia;
	}

	public void setPorcentajeProbLluvia(int porcentajeProbLluvia) {
		this.porcentajeProbLluvia = porcentajeProbLluvia;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public boolean isEventoExtremo() {
		return eventoExtremo;
	}

	public void setEventoExtremo(boolean eventoExtremo) {
		this.eventoExtremo = eventoExtremo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ciudad == null) ? 0 : ciudad.hashCode());
		result = prime * result + ((fechaPronosticada == null) ? 0 : fechaPronosticada.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pronostico other = (Pronostico) obj;
		if (ciudad == null) {
			if (other.ciudad != null)
				return false;
		} else if (!ciudad.equals(other.ciudad))
			return false;
		if (fechaPronosticada == null) {
			if (other.fechaPronosticada != null)
				return false;
		} else if (!fechaPronosticada.equals(other.fechaPronosticada))
			return false;
		return true;
	}

	
	
	
	
}
