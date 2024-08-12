package com.prospera.servicei;

import java.util.List;

import com.prospera.model.Customer;
import com.prospera.model.Sanction;

public interface CustomerServiceI
{

	List<Customer> getAllPendingSanction();

	float calculateEMI(Customer c);

	Customer getById(int cid);

	Sanction setLoanDetails(Customer c, Sanction s);

	Customer getCustomerforEMI(int cid);

	Customer getSanctionRejected(int cid);

	void reopenSanctionProcess(Customer c);

	Customer getForRejectSanction(int cid);

	void sanction(Customer c, String enquiryStatus);

	byte[] generateSanctionLetter(int cid);

	void emailSanctionLetter(int cid) throws Exception;

	void forwardToAH(int cid);

}
