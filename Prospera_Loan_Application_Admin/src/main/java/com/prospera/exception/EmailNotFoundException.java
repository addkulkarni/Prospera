package com.prospera.exception;

public class EmailNotFoundException extends RuntimeException {
	public EmailNotFoundException(String msg)
	{
		super(msg);
	}
}
