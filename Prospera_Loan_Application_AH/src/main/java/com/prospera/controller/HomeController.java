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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prospera.model.Customer;
import com.prospera.model.Ledger;
import com.prospera.servicei.CustomerServiceI;
import com.prospera.servicei.LedgerServiceI;

import jakarta.mail.MessagingException;

@RestController
@RequestMapping("ah")
public class HomeController
{
	@Autowired
	CustomerServiceI csi;
	
	@Autowired
	LedgerServiceI lsi;
	
	@GetMapping("getallsanctionapproved")
	public ResponseEntity<List<Customer>> getAllForwaredtoAH()
	{
		List<Customer> l = csi.getAllForwaredtoAH();
		ResponseEntity<List<Customer>> response = new ResponseEntity<>(l,HttpStatus.OK);
		return response;
	}
	
	@GetMapping("disburseamount/{cid}")
	public ResponseEntity<String> disburseLoanAmount(@PathVariable("cid")int cid) throws MessagingException
	{
		float amount = csi.disburseLoanAmount(cid);
		ResponseEntity<String> response = new ResponseEntity<String>("Amount of Rs. "+amount+" has been disbursed to the bank account",HttpStatus.OK);
		return response;
	}
	
	@PostMapping("createledger/{cid}")
	public ResponseEntity<String> createLedger(@PathVariable("cid")int cid)
	{
		csi.createLedger(cid);
		ResponseEntity<String> response = new ResponseEntity<String>("Ledger has been created",HttpStatus.OK);
		return response;
	}
	
	@GetMapping("payEMI/{cid}/{ledgerId}")
	public ResponseEntity<String> payEMI(@PathVariable("ledgerId")int ledgerId, @PathVariable("cid")int cid) throws Exception
	{
		Ledger ledger = lsi.getLedger(ledgerId);
		String message = csi.updateLedgerList(cid, ledger);
		HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("Attachment","EMI Reciept.pdf");
		if(message.equals("You have failed to pay consecutive three EMIs and hence you've been marked as defaulter"))
		{
			ResponseEntity<String> response = new ResponseEntity<String>(message,HttpStatus.OK);
			return response;
		}
		else
		{
			ResponseEntity<String> response = new ResponseEntity<String>(message,HttpStatus.OK);
			return response;
		}
		
	}
	
	@GetMapping("skipEMI/{cid}/{ledgerId}")
	public ResponseEntity<String> skipEMI(@PathVariable("cid")int cid,@PathVariable("ledgerId")int ledgerId)
	{
		List<Ledger> ledgerlist = csi.getLedgerList(cid);
		List<Ledger> updatedLedgerList = lsi.skipEMI(ledgerId, ledgerlist);
		csi.updateLedgerList(cid,updatedLedgerList);
		ResponseEntity<String> response = new ResponseEntity<String>("Updated successfully",HttpStatus.OK);
		return response;
	}
}
