package com.tsti.clima.repo;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.tsti.clima.entities.Suscripcion;

public interface SuscripcionRepo extends JpaRepository<Suscripcion, Long> {
	
	public Suscripcion findSuscripcionByEmail(String mail);
	
	public List<Suscripcion> findSuscripcionByCiudadId(Long idCiudad);
	

}
