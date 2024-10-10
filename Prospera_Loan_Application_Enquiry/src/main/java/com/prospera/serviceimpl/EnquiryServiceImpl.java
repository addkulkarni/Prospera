package com.prospera.serviceimpl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.prospera.exceptionhandler.CannotForwardToOEException;
import com.prospera.exceptionhandler.EmailNotFoundException;
import com.prospera.exceptionhandler.EnquiryIdNotFoundException;
import com.prospera.exceptionhandler.PanCardAlreadySubmittedException;
import com.prospera.model.Enquiry;
import com.prospera.repository.EnquiryRepository;
import com.prospera.servicei.EnquiryServiceI;

@Service
public class EnquiryServiceImpl implements EnquiryServiceI{

	@Autowired
	EnquiryRepository er;
	
	@Autowired 
	private JavaMailSender sender;
	
	@Override
	public ResponseEntity<String> addEnquiry(Enquiry e)
	{
		e.setLoanStatus("Pending");
		e.setEnquiryStatus("Forwarded to OE");
		e.setTimeStamp(new Date());
		Optional<Enquiry> o1 = er.findByPancardNo(e.getPancardNo());
//		Remove this comment to enable unique email ids in DB
//		List<Enquiry> l = er.findByEmail(e.getEmail());
//		if(!(l.isEmpty()))
//		{
//			throw new EmailAlreadyRegisteredException("Email is already used to register");
//		}
		if(o1.isPresent())
		{
			throw new PanCardAlreadySubmittedException("Pan card has already been used for enquiry");
		}

		er.save(e);
		ResponseEntity<String> response = new ResponseEntity<String>("Enquiry added",HttpStatus.OK);
		try
		{
		  SimpleMailMessage message=new SimpleMailMessage();
		  message.setTo(e.getEmail());
		  message.setSubject("Welcome to Prospera Finance");

		  message.setText("Hello "+ e.getFirstName()+",\nWe are pleased to know about your interest in Prospera Loans. Your Enquiry has been forworded to Operational Executive for CIBIL score check.\nWe will contact you via email as soon as we have your CIBIL score with us.\nWe will keep you posted throughout the loan process.\nHave a nice day.\nTeam Prospera Finance");

		  sender.send(message);
		}
		catch(MailException exception)
		{
			System.out.println("email is incorrect");
		}
		return response;
	}
	
	@Override
	public ResponseEntity<Enquiry> getById(int enquiryID)
	{
        Optional<Enquiry> o = er.findById(enquiryID);
		if(o.isPresent())
		{
			ResponseEntity<Enquiry> response = new ResponseEntity<>(o.get(),HttpStatus.OK);
			return response;
		}
		else
		{
			throw new EnquiryIdNotFoundException("Enquiry not found");
		}
    }

	@Override
	public ResponseEntity<List<Enquiry>> getByEmail(String email)
	{
		List<Enquiry> l  = er.findAllByEmail(email);
		if(!(l.isEmpty()))
		{
			ResponseEntity<List<Enquiry>> response = new ResponseEntity<>(l, HttpStatus.OK);
			return response;
		}
		else
		{
			throw new EmailNotFoundException("No enquiry with the given email found");
		}
	}
	
	@Override
	public List<Enquiry> getEnquiryByLoanStatus(String loanStatus)
	{
		List<Enquiry> l = er.findAllByLoanStatus(loanStatus);
		return l;
	}
	
	@Override
	public List<Enquiry> getEnquiryByEnquiryStatus(String enquiryStatus)
	{
		List<Enquiry> l = er.findAllByEnquiryStatus(enquiryStatus);
		return l;
	}
	
	@Override
	public List<Enquiry> getallenquiry()
	{
		List<Enquiry> l = er.findAll();
		return l;
	}
	
	@Override
	public ResponseEntity<String> updateEnquiry(int enquiryID, Enquiry e)
	{
		er.save(e);
		ResponseEntity<String> response = new ResponseEntity<String>("Update successfull", HttpStatus.OK);
		return response;
	}

	
	@Override
	public ResponseEntity<String> deleteById(int enquiryID)
	{
		Optional<Enquiry> o = er.findById(enquiryID);
		if(o.isPresent())
		{
			er.deleteById(enquiryID);
			ResponseEntity<String> response = new ResponseEntity<String>("Enquiry deleted succesffuly", HttpStatus.OK);
			return response;
		}
		else
		{
			throw new EnquiryIdNotFoundException("Enquiry not found");
		}	
	}
	
	@Override
	public ResponseEntity<String> forwardToOE(int enquiryID)
	{
		Optional<Enquiry> o = er.findByEnquiryIDAndEnquiryStatus(enquiryID, "Initiated");
		if(o.isPresent())
		{
			Enquiry e = o.get();
			e.setEnquiryStatus("Forwarded to OE");
			er.save(e);
			try
			{
				SimpleMailMessage message=new SimpleMailMessage();
				message.setTo(e.getEmail());
				message.setSubject("Loan Enquiry Status Update");
				message.setText("Hello "+ e.getFirstName()+ ",\nYour Enquiry has been forworded to Operational Executive for CIBIL score check.\nWe will contact you via email as soon as we have your CIBIL score with us.\nHave a nice day.\nTeam Prospera Finance");
				sender.send(message);
			}
			catch(MailException exception)
			{
				System.out.println("email is incorrect");
			}
			return new ResponseEntity<String>("Enquiry has been successfully forwared to OE",HttpStatus.OK);
			
		}
		else
		{
			throw new CannotForwardToOEException("Enquiry cannot be forwarded to OE");
		}
	}

}

