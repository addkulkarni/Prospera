package com.prospera.serviceimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.prospera.exception.CustomerLoginFailedException;
import com.prospera.model.Bank;
import com.prospera.model.Customer;
import com.prospera.model.Document;
import com.prospera.model.Employment;
import com.prospera.model.LocalAddress;
import com.prospera.model.PerAddress;
import com.prospera.repository.CustomerRepository;
import com.prospera.servicei.CustomerServiceI;

@Service
public class CustomerServiceImpl implements CustomerServiceI
{
	@Autowired
	CustomerRepository cr;
	
	@Autowired
	JavaMailSender sender;
	
	String s= "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	Random r = new Random();
	String new_password="";
	
	@Override
	public Customer checkCredentials(String username, String password)
	{
		Customer c = cr.findByUsernameAndPassword(username, password);
		if(c==null)
		{
			throw new CustomerLoginFailedException("Wrong username or password");
		}
		else
		{
			return c;
		}
	}

	@Override
	public void updateData(String username, Customer c)
	{
		Optional<Customer> o = cr.findByUsername(username);
		if(!(o.isPresent()))
		{
			throw new CustomerLoginFailedException("Invalid username");
		}
		else
		{
			int cid = o.get().getCid();
		}
		cr.save(c);
	}

	@Override
	public void changePassword(String username, String password)
	{
		Optional<Customer> o = cr.findByUsername(username);
		Customer c = o.get();
		c.setPassword(password);
		cr.save(c);
	}

	@Override
	public Optional<Customer> getCustomer(String username)
	{
		return cr.findByUsername(username);
	}

	@Override
	public ResponseEntity<String> forgotPassword(String username)
	{
		for(int i=1; i<=6; i++)
		{
			new_password = new_password+s.charAt(r.nextInt(s.length()));
		}
		
		Optional<Customer> o  = cr.findByUsername(username);
		if(!(o.isPresent()))
		{
			throw new CustomerLoginFailedException("Incorrect username");
		}
		else
		{
			Customer c = o.get();
			c.setPassword(new_password);
			cr.save(c);
			SimpleMailMessage msg = new SimpleMailMessage();
			msg.setTo(c.getEmail());
			msg.setSubject("Reset Password");
			msg.setText("Your password has been reset.\nYour current new password is:\n"+new_password+"\nPlease change this temporary password as soon as possible for security purpose.");
			sender.send(msg);	
		}
		ResponseEntity<String> response = new ResponseEntity<>("Password changed successfully",HttpStatus.OK);
		return response;
	}

	@Override
	public ResponseEntity<Bank> getBankDetails(String username, String password)
	{
		Customer c = cr.findByUsernameAndPassword(username, password);
		if(c==null)
		{
			throw new CustomerLoginFailedException("Wrong username or password");
		}
		else
		{
			Bank b = c.getBank();
			ResponseEntity<Bank> response = new ResponseEntity<>(b,HttpStatus.OK);
			return response;
		}
	}

	@Override
	public ResponseEntity<Employment> getEmploymentDetails(String username, String password)
	{
		Customer c = cr.findByUsernameAndPassword(username, password);
		if(c==null)
		{
			throw new CustomerLoginFailedException("Wrong username or password");
		}
		else
		{
			Employment e = c.getEmp();
			ResponseEntity<Employment> response = new ResponseEntity<>(e,HttpStatus.OK);
			return response;
		}
	}

	@Override
	public ResponseEntity<List<Object>> getAddressDetails(String username, String password)
	{
		List<Object> l = new ArrayList<>();
		Customer c = cr.findByUsernameAndPassword(username, password);
		if(c==null)
		{
			throw new CustomerLoginFailedException("Wrong username or password");
		}
		else
		{
			LocalAddress ladr = c.getLadr();
			PerAddress padr = c.getPadr();
			l.add(ladr);
			l.add(padr);
			ResponseEntity<List<Object>> response = new ResponseEntity<>(l,HttpStatus.OK);
			return response;
		}
	}

	@Override
	public ResponseEntity<Document> getDocumentDetails(String username, String password)
	{
		Customer c = cr.findByUsernameAndPassword(username, password);
		if(c==null)
		{
			throw new CustomerLoginFailedException("Wrong username or password");
		}
		else
		{
			Document d = c.getDoc();
			ResponseEntity<Document> response = new ResponseEntity<>(d,HttpStatus.OK);
			return response;
		}
	}

	@Override
	public ResponseEntity<Customer> getPersonalDetails(String username, String password)
	{
		Customer c = cr.findByUsernameAndPassword(username, password);
		if(c==null)
		{
			throw new CustomerLoginFailedException("Wrong username or password");
		}
		else
		{
			ResponseEntity<Customer> response = new ResponseEntity<>(c,HttpStatus.OK);
			return response;
		}
	}

	@Override
	public void saveData(Customer c)
	{
		cr.save(c);
	}
}