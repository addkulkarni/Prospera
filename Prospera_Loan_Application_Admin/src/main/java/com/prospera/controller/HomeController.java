package com.prospera.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prospera.model.Customer;
import com.prospera.model.Enquiry;
import com.prospera.model.User;
import com.prospera.servicei.CustomerServiceI;
import com.prospera.servicei.EnquiryServiceI;
import com.prospera.servicei.UserServiceI;

@RestController
@RequestMapping("/admin")
public class HomeController {
	
     @Autowired
     UserServiceI usi;
     
     @Autowired
     CustomerServiceI csi;
     
     @Autowired
     EnquiryServiceI esi;
     
     @PostMapping(value="/saveUser" ,consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
     public ResponseEntity<String> saveUser(@RequestPart("photo") MultipartFile photo,@RequestPart("data") String data)
 	{
    	 ObjectMapper om=new ObjectMapper();
    	try {
    		 User u = om.readValue(data, User.class);
    		 u.setPhoto(photo.getBytes());
    		 usi.saveUser(u);
    		 ResponseEntity<String> response = new ResponseEntity<>("data Saved",HttpStatus.OK);
    	 	 return response;
			 } 
    	 catch (Exception e) 
    	 	{
			 e.printStackTrace();
			 return new ResponseEntity<>("failed...",HttpStatus.EXPECTATION_FAILED);
    	 	}
    	 	
 	}
     
    @GetMapping("/loginByUsernameAndPassword/{username}/{password}")
    public ResponseEntity<User> getByUsernameAndPassword(@PathVariable("username")String username, @PathVariable("password")String password)
	{
    	User u = usi.getByUsernameAndPassword(username, password);
		ResponseEntity<User> response = new ResponseEntity<User>(u,HttpStatus.OK);
		return response;
	}
 	@GetMapping("/getByCustomerEmail/{email}")
	public ResponseEntity<List<Customer>> getByEmail(@PathVariable("email")String email)
	{
		ResponseEntity<List<Customer>> response=csi.getByEmail(email);
		return response;
	}
 	@GetMapping("/getByCustomerLoanStatus/{loanStatus}")
 	public ResponseEntity<List<Customer>> getByLoanStatus(@PathVariable("loanStatus")String loanStatus)
	{
		ResponseEntity<List<Customer>> response=csi.getByLoanStatus(loanStatus);
		return response;
	}
 	@GetMapping("/getByEnquiryLoanStatus/{loanStatus}")
 	public ResponseEntity<List<Enquiry>> getByEnquiryLoanStatus(@PathVariable("loanStatus")String loanStatus)
	{
		ResponseEntity<List<Enquiry>> response=esi.getByEnquiryLoanStatus(loanStatus);
		return response;
	}
 	@GetMapping("/getByEnquiryEnquiryStatus/{enquiryStatus}")
 	public ResponseEntity<List<Enquiry>> getByEnquiryEnquiryStatus(@PathVariable("enquiryStatus")String enquiryStatus)
	{
		ResponseEntity<List<Enquiry>> response=esi.getByEnquiryenquiryStatus(enquiryStatus);
		return response;
	}
 	@GetMapping("/getByCustomerEnquiryStatus/{enquiryStatus}")
 	public ResponseEntity<List<Customer>> getByCustomerEnquiryStatus(@PathVariable("enquiryStatus")String enquiryStatus)
	{
		ResponseEntity<List<Customer>> response=csi.getByCustomerEnquiryStatus(enquiryStatus);
		return response;
	}
 	@GetMapping("/getAllEnquiries")
 	public ResponseEntity<List<Enquiry>> getAllEnquiries()
 	{
 			List<Enquiry> l = esi.getallenquiries();
 			return new ResponseEntity<>(l, HttpStatus.OK);
 	}
 	@GetMapping("/getAllCustomer")
 	public ResponseEntity<List<Customer>> getAllCustomer()
 	{
 			List<Customer> l = csi.getAllCustomer();
 			return new ResponseEntity<>(l, HttpStatus.OK);
 	}
 	@GetMapping("getAllUsers")
 	public ResponseEntity<List<User>> getAllUsers()
 	{
 		List<User> l = usi.getAllUsers();
			return new ResponseEntity<>(l, HttpStatus.OK);
 	}
 	
 	
 	
}
