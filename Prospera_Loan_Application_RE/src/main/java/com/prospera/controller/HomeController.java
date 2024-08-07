package com.prospera.controller;

import javax.print.attribute.standard.Media;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prospera.exception.InvalidEnquiryIDException;
import com.prospera.model.Customer;
import com.prospera.model.Document;
import com.prospera.model.Enquiry;
import com.prospera.servicei.CustomerServiceI;
import com.prospera.servicei.EnquiryServiceI;




@RestController
public class HomeController
{
	@Autowired
	CustomerServiceI csi;
	
	@Autowired
	EnquiryServiceI esi;
	
	@PostMapping(value = "savedata", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<String> savedata(@RequestPart("data") String data, @RequestPart("adhar")MultipartFile adhar, @RequestPart("pan")MultipartFile pan, @RequestPart("photo")MultipartFile photo, @RequestPart("sign")MultipartFile sign, @RequestPart("incomeCertificate")MultipartFile incomeCertificate, @RequestPart("salarySlip")MultipartFile salarySlip) throws Exception
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
		int eid = c.getEnquiry().getEnquiryID();
		Enquiry e = esi.getEnquiry(eid);
		
		if(!(e.getEnquiryStatus().equals("Pending Registration")))
		{
			throw new InvalidEnquiryIDException("Registration for this enquiry ID is not allowed as its cibil score is either not generated or rejected");
		}
		else
		{
			c.setDoc(d);
			c.setEnquiry(e);
			csi.saveData(e,c);
			esi.setEnquiryStatus(eid);
			ResponseEntity<String> response = new ResponseEntity<>("Data saved succcesfully", HttpStatus.OK);
			return response;
		}
	}
}	
