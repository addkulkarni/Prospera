package com.prospera.exception;

public class InvalidEnquiryStatusException extends RuntimeException {
   public InvalidEnquiryStatusException(String msg)
   {
	   super(msg);
   }
}
