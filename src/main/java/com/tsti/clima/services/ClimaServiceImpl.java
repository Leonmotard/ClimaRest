package com.tsti.clima.services;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tsti.clima.entities.ClimaActual;
import com.tsti.clima.exceptions.Excepcion;
import com.tsti.clima.repo.ClimaActualRepo;

@Service
public class ClimaServiceImpl implements ClimaService{
	
	@Autowired
	private  Validator validator;
	
	@Autowired
	private ClimaActualRepo repoClima;
	

	
	public ClimaActual getEstadoActual(Long id) {
		return repoClima.findClimaActualByIdCiudad(id);
	}
	
	@Override
	public List<ClimaActual> getAll() {
		return repoClima.findAll();
	}
	@Override
	public Optional<ClimaActual> getById(Long id) {
		return  repoClima.findById(id);
		
	}
	@Override
	public void update(ClimaActual c) throws Excepcion {
		
		Set<ConstraintViolation<ClimaActual>> cv = validator.validate(c);
		if(cv.size()>0)
		{
			String err="";
			for (ConstraintViolation<ClimaActual> constraintViolation : cv) {
				err+=constraintViolation.getPropertyPath()+": "+constraintViolation.getMessage()+"\n";
			}
			throw new Excepcion(err,400);
		}
		
		
		else
			repoClima.save(c);
	}
	
	@Override
	public void insert(ClimaActual c) throws Exception {
		Set<ConstraintViolation<ClimaActual>> cv = validator.validate(c);
		if(cv.size()>0)
		{
			String err="";
			for (ConstraintViolation<ClimaActual> constraintViolation : cv) {
				err+=constraintViolation.getPropertyPath()+": "+constraintViolation.getMessage()+"\n";
			}
			throw new Excepcion(err,400);
		}
		
		else
			repoClima.save(c);
	}
	@Override
	public void delete(Long id) throws Excepcion {
		
		if(!repoClima.findById(id).isPresent())
		{
			throw new Excepcion("No existen los datos climáticos citados",400);
		}
		else
			repoClima.deleteById(id);
	}
	
	
		
}
