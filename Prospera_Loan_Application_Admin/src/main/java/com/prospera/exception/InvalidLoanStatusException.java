package com.prospera.exception;

public class InvalidLoanStatusException extends RuntimeException {

	public InvalidLoanStatusException(String msg)
	{
		super(msg);
	}
}
