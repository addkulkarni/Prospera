package com.prospera.exceptionhandler;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler
{
	@ExceptionHandler(value = EmailAlreadyRegisteredException.class)
	public ResponseEntity<ApiError> EmailAlreadyRegisteredHandler(EmailAlreadyRegisteredException e, HttpServletRequest request)
	{
		ApiError error=new ApiError();
		error.setMessage("message");
		error.setMessage(e.getMessage());
		error.setPath(request.getRequestURI());
		error.setStatusCode(HttpStatus.NOT_FOUND.value());
		error.setStatusMessage(HttpStatus.NOT_FOUND);
		error.setTimeStamp(new Date());
		return new ResponseEntity<ApiError>(error,HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(value = EnquiryIdNotFoundException.class)
	public ResponseEntity<ApiError> enquiryIdNotFouneExHandler(EnquiryIdNotFoundException e,HttpServletRequest request)
	{
		ApiError error=new ApiError();
		error.setMessage(e.getMessage());
		error.setPath(request.getRequestURI());
		error.setStatusCode(HttpStatus.NOT_FOUND.value());
		error.setStatusMessage(HttpStatus.NOT_FOUND);
		error.setTimeStamp(new Date());
		return new ResponseEntity<ApiError>(error,HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(value = PanCardAlreadySubmittedException.class)
	public ResponseEntity<ApiError> panCardExistsHandler(PanCardAlreadySubmittedException e, HttpServletRequest request)
	{
		ApiError error = new ApiError();
		error.setMessage(e.getMessage());
		error.setPath(request.getRequestURI());
		error.setStatusCode(HttpStatus.ALREADY_REPORTED.value());
		error.setStatusMessage(HttpStatus.ALREADY_REPORTED);
		error.setTimeStamp(new Date());
		return new ResponseEntity<ApiError>(error, HttpStatus.ALREADY_REPORTED);
	}
	
	@ExceptionHandler(value = CannotForwardToOEException.class)
	public ResponseEntity<ApiError> CannotForwardToOEHandler(CannotForwardToOEException e, HttpServletRequest request)
	{
		ApiError error = new ApiError();
		error.setMessage(e.getMessage());
		error.setPath(request.getRequestURI());
		error.setStatusCode(HttpStatus.ALREADY_REPORTED.value());
		error.setStatusMessage(HttpStatus.ALREADY_REPORTED);
		error.setTimeStamp(new Date());
		return new ResponseEntity<ApiError>(error, HttpStatus.ALREADY_REPORTED);
	}
	
	@ExceptionHandler(value = EmailNotFoundException.class)
	public ResponseEntity<ApiError> EmailNotFoundHandler(EmailNotFoundException e,HttpServletRequest request)
	{
		ApiError error=new ApiError();
		error.setMessage(e.getMessage());
		error.setPath(request.getRequestURI());
		error.setStatusCode(HttpStatus.NOT_FOUND.value());
		error.setStatusMessage(HttpStatus.NOT_FOUND);
		error.setTimeStamp(new Date());
		return new ResponseEntity<ApiError>(error,HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(value = MethodArgumentNotValidException.class)
	public ResponseEntity<ApiError> MethodArgumentNotValidHandler(MethodArgumentNotValidException e,HttpServletRequest request)
	{
		ApiError error=new ApiError();
		error.setMessage(e.getMessage());
		error.setPath(request.getRequestURI());
		error.setStatusCode(HttpStatus.NOT_FOUND.value());
		error.setStatusMessage(HttpStatus.NOT_FOUND);
		error.setTimeStamp(new Date());
		return new ResponseEntity<ApiError>(error,HttpStatus.NOT_FOUND);
	}
}
