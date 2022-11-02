package com.tsti.clima.restServices.error;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.tsti.clima.exceptions.Excepcion;

@RestControllerAdvice
public class ErrorHandler {

   @ExceptionHandler(com.tsti.clima.exceptions.Excepcion.class)
   public ResponseEntity<ErrorInfo> methodArgumentNotValidException(HttpServletRequest request, Excepcion e) {
	   int statusCode= e.getStatusCode();
	   
	   
       ErrorInfo errorInfo = new ErrorInfo(HttpStatus.BAD_REQUEST.value(), e.getMessage(), request.getRequestURL().toString());
       return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
   }
}