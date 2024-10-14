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
		String returnMessage;
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
				returnMessage = "Verification has been rejected";
				msg.setText("Hello "+c.getFirstName()+",\nYour loan document verification has been rejected.\nPlease contact the Relationship Manager and submit the correct documents as soon as possible for smooth application process.\nHave a nice day.\nTeam Propera Finance");
				msg.setTo(c.getEmail());
				msg.setSubject("Loan Document Verification Status");
				sender.send(msg);
			}
			else if(loanStatus.equals("approved"))
			{
				e.setLoanStatus("Verification Approved");
				e.setEnquiryStatus("Pending Sanction");
				returnMessage = "Verification has been approved";
				msg.setText("Hello "+c.getFirstName()+",\nYour loan document verification has been approved.\nPlease contact the Relationship Manager for further course of action and a smooth experience.\nHave a nice day.\nTeam Propera Finance");
				msg.setTo(c.getEmail());
				msg.setSubject("Loan Document Verification Status");
				sender.send(msg);
			}
			else{
				returnMessage="Input status must be either 'approved' or 'rejected' only";
			}
			c.setEnquiry(e);
			cr.save(c);
		}
		ResponseEntity<String> response = new ResponseEntity<String>(returnMessage,HttpStatus.OK);
		return response;
	}

	@Override
		public ResponseEntity<String> forwardToCm(int cid)
		{
			Optional<Customer> o = cr.findById(cid);
			if(!(o.isPresent()))
			{
				throw new InvalidIdException("Invalid Customer");
			}
			else if(o.get().getEnquiry().getEnquiryStatus().equals("Verification Approved")&& o.get().getEnquiry().getLoanStatus().equals("Verification Approved"))
			{
				o.get().getEnquiry().setEnquiryStatus("Pending Sanction");
				cr.save(o.get());
				ResponseEntity<String> response = new ResponseEntity<String>("Enquiry forwarded to CM", HttpStatus.OK);
				SimpleMailMessage msg = new SimpleMailMessage();
				msg.setTo(o.get().getEmail());
				msg.setSubject("Loan Application Update");
				msg.setText("Hello "+o.get().getFirstName()+",\nYour applicaion has been forwared to Credit Manager for further checks and sanctioning of loan.\nYou will shortly receive the sanction letter with all the loan details. Please go through the letter carefully\nWe request you to contact the Relationship Executive soon along with the signed sanction letter so that we can go ahead with your application smoothly.\n\nThank you.\nTeam Prospera Finance");
				return response;
			}
			else if(o.get().getEnquiry().getEnquiryStatus().equals("Verification Completed"))
			{
				throw new InvalidIdException("Verification already completed");
			}
			else 
			{
				throw new InvalidIdException("Invalid Enquiry");
					
			}			
		}

	@Override
	public Customer getCustomerDetails(int enquiryId)
	{
		Customer c = cr.getCustomerByEnquiryEnquiryID(enquiryId);
		return c;
	}
		
	}

