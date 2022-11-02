package com.tsti.clima.restServices;


import com.tsti.clima.entities.Suscripcion;

public class SuscripcionDTO {

	private String email;

	private Long idCiudad;
	
	public SuscripcionDTO() {
	super();
	}

	public SuscripcionDTO(Suscripcion pojo) {
		super();
		this.email = pojo.getEmail();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Long getIdCiudad() {
		return idCiudad;
	}

	public void setIdCiudad(Long idCiudad) {
		this.idCiudad = idCiudad;
	}
	
	public Suscripcion toPojo() {
		Suscripcion sus = new Suscripcion();
		sus.setEmail(this.email);
		return sus;
	}

}
