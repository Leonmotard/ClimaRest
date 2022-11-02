package com.tsti.clima.entities;

import javax.validation.constraints.Email;

public class Mail {

	@Email
	private String email;
	
	private String mensaje;

	public Mail() {
		
	}
	
	public Mail(@Email String email, String mensaje) {
		super();
		this.email = email;
		this.mensaje = mensaje;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	
	
	
}
