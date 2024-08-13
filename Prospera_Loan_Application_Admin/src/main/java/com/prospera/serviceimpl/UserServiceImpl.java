package com.prospera.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.prospera.exception.EmailNotFoundException;
import com.prospera.exception.InvalidLoanStatusException;
import com.prospera.exception.InvalidUserException;
import com.prospera.model.Customer;
import com.prospera.model.User;
import com.prospera.repository.CustomerRepository;
import com.prospera.repository.UserRepository;
import com.prospera.servicei.UserServiceI;

	
@Service
public class UserServiceImpl implements UserServiceI{
	@Autowired
	UserRepository ur;
	
	
	
	
	
	@Override
	public ResponseEntity<String> saveUser( User u) {
		ur.save(u);
		ResponseEntity<String> response= new ResponseEntity<String>("User added",HttpStatus.OK);
		return response;
	}


	@Override
	public User getByUsernameAndPassword(String username, String password) {
		User us=ur.findByUsernameAndPassword(username,password);
//		        List<Customer> cust=cr.findAll();
		        
		if(us==null)
		{
			throw new InvalidUserException("incorrect username or password");
		}
 		    return us;
		}


	
	}


	
		
	
	   
		
	


