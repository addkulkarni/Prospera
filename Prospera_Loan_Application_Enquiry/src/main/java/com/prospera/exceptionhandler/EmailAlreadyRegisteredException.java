package com.prospera.exceptionhandler;

public class EmailAlreadyRegisteredException extends RuntimeException
{
	public EmailAlreadyRegisteredException(String msg)
	{
		super(msg);
	}
}
