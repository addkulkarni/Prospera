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
	public void saveUser( User u) {
		ur.save(u);
		
	}


	@Override
	public User getByUsernameAndPassword(String username, String password) {
		User us=ur.findByUsernameAndPassword(username,password);
		        
		if(us==null)
		{
			throw new InvalidUserException("incorrect username or password");
		}
 		    return us;
		}


	@Override
	public List<User> getAllUsers() {
		
		return ur.findAll();
	}
	}


	
		
	
	   
		
	


