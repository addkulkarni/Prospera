package com.prospera.exceptionhandler;

public class EnquiryIdNotFoundException extends RuntimeException
{
  public EnquiryIdNotFoundException(String msg)
  {
	  super(msg);
  }
}
