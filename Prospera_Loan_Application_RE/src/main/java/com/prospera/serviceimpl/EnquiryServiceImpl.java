package com.prospera.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prospera.model.Enquiry;
import com.prospera.repository.EnquiryRepository;
import com.prospera.servicei.EnquiryServiceI;

@Service
public class EnquiryServiceImpl implements EnquiryServiceI
{
	@Autowired
	EnquiryRepository er;
	
	@Override
	public Enquiry getEnquiry(int eid)
	{
		Enquiry e = er.findById(eid).get();
		return e;
	}

	@Override
	public void setEnquiryStatus(int eid)
	{
		Enquiry e = er.findById(eid).get();
		e.setEnquiryStatus("Registration Completed");
		er.save(e);
	}

	
}
