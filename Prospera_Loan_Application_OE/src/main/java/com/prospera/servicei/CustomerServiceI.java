package com.prospera.servicei;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.prospera.model.Customer;

public interface CustomerServiceI
{

	List<Customer> getAllVerificationPending();

	ResponseEntity<String> getVerification(int cid, String loanStatus);

	ResponseEntity<String> forwardToCm(int cid);

	Customer getCustomerDetails(int enquiryId);
}
