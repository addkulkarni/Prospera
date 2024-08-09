package com.prospera.servicei;

import java.util.List;
import org.springframework.http.ResponseEntity;

import com.prospera.model.Enquiry;

public interface EnquiryServiceI {

	ResponseEntity<String> updateEnquiry(int enquiryID, Enquiry e);

	ResponseEntity<String> addEnquiry(Enquiry e);

	ResponseEntity<String> deleteById(int enquiryID);

	List<Enquiry> getallenquiry();
	
	ResponseEntity<Enquiry> getById(int enquiryID);

	List<Enquiry> getEnquiryByLoanStatus(String loanStatus);

	List<Enquiry> getEnquiryByEnquiryStatus(String enquiryStatus);

	ResponseEntity<String> forwardToOE(int enquiryID);

	ResponseEntity<List<Enquiry>> getByEmail(String email);
}
