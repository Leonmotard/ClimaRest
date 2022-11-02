package com.tsti.clima.services;

import java.util.List;

import org.springframework.stereotype.Service;
import com.tsti.clima.entities.Suscripcion;
import com.tsti.clima.exceptions.Excepcion;

@Service
public interface SuscripcionService {
	
	/**
	 * Devuelve la lista de suscripciones para una ciudad determinada.
	 * @param idCiudad
	 * @return List<Suscripcion>
	 */
	
	public List<Suscripcion> getSuscripcionByCiudadId(Long idCiudad);
	
	/** 
	 * Devuelve una suscripción dada una dirección de mail específica.
	 * @param mail
	 * @return Suscripcion
	 */
	public Suscripcion getSuscripcionByEmail(String mail);
	
	
	/**
	 * Actualiza datos de un mail a suscribir
	 * @param suscripcion
	 * @throws Excepcion 
	 */
	public void update(Suscripcion suscripcion) throws Excepcion;

	/**
	 * Inserta un nuevo mail a la suscripción
	 * @param p
	 * @throws Exception
	 */
	public void insert(Suscripcion suscripcion) throws Exception;

	/**
	 * Elimina un mail de la suscripcion
	 * @param id del mail a eliminar
	 * @throws Excepcion 
	 */

	public void delete(String mail) throws Excepcion;
}
