package com.prospera.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prospera.model.Customer;
import com.prospera.model.Sanction;
import com.prospera.servicei.CustomerServiceI;
import com.prospera.servicei.SanctionServiceI;


@RestController
@RequestMapping("cm")
public class HomeController
{
	@Autowired
	CustomerServiceI csi;
	
	@Autowired
	SanctionServiceI ssi;
	
	@GetMapping("getallpendingsanction")
	public ResponseEntity<List<Customer>> getAllPendingSanction()
	{
		List<Customer> l = csi.getAllPendingSanction();
		ResponseEntity<List<Customer>> response = new ResponseEntity<List<Customer>>(l,HttpStatus.OK);
		return response;
	}
	
	@PostMapping("setloandetails/{cid}")
	public ResponseEntity<Sanction> setLoanDetails(@PathVariable("cid")int cid, @RequestBody Sanction s)
	{
		Customer c = csi.getById(cid);
		Sanction sanction = csi.setLoanDetails(c,s);
		ResponseEntity<Sanction> response = new ResponseEntity<Sanction>(sanction,HttpStatus.OK);
		return response;
	}
	
	@PostMapping("calculateEMI/{cid}")
	public ResponseEntity<String> calculateEMI(@PathVariable("cid")int cid)
	{
		Customer c = csi.getCustomerforEMI(cid);
		double emiAmount = csi.calculateEMI(c);
		ResponseEntity<String> response = new ResponseEntity<String>("EMI amount will be "+emiAmount,HttpStatus.OK);
		return response;
	}
	
	@GetMapping("reopensanction/{cid}")
	public ResponseEntity<String> reopensanction(@PathVariable("cid")int cid)
	{
		Customer c = csi.getSanctionRejected(cid);
		csi.reopenSanctionProcess(c);
		ResponseEntity<String> response = new ResponseEntity<String>("Sanction process has been reopened",HttpStatus.OK);
		return response;
	}
	

	@GetMapping("sanction/{cid}/{enquiryStatus}")
	public ResponseEntity<String> rejectsanction(@PathVariable("cid")int cid, @PathVariable("enquiryStatus") String enquiryStatus)
	{
		Customer c = csi.getForRejectSanction(cid);
		csi.sanction(c,enquiryStatus);
		ResponseEntity<String> response = new ResponseEntity<String>("Sanction process has been "+enquiryStatus,HttpStatus.OK);
		return response;
	}
	
	@GetMapping("generatesanctionletter/{cid}")
	public ResponseEntity<byte[]> generateSanctionLetter(@PathVariable("cid")int cid)
	{
		byte[] pdfBytes = csi.generateSanctionLetter(cid);
		HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("Attachment","Invoice.pdf");
		ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(pdfBytes,headers,HttpStatus.OK);
		return response;
	}
	
	@GetMapping("emailsanctionletter/{cid}")
	public ResponseEntity<String> emailSanctionLetter(@PathVariable("cid")int cid) throws Exception
	{
		csi.emailSanctionLetter(cid);
		ResponseEntity<String> response = new ResponseEntity<String>("Email sent succesfully",HttpStatus.OK);
		return response;
	}
}
