package com.tsti.clima.services;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import com.tsti.clima.entities.ClimaActual;
import com.tsti.clima.exceptions.Excepcion;

@Service
public interface ClimaService {
	public ClimaActual getEstadoActual(Long id);
	
	/**
	 * Devuelve la lista completa de estados del clima 	
	 * @return Lista de estados del clima
	 */
	public List<ClimaActual> getAll();

	/**
	 * Obtiene el estado actual del clima a partir de su identidicador
	 * @param id
	 * @return
	 */
	public Optional<ClimaActual> getById(Long id);
	
	

	/**
	 * Actualiza datos del estado del clima actual
	 * @param clima
	 * @throws Excepcion 
	 */
	public void update(ClimaActual clima) throws Excepcion;

	/**
	 * Inserta un nuevo estado actual del clima.
	 * @param clima
	 * @throws Exception
	 */
	public void insert(ClimaActual clima) throws Exception;

	/**
	 * Elimina un estado actual de clima del sistema
	 * @param id del estado a eliminar
	 * @throws Excepcion 
	 */
	public void delete(Long id) throws Excepcion;

	
		
}
