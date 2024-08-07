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
}
