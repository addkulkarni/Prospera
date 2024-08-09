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
import com.prospera.model.Customer;
import com.prospera.model.Enquiry;
import com.prospera.repository.CibilRepository;
import com.prospera.repository.CustomerRepository;
import com.prospera.repository.EnquiryRepository;
import com.prospera.servicei.EnquiryServiceI;

@Service
public class EnquiryServiceImpl implements EnquiryServiceI
{
	@Autowired
	EnquiryRepository er;
	
//	@Autowired
	//CibilRepository cr;
	
	@Autowired
	CustomerRepository cmr;
	
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
				en.setEnquiryStatus("Cibil Check Cleared");
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
				    message.setSubject("Congratulations " + en.getFirstName());
				    message.setText("Your cibil score is approved-" +c.getCibilscore()+"\n"+"Required documents for Further Process are following - \n"+" 1. Adharcard \n"+ "2. PanCard \n"+"3. 2 Photo copy \n"+"4. Sign \n"+"5.Income Certificate \\n\"+ \"6. Salary Slip");
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
	public ResponseEntity<String> forwardToCm(int cid) {
		Optional<Customer> o = cmr.findById(cid);
		
		if(o.get().getEnquiry().getEnquiryStatus().equals("Verification Completed")&& o.get().getEnquiry().getLoanStatus().equals("Verification Completed"))
		{
			o.get().getEnquiry().setEnquiryStatus("Pending Sanction");
			cmr.save(o.get());
			ResponseEntity<String> response = new ResponseEntity<String>("Enquiry forwarded to CM", HttpStatus.OK);
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
	public ResponseEntity<List<Customer>> getAllVerificationPending() 
	{
//		cmr.fin
//    	List<Customer> c = cmr.findByEnquiryStatus("hhh");
	     ResponseEntity<List<Customer>> response=new ResponseEntity<>(HttpStatus.OK);
		 return response;
	}

	@Override
	public ResponseEntity<String> getVerification(int enquiryID, String loanStatus) {
		// TODO Auto-generated method stub
		return null;
	}
//	@Override
//	public ResponseEntity<String> getVerification(int cid, String loanStatus) 
//	{
//		Optional<Customer> o = cmr.findById(cid);
//		if(!(o.isPresent()))
//		{
//			throw new InvalidIdException("Enquiry not present in database"); 
//		}
//		else
//		{
//			if(!(o.get().getDoc()==null) && o.get().getEnquiry().getLoanStatus().equals("Cibil Approved"))
//			{
//				o.get().getEnquiry().setEnquiryStatus("Doc Accepted");
//			}
//			else
//			{
//				o.get().getEnquiry().setEnquiryStatus("Doc Rejected");
//			}
//		}
//		cmr.save(o.get());
//		ResponseEntity<String> response = new ResponseEntity<String>("Document Verified", HttpStatus.OK);
//		try
//		{
//			SimpleMailMessage message=new SimpleMailMessage();
//			if(o.get().getEnquiry().getEnquiryStatus().equals("Doc Accepted"))
//			{
//				message.setTo(o.get().getEmail());
//				message.setSubject("Congratulations " + o.get().getFirstName());
//				message.setText("Your Document Verification Accepted");
//			}
//			else
//			{
//				message.setTo(o.get().getEmail());
//				message.setSubject("Sorry " + o.get().getFirstName());
//				message.setText("Your Document Verification Rejected");
//			}
//		    sender.send(message);
//		}
//		catch(MailException exception)
//		{
//			System.out.println("email is incorrect");
//		}
//		return response;
//		
//	}

	
}

