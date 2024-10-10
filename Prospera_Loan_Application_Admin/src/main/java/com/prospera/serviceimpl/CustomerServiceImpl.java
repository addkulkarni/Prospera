package com.prospera.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.prospera.exception.EmailNotFoundException;
import com.prospera.exception.InvalidEnquiryStatusException;
import com.prospera.exception.InvalidLoanStatusException;
import com.prospera.model.Customer;
import com.prospera.model.Enquiry;
import com.prospera.repository.CustomerRepository;
import com.prospera.repository.EnquiryRepository;
import com.prospera.servicei.CustomerServiceI;
@Service
public class CustomerServiceImpl implements CustomerServiceI {

	
	@Autowired
	CustomerRepository cr;
	
	
	@Override
	public ResponseEntity<List<Customer>> getByEmail(String email) {
		 
		List<Customer> l  = cr.findAllByEnquiryEmail(email);
		if(!(l.isEmpty()))
		{
			
			ResponseEntity<List<Customer>> response = new ResponseEntity<>(l, HttpStatus.OK);
			return response;
		}
		else
		{
			throw new EmailNotFoundException("No enquiry with the given email found");
		}
	}
	@Override
	public ResponseEntity<List<Customer>> getByLoanStatus(String loanStatus) {
		List<Customer> cust=cr.findAllByEnquiryLoanStatus(loanStatus);
		if(!(cust.isEmpty()))
		{	
			return new ResponseEntity<>(cust,HttpStatus.OK);
		}
		
		else 
			{
				throw new InvalidLoanStatusException(" loanstatus Not Found ");
			}
	}
	@Override
	public ResponseEntity<List<Customer>> getByCustomerEnquiryStatus(String enquiryStatus) {
		List<Customer> cust= cr.findAllByEnquiryEnquiryStatus(enquiryStatus);
		if(!(cust.isEmpty()))
		{
			return new ResponseEntity<>(cust,HttpStatus.OK);
		}
		else
		{
			throw new InvalidEnquiryStatusException("Enquiry status not found ");
		}
	}
	@Override
	public List<Customer> getAllCustomer() {
		List<Customer>l=cr.findAll();
		return l;
	}
	@Override
	public List<Customer> getByCustomerCibilscore() {
		
	    List<Customer> l=cr.findAll();
	    return l;
	}
	@Override
	public List<Customer> getByCustomerAge()
	{
		List<Customer> l=cr.findAll();
		return l;
	}
	
			
		
}

