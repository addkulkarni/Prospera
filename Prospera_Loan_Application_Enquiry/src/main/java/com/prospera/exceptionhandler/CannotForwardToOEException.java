package com.prospera.exceptionhandler;

public class CannotForwardToOEException extends RuntimeException
{
	public CannotForwardToOEException(String msg)
	{
		super(msg);
	}
}
