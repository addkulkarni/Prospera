package com.prospera.exception;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler
{
	@ExceptionHandler(value = InvalidIdException.class)
	public ApiError checkEnquiryForIdAndStatus(InvalidIdException e , HttpServletRequest request)
	{
		ApiError error = new ApiError();
		error.setMessage(e.getMessage());
		error.setPath(request.getRequestURI());
		error.setStatusMessage(HttpStatus.NOT_FOUND);
		error.setStatusCode(HttpStatus.NOT_FOUND.value());
		error.setTimeStamp(new Date());
		return error;
	}
	
	@ExceptionHandler(value = CibilScoreNotGeneratedException.class)
	public ApiError checkloanStatusForOE(CibilScoreNotGeneratedException e , HttpServletRequest request)
	{
		ApiError error = new ApiError();
		error.setMessage(e.getMessage());
		error.setPath(request.getRequestURI());
		error.setStatusMessage(HttpStatus.NOT_FOUND);
		error.setStatusCode(HttpStatus.NOT_FOUND.value());
		error.setTimeStamp(new Date());
		return error;
	}
	
	
	@ExceptionHandler(value = CibilScoreRejectedException.class)
	public ApiError checkloanStatusForRejection(CibilScoreRejectedException e , HttpServletRequest request)
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
