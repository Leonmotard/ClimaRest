package com.tsti.clima.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.tsti.clima.entities.ClimaActual;

public interface ClimaActualRepo extends JpaRepository<ClimaActual, Long>{
 
	@Query(value = "SELECT * FROM clima_actual clima INNER JOIN ciudad ci ON (clima.ciudad_id = ci.id) where ci.id = ?1 order by clima.id desc limit 1", nativeQuery = true)
	public ClimaActual findClimaActualByIdCiudad(Long idCiudad);
	
}
