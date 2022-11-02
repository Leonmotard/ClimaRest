package com.tsti.clima.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tsti.clima.entities.Ciudad;
import com.tsti.clima.repo.CiudadRepo;
@Service
public class CiudadServiceImpl implements CiudadService{

	@Autowired
	CiudadRepo repoCiudades;
	
	@Override
	public java.util.List<Ciudad> findAll()
	{
		return repoCiudades.findAll();
	}

	@Override
	public Optional<Ciudad> getById(Long id) {

		return repoCiudades.findById(id);
		
	}

	
}
