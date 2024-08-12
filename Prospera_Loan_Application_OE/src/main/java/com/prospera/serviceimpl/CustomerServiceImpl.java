package com.prospera.serviceimpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.prospera.exception.InvalidIdException;
import com.prospera.model.Customer;
import com.prospera.model.Enquiry;
import com.prospera.repository.CustomerRepository;
import com.prospera.servicei.CustomerServiceI;

@Service
public class CustomerServiceImpl implements CustomerServiceI
{
	@Autowired
	CustomerRepository cr;
	
	@Autowired
	JavaMailSender sender;
	
	@Override
	public List<Customer> getAllVerificationPending()
	{
		List<Customer> l = cr.findAllByEnquiryEnquiryStatus("Pending Verification");
		return l;
	}

	@Override
	public ResponseEntity<String> getVerification(int cid, String loanStatus)
	{
		Optional<Customer> o = cr.findById(cid);
		if(!(o.isPresent()))
		{
			throw new InvalidIdException("Invalid customer");
		}
		else
		{
			SimpleMailMessage msg = new SimpleMailMessage();
			Customer c = o.get();
			Enquiry e = c.getEnquiry();
			if(loanStatus.equals("rejected"))
			{
				e.setLoanStatus("Verification Rejected");
				e.setEnquiryStatus("Verification Rejected");
				msg.setText("Hello "+c.getFirstName()+",\nYour loan document verification has been rejected.\nPlease contact the Relationship Manager and submit the correct documents as soon as possible for smooth application process.\nHave a nice day.\nTeam Propera Finance");
			}
			else if(loanStatus.equals("approved"))
			{
				e.setLoanStatus("Verification Approved");
				e.setEnquiryStatus("Verification Approved");
				msg.setText("Hello "+c.getFirstName()+",\nYour loan document verification has been approved.\nPlease contact the Relationship Manager for further course of action and a smooth experience.\nHave a nice day.\nTeam Propera Finance");
			}	
			c.setEnquiry(e);
			cr.save(c);
			
			msg.setTo(c.getEmail());
			msg.setSubject("Loan Document Verification Status");
			
			sender.send(msg);
			
		}
		ResponseEntity<String> response = new ResponseEntity<String>("Verification has been "+loanStatus,HttpStatus.OK);
		return response;
	}


}
