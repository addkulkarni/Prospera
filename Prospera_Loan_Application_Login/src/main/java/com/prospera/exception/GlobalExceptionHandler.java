package com.prospera.exception;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler
{
	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

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
	@ExceptionHandler(value = UserLoginFailedException.class)
	public ApiError UserLoginFailedHandler(UserLoginFailedException e, HttpServletRequest request)
	{
		ApiError error = new ApiError();
		logger.error("Login failed: {}", e.getMessage());
		error.setMessage(e.getMessage());
		error.setPath(request.getRequestURI());
		error.setStatusCode(HttpStatus.UNAUTHORIZED.value());
		error.setStatusMessage(HttpStatus.UNAUTHORIZED);
		error.setTimeStamp(new Date());
		return error;
	}
}
