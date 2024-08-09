package com.prospera.servicei;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;

import com.prospera.model.Bank;
import com.prospera.model.Customer;
import com.prospera.model.Document;
import com.prospera.model.Employment;

public interface CustomerServiceI
{
	Customer checkCredentials(String username, String password);

	void updateData(String username, Customer c);

	void changePassword(String username, String password);

	Optional<Customer> getCustomer(String username);

	ResponseEntity<String> forgotPassword(String username);

	ResponseEntity<Bank> getBankDetails(String username, String password);

	ResponseEntity<Employment> getEmploymentDetails(String username, String password);

	ResponseEntity<List<Object>> getAddressDetails(String username, String password);

	ResponseEntity<Document> getDocumentDetails(String username, String password);

	ResponseEntity<Customer> getPersonalDetails(String username, String password);

	void saveData(Customer c);

}