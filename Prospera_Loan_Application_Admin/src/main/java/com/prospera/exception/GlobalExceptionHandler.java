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
	@ExceptionHandler(value = InvalidUserException.class)
	public ResponseEntity<ApiError> InvalidUserExceptionHandler(InvalidUserException e, HttpServletRequest request)
	{
		ApiError error = new ApiError();
		error.setMessage(e.getMessage());
		error.setPath(request.getRequestURI());
		error.setStatusCode(HttpStatus.NOT_FOUND.value());
		error.setStatusMessage(HttpStatus.NOT_FOUND);
		error.setTimeStamp(new Date());
		ResponseEntity<ApiError> repsonse = new ResponseEntity<ApiError>(error,HttpStatus.NOT_FOUND);
		return repsonse;
	}
	
	@ExceptionHandler(value = EmailNotFoundException.class)
	public ResponseEntity<ApiError> EmailNotFoundHandler(EmailNotFoundException e, HttpServletRequest request)
	{
		ApiError error = new ApiError();
		error.setMessage(e.getMessage());
		error.setPath(request.getRequestURI());
		error.setStatusCode(HttpStatus.NOT_FOUND.value());
		error.setStatusMessage(HttpStatus.NOT_FOUND);
		error.setTimeStamp(new Date());
		ResponseEntity<ApiError> repsonse = new ResponseEntity<ApiError>(error,HttpStatus.NOT_FOUND);
		return repsonse;
	}
	
	@ExceptionHandler(value = InvalidEnquiryStatusException.class)
	public ResponseEntity<ApiError> InvalidEnquiryStatusHandler(InvalidEnquiryStatusException e, HttpServletRequest request)
	{
		ApiError error = new ApiError();
		error.setMessage(e.getMessage());
		error.setPath(request.getRequestURI());
		error.setStatusCode(HttpStatus.NOT_FOUND.value());
		error.setStatusMessage(HttpStatus.NOT_FOUND);
		error.setTimeStamp(new Date());
		ResponseEntity<ApiError> repsonse = new ResponseEntity<ApiError>(error,HttpStatus.NOT_FOUND);
		return repsonse;
	}
	
	@ExceptionHandler(value = InvalidLoanStatusException.class)
	public ResponseEntity<ApiError> InvalidLoanStatusHandler(InvalidLoanStatusException e, HttpServletRequest request)
	{
		ApiError error = new ApiError();
		error.setMessage(e.getMessage());
		error.setPath(request.getRequestURI());
		error.setStatusCode(HttpStatus.NOT_FOUND.value());
		error.setStatusMessage(HttpStatus.NOT_FOUND);
		error.setTimeStamp(new Date());
		ResponseEntity<ApiError> repsonse = new ResponseEntity<ApiError>(error,HttpStatus.NOT_FOUND);
		return repsonse;
	}
}