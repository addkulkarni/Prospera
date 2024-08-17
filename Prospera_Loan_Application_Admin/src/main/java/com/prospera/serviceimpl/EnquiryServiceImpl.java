package com.prospera.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.prospera.exception.InvalidEnquiryStatusException;
import com.prospera.exception.InvalidLoanStatusException;
import com.prospera.model.Enquiry;
import com.prospera.repository.EnquiryRepository;
import com.prospera.servicei.EnquiryServiceI;

@Service
public class EnquiryServiceImpl implements EnquiryServiceI {

	@Autowired
	EnquiryRepository er;
	@Override
	public ResponseEntity<List<Enquiry>> getByEnquiryLoanStatus(String loanStatus) {
		
		List<Enquiry> enq=er.findAllByLoanStatus(loanStatus);
		if(!(enq.isEmpty()))
		{	
			return new ResponseEntity<>(enq,HttpStatus.OK);
		}
		
		else 
			{
				throw new InvalidLoanStatusException(" loanstatus Not Found ");
			}	         
	}
	@Override
	public ResponseEntity<List<Enquiry>> getByEnquiryenquiryStatus(String enquiryStatus) {
		List <Enquiry> enq= er.findAllByEnquiryStatus(enquiryStatus);
		if(!(enq.isEmpty()))
		{
			return new ResponseEntity<>(enq ,HttpStatus.OK);
		}
		else
		{
			throw new InvalidEnquiryStatusException(" Enquiry Status Not Found ");
		}
	}
	@Override
	public List<Enquiry> getallenquiries() {
		List<Enquiry> l = er.findAll();
		return l;
		
	}
	@Override
	public List<Enquiry> getByEnquiryFirstname() 
	{
		List<Enquiry> l=er.findAll();
		return l;
	}

}
