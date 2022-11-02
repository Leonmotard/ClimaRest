package com.tsti.clima.restServices;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.RepresentationModel;
import com.tsti.clima.entities.ClimaActual;
import com.tsti.clima.repo.CiudadRepo;

public class ClimaActualDTO extends RepresentationModel<ClimaActualDTO>
{
	@Autowired
	CiudadRepo repo;
	
	private Double temperatura;
	
	private String estadoClima;
	
	private Double humedad;
	
	private Long idCiudad;
	
	
	public ClimaActualDTO() {
		
	}
	public ClimaActualDTO(ClimaActual pojo) {
		super();
		temperatura=pojo.getTemperatura();
		estadoClima=pojo.getEstadoClima();
		humedad=pojo.getHumedad();
	}


	public Double getTemperatura() {
		return temperatura;
	}

	public void setTemperatura(Double temperatura) {
		this.temperatura = temperatura;
	}

	public String getEstadoClima() {
		return estadoClima;
	}

	public void setEstadoClima(String estadoClima) {
		this.estadoClima = estadoClima;
	}

	public Double getHumedad() {
		return humedad;
	}

	public void setHumedad(Double humedad) {
		this.humedad = humedad;
	}
	
	
	
	public Long getIdCiudad() {
		return idCiudad;
	}


	public void setIdCiudad(Long idCiudad) {
		this.idCiudad = idCiudad;
	}


	public ClimaActual toPojo()
	{
		ClimaActual c = new ClimaActual();
		c.setTemperatura(this.getTemperatura());
		c.setEstadoClima(this.getEstadoClima());;
		c.setHumedad(this.getHumedad());
		return c;

	}
}
