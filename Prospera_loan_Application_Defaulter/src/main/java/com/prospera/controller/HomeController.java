package com.prospera.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.prospera.model.Customer;
import com.prospera.servicei.CustomerServiceI;

@RestController
@RequestMapping("df")
public class HomeController 
{
  @Autowired
  CustomerServiceI csi;
  
  @Autowired
  RestTemplate restT;
  
  @GetMapping("getalldefaulter")
	public ResponseEntity<List<Customer>> getAllDefaulter()
	{
		List<Customer> l = csi.getAllDefaulter();
		ResponseEntity<List<Customer>> response = new ResponseEntity<List<Customer>>(l,HttpStatus.OK);
		return response;
	}
  
//  @GetMapping("/getByEnquiryStatus/{enquiryStatus}")
//  public List getByEnquiryStatus(@PathVariable("enquiryStatus") String enquiryStatus)
//  {
//	  String url="http://localhost:9097/getByEnquiryEnquiryStatus/"+enquiryStatus+"";
//	  List al=restT.getForObject(url, List.class);
//	  return al;
//  }
}
