package com.prospera.serviceimpl;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.prospera.exception.InvalidCustomerException;
import com.prospera.exception.InvalidEnquiryIDException;
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
	private JavaMailSender sender;
	
	@Override
	public void saveData(Enquiry e, Customer c)
	{
		String string = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		Random r = new Random();
		String username = "";
		String password = "";
		for(int i=1; i<=6; i++)
		{
			int random = r.nextInt(string.length());
			username = username + string.charAt(random);
		}
		for(int i=1; i<=6; i++)
		{
			int random = r.nextInt(string.length());
			password = password + string.charAt(random);
		}
		c.setUsername(username);
		c.setPassword(password);
		c.setAdharcardNo(e.getAdharcardNo());
		c.setAge(e.getAge());
		c.setEmail(e.getEmail());
		c.setFirstName(e.getFirstName());
		c.setGender(e.getGender());
		c.setLastName(e.getLastName());
		c.setMobileNo(e.getMobileNo());
		c.setPancardNo(e.getPancardNo());
		
		cr.save(c);
		  try
		  {
		 SimpleMailMessage message=new SimpleMailMessage(); 
		 message.setTo(c.getEmail());
		 message.setSubject("Loan Application Update");
		 message.setText("Hello "+c.getFirstName()+",\nYour Registration has been completed successfully.\nThank for showing interest and registering at Prospera.\nBelow are your username and password for logging into your loan portal and getting updates.\nUsername: "+username+"\nPassword: "+password+"\nHave a wonderful day.\nTeam Prospera Finance");
		 sender.send(message);
		  }
		  catch(MailSendException exception)
		  {
			     exception.getMessage(); 
		  }
	}

	@Override
	public ResponseEntity<List<Customer>> getAllRegistrationComplete()
	{
		List<Customer> l= cr.findAllByEnquiryEnquiryStatus("Registration Completed"); 
		ResponseEntity<List<Customer>> response=new ResponseEntity<>(l,HttpStatus.OK);
		return response;
	}

	@Override
	public ResponseEntity<String> forwardforverification(int cid)
	{
		Optional<Customer> o= cr.findById(cid);
		if(!(o.isPresent()))
		 {
			throw new InvalidCustomerException("Invalid Customer");
		 }
	     else if(o.get().getEnquiry().getEnquiryStatus().equals("Registration Completed") && o.get().getEnquiry().getLoanStatus().equals("Cibil Approved"))
	     {
    		o.get().getEnquiry().setEnquiryStatus("Pending Verification");
    		cr.save(o.get());
    		ResponseEntity<String> response=new ResponseEntity<String>(" Enquiry forwarded to OE for document verification ",HttpStatus.OK);
    		  try
    		  {
    			  SimpleMailMessage message=new SimpleMailMessage();
    			  message.setTo(o.get().getEmail());
    			  message.setSubject("Loan Application Update");
    			  message.setText("Hello "+o.get().getFirstName()+",\nYour documents have been forwarded to Operational Executive for verification.\nWe will keep you posted on further updates.\nHave a nice day.\nTeam Prospera Finance");
    			  sender.send(message);
    		  }
    		  catch(MailSendException Exception)
    		  {
    			 System.out.println("Mail is incorrect");
    		  }
    		  return response;
    	}  
    	  else
    	  {
    		  throw new InvalidEnquiryIDException("Invalid Enquiry");
    	  }
  
		
    	  
	}

	
}
