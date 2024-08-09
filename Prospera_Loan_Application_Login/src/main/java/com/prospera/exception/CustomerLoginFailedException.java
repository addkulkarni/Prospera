package com.prospera.exception;

public class CustomerLoginFailedException extends RuntimeException
{
	public CustomerLoginFailedException(String msg)
	{
		super(msg);
	}
}
