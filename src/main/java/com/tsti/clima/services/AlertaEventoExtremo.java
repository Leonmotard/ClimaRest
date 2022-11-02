package com.tsti.clima.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tsti.clima.entities.Pronostico;
import com.tsti.clima.entities.Suscripcion;

@Service
public class AlertaEventoExtremo {
	
	@Autowired
	EnvioMailService mail;
	@Autowired
	SuscripcionService sus;
	
	public final String mensaje = "Se le envía el presente mail para alertar el siguiente evento climático extremo:";
	
	public void avisoEvento(String descripcion, Long idCiudad) {
		List<Suscripcion> listaSuscriptores = sus.getSuscripcionByCiudadId(idCiudad);
		
		for(Suscripcion s : listaSuscriptores) {
			mail.enviarMail(s.getEmail(), this.mensaje +" " + descripcion + " Fecha de alerta: " + LocalDate.now().toString());		
	}
	}
	/**
	 * Comprueba si un pronóstico ingresado corresponde a una alerta de evento extremo y en caso de ser asi, imprime por consola
	 * la dirección mail a la que se enviará el mensaje, e imprime el contenido del mismo.
	 * @param Pronostico p
	 */

	public void comprobarEvento(Pronostico p)
	{
		if(p.isEventoExtremo() == true)
		{
			List<Suscripcion> listaSuscriptores = sus.getSuscripcionByCiudadId(p.getCiudad().getId());
			
			for(Suscripcion s : listaSuscriptores) {
				mail.enviarMail(s.getEmail(), this.mensaje + p.getDescripcion() +" "+ p.getCiudad().getNombre() +" " +p.getFechaPronosticada());	
			}
				
				
	
		}
	}
	
	
}
