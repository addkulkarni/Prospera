package com.prospera.servicei;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.prospera.model.Customer;
import com.prospera.model.Enquiry;

public interface EnquiryServiceI
{

	ResponseEntity<String> calculateCibil(int enquiryID);

	ResponseEntity<List<Enquiry>> getForwaredToOEEnquiries();

	ResponseEntity<List<Enquiry>> getAllCibilApproved();

	ResponseEntity<String> forwardToRE(int enquiryID);


//	ResponseEntity<String> getVerification(int cid, String loanStatus);
	
//	ResponseEntity<String> getVerification(int enquiryID, String loanStatus);
	
	ResponseEntity<List<Customer>> getAllVerificationPending();

	ResponseEntity<String> forwardToCm(int cid);

	ResponseEntity<String> getVerification(int cid, String loanStatus);

//	ResponseEntity<List<Enquiry>> getAllVerificationPending();
}
