package com.tsti.clima.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import com.tsti.clima.entities.Pronostico;
import com.tsti.clima.exceptions.Excepcion;

@Service
public interface PronosticoService {
	/**
	 * Devuelve el pronóstico ingresado para una ciudad y fecha determinadas.
	 * @return pronostico
	 * @param id de la ciudad
	*/
	public Pronostico getPronosticoPorCiudadYFecha(Long id, LocalDate fecha);
	
	/**
	 * Devuelve la lista completa de Pronosticos
	 * @return Lista de Pronosticos
	 */
	public List<Pronostico> getAll();
	
	/**
	 * Devuelve la lista del pronóstico para los diez días que siguen a la fecha de la consulta.
	 * @Param id de la ciudad a consultar
	 * @return Lista de pronóstico extendido
	 */
	public List<Pronostico> getPronosticoExtendido(Long id);

	/**
	 * Obtiene un Pronostico a partir de su identidicador
	 * @param id
	 * @return
	 */
	public Optional<Pronostico> getById(Long id);
	
	

	/**
	 * Actualiza datos de un Pronostico
	 * @param p
	 * @throws Excepcion 
	 */
	public void update(Pronostico pronostico) throws Excepcion;

	/**
	 * Inserta un nuevo Pronostico
	 * @param p
	 * @throws Exception
	 */
	public void insert(Pronostico pronostico) throws Exception;

	/**
	 * Elimina un pronostico del sistema
	 * @param id del pronostico a eliminar
	 * @throws Excepcion 
	 */

	public void delete(Long id) throws Excepcion;
	
}

