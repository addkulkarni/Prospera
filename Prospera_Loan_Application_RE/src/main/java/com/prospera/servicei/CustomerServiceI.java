package com.prospera.servicei;

import com.prospera.model.Customer;
import com.prospera.model.Enquiry;

public interface CustomerServiceI
{
	void saveData(Enquiry e, Customer c);
}
