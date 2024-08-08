package com.prospera.serviceimpl;

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
import com.prospera.model.Enquiry;
import com.prospera.repository.CibilRepository;
import com.prospera.repository.EnquiryRepository;
import com.prospera.servicei.EnquiryServiceI;

@Service
public class EnquiryServiceImpl implements EnquiryServiceI
{
	@Autowired
	EnquiryRepository er;
	
	@Autowired
	CibilRepository cr;
	
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
			int max=900,min=300;
			int randomnum=random.nextInt(min,max);
			 Cibil c=new Cibil();
			c.setCibilscore(randomnum);
			c.setTimeStamp(new Date());
			if(randomnum>650)
			{
				c.setCibilStatus("Approved");
				en.setLoanStatus("Cibil Approved");
				en.setEnquiryStatus("Cibil check cleared");
			}
			else
			{
				c.setCibilStatus("Rejected");
				en.setLoanStatus("Cibil Rejected");
				en.setEnquiryStatus("Eligibility rejected");
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
				    message.setSubject("Congratulations " + en.getFirstName());
				    message.setText("Your cibil score is approved-" +c.getCibilscore()+"\n"+"Required documents for Further Process are following - \n"+" 1. Adharcard \n"+ "2. PanCard \n"+"3. 2 photo copy \n"+"4. Address Proof \n"+"5. Light bill");
			    }
			    else
			    {
				    message.setTo(en.getEmail());
				    message.setSubject("Sorry " + en.getFirstName());
				    message.setText("Your Cibil Score Rejected");
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
	     List<Enquiry> l= er.findAllByLoanStatus("Cibil Approved");
	     ResponseEntity<List<Enquiry>> response=new ResponseEntity<>(l,HttpStatus.OK);
		return response;
	}

	@Override
	public ResponseEntity<String> forwardToRE(int enquiryID)  {
//		Enquiry e = er.findById(enquiryID).get();
		
		Optional<Enquiry> o = er.findById(enquiryID);
		if(!(o.isPresent()))
		{
			throw new InvalidIdException("Enquiry not present in database"); 
		}
		else 
		{
			if(o.get().getLoanStatus().equals("Pending"))
			{
				throw new InvalidIdException("Cibil score not yet generated please generate cibil score");
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
				
				er.save(o.get());
				o.get().setEnquiryStatus("Pending Registration");
				ResponseEntity<String> response = new ResponseEntity<String>("Enquiry forwarded to RE", HttpStatus.OK);
				return response;
			}			
	        
		}
		
	}
}

