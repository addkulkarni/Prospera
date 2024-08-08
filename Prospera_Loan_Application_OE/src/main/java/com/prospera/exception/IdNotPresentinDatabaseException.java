package com.prospera.exception;

public class IdNotPresentinDatabaseException extends RuntimeException
{
  public IdNotPresentinDatabaseException(String msg)
  {
	  super(msg);
  }
}
