package com.prospera.servicei;

import java.util.List;

import com.prospera.model.Customer;
import com.prospera.model.Ledger;

public interface LedgerServiceI
{
	Ledger getLedger(int ledgerId);

	List<Ledger> skipEMI(int ledgerId, List<Ledger>ledger, Customer c);

	
}
