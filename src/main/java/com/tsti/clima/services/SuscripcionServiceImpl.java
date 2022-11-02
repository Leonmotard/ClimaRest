package com.tsti.clima.services;

import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tsti.clima.entities.Suscripcion;
import com.tsti.clima.exceptions.Excepcion;
import com.tsti.clima.repo.SuscripcionRepo;

@Service
public class SuscripcionServiceImpl implements SuscripcionService {
	
	@Autowired	
	private SuscripcionRepo sRepo;
	
	@Autowired
	private  Validator validator;
	
	@Override
	public List<Suscripcion> getSuscripcionByCiudadId(Long idCiudad){
		return sRepo.findSuscripcionByCiudadId(idCiudad);
	}
	
	@Override
	public void update(Suscripcion suscripcion) throws Excepcion {
		
		Set<ConstraintViolation<Suscripcion>> cv = validator.validate(suscripcion);
		if(cv.size()>0)
		{
			String err="";
			for (ConstraintViolation<Suscripcion> constraintViolation : cv) {
				err+=constraintViolation.getPropertyPath()+": "+constraintViolation.getMessage()+"\n";
			}
			throw new Excepcion(err,400);
		}
		else if(!sRepo.findSuscripcionByEmail(suscripcion.getEmail()).equals(suscripcion))
		{
			throw new Excepcion("No se encuentra la persona que desea modificar.",400);
		}
		else
			sRepo.save(suscripcion);
		
	}
	@Override
	public void insert(Suscripcion suscripcion) throws Exception {
		Set<ConstraintViolation<Suscripcion>> cv = validator.validate(suscripcion);
		if(cv.size()>0)
		{
			String err="";
			for (ConstraintViolation<Suscripcion> constraintViolation : cv) {
				err+=constraintViolation.getPropertyPath()+": "+constraintViolation.getMessage()+"\n";
			}
			throw new Excepcion(err,400);
		}
		else if(sRepo.findSuscripcionByEmail(suscripcion.getEmail()) != null)
		{
			throw new Excepcion("El mail ya se encuentra suscripto.",400);
		}
		else	
			sRepo.save(suscripcion);
	}
	@Override
	public void delete(String mail) throws Excepcion {
		
		if(sRepo.findSuscripcionByEmail(mail) == null)
		{
			throw new Excepcion("No existe una suscripcion con ese mail",400);
		}
		else
			sRepo.deleteById(sRepo.findSuscripcionByEmail(mail).getId());
	}
	
	
	@Override
	public Suscripcion getSuscripcionByEmail(String mail) {
		return sRepo.findSuscripcionByEmail(mail);
	}

}
