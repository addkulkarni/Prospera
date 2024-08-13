package com.prospera.exception;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler
{
	@ExceptionHandler(value = InvalidCustomerException.class)
	public ResponseEntity<ApiError> InvalidCustomerHandler(InvalidCustomerException e, HttpServletRequest request)
	{
		ApiError error = new ApiError();
		error.setMessage(e.getMessage());
		error.setPath(request.getRequestURI());
		error.setStatusCode(HttpStatus.NOT_FOUND.value());
		error.setStatusMessage(HttpStatus.NOT_FOUND);
		error.setTimeStamp(new Date());
		ResponseEntity<ApiError> response = new ResponseEntity<>(error,HttpStatus.NOT_FOUND);
		return response;
	}
	
	@ExceptionHandler(value = InvalidLedgerException.class)
	public ResponseEntity<ApiError> InvalidLedgerHandler(InvalidLedgerException e, HttpServletRequest request)
	{
		ApiError error = new ApiError();
		error.setMessage(e.getMessage());
		error.setPath(request.getRequestURI());
		error.setStatusCode(HttpStatus.NOT_FOUND.value());
		error.setStatusMessage(HttpStatus.NOT_FOUND);
		error.setTimeStamp(new Date());
		ResponseEntity<ApiError> response = new ResponseEntity<>(error,HttpStatus.NOT_FOUND);
		return response;
	}
}
