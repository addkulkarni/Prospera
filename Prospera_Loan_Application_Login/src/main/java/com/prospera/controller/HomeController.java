package com.prospera.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prospera.model.Bank;
import com.prospera.model.Customer;
import com.prospera.model.Document;
import com.prospera.model.Employment;
import com.prospera.model.Enquiry;
import com.prospera.model.Sanction;
import com.prospera.servicei.CustomerServiceI;
import com.prospera.servicei.UserServiceI;
@CrossOrigin("*")
@RestController
@RequestMapping("login")
public class HomeController
{
	@Autowired
	CustomerServiceI csi;
	@Autowired
	UserServiceI usi;
	
	@GetMapping("login/{username}/{password}")
	public ResponseEntity<Customer> login(@PathVariable("username")String username, @PathVariable("password")String password)
	{
		Customer c = csi.checkCredentials(username, password);
		ResponseEntity<Customer> response = new ResponseEntity<Customer>(c,HttpStatus.OK);
		return response;
	}
	
	@GetMapping("changepassword/{username}/{password}")
	public ResponseEntity<String> changePassword(@PathVariable("username")String username, @PathVariable("password")String password)
	{
		csi.changePassword(username,password);
		usi.changePassword(username,password);
		ResponseEntity<String> response = new ResponseEntity<String>("Password changed successfully",HttpStatus.OK);
		return response;
	}
	
	@GetMapping("forgotpassword/{username}")
	public ResponseEntity<String> forgotPassword(@PathVariable("username")String username)
	{		
		
		ResponseEntity<String> response = csi.forgotPassword(username);
		return response;
	}
	
	@GetMapping("getbankdetails/{username}/{password}")
	public ResponseEntity<Bank> getBankDetails(@PathVariable("username")String username, @PathVariable("password")String password)
	{
		ResponseEntity<Bank> response = csi.getBankDetails(username,password);
		return response;
	}
	
	@GetMapping("getemploymentdetails/{username}/{password}")
	public ResponseEntity<Employment> getEmploymentDetails(@PathVariable("username")String username, @PathVariable("password")String password)
	{
		ResponseEntity<Employment> response = csi.getEmploymentDetails(username,password);
		return response;
	}
	
	@GetMapping("getaddressdetails/{username}/{password}")
	public ResponseEntity<List<Object>> getAddressDetails(@PathVariable("username")String username, @PathVariable("password")String password)
	{
		ResponseEntity<List<Object>> response = csi.getAddressDetails(username,password);
		return response;
	}
	
	@GetMapping("getdocumentdetails/{username}/{password}")
	public ResponseEntity<Document> getDocumentDetails(@PathVariable("username")String username, @PathVariable("password")String password)
	{
		ResponseEntity<Document> response = csi.getDocumentDetails(username,password);
		return response;
	}
	
	@GetMapping("getpersonaldetails/{username}/{password}")
	public ResponseEntity<Customer> getPersonalDetails(@PathVariable("username")String username, @PathVariable("password")String password)
	{
		ResponseEntity<Customer> response = csi.getPersonalDetails(username,password);
		return response;
	}
	
	@GetMapping("rejectsanctiondetails/{username}/{password}")
	public ResponseEntity<String> rejectSanctionDetails(@PathVariable("username")String username,@PathVariable("password")String password)
	{
		String msg = csi.rejectSanctionDetails(username,password);
		ResponseEntity<String> response = new ResponseEntity<>(msg,HttpStatus.OK);
		return response;
	}
	
	@GetMapping("approvesanctiondetails/{username}/{password}")
	public ResponseEntity<String> approveSanctionDetails(@PathVariable("username")String username,@PathVariable("password")String password)
	{
		String msg = csi.approveSanctionDetails(username,password);
		ResponseEntity<String> response = new ResponseEntity<>(msg,HttpStatus.OK);
		return response;
	}
	
	@PutMapping(value="updatecustomer/{cid}", consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<String> savedata(@PathVariable("cid") int cid,
	@RequestPart("data") String data, 
	@RequestPart("adhar")MultipartFile adhar, 
	@RequestPart("pan")MultipartFile pan, 
	@RequestPart("photo")MultipartFile photo, 
	@RequestPart("sign")MultipartFile sign, 
	@RequestPart("incomeCertificate")MultipartFile incomeCertificate, 
	@RequestPart("salarySlip")MultipartFile salarySlip) throws Exception
	{
		ObjectMapper om = new ObjectMapper();
		Customer c = om.readValue(data, Customer.class);
		Document d = new Document();
		d.setAdhar(adhar.getBytes());
		d.setPan(pan.getBytes());
		d.setIncomeCertificate(incomeCertificate.getBytes());
		d.setPhoto(photo.getBytes());
		d.setSalarySlip(salarySlip.getBytes());
		d.setSign(sign.getBytes());
		c.setDoc(d);
		Enquiry e = c.getEnquiry();
		c.setEnquiry(e);
		csi.saveData(c);
		ResponseEntity<String> response = new ResponseEntity<>("Data saved succcesfully", HttpStatus.OK);
		return response;
	}
}
