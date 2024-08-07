package com.prospera.serviceimpl;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prospera.model.Customer;
import com.prospera.model.Enquiry;
import com.prospera.repository.CustomerRepository;
import com.prospera.servicei.CustomerServiceI;

@Service
public class CustomerServiceImpl implements CustomerServiceI
{

	@Autowired
	CustomerRepository cr;
	
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
	}

}
