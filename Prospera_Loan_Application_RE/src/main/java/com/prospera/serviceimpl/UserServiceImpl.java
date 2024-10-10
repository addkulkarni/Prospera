package com.prospera.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prospera.model.User;
import com.prospera.repository.UserRepository;
import com.prospera.servicei.UserServiceI;

@Service
public class UserServiceImpl implements UserServiceI
{
	@Autowired
	UserRepository ur;
	@Override
	public void setUser(User u)
	{
		ur.save(u);
	}

}
