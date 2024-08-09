           package com.prospera.serviceimpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import com.prospera.exception.InvalidEnquiryIDException;
import com.prospera.model.Customer;
import com.prospera.model.Enquiry;
import com.prospera.repository.EnquiryRepository;
import com.prospera.servicei.EnquiryServiceI;

@Service
public class EnquiryServiceImpl implements EnquiryServiceI
{
	@Autowired
	EnquiryRepository er;
	
	@Autowired
	 private JavaMailSender sender;
	
	@Override
	public Optional<Enquiry> getEnquiry(int eid)
	{
		Optional<Enquiry> e = er.findById(eid);
		return e;
	}

	@Override
	public void setEnquiryStatus(int eid)
	{
		Enquiry e = er.findById(eid).get();
		e.setEnquiryStatus("Registration Completed");
		er.save(e);
	}

	@Override
	public ResponseEntity<List<Enquiry>> getAllPendingRegistration() {
		List<Enquiry> l= er.findByEnquiryStatus("Pending Registration"); 
		ResponseEntity<List<Enquiry>> response=new ResponseEntity<>(l,HttpStatus.OK);
		return response;
	}

}
