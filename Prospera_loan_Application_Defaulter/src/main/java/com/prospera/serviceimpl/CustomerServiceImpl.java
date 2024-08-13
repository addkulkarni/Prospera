package com.prospera.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prospera.model.Customer;
import com.prospera.repository.CustomerRepository;
import com.prospera.servicei.CustomerServiceI;
@Service
public class CustomerServiceImpl implements CustomerServiceI
{
	@Autowired
	CustomerRepository cr;

	@Override
	public List<Customer> getAllDefaulter() 
	{
		List<Customer> l = cr.findAllByEnquiryEnquiryStatusAndEnquiryLoanStatus("Defaulter","Defaulter");
		return l;
		
	}
	
}