package com.prospera.servicei;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.prospera.model.Customer;
import com.prospera.model.User;


public interface UserServiceI {

	void  saveUser(User u);

	User getByUsernameAndPassword(String username, String password);

	List<User> getAllUsers();



	

}


