package com.prospera.exceptionhandler;

public class EmailNotFoundException extends RuntimeException
{
	public EmailNotFoundException(String msg)
	{
		super(msg);
	}
}
