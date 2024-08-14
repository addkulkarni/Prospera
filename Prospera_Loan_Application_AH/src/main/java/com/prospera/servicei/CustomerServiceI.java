package com.prospera.servicei;

import java.util.List;

import com.prospera.model.Customer;
import com.prospera.model.Ledger;

import jakarta.mail.MessagingException;

public interface CustomerServiceI
{

	List<Customer> getAllForwaredtoAH();

	float disburseLoanAmount(int cid) throws MessagingException;

	void createLedger(int cid);

	String updateLedgerList(int cid, Ledger ledger) throws Exception;

	List<Ledger> getLedgerList(int cid);

	void updateLedgerList(int cid, List<Ledger> updatedLedgerList);

	

	byte[] generateDisbursementletter(int cid) throws MessagingException;

	Customer getCustomer(int cid);


}

