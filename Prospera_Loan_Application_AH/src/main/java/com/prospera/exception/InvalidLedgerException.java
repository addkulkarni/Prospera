package com.prospera.exception;

public class InvalidLedgerException extends RuntimeException
{
	public InvalidLedgerException(String msg)
	{
		super(msg);
	}
}
