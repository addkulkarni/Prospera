package com.prospera.servicei;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;

import com.prospera.model.Customer;
import com.prospera.model.Enquiry;

public interface EnquiryServiceI
{
	Optional<Enquiry> getEnquiry(int eid);

	void setEnquiryStatus(int eid);

	ResponseEntity<List<Enquiry>> getAllPendingRegistration();

	

	
}

