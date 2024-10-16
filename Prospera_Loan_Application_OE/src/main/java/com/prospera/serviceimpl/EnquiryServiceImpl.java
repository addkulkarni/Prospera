package com.prospera.serviceimpl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.prospera.exception.CibilScoreRejectedException;
import com.prospera.exception.InvalidIdException;
import com.prospera.model.Cibil;
import com.prospera.model.Customer;
import com.prospera.model.Enquiry;
import com.prospera.repository.CustomerRepository;
import com.prospera.repository.EnquiryRepository;
import com.prospera.servicei.EnquiryServiceI;

@Service
public class EnquiryServiceImpl implements EnquiryServiceI
{
	@Autowired
	EnquiryRepository er;
	
	@Autowired
	CustomerRepository cr;

	
	@Autowired
	private JavaMailSender sender;
	
	@Override
	public ResponseEntity<String> calculateCibil(int enquiryID)
	{  
		Enquiry en =er.findByEnquiryIDAndEnquiryStatus(enquiryID, "Forwarded to OE");
		if(en==null)
		{
			throw new InvalidIdException("invalid enquiry");
		}
		else
		{
			Random random=new Random();
			int max=900,min=651;
			int randomnum=random.nextInt(min,max);
			 Cibil c=new Cibil();
			c.setCibilscore(randomnum);
			c.setTimeStamp(new Date());
			if(randomnum>650)
			{
				c.setCibilStatus("Approved");
				en.setLoanStatus("Cibil Approved");
				en.setEnquiryStatus("Pending Registration");
			}
			else
			{
				c.setCibilStatus("Rejected");
				en.setLoanStatus("Cibil Rejected");
				en.setEnquiryStatus("Eligibility Rejected");
			}	
			en.setCibil(c);
			er.save(en);
			ResponseEntity<String> response=new ResponseEntity<String>("Your Cibil Score is "+randomnum,HttpStatus.OK);
			try
			{
			    SimpleMailMessage message=new SimpleMailMessage();
			    if(c.getCibilStatus().equals("Approved"))
			    {
				    message.setTo(en.getEmail());
				    message.setSubject("CIBIL Score Status Update");
				    message.setText("Your CIBIL score is approved and we are pleased to go ahead with your application.\nBelow is the list of required documents for further registration process\n1. Aadhar card \n2. PAN Card \n3. Passport size photo\n4. Signature\n5. Income Certificate\n6. Salary Slip\n"
				    		+ "\nPlease contact the relationship executive for smooth registration process.\nTeam Prospera Finance");
			    }
			    else
			    {
				    message.setTo(en.getEmail());
				    message.setSubject("CIBIL Score Status Update");
				    message.setText("Hello "+en.getFirstName()+",\nYour CIBIL score has been rejected.\nHowever we would like to have you as our customer in the future. Please get in touch with the relationship manager in the future so that we can discuss your eligibility for loan once again.\nTeam Propera Finance.");
			    }
				sender.send(message);
			}
			catch(MailException exception)
			{
				System.out.println("email is incorrect");
			}
			return response;
		}
	}

	@Override
	public ResponseEntity<List<Enquiry>> getForwaredToOEEnquiries()
	{
		List<Enquiry> l = er.findAllByEnquiryStatus("Forwarded to OE");
		ResponseEntity<List<Enquiry>> response = new ResponseEntity<>(l,HttpStatus.OK);
		return response;
    }

	@Override
	public ResponseEntity<List<Enquiry>> getAllCibilApproved() 
	{
	     List<Enquiry> l= er.findAllByEnquiryStatus("Cibil Check Cleared");
	     ResponseEntity<List<Enquiry>> response=new ResponseEntity<>(l,HttpStatus.OK);
		return response;
	}

	@Override
	public ResponseEntity<String> forwardToRE(int enquiryID)
	{		
		Optional<Enquiry> o = er.findById(enquiryID);
		if(o.get().getLoanStatus().equals("Cibil Approved")&& o.get().getEnquiryStatus().equals("Cibil Check Cleared"))
		{
			o.get().setEnquiryStatus("Pending Registration");
			er.save(o.get());
			ResponseEntity<String> response = new ResponseEntity<String>("Enquiry forwarded to RE", HttpStatus.OK);
			return response;
		}
		else if(o.get().getLoanStatus().equals("Cibil Rejected"))
		{
			throw new CibilScoreRejectedException("Cibil score has been rejected");
		}
		else if(o.get().getEnquiryStatus().equals("Registration Completed"))
		{
			throw new InvalidIdException("Registration already completed");
		}
		else 
		{
				throw new InvalidIdException("Invalid Enquiry");
		}
	}

	@Override
	public List<Enquiry> getOeTasks()
	{
		List<Enquiry> enquiries = er.findByEnquiryStatusIn(Arrays.asList("Forwarded to OE", "Pending Verification"));

		return enquiries;
	}

	
}

