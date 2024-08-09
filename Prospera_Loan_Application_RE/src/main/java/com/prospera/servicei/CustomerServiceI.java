package com.prospera.servicei;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.prospera.model.Customer;
import com.prospera.model.Enquiry;

public interface CustomerServiceI
{
	void saveData(Enquiry e, Customer c);
	
	ResponseEntity<List<Customer>> getAllRegistrationComplete();

	ResponseEntity<String> forwardforverification(int cid);
}
