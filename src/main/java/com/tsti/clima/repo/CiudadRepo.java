package com.tsti.clima.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tsti.clima.entities.Ciudad;

public interface CiudadRepo extends JpaRepository<Ciudad, Long>{

	public List<Ciudad> findByNombreOrCp(String nombre, String cp);
}
