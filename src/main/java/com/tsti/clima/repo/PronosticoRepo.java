package com.tsti.clima.repo;

import java.time.LocalDate;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tsti.clima.entities.Pronostico;

public interface PronosticoRepo extends JpaRepository<Pronostico, Long> {
	
	

	@Query(value = "SELECT * FROM pronostico p INNER JOIN ciudad ci ON (p.ciudad_id = ci.id) WHERE ci.id = ?1 AND fecha_pronosticada = ?2 order by ci.id limit 1", nativeQuery = true)
	public Pronostico findPronosticoByIdCiudadAndFecha(Long idCiudad, LocalDate fecha);
	
	@Query(value = "SELECT * FROM pronostico p INNER JOIN ciudad ci ON (p.ciudad_id = ci.id) where ci.id = ?1 AND p.fecha_pronosticada BETWEEN CURDATE() AND (CURDATE()+10)", nativeQuery = true)
	public List<Pronostico> findPronosticoExtendidoByCiudad(Long idCiudad);

	@Transactional
	@Modifying
	@Query(value= "UPDATE pronostico p SET p.descripcion = :descripcion, p.evento_extremo = :eventoExtremo, p.porcentaje_prob_lluvia = :porcentajeProbLluvia  where ciudad_id = :idCiudad and fecha_pronosticada = :fechaPronosticada",nativeQuery= true)
	public void updatePronostico(@Param("porcentajeProbLluvia") int porcentajeProbLluvia, @Param("descripcion") String descripcion, @Param("eventoExtremo") boolean eventoExtremo, @Param("idCiudad") Long idCiudad, @Param("fechaPronosticada") LocalDate fechaPronosticada);
	
}
