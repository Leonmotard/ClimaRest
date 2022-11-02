package com.tsti.clima.restServices;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tsti.clima.entities.Pronostico;
import com.tsti.clima.repo.CiudadRepo;

public class PronosticoDTO extends RepresentationModel<PronosticoDTO> {

	@Autowired
	CiudadRepo repo;
	
	private Long idCiudad;
	
    @JsonFormat(pattern="yyyy-MM-dd")
	private LocalDate fechaPronosticada;
	
	private int porcentajeProbLluvia;
	
	private String descripcion;
	
	private boolean eventoExtremo;
	
	
	public PronosticoDTO() {
		
	}
	public PronosticoDTO(Pronostico pojo) {
		super();
		fechaPronosticada=pojo.getFechaPronosticada();
		porcentajeProbLluvia=pojo.getPorcentajeProbLluvia();
		descripcion=pojo.getDescripcion();
		eventoExtremo = pojo.isEventoExtremo();
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
	
	
	
	public Long getIdCiudad() {
		return idCiudad;
	}


	public void setIdCiudad(Long idCiudad) {
		this.idCiudad = idCiudad;
	}


	
	
	public boolean isEventoExtremo() {
		return eventoExtremo;
	}
	
	public void setEventoExtremo(boolean eventoExtremo) {
		this.eventoExtremo = eventoExtremo;
	}
	
	public Pronostico toPojo()
	{
		Pronostico c = new Pronostico();
		c.setFechaPronosticada(this.getFechaPronosticada());
		c.setPorcentajeProbLluvia(this.getPorcentajeProbLluvia());;
		c.setDescripcion(this.getDescripcion());
		c.setEventoExtremo(this.isEventoExtremo());
		return c;

	}

}
