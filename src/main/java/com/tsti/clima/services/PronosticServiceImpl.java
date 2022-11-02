package com.tsti.clima.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.Optional;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tsti.clima.entities.Pronostico;
import com.tsti.clima.exceptions.Excepcion;
import com.tsti.clima.repo.PronosticoRepo;


@Service
public class PronosticServiceImpl implements PronosticoService {

	@Autowired
	private  Validator validator;
	
	@Autowired
	private PronosticoRepo repoPron;
	
	
	public Pronostico getPronosticoPorCiudadYFecha(Long id, LocalDate fecha) {
		return repoPron.findPronosticoByIdCiudadAndFecha(id, fecha);
		
	}
	
	@Override
	public List<Pronostico> getAll() {
		return repoPron.findAll();
	}
	
	public List<Pronostico> getPronosticoExtendido(Long id){
		return repoPron.findPronosticoExtendidoByCiudad(id);
	}
	
	@Override
	public Optional<Pronostico> getById(Long id) {
		return  repoPron.findById(id);
		
	}
	@Override
	public void update(Pronostico c) throws Excepcion {
		
		Set<ConstraintViolation<Pronostico>> cv = validator.validate(c);
		if(cv.size()>0)
		{
			String err="";
			for (ConstraintViolation<Pronostico> constraintViolation : cv) {
				err+=constraintViolation.getPropertyPath()+": "+constraintViolation.getMessage()+"\n";
			}
			throw new Excepcion(err,400);
		}
		
		else
		
			if(!repoPron.findPronosticoByIdCiudadAndFecha(c.getCiudad().getId(), c.getFechaPronosticada()).equals(c)) 
		{
			throw new Excepcion("No se encuentra el pronostico que desea modificar.",400);
		}
		else
			repoPron.updatePronostico(c.getPorcentajeProbLluvia(),c.getDescripcion(),c.isEventoExtremo(),c.getCiudad().getId(),c.getFechaPronosticada()) ;
		
	}
	
	@Override
	public void insert(Pronostico c) throws Exception {
		Set<ConstraintViolation<Pronostico>> cv = validator.validate(c);
		if(cv.size()>0)
		{
			String err="";
			for (ConstraintViolation<Pronostico> constraintViolation : cv) {
				err+=constraintViolation.getPropertyPath()+": "+constraintViolation.getMessage()+"\n";
			}
			throw new Excepcion(err,400);
		}
		
		else
			repoPron.save(c);
	}
	@Override
	public void delete(Long id) throws Excepcion {
		
		if(!repoPron.findById(id).isPresent())
		{
			throw new Excepcion("No existen los datos climáticos citados",400);
		}
		else
			repoPron.deleteById(id);
	}
	
}
