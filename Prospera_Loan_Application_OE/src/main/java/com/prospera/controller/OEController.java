package com.prospera.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prospera.model.Customer;
import com.prospera.model.Enquiry;
import com.prospera.servicei.EnquiryServiceI;


@RestController
@RequestMapping("/oe")
public class OEController
{
	
	@Autowired
	EnquiryServiceI esi;
	
	@GetMapping("/cibilscore/{enquiryID}")
	public ResponseEntity<String> calculateCibil(@PathVariable("enquiryID")int enquiryID)
	{
		ResponseEntity<String> response=esi.calculateCibil(enquiryID);
		return response;
	}
	
	@GetMapping("getforwardedtoOEenquiries")
	public ResponseEntity<List<Enquiry>> getForwaredToOEEnquiries()
	{
		ResponseEntity<List<Enquiry>> response=esi.getForwaredToOEEnquiries();
		return response;
	}
	
	@GetMapping("/getAllCibilApproved")
	public ResponseEntity<List<Enquiry>> getAllCibilApproved()
	{
		
		ResponseEntity<List<Enquiry>> response = esi.getAllCibilApproved();
		return response;		
	}
	
	@GetMapping("/forwardtore/{enquiryID}")
	public ResponseEntity<String> forwardToRE(@PathVariable("enquiryID") int enquiryID)
	{
		ResponseEntity<String> e = esi.forwardToRE(enquiryID);
		return e;
	}

	
	@GetMapping("/forwardtoCM/{cid}")
	public ResponseEntity<String> forwardToCm(@PathVariable("cid") int cid)
	{
		ResponseEntity<String> e = esi.forwardToCm(cid);
		return e;
	}
	
	@GetMapping("/getforwardedtoVerificationPending")
	public ResponseEntity<List<Customer>> getforwardtoreVerificationPending()
	{
		ResponseEntity<List<Customer>> response = esi.getAllVerificationPending();
		return response;
	}

	@GetMapping("/getDocVerification/{enquiryID}/{loanStatus}")
	ResponseEntity<String> getVerification(@PathVariable("enquiryID") int enquiryID,@PathVariable("loanStatus")String loanStatus)
	{
		ResponseEntity<String> e = esi.getVerification(enquiryID,loanStatus);
		return e;
	}
}
