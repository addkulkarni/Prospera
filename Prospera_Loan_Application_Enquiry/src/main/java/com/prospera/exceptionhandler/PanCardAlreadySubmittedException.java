package com.prospera.exceptionhandler;

public class PanCardAlreadySubmittedException extends RuntimeException
{
	public PanCardAlreadySubmittedException(String msg)
	{
		super(msg);
	}
}
