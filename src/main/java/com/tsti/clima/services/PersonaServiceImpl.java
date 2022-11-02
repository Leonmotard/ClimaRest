package com.tsti.clima.services;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tsti.clima.entities.Persona;
import com.tsti.clima.exceptions.Excepcion;
import com.tsti.clima.repo.PersonaRepo;
@Service
public class PersonaServiceImpl implements PersonaService {
	@Autowired
	private  Validator validator;
	
	@Autowired 
	private PersonaRepo repoPersonas;
	@Override
	public List<Persona> getAll() {
		return repoPersonas.findAll();
	}
	@Override
	public Optional<Persona> getById(Long id) {
		return  repoPersonas.findById(id);
		
	}
	@Override
	public void update(Persona p) throws Excepcion {
		
		Set<ConstraintViolation<Persona>> cv = validator.validate(p);
		if(cv.size()>0)
		{
			String err="";
			for (ConstraintViolation<Persona> constraintViolation : cv) {
				err+=constraintViolation.getPropertyPath()+": "+constraintViolation.getMessage()+"\n";
			}
			throw new Excepcion(err,400);
		}
		else if(!repoPersonas.findById(p.getDni()).isPresent())
		{
			throw new Excepcion("No se encuentra la persona que desea modificar.",400);
		}
		else
			repoPersonas.save(p);
	}
	@Override
	public void insert(Persona p) throws Exception {
		Set<ConstraintViolation<Persona>> cv = validator.validate(p);
		if(cv.size()>0)
		{
			String err="";
			for (ConstraintViolation<Persona> constraintViolation : cv) {
				err+=constraintViolation.getPropertyPath()+": "+constraintViolation.getMessage()+"\n";
			}
			throw new Excepcion(err,400);
		}
		else if(repoPersonas.findById(p.getDni()).isPresent())
		{
			throw new Excepcion("La persona ya existe con el dni="+p.getDni()+" ya existe",400);
		}
		else
			repoPersonas.save(p);
	}
	@Override
	public void delete(Long id) throws Excepcion {
		
		if(!repoPersonas.findById(id).isPresent())
		{
			throw new Excepcion("No existe una persona con ese dni",400);
		}
		else
			repoPersonas.deleteById(id);
	}
	@Override
	public List<Persona> filtrar(String apellido, String nombre) {
		if(apellido==null && nombre==null)
			return repoPersonas.findAll();
		else
			return repoPersonas.findByApellidoOrNombre(apellido, nombre);
	}

}
