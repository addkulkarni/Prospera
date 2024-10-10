package com.prospera.controller;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prospera.model.Cibil;
import com.prospera.model.Enquiry;
import com.prospera.servicei.EnquiryServiceI;

import jakarta.validation.Valid;

@CrossOrigin("*")
@RestController
@RequestMapping("/enquiry")
public class EnquiryController {
	
	@Autowired
	EnquiryServiceI esi;
	
	@PostMapping("addenquiry")
	public ResponseEntity<String> addEnquiry(@Valid @RequestBody Enquiry e)
	{
		ResponseEntity<String> response = esi.addEnquiry(e);
		return response;
	}
	
	@GetMapping("getbyid/{enquiryID}")
	public ResponseEntity<Enquiry> getById(@PathVariable("enquiryID")int enquiryID)
	{
		ResponseEntity<Enquiry> response=esi.getById(enquiryID);
		return response;
	}
	
	@GetMapping("getbyemail/{email}")
	public ResponseEntity<List<Enquiry>> getByEmail(@PathVariable("email")String email)
	{
		ResponseEntity<List<Enquiry>> response=esi.getByEmail(email);
		return response;
	}
	
	@GetMapping("/getallenquiry")
	public ResponseEntity<List<Enquiry>> getallenquiry ()
	{
		List<Enquiry> l = esi.getallenquiry();
		//List<Enquiry> sortedlistEnquiries = l.stream().sorted(Comparator.comparing(Enquiry::getFirstName).reversed()).collect(Collectors.toList());
		return new ResponseEntity<>(l, HttpStatus.OK);
	}
	
	@GetMapping("/sortByLoanAmount")
	public ResponseEntity<List<Enquiry>> sortByLoanAmount ()
	{
		List<Enquiry> l = esi.getallenquiry();
		List<Enquiry> sortedlistEnquiries = l.stream().sorted(Comparator.comparing(Enquiry::getLoanamount).reversed()).collect(Collectors.toList());
		return new ResponseEntity<>(sortedlistEnquiries, HttpStatus.OK);
	}
	
	@GetMapping("/sortByDate")
	public ResponseEntity<List<Enquiry>> sortByDate()
	{
		List<Enquiry> l = esi.getallenquiry();
		List<Enquiry> sortedlistEnquiries = l.stream().sorted(Comparator.comparing(Enquiry::getTimeStamp)).collect(Collectors.toList());
		return new ResponseEntity<>(sortedlistEnquiries, HttpStatus.OK);
	}
	
	@GetMapping("/sortByCibil")
	public ResponseEntity<List<Enquiry>> sortByCibil()
	{
		List<Enquiry> l = esi.getallenquiry();
		List<Enquiry> sortedlistEnquiries = l.stream().sorted(Comparator.comparing(Enquiry::getCibil, Comparator.comparing(Cibil::getCibilscore))).collect(Collectors.toList());
		return new ResponseEntity<>(sortedlistEnquiries, HttpStatus.OK);
	}
	
	@GetMapping("/getbyloanstatus/{loanStatus}")
	public ResponseEntity<List<Enquiry>> getByLoanStatus(@PathVariable("loanStatus")String loanStatus)
	{
		List<Enquiry> l = esi.getEnquiryByLoanStatus(loanStatus);
		return new ResponseEntity<>(l,HttpStatus.OK);
	}
	
	@GetMapping("/getbyenquirystatus/{enquiryStatus}")
	public ResponseEntity<List<Enquiry>> getByEnquiryStatus(@PathVariable("enquiryStatus")String enquiryStatus)
	{
		List<Enquiry> l = esi.getEnquiryByEnquiryStatus(enquiryStatus);
		return new ResponseEntity<>(l,HttpStatus.OK);
	}
	
	@PutMapping("updateenquiry/{enquiryID}")
	public ResponseEntity<String> updateEnquiry(@PathVariable("enquiryID")int enquiryID, @RequestBody Enquiry e)
	{
		ResponseEntity<String> response = esi.updateEnquiry(enquiryID, e);
		return response;
	}
	
	@DeleteMapping("deletebyid/{enquiryID}")
	public ResponseEntity<String> deleteEnquiry(@PathVariable("enquiryID")int enquiryID)
	{
		ResponseEntity<String> response = esi.deleteById(enquiryID);
		return response;
	}
	
	@GetMapping("forwardtoOE/{enquiryID}")
	public ResponseEntity<String> forwardToOe(@PathVariable("enquiryID")int enquiryID)
	{
		ResponseEntity<String> response = esi.forwardToOE(enquiryID);
		return response;
	}
}


