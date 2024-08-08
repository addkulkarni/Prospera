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
	@ExceptionHandler(value = InvalidIdException.class)
	public ResponseEntity<ApiError> checkEnquiryForIdAndStatus(InvalidIdException e , HttpServletRequest request)
	{
		ApiError error = new ApiError();
		error.setMessage(e.getMessage());
		error.setPath(request.getRequestURI());
		error.setStatusMessage(HttpStatus.NOT_FOUND);
		error.setStatusCode(HttpStatus.NOT_FOUND.value());
		error.setTimeStamp(new Date());
		return new ResponseEntity<ApiError>(error,HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(value = CibilScoreNotGeneratedException.class)
	public ResponseEntity<ApiError> checkloanStatusForOE(CibilScoreNotGeneratedException e , HttpServletRequest request)
	{
		ApiError error = new ApiError();
		error.setMessage(e.getMessage());
		error.setPath(request.getRequestURI());
		error.setStatusMessage(HttpStatus.NOT_FOUND);
		error.setStatusCode(HttpStatus.NOT_FOUND.value());
		error.setTimeStamp(new Date());
	
        return new ResponseEntity<ApiError>(error,HttpStatus.NOT_FOUND);
	}
	
	
	@ExceptionHandler(value = CibilScoreRejectedException.class)
	public ResponseEntity<ApiError> checkloanStatusForRejection(CibilScoreRejectedException e , HttpServletRequest request)
	{
		ApiError error = new ApiError();
		error.setMessage(e.getMessage());
		error.setPath(request.getRequestURI());
		error.setStatusMessage(HttpStatus.NOT_ACCEPTABLE);
		error.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
		error.setTimeStamp(new Date());
		return new ResponseEntity<ApiError>(error,HttpStatus.NOT_ACCEPTABLE);
	}
	
	@ExceptionHandler(value = IdNotPresentinDatabaseException.class)
	public ResponseEntity<ApiError> forwardtoReForOe(IdNotPresentinDatabaseException e , HttpServletRequest request)
	{
		ApiError error = new ApiError();
		error.setMessage(e.getMessage());
		error.setPath(request.getRequestURI());
		error.setStatusMessage(HttpStatus.NOT_FOUND);
		error.setStatusCode(HttpStatus.NOT_FOUND.value());
		error.setTimeStamp(new Date());
		return new ResponseEntity<ApiError>(error,HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(value = IdNotPresentinDatabaseException.class)
	public ApiError forwardtoReForOe(IdNotPresentinDatabaseException e , HttpServletRequest request)
	{
		ApiError error = new ApiError();
		error.setMessage(e.getMessage());
		error.setPath(request.getRequestURI());
		error.setStatusMessage(HttpStatus.NOT_FOUND);
		error.setStatusCode(HttpStatus.NOT_FOUND.value());
		error.setTimeStamp(new Date());
		return error;
	}
	
}
