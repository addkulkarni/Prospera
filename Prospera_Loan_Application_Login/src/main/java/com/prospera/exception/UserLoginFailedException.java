package com.prospera.exception;

public class UserLoginFailedException extends RuntimeException {
	public UserLoginFailedException (String msg)
	{
		super(msg);
	}
}
