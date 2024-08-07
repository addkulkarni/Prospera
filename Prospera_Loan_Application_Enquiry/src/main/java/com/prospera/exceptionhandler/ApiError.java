package com.prospera.exceptionhandler;

import java.util.Date;

import org.springframework.http.HttpStatus;

import lombok.Data;

@Data
public class ApiError
{
	private String message;
	private Date timeStamp;
	private int statusCode;
	private String path;
	private HttpStatus statusMessage;
}
