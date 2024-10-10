package com.prospera.servicei;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.prospera.model.Customer;
import com.prospera.model.Enquiry;
import com.prospera.model.User;

public interface CustomerServiceI
{
	User saveData(Enquiry e, Customer c);
	
	ResponseEntity<List<Customer>> getAllRegistrationComplete();

	ResponseEntity<String> forwardforverification(int cid);
}
