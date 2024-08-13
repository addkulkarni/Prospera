package com.prospera.servicei;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.prospera.model.Enquiry;

public interface EnquiryServiceI {

	ResponseEntity<List<Enquiry>> getByEnquiryLoanStatus(String loanStatus);

	ResponseEntity<List<Enquiry>> getByEnquiryenquiryStatus(String enquiryStatus);

	List<Enquiry> getallenquiries();

}


