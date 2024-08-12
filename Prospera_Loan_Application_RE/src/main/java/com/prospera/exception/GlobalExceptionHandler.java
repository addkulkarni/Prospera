package com.prospera.exception;

import java.util.Date;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler
{
	@ExceptionHandler(value=InvalidEnquiryIDException.class)
	public ResponseEntity<ApiError> InvalidEnquiryIDHandler(InvalidEnquiryIDException e, HttpServletRequest request)
	{
		ApiError error = new ApiError();
		error.setMessage(e.getMessage());
		error.setPath(request.getRequestURI());
		error.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
		error.setStatusMessage(HttpStatus.NOT_ACCEPTABLE);
		error.setTimeStamp(new Date());
		ResponseEntity<ApiError> response = new ResponseEntity<ApiError>(error, HttpStatus.NOT_ACCEPTABLE);
		return response;
	}
	
	@ExceptionHandler(value=MethodArgumentNotValidException.class)
	public ResponseEntity<ApiError> MethodArgumentNotValidHandler(MethodArgumentNotValidException e, HttpServletRequest request)
	{
		ApiError error = new ApiError();
		error.setMessage(e.getMessage());
		error.setPath(request.getRequestURI());
		error.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
		error.setStatusMessage(HttpStatus.NOT_ACCEPTABLE);
		error.setTimeStamp(new Date());
		ResponseEntity<ApiError> response = new ResponseEntity<ApiError>(error, HttpStatus.NOT_ACCEPTABLE);
		return response;
	}
	
	@ExceptionHandler(value=DataIntegrityViolationException.class)
	public ResponseEntity<ApiError> DataIntegrityViolationHandler(DataIntegrityViolationException e, HttpServletRequest request)
	{
		ApiError error = new ApiError();
		error.setMessage(e.getMessage());
		error.setPath(request.getRequestURI());
		error.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
		error.setStatusMessage(HttpStatus.NOT_ACCEPTABLE);
		error.setTimeStamp(new Date());
		ResponseEntity<ApiError> response = new ResponseEntity<ApiError>(error, HttpStatus.NOT_ACCEPTABLE);
		return response;
	}
	@ExceptionHandler(value=InvalidCustomerException.class)
	public ResponseEntity<ApiError> InvalidCustomerExceptionHandler(InvalidCustomerException e, HttpServletRequest request)
	{
		ApiError error = new ApiError();
		error.setMessage(e.getMessage());
		error.setPath(request.getRequestURI());
		error.setStatusCode(HttpStatus.NOT_FOUND.value());
		error.setStatusMessage(HttpStatus.NOT_FOUND);
		error.setTimeStamp(new Date());
		ResponseEntity<ApiError> response = new ResponseEntity<ApiError>(error, HttpStatus.NOT_FOUND);
		return response;
	}
}
