package com.prospera.serviceimpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prospera.exception.CustomerLoginFailedException;
import com.prospera.model.User;
import com.prospera.repository.UserRepository;
import com.prospera.servicei.UserServiceI;

@Service
public class UserServiceImpl implements UserServiceI
{

	@Autowired
	UserRepository ur;
	
	@Override
	public void changePassword(String username, String password)
	{
		Optional<User> o = ur.findByUsername(username);
		if(!(o.isPresent()))
		{
			throw new CustomerLoginFailedException("Incorrect username");
		}
		else 
		{
			User u = o.get();
			u.setPassword(password);
			ur.save(u);
		}
	}

}
