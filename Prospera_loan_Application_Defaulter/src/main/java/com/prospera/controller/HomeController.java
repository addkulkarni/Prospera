package com.prospera.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prospera.model.Customer;
import com.prospera.servicei.CustomerServiceI;

@RestController
@RequestMapping("df")
public class HomeController 
{
  @Autowired
  CustomerServiceI csi;
  
  @GetMapping("getalldefaulter")
	public ResponseEntity<List<Customer>> getAllDefaulter()
	{
		List<Customer> l = csi.getAllDefaulter();
		ResponseEntity<List<Customer>> response = new ResponseEntity<List<Customer>>(l,HttpStatus.OK);
		return response;
	}
}
