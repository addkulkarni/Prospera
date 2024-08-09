package com.prospera.exception;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler
{
	@ExceptionHandler(value = CustomerLoginFailedException.class)
	public ApiError CustomerLoginFailedHandler(CustomerLoginFailedException e, HttpServletRequest request)
	{
		ApiError error = new ApiError();
		error.setMessage(e.getMessage());
		error.setPath(request.getRequestURI());
		error.setStatusCode(HttpStatus.UNAUTHORIZED.value());
		error.setStatusMessage(HttpStatus.UNAUTHORIZED);
		error.setTimeStamp(new Date());
		return error;
	}
}
