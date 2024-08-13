package com.prospera.servicei;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.prospera.model.Customer;

public interface CustomerServiceI {

	ResponseEntity<List<Customer>> getByLoanStatus(String loanStatus);

	ResponseEntity<List<Customer>> getByEmail(String email);

	ResponseEntity<List<Customer>> getByCustomerEnquiryStatus(String enquiryStatus);

	List<Customer> getAllCustomer();

}
